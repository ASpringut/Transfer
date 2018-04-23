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
import android.view.Menu;
import android.view.MenuItem;

import com.polarizedraven.transfer.loader.StationLoader;
import com.polarizedraven.transfer.trainfragment.TrainFragment;

public class StationViewer extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String KEY_LINE_ID = "KEY_LINE_ID";
    public static final String KEY_STOP = "KEY_STATION";
    public static final String KEY_LINE = "KEY_LINE";

    private static final int STATION_LOADER_ID = 0;

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
        Line line = (Line) getIntent().getSerializableExtra(StationViewer.KEY_LINE);
        setTheme(line.getTheme());
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
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor c) {
        switch (loader.getId()) {
            case STATION_LOADER_ID:
                setupStations(c);
                return;
            default:
                return;
        }
    }

    private void setupStations(Cursor c) {
        c.moveToFirst();
        toolbar.setTitle(c.getString(c.getColumnIndex(StopsDatabase.STOP_COLUMN)));
        String firstTerminus = c.getString(c.getColumnIndex(StopsDatabase.DIRECTION1));
        String secondTerminus = c.getString(c.getColumnIndex(StopsDatabase.DIRECTION2));
        String firstLayout = c.getString(c.getColumnIndex(StopsDatabase.LAYOUT1));
        String secondLayout = c.getString(c.getColumnIndex(StopsDatabase.LAYOUT2));


        stationAdapter = new StationAdapter(getSupportFragmentManager(), firstTerminus, secondTerminus, firstLayout, secondLayout);
        mViewPager.setAdapter(stationAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.getTabAt(0).setText(firstTerminus);
        mTabLayout.getTabAt(1).setText(secondTerminus);
    }

    @Override
    public void onLoaderReset(Loader loader) {}

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class StationAdapter extends FragmentPagerAdapter {

        private String firstTerminus;
        private String secondTerminus;
        private String firstLayout;
        private String secondLayout;

        public StationAdapter(FragmentManager fm, String startTerminus, String endTerminus,
                              String firstLayout, String secondLayout) {
            super(fm);
            this.firstTerminus = startTerminus;
            this.secondTerminus = endTerminus;
            this.firstLayout = firstLayout;
            this.secondLayout = secondLayout;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return firstTerminus;
            } else {
                return secondTerminus;
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return TrainFragment.newInstance(firstTerminus, firstLayout);
            } else {
                return TrainFragment.newInstance(secondTerminus, secondLayout);
            }
        }

        @Override
        public int getCount() {
            return 2;//always two ends
        }
    }
}
