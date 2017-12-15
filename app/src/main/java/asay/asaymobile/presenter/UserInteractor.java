package asay.asaymobile.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private void retriveCurrentUsers(){
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
