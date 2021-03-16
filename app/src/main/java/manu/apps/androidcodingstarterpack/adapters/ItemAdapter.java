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


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    Context context;
    List<Item> itemList;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {

        final Item currentItem = itemList.get(position);

        holder.tvItemId.setText(String.valueOf(currentItem.getItemId()));
        holder.tvItemName.setText(currentItem.getItemName());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemName, tvItemId;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemId = itemView.findViewById(R.id.tv_item_id);
        }
    }

}
