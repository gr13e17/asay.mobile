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

    public UserPresenter(final UserContract.View view, final double userid){
        mView = view;
        interactor = new UserInteractor(this, userid);
    }

    @Override
    public void getUser(double id) {
        interactor.retrieveUser(id);
    }

    @Override
    public void refreshUser(UserDTO user) {
        mView.refreshUser(user);
    }

    @Override
    public void addUser(UserDTO user) {
        interactor.addUser(user);
    }

    @Override
    public void UpdateFavorites(double billId, ArrayList<Integer> savedBills) {
        interactor.updateFavorites(billId, savedBills);
    }

}
