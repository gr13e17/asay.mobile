package asay.asaymobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ActiveBills extends Fragment {
    public ActiveBills() {
        // Required empty public constructor
    }
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Alle"),
                BillListFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Mine"),
                BillListFragment.class, null);

        // Inflate the layout for this fragment
        return mTabHost;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}
