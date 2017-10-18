package asay.asaymobile;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by s123725 on 17/10/2017.
 */

public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private String baseUrl = "http://oda.ft.dk/api/";

    public JSONObject getJSONObject() {
        Log.d("Getter", "getJSONObject: " + this.jsonObject.toString());
        return this.jsonObject;
    }

    public void setJSONObject(JSONObject value) {
        this.jsonObject = value;
    }

    @Override
    protected String doInBackground(String... urls) {

        return GET(urls[0]);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        try {
            JSONObject object = new JSONObject(result);
            Log.d("JSON", "onPostExecute: " + object.toString());
            setJSONObject(object);
        } catch (Exception exc) {
            Log.d("JsonException", "Exception: " + exc.getMessage());
        }
    }

    private String GET(String url) {
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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}

