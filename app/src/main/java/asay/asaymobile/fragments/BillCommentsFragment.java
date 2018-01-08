package asay.asaymobile.fragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import asay.asaymobile.ForumContract;
import asay.asaymobile.R;
import asay.asaymobile.model.ArgumentType;
import asay.asaymobile.model.BillDTO;
import asay.asaymobile.model.CommentDTO;
import asay.asaymobile.model.UserDTO;
import asay.asaymobile.presenter.ForumPresenter;

public class BillCommentsFragment extends Fragment implements View.OnClickListener, ForumContract.View{

    TextView BillDesc;
    String BillDescOrg;
    TextView expBillDesc;
    TextView arg1;
    String arg1Org;
    TextView expArg1;
    TextView arg2;
    String arg2Org;
    TextView expArg2;
    boolean isExpandedBillDesc  = false;
    boolean isExpandedFor  = false;
    boolean isExpandedAgainst  = false;
    private BillDTO bill;
    private double userId = 1;
    ForumPresenter presenter;

    public BillCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bill = getArguments().getParcelable("bill");
        presenter = new ForumPresenter(this, bill.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_comments, container, false);
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {
        expBillDesc = (TextView) rootView.findViewById(R.id.expandBillDesc);
        expBillDesc.setOnClickListener(this);

        BillDesc = (TextView) rootView.findViewById(R.id.billDesc);
        BillDescOrg = bill.getResume();
        BillDesc.setText(BillDescOrg);
        BillDesc.setOnClickListener(this);

        arg1 = (TextView) rootView.findViewById(R.id.argForTxt);
       arg1.setMaxLines(3);

        arg2 = (TextView) rootView.findViewById(R.id.argAgainstTxt);
       arg2.setMaxLines(3);

        expArg1 = (TextView) rootView.findViewById(R.id.expandArgFor);
        expArg1.setOnClickListener(this);
        expArg1.setVisibility(View.INVISIBLE);

        expArg2 = (TextView) rootView.findViewById(R.id.expandArgAgainst);
        expArg2.setOnClickListener(this);
        expArg2.setVisibility(View.INVISIBLE);

        super.onViewCreated(rootView, savedInstanceState);

                BillDesc.post(new Runnable() {
            @Override
            public void run() {
                if (BillDesc.length() <= 0) {
                    BillDesc.setText(R.string.noDesc);
                }
                if (BillDesc.getLineCount() > 3) {
                    expBillDesc.setVisibility(View.VISIBLE);
                } else
                    expBillDesc.setVisibility(View.INVISIBLE);
                BillDesc.setOnClickListener(null);
                BillDesc.setMaxLines(3);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            //BIll Description expanding
            case R.id.billDesc :
            case R.id.expandBillDesc :
                if(isExpandedBillDesc){
                    collapseTextView(BillDesc, 3);
                    expBillDesc.setText(R.string.showMore);
                    isExpandedBillDesc = false;
                    BillDesc.setOnClickListener(this);
                }
                else {
                    expandTextView(BillDesc, BillDescOrg);
                    expBillDesc.setText(R.string.showLess);
                    isExpandedBillDesc = true;
                    BillDesc.setOnClickListener(null);
                }
                break;

            // Top Argument Against Expanding
            case R.id.argAgainstTxt :
            case R.id.expandArgAgainst :
                if(isExpandedAgainst){
                    expArg2.setText(R.string.showMore);
                    collapseTextView(arg2, 3);
                    isExpandedAgainst = false;
                    arg2.setOnClickListener(this);
                }
                else {
                    expandTextView(arg2, arg2Org);
                    expArg2.setText(R.string.showLess);
                    isExpandedAgainst = true;
                    arg2.setOnClickListener(null);
                }
                break;

            // Top Argument For Expanding
            case R.id.argForTxt :
            case R.id.expandArgFor :

                if(isExpandedFor){
                    expArg1.setText(R.string.showMore);
                    collapseTextView(arg1, 3);
                    isExpandedFor = false;
                    arg1.setOnClickListener(this);
                }
                else {
                    expandTextView(arg1, arg1Org);
                    expArg1.setText(R.string.showLess);
                    isExpandedFor = true;
                    arg1.setOnClickListener(null);
                }
                break;
        }
    }
    private void expandTextView(TextView billDesc, String orgTxt){
        billDesc.setText(orgTxt);
        ObjectAnimator animation = ObjectAnimator.ofInt(billDesc, "maxLines", billDesc.getLineCount());
        animation.setDuration(80).start();
    }

    private void collapseTextView(TextView txt, int numLines){

        int lineEndIndex = txt.getLayout().getLineEnd(2);
        String text;
        if(txt.getText().toString().length() >= 3) {
          text  = txt.getText().subSequence(0, lineEndIndex - 3) + "...";
        } else
            text = "";
        txt.setText(text);
        ObjectAnimator animation = ObjectAnimator.ofInt(txt, "maxLines", numLines);
        animation.setDuration(80).start();
    }

    @Override
    public void closeForum() {

    }

    @Override
    public void showUnloggedUserError() {

    }

    @Override
    public void refreshCurrentCommentList(ArrayList<CommentDTO> currentComment) {
        if(currentComment.size() != 0){
            int maxFor = 0, maxAgainst = 0, positionFor = 0, positionAgainst = 0, counter = 0;
            for(CommentDTO comment : currentComment){
                if(maxFor < comment.getScore() && comment.getArgumentType().equals(ArgumentType.FOR)){
                    maxFor = comment.getScore();
                    positionFor = counter;
                } else if (maxAgainst < comment.getScore() && comment.getArgumentType().equals(ArgumentType.AGAINST)) {
                    maxAgainst = comment.getScore();
                    positionAgainst = counter;
                }
                counter++;
            }

            if (arg1 != null) {
                arg1.setText(currentComment.get(positionFor).getText());
                arg1Org = arg1.getText().toString();

                if (arg1.getLineCount() > 3) {
                    arg1.setOnClickListener(this);
                    expArg1.setVisibility(View.VISIBLE);
                } else {
                    expArg1.setVisibility(View.INVISIBLE);
                }
            }
            if (arg2 != null) {
                arg2.setText(currentComment.get(positionAgainst).getText());
                arg2Org = arg2.getText().toString();

                if (arg2.getLineCount() > 3) {
                    arg2.setOnClickListener(this);
                    expArg2.setVisibility(View.VISIBLE);
                } else {
                    expArg2.setVisibility(View.INVISIBLE);
                }
            }
        } else{
            if (arg1 != null)
                arg1.setText(R.string.noComments);
            if (arg2 != null)
                arg2.setText(R.string.noComments);
            if (expArg1 != null)
                expArg1.setVisibility(View.INVISIBLE);
            if (expArg2 != null)
                expArg2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void refreshUsers(ArrayList<UserDTO> users) {

    }
}
