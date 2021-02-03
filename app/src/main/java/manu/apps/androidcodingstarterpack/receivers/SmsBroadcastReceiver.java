package manu.apps.androidcodingstarterpack.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import manu.apps.androidcodingstarterpack.interfaces.OtpReceivedInterface;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    OtpReceivedInterface otpReceivedInterface = null;

    public void setOnOtpListener(OtpReceivedInterface otpReceivedInterface){
        this.otpReceivedInterface = otpReceivedInterface;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {

                case CommonStatusCodes.SUCCESS:

                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);

                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server

                    String regex = "\\d+";
                    Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                    Matcher matcher = pattern.matcher(message);

                    if (matcher.find()) {

                        new AlertDialog.Builder(context)
                                .setMessage("Your OTP is: " + matcher.group(0))
                                .show();

                        otpReceivedInterface.onOtpReceived(matcher.group(0));

                    }

                    break;

                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    Log.wtf("SmsBroadcastReceiverStatus", "status: failure");

                    if (otpReceivedInterface != null){
                        otpReceivedInterface.onOtpTimeout();
                    }

                    break;


            }
        }
    }
}
