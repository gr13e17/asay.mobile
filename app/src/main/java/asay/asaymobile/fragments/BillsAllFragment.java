package asay.asaymobile.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import asay.asaymobile.BillContract;
import asay.asaymobile.R;
import asay.asaymobile.UserContract;
import asay.asaymobile.activities.BillActivity;
import asay.asaymobile.activities.MainActivity;
import asay.asaymobile.fetch.HttpAsyncTask;
import asay.asaymobile.model.BillDTO;
import asay.asaymobile.model.UserDTO;
import asay.asaymobile.presenter.BillPresenter;
import asay.asaymobile.presenter.UserPresenter;


public class BillsAllFragment extends Fragment implements AdapterView.OnItemClickListener, BillContract.View, UserContract.View{
    EditText etResponse;
    private BillPresenter billPresenter;
    private UserPresenter userPresenter;
    double userId = 1;
    private ArrayList<BillDTO> bills = new ArrayList<>();
    private ArrayList<Integer> savedbills = new ArrayList<>();
    public ArrayAdapter<BillDTO> adapter;
    ListView listview;
    private MainActivity activity;
    private Typeface typeface;
    private ArrayList<BillDTO> filteredList;
    private ListFilter listFilter;
    public BillsAllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bills_all, container, false);
        // ButterKnife.bind(this, view);
        billPresenter = new BillPresenter(this);
        userPresenter = new UserPresenter(this);
        boolean isFavorite = false;
        boolean isEnded = false;
        if(getArguments() != null){
            isFavorite = getArguments().getBoolean("isFavorite");
            isEnded = getArguments().getBoolean("isEnded");
        }

        String baseUrl = "http://oda.ft.dk/api/Sag?$orderby=id%20desc";
        String proposalExpand = "&$expand=Sagsstatus,Periode,Sagstype,SagAkt%C3%B8r,Sagstrin";
        String proposalFilter = "&$filter=(typeid%20eq%203%20or%20typeid%20eq%205)%20and%20periodeid%20eq%20146";
        String urlAsString = new StringBuilder().append(baseUrl).append(proposalExpand).append(proposalFilter).toString();

        if(isFavorite){
            userPresenter.getUser(userId);
            view.findViewById(R.id.loadingBill).setVisibility(View.GONE);

        } else if (isEnded) {
            new HttpAsyncTask(getActivity(), new AsyncTaskCompleteListener()).execute(urlAsString);
            billPresenter.getEndedBills();
            view.findViewById(R.id.loadingBill).setVisibility(View.GONE);

        } else {
            new HttpAsyncTask(getActivity(), new AsyncTaskCompleteListener()).execute(urlAsString);
            billPresenter.getAllBills();
        }
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        // call AsynTask to perform network operation on separate thread
        // TODO: Use isEnded to show only ended bills
        getFilter();
        // get reference to the views
        adapter = new ArrayAdapter<BillDTO>(getActivity(), R.layout.list_item_bill,R.id.listeelem_header,bills){
            @SuppressLint("DefaultLocale")
            @Override
            public View getView(int position, View cachedView, ViewGroup parent){
                View view = super.getView(position, cachedView, parent);
                BillDTO bill = bills.get(position);
                TextView titleTextView = view.findViewById(R.id.listeelem_header);
                String title = bill.number + ": " + bill.getTitleShort();
                titleTextView.setText(title);
                TextView date = view.findViewById(R.id.listeelem_date);
                date.setText(String.format(getResources().getString(R.string.days_until_deadline), calcDateFromToday(bill.getDeadline())));
                TextView numberOfVotes = view.findViewById(R.id.listeelem_number_of_votes);
                if(bill.votes.size() > 0)
                    numberOfVotes.setText(String.format(getResources().getString(R.string.number_of_votes), bill.votes.size()));
                else{
                    numberOfVotes.setText("");
                }
                return view;
            }
        };
        ListView listview = (ListView) view.findViewById(R.id.allBillsListView);
        listview.setOnItemClickListener(this);
        listview.setDividerHeight(10);
        listview.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BillDTO item = bills.get(position);
        Intent switchview = new Intent(getContext(), BillActivity.class);
        switchview.putExtra("bill", (Parcelable) item);
        startActivity(switchview);

    }

    @Override
    public void refreshCurrentBills(final ArrayList<BillDTO> bills) {
        this.bills.clear();
        for(BillDTO bill : bills){
            if(bill.getResume() != null && !bill.getResume().isEmpty())
                this.bills.add(bill);
        }
        adapter.notifyDataSetChanged();

        if (getView() != null)
            getView().findViewById(R.id.loadingBill).setVisibility(View.GONE);
    }

    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    @Override
    public void refreshUser(UserDTO user) {
        savedbills = user.getbillsSaved();
        System.out.println("number of savedbills userRefresh :" + savedbills.size());
        billPresenter.getSavedBills(savedbills);
    }

    private class AsyncTaskCompleteListener implements asay.asaymobile.fetch.AsyncTaskCompleteListener<JSONObject> {
        @Override
        public void onTaskComplete(JSONObject result)
        {
            try{
                String str = "";
                if (result == null){
                    etResponse.setText("Result is null");
                }
                Log.d("OnTaskComplete", "onTaskComplete: " + result);
                JSONArray articles = result.getJSONArray("value"); // get articles array
                for (int i = 0; i < articles.length(); i++){
                    BillDTO bill = new BillDTO(
                            " ",
                            articles.getJSONObject(i).getJSONObject("Periode").getString("slutdato"),
                            " ",
                            0,
                            Integer.valueOf(articles.getJSONObject(i).getString("id")),
                            articles.getJSONObject(i).getString("nummer"),
                            articles.getJSONObject(i).getString("titel"),
                            articles.getJSONObject(i).getString("titelkort"),
                            articles.getJSONObject(i).getString("resume"),
                           Integer.valueOf(articles.getJSONObject(i).getString("typeId"))
                    );
                    billPresenter.addNewBill(bill);
                }
            } catch (Exception excep){
                Log.d("JSON Exception", "onTaskComplete: " + excep.getMessage());
            }
        }

    }

    private long calcDateFromToday(String date) {
        long diffDays = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d = sdf.parse(date);
            Date today = new Date();
            long diff = d.getTime() - today.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
        } catch (ParseException ex){
            System.out.println(ex.getMessage());
        }

        return diffDays;
    }


    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<BillDTO> tempList = new ArrayList<>();

                // search content in friend list
                for (BillDTO bill : bills) {
                    if (bill.getNumber().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            bill.getTitleShort().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(bill);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = bills.size();
                filterResults.values = bills;
            }
            System.out.println(bills.size());
            System.out.println("qq: " + filterResults.values.toString());
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<BillDTO>) results.values;
            adapter.notifyDataSetChanged();
            System.out.println("n: " + filteredList.size());

        }

    }

}
