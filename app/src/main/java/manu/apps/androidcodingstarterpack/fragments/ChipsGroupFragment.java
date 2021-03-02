package manu.apps.androidcodingstarterpack.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import manu.apps.androidcodingstarterpack.R;

public class ChipsGroupFragment extends Fragment implements View.OnClickListener {

    ChipGroup cgGender;
    Chip cMale, cFemale;

    MaterialButton btnCheckSelectedChip;

    String gender;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chips_group_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cgGender = view.findViewById(R.id.cg_gender);
        cMale = view.findViewById(R.id.c_male);
        cFemale = view.findViewById(R.id.c_female);
        btnCheckSelectedChip = view.findViewById(R.id.btn_check_selected_chip);

        cMale.setOnClickListener(v -> {

            cMale.setChipIconVisible(true);

            cMale.setChipBackgroundColor(AppCompatResources.
                    getColorStateList(requireActivity(), R.color.colorPrimary));

            cMale.setChipIcon(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_check));

            cFemale.setChipBackgroundColor(AppCompatResources.
                    getColorStateList(requireActivity(), R.color.colorAccent));

            cFemale.setChipIconVisible(false);

        });

        cFemale.setOnClickListener(v -> {

            cFemale.setChipIconVisible(true);

            cFemale.setChipBackgroundColor(AppCompatResources.
                    getColorStateList(requireActivity(), R.color.colorPrimary));

            cFemale.setChipIcon(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_check));

            cMale.setChipBackgroundColor(AppCompatResources.
                    getColorStateList(requireActivity(), R.color.colorAccent));

            cMale.setChipIconVisible(false);

        });

        cgGender.setOnCheckedChangeListener((group, checkedId) -> {

            Chip chip = cgGender.findViewById(checkedId);

            if (chip != null) {

                Toast.makeText(requireActivity(), "Chip is " + chip.getText(), Toast.LENGTH_LONG).show();

                gender = chip.getText().toString();

            }

        });

        btnCheckSelectedChip.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        if (viewId == R.id.btn_check_selected_chip){

            if (cgGender.getCheckedChipId() == -1){
                Toast.makeText(requireActivity(), "No Chip has been selected", Toast.LENGTH_SHORT).show();
            }else {

                if (!TextUtils.isEmpty(gender)){
                    new AlertDialog.Builder(requireActivity())
                            .setMessage("Selected Gender is:" + gender)
                            .show();
                }else {
                    Toast.makeText(requireActivity(), "No Chip has been selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}