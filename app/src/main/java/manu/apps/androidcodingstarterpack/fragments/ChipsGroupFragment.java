package manu.apps.androidcodingstarterpack.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import manu.apps.androidcodingstarterpack.R;
import manu.apps.androidcodingstarterpack.adapters.ItemAdapter;
import manu.apps.androidcodingstarterpack.adapters.VehicleAdapter;
import manu.apps.androidcodingstarterpack.classes.Config;
import manu.apps.androidcodingstarterpack.classes.Item;
import manu.apps.androidcodingstarterpack.classes.Vehicle;

public class ChipsGroupFragment extends Fragment implements View.OnClickListener {

    ChipGroup cgGender;
    Chip cMale, cFemale;

    MaterialButton btnCheckSelectedChip;

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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View fragmentView = inflater.inflate(R.layout.chips_group_fragment, container, false);

        bundle = getArguments();

        if (bundle != null && bundle.containsKey("SERIALIZABLE")) {

            fragment = getArguments().getString("fragmentName");

            if (fragment.equalsIgnoreCase("gSonFragment")){

                fragmentView = inflater.inflate(R.layout.g_son_fragment, container, false);

            }else if (fragment.equalsIgnoreCase("stateProgressBarFragment")){

                fragmentView = inflater.inflate(R.layout.step_progress_bar_fragment, container, false);

            }


        } else {

            fragmentView = inflater.inflate(R.layout.chips_group_fragment, container, false);

        }

        //return inflater.inflate(R.layout.chips_group_fragment, container, false);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();

        if (bundle != null && bundle.containsKey("SERIALIZABLE")) {

            fragment = getArguments().getString("fragmentName");

            if (fragment.equalsIgnoreCase("gSonFragment")){

                Toast.makeText(requireActivity(), "gSon Fragment", Toast.LENGTH_LONG).show();

                itemList = new ArrayList<>();

                vehicleList = new ArrayList<>();

                srlItems = view.findViewById(R.id.srl_items);
                srlVehicles = view.findViewById(R.id.srl_vehicles);
                rvItems = view.findViewById(R.id.rv_items);
                rvVehicles = view.findViewById(R.id.rv_vehicles);

                if (itemList.isEmpty()){

                    fetchItems();

                }

                if (vehicleList.isEmpty()){

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

            }else if (fragment.equalsIgnoreCase("stateProgressBarFragment")){

                Toast.makeText(requireActivity(), "State Progress Bar Fragment", Toast.LENGTH_LONG).show();

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

//        int viewId = v.getId();
//
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
    }

    private void fetchItems(){

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

    private void setUpItemRecyclerView(List<Item> itemList){

        itemAdapter = new ItemAdapter(requireActivity(), itemList);

        rvItems.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvItems.setAdapter(itemAdapter);

    }

    private void fetchVehicles(){

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

    private void setUpVehicleRecyclerView(List<Vehicle> vehicleList){

        vehicleAdapter = new VehicleAdapter(requireActivity(), vehicleList);

        rvVehicles.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvVehicles.setAdapter(vehicleAdapter);

    }
}