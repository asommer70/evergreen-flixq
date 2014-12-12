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

    public static List<Dvd> mDvdList = new ArrayList<Dvd>();;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mGridView = (GridView)findViewById(R.id.dvds);
        mGridView.setOnItemClickListener(mOnItemClickListener);

        Netflix netflix = new Netflix();
        netflix.execute();


        //Log.i(TAG, "dvdList size: " + netflix.mDvds.size());


        //DvdAdapter dvdCustomAdapter = new DvdAdapter(this, mDvdList);
        //mGridView.setAdapter(dvdCustomAdapter);
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
            Intent i = new Intent(this, SettingsActivity.class);
            this.startActivityForResult(i, RESULT_SETTINGS);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ImageView checkImageView = (ImageView)view.findViewById(R.id.dvdImage);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mDvdList.get(position).getLink()));
            mContext.startActivity(intent);

        }
    };
}
