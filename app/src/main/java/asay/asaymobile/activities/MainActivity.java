package asay.asaymobile.activities;





import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import asay.asaymobile.R;
import asay.asaymobile.fetch.HttpAsyncTask;
import asay.asaymobile.fragments.BillsAllFragment;
import asay.asaymobile.model.BillDTO;
import asay.asaymobile.presenter.BillPresenter;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    EditText etResponse;
    private Toolbar toolbar;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private BillsAllFragment billAll;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private BillPresenter billPresenter;
    //Default user
    private int userId = 1;
    private boolean loggedIn = true;


    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String baseUrl = "http://oda.ft.dk/api/Sag?$orderby=id%20desc";
        String proposalExpand = "&$expand=Sagsstatus,Periode,Sagstype,SagAkt%C3%B8r,Sagstrin";
        String proposalFilter = "&$filter=(typeid%20eq%203%20or%20typeid%20eq%205)%20and%20periodeid%20eq%20146";
        String urlAsString = new StringBuilder().append(baseUrl).append(proposalExpand).append(proposalFilter).toString();
      new HttpAsyncTask(this, new AsyncTaskCompleteListener()).execute(urlAsString);

        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(R.string.asay);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.containerMain);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsMain);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setCurrentItem(0,true);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager){
            //change the color on the selected tab icon to black
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                super.onTabSelected(tab);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                super.onTabUnselected(tab);
            }
        });
        mViewPager.setCurrentItem(0, true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position) {
                case 1:
                    System.out.println("hej2");
                    bundle.putBoolean("isEnded",true);
                    BillsAllFragment Ended = new BillsAllFragment();
                    billAll = Ended;
                    Ended.setArguments(bundle);
                    return Ended;
                case 2:
                    System.out.println("hej3");
                    if(loggedIn){
                        bundle.putBoolean("isFavorite",true);
                        BillsAllFragment Favorites = new BillsAllFragment();
                        Favorites.setArguments(bundle);
                        return Favorites;
                    } else{
                        return null;
                    }
                case 0:
                    System.out.println("hej1");
                    BillsAllFragment billsAllFragment = new BillsAllFragment();
                    billAll = billsAllFragment;
                    return billsAllFragment;


                default:
                   return null;
            }
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.Search_hint));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.clearFocus();
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        billAll.getFilter().filter(newText);
        return true;
    }

    private class AsyncTaskCompleteListener implements asay.asaymobile.fetch.AsyncTaskCompleteListener<JSONObject> {
        @Override
        public void onTaskComplete(JSONObject result)
        {
            try{
                String str = "";
                if (result == null){
                    etResponse.setText("Result is null");
                }
                Log.d("OnTaskComplete", "onTaskComplete: " + result);
                JSONArray articles = result.getJSONArray("value"); // get articles array
                int actorId = 0;

                for (int i = 0; i < articles.length(); i++){
                    ArrayList<BillDTO.CaseStep> steps = new ArrayList<BillDTO.CaseStep>();
                    if(articles.getJSONObject(i).has("Sagstrin")){
                        JSONArray caseSteps = articles.getJSONObject(i).getJSONArray("Sagstrin");
                        for (int k = 0; k < caseSteps.length(); k++){
                            BillDTO.CaseStep step = new BillDTO.CaseStep(
                                    Double.valueOf(caseSteps.getJSONObject(k).getString("id")),
                                    caseSteps.getJSONObject(k).getString("titel"),
                                    caseSteps.getJSONObject(k).getString("dato"),
                                    Double.valueOf(caseSteps.getJSONObject(k).getString("typeid")),
                                    Double.valueOf(caseSteps.getJSONObject(k).getString("statusid")),
                                    caseSteps.getJSONObject(k).getString("opdateringsdato")
                            );
                            steps.add(step);
                        }
                    }
                    JSONArray actors = articles.getJSONObject(i).getJSONArray("SagAktør");
                    for(int j=0; j < actors.length(); j++){
                        System.out.println(actors.getJSONObject(j).getString("rolleid"));
                        if (actors.getJSONObject(j).getString("rolleid").equals("11")){
                            System.out.println(actors.getJSONObject(j).getString("aktørid"));
                            actorId = Integer.valueOf(actors.getJSONObject(j).getString("aktørid"));
                        }

                    }
                    System.out.println(articles.getJSONObject(i).getJSONObject("Sagsstatus").getString("status"));
                    BillDTO bill = new BillDTO(
                            " ",
                            articles.getJSONObject(i).getJSONObject("Periode").getString("slutdato"),
                            " ",
                            0,
                            Integer.valueOf(articles.getJSONObject(i).getString("id")),
                            articles.getJSONObject(i).getString("nummer"),
                            articles.getJSONObject(i).getString("titel"),
                            articles.getJSONObject(i).getString("titelkort"),
                            articles.getJSONObject(i).getString("resume"),
                            Integer.valueOf(articles.getJSONObject(i).getString("typeid")),
                            actorId,
                            articles.getJSONObject(i).getJSONObject("Sagsstatus").getString("status"),
                            steps
                    );
                    billPresenter.addNewBill(bill);
                }
            } catch (Exception excep){
                Log.d("JSON Exception", "onTaskComplete: " + excep.getMessage());
            }
        }

    }

}