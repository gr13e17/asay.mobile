package asay.asaymobile.presenter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import asay.asaymobile.model.BillDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class BillInteractor {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private BillPresenter presenter;
    private DatabaseReference billElementReference = database.getReference("bills");



    void addNewBill(BillDTO billDTO){
        billElementReference.child(toString().valueOf(billDTO.getId())).setValue(billDTO);
    }

}
