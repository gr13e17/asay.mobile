package asay.asaymobile.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import asay.asaymobile.model.UserDTO;

/**
 * Created by s123725 on 15/12/2017.
 */

public class UserInteractor {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userElement = database.getReference("users");
    private UserPresenter presenter;
    private ArrayList<UserDTO> mUsersList = new ArrayList<UserDTO>();

    public UserInteractor(UserPresenter presenter){
        this.presenter = presenter;


    }

    void retrieveUser(double id){
        Query query = userElement.child("").orderByChild("id").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("data has changed");
                UserDTO userDTO = new UserDTO();
                for (DataSnapshot messagesSnapshot : dataSnapshot.getChildren()) {
                    userDTO = messagesSnapshot.getValue(UserDTO.class);
                }
                presenter.refreshUser(userDTO);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Handle error on presenter here.
            }
        });
    }

    void addUser(final UserDTO userDTO){
        userElement.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean exist = false;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child("id").getValue().toString().equals(String.valueOf(userDTO.getid()))) {
                        exist = true;
                        //do ur stuff
                    } else {
                        //do something
                    }
                }
                if(!exist)
                    userElement.getRef().push().setValue(userDTO);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    void updateFavorites(double userid, final ArrayList<Integer> savedBills){
        userElement.orderByChild("id").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    // update count for child: child.getKey()
                    DatabaseReference Userref = userElement.child(child.getKey());
                    DatabaseReference billsref = Userref.child("billsSaved");
                    billsref.setValue(savedBills);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
