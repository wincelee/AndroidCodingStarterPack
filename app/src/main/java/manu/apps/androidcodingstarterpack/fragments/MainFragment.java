package manu.apps.androidcodingstarterpack.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;

import manu.apps.androidcodingstarterpack.R;

public class MainFragment extends Fragment implements View.OnClickListener {

    MaterialButton btnVibration, btnOneShotVibration, btnWaveFormVibration,
            btnSmsSenderRetriever, btnTextInputLayoutAutoCompleteTextViewSpinner,
            btnChipsFragment;

    BottomSheetBehavior<View> bottomSheetBehavior;

    NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        btnVibration = view.findViewById(R.id.btn_vibration);
        btnOneShotVibration = view.findViewById(R.id.btn_one_shot_vibration);
        btnWaveFormVibration = view.findViewById(R.id.btn_wave_form_vibration);
        btnSmsSenderRetriever = view.findViewById(R.id.btn_sms_sender_retriever);
        btnTextInputLayoutAutoCompleteTextViewSpinner = view.findViewById(R.id.btn_text_input_layout_auto_complete_text_view_spinner);
        btnChipsFragment = view.findViewById(R.id.btn_chips_fragment);

        View bottomSheetView = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        btnVibration.setOnClickListener(this);
        btnOneShotVibration.setOnClickListener(this);
        btnWaveFormVibration.setOnClickListener(this);
        btnSmsSenderRetriever.setOnClickListener(this);
        btnTextInputLayoutAutoCompleteTextViewSpinner.setOnClickListener(this);
        btnChipsFragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        if (viewId == R.id.btn_vibration) {

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }

        if (viewId == R.id.btn_one_shot_vibration) {

            if (Build.VERSION.SDK_INT >= 26) {

                // Vibration Amplitude is the intensity of the vibration ranges from 1 to 255
                // Changing the vibration in milliseconds is how long the vibration will take
                ((Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE))
                        .vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

            } else {

                // Changing the vibration in milliseconds is how long the vibration will take
                ((Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE))
                        .vibrate(500);

            }
        }

        if (viewId == R.id.btn_wave_form_vibration) {

            // Get instance of Vibrator from current Context
            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

            // Start without a delay
            // Vibrate for 200 milliseconds
            // Sleep for 200 milliseconds
            //long[] VIBRATE_PATTERN = {0, 200, 200};
            long[] VIBRATE_PATTERN = {0, 300, 300};

            if (vibrator.hasVibrator()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // API 26 and above
                    // Repeat pattern is how many times the vibration will occur -1 means it will occur only once
                    vibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN, 0));
                } else {
                    // Below API 26
                    // Repeat pattern is how many times the vibration will occur -1 means it will occur only once
                    vibrator.vibrate(VIBRATE_PATTERN, 0);
                }
            }

            // Stop the vibration after some time
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(vibrator::cancel, 1000);

        }

        if (viewId == R.id.btn_sms_sender_retriever) {

            navController.navigate(R.id.action_main_to_sender_one_time);

        }

        if (viewId == R.id.btn_text_input_layout_auto_complete_text_view_spinner) {

            navController.navigate(R.id.nav_til_spinner_fragment);

        }

        if (viewId == R.id.btn_chips_fragment){

            navController.navigate(R.id.nav_chips_group);

        }

    }
}