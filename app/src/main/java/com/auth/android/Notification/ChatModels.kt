package com.fitlinks.chat.model


import com.athleteofthemind.ui.messages.model.MessageDialogModel
import com.stfalcon.chatkit.commons.models.IDialog
import com.stfalcon.chatkit.commons.models.IUser
import java.util.*

/**
 * Created by aditlal on 15/08/17.
 */

class ChatModels : IDialog<MessageModel> {
    lateinit var messageDialogModel: MessageDialogModel
    var count: Int = 0
    var message: MessageModel? = null

    fun setChatLastMessage(msg: MessageModel) {
        message = msg
    }


    override fun getId(): String {
        return messageDialogModel.channelId
    }

    override fun getDialogPhoto(): String {
        /*if (AOTMApp.sInstance.isCoach)
            return messageDialogModel.playerImg
        else
            return messageDialogModel.coachImg*/
        return messageDialogModel.playerImg
    }

    override fun getDialogName(): String {
        /*if (AOTMApp.sInstance.isCoach)
            return messageDialogModel.playerName
        else
            return messageDialogModel.coachName*/
        return messageDialogModel.playerImg
    }

    override fun getUsers(): List<IUser> {
        val userList = ArrayList<InvitieResponse>()
        val usr = InvitieResponse()
        if (!messageDialogModel.currentUserCoach) {
            usr.invitie_email = messageDialogModel.coachEmail
            usr.invitie_img = messageDialogModel.coachImg
            usr.invitie_name = messageDialogModel.coachName
            userList.add(usr)
        } else {
            usr.invitie_email = messageDialogModel.playerEmail
            usr.invitie_img = messageDialogModel.playerImg
            usr.invitie_name = messageDialogModel.playerName
            userList.add(usr)
        }
        return userList
    }

    override fun getLastMessage(): MessageModel? {
        if (message != null)
            return message
        else
            return MessageModel()
    }

    override fun setLastMessage(msg: MessageModel) {
        message = msg
    }

    override fun getUnreadCount(): Int {
        return count
    }
}
