package asay.asaymobile.presenter;

import java.util.ArrayList;

import asay.asaymobile.UserContract;
import asay.asaymobile.model.UserDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class UserPresenter implements UserContract.Presenter{
    private UserContract.View mView;
    private UserInteractor interactor;

    public UserPresenter(final UserContract.View view){
        mView = view;
        interactor = new UserInteractor(this);
    }

    @Override
    public void refreshCurrentUsersList(ArrayList<UserDTO> currentUsers) {
        mView.refreshCurrentUserList(currentUsers);
    }
}
