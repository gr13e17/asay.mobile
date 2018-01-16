package asay.asaymobile.activities;

import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.MenuItem;

import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import asay.asaymobile.R;
import asay.asaymobile.model.BillDTO;


/**
 * Created by Andreas on 15-01-2018.
 */

public class BillEndedActivity extends AppCompatActivity{
    private BillDTO bill;
    private double userId = 1;
    private int nFor = 0;
    private int nAgainst = 0;

    TextView billHeader;
    TextView status;
    String statusTxt;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ended_bill);
        bill = getIntent().getParcelableExtra("bill");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.setTransparentCircleRadius(15f);
        pieChart.setHoleRadius(15f);

        for(int i = 0; i < bill.getVotes().size(); i++){
            if (bill.getVotes().get(i).getVote().toString().equals("For")){
                nFor++;
            }
            if (bill.getVotes().get(i).getVote().toString().equals("Againt")){
                nAgainst++;
            }
        }

        if(nFor == 0 && nAgainst == 0){
            pieChart.setCenterText(getResources().getString(R.string.noVotes));
            pieChart.setTransparentCircleRadius(65f);
            pieChart.setHoleRadius(65f);
        }
        ArrayList<Entry> votes = new ArrayList<Entry>();
        votes.add(new Entry(nFor, 0));
        votes.add(new Entry(nAgainst, 1));
        PieDataSet dataSet = new PieDataSet(votes, "");

        ArrayList<String> labels = new ArrayList<String>();
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

        status = findViewById(R.id.status2);
        SpannableString statusTxt = new SpannableString(getResources().getString(R.string.status) + ": " + bill.getStatus());
        statusTxt.setSpan(new StyleSpan(Typeface.BOLD), 0, getResources().getString(R.string.status).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        status.setText(statusTxt);

        billHeader = findViewById(R.id.headerBill2);
        String billTitle = bill.getNumber().concat(": ").concat(bill.getTitleShort());
        TextView header = findViewById(R.id.headerBill);
        billHeader.setText(billTitle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(billTitle);
        }

    }
    // add back arrow to toolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

}
