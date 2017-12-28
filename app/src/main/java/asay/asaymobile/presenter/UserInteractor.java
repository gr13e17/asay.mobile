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
    private ArrayList<UserDTO> mCurrentUsers = new ArrayList<UserDTO>();

    public UserInteractor(UserPresenter presenter){
        this.presenter = presenter;
    }

    void retrieveUser(double id){
        Query query = userElement.child("").orderByChild("id").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    if (data.child("id").equals(userDTO.getid())) {
                        exist = true;
                        System.out.println("userDTO all ready exist");
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

    private void updateUser(UserDTO userDTO){

    }

    void retriveCurrentUsers(){
        userElement.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCurrentUsers.clear();
                for (DataSnapshot usersSnapShot : dataSnapshot.getChildren()){
                    UserDTO user = usersSnapShot.getValue(UserDTO.class);
                    mCurrentUsers.add(user);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
