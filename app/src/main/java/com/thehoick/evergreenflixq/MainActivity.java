package com.thehoick.evergreenflixq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.jdom2.JDOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final int RESULT_SETTINGS = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private int mOffset;
    private int mIndex;

    public static ProgressDialog mDialog;
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


        if (checkUrls()) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
        } else {

            mDialog = new ProgressDialog(MainActivity.mContext);
            mDialog.setMessage("Please wait");
            mDialog.show();

            Log.i(TAG, "MainActivity mGridView adapter: " + mGridView.getAdapter());

            if (mGetNetflix) {
                Netflix netflix = new Netflix();
                netflix.execute();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Log.i(TAG, "onResume mGridView.getAdapter: " + mGridView.getAdapter());
        //Log.i(TAG, "mDvdList.size(): " + mDvdList.size());

        if (checkUrls()) {
            Intent intent = new Intent(this, SettingsActivity.class);
            this.startActivity(intent);
        }

        mGridView.setAdapter(mDvdCustomAdapter);

        mGridView.setSelection(mIndex);
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
        mOffset = (int)(mGridView.getVerticalSpacing() * getResources().getDisplayMetrics().density);
        mIndex = mGridView.getFirstVisiblePosition();
        final View first = mGridView.getChildAt(0);
        if (first != null) {
            mOffset -= first.getTop();
        }

        //mIndex = MainActivity.mGridView.getFirstVisiblePosition();

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
            if (checkUrls()) {
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
            } else {
                Netflix netflix = new Netflix();
                netflix.execute();
                mGetNetflix = true;
            }
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

    @Override
    protected void onStop() {
        super.onStop();

        if (mDialog != null)
            mDialog.dismiss();
    }

    private boolean checkUrls() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String netflixUrl = prefs.getString("netflixUrl", "");
        String evergreenUrl = prefs.getString("libraryUrl", "");

        boolean problem = false;
        if (netflixUrl.equals("")) {
            Toast.makeText(this, R.string.netflix_config_warning, Toast.LENGTH_LONG).show();
            problem = true;
        }

        if (evergreenUrl.equals("")) {
            Toast.makeText(this, R.string.evergreen_config_warning, Toast.LENGTH_LONG).show();
            problem = true;
        }

        if (problem) {
            return true;
        } else {
            return false;
        }
    }
}
