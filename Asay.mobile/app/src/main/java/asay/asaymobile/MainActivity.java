package asay.asaymobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            BillListFragment fragment = new BillListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.billListFrame, fragment) // tom container i layout
                    .commit();
        }
    }
}
