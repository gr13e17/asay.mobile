package asay.asaymobile.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import asay.asaymobile.R;

/**
 * Created by Soelberg on 31-10-2017.
 */

public class BillForumFragment extends Fragment {
    //contains names of the one who wrote the comment. must be populated from database
    ArrayList<String> nameArray = new ArrayList<String>();
    ArrayList<String> commentArray = new ArrayList<String>();
    ArrayList<Integer> colorArray = new ArrayList<Integer>();
    ArrayAdapter arrayAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bill_forum, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        // Inflate the layout for this fragment
        // call AsynTask to perform network operation on separate thread

        namePlaceholder();
        commentPlaceholder();
        colorPlaceholder();
        // get reference to the views
        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.list_item_comment,R.id.nameView,nameArray){
            @Override
            public View getView(int position, View cachedView, ViewGroup parent){
                View view = super.getView(position, cachedView, parent);

                TextView commentText = view.findViewById(R.id.comment);
                commentText.setText(commentArray.get(position));

                TextView nameView = view.findViewById(R.id.nameView);
                nameView.setBackgroundColor(colorArray.get(position));
                return view;
            }
        };


        ListView listview = new ListView(getActivity());
        listview.setAdapter(arrayAdapter);
        ViewGroup viewGroup = (ViewGroup) rootView;
        viewGroup.addView(listview);

    }

    //placeholder to populate the name arreylise
    private void namePlaceholder(){
        nameArray.add("Ole Hansen");
        nameArray.add("Gitte Andersen");
        nameArray.add("Anne Larsen");
        nameArray.add("Ulla Nilsen");
        nameArray.add("Allan Sønder");
        nameArray.add("Sonja Rasmussen");
    }

    private void commentPlaceholder(){
        commentArray.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras vitae est mollis, condimentum dolor in, porta orci. Maecenas et elit vel justo sagittis viverra non a ipsum. Nullam turpis mi, dignissim et lobortis nec, venenatis congue felis. Praesent eget eleifend sapien. Aliquam pulvinar at nunc eget efficitur. Nam vel pretium elit.");
        commentArray.add("Pellentesque dignissim, lacus molestie tempus pellentesque, augue lorem dignissim nisl, quis tempor ipsum lacus id justo. Vivamus massa mi, ornare vitae elit vitae, imperdiet imperdiet tortor. Suspendisse elementum tincidunt neque, at bibendum odio sagittis in. Sed ut ullamcorper metus, id aliquet quam. Maecenas commodo pulvinar urna sit amet mollis. Sed vel purus congue, viverra eros sed, vestibulum erat. In facilisis est at erat imperdiet maximus. Pellentesque accumsan mauris lorem, et sagittis massa euismod eget.");
        commentArray.add("Duis consectetur vestibulum posuere. In laoreet dapibus condimentum.");
        commentArray.add("Aliquam blandit at risus nec mollis. Etiam a odio est. Etiam vitae finibus augue, et mollis neque. Phasellus ac nisl diam. Quisque in convallis ex. Donec ultrices molestie velit. Morbi ac enim commodo, sagittis tortor pellentesque, lobortis augue. Quisque ut ex quam. Vestibulum porta nunc ullamcorper ligula viverra, vitae tempus sapien bibendum.");
        commentArray.add("Praesent convallis venenatis massa. aaskk aklaskh aslkjh ash ada");
        commentArray.add("Nullam placerat magna metus, id tincidunt nunc vestibulum at. Donec ut ligula sagittis, posuere purus sollicitudin, tristique leo. Integer et ultrices risus, et cursus augue. ");
    }

    private void colorPlaceholder(){
        colorArray.add(getResources().getColor(R.color.tempAgainstColor));
        colorArray.add(getResources().getColor(R.color.tempForColor));
        colorArray.add(getResources().getColor(R.color.tempAgainstColor));
        colorArray.add(getResources().getColor(R.color.tempAgainstColor));
        colorArray.add(getResources().getColor(R.color.tempForColor));
        colorArray.add(getResources().getColor(R.color.tempAgainstColor));
    }
}
