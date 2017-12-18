package asay.asaymobile.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import asay.asaymobile.model.CommentDTO;
import asay.asaymobile.model.UserDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class ForumInteractor {
    private static final String TAG = ForumInteractor.class.getSimpleName();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference forumElementReference = database.getReference("forums");
    private DatabaseReference userElement = database.getReference("users");

    private ForumPresenter presenter;

    private ArrayList<CommentDTO> mCurrentForumList = new ArrayList<>();
    private ArrayList<UserDTO> mUserList = new ArrayList<>();

    ForumInteractor(ForumPresenter presenter, double billId) {
        this.presenter = presenter;
        retriveUsers();
        retrieveCurrentForum(billId);
    }

    void retriveUsers(){
        userElement.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserList.clear();

                for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                    UserDTO userDTO = messagesSnapshot.getValue(UserDTO.class);
                    mUserList.add(userDTO);
                }

                presenter.refreshUser(mUserList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Handle error on presenter here.
            }
        });
    }

    private void retrieveCurrentForum(double billId) {
        Query query = forumElementReference.child("").orderByChild("billId").equalTo(billId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCurrentForumList.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot forumSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot commentsSnapshot : forumSnapshot.child("comments").getChildren()){
                            CommentDTO comment = commentsSnapshot.getValue(CommentDTO.class);
                            mCurrentForumList.add(comment);
                        }
                    }
                }
                presenter.refreshCurrentCommentList(mCurrentForumList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Handle error on presenter here.
            }
        });
    }

    void addNewComment(CommentDTO comment) {
        forumElementReference.child(toString().valueOf(comment.getId())).setValue(comment);
    }
}
