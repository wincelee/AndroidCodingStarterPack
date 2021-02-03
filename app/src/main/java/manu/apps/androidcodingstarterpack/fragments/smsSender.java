package manu.apps.androidcodingstarterpack.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import manu.apps.androidcodingstarterpack.R;
import manu.apps.androidcodingstarterpack.viewmodels.SmsSenderViewModel;

import static android.app.Activity.RESULT_OK;

public class smsSender extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    private SmsSenderViewModel smsSenderViewModel;

    TextInputEditText etOtpPhoneNumber;

    GoogleApiClient googleApiClient;

    private GoogleSignInClient googleSignInClient;

    private static final int RESOLVE_HINT = 9001;

    private static final int RC_SIGN_IN = 9001;

    MaterialButton btnProceed;

    NavController navController;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.sms_sender_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        smsSenderViewModel = new ViewModelProvider(this).get(SmsSenderViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);


        etOtpPhoneNumber = view.findViewById(R.id.et_otp_phone_number);
        btnProceed = view.findViewById(R.id.btn_proceed);

        if(googleApiClient != null){

            googleApiClient = new GoogleApiClient.Builder(requireActivity())
                    .addConnectionCallbacks(this)
                    .enableAutoManage(requireActivity(), this)
                    .addApi(Auth.CREDENTIALS_API)
                    .build();

        }

//        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        googleSignInClient = GoogleSignIn.getClient(requireActivity(), options);

        //signIn();

        btnProceed.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        if(viewId == R.id.btn_proceed){

            navController.navigate(R.id.action_sender_to_retriever);

        }

    }

    private void signIn() {
        // Launches the sign in flow, the result is returned in onActivityResult
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        requestPhoneNumberHint();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void requestPhoneNumberHint() {

        // Hint request for email

        /*HintRequest hintRequest = new HintRequest.Builder()
                .setEmailAddressIdentifierSupported(true)
                .build();*/

        // Hint request for phone number

        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                googleApiClient, hintRequest);

        try {

            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_HINT, null, 0, 0, 0, new Bundle());

        } catch (IntentSender.SendIntentException e) {

            e.printStackTrace();

        }

    }


    // Obtain the phone number or email from the result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                // credential.getId();  <-- will need to process phone number string

                // Trims the first 3 characters
                //etOtpPhoneNumber.setText(credential.getId().substring(3));
                etOtpPhoneNumber.setText(credential.getId());
            }
        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//
//            Task<GoogleSignInAccount> task =
//                    GoogleSignIn.getSignedInAccountFromIntent(data);
//            if (task.isSuccessful()) {
//                // Sign in succeeded, proceed with account
//                GoogleSignInAccount acct = task.getResult();
//
//                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
//
//                etOtpPhoneNumber.setText(credential.getId());
//
//
//            } else {
//                // Sign in failed, handle failure and update UI
//                // ...
//            }
//        }
//    }

}