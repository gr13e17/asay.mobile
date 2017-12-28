package asay.asaymobile.fragments;

import android.content.Intent;
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
import asay.asaymobile.activities.BillActivity;
import asay.asaymobile.fetch.HttpAsyncTask;
import asay.asaymobile.model.ArgumentType;
import asay.asaymobile.model.BillDTO;
import asay.asaymobile.presenter.BillPresenter;
import butterknife.ButterKnife;


public class BillsAllFragment extends Fragment implements AdapterView.OnItemClickListener, BillContract.View{
    //@BindView(R.id.allBillsListView)
    EditText etResponse;
    private BillPresenter presenter;
    private ArrayList<BillDTO> bills = new ArrayList<>();
    private ArrayList<Integer> savedbills = new ArrayList<>();
    ArrayAdapter adapter;
    ListView listview;

    public BillsAllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bills_all, container, false);
        System.out.println("pre current user");
        ButterKnife.bind(this, view);

        presenter = new BillPresenter(this);
        if(getArguments() != null){
            savedbills = getArguments().getIntegerArrayList("savedBills");
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        // call AsynTask to perform network operation on separate thread
        String baseUrl = "http://oda.ft.dk/api/Sag?$orderby=id%20desc";
        String proposalExpand = "&$expand=Sagsstatus,Periode,Sagstype,SagAkt%C3%B8r,Sagstrin";
        String proposalFilter = "&$filter=(typeid%20eq%203%20or%20typeid%20eq%205)%20and%20periodeid%20eq%20146";
        String urlAsString = new StringBuilder().append(baseUrl).append(proposalExpand).append(proposalFilter).toString();
        if(savedbills.size() == 0){
            new HttpAsyncTask(getActivity(), new AsyncTaskCompleteListener()).execute(urlAsString);
        } else{
            presenter.getSavedBills(savedbills);
        }
            // get reference to the views
        adapter = new ArrayAdapter(getActivity(), R.layout.list_item_bill,R.id.listeelem_header,bills){
            @Override
            public View getView(int position, View cachedView, ViewGroup parent){
                View view = super.getView(position, cachedView, parent);
                    TextView title = view.findViewById(R.id.listeelem_header);
                    title.setText(bills.get(position).getTitleShort());
                    TextView date = view.findViewById(R.id.listeelem_date);
                    date.setText(toString().valueOf(CalcDateFromToday(bills.get(position).getDeadline())));
                return view;
            }
        };
        listview = new ListView(getActivity());
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);
        ViewGroup viewGroup = (ViewGroup) view;
        viewGroup.addView(listview);

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
        for(BillDTO bill : bills){
            this.bills.add(bill);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshBill(BillDTO bill) {

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
                            new ArrayList<BillDTO.Vote>(){{add(new BillDTO.Vote(0,"", ArgumentType.NEUTRAL ));}}
                    );
                    bills.add(bill);
                    presenter.addNewBill(bill);
                }
                adapter.notifyDataSetChanged();
            } catch (Exception excep){
                Log.d("JSON Exception", "onTaskComplete: " + excep.getMessage());
            }
        }
    }

    private long CalcDateFromToday(String date) {
        long diffDays = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sdf.parse(date);
            Date today = new Date();
            long diff = Math.abs(d.getTime() - today.getTime());
            diffDays = diff / (24 * 60 * 60 * 1000);
            System.out.println("diff days: " + diffDays);
            String formattedTime = output.format(d);
        } catch (ParseException ex){
            System.out.println(ex.getMessage());
        }

        return diffDays;
    }
}
