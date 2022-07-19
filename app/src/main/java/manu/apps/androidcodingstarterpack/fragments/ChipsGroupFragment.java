package manu.apps.androidcodingstarterpack.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import manu.apps.androidcodingstarterpack.R;
import manu.apps.androidcodingstarterpack.adapters.ItemAdapter;
import manu.apps.androidcodingstarterpack.adapters.VehicleAdapter;
import manu.apps.androidcodingstarterpack.classes.Config;
import manu.apps.androidcodingstarterpack.classes.Item;
import manu.apps.androidcodingstarterpack.classes.Vehicle;

import static android.app.Activity.RESULT_OK;

public class ChipsGroupFragment extends Fragment implements View.OnClickListener {

    ChipGroup cgGender;
    Chip cMale, cFemale;

    MaterialButton btnCheckSelectedChip, btnPickCaptureImage, btnCaptureFromCamera,
            btnPickFromGallery;

    RelativeLayout rlCaptureImage, rlPickImage;

    String gender;

    String fragment;

    Bundle bundle;

    SwipeRefreshLayout srlItems, srlVehicles;

    RecyclerView rvItems, rvVehicles;

    private List<Item> itemList;

    private List<Vehicle> vehicleList;

    ItemAdapter itemAdapter;

    VehicleAdapter vehicleAdapter;

//    RequestQueue itemsRequestQueue, vehiclesRequestQueue;
//    StringRequest stringRequest, vehicleRequest;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    BottomSheetBehavior<View> pickCaptureImageBSheetBehavior;

    ImageView imvImage;

    private static final int PICK_IMAGE_FROM_GALLERY_REQUEST_CODE = 999;

    private static final int CAPTURE_IMAGE_FROM_CAMERA_REQUEST_CODE = 555;

    private static final int CROP_IMAGE_REQUEST_CODE = 111;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View fragmentView = inflater.inflate(R.layout.chips_group_fragment, container, false);

        bundle = getArguments();

        if (bundle != null && bundle.containsKey("SERIALIZABLE")) {

            fragment = getArguments().getString("fragmentName");

            if (fragment.equalsIgnoreCase("gSonFragment")) {

                fragmentView = inflater.inflate(R.layout.g_son_fragment, container, false);

            } else if (fragment.equalsIgnoreCase("stateProgressBarFragment")) {

                fragmentView = inflater.inflate(R.layout.step_progress_bar_fragment, container, false);

            } else if (fragment.equalsIgnoreCase("imagePickerFragment")) {

                fragmentView = inflater.inflate(R.layout.image_picker_capture_fragment, container, false);

            }


        } else {

            fragmentView = inflater.inflate(R.layout.chips_group_fragment, container, false);

        }

        //return inflater.inflate(R.layout.chips_group_fragment, container, false);
        return fragmentView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();

        if (bundle != null && bundle.containsKey("SERIALIZABLE")) {

            fragment = getArguments().getString("fragmentName");

            if (fragment.equalsIgnoreCase("gSonFragment")) {

                Toast.makeText(requireActivity(), "gSon Fragment", Toast.LENGTH_LONG).show();

                itemList = new ArrayList<>();

                vehicleList = new ArrayList<>();

                srlItems = view.findViewById(R.id.srl_items);
                srlVehicles = view.findViewById(R.id.srl_vehicles);
                rvItems = view.findViewById(R.id.rv_items);
                rvVehicles = view.findViewById(R.id.rv_vehicles);

                if (itemList.isEmpty()) {

                    fetchItems();

                }

                if (vehicleList.isEmpty()) {

                    fetchVehicles();
                }

                srlItems.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                        getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimary));

                srlItems.setOnRefreshListener(() -> {

                    // Clear your list first
                    itemList.clear();

                    // Set up your recycler view and load items again
                    fetchItems();

                    itemAdapter.notifyDataSetChanged();

                    srlItems.setRefreshing(false);

                });

                srlVehicles.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                        getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimary));

                srlVehicles.setOnRefreshListener(() -> {

                    // Clear your list first
                    vehicleList.clear();

                    // Set up your recycler view and load items again
                    fetchVehicles();

                    vehicleAdapter.notifyDataSetChanged();

                    srlVehicles.setRefreshing(false);

                });

            } else if (fragment.equalsIgnoreCase("stateProgressBarFragment")) {

                Toast.makeText(requireActivity(), "State Progress Bar Fragment", Toast.LENGTH_LONG).show();

            } else if (fragment.equalsIgnoreCase("imagePickerFragment")) {

                btnPickCaptureImage = view.findViewById(R.id.btn_pick_capture_image);
                btnCaptureFromCamera = view.findViewById(R.id.btn_capture_from_camera);
                btnPickFromGallery = view.findViewById(R.id.btn_pick_from_gallery);

                imvImage = view.findViewById(R.id.imv_image);


                rlCaptureImage = view.findViewById(R.id.rl_capture_image);
                rlPickImage = view.findViewById(R.id.rl_pick_image);


                View itemClickView = view.findViewById(R.id.item_click_bottom_sheet);
                pickCaptureImageBSheetBehavior = BottomSheetBehavior.from(itemClickView);
                pickCaptureImageBSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                btnPickCaptureImage.setOnClickListener(this);
                btnCaptureFromCamera.setOnClickListener(this);
                btnPickFromGallery.setOnClickListener(this);
                rlCaptureImage.setOnClickListener(this);
                rlPickImage.setOnClickListener(this);

            }


        }


