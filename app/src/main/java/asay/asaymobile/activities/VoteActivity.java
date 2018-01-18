package asay.asaymobile.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import asay.asaymobile.BillContract;
import asay.asaymobile.R;
import asay.asaymobile.model.ArgumentType;
import asay.asaymobile.model.BillDTO;
import asay.asaymobile.model.UserDTO;
import asay.asaymobile.presenter.BillPresenter;

public class VoteActivity extends AppCompatActivity implements BillContract.View {
    private BillDTO bill;
    private final BillDTO.Vote vote = new BillDTO.Vote();
    private TextView status;
    private Button buttonCancel;
    private UserDTO currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        bill = getIntent().getExtras().getParcelable("bill");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        }

        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelVote();
            }
        });

        findViewById(R.id.buttonAgainst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vote(ArgumentType.AGAINST);
            }
        });

        findViewById(R.id.buttonFor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vote(ArgumentType.FOR);
            }
        });

        //Set title of bill
        ((TextView) findViewById(R.id.title)).setText(String.format("%s: %s", bill.getNumber(), bill.getTitle()));

        //Set status of vote
        status = findViewById(R.id.status);
        //TODO: Set status according to registered vote on bill and user
    }

    /**
     * Shows vote registered dialog
     */
    private void vote(ArgumentType argumentType) {
        vote.setVote(argumentType);
        final BillPresenter presenter = new BillPresenter(this,false);
        new AlertDialog.Builder(this)
                .setMessage(R.string.your_vote_is_registered)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bill.addVote(vote);
                        presenter.updateBill(bill);
                        finish(); // close this activity and return to previous activity
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * Shows cancel vote dialog
     */
    private void cancelVote() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.are_you_sure_you_want_to_cancel_vote)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        status.setVisibility(View.INVISIBLE);
                        buttonCancel.setVisibility(View.INVISIBLE);
                        //TODO: Use bill.removeVote() to remove vote for user
                    }
                })
                .setNegativeButton(R.string.no, null)
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home)
            finish(); // close this activity and return to previous activity
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshCurrentBills(ArrayList<BillDTO> bills) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition( R.anim.stay, R.anim.slide_out_down);
    }

    @Override
    public void refreshEndedBills(ArrayList<BillDTO> bills){

    }
}
