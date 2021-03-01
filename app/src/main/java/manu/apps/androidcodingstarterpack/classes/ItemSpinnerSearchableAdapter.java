package manu.apps.androidcodingstarterpack.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import manu.apps.androidcodingstarterpack.R;

public class ItemSpinnerSearchableAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<Item> itemList;
    List<Item> itemSuggestionList = new ArrayList<>();
    Filter filter = new CustomFilter();

    public ItemSpinnerSearchableAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemSuggestionList.size(); // Return the size of the suggestions list.
    }

    @Override
    public Object getItem(int position) {
        return itemSuggestionList.get(position).getItemName();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ViewHolder holder;

        if (convertView == null) {

            // Custom layout
            convertView = inflater.inflate(R.layout.spinner_layout, parent, false);

            holder = new ViewHolder();

            holder.tvSpinnerItemName = (TextView) convertView.findViewById(R.id.tv_spinner_item_name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvSpinnerItemName.setText(itemSuggestionList.get(position).getItemName());

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private static class ViewHolder {

        // Id of the text view in your custom layout spinner
        TextView tvSpinnerItemName;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            itemSuggestionList.clear();

            if (itemList != null && constraint != null) { // Check if item List and Constraint aren't null.
                for (int i = 0; i < itemList.size(); i++) {

                    // Compare item in item list if it contains constraints.
                    // First Items List Searches for constraints with lowercase second searches with sentence case
                    // and the third one searches for constraints with uppercase
                    if (itemList.get(i).getItemName().toLowerCase().contains(constraint) |
                            itemList.get(i).getItemName().contains(constraint) |
                            itemList.get(i).getItemName().toUpperCase().contains(constraint)) {
                        itemSuggestionList.add(itemList.get(i)); // If TRUE add item in Suggestions.
                    }
                }
            }

            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
            results.values = itemSuggestionList;
            results.count = itemSuggestionList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
