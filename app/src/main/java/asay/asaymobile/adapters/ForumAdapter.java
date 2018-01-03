package asay.asaymobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import asay.asaymobile.R;
import asay.asaymobile.model.ArgumentType;
import asay.asaymobile.model.CommentDTO;
import asay.asaymobile.model.UserDTO;
import asay.asaymobile.presenter.ForumPresenter;

/**
 * Created by s123725 on 30/12/2017.
 */

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
        this.mInflater = LayoutInflater.from(this.context);
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
