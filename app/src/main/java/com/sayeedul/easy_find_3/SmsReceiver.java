package com.sayeedul.easy_find_3;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;


public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;
    ProgressDialog progressDoalog;

    @Override
    public void onReceive(Context context, Intent intent) {

        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Please wait! Operating....");
        progressDoalog.setTitle("Received SMS");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String smsNumber = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();
            String[] smsToken = messageBody.split(" ");
            String smsName = "";
            if(smsToken[0].toLowerCase().contains("abc"))
            {
                smsName = smsToken[1].toLowerCase().trim();
            }
            //Pass on the text to our listener.
            mListener.messageReceived(messageBody,smsName,smsNumber);
            progressDoalog.dismiss();
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
