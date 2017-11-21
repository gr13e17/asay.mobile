package asay.asaymobile.activities;

/**
 * Created by Ber on 02/11/2017.
 */

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import asay.asaymobile.R;


public class VoteActivity extends AppCompatActivity implements View.OnClickListener {

    PopupWindow votedWindow;
    PopupWindow cancelWindow;
    ConstraintLayout mainLayout;
    TextView title;
    TextView voted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        mainLayout = (ConstraintLayout) findViewById(R.id.activity_stemmeboks);

        //Creates 2 popup windows

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View votedView = inflater.inflate(R.layout.dialog_vote_complete, null);

        votedWindow = new PopupWindow(votedView, 800, 450, true);
        votedWindow.setElevation(15);

        View cancelView = inflater.inflate(R.layout.dialog_cancel_vote, null);

        cancelWindow = new PopupWindow(cancelView, 800, 450, true);
        cancelWindow.setElevation(15);

        //creates all buttons

        Button b1 = (Button) findViewById(R.id.buttonCancel);
        Button b2 = (Button) findViewById(R.id.buttonAgainst);
        Button b3 = (Button) findViewById(R.id.buttonFor);
        Button b4 = (Button) votedView.findViewById(R.id.buttonVoted);
        Button b5 = (Button) cancelView.findViewById(R.id.buttonCancelNo);
        Button b6 = (Button) cancelView.findViewById(R.id.buttonCancelYes);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);

        title = (TextView) findViewById(R.id.textView2);
        voted = (TextView) findViewById(R.id.textView3);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonCancel:
                cancelWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                break;

            case R.id.buttonVoted:
                votedWindow.dismiss();
                finish();
                break;

            case R.id.buttonCancelYes:
                cancelWindow.dismiss();
                voted.setVisibility(view.INVISIBLE);
                break;

            case R.id.buttonCancelNo:
                cancelWindow.dismiss();
                break;

            case R.id.buttonAgainst:
                votedWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                break;

            case R.id.buttonFor:
                votedWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                break;

            default:
                break;
        }
    }
}
