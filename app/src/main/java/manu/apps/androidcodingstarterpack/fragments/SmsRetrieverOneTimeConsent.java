package manu.apps.androidcodingstarterpack.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import manu.apps.androidcodingstarterpack.MainActivity;
import manu.apps.androidcodingstarterpack.R;

import static android.app.Activity.RESULT_OK;

public class SmsRetrieverOneTimeConsent extends Fragment {

    TextInputLayout tilOtp;
    TextInputEditText etOtp;

    TextView tvSmsMessage;

    private static final int SMS_CONSENT_REQUEST = 1111;  // Set to an unused request code

    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);

                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                            e.printStackTrace();
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        new AlertDialog.Builder(requireActivity())
                                .setMessage("OTP request time out")
                                .setCancelable(false)
                                .show();
                        break;
                }
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sms_retriever_one_time_consent_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tilOtp = view.findViewById(R.id.til_otp);
        etOtp = view.findViewById(R.id.et_otp);
        tvSmsMessage = view.findViewById(R.id.tv_sms_message);

        // Start listening for SMS User Consent broadcasts from senderPhoneNumber or sender
        // The Task<Void> will be successful if SmsRetriever was able to start
        // SMS User Consent, and will error if there was an error starting.
        // startSmsUserConsent - Enter phone number here if you are using number from contact list to send opt verification
        Task<Void> task = SmsRetriever.getClient(requireActivity()).startSmsUserConsent(null/*senderPhoneNumber or null */);

        task.addOnCompleteListener(listener -> {

            if (listener.isSuccessful()) {

                Log.wtf("SmsRetrieverListenerSuccess", "SmsRetrieverListenerSuccess: " + true);

            } else {

                Exception exception = listener.getException();

                if (exception != null) {
                    exception.printStackTrace();
                }

            }
        });

        startIntentFilter();
    }

    private void startIntentFilter() {

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        requireActivity().registerReceiver(smsVerificationReceiver, intentFilter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SMS_CONSENT_REQUEST) {
            if (resultCode == RESULT_OK) {

                requireActivity().unregisterReceiver(smsVerificationReceiver);

                // Get SMS message content
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

//                new AlertDialog.Builder(requireActivity())
//                        .setMessage(message)
//                        .setCancelable(true)
//                        .show();

                try {

                    new AlertDialog.Builder(requireActivity())
                            .setMessage(parseOneTimeCode(message))
                            .setCancelable(true)
                            .show();

                } catch (Exception e) {

                    new AlertDialog.Builder(requireActivity())
                            .setMessage("OTP not from Doh Yangu")
                            .setCancelable(true)
                            .show();

                    e.printStackTrace();

                    startIntentFilter();

                }

//                tvSmsMessage.setText(String.format("%s%s%s%s",getString(R.string.message), message,
//                        getString(R.string.matched_string), parseOneTimeCode(message)));

                // send one time code to the server

            } else {

                // Consent canceled, handle the error ...
                Log.wtf("ConsentCancelled", "onActivityResultConsentCancelled: " + true);

            }
        } else {

            // Sms consent denied
            new AlertDialog.Builder(requireActivity())
                    .setMessage("Sms Consent Denied")
                    .show();
        }
    }


    private String parseOneTimeCode(String messageBody) {

        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(messageBody);

        String matchedString;

        if (matcher.find()) {

            matchedString = matcher.group(0);

        } else {

            matchedString = "";
        }

        return matchedString;
    }

    @Override
    public void onResume() {

        Toast.makeText(requireActivity(), "Fragment Resumed", Toast.LENGTH_LONG).show();

        startIntentFilter();

        super.onResume();
    }

    @Override
    public void onStop() {

        Toast.makeText(requireActivity(), "Fragment Stopped", Toast.LENGTH_LONG).show();

        if (!requireActivity().isFinishing()) {

            requireActivity().unregisterReceiver(smsVerificationReceiver);

        }

        super.onStop();
    }

    @Override
    public void onDestroy() {

        Toast.makeText(requireActivity(), "Fragment Destroyed", Toast.LENGTH_LONG).show();

        startIntentFilter();

        if (!requireActivity().isFinishing()) {

            requireActivity().unregisterReceiver(smsVerificationReceiver);

        }

        super.onDestroy();
    }
}