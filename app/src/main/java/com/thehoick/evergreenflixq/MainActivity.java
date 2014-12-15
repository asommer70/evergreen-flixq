package com.thehoick.evergreenflixq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import org.jdom2.JDOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final int RESULT_SETTINGS = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    public static GridView mGridView;
    public static Context mContext;

    public static List<Dvd> mDvdList = new ArrayList<Dvd>();

    public static Boolean mGetNetflix = true;

    public static DvdAdapter mDvdCustomAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mGridView = (GridView)findViewById(R.id.dvds);
        //mGridView.setOnItemClickListener(mOnItemClickListener);

        Log.i(TAG, "MainActivity mGridView adapter: " + mGridView.getAdapter());

        if (mGetNetflix) {
            Netflix netflix = new Netflix();
            netflix.execute();
            //mGetNetflix = false;
        }

        /*if (mGridView.getAdapter() != null) {
            DvdAdapter dvdCustomAdapter = new DvdAdapter(mGridView.getContext(), mDvdList);
            mGridView.setAdapter(dvdCustomAdapter);
            //dvdCustomAdapter.notifyDataSetChanged();
        } *///else {
            //((DvdAdapter)mGridView.getAdapter()).refill(mDvdList);
            //MainActivity.mGridView.getAdapter().refill(MainActivity.mDvdList);
        //}


        //Log.i(TAG, "dvdList size: " + netflix.mDvds.size());


        //DvdAdapter dvdCustomAdapter = new DvdAdapter(this, mDvdList);
        //mGridView.setAdapter(dvdCustomAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        //this.setProgressBarIndeterminateVisibility(true);

        Log.i(TAG, "onResume mGridView.getAdapter: " + mGridView.getAdapter());

        Log.i(TAG, "mDvdList.size(): " + mDvdList.size());
        mGridView.setAdapter(mDvdCustomAdapter);
    }

    @Override
    public void onBackPressed() {
        //Log.i(TAG, "Back button pressed...");
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mGetNetflix = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            mGetNetflix = false;
            Intent i = new Intent(this, SettingsActivity.class);
            this.startActivityForResult(i, RESULT_SETTINGS);

            return true;
        } else if (id == R.id.action_refresh) {
            Netflix netflix = new Netflix();
            netflix.execute();
            mGetNetflix = true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ImageView dvdImage = (ImageView)view.findViewById(R.id.dvdImage);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mDvdList.get(position).getLink()));
            mContext.startActivity(intent);

        }
    };
}
