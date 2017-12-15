package asay.asaymobile.presenter;

import java.util.ArrayList;

import asay.asaymobile.ForumContract;
import asay.asaymobile.model.CommentDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class ForumPresenter implements ForumContract.Presenter {
    private ForumContract.View mView;

    private ForumInteractor interactor;

    public ForumPresenter(final ForumContract.View view, double billId) {
        mView = view;
        interactor = new ForumInteractor(this, billId);
    }
    @Override
    public void addNewComment(CommentDTO commentDTO) {
        CommentDTO comment = new CommentDTO(commentDTO);

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            chatMessage.setSenderUserName(user.getDisplayName());
//        } else {
//            mView.showUnloggedUserError();
//        }

//        String profilePicUrl = "https://" + user.getPhotoUrl().getAuthority() + user.getPhotoUrl().getPath();
//        chatMessage.setProfilePicUri(profilePicUrl.trim());

//        chatMessage.setTimestamp(String.valueOf(System.currentTimeMillis()));

        interactor.addNewComment(comment);
    }

    @Override
    public void refreshCurrentCommentList(ArrayList<CommentDTO> currentcomment) {
        //Collections.sort(currentcomment);
        System.out.println("Comment"+currentcomment);
        mView.refreshCurrentCommentList(currentcomment);
    }

    @Override
    public void signOut() {

    }
}
