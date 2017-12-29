package asay.asaymobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class BillForumFragment extends Fragment implements ForumContract.View {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bill_forum, container, false);
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

        ImageButton submit = (ImageButton) rootView.findViewById(R.id.reply_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) rootView.findViewById(R.id.content);
                final String content = editText.getText().toString();
                CommentDTO comment = new CommentDTO(
                        ArgumentType.FOR,
                        billId,
                        0,
                        0,
                        content,
                        1
                );
                forumPresenter.addNewComment(comment);
            }
        });
    }

    private void commentPlaceholder(){
        commentArray.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae est mollis, condimentum dolor in, porta orci. Maecenas et elit vel justo sagittis viverra non a ipsum. Nullam turpis mi, dignissim et lobortis nec, venenatis congue felis. Praesent eget eleifend sapien. Aliquam pulvinar at nunc eget efficitur. Nam vel pretium elit.");
        commentArray.add("Pellentesque dignissim, lacus molestie tempus pellentesque, augue lorem dignissim nisl, quis tempor ipsum lacus id justo. Vivamus massa mi, ornare vitae elit vitae, imperdiet imperdiet tortor. Suspendisse elementum tincidunt neque, at bibendum odio sagittis in. Sed ut ullamcorper metus, id aliquet quam. Maecenas commodo pulvinar urna sit amet mollis. Sed vel purus congue, viverra eros sed, vestibulum erat. In facilisis est at erat imperdiet maximus. Pellentesque accumsan mauris lorem, et sagittis massa euismod eget.");
        commentArray.add("Duis consectetur vestibulum posuere. In laoreet dapibus condimentum.");
        commentArray.add("Aliquam blandit at risus nec mollis. Etiam a odio est. Etiam vitae finibus augue, et mollis neque. Phasellus ac nisl diam. Quisque in convallis ex. Donec ultrices molestie velit. Morbi ac enim commodo, sagittis tortor pellentesque, lobortis augue. Quisque ut ex quam. Vestibulum porta nunc ullamcorper ligula viverra, vitae tempus sapien bibendum.");
        commentArray.add("Praesent convallis venenatis massa. aaskk aklaskh aslkjh ash ada");
        commentArray.add("Nullam placerat magna metus, id tincidunt nunc vestibulum at. Donec ut ligula sagittis, posuere purus sollicitudin, tristique leo. Integer et ultrices risus, et cursus augue. ");
    }

    private Integer getColor(ArgumentType argumentType){
        switch (argumentType) {
            case FOR:
                return getResources().getColor(R.color.forColor);
            case AGAINST:
                return getResources().getColor(R.color.againstColor);
            case NEUTRAL:
                return getResources().getColor(R.color.neutralColor);
            default:
                return getResources().getColor(R.color.neutralColor);
        }
    }

    @Override
    public void closeForum() {
    }

    @Override
    public void showUnloggedUserError() {

    }

    @Override
    public void refreshCurrentCommentList(final ArrayList<CommentDTO> currentComment) {
        ArrayAdapter commentArrayAdapter = new ArrayAdapter(getActivity(), R.layout.list_item_comment,R.id.nameView,currentComment){
            @Override
            public View getView(int position, View cachedView, ViewGroup parent){
                View view = super.getView(position, cachedView, parent);
                TextView commentText = view.findViewById(R.id.comment);
                commentText.setText(currentComment.get(position).getText());
                TextView nameView = view.findViewById(R.id.nameView);
                for (UserDTO user: nameArray ){
                    if(user.getid() == currentComment.get(position).getUserid()){
                        nameView.setText(user.getname());
                        nameView.setBackgroundColor(getColor(currentComment.get(position).getArgumentType()));
                    }
                }

                return view;
            }
        };
        listView.setAdapter(commentArrayAdapter);
    }

    @Override
    public void refreshUsers(ArrayList<UserDTO> users) {
        this.nameArray = users;
    }
}
