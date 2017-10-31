package asay.asaymobile;

import static android.R.attr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Soelberg on 31-10-2017.
 */

public class BillDebat extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bill_debat_fragment, container, false);
        return rootView;
    }
}