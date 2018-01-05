package asay.asaymobile.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import asay.asaymobile.ForumContract;
import asay.asaymobile.R;
import asay.asaymobile.activities.BillActivity;
import asay.asaymobile.activities.MainActivity;
import asay.asaymobile.adapters.ForumAdapter;
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
    private View bottomSheetView;
    private Dialog mBottomSheetDialog;

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
        ForumAdapter commentArrayAdapter = new ForumAdapter(currentComment,nameArray,getContext(), forumPresenter);

        listView.setAdapter(commentArrayAdapter);
    }

    @Override
    public void refreshUsers(ArrayList<UserDTO> users) {
        this.nameArray = users;
    }

//////////////////////

    public void openBottomSheet (View v) {

        bottomSheetView = getLayoutInflater ().inflate (R.layout.bottom_sheet, null);

        mBottomSheetDialog = new Dialog (getContext(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (bottomSheetView);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mBottomSheetDialog.show ();

        replyButton = bottomSheetView.findViewById(R.id.reply_button);
        replyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == commentButtonMain){
            openBottomSheet(rootView);
        } else if (view == replyButton){
            EditText editText = bottomSheetView.findViewById(R.id.content);
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
            mBottomSheetDialog.dismiss();
        }
    }

}
