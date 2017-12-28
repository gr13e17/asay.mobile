package asay.asaymobile;

import asay.asaymobile.model.UserDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public interface UserContract {
    interface View{
        void refreshUser(UserDTO currentUsers);

    }
    interface Presenter{
        void getUser(double id);
        void refreshUser(UserDTO user);
        void addUser(UserDTO user);
        void UpdateFavorites(UserDTO user);
    }
}
