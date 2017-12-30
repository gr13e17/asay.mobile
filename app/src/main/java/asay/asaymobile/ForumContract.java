package asay.asaymobile;

import java.util.ArrayList;

import asay.asaymobile.model.CommentDTO;
import asay.asaymobile.model.UserDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public interface ForumContract {
    interface  View{

        void closeForum();

        void showUnloggedUserError();

        void refreshCurrentCommentList(ArrayList<CommentDTO> currentComment);

        void refreshUsers(ArrayList<UserDTO> users);
    }

    interface Presenter{
        void addNewComment(CommentDTO comment);

        void updateComment(CommentDTO commentDTO);

        void refreshCurrentCommentList(ArrayList<CommentDTO> currentComment);

        void refreshUser(ArrayList<UserDTO> users);

        void signOut();

    }
}
