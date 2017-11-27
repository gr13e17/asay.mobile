package asay.asaymobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asay.asaymobile.R;


public class BillsFragment extends Fragment {
    public BillsFragment() {
        // Required empty public constructor
    }
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Alle"),
                BillsAllFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Mine"),
                BillsAllFragment.class, null);

        // Inflate the layout for this fragment
        return mTabHost;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}
