package asay.asaymobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import asay.asaymobile.R;
import asay.asaymobile.UserContract;
import asay.asaymobile.model.UserDTO;
import asay.asaymobile.presenter.UserPresenter;
import butterknife.ButterKnife;


public class BillsFragment extends Fragment implements UserContract.View{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private BillsFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    private int userId = 1;
    private ArrayList<Integer> savedBills = new ArrayList<>();
    private boolean loggedIn = true;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public BillsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserPresenter presenter = new UserPresenter(this);
        presenter.getUser(userId);
        View rootView = inflater.inflate(R.layout.fragment_bills,container,false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public void onViewCreated(final View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
            }

    @Override
    public void refreshUser(UserDTO currentUsers) {
        savedBills = currentUsers.getbillsSaved();
        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = getActivity().findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    BillsAllFragment billsAllFragment = new BillsAllFragment();
                    return billsAllFragment;
                case 1:
                    //TODO: return billsFavoriterFragment
                    if(loggedIn){

                        Bundle bundle = new Bundle();
                        bundle.putIntegerArrayList("savedBills",savedBills);
                        BillsAllFragment billsAllFragmentFavorite = new BillsAllFragment();
                        billsAllFragmentFavorite.setArguments(bundle);
                        return billsAllFragmentFavorite;
                    } else{
                        return null;
                    }
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Alle";
                case 1:
                    return "Mine";

            }
            return null;
        }
    }
}