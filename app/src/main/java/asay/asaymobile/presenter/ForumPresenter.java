package asay.asaymobile.presenter;

import java.util.ArrayList;

import asay.asaymobile.ForumContract;
import asay.asaymobile.model.CommentDTO;
import asay.asaymobile.model.UserDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class ForumPresenter implements ForumContract.Presenter {
    private ForumContract.View mView;

    private ForumInteractor interactor;

    private UserDTO userDTO;

    public ForumPresenter(final ForumContract.View view, double billId) {
        mView = view;
        interactor = new ForumInteractor(this, billId);
    }

    @Override
    public void addNewComment(CommentDTO commentDTO) {
        CommentDTO comment = new CommentDTO(commentDTO);
        interactor.addNewComment(comment);
    }

    @Override
    public void refreshCurrentCommentList(ArrayList<CommentDTO> currentcomment) {
        mView.refreshCurrentCommentList(currentcomment);
    }

    @Override
    public void refreshUser(ArrayList<UserDTO> users) {
        mView.refreshUsers(users);
    }


    @Override
    public void signOut() {

    }
}
