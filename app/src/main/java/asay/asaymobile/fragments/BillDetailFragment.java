package asay.asaymobile.fragments;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import asay.asaymobile.R;
import asay.asaymobile.fetch.HttpAsyncTask;
import asay.asaymobile.model.BillDTO;

import static asay.asaymobile.model.BillDTO.*;


public class BillDetailFragment extends Fragment implements OnClickListener {

    private View rootView;
    private TextView scheduleTV;
    private TextView expListTV;
    private boolean isExpanded = false;
    private String prpByTitle;
    private String committeeTitle;
    private BillDTO bill;

    public BillDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bill = getArguments().getParcelable("bill");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String urlAsString = "http://oda.ft.dk/api/Akt%C3%B8r?$filter=id%20eq%20" + bill.getActorId();
        new HttpAsyncTask(getActivity(), new AsyncTaskCompleteListener()).execute(urlAsString);

        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_bill_detail, container, false);

        // Status
        String statusTitle = getResources().getString(R.string.status) + ": ";
        TextView statusTV = rootView.findViewById(R.id.status);
        SpannableString statusTxt = new SpannableString(statusTitle + bill.getStatus());
        statusTxt.setSpan(new StyleSpan(Typeface.BOLD), 0, statusTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        statusTV.setText(statusTxt);

        // Proposed by
        prpByTitle = getResources().getString(R.string.proposed_by) + ": ";

        // Committee
        committeeTitle = getResources().getString(R.string.committee) + ": ";

        // Link
        String billID = bill.getNumber().replaceAll("\\s+", "");
        String fthtml = "http://www.ft.dk/samling/20171/lovforslag/" + billID + "/index.htm";
        String link = "<a href=\"" + fthtml + "\"> " + getResources().getString(R.string.see_bill_on_ft) + "</a>";
        System.out.println(fthtml);

        TextView linkTV = rootView.findViewById(R.id.pdf);
        linkTV.setClickable(true);
        linkTV.setMovementMethod(LinkMovementMethod.getInstance());
        linkTV.setText(Html.fromHtml(link));

        // Case steps
        StringBuilder builder = new StringBuilder();
        for (CaseStep step : bill.caseSteps) {
            builder.append(step.getTitle());
            builder.append(" ");
            builder.append(formatDateTime(step.getUpdateDate()));
            builder.append("\n");
        }

        scheduleTV = rootView.findViewById(R.id.scheduleList);
        scheduleTV.setText(builder.toString());
        scheduleTV.setMaxLines(3);
        scheduleTV.setOnClickListener(this);

        expListTV = rootView.findViewById(R.id.expandSchedule);
        expListTV.setOnClickListener(this);

        return rootView;
    }

    private String formatDateTime(String dateTime) {
        return ""
                .concat(dateTime.substring(8, 10)) //day
                .concat("/")
                .concat(dateTime.substring(5, 7)) //month
                .concat("/")
                .concat(dateTime.substring(0, 4)) //year
                .concat(" ")
                .concat(dateTime.substring(11, 16)); //time
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.scheduleList:
                if (isExpanded) {
                    collapseTextView(scheduleTV, 3);
                    expListTV.setText(getResources().getString(R.string.show_more));
                    isExpanded = false;
                } else {
                    expandTextView(scheduleTV);
                    expListTV.setText(getResources().getString(R.string.show_less));
                    isExpanded = true;
                }
                break;

            case R.id.expandSchedule:
                if (isExpanded) {
                    collapseTextView(scheduleTV, 3);
                    expListTV.setText(getResources().getString(R.string.show_more));
                    isExpanded = false;
                } else {
                    expandTextView(scheduleTV);
                    expListTV.setText(getResources().getString(R.string.show_less));
                    isExpanded = true;
                }
                break;

        }
    }

    private void expandTextView(TextView billDesc) {
        ObjectAnimator animation = ObjectAnimator.ofInt(billDesc, "maxLines", billDesc.getLineCount());
        animation.setDuration(80).start();
    }

    private void collapseTextView(TextView billDesc, int numLines) {
        ObjectAnimator animation = ObjectAnimator.ofInt(billDesc, "maxLines", numLines);
        animation.setDuration(80).start();
    }

    private class AsyncTaskCompleteListener implements asay.asaymobile.fetch.AsyncTaskCompleteListener<JSONObject> {
        @Override
        public void onTaskComplete(JSONObject result) {
            try {
                JSONArray articles = result.getJSONArray("value"); // get articles array
                for (int i = 0; i < articles.length(); i++) {
                    if (articles.getJSONObject(i).has("gruppenavnkort")) {
                        String prpBy = articles.getJSONObject(i).get("gruppenavnkort").toString();
                        TextView prpByTV = rootView.findViewById(R.id.proposedBy);
                        SpannableString prpByTxt = new SpannableString(prpByTitle + prpBy);
                        prpByTxt.setSpan(new StyleSpan(Typeface.BOLD), 0, prpByTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        prpByTV.setText(prpByTxt);
                    }

                    if (articles.getJSONObject(i).has("gruppenavnkort")) {
                        String committee = articles.getJSONObject(i).get("navn").toString();
                        TextView committeeTV = rootView.findViewById(R.id.committee);
                        SpannableString committeeTxt = new SpannableString(committeeTitle + committee);
                        committeeTxt.setSpan(new StyleSpan(Typeface.BOLD), 0, committeeTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //    committeeTxt.setSpan(new StyleSpan(Typeface.ITALIC), committeeTitle.length(), committeeTitle.length() + committee.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        committeeTV.setText(committeeTxt);
                    }
                }
            } catch (Exception e) {
                Log.d("JSON Exception", "onTaskComplete: " + e.getMessage());
            }
        }

    }
}