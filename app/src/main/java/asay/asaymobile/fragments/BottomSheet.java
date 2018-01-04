package asay.asaymobile.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import asay.asaymobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheet extends Fragment {


    private View bottomSheetView;
    private Dialog mBottomSheetDialog;


    public BottomSheet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bottomSheetView = getLayoutInflater ().inflate (R.layout.bottom_sheet, null);

        mBottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (bottomSheetView);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mBottomSheetDialog.show ();
        return bottomSheetView;
    }



}
