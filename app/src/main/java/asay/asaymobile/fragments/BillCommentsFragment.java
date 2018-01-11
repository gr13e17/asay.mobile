package asay.asaymobile.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import asay.asaymobile.ForumContract;
import asay.asaymobile.R;
import asay.asaymobile.UserContract;
import asay.asaymobile.activities.VoteActivity;
import asay.asaymobile.model.ArgumentType;
import asay.asaymobile.model.BillDTO;
import asay.asaymobile.model.CommentDTO;
import asay.asaymobile.model.UserDTO;
import asay.asaymobile.presenter.ForumPresenter;
import asay.asaymobile.presenter.UserPresenter;

public class BillCommentsFragment extends Fragment implements View.OnClickListener, ForumContract.View, UserContract.View{

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
    ImageButton sub;
    Button vote;
    boolean isSub = false;
    private UserDTO user;
    private UserPresenter uPresenter;

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
        super.onCreate(savedInstanceState);
        bill = getArguments().getParcelable("bill");
        Bundle bundle = new Bundle();
        bundle.putParcelable("bill", bill);
        setArguments(bundle);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_overview, container, false);


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

        sub = (ImageButton) rootView.findViewById(R.id.subbtn);
        sub.setOnClickListener(this);

        uPresenter = new UserPresenter(this);
        uPresenter.getUser(userId);
        String billTitle = bill.getNumber().concat(": ").concat(bill.getTitleShort());
        TextView header = (TextView) rootView.findViewById(R.id.headerBill);
        header.setText(billTitle);
        vote = (Button) rootView.findViewById(R.id.buttonVote);
        vote.setOnClickListener(this);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(billTitle);


        super.onViewCreated(rootView, savedInstanceState);

                BillDesc.post(new Runnable() {
            @Override
            public void run() {
                if (BillDesc.length() <= 0) {
                    BillDesc.setText(R.string.noDesc);
                }
                if (BillDesc.getLineCount() > 3) {
                   addDots(BillDesc);
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
            case R.id.buttonVote:
                Intent voteIntent = new Intent(this.getActivity(), VoteActivity.class);
                voteIntent.putExtra("bill", bill);
                startActivity(voteIntent);
                break;

            case R.id.subbtn :
                toogleFavorite(sub);
                break;

        }
    }

    private void addDots(TextView txt){
        int lineEndIndex = BillDesc.getLayout().getLineEnd(2);
        String text;
        if(BillDesc.getText().toString().length() >= 3) {
            text  = BillDesc.getText().subSequence(0, lineEndIndex - 3) + "...";
        } else
            text = "";
        txt.setText(text);
    }

    private void expandTextView(TextView billDesc, String orgTxt){
        billDesc.setText(orgTxt);
        ObjectAnimator animation = ObjectAnimator.ofInt(billDesc, "maxLines", billDesc.getLineCount());
        if(billDesc.getLineCount()>7){
        animation.setDuration(80).start();}
        else{
            animation.setDuration(30).start();
        }
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
                    addDots(arg1);
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
                    addDots(arg2);
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

    private void toogleFavorite(ImageButton button){
        ArrayList<Integer> billSaved = user.getbillsSaved();

        if(isSub){
            for(int i = billSaved.size() -1 ; i >= 0; i-- ){
                if(billSaved.get(i).equals(bill.getId()))
                    billSaved.remove(i);
            }
            sub.setImageResource(R.drawable.ic_star);
        }else {
            billSaved.add(bill.getId());
            sub.setImageResource(R.drawable.ic_star_border);
        }
        uPresenter.UpdateFavorites(userId,billSaved);
    }

    @Override
    public void refreshUser(UserDTO user) {
        System.out.println("number of bills saved: " + user.getbillsSaved().size());
        isSub = user.getbillsSaved().contains(bill.getId());
        try{
            if (isSub)
                sub.setImageResource(R.drawable.ic_star);
            else{
                sub.setImageResource(R.drawable.ic_star_border);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        this.user = user;
    }

    @Override
    public void refreshUsers(ArrayList<UserDTO> users) {

    }


}