//        cgGender = view.findViewById(R.id.cg_gender);
//        cMale = view.findViewById(R.id.c_male);
//        cFemale = view.findViewById(R.id.c_female);
//        btnCheckSelectedChip = view.findViewById(R.id.btn_check_selected_chip);
//
//        cMale.setOnClickListener(v -> {
//
//            cMale.setChipIconVisible(true);
//
//            cMale.setChipBackgroundColor(AppCompatResources.
//                    getColorStateList(requireActivity(), R.color.colorPrimary));
//
//            cMale.setChipIcon(ContextCompat.getDrawable(requireActivity(),
//                    R.drawable.ic_check));
//
//            cFemale.setChipBackgroundColor(AppCompatResources.
//                    getColorStateList(requireActivity(), R.color.colorAccent));
//
//            cFemale.setChipIconVisible(false);
//
//        });
//
//        cFemale.setOnClickListener(v -> {
//
//            cFemale.setChipIconVisible(true);
//
//            cFemale.setChipBackgroundColor(AppCompatResources.
//                    getColorStateList(requireActivity(), R.color.colorPrimary));
//
//            cFemale.setChipIcon(ContextCompat.getDrawable(requireActivity(),
//                    R.drawable.ic_check));
//
//            cMale.setChipBackgroundColor(AppCompatResources.
//                    getColorStateList(requireActivity(), R.color.colorAccent));
//
//            cMale.setChipIconVisible(false);
//
//        });
//
//        cgGender.setOnCheckedChangeListener((group, checkedId) -> {
//
//            Chip chip = cgGender.findViewById(checkedId);
//
//            if (chip != null) {
//
//                Toast.makeText(requireActivity(), "Chip is " + chip.getText(), Toast.LENGTH_LONG).show();
//
//                gender = chip.getText().toString();
//
//            }
//
//        });
//
//        btnCheckSelectedChip.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

