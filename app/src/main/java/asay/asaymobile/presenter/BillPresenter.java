package asay.asaymobile.presenter;

import java.util.ArrayList;

import asay.asaymobile.BillContract;
import asay.asaymobile.model.BillDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class BillPresenter implements BillContract.Presenter {
    private BillInteractor interactor;

    @Override
    public void refreshCurrentBillDTO(ArrayList<BillDTO> bills) {

    }

    @Override
    public void addNewBill(BillDTO billDTO) {
        BillDTO bill = new BillDTO(billDTO);
        interactor.addNewBill(bill);
    }
}
