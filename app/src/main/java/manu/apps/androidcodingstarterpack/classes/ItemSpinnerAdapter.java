package manu.apps.androidcodingstarterpack.classes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import manu.apps.androidcodingstarterpack.R;

public class ItemSpinnerAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final List<Item> itemList;
    private final List<Item> itemListAll;
    private final int layoutResourceId;

    public ItemSpinnerAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        this.context = context;
        this.layoutResourceId = resource;
        this.itemList = new ArrayList<>(items);
        this.itemListAll = new ArrayList<>(items);
    }

    public int getCount() {
        return itemList.size();
    }

    public Item getItem(int position) {
        return itemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            Item item = getItem(position);
            TextView tvSpinnerItemName = (TextView) convertView.findViewById(R.id.tv_spinner_item_name);
            tvSpinnerItemName.setText(item.getItemName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {

                return ((Item) resultValue).getItemName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Item> itemsSuggestion = new ArrayList<>();
                if (constraint != null) {
                    for (Item item : itemListAll) {

                        if (item.getItemName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            itemsSuggestion.add(item);
                        }
                    }
                    filterResults.values = itemsSuggestion;
                    filterResults.count = itemsSuggestion.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using itemList.addAll((ArrayList<Item>) results.values);
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof Item) {
                            itemList.add((Item) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    itemList.addAll(itemListAll);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
