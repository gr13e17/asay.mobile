package asay.asaymobile.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Button;
import asay.asaymobile.R;
import asay.asaymobile.activities.VoteActivity;

import static android.text.TextUtils.TruncateAt.END;

/**
 * Created by Soelberg on 31-10-2017.
 */

public class BillOverviewFragment extends Fragment implements OnClickListener{


    ImageButton sub;
    TextView BillDesc;
    int BillDescOrg;
    TextView expBillDesc;
    TextView arg1;
    int arg1Org;
    TextView expArg1;
    TextView arg2;
    int arg2Org;
    TextView expArg2;
    TextView popup;
    View Scroll;
    Button vote;
    boolean isSub = false;
    boolean isExpandedBillDesc  = false;
    boolean isExpandedFor  = false;
    boolean isExpandedAgainst  = false;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_bill_overview, container, false);

        sub = (ImageButton) rootView.findViewById(R.id.subbtn);
        sub.setOnClickListener(this);

        vote = (Button) rootView.findViewById(R.id.buttonVote);
        vote.setOnClickListener(this);

        BillDesc = (TextView) rootView.findViewById(R.id.billDesc);
        BillDescOrg = R.string.dummy_description_short;
        BillDesc.setText(BillDescOrg);
        BillDesc.setOnClickListener(this);
        BillDesc.setMaxLines(3);



        arg1 = (TextView) rootView.findViewById(R.id.argForTxt);
        arg1Org = R.string.dummy_arg2;
        arg1.setText(arg1Org);
        arg1.setOnClickListener(this);
        arg1.setMaxLines(3);



        arg2 = (TextView) rootView.findViewById(R.id.argAgainstTxt);
        arg2Org = R.string.dummy_arg1;
        arg2.setText(arg2Org);
        arg2.setOnClickListener(this);
        arg2.setMaxLines(3);

        expBillDesc = (TextView) rootView.findViewById(R.id.expandBillDesc);
        expBillDesc.setOnClickListener(this);
        expArg1 = (TextView) rootView.findViewById(R.id.expandArgFor);
        expArg1.setOnClickListener(this);
        expArg2 = (TextView) rootView.findViewById(R.id.expandArgAgainst);
        expArg2.setOnClickListener(this);

        return rootView;
}

    @Override
    public void onClick(View v){
        switch(v.getId()){

            case R.id.buttonVote:
                Intent voteIntent = new Intent(this.getActivity(), VoteActivity.class);
                startActivity(voteIntent);
                break;

            case R.id.subbtn :
               if(isSub == false){
                    sub.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                   isSub = true;
                   break;
                }
                if(isSub == true) {
                   sub.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    isSub = false;
                    break;
                }

                //BIll Description expanding
            case R.id.billDesc :
            case R.id.expandBillDesc :
                if(isExpandedBillDesc == true){
                    collapseTextView(BillDesc, 3);
                    expBillDesc.setText("Se mere");
                    isExpandedBillDesc = false;
                }
                else {
                    expandTextView(BillDesc, BillDescOrg);
                    expBillDesc.setText("Se mindre");
                    isExpandedBillDesc = true;
                }
                break;

                // Top Argument Against Expanding
            case R.id.argAgainstTxt :
            case R.id.expandArgAgainst :
                if(isExpandedAgainst == true){
                    expArg2.setText("Se mere");
                    collapseTextView(arg2, 3);
                    isExpandedAgainst = false;
                }
                else {
                    expandTextView(arg2, arg2Org);
                    expArg2.setText("Se mindre");
                    isExpandedAgainst = true;
                }
                break;

             // Top Argument For Expanding
            case R.id.argForTxt :
            case R.id.expandArgFor :
                if(isExpandedAgainst == true){
                    expArg1.setText("Se mere");
                    collapseTextView(arg1, 3);
                    isExpandedAgainst = false;
                }
                else {
                    expandTextView(arg1, arg1Org);
                    expArg1.setText("Se mindre");
                    isExpandedAgainst = true;
                }
                break;
        }

    }

    private void expandTextView(TextView billDesc, int orgTxt){

       billDesc.setText(orgTxt);
        ObjectAnimator animation = ObjectAnimator.ofInt(billDesc, "maxLines", billDesc.getLineCount());
        animation.setDuration(80).start();

    }

    private void collapseTextView(TextView billDesc, int numLines){

        int lineEndIndex = billDesc.getLayout().getLineEnd(2);
        String text = billDesc.getText().subSequence(0, lineEndIndex - 3) + "...";
        billDesc.setText(text);
        ObjectAnimator animation = ObjectAnimator.ofInt(billDesc, "maxLines", numLines);
        animation.setDuration(80).start();
    }
}


