package manu.apps.androidcodingstarterpack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import manu.apps.androidcodingstarterpack.R;
import manu.apps.androidcodingstarterpack.classes.Item;
import manu.apps.androidcodingstarterpack.classes.Vehicle;


public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ItemViewHolder> {

    Context context;
    List<Vehicle> vehicleList;

    public VehicleAdapter(Context context, List<Vehicle> vehicleList) {
        this.context = context;
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vehicle, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {

        final Vehicle currentVehicle = vehicleList.get(position);

        holder.tvVehicleId.setText(String.valueOf(currentVehicle.getVehicleId()));
        holder.tvVehicleName.setText(currentVehicle.getVehicleName());

    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvVehicleId, tvVehicleName;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvVehicleId = itemView.findViewById(R.id.tv_vehicle_id);
            tvVehicleName = itemView.findViewById(R.id.tv_vehicle_name);
        }
    }

}
