package manu.apps.androidcodingstarterpack.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;

import manu.apps.androidcodingstarterpack.R;

public class SmsRetriever extends Fragment {


    NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sms_retriever_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

    }

//    private void startSMSListener(){
//
//        SmsRetrieverClient smsRetrieverClient = SmsRetriever.getClient(requireActivity());
//
//        Task<Void> smsTask = smsRetrieverClient.startSmsRetriever();
//
//        smsTask.addOnSuccessListener(aVoid -> {
//
//            Log.d("SMSListenerStarted", "SMSListenerStartedTrue");
//
//        });
//
//        smsTask.addOnFailureListener(e -> {
//
//            Log.d("SMSFailureListener", "Error: " + e);
//
//            e.printStackTrace();
//
//        });
//    }


}