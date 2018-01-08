package asay.asaymobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import butterknife.ButterKnife;

/**
 * Created by Soelberg on 31-10-2017.
 */

public class BillOverviewFragment extends Fragment implements OnClickListener,UserContract.View{

    ImageButton sub;
    View Scroll;
    Button vote;
    boolean isSub = false;
    private BillDTO bill;
    private double userId = 1;
    private UserDTO user;
    private UserPresenter presenter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_bill_overview, container, false);
        ButterKnife.bind(this, rootView);
        bill = getArguments().getParcelable("bill");
        String childView = getArguments().getString("view");
        Bundle bundle = new Bundle();
        bundle.putParcelable("bill", bill);
        if(childView.equals("details")){
            BillDetailFragment details = new BillDetailFragment();
            details.setArguments(bundle);
            addChildFragment(details);
        } else if (childView.equals("comments")){
            BillCommentsFragment comments = new BillCommentsFragment();
            comments.setArguments(bundle);
            addChildFragment(comments);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        sub = (ImageButton) rootView.findViewById(R.id.subbtn);
        sub.setOnClickListener(this);
        presenter = new UserPresenter(this);
        presenter.getUser(userId);
        String billTitle = bill.getNumber().concat(": ").concat(bill.getTitleShort());
        TextView header = (TextView) rootView.findViewById(R.id.headerBill);
        header.setText(billTitle);
        vote = (Button) rootView.findViewById(R.id.buttonVote);
        vote.setOnClickListener(this);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(billTitle);
    }

    public void addChildFragment(Fragment fragment){
        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        childFragTrans.add(R.id.fragment_container, fragment);
        childFragTrans.addToBackStack("childFragment");
        childFragTrans.commit();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){

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
        presenter.UpdateFavorites(userId,billSaved);
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
}



