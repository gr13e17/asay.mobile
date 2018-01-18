package asay.asaymobile;

import java.util.ArrayList;

import asay.asaymobile.model.BillDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public interface BillContract {
    interface View {
        void refreshCurrentBills(ArrayList<BillDTO> bills);
        void refreshEndedBills(ArrayList<BillDTO> bills);
    }

    interface Presenter{
        void refreshCurrentBillDTO(ArrayList<BillDTO> bills);
        void refreshEndedBillDTO(ArrayList<BillDTO> bills);
        void getSavedBills(ArrayList<Integer> ids);
        void getAllBills();
        void getEndedBills();
        void addNewBill(BillDTO bill);
        void updateBill(BillDTO bill);
    }
}
