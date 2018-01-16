package asay.asaymobile.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import asay.asaymobile.R;
import asay.asaymobile.activities.MainActivity;

/**
 * Created by Andreas on 10-01-2018.
 * source: https://github.com/erangaeb/dev-notes/blob/master/android-search-list/FriendListAdapter.java
 */

public class BillListAdapter extends BaseAdapter implements Filterable {
    private MainActivity activity;
    private ListFilter listFilter;
    private Typeface typeface;
    private ArrayList<BillDTO> billList;
    private ArrayList<BillDTO> filteredList;

    public BillListAdapter(MainActivity activity, ArrayList<BillDTO> billList) {
        this.activity = activity;
        this.billList = billList;
        this.filteredList = billList;
        typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/vegur_2.otf");

        getFilter();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    /**
     * Get specific item from user list
     * @param i item index
     * @return list item
     */
    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    /**
     * Get user list item id
     * @param i item index
     * @return current item id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Create list row view
     * @param position index
     * @param view current list item view
     * @param parent parent
     * @return view
     */
    @Override
    public View getView(int position, View cachedView, ViewGroup parent){
        View view = getView(position, cachedView, parent);
        final BillDTO bill = (BillDTO) getItem(position);
        TextView titleTextView = view.findViewById(R.id.listeelem_header);
        String title = bill.number + ": " + bill.getTitleShort();
        titleTextView.setText(title);
        TextView date = view.findViewById(R.id.listeelem_date);
        TextView numberOfVotes = view.findViewById(R.id.listeelem_number_of_votes);
        return view;
    }

    /**
     * Get custom filter
     * @return filter
     */
    @Override
    public Filter getFilter() {
        if (listFilter  == null) {
            listFilter = new ListFilter();
        }

        return listFilter;
    }


    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */

    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<BillDTO> tempList = new ArrayList<BillDTO>();

                // search content in friend list
                for (BillDTO bill : billList) {
                    if (bill.getTitleShort().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            bill.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            bill.getNumber().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(bill);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = billList.size();
                filterResults.values = billList;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<BillDTO>) results.values;
            notifyDataSetChanged();
        }

    }

}
