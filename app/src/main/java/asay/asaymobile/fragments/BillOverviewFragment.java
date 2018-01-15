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
    private View rootView;
    private TextView billDesc;
    private String billDescFull;
    private TextView expandBillDesc;
    private TextView argForTextView;
    private String argForFull;
    private TextView expandArgFor;
    private TextView argAgainstTextView;
    private String argAgainstFull;
    private TextView expandArgAgainst;
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
    private ArrayList<UserDTO> users;

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
        super.onViewCreated(rootView, savedInstanceState);
        this.rootView = rootView;

        // Header
        uPresenter = new UserPresenter(this);
        uPresenter.getUser(userId);
        String billTitle = bill.getNumber().concat(": ").concat(bill.getTitleShort());
        TextView header = rootView.findViewById(R.id.headerBill);
        header.setText(billTitle);

        // Actionbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(billTitle);

        // Bill description
        billDesc = rootView.findViewById(R.id.billDesc);
        billDescFull = bill.getResume();
        billDesc.setText(billDescFull);
        billDesc.setOnClickListener(this);
        expandBillDesc = rootView.findViewById(R.id.expandBillDesc);
        expandBillDesc.setOnClickListener(this);
        billDesc.post(new Runnable() {
            @Override
            public void run() {
                if (billDesc.length() <= 0) {
                    billDesc.setText(R.string.there_is_no_description_for_this_bill);
                }
                if (billDesc.getLineCount() > 3) {
                    addDots(billDesc);
                    expandBillDesc.setVisibility(View.VISIBLE);

                } else
                    expandBillDesc.setVisibility(View.INVISIBLE);
                billDesc.setOnClickListener(null);
                billDesc.setMaxLines(3);
            }
        });

        // Argument for
        View argForView = rootView.findViewById(R.id.comment_for);
        argForTextView = argForView.findViewById(R.id.comment_text);
        argForTextView.setMaxLines(3);
        expandArgFor = rootView.findViewById(R.id.expand_for);
        expandArgFor.setOnClickListener(this);
        expandArgFor.setVisibility(View.GONE);
        //Header background color based on ArgumentType
        View argForHeader = argForView.findViewById(R.id.header);
        argForHeader.setBackground(getResources().getDrawable(R.drawable.round_header_for));

        // Argument against
        View argAgainstView = rootView.findViewById(R.id.comment_against);
        argAgainstTextView = argAgainstView.findViewById(R.id.comment_text);
        argAgainstTextView.setMaxLines(3);
        expandArgAgainst = rootView.findViewById(R.id.expand_against);
        expandArgAgainst.setOnClickListener(this);
        expandArgAgainst.setVisibility(View.GONE);
        //Header background color based on ArgumentType
        View argAgainstHeader = argAgainstView.findViewById(R.id.header);
        argAgainstHeader.setBackground(getResources().getDrawable(R.drawable.round_header_against));

        // Add to favorites button
        sub = rootView.findViewById(R.id.subbtn);
        sub.setOnClickListener(this);

        // Vote button
        vote = rootView.findViewById(R.id.buttonVote);
        vote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == billDesc || v == expandBillDesc) {
            if (isExpandedBillDesc) {
                collapseTextView(billDesc, 3);
                expandBillDesc.setText(R.string.show_more);
                isExpandedBillDesc = false;
                billDesc.setOnClickListener(this);
            } else {
                expandTextView(billDesc, billDescFull);
                expandBillDesc.setText(R.string.show_less);
                isExpandedBillDesc = true;
                billDesc.setOnClickListener(null);
            }
        } else if (v == argForTextView || v == expandArgFor) {
            if (isExpandedFor) {
                expandArgFor.setText(R.string.show_more);
                collapseTextView(argForTextView, 3);
                isExpandedFor = false;
                argForTextView.setOnClickListener(this);
            } else {
                expandTextView(argForTextView, argForFull);
                expandArgFor.setText(R.string.show_less);
                isExpandedFor = true;
                argForTextView.setOnClickListener(null);
            }
        } else if (v == argAgainstTextView || v == expandArgAgainst) {
            if (isExpandedAgainst) {
                expandArgAgainst.setText(R.string.show_more);
                collapseTextView(argAgainstTextView, 3);
                isExpandedAgainst = false;
                argAgainstTextView.setOnClickListener(this);
            } else {
                expandTextView(argAgainstTextView, argAgainstFull);
                expandArgAgainst.setText(R.string.show_less);
                isExpandedAgainst = true;
                argAgainstTextView.setOnClickListener(null);
            }
        } else if (v.getId() == R.id.buttonVote) {
            Intent voteIntent = new Intent(this.getActivity(), VoteActivity.class);
            voteIntent.putExtra("bill", bill);
            startActivity(voteIntent);
            getActivity().overridePendingTransition( R.anim.slide_in_up, R.anim.stay );
        } else if (v.getId() == R.id.subbtn) {
            toggleFavorite();
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
        if (rootView != null) {
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

                if (positionFor == null) { // No arguments for
                    rootView.findViewById(R.id.argForContext).setVisibility(View.INVISIBLE);
                    rootView.findViewById(R.id.noArgForText).setVisibility(View.VISIBLE);
                } else {
                    CommentDTO commentFor = comments.get(positionFor);
                    View commentView = rootView.findViewById(R.id.comment_for);

                    //Hide "No arguments for" and show comment
                    rootView.findViewById(R.id.argForContext).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.noArgForText).setVisibility(View.INVISIBLE);

                    argForFull = commentFor.getText();
                    argForTextView.setText(argForFull);
                    System.out.println("Top 1 argument FOR text: " + argForFull);

                    //Set name text
                    TextView nameView = commentView.findViewById(R.id.nameView);
                    for (UserDTO user : users) {
                        if (user.getid() == commentFor.getUserid()) {
                            nameView.setText(user.getname());
                            break;
                        }
                    }

                    //Set datetime text
                    if (commentFor.getDateTime() != null) {
                        TextView dateTextView = commentView.findViewById(R.id.dateTextView);
                        dateTextView.setText(commentFor.getDateTime());
                    }
                }
                if (positionAgainst == null) { // No arguments against
                    rootView.findViewById(R.id.argAgainstContext).setVisibility(View.INVISIBLE);
                    rootView.findViewById(R.id.noArgAgainstText).setVisibility(View.VISIBLE);
                } else {
                    CommentDTO commentAgainst = comments.get(positionAgainst);
                    View commentView = rootView.findViewById(R.id.comment_against);

                    //Hide "No arguments against" and show comment
                    rootView.findViewById(R.id.argAgainstContext).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.noArgAgainstText).setVisibility(View.INVISIBLE);

                    argAgainstFull = commentAgainst.getText();
                    argAgainstTextView.setText(argAgainstFull);
                    System.out.println("Top 1 argument AGAINST text: " + argAgainstFull);

                    //Set name text
                    TextView nameView = commentView.findViewById(R.id.nameView);
                    for (UserDTO user : users) {
                        if (user.getid() == commentAgainst.getUserid()) {
                            nameView.setText(user.getname());
                            break;
                        }
                    }

                    //Set datetime text
                    if (commentAgainst.getDateTime() != null) {
                        TextView dateTextView = commentView.findViewById(R.id.dateTextView);
                        dateTextView.setText(commentAgainst.getDateTime());
                    }
                }
            } else { // No comments at all
                if (rootView != null) {
                    rootView.findViewById(R.id.argForContext).setVisibility(View.INVISIBLE);
                    rootView.findViewById(R.id.noArgForText).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.argAgainstContext).setVisibility(View.INVISIBLE);
                    rootView.findViewById(R.id.noArgAgainstText).setVisibility(View.VISIBLE);
                }
            }

            // View or hide expand text
            if (argForTextView.getLineCount() > 3) {
                argForTextView.setOnClickListener(this);
                addDots(argForTextView);
                expandArgFor.setVisibility(View.VISIBLE);
            } else {
                expandArgFor.setVisibility(View.GONE);
            }
            if (argAgainstTextView.getLineCount() > 3) {
                argAgainstTextView.setOnClickListener(this);
                addDots(argAgainstTextView);
                expandArgAgainst.setVisibility(View.VISIBLE);
            } else {
                expandArgAgainst.setVisibility(View.GONE);
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
        this.users = users;
    }

}
