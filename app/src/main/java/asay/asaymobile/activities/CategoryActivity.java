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

import java.io.File;
import java.io.IOException;

import asay.asaymobile.R;
import asay.asaymobile.model.BillDTO;

public class CategoryActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://asay-gr13.appspot.com");
    StorageReference storageRef = storage.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        if(savedInstanceState != null){
            StorageReference dataRef = storageRef.child("data.json");
            File localFile = null;
            try {
                localFile = File.createTempFile("data", "json");
            } catch (IOException e) {
                e.printStackTrace();
            }

            dataRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    // Local temp file has been created
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            BillDTO billDTO = savedInstanceState.getParcelable("bill");
            TextView title = findViewById(R.id.title);
            title.setText(billDTO.getNumber()+": " + billDTO.getTitle());
            TextView description = findViewById(R.id.resume);
            description.setText(billDTO.getResume());
            final EditText category = findViewById(R.id.category);
            Button button = findViewById(R.id.submit);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = category.getText().toString();

                    finish();
                }
            });
        }

    }
}
