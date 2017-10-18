package asay.asaymobile;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    EditText etResponse;
    TextView tvIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get reference to the views
        etResponse = (EditText) findViewById(R.id.etResponse);
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        // check if you are connected or not
//        if(isConnected()){
//            tvIsConnected.setBackgroundColor(0xFF00CC00);
//            tvIsConnected.setText("You are connected");
//        }
//        else{
//            tvIsConnected.setText("You are NOT connected");
//        }

        // call AsynTask to perform network operation on separate thread
        String baseUrl = "http://oda.ft.dk/api/";
        String proposalExpand = "&$expand=Sagsstatus,Periode,Sagstype,SagAktør,Sagstrin";
        String proposalFilter = "&$filter=(typeiSag?$orderby=id%20descd%20eq%203%20or%20typeid%20eq%205)%20and%20periodeid%20eq%20146";
        String call = "Sag?$orderby=id%20desc"+proposalExpand;
//        String baseUrl ="http://hmkcode.appspot.com/rest/controller/get.json";
        new HttpAsyncTask().execute(baseUrl + call);
    }


    public boolean isConnected(){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;
        } catch (Exception exp) {
            System.out.print("Exception: " + exp.getMessage());
        }
        return false;
    }

 }
