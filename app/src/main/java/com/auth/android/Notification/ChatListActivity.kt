package com.auth.android.Notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.appsng.firebaseapptoappnotification.models.NotificationModel
import com.auth.android.R
import com.auth.android.R.id.input
import com.auth.android.R.id.scrim
import com.auth.android.authentication.AuthenticationApplication
import com.auth.android.utils.kotlin.hide
import com.auth.android.utils.kotlin.show
import com.fitlinks.chat.GlideImageLoader
import com.fitlinks.chat.model.InvitieResponse
import com.fitlinks.chat.model.MessageModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.twitter.sdk.android.core.models.User
import io.reactivex.internal.util.HalfSerializer.onComplete
import kotlinx.android.synthetic.main.activity_chat_list.*
import java.util.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.FirebaseDatabase



/**
 * Created by nasima on 09/10/17.
 */

class ChatListActivity : AppCompatActivity(), MessageInput.InputListener, MessageInput.AttachmentsListener {


    private val TOTAL_MESSAGES_COUNT = 100
    protected var senderId = ""
    protected var receiverId = ""
    protected var receiverrId = ""

    private var channelId = ""
    private var mAuth: FirebaseAuth? = null

    lateinit var imageLoader: ImageLoader
    lateinit var messagesAdapter: MessagesListAdapter<MessageModel>
    lateinit var chatMessagesList: ArrayList<MessageModel>
    lateinit var otherUser: InvitieResponse
    var preferences = AuthenticationApplication.getPreferences()

    private val PICK_IMAGE_REQUEST = 234
    private val CAMERA_REQUEST_CODE = 235
    private val PERMISSIONS_REQUEST_CODE_CAMERA = 111
    private val PERMISSIONS_REQUEST_CODE_GALLERY = 112
    //a Uri object to store file path
    private var filePath: Uri? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>


    lateinit var menu: Menu
    private val selectionCount: Int = 0
    lateinit var lastLoadedDate: Date
    //var chatDialog: ChatModels? = null

    val user = AuthenticationApplication.getPreferences().getUser()

    companion object {
        fun newIntent(context: Context, channelId: String, otherUser: InvitieResponse): Intent {
            val intent = Intent(context, ChatListActivity::class.java)
            intent.putExtra("channelId", channelId)
            intent.putExtra("otherUser", otherUser)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)
        setupToolbar()
        init()
    }

    private fun init() {
        input.button.setImageDrawable(resources.getDrawable(R.drawable.ic_send))
        preferences.setMessageCount(0)
        mAuth = FirebaseAuth.getInstance()


        //chatDialog = FitLinksApplication.instance.currentDialog!!
        imageLoader = GlideImageLoader()
        chatMessagesList = ArrayList()
        channelId = " "
        //receiverId =  "+918248947745"  //emulatlor sender - jio
                                    //mobile sender - aircel
        //senderId ="+918508439991"


        receiverId =  "+918508439991"  //emulatlor sender - jio
        //mobile sender - aircel
        senderId ="+918248947745"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = receiverId

        bottomSheetBehavior = BottomSheetBehavior.from(cardAttachmentBottomSheet)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    scrim.hide()
                    input.inputEditText.isEnabled = true
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    scrim.show()
                    input.inputEditText.isEnabled = false
                }
            }
        })

        messagesAdapter = MessagesListAdapter(senderId, /* getMessageHolder(),*/ imageLoader)
        messagesList.setAdapter(messagesAdapter)
        messagesAdapter.addToEnd(chatMessagesList, true)

        input.setInputListener(this)
        input.setAttachmentsListener(this)
        fetchMessagesForChannel(channelId)
        messagesAdapter.setLoadMoreListener { page, totalItemsCount ->
            if (totalItemsCount >= 100)
                FirebaseHandler.getMessages().child(channelId).endAt(oldestKeySeen).limitToLast(100).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {
                        p0!!.toException().toString()
                    }

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        if (snapshot!!.exists()) {
                            val list = ArrayList<MessageModel>()
                            for (child in snapshot.children) {
                                val messageModel = child.getValue(MessageModel::class.java)
                                if (messageModel!!.ownerId == senderId)
                                    messageModel.isOutComing = true
                                list.add(messageModel!!)
                                messagesAdapter.addToEnd(list, false)
                            }
                        }
                    }
                })
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        setTitle("Chats")
    }

    private var oldestKeySeen: String = ""
    private fun fetchMessagesForChannel(channelId: String) {
        FirebaseHandler.getMessages().child(channelId).limitToLast(100).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot?, p1: String?) {
                if (oldestKeySeen.isEmpty())
                    oldestKeySeen = snapshot!!.key
                val messageModel = snapshot!!.getValue(MessageModel::class.java)
                if (messageModel!!.ownerId == senderId)
                    messageModel.isOutComing = true
                chatMessagesList.add(messageModel)
                messagesAdapter.addToStart(messageModel, true)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }
        })
    }

    override fun onSubmit(input: CharSequence?): Boolean {

        sendNotification(senderId,receiverId,input.toString(),"Chat Notification","Notifications")


        val msg = MessageModel(msgId = "msg_" + chatMessagesList.size + 1,
                receiverId = receiverId, receiverName = "Invitie", receiverImg = " ",
                ownerImg = "", ownerName = "Nasima", ownerId = senderId,
                message = input.toString(), channelId = channelId, file = "", updatedTimeStamp = Date().time)
        FirebaseHandler.getMessages().child(channelId).push().setValue(msg)
        var user = FirebaseAuth.getInstance().currentUser
        val emailKey = "+918508439991"//(user!!.email)!!.replace("[-+.^:,]".toRegex(), "_dot_")
        val otherUserEmailKey = ""//(otherUser.invitie_email).replace("[-+.^:,]".toRegex(), "_dot_")
        val postValues = HashMap<String, Any>()
        postValues.put("last_chat", input.toString())
        postValues.put("last_user", emailKey)
        postValues.put("updatedTimeStamp", Date().time)
        FirebaseHandler.getMessageDialogs().child(emailKey).child(otherUserEmailKey).updateChildren(postValues)
        FirebaseHandler.getMessageDialogs().child(otherUserEmailKey).child(emailKey).updateChildren(postValues)
        return true
    }

    fun sendNotification(senderId_id: String ,receiver_id: String, message: String, description: String, type: String) {
        val notification = NotificationModel()
      notification.description=description
        notification.message=message
        notification.status=0
        notification.timestamp=Date().time
        notification.type=type
        notification.sender_id=senderId
        notification.receiver_id=receiver_id
        FirebaseHandler.getNotification().child(senderId_id).push().setValue(notification)
        startService(Intent(this, MyFirebaseMessageService::class.java))


        /*val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this,receiver_id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New message from "+receiver_id)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build())*/

    }


    override fun onAddAttachments() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferences.setMessageCount(0)
        //updateMessageCount(0)
        //chatDialog!!.count = 0
        //FitLinksApplication.instance.currentDialog = chatDialog
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                preferences.setMessageCount(0)
                finish()
                return true
            }
            else -> {
                return true
            }
        }
    }
}