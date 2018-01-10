package asay.asaymobile.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import asay.asaymobile.R;
import asay.asaymobile.model.BillDTO;

public class CategoryActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://asay-gr13.appspot.com");
    StorageReference storageRef = storage.getReference();
    StringBuilder jsonstring = new StringBuilder();
    File localFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        StorageReference dataRef = storageRef.child("data.json");
        final BillDTO billDTO = getIntent().getParcelableExtra("bill");

        try {
            localFile = File.createTempFile("data", "json");
        } catch (IOException e) {
            System.out.println("Error " +e.getMessage());
            e.printStackTrace();
        }

        dataRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(localFile));
                    String line;

                    while ((line = br.readLine()) != null) {
                        jsonstring.append(line);
                    }
                    System.out.println(jsonstring.toString());
                    br.close();
                    final EditText category = findViewById(R.id.category);
                    Button button = findViewById(R.id.submit);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String text = category.getText().toString().toLowerCase();
                            try {
                                JSONObject categoriesObject = new JSONObject(jsonstring.toString());
                                if(categoriesObject.has(text)){
                                    JSONArray list = categoriesObject.getJSONArray(text);
                                    list.put(billDTO.getResume());
                                } else{
                                    JSONArray list = new JSONArray();
                                    list.put(billDTO.getResume());
                                    categoriesObject.putOpt(text,list);
                                }
                                System.out.println(categoriesObject);

                            } catch (JSONException e) {
                                System.out.println("Error" + e.getMessage());
                                e.printStackTrace();
                            }
                            finish();
                        }
                    });
                }
                catch (IOException e) {
                    System.out.println("Error " + e.getMessage());
                    //You'll need to add proper error handling here
                }            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Error " + exception.getMessage());
                // Handle any errors
            }
        });



        TextView title = findViewById(R.id.title);
        title.setText(billDTO.getNumber()+": " + billDTO.getTitle());
        TextView description = findViewById(R.id.resume);
        description.setText(billDTO.getResume());

        }


}
