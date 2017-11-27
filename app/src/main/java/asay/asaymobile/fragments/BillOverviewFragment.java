package asay.asaymobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import android.view.View.OnClickListener;
import android.widget.Button;

import asay.asaymobile.R;
import asay.asaymobile.activities.VoteActivity;

/**
 * Created by Soelberg on 31-10-2017.
 */

public class BillOverviewFragment extends Fragment implements OnClickListener{

    ToggleButton sub;
    TextView BillDesc;
    TextView arg1;
    TextView arg2;
    TextView popup;
    View Scroll;
    Button vote;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_bill_overview, container, false);


        sub = (ToggleButton) rootView.findViewById(R.id.toggleButton);

        vote = (Button) rootView.findViewById(R.id.buttonVote);
        vote.setOnClickListener(this);

        BillDesc = (TextView) rootView.findViewById(R.id.textViewAboutBill);
        BillDesc.setKeyListener(null);
        BillDesc.setOnClickListener(this);

        arg1 = (TextView) rootView.findViewById(R.id.textArg1);
        arg1.setKeyListener(null);
        arg1.setOnClickListener(this);

        arg2 = (TextView) rootView.findViewById(R.id.textArg2);
        arg2.setKeyListener(null);
        arg2.setOnClickListener(this);

        popup = (TextView) rootView.findViewById(R.id.popup);
        popup.setKeyListener(null);
        popup.setVisibility(View.INVISIBLE);
        popup.setOnClickListener(this);
        popup.setVerticalScrollBarEnabled(true);

        Scroll = (View) rootView.findViewById(R.id.RLScroll);
        Scroll.setVerticalScrollBarEnabled(true);

        return rootView;


}
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.textViewAboutBill:
                popup.setVisibility(View.VISIBLE);
                popup.setText(getResources().getString(R.string.dummy_description) + getResources().getString(R.string.dummy_description));
                popup.bringToFront();
                sub.setVisibility(View.INVISIBLE);
                break;

            case R.id.textArg1:
                popup.setVisibility(View.VISIBLE);
                popup.setText(getResources().getString(R.string.dummy_arg1));
                popup.bringToFront();
                sub.setVisibility(View.INVISIBLE);
                break;

            case R.id.textArg2:
                popup.setVisibility(View.VISIBLE);
                popup.setText(getResources().getString(R.string.dummy_arg2));
                popup.bringToFront();
                sub.setVisibility(View.INVISIBLE);
                break;

            case R.id.popup:
                popup.setVisibility(View.INVISIBLE);
                sub.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonVote:
                Intent voteIntent = new Intent(this.getActivity(), VoteActivity.class);
                startActivity(voteIntent);
                break;

            default:
                popup.setVisibility(View.INVISIBLE);
                sub.setVisibility(View.VISIBLE);
                break;
        }

    }
}

//Method to create expandable textview (not implemented in asay prototype 1.0)
//public class ExpandableTextView extends TextView implements View.OnClickListener {
//
//    private static final int MaxLines = 4;
//    private int currentMaxLines = Integer.MAX_VALUE;
//
//    public ExpandableTextView(Context text){
//        super(text);
//        setOnClickListener(this);
//    }
//
//    public ExpandableTextView(Context text, AttributeSet att, int dStyle){
//    super(text, att, dStyle);
//    setOnClickListener(this);
//    }
//
//    public ExpandableTextView(Context text, AttributeSet att){
//        super(text, att);
//        setOnClickListener(this);
//    }
//@Override
//protected void onTextChanged(CharSequence text, int start, int lengthBf, int lengthAft){
//        post(new Runnable() {
//            @Override
//            public void run() {
//                if (getLineCount()>MaxLines)
//                    setCompoundDrawablesWithIntrinsicBounds(0,0,0, R.drawable.icon_more_text;
//                else
//                    setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
//            setMaxLines(MaxLines);
//            }
//
//        });
//}
//@Override
//    public void setMaxLines(int maxLines){
//    currentMaxLines = maxLines;
//    super.setMaxLines(maxLines);
//
//}
//
//    public int getMyMaxLines()
//    {
//        return currentMaxLines;
//    }
//
//    @Override
//    public void onClick(View v)
//    {
//        /* Toggle between expanded collapsed states */
//        if (getMyMaxLines() == Integer.MAX_VALUE)
//            setMaxLines(MaxLines);
//        else
//            setMaxLines(Integer.MAX_VALUE);
//    }
//
//}
