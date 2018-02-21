package com.polarizedraven.transfer;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.TextView;

import com.polarizedraven.transfer.loader.StationLoader;
import com.polarizedraven.transfer.loader.TerminusLoader;
import com.polarizedraven.transfer.view.TrainView;

public class StationViewer extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String KEY_LINE_ID = "KEY_LINE_ID";
    public static final String KEY_STOP = "KEY_STATION";

    private static final int STATION_LOADER_ID = 0;
    private static final int TERMINUS_LOADER_ID = 1;

    private Toolbar toolbar;
    private int line_id;
    private String stop;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private StationAdapter stationAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_station);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);


        line_id = getIntent().getIntExtra(StationViewer.KEY_LINE_ID,0);
        stop = getIntent().getStringExtra(StationViewer.KEY_STOP);

        getLoaderManager().initLoader(STATION_LOADER_ID,null, this);
        getLoaderManager().initLoader(TERMINUS_LOADER_ID,null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_station, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        switch (i){
            case STATION_LOADER_ID:
                return new StationLoader(this, line_id, stop);
            case TERMINUS_LOADER_ID:
                return new TerminusLoader(this, line_id);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor c) {
        switch (loader.getId()) {
            case STATION_LOADER_ID:
                c.moveToFirst();
                toolbar.setTitle(c.getString(c.getColumnIndex(StopsDatabase.STOP_COLUMN)));
                return;
            case TERMINUS_LOADER_ID:
                setupStations(c);
            default:
                return;
        }
    }

    private void setupStations(Cursor c) {
        c.moveToFirst();
        String startTerminus = c.getString(c.getColumnIndex(StopsDatabase.STOP_COLUMN));
        c.moveToNext();
        String endTerminus = c.getString(c.getColumnIndex(StopsDatabase.STOP_COLUMN));

        stationAdapter = new StationAdapter(getSupportFragmentManager(), startTerminus, endTerminus);
        mViewPager.setAdapter(stationAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.getTabAt(0).setText(startTerminus);
        mTabLayout.getTabAt(1).setText(endTerminus);
    }

    @Override
    public void onLoaderReset(Loader loader) {}

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_TITLE = "title";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String title) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, title);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_station, container, false);
            TrainView trainView = (TrainView) rootView.findViewById(R.id.train_diagram);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class StationAdapter extends FragmentPagerAdapter {

        private String startTerminus;
        private String endTerminus;

        public StationAdapter(FragmentManager fm, String startTerminus, String endTerminus) {
            super(fm);
            this.startTerminus = startTerminus;
            this.endTerminus = endTerminus;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return startTerminus;
            } else {
                return endTerminus;
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return PlaceholderFragment.newInstance(startTerminus);
            } else {
                return PlaceholderFragment.newInstance(endTerminus);
            }
        }

        @Override
        public int getCount() {
            return 2;//always two ends
        }
    }
}
