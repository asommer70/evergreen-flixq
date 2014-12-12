package com.thehoick.evergreenflixq;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jdom2.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
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

    public static String mLibraryUrlBegin = "/eg/opac/results?query=";
    public static String mLibraryUrlEnd = "&qtype=keyword&fi%3Asearch_format=dvd&locg=126&sort=";

    protected String doInBackground(String... args) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mContext);
        String libraryUrl = prefs.getString("libraryUrl", "");

        for (int i = 0; i < MainActivity.mDvdList.size(); i++) {

            Dvd dvd = MainActivity.mDvdList.get(i);

            Log.i(TAG, "evergreen title: " + dvd.getTitle());

            Document doc = null;
            try {
                doc = Jsoup.connect(libraryUrl + mLibraryUrlBegin + dvd.getTitle() + mLibraryUrlEnd)
                        .timeout(6000).get();
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
                    break;
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

                            dvd.setEvergreenLink("http://appalachian.nccardinal.org" +
                                    record_title.attr("href"));
                            Log.i(TAG, "The library has a copy of: " + dvd.getTitle());
                            //Log.i(TAG, record_title.attr("href"));

                            Elements libraries = doc.select("a[typeof=\"Library\"");
                            List<Library> libraryList = new ArrayList<Library>();
                            for (org.jsoup.nodes.Element a : libraries) {
                                Library library = new Library();

                                String library_name = a.children().get(0).text();
                                library.setName(library_name);

                                String status = a.parent().nextElementSibling()
                                        .nextElementSibling().nextElementSibling().text();
                                library.setStatus(status);
                                libraryList.add(library);
                                Log.i(TAG, "Library: " + library_name + " status: " + status);
                            }

                            dvd.setLibaries(libraryList);
                            break;
                        } else {
                          dvd.setEvergreenLink(Netflix.mCardUrlBegin + dvd.getTitle() +
                                  Netflix.mCardUrlEnd);
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
