package asay.asaymobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText etResponse;
    private ArrayList<String> bills = new ArrayList<String>();
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // call AsynTask to perform network operation on separate thread
        String baseUrl = "http://oda.ft.dk/api/";
        String proposalExpand = "&$expand=Sagsstatus,Periode,Sagstype,SagAktør,Sagstrin";
        String proposalFilter = "&$filter=(typeiSag?$orderby=id%20descd%20eq%203%20or%20typeid%20eq%205)%20and%20periodeid%20eq%20146";
        String call = baseUrl + "Sag?$orderby=id%20desc"+proposalExpand;
//        String baseUrl ="http://hmkcode.appspot.com/rest/controller/get.json";
        new HttpAsyncTask(this, new AsyncTaskCompleteListener()).execute(call);

        // get reference to the views
        adapter = new ArrayAdapter(this, R.layout.bill_list_item,R.id.listeelem_header,bills);

        ListView listview = new ListView(this);
        listview.setOnItemClickListener(this);
        listview.setAdapter(adapter);

        setContentView(listview);
    }

    @Override
    public void onItemClick(AdapterView<?> list, View v, int position, long id){
        Toast.makeText(this, "Click on" + position,Toast.LENGTH_SHORT).show();
        //we are going to point at relative layout in the root of R.layout.bill_list_item
    }

    private class AsyncTaskCompleteListener implements asay.asaymobile.AsyncTaskCompleteListener<JSONObject> {
        @Override
        public void onTaskComplete(JSONObject result)
        {
            try{
                String str = "";
                if (result == null){
                    etResponse.setText("Result is null");
                }
                Log.d("OnTaskComplete", "onTaskComplete: " + result);
                JSONArray articles = result.getJSONArray("value"); // get articles array
                for (int i = 0; i < articles.length(); i++){
                    bills.add(articles.getJSONObject(i).getString("titelkort"));
                }
                adapter.notifyDataSetChanged();
            } catch (Exception excep){
                Log.d("JSON Exception", "onTaskComplete: " + excep.getMessage());
            }
            // do something with the result
        }
    }

 }
