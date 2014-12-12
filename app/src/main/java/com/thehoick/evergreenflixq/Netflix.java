package com.thehoick.evergreenflixq;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.widget.GridView;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 12/5/14.
 * Parses the RSS feed from Netflix DVD Queue.
 */
public class Netflix extends AsyncTask<String, Object, List<Dvd>> {
//public class Netflix extends AsyncTask<String, Void, String> {

    private static final String TAG = Netflix.class.getSimpleName();

    public static String mCardUrlBegin = "http://appalachian.nccardinal.org/eg/opac/results?query=";
    public static String mCardUrlEnd = "&qtype=keyword&fi%3Asearch_format=dvd&locg=126&sort=";
    public static String mTitle;
    public static Dvd mDvd;

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
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);
        // Starts the query
        try {
            conn.connect();
            stream = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse the feed XML.
        SAXBuilder jdomBuilder = new SAXBuilder();

        // jdomDocument is the JDOM2 Object
        Document jdomDocument = null;

        try {
            jdomDocument = jdomBuilder.build(stream);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Dvd> dvds = new ArrayList<Dvd>();
        if (jdomDocument != null) {

            Element rss = jdomDocument.getRootElement();
            Element channel = rss.getChild("channel");

            List<Element> channelChildren = channel.getChildren("item");

            for (int i = 0; i < 9; i++) {
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
                dvd.setDescription(descHtml.text());

                dvd.setLink(link.attr("href"));
                dvd.setImgUrl(img.attr("src").replace("small", "large"));

                dvds.add(dvd);
            }
        }

        return dvds;
    }

    protected void onPostExecute(List<Dvd> dvds) {
        MainActivity.mDvdList = dvds;

        Log.i(TAG, "dvds size: " + dvds.size());

        DvdAdapter dvdCustomAdapter = new DvdAdapter(MainActivity.mContext, dvds);
        MainActivity.mGridView.setAdapter(dvdCustomAdapter);

        Evergreen evergreen = new Evergreen();
        evergreen.execute();
    }

}
