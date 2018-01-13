package asay.asaymobile.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

public class BillOverviewFragment extends Fragment implements View.OnClickListener, ForumContract.View, UserContract.View {
    private TextView BillDesc;
    private String BillDescOrg;
    private TextView expBillDesc;
    private TextView arg1;
    private String arg1Org;
    private TextView expArg1;
    private TextView arg2;
    private String arg2Org;
    private TextView expArg2;
    private boolean isExpandedBillDesc = false;
    private boolean isExpandedFor = false;
    private boolean isExpandedAgainst = false;
    private BillDTO bill;
    private double userId = 1;
    private ForumPresenter presenter;
    private ImageButton sub;
    private Button vote;
    private boolean isSub = false;
    private UserDTO user;
    private UserPresenter uPresenter;

    public BillOverviewFragment() {
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
        expBillDesc = rootView.findViewById(R.id.expandBillDesc);
        expBillDesc.setOnClickListener(this);

        BillDesc = rootView.findViewById(R.id.billDesc);
        BillDescOrg = bill.getResume();
        BillDesc.setText(BillDescOrg);
        BillDesc.setOnClickListener(this);

        arg1 = rootView.findViewById(R.id.argForTxt);
        arg1.setMaxLines(3);

        arg2 = rootView.findViewById(R.id.argAgainstTxt);
        arg2.setMaxLines(3);

        expArg1 = rootView.findViewById(R.id.expandArgFor);
        expArg1.setOnClickListener(this);
        expArg1.setVisibility(View.GONE);

        expArg2 = rootView.findViewById(R.id.expandArgAgainst);
        expArg2.setOnClickListener(this);
        expArg2.setVisibility(View.GONE);

        sub = rootView.findViewById(R.id.subbtn);
        sub.setOnClickListener(this);

        uPresenter = new UserPresenter(this);
        uPresenter.getUser(userId);
        String billTitle = bill.getNumber().concat(": ").concat(bill.getTitleShort());
        TextView header = rootView.findViewById(R.id.headerBill);
        header.setText(billTitle);
        vote = rootView.findViewById(R.id.buttonVote);
        vote.setOnClickListener(this);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(billTitle);

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
        switch (v.getId()) {

            // Bill Description expanding
            case R.id.billDesc:
            case R.id.expandBillDesc:
                if (isExpandedBillDesc) {
                    collapseTextView(BillDesc, 3);
                    expBillDesc.setText(R.string.showMore);
                    isExpandedBillDesc = false;
                    BillDesc.setOnClickListener(this);
                } else {
                    expandTextView(BillDesc, BillDescOrg);
                    expBillDesc.setText(R.string.showLess);
                    isExpandedBillDesc = true;
                    BillDesc.setOnClickListener(null);
                }
                break;

            // Top Argument Against Expanding
            case R.id.argAgainstTxt:
            case R.id.expandArgAgainst:
                if (isExpandedAgainst) {
                    expArg2.setText(R.string.showMore);
                    collapseTextView(arg2, 3);
                    isExpandedAgainst = false;
                    arg2.setOnClickListener(this);
                } else {
                    expandTextView(arg2, arg2Org);
                    expArg2.setText(R.string.showLess);
                    isExpandedAgainst = true;
                    arg2.setOnClickListener(null);
                }
                break;

            // Top Argument For Expanding
            case R.id.argForTxt:
            case R.id.expandArgFor:
                if (isExpandedFor) {
                    expArg1.setText(R.string.showMore);
                    collapseTextView(arg1, 3);
                    isExpandedFor = false;
                    arg1.setOnClickListener(this);
                } else {
                    expandTextView(arg1, arg1Org);
                    expArg1.setText(R.string.showLess);
                    isExpandedFor = true;
                    arg1.setOnClickListener(null);
                }
                break;

            // Vote button
            case R.id.buttonVote:
                Intent voteIntent = new Intent(this.getActivity(), VoteActivity.class);
                voteIntent.putExtra("bill", bill);
                startActivity(voteIntent);
                break;

            // Add to favorites button
            case R.id.subbtn:
                toggleFavorite();
                break;

        }
    }

    private void addDots(TextView txt) {
        int lineEndIndex = txt.getLayout().getLineEnd(2);
        String text;
        if (txt.getText().toString().length() >= 3) {
            text = txt.getText().subSequence(0, lineEndIndex - 3) + "...";
        } else
            text = "";
        txt.setText(text);
    }

