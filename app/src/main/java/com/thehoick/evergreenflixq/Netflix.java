package com.thehoick.evergreenflixq;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 12/5/14.
 * Parses the RSS feed from Netflix DVD Queue.
 */
public class Netflix extends AsyncTask<String, Object, List<Dvd>> {

    private static final String TAG = Netflix.class.getSimpleName();
    private boolean mUrlProblem = false;

    public static boolean mLibraryUrlProblem = false;

    @Override
    protected List<Dvd> doInBackground(String... arg) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mContext);
        String qUrl = prefs.getString("netflixUrl", "");

        // Get the RSS feed content.
        InputStream stream = null;
        URL url = null;
        try {
            url = new URL(qUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mUrlProblem = true;
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            mUrlProblem = true;
        }
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
            mUrlProblem = true;
        }
        conn.setDoInput(true);
        // Starts the query
        try {
            conn.connect();
            //Log.i(TAG, "Getting RSS from Netflix...");
            stream = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            mUrlProblem = true;
        }

        // Parse the feed XML.
        SAXBuilder jdomBuilder = new SAXBuilder();

        // jdomDocument is the JDOM2 Object
        Document jdomDocument = null;

        try {
            jdomDocument = jdomBuilder.build(stream);
        } catch (JDOMException e) {
            e.printStackTrace();
            mUrlProblem = true;
        } catch (IOException e) {
            e.printStackTrace();
            mUrlProblem = true;
        }

        List<Dvd> dvds = new ArrayList<Dvd>();
        if (jdomDocument != null) {

            Element rss = jdomDocument.getRootElement();
            Element channel = rss.getChild("channel");

            List<Element> channelChildren = channel.getChildren("item");

            for (int i = 0; i < channelChildren.size(); i++) {
            //for (int i = 0; i < 3; i++) {
                Element channelChild = channelChildren.get(i);
                Element titleElement = channelChild.getChild("title");
                Element description = channelChild.getChild("description");

                // Grab the image src and link href from the RSS description element.
                org.jsoup.nodes.Document descHtml = Jsoup.parse(description.getValue());
                Elements img = descHtml.select("img");
                Elements link = descHtml.select("a");
                img.remove();
                link.remove();

                // Setup the DVD object.
                Dvd dvd = new Dvd();
                dvd.setTitle(titleElement.getValue().substring(5));
                dvd.setDescription(descHtml.text() + "\n");

                dvd.setLink(link.attr("href"));
                dvd.setImgUrl(img.attr("src").replace("small", "large"));

                dvds.add(dvd);
            }
        }

        MainActivity.mDvdList = dvds;
        return dvds;
    }


    protected void onPostExecute(List<Dvd> dvds) {
        if (MainActivity.mDialog.isShowing()) {
            MainActivity.mDialog.dismiss();
        }

        if (mUrlProblem) {
            Toast.makeText(MainActivity.mContext, "There is a problem with your Netflix URL.",
                    Toast.LENGTH_LONG).show();
        } else {


            MainActivity.mDvdList = dvds;



            if (MainActivity.mGetNetflix) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mContext);
                String libraryUrl = prefs.getString("libraryUrl", "");

                // Check for https
                if (libraryUrl.contains("https") || mLibraryUrlProblem) {
                    Toast.makeText(MainActivity.mContext, "There is a problem with your Library URL.",
                            Toast.LENGTH_LONG).show();

                } else {
                    for (Dvd dvd : MainActivity.mDvdList) {
                        Evergreen evergreen = new Evergreen();
                        evergreen.execute(dvd);
                    }
                }

                MainActivity.mDvdCustomAdapter = new DvdAdapter(MainActivity.mContext, dvds);
                MainActivity.mGridView.setAdapter(MainActivity.mDvdCustomAdapter);

                MainActivity.mGetNetflix = false;
            } else {
                ((DvdAdapter) MainActivity.mGridView.getAdapter()).refill(MainActivity.mDvdList);
            }
        }
    }

    public static boolean ismLibraryUrlProblem() {
        return mLibraryUrlProblem;
    }

    public static void setmLibraryUrlProblem(boolean mLibraryUrlProblem) {
        Netflix.mLibraryUrlProblem = mLibraryUrlProblem;
    }

}
