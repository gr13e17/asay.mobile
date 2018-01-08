package asay.asaymobile.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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

public class BillForumFragment extends Fragment implements ForumContract.View, View.OnClickListener {
    //contains names of the one who wrote the comment. must be populated from database
    @BindView(R.id.forum_list_view)
    ListView listView;
    int billId;
    ForumPresenter forumPresenter;
    ArrayList<UserDTO> nameArray = new ArrayList<UserDTO>();
    ArrayList<String> commentArray = new ArrayList<String>();
    ArrayList<Integer> colorArray = new ArrayList<Integer>();
    ArrayAdapter arrayAdapter;
    UserDTO userDTO;
    private View rootView;
    private FloatingActionButton commentButtonMain;
    private Button replyButton;
    private View writeCommentView;
    private Dialog writeCommentDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        // call AsynTask to perform network operation on separate thread
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
    public void refreshCurrentCommentList(final ArrayList<CommentDTO> currentComment) {
        if (getContext() != null) {
            ForumAdapter commentArrayAdapter = new ForumAdapter(currentComment, nameArray, getContext(), forumPresenter);
            listView.setAdapter(commentArrayAdapter);
        }
    }

    @Override
    public void refreshUsers(ArrayList<UserDTO> users) {
        this.nameArray = users;
    }


    public void writeComment(View v) {

        writeCommentView = getLayoutInflater ().inflate (R.layout.dialog_new_comment, null);

        writeCommentDialog = new Dialog (getContext(), R.style.MaterialDialogSheet);
        writeCommentDialog.setContentView (writeCommentView);
        writeCommentDialog.setCancelable (true);
        writeCommentDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        writeCommentDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        writeCommentDialog.show ();

        replyButton = writeCommentView.findViewById(R.id.reply_button);
        replyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == commentButtonMain){
            writeComment(rootView);
        } else if (view == replyButton){
            EditText editText = writeCommentView.findViewById(R.id.content);
            String content = editText.getText().toString();
            content = content.trim(); //trim string for trailing and leading whitespaces

            // Show error if no comment is written
            if(content.length() == 0){
                editText.setError(getResources().getString(R.string.error_no_text));
                return;
            }

            CommentDTO comment = new CommentDTO(
                    ArgumentType.FOR,
                    billId,
                    0,
                    0,
                    content,
                    1,
                    0
            );
            forumPresenter.addNewComment(comment);
            writeCommentDialog.dismiss();
        }
    }





    public class ForumAdapter extends BaseAdapter implements View.OnClickListener {
        ArrayList<CommentDTO> currentComments;
        ArrayList<UserDTO> currentUsers;
        Context context;
        LayoutInflater mInflater;
        public View.OnClickListener listener;
        ForumPresenter presenter;


        public ForumAdapter(ArrayList<CommentDTO> currentComments, ArrayList<UserDTO> currentUsers, Context context, ForumPresenter presenter){
            this.currentComments = currentComments;
            this.currentUsers = currentUsers;
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
            this.presenter = presenter;
        }

        public void setButtonListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        private Integer getColor(ArgumentType argumentType){
            switch (argumentType) {
                case FOR:
                    return context.getResources().getColor(R.color.forColor);
                case AGAINST:
                    return context.getResources().getColor(R.color.againstColor);
                case NEUTRAL:
                    return context.getResources().getColor(R.color.neutralColor);
                default:
                    return context.getResources().getColor(R.color.neutralColor);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.list_item_comment, parent, false);
            }
            final CommentDTO currentComment = currentComments.get(position);
            TextView commentText = convertView.findViewById(R.id.comment);
            commentText.setText(currentComment.getText());
            TextView nameView = convertView.findViewById(R.id.nameView);
            ImageButton upvote = (ImageButton) convertView.findViewById(R.id.up);
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("you pressed upvote");
                    int score = currentComment.getScore();
                    currentComment.setScore(score +1);
                    presenter.updateComment(currentComment);
                }
            });
            ImageButton downvote = (ImageButton) convertView.findViewById(R.id.down);
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("you pressed downVote");
                    int score = currentComment.getScore();
                    currentComment.setScore(score -1);
                    presenter.updateComment(currentComment);
                }
            });
            for (UserDTO user: currentUsers ){
                if(user.getid() == currentComment.getUserid()){
                    nameView.setText(user.getname());
                    nameView.setBackgroundColor(getColor(currentComment.getArgumentType()));
                }
            }
            Button replyToComment = convertView.findViewById(R.id.replyToComment);
            replyToComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    writeComment(rootView);
                }
            });

            return convertView;
        }

        @Override
        public void onClick(View v) {

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
