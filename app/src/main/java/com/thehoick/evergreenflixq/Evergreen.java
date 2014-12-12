package com.thehoick.evergreenflixq;

import android.os.AsyncTask;
import android.util.Log;

import org.jdom2.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adam on 12/4/14.
 * Class for parsing HTML from Evergreen OPAC search results.
 */
//public class Evergreen implements Runnable {
public class Evergreen extends AsyncTask<String, Void, String> {
    private static final String TAG = Evergreen.class.getSimpleName();

    protected String doInBackground(String... args) {

        for (int i = 0; i < MainActivity.mDvdList.size(); i++) {

            Dvd dvd = MainActivity.mDvdList.get(i);

            Log.i(TAG, "evergreen title: " + dvd.getTitle());

            Document doc = null;
            try {
                doc = Jsoup.connect(Netflix.mCardUrlBegin + dvd.getTitle() + Netflix.mCardUrlEnd).timeout(6000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Pattern p = Pattern.compile(dvd.getTitle().toLowerCase(), Pattern.CASE_INSENSITIVE);

            Elements zero_search_hits = null;
            if (doc != null) {
                zero_search_hits = doc.select("#zero_search_hits");
                if (zero_search_hits.size() != 0) {
                    dvd.setStatus("Not Available");
                    Log.i(TAG, "Sorry nothing for: " + dvd.getTitle());
                } else {
                    //Log.i(TAG, "We might have something for: " + dvd.getTitle());

                    Elements record_titles = doc.select(".record_title");
                    for (org.jsoup.nodes.Element record_title : record_titles) {

                        Matcher m = p.matcher(record_title.text().toLowerCase());
                        boolean b = m.find();

                        if (b) {
                            // Check the actual status option in the table.
                            dvd.setStatus("Available");
                            // Or 'Checked Out'

                            dvd.setEvergreenLink("http://appalachian.nccardinal.org" + record_title.attr("href"));
                            Log.i(TAG, "The library has a copy of: " + dvd.getTitle());
                            Log.i(TAG, record_title.attr("href"));
                            break;
                        } else {
                          dvd.setEvergreenLink(Netflix.mCardUrlBegin + dvd.getTitle() + Netflix.mCardUrlEnd);
                          dvd.setStatus("Not Available");
                        }
                    }
                }
            }
        }
        return null;
    }

    protected void onPostExecute(String res) {

        DvdAdapter dvdCustomAdapter = new DvdAdapter(MainActivity.mContext, MainActivity.mDvdList);
        MainActivity.mGridView.setAdapter(dvdCustomAdapter);

    }
}
