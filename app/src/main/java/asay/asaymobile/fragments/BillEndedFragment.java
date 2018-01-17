package asay.asaymobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import asay.asaymobile.R;
import asay.asaymobile.model.BillDTO;

public class BillEndedFragment extends Fragment {
    private BillDTO bill;
    private double userId = 1;
    private int nFor = 0;
    private int nAgainst = 0;

    private TextView billHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_ended_bill, container, false);
        bill = getArguments().getParcelable("bill");
        Bundle bundle = new Bundle();
        bundle.putParcelable("bill", bill);
        setArguments(bundle);

        return view;
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        String billTitle = bill.getNumber().concat(": ").concat(bill.getTitleShort());

        // Actionbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(billTitle);

        // Header
        billHeader = rootView.findViewById(R.id.headerBill);
        billHeader.setText(billTitle);

        // Pie Chart
        PieChart pieChart = rootView.findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.setTransparentCircleRadius(15f);
        pieChart.setHoleRadius(15f);

        for (int i = 0; i < bill.getVotes().size(); i++) {
            if (bill.getVotes().get(i).getVote().toString().equals("For")) {
                nFor++;
            }
            if (bill.getVotes().get(i).getVote().toString().equals("Againt")) {
                nAgainst++;
            }
        }

        if (nFor == 0 && nAgainst == 0) {
            pieChart.setCenterText(getResources().getString(R.string.noVotes));
            pieChart.setTransparentCircleRadius(65f);
            pieChart.setHoleRadius(65f);
        }
        ArrayList<Entry> votes = new ArrayList<>();
        votes.add(new Entry(nFor, 0));
        votes.add(new Entry(nAgainst, 1));
        PieDataSet dataSet = new PieDataSet(votes, "");

        ArrayList<String> labels = new ArrayList<>();
        labels.add("For");
        labels.add("Imod");
        PieData data = new PieData(labels, dataSet);

        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);

        pieChart.setDescription(getResources().getString(R.string.voteResult));
        pieChart.setDrawHoleEnabled(true);

        int[] color = new int[2];
        color[0] = getResources().getColor(R.color.forColor);
        color[1] = getResources().getColor(R.color.againstColor);
        dataSet.setColors(color);

        data.setValueTextSize(16);
        data.setValueTextColor(getResources().getColor(R.color.primaryTextColor));
        pieChart.animateXY(1300, 1300);
    }

}