//        if (viewId == R.id.btn_check_selected_chip) {
//
//            if (cgGender.getCheckedChipId() == -1) {
//                Toast.makeText(requireActivity(), "No Chip has been selected", Toast.LENGTH_SHORT).show();
//            } else {
//
//                if (!TextUtils.isEmpty(gender)) {
//                    new AlertDialog.Builder(requireActivity())
//                            .setMessage("Selected Gender is:" + gender)
//                            .show();
//                } else {
//                    Toast.makeText(requireActivity(), "No Chip has been selected", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
        if (viewId == R.id.btn_pick_capture_image) {

            pickCaptureImageBSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }
        if (viewId == R.id.btn_capture_from_camera | viewId == R.id.rl_capture_image) {

            checkAndRequestCameraPermission();

        }
        if (viewId == R.id.btn_pick_from_gallery | viewId == R.id.rl_pick_image) {

            pickCaptureImageBSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            pickImageFromGallery();

        }
    }

    private void fetchItems() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        stringRequest = new StringRequest(Request.Method.GET, Config.ITEMS,
                response -> {

                    int itemId;
                    String itemName;

                    if (response.length() == 0) {

                        progressDialog.dismiss();

                        Toast.makeText(requireActivity(), "Json response is empty", Toast.LENGTH_LONG).show();


                    } else {

                        try {

                            //JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = new JSONArray(response);

                            //JSONArray jsonArray = jsonObject.getJSONArray(response);

                            //JSONArray jsonArray = jsonObject.getJSONArray("redemption");

                            //JSONObject jsonObject = new JSONObject(response);

                            //JSONArray jsonArray = jsonObject.getJSONArray("responseArray");

                            if (jsonArray.length() == 0) {

                                Toast.makeText(requireActivity(), "Json response is empty", Toast.LENGTH_LONG).show();

                                progressDialog.dismiss();

                            } else {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    Item item = new Item();

                                    try {

                                        itemId = object.getInt("itemId");

                                    } catch (Exception e) {

                                        itemId = 0;

                                        e.printStackTrace();

                                    }

                                    item.setItemId(itemId);

                                    try {

                                        itemName = object.getString("itemName");


                                    } catch (Exception e) {

                                        itemName = "n/a";

                                        e.printStackTrace();

                                    }

                                    item.setItemName(itemName);

                                    itemList.add(item);

                                    progressDialog.dismiss();
                                }

                            }
                        } catch (JSONException e) {

                            e.printStackTrace();

                            progressDialog.dismiss();

                            Toast.makeText(getActivity(), "Json Error" + e.toString(), Toast.LENGTH_LONG).show();


                        }

                    }

                    setUpItemRecyclerView(itemList);


                }, error -> {

            error.printStackTrace();

            progressDialog.dismiss();

            Toast.makeText(getActivity(), "Volley Error" + error.getMessage(), Toast.LENGTH_LONG).show();


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        requestQueue = Volley.newRequestQueue(requireActivity());
        requestQueue.add(stringRequest);

    }

    private void setUpItemRecyclerView(List<Item> itemList) {

        itemAdapter = new ItemAdapter(requireActivity(), itemList);

        rvItems.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvItems.setAdapter(itemAdapter);

    }

    private void fetchVehicles() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        stringRequest = new StringRequest(Request.Method.GET, Config.VEHICLES,
                response -> {

                    int vehicleId;
                    String vehicleName;

                    if (response.length() == 0) {

                        progressDialog.dismiss();

                        Toast.makeText(requireActivity(), "Json response is empty", Toast.LENGTH_LONG).show();


                    } else {

                        try {

                            //JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = new JSONArray(response);

                            //JSONArray jsonArray = jsonObject.getJSONArray(response);

                            //JSONArray jsonArray = jsonObject.getJSONArray("redemption");

                            //JSONObject jsonObject = new JSONObject(response);

                            //JSONArray jsonArray = jsonObject.getJSONArray("responseArray");

                            if (jsonArray.length() == 0) {

                                Toast.makeText(requireActivity(), "Json response is empty", Toast.LENGTH_LONG).show();

                                progressDialog.dismiss();

                            } else {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    Vehicle vehicle = new Vehicle();

                                    try {

                                        vehicleId = object.getInt("id");

                                    } catch (Exception e) {

                                        vehicleId = 0;

                                        e.printStackTrace();

                                    }

                                    vehicle.setVehicleId(vehicleId);

                                    try {

                                        vehicleName = object.getString("name");


                                    } catch (Exception e) {

                                        vehicleName = "n/a";

                                        e.printStackTrace();

                                    }

                                    vehicle.setVehicleName(vehicleName);

                                    vehicleList.add(vehicle);

                                    progressDialog.dismiss();
                                }

                            }
                        } catch (JSONException e) {

                            e.printStackTrace();

                            progressDialog.dismiss();

                            Toast.makeText(getActivity(), "Json Error" + e.toString(), Toast.LENGTH_LONG).show();


                        }

                    }

                    setUpVehicleRecyclerView(vehicleList);


                }, error -> {

            error.printStackTrace();

            progressDialog.dismiss();

            Toast.makeText(getActivity(), "Volley Error" + error.getMessage(), Toast.LENGTH_LONG).show();


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        requestQueue = Volley.newRequestQueue(requireActivity());
        requestQueue.add(stringRequest);

    }

    private void setUpVehicleRecyclerView(List<Vehicle> vehicleList) {

        vehicleAdapter = new VehicleAdapter(requireActivity(), vehicleList);

        rvVehicles.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvVehicles.setAdapter(vehicleAdapter);

    }

    private void captureImageFromCamera(){

        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (openCameraIntent.resolveActivity(requireActivity().getPackageManager()) != null){

            startActivityForResult(openCameraIntent, CAPTURE_IMAGE_FROM_CAMERA_REQUEST_CODE);

        }
    }

    private void checkAndRequestCameraPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    // If The Permission has already been granted proceed with the action

                    Toast.makeText(requireActivity(), "Camera Permission has been granted", Toast.LENGTH_SHORT).show();

                    captureImageFromCamera();


                } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected. In this UI,
                    // include a "cancel" or "no thanks" button that allows the user to
                    // continue using your app without granting the permission.

                    Toast.makeText(requireActivity(), "We require camera permissions to be able " +
                            "to capture a clients photo, you won't be able to capture an image", Toast.LENGTH_LONG).show();

