package asay.asaymobile.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import asay.asaymobile.ForumContract;
import asay.asaymobile.R;
import asay.asaymobile.model.ArgumentType;
import asay.asaymobile.model.CommentDTO;
import asay.asaymobile.model.UserDTO;
import asay.asaymobile.presenter.ForumPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Soelberg on 31-10-2017.
 */

public class BillForumFragment extends Fragment implements ForumContract.View, View.OnClickListener, WriteCommentDialog.WriteCommentListener {
    @BindView(R.id.forum_list_view)
    ListView listView;
    private int billId;
    private ForumPresenter forumPresenter;
    private ArrayList<UserDTO> nameArray = new ArrayList<>(); //contains names of the one who wrote the comment. must be populated from database
    ArrayAdapter arrayAdapter;
    UserDTO userDTO;
    private View rootView;
    private FloatingActionButton commentButtonMain;
    private WriteCommentDialog writeCommentDialog;
    String dateTime;
    CommentDTO newComment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_bill_forum, container, false);
        billId = getArguments().getInt("billId");
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        // Inflate the layout for this fragment
        // call AsyncTask to perform network operation on separate thread
        forumPresenter = new ForumPresenter(this, billId);
        // get reference to the views

        commentButtonMain = this.rootView.findViewById(R.id.commentButtonMain);
        commentButtonMain.setOnClickListener(this);
    }


    @Override
    public void closeForum() {
    }

    @Override
    public void showUnloggedUserError() {

    }

    @Override
    public void refreshCurrentCommentList(final ArrayList<CommentDTO> currentComments) {
        List<CommentDTO> threadedComments = toThreadedComments(currentComments);
        ForumAdapter commentArrayAdapter = new ForumAdapter((ArrayList<CommentDTO>) threadedComments, nameArray, getContext(), forumPresenter);
        if (listView.getAdapter() == null || newComment != null) {
            listView.setAdapter(commentArrayAdapter);
            newComment = null;
        }
    }

    @Override
    public void refreshUsers(ArrayList<UserDTO> users) {
        this.nameArray = users;
    }

    private void openWriteCommentDialog(View v, final Double parentId) {
        writeCommentDialog = WriteCommentDialog.newInstance(parentId);
        writeCommentDialog.setCancelable(false);
        writeCommentDialog.show(BillForumFragment.this.getChildFragmentManager(), "writeComment");
    }

    @Override
    public void onClick(View view) {
        if (view == commentButtonMain) {
            openWriteCommentDialog(rootView, 0.0);
        }
    }

    private List<CommentDTO> toThreadedComments(List<CommentDTO> comments) {

        //comments should be sorted first
        Collections.sort(comments);

        //The resulting array of threaded comments
        List<CommentDTO> threaded = new ArrayList<>();

        //An array used to hold processed comments which should be removed at the end of the cycle
        List<CommentDTO> removeComments = new ArrayList<>();

        //get the root comments first (comments with no parent)
        for (int i = 0; i < comments.size(); i++) {
            CommentDTO c = comments.get(i);
            if (c.getParrentId() == 0) {
                c.setCommentDepth(0); //A property of Comment to hold its depth
                c.setChildrentCount(0); //A property of Comment to hold its child count
                threaded.add(c);
                removeComments.add(c);
            }
        }

        if (removeComments.size() > 0) {
            //clear processed comments
            comments.removeAll(removeComments);
            removeComments.clear();
        }

        int depth = 0;
        //get the child comments up to a max depth of 10
        while (comments.size() > 0 && depth <= 10) {
            depth++;
            for (int j = 0; j < comments.size(); j++) {
                CommentDTO child = comments.get(j);
                //check root comments for match
                for (int i = 0; i < threaded.size(); i++) {
                    CommentDTO parent = threaded.get(i);
                    if (parent.getId() == child.getParrentId()) {
                        parent.setChildrentCount(parent.getChildrentCount() + 1);
                        child.setCommentDepth(depth + parent.getCommentDepth());
                        threaded.add((int) (i + parent.getChildrentCount()), child);
                        removeComments.add(child);
                    }
                }
            }
            if (removeComments.size() > 0) {
                //clear processed comments
                comments.removeAll(removeComments);
                removeComments.clear();
            }
        }

        return threaded;
    }

    @Override
    public void onSave(String commentContent, Double parentId, ArgumentType argumentType) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy  HH:mm");
        dateTime = format.format(calendar.getTime());
        CommentDTO comment = new CommentDTO(
                argumentType,
                billId,
                0,
                0,
                commentContent,
                1,
                parentId,
                0,
                0,
                dateTime
        );
        forumPresenter.addNewComment(comment);
        writeCommentDialog.dismiss();
        newComment = comment;
    }


    public class ForumAdapter extends BaseAdapter {
        final ArrayList<CommentDTO> currentComments;
        final ArrayList<UserDTO> currentUsers;
        final Context context;
        final ForumPresenter presenter;

        ForumAdapter(ArrayList<CommentDTO> currentComments, ArrayList<UserDTO> currentUsers, Context context, ForumPresenter presenter) {
            this.currentComments = currentComments;
            this.currentUsers = currentUsers;
            this.context = context;
            this.presenter = presenter;
        }

        private Drawable getBackground(ArgumentType argumentType) {
            switch (argumentType) {
                case FOR:
                    return context.getResources().getDrawable(R.drawable.round_header_for);
                case AGAINST:
                    return context.getResources().getDrawable(R.drawable.round_header_against);
                case NEUTRAL:
                    return context.getResources().getDrawable(R.drawable.round_header_neutral);
                default:
                    return null;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.list_item_comment, parent, false);
            }
            final CommentDTO currentComment = currentComments.get(position);

            //Padding for threaded comments
            View view2 = new View(getActivity());
            ConstraintLayout cl = convertView.findViewById(R.id.commentConstrain);
            cl.addView(view2, new ViewGroup.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT));
            //Find width of screen
            float dpWidth = getResources().getDisplayMetrics().widthPixels;
            cl.setPadding((int) (currentComment.getCommentDepth()*dpWidth*0.1),0,0,0);

            //Set comment text
            TextView commentText = convertView.findViewById(R.id.comment_text);
            commentText.setText(currentComment.getText());

            //Header background color based on ArgumentType
            View header = convertView.findViewById(R.id.header);
            header.setBackground(getBackground(currentComment.getArgumentType()));

            //Set name text
            TextView nameView = convertView.findViewById(R.id.nameView);
            for (UserDTO user : currentUsers) {
                if (user.getid() == currentComment.getUserid()) {
                    nameView.setText(user.getname());
                    break;
                }
            }

            //Set datetime text
            if (currentComment.getDateTime() != null) {
                TextView dateTextView = convertView.findViewById(R.id.dateTextView);
                dateTextView.setText(currentComment.getDateTime());
            }

            ImageButton upVoteButton = convertView.findViewById(R.id.up);
            upVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("you pressed upVote");
                    int score = currentComment.getScore();
                    currentComment.setScore(score + 1);
                    presenter.updateComment(currentComment);
                    Toast.makeText(getActivity(), R.string.upVote,
                            Toast.LENGTH_SHORT).show();
                }
            });

            ImageButton downVoteButton = convertView.findViewById(R.id.down);
            downVoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("you pressed downVote");
                    int score = currentComment.getScore();
                    currentComment.setScore(score - 1);
                    presenter.updateComment(currentComment);
                    Toast.makeText(getActivity(), R.string.downVote,
                            Toast.LENGTH_SHORT).show();
                }
            });

            ImageButton replyToComment = convertView.findViewById(R.id.replyToComment);
            replyToComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openWriteCommentDialog(rootView, currentComment.getId());
                }
            });

            return convertView;
        }

        @Override
        public int getCount() {
            return currentComments.size();
        }

        @Override
        public Object getItem(int position) {
            return currentComments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}
