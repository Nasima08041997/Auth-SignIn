package com.auth.android


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.auth.android.authentication.SplashNavigationListener
import java.util.regex.Pattern


/**
 * Created by nasima on 06/10/17.
 */
class BroadcastSms : BroadcastReceiver()
{

        val sms = SmsManager.getDefault()
        lateinit var splashNavigstionListener : SplashNavigationListener



    override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            var otp = Pattern.compile("(|^)\\d{6}")


        try {

                if (bundle != null) {
                    val pdusObj = bundle.get("pdus") as Array<Any>
                    for (i in pdusObj.indices) {
                        val currentMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                        val phoneNumber = currentMessage.displayOriginatingAddress
                      //  var message = currentMessage.displayMessageBody.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                       // message = message.substring(0, message.length - 1)
                      //  splashNavigstionListener.setOTP(message)
                        val extract = currentMessage.messageBody
                        val message = otp.matcher(extract)
                        if (message.find()) {
                            Log.i("SmsReceiver", "senderNum: $phoneNumber; message: ${message.group(0)}")
                            val duration = Toast.LENGTH_LONG
                            val toast = Toast.makeText(context, "senderNum: $phoneNumber, message: ${message.group(0)}", duration)
                            toast.show()

                        }
                        else
                        {
                            Log.i("message---------->","not found")

                        }


                         val myIntent = Intent("otp")
                         myIntent.putExtra("message", message.group(0))
                         LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent)
                        // Show Alert*/

                    } // end for loop
                } // bundle is null

                else
                {
                    Log.i("message---------->","not found")
                }




            } catch (e: Exception) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e)

            }

        }




}