    private void expandTextView(TextView txt, String orgTxt) {
        txt.setText(orgTxt);
        ObjectAnimator animation = ObjectAnimator.ofInt(txt, "maxLines", txt.getLineCount());
        if (txt.getLineCount() > 7) {
            animation.setDuration(80).start();
        } else {
            animation.setDuration(30).start();
        }
    }

    private void collapseTextView(TextView txt, int numLines) {
        String text;
        if (txt.getText().toString().length() >= 3) {
            int lineEndIndex = txt.getLayout().getLineEnd(2);
            text = txt.getText().subSequence(0, lineEndIndex - 3) + "...";
        } else {
            text = "";
        }
        txt.setText(text);

        ObjectAnimator animation = ObjectAnimator.ofInt(txt, "maxLines", numLines);
        if (txt.getLineCount() > 7) {
            animation.setDuration(80).start();
        } else {
            animation.setDuration(30).start();
        }
    }

    @Override
    public void closeForum() {

    }

    @Override
    public void showUnloggedUserError() {

    }

    @Override
    public void refreshCurrentCommentList(ArrayList<CommentDTO> comments) {
        if (comments.size() != 0) {
            Integer maxFor = null, maxAgainst = null, positionFor = null, positionAgainst = null, counter = 0;
            for (CommentDTO comment : comments) {
                int score = comment.getScore();
                ArgumentType type = comment.getArgumentType();
                if (type.equals(ArgumentType.FOR)) {
                    if (maxFor == null || maxFor < score) {
                        maxFor = comment.getScore();
                        positionFor = counter;
                    }
                } else if (type.equals(ArgumentType.AGAINST)) {
                    if (maxAgainst == null || maxAgainst < score) {
                        maxAgainst = comment.getScore();
                        positionAgainst = counter;
                    }
                }
                counter++;
            }

            if (arg1 != null) {
                if (positionFor == null) // No arguments for
                    arg1.setText(getResources().getString(R.string.noCommentsFor));
                else {
                    arg1.setText(comments.get(positionFor).getText());
                    System.out.println("Top 1 argument FOR text: " + arg1.getText().toString());
                    arg1Org = arg1.getText().toString();
                }
            }
            if (arg2 != null) {
                if (positionAgainst == null) // No arguments against
                    arg2.setText(getResources().getString(R.string.noCommentsAgainst));
                else {
                    arg2.setText(comments.get(positionAgainst).getText());
                    System.out.println("Top 1 argument AGAINST text: " + arg1.getText().toString());
                    arg2Org = arg2.getText().toString();
                }
            }
        } else { // No comments at all
            if (arg1 != null)
                arg1.setText(R.string.noComments);
            if (arg2 != null)
                arg2.setText(R.string.noComments);
        }

        // View or hide expand text
        if (arg1 != null) {
            if (arg1.getLineCount() > 3) {
                arg1.setOnClickListener(this);
                addDots(arg1);
                expArg1.setVisibility(View.VISIBLE);
            } else {
                expArg1.setVisibility(View.GONE);
            }
        }
        if (arg2 != null) {
            if (arg2.getLineCount() > 3) {
                arg2.setOnClickListener(this);
                addDots(arg2);
                expArg2.setVisibility(View.VISIBLE);
            } else {
                expArg2.setVisibility(View.GONE);
            }
        }
    }

    private void toggleFavorite() {
        ArrayList<Integer> billSaved = user.getbillsSaved();

        if (isSub) {
            for (int i = billSaved.size() - 1; i >= 0; i--) {
                if (billSaved.get(i).equals(bill.getId()))
                    billSaved.remove(i);
            }
            sub.setImageResource(R.drawable.ic_star);
        } else {
            billSaved.add(bill.getId());
            sub.setImageResource(R.drawable.ic_star_border);
        }
        uPresenter.UpdateFavorites(userId, billSaved);
    }

    @Override
    public void refreshUser(UserDTO user) {
        System.out.println("number of bills saved: " + user.getbillsSaved().size());
        isSub = user.getbillsSaved().contains(bill.getId());
        try {
            if (isSub)
                sub.setImageResource(R.drawable.ic_star);
            else {
                sub.setImageResource(R.drawable.ic_star_border);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.user = user;
    }

    @Override
    public void refreshUsers(ArrayList<UserDTO> users) {

    }

}
