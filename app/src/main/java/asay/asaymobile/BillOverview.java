package asay.asaymobile;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

/**
 * Created by Soelberg on 31-10-2017.
 */

public class BillOverview extends Fragment implements OnClickListener{

    ToggleButton sub;
    TextView BillDesc;
    TextView arg1;
    TextView arg2;
    TextView popup;
    View Scroll;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.bill_overview_fragment, container, false);


        sub = (ToggleButton) rootView.findViewById(R.id.toggleButton);

        BillDesc = (TextView) rootView.findViewById(R.id.textViewAboutBill);
        BillDesc.setKeyListener(null);
        BillDesc.setOnClickListener(this);

        arg1 = (TextView) rootView.findViewById(R.id.textArg1);
        arg1.setKeyListener(null);
        arg1.setOnClickListener(this);

        arg2 = (TextView) rootView.findViewById(R.id.textArg2);
        arg2.setKeyListener(null);
        arg2.setOnClickListener(this);

        popup = (TextView) rootView.findViewById(R.id.popup);
        popup.setKeyListener(null);
        popup.setVisibility(View.INVISIBLE);
        popup.setOnClickListener(this);
        popup.setVerticalScrollBarEnabled(true);

        Scroll = (View) rootView.findViewById(R.id.RLScroll);
        Scroll.setVerticalScrollBarEnabled(true);

        return rootView;


}
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.textViewAboutBill:
                popup.setVisibility(View.VISIBLE);
                popup.setText(getResources().getString(R.string.dummy_description) + getResources().getString(R.string.dummy_description));
                popup.bringToFront();
                sub.setVisibility(View.INVISIBLE);
                break;

            case R.id.textArg1:
                popup.setVisibility(View.VISIBLE);
                popup.setText(getResources().getString(R.string.dummy_arg1));
                popup.bringToFront();
                sub.setVisibility(View.INVISIBLE);
                break;

            case R.id.textArg2:
                popup.setVisibility(View.VISIBLE);
                popup.setText(getResources().getString(R.string.dummy_arg2));
                popup.bringToFront();
                sub.setVisibility(View.INVISIBLE);
                break;

            case R.id.popup:
                popup.setVisibility(View.INVISIBLE);
                sub.setVisibility(View.VISIBLE);
                break;
            default:
                popup.setVisibility(View.INVISIBLE);
                sub.setVisibility(View.VISIBLE);
                break;
        }

    }
}

//Method to create expandable textview (not implemented in asay prototype 1.0)
//public class ExpandableTextView extends TextView implements View.OnClickListener {
//
//    private static final int MaxLines = 4;
//    private int currentMaxLines = Integer.MAX_VALUE;
//
//    public ExpandableTextView(Context text){
//        super(text);
//        setOnClickListener(this);
//    }
//
//    public ExpandableTextView(Context text, AttributeSet att, int dStyle){
//    super(text, att, dStyle);
//    setOnClickListener(this);
//    }
//
//    public ExpandableTextView(Context text, AttributeSet att){
//        super(text, att);
//        setOnClickListener(this);
//    }
//@Override
//protected void onTextChanged(CharSequence text, int start, int lengthBf, int lengthAft){
//        post(new Runnable() {
//            @Override
//            public void run() {
//                if (getLineCount()>MaxLines)
//                    setCompoundDrawablesWithIntrinsicBounds(0,0,0, R.drawable.icon_more_text;
//                else
//                    setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
//            setMaxLines(MaxLines);
//            }
//
//        });
//}
//@Override
//    public void setMaxLines(int maxLines){
//    currentMaxLines = maxLines;
//    super.setMaxLines(maxLines);
//
//}
//
//    public int getMyMaxLines()
//    {
//        return currentMaxLines;
//    }
//
//    @Override
//    public void onClick(View v)
//    {
//        /* Toggle between expanded collapsed states */
//        if (getMyMaxLines() == Integer.MAX_VALUE)
//            setMaxLines(MaxLines);
//        else
//            setMaxLines(Integer.MAX_VALUE);
//    }
//
//}
