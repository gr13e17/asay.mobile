package asay.asaymobile.activities;

/**
 * Created by Ber on 02/11/2017.
 */

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import asay.asaymobile.BillContract;
import asay.asaymobile.R;
import asay.asaymobile.model.ArgumentType;
import asay.asaymobile.model.BillDTO;
import asay.asaymobile.presenter.BillPresenter;


public class VoteActivity extends AppCompatActivity implements View.OnClickListener, BillContract.View {

    PopupWindow votedWindow;
    PopupWindow cancelWindow;
    ConstraintLayout mainLayout;
    BillPresenter presenter;
    BillDTO bill;
    BillDTO.Vote vote = new BillDTO.Vote();
    TextView title;
    TextView voted;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        bill = getIntent().getExtras().getParcelable("bill");
        presenter = new BillPresenter(this);
        mainLayout = (ConstraintLayout) findViewById(R.id.activity_vote);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);

        //Creates 2 popup windows
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View votedView = inflater.inflate(R.layout.dialog_vote_complete, null);
        votedWindow = new PopupWindow(votedView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        votedWindow.setElevation(15);

        View cancelView = inflater.inflate(R.layout.dialog_cancel_vote, null);
        cancelWindow = new PopupWindow(cancelView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        cancelWindow.setElevation(15);

        //creates all buttons

        Button b1 = (Button) findViewById(R.id.buttonCancel);
        Button b2 = (Button) findViewById(R.id.buttonAgainst);
        Button b3 = (Button) findViewById(R.id.buttonFor);
        Button b4 = (Button) votedView.findViewById(R.id.buttonOk);
        Button b5 = (Button) cancelView.findViewById(R.id.buttonCancelNo);
        Button b6 = (Button) cancelView.findViewById(R.id.buttonCancelYes);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);

        title = (TextView) findViewById(R.id.textView2);
        ((TextView) findViewById(R.id.textView)).setText(bill.getNumber() + ": " + bill.getTitleShort());
        voted = (TextView) findViewById(R.id.argForHead);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonCancel:
                cancelWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                break;

            case R.id.buttonOk:
                votedWindow.dismiss();
                bill.addVote(vote);
                presenter.updateBill(bill);
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
                vote.setVote(ArgumentType.AGAINST);
                votedWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                break;

            case R.id.buttonFor:
                vote.setVote(ArgumentType.FOR);
                votedWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshCurrentBills(ArrayList<BillDTO> bills) {

    }
}
