package manu.apps.androidcodingstarterpack.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import manu.apps.androidcodingstarterpack.R;
import manu.apps.androidcodingstarterpack.classes.Config;
import manu.apps.androidcodingstarterpack.classes.CustomTextWatcher;
import manu.apps.androidcodingstarterpack.classes.Item;
import manu.apps.androidcodingstarterpack.classes.ItemSpinnerAdapter;
import manu.apps.androidcodingstarterpack.classes.ItemSpinnerSearchableAdapter;

public class TilSpinnerFragment extends Fragment implements View.OnClickListener {

    TextInputLayout tilItems, tilItemsWithThreshold;
    AutoCompleteTextView actvItems, actvItemsWithThreshold;

    ItemSpinnerAdapter itemSpinnerAdapter;
    List<Item> itemList;

    MaterialButton btnCheckSelection;

    int itemId;
    String itemName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.til_spinner_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemList = new ArrayList<>();

        tilItems = view.findViewById(R.id.til_items);
        actvItems = view.findViewById(R.id.actv_items);
        tilItemsWithThreshold = view.findViewById(R.id.til_items_with_threshold);
        actvItemsWithThreshold = view.findViewById(R.id.actv_items_with_threshold);
        btnCheckSelection = view.findViewById(R.id.btn_check_selection);

        // Fetching Items
        fetchItems();

        // Getting selected items from auto complete text view
        actvItems.setOnItemClickListener((parent, arg1, pos, id) -> {

            Item selectedItem = (Item) parent.getItemAtPosition(pos);

            int itemId = selectedItem.getItemId();
            String itemName = selectedItem.getItemName();

            Toast.makeText(requireActivity(), "Item Id: " + itemId + "\nItem Name: " + itemName, Toast.LENGTH_LONG).show();

        });

        actvItemsWithThreshold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                itemName = null;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        actvItemsWithThreshold.setOnItemClickListener((parent, arg1, pos, id) -> {

            Item selectedItem = (Item) parent.getItemAtPosition(pos);

            itemId = selectedItem.getItemId();
            itemName = selectedItem.getItemName();

            Toast.makeText(requireActivity(), "Item Id: " + itemId + "\nItem Name: " + itemName, Toast.LENGTH_LONG).show();

        });

        btnCheckSelection.setOnClickListener(this);

        actvItems.addTextChangedListener(new CustomTextWatcher(tilItems));
        actvItemsWithThreshold.addTextChangedListener(new CustomTextWatcher(tilItemsWithThreshold));


    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        if (viewId == R.id.btn_check_selection){

            String checkItemName = actvItems.getText().toString().trim();
            String checkItemName2 = actvItemsWithThreshold.getText().toString().trim();

            if (TextUtils.isEmpty(checkItemName)){

                tilItems.setError("You have not selected any item");

            }else if (TextUtils.isEmpty(checkItemName2)){

                tilItemsWithThreshold.setError("You have not selected any item");

            }else {

                tilItems.setErrorEnabled(false);
                tilItemsWithThreshold.setErrorEnabled(false);
            }

        }
    }

    private void fetchItems() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.ITEMS,
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

                            Toast.makeText(getActivity(), "Json Error", Toast.LENGTH_SHORT).show();

                        }

                    }

                    setUpItemSpinner(itemList);


                }, error -> {

            error.printStackTrace();

            progressDialog.dismiss();

            Toast.makeText(getActivity(), "Volley Error", Toast.LENGTH_SHORT).show();


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        requestQueue.add(stringRequest);

    }

    private void setUpItemSpinner(List<Item> itemList) {

        itemSpinnerAdapter = new ItemSpinnerAdapter(requireActivity(), R.layout.spinner_layout, itemList);
        actvItems.setAdapter(itemSpinnerAdapter);

//        ItemSpinnerSearchableAdapter itemSpinnerSearchableAdapter = new ItemSpinnerSearchableAdapter(requireActivity(), itemList);
//        actvItemsWithThreshold.setAdapter(itemSpinnerSearchableAdapter);
//        // Setting threshold to start displaying the drop down upon number of characters typed
//        actvItemsWithThreshold.setThreshold(1);

        // Hardcoding list items for Auto Complete Text View
//        List<String> items = new ArrayList<>();
//        items.add("Toyota");
//        items.add("Volvo");
//        items.add("Suzuki");
//        items.add("Mazda");
//        items.add("BMW");
//        items.add("Nissan");
//        items.add("Ford ");
//        items.add("Volkswagen");
//        items.add("Audi");
//        items.add("Hyundai");
//        items.add("Jeep");
//        items.add("Land Rover");
//        items.add("Mitsubishi");
//        items.add("Subaru");
//
//        ArrayAdapter<String> itemArrayAdapter = new ArrayAdapter<>(requireActivity(),
//                android.R.layout.simple_list_item_1, items);
//
//        actvItemsWithThreshold.setAdapter(itemArrayAdapter);

        ArrayAdapter<Item> arrayAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, itemList);
        actvItemsWithThreshold.setThreshold(1);
        actvItemsWithThreshold.setAdapter(arrayAdapter);

    }
}