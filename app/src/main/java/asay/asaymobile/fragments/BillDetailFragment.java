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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import asay.asaymobile.R;


public class BillDetailFragment extends Fragment implements OnClickListener {

    TextView status;
    TextView prpBy;
    TextView committee;
    TextView ministry;
    TextView pdf;
    TextView schedule;
    TextView expList;
    boolean isExpanded = false;
    String dummy1 = "Miljø- og fødevareminister Esben Lunde Larsen";
    String dummy1Bold = "Fremsat af: ";

    String dummy2 = "Miljø- og Fødevareudvalget";
    String dummy2Bold = "Udvalg: ";

    String dummy3 = "Betænkning afgivet";
    String dummy3Bold = "Status: ";

    String dummy4 = "Miljø- og Fødevareministeriet";
    String dummy4Bold = "Ministerområde: ";

    String pdfdummy = "<a href='http://www.folketingstidende.dk/RIpdf/samling/20171/lovforslag/L72/20171_L72_fremsaettelsestale.pdf'> Se fulde Lovforslag</a>";

    public BillDetailFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_bill_detail, container, false);

        status = (TextView) rootView.findViewById(R.id.status);
        SpannableString statusTxt = new SpannableString(dummy3Bold + dummy3);
        statusTxt.setSpan(new StyleSpan(Typeface.BOLD), 0, dummy3Bold.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        status.setText(statusTxt);

        prpBy = (TextView) rootView.findViewById(R.id.proposedBy);
        SpannableString prpByTxt = new SpannableString(dummy1Bold + dummy1);
        prpByTxt.setSpan(new StyleSpan(Typeface.BOLD), 0, dummy1Bold.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        prpBy.setText(prpByTxt);

        committee = (TextView) rootView.findViewById(R.id.committee);
        SpannableString committeeTxt = new SpannableString(dummy2Bold + dummy2);
        committeeTxt.setSpan(new StyleSpan(Typeface.BOLD), 0, dummy2Bold.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //    committeeTxt.setSpan(new StyleSpan(Typeface.ITALIC), dummy2Bold.length(), dummy2Bold.length() + dummy2.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        committee.setText(committeeTxt);

        ministry = (TextView) rootView.findViewById(R.id.ministry);
        SpannableString ministryTxt = new SpannableString(dummy4Bold + dummy4);
        ministryTxt.setSpan(new StyleSpan(Typeface.BOLD), 0, dummy4Bold.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //   ministryTxt.setSpan(new StyleSpan(Typeface.ITALIC), dummy4Bold.length(), dummy4Bold.length() + dummy4.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ministry.setText(ministryTxt);


        schedule = (TextView) rootView.findViewById(R.id.scheduleList);
        schedule.setText(R.string.billSchedule);
        schedule.setMaxLines(3);
        schedule.setOnClickListener(this);

        expList = (TextView) rootView.findViewById(R.id.expandSchedule);
        expList.setOnClickListener(this);

        pdf = (TextView) rootView.findViewById(R.id.pdf);
        pdf.setClickable(true);
        pdf.setMovementMethod(LinkMovementMethod.getInstance());
        pdf.setText(Html.fromHtml(pdfdummy));

        return rootView;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.scheduleList:
                if (isExpanded == true) {
                    collapseTextView(schedule, 3);
                    expList.setText("Se mere");
                    isExpanded = false;
                } else {
                    expandTextView(schedule);
                    expList.setText("Se mindre");
                    isExpanded = true;
                }

                break;

            case R.id.expandSchedule:
                if (isExpanded == true) {
                    collapseTextView(schedule, 3);
                    expList.setText("Se mere");
                    isExpanded = false;
                } else {
                    expandTextView(schedule);
                    expList.setText("Se mindre");
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
}