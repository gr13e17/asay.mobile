package asay.asaymobile.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import asay.asaymobile.model.BillDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class BillInteractor {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private BillPresenter presenter;
    private DatabaseReference billElementReference = database.getReference("bills");
    private final ArrayList<BillDTO> mbillList = new ArrayList<>();

    BillInteractor(BillPresenter presenter, boolean isEnded) {
        this.presenter = presenter;
        if(!isEnded)
            retrieveAllBills();
        else
            retriveEndedBills();
    }

    void retriveSavedBills(final ArrayList<Integer> savedbills) {
        billElementReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BillDTO billDTO = new BillDTO();
                mbillList.clear();
                if(dataSnapshot.getChildren() != null){
                    for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                        billDTO = messagesSnapshot.getValue(BillDTO.class);
                        for (int billId : savedbills) {
                            if (billDTO.getId() == billId) {
                                mbillList.add(billDTO);
                            }
                        }
                    }
                    presenter.refreshCurrentBillDTO(mbillList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Handle error on presenter here.
            }
        });
    }

    void retriveEndedBills() {
        Date date = new Date();
        String str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
        Query query = billElementReference.orderByChild("deadline").endAt(str);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BillDTO billDTO = new BillDTO();
                mbillList.clear();
                if(dataSnapshot.getChildren() != null) {
                    for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                        billDTO = messagesSnapshot.getValue(BillDTO.class);
                        mbillList.add(billDTO);
                    }
                }
                presenter.refreshEndedBillDTO(mbillList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    void retrieveAllBills() {
        Date date = new Date();
        String str = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date);
        Query query = billElementReference.orderByChild("deadline").startAt(str);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mbillList.clear();
                if(dataSnapshot.getChildren() != null) {
                    for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                        BillDTO billDTO = messagesSnapshot.getValue(BillDTO.class);
                        mbillList.add(billDTO);
                    }
                }
                presenter.refreshCurrentBillDTO(mbillList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    void addNewBill(final BillDTO billDTO) {
        billElementReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean exist = false;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Object result = data.child("id").getValue();
                    if (data.child("id").getValue().toString().equals(String.valueOf(billDTO.id))) {
                        exist = true;
                        BillDTO databillDTO = data.getValue(BillDTO.class);
                        if(!databillDTO.equals(billDTO)){
                            DatabaseReference billref = billElementReference.child(data.getKey());
                            billref.setValue(billDTO);
                        }
                    } else {
                        //do something
                    }
                }
                if (!exist)
                    billElementReference.getRef().push().setValue(billDTO);

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println(firebaseError.getMessage());
            }
        });
    }

    public void updateBill(final BillDTO bill){
        billElementReference.orderByChild("id").equalTo(bill.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    // update count for child: child.getKey()
                    DatabaseReference billref = billElementReference.child(child.getKey());
                    billref.setValue(bill);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
    }

}
