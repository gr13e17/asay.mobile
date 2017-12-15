package asay.asaymobile.model;

import java.util.ArrayList;

/**
 * Created by s123725 on 21/11/2017.
 */

public class UserDTO {
    private int mid;
    private String mname;
    private ArrayList<String> msavedBills;

    public UserDTO(){
        //empty constructor
    }
    public UserDTO(final UserDTO userDTO){
        mid = userDTO.getid();
        mname = userDTO.getname();
        msavedBills = userDTO.getsavedBills();
    }

    public UserDTO(final int id, final String name, ArrayList<String> savedBills){
        this.mid = id;
        this.mname = name;
        this.msavedBills = savedBills;
    }

    public int getid() { return mid;}

    public void setid(int id) { this.mid = id; }

    public String getname() { return mname; }

    public void setname(String name) { this.mname = name; }

    public ArrayList<String> getsavedBills() { return msavedBills; }

    public void setsavedBills(ArrayList<String> savedBills) { this.msavedBills = savedBills; }

}
