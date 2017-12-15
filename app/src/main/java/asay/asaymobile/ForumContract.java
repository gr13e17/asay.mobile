package asay.asaymobile;

import java.util.ArrayList;

import asay.asaymobile.model.CommentDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public interface ForumContract {
    interface  View{

        void closeForum();

        void showUnloggedUserError();

        void refreshCurrentCommentList(ArrayList<CommentDTO> currentComment);

    }

    interface Presenter{
        void addNewComment(CommentDTO comment);

        void refreshCurrentCommentList(ArrayList<CommentDTO> currentComment);

        void signOut();

    }
}
