package asay.asaymobile.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import asay.asaymobile.R;
import asay.asaymobile.UserContract;
import asay.asaymobile.activities.VoteActivity;
import asay.asaymobile.model.BillDTO;
import asay.asaymobile.model.UserDTO;
import asay.asaymobile.presenter.UserPresenter;

/**
 * Created by Soelberg on 31-10-2017.
 */

public class BillOverviewFragment extends Fragment implements OnClickListener,UserContract.View{

    ImageButton sub;
    TextView BillDesc;
    String BillDescOrg;
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
    private BillDTO bill;
    private double userId = 1;
    private UserDTO user;
    private UserPresenter presenter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_bill_overview, container, false);

        bill = getArguments().getParcelable("bill");
        presenter = new UserPresenter(this, userId);
        return rootView;
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {



        sub = (ImageButton) rootView.findViewById(R.id.subbtn);
        sub.setOnClickListener(this);


        TextView header = (TextView) rootView.findViewById(R.id.headerBill);
        header.setText(bill.getNumber().concat(": ").concat(bill.getTitleShort()));
        vote = (Button) rootView.findViewById(R.id.buttonVote);
        vote.setOnClickListener(this);

        BillDesc = (TextView) rootView.findViewById(R.id.billDesc);
        BillDescOrg = bill.getResume();
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
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){

            case R.id.buttonVote:
                Intent voteIntent = new Intent(this.getActivity(), VoteActivity.class);
                startActivity(voteIntent);
                break;

            case R.id.subbtn :
                toogleFavorite(sub);
                break;
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
    private void expandTextView(TextView billDesc, String orgTxt){
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

    private void toogleFavorite(ImageButton button){
        ArrayList<Integer> billSaved = user.getbillsSaved();

        if(isSub){
            for(int i = billSaved.size() -1 ; i >= 0; i-- ){
                if(billSaved.get(i).equals(bill.getId()))
                    billSaved.remove(i);
            }
            presenter.UpdateFavorites(userId, billSaved);
            sub.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
        }else {
            billSaved.add(bill.getId());
            presenter.UpdateFavorites(userId,billSaved);
            sub.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
        }
    }

    @Override
    public void refreshUser(UserDTO user) {
        isSub = user.getbillsSaved().contains(bill.getId());
        this.user = user;
    }
}



