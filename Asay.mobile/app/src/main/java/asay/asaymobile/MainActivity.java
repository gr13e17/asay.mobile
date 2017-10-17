package asay.asaymobile;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        String call = "Sag?$orderby=id%20desc";
//        String baseUrl ="http://hmkcode.appspot.com/rest/controller/get.json";
        new HttpAsyncTask().execute(baseUrl + call);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try{
                JSONObject json = new JSONObject(result);
                String str = "";

                JSONArray articles = json.getJSONArray("value"); // get articles array

                str += "articles length = "+json.getJSONArray("value").length();
                str += "\n--------\n";
                str += "names: "+articles.getJSONObject(0).names();
//                str += "\n--------\n";
//                str += "url: "+articles.getJSONObject(0).getString("url");

                etResponse.setText(str);

            } catch (Exception exc){
                etResponse.setText("Exception: " + exc.getMessage());
            }
        }
    }
}