//                    showActionDialog("We require SMS permission to be able to read the OTP you will receive and " +
//                                    "validate your phone number, you will be directed to login on Cancel",
//                            "Cancel", "onCancelClicked",
//                            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_redirect));


                }else {

                    // If Permission has not been granted
                    // Request the permission first time
                    requestCameraPermissionLauncher.launch(
                            Manifest.permission.CAMERA);

                }
            }
        }


    }


    private void pickImageFromGallery() {

        getImageFromGallery.launch("image/*");


//        Intent pickImageIntent = new Intent();
//        pickImageIntent.setType("image/*");
//        pickImageIntent.setAction(Intent.ACTION_PICK);
//        startActivityForResult(pickImageIntent, PICK_IMAGE_FROM_GALLERY_REQUEST_CODE);

//        Intent pickImageIntent = new Intent();
//
//        pickImageIntent.setType("image/*");
//        //pickImageIntent.setAction(Intent.ACTION_GET_CONTENT);
//        // Action Pick has better pick UI
//        pickImageIntent.setAction(Intent.ACTION_PICK);
//        pickImageIntent.putExtra("crop", "true");
//        pickImageIntent.putExtra("aspectX", 0);
//        pickImageIntent.putExtra("aspectY", 0);
//
//        try {
//            pickImageIntent.putExtra("return-data", true);
//            startActivityForResult(
//                    Intent.createChooser(pickImageIntent, "Complete action using"),
//                    PICK_IMAGE_FROM_GALLERY_REQUEST_CODE);
//        } catch (ActivityNotFoundException e) {
//
//            e.printStackTrace();
//
//            // show Error Exception
//        }

//        try{
//
//            //Intent pickImageFromGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(new Intent(Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
//                    PICK_IMAGE_FROM_GALLERY_REQUEST_CODE);
//
//        }catch (Exception e){
//
//            e.printStackTrace();
//
//            // Pick Image From Gallery Error
//            // Show Error Exception
//
//        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode,
//                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST_CODE) {
//            if (resultCode == RESULT_OK && data != null) {
//
//                Uri selectedImageUri = data.getData();
//
//                imvImage.setImageURI(selectedImageUri);
//
//            } else {
//                // Show Error Exception Dialog
//                // We encountered an error while picking your image from gallery
//                // Try Again, Cancel
//            }
//        }

//        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK && data != null) {
//
//                Bundle bundle = data.getExtras();
//
//                if (bundle != null) {
//
//                    Bitmap bitmapImage = bundle.getParcelable("data");
//
//                    imvImage.setImageBitmap(bitmapImage);
//                }
//            }
//        }

//        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK && data != null) {
//
//                try{
//                    Uri selectedImageUri = data.getData();
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA };
//                    Cursor cursor = requireActivity().getContentResolver().query(selectedImageUri,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String imagePath = cursor.getString(columnIndex);
//                    cursor.close();
//
//                    performCrop(imagePath);
//
//                }catch(Exception e){
//
//                    e.printStackTrace();
//
//                }
//
//            }
//        }else if (requestCode == CROP_IMAGE_REQUEST_CODE) {
//
//            if (resultCode == RESULT_OK && data != null) {
//
//                Bundle extras = data.getExtras();
//                Bitmap selectedBitmap = extras.getParcelable("data");
//                imvImage.setImageBitmap(selectedBitmap);
//
//            }
//        }
//    }

//    private void performCrop(String imagePath){
//
//        try {
//            //Start Crop Activity
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            Uri contentUri = Uri.fromFile(new File(imagePath));
//
//            cropIntent.setDataAndType(contentUri, "image/*");
//            // set crop properties
//            cropIntent.putExtra("crop", "true");
//            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            // indicate output X and Y
//            cropIntent.putExtra("outputX", 280);
//            cropIntent.putExtra("outputY", 280);
//
//            // retrieve data on return
//            cropIntent.putExtra("return-data", true);
//            // start the activity - we handle returning in onActivityResult
//            startActivityForResult(cropIntent, CROP_IMAGE_REQUEST_CODE);
//        }
//        // respond to users whose devices do not support the crop action
//        catch (ActivityNotFoundException e) {
//
//            e.printStackTrace();
//
//            // ShowErrorException
//            // We encountered an error while performing crop
//        }
//
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_FROM_CAMERA_REQUEST_CODE) {

            if (resultCode == RESULT_OK && data != null){

                // Bitmap captureImageBitmap = data.getParcelableExtra("data");
                // or use method below
                Bitmap captureImageBitmap = (Bitmap) data.getExtras().get("data");

                imvImage.setImageBitmap(captureImageBitmap);


            }

//            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//                new AlertDialog.Builder(requireActivity())
//                        .setMessage("Voila, reached")
//                        .show();
//
//                Bitmap captureImageBitmap = data.getParcelableExtra("data");
//
//                imvImage.setImageBitmap(captureImageBitmap);
//
//            }
        }
    }

    ActivityResultLauncher<String> getImageFromGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {

                    imvImage.setImageURI(uri);
                }
            });

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    // Permission is granted. Continue the action or workflow in your
                    // app.

                    captureImageFromCamera();

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.

                    Toast.makeText(requireActivity(), "You wont be able to capture clients image",
                            Toast.LENGTH_LONG).show();

//                    showActionDialog("You won't be able to proceed because SMS permission " +
//                                    "is required to validate your phone number, you will be directed to login on Close",
//                            "Close", "onCloseClicked",
//                            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_redirect));

                }
            });

}