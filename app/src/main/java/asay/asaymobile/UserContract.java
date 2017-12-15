package asay.asaymobile;

import java.util.ArrayList;

import asay.asaymobile.model.UserDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public interface UserContract {
    interface View{
        void refreshCurrentUserList(ArrayList<UserDTO> currentUsers);

    }
    interface Presenter{
        void refreshCurrentUsersList(ArrayList<UserDTO> currentUsers);
    }
}
