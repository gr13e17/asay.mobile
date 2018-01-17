package asay.asaymobile.presenter;

import java.util.ArrayList;

import asay.asaymobile.BillContract;
import asay.asaymobile.model.BillDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class BillPresenter implements BillContract.Presenter {
    private BillInteractor interactor;
    private BillContract.View mView;

    public BillPresenter(final BillContract.View view, boolean isEnded) {
        mView = view;
        interactor = new BillInteractor(this, isEnded);
    }

    @Override
    public void refreshCurrentBillDTO(ArrayList<BillDTO> bills) {
        mView.refreshCurrentBills(bills);
    }

    @Override
    public void refreshEndedBillDTO(ArrayList<BillDTO> bills){
        mView.refreshEndedBills(bills);
    }

    @Override
    public void getSavedBills(ArrayList<Integer> ids) {
        interactor.retriveSavedBills(ids);
    }

    @Override
    public void getAllBills() {
        interactor.retrieveAllBills();
    }

    @Override
    public void getEndedBills() {
        interactor.retriveEndedBills();
    }

    @Override
    public void addNewBill(BillDTO billDTO) {
        BillDTO bill = new BillDTO(billDTO);
        interactor.addNewBill(bill);
    }

    @Override
    public void updateBill(BillDTO bill) {
        interactor.updateBill(bill);
    }
}
