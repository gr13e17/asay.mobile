package asay.asaymobile.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import asay.asaymobile.model.BillDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class BillInteractor {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private BillPresenter presenter;
    private DatabaseReference billElementReference = database.getReference("bills");
    private ArrayList<BillDTO> mbillList = new ArrayList<>();

    BillInteractor(BillPresenter presenter) {
        this.presenter = presenter;
    }

    void retriveSavedBills(ArrayList<Integer> savedbills){
        for (int billId : savedbills){
            Query query = billElementReference.child("bills").orderByChild("id").equalTo(billId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mbillList.clear();
                    for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                        BillDTO billDTO = messagesSnapshot.getValue(BillDTO.class);
                        mbillList.add(billDTO);
                    }
                    presenter.refreshCurrentBillDTO(mbillList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //TODO: Handle error on presenter here.
                }
            });
        }
    }

    void retriveEndedBills(){

    }

    void retriveCurrentBill(int billid){
        Query query = billElementReference.child("bills").orderByChild("id").equalTo(billid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BillDTO billDTO = new BillDTO();
                for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                    billDTO = messagesSnapshot.getValue(BillDTO.class);
                }
                presenter.refreshBill(billDTO);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Handle error on presenter here.
            }
        });
    }

    void retrieveAllBills(){
        billElementReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mbillList.clear();
                for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                    BillDTO billDTO = messagesSnapshot.getValue(BillDTO.class);
                    mbillList.add(billDTO);
                }
                presenter.refreshCurrentBillDTO(mbillList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Handle error on presenter here.
            }
        });
    }

    void addNewBill(final BillDTO billDTO){
        billElementReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean exist = false;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child("id").equals(billDTO.id)) {
                        exist = true;
                        System.out.println("billDTO all ready exist");
                        //do ur stuff

                    } else {
                        //do something
                    }
                }
                if(!exist)
                    billElementReference.getRef().push().setValue(billDTO);

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

}
