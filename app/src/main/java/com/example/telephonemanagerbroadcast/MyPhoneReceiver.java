package com.example.telephonemanagerbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyPhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(context);
                Intent intent1=new Intent("my.result.receiver");
                intent1.putExtra("inComingNumber",phoneNumber);
                localBroadcastManager.sendBroadcast(intent1);
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
    }
}
