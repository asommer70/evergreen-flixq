package com.thehoick.evergreenflixq;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adam on 12/4/14.
 * Class for parsing HTML from Evergreen OPAC search results.
 */
public class Evergreen extends AsyncTask<Dvd, Void, String> {
    private static final String TAG = Evergreen.class.getSimpleName();

    public static String mLibraryUrlBegin = "/eg/opac/results?query=";
    public static String mLibraryUrlEnd = "&qtype=keyword&fi%3Asearch_format=dvd&sort=";

    protected String doInBackground(Dvd... args) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mContext);
        String libraryUrl = prefs.getString("libraryUrl", "");

        Dvd dvd = args[0];

        Document doc = null;
        try {
            doc = Jsoup.connect(libraryUrl + mLibraryUrlBegin + dvd.getTitle() + mLibraryUrlEnd)
                    .timeout(6000).get();
        } catch (IOException e) {
            e.printStackTrace();
            Netflix.mLibraryUrlProblem = true;
        }

        Pattern p = Pattern.compile(dvd.getTitle().toLowerCase(), Pattern.CASE_INSENSITIVE);

        Elements zero_search_hits;
        if (doc != null) {
            zero_search_hits = doc.select("#zero_search_hits");
            if (zero_search_hits.size() != 0) {
                dvd.setStatus("Not Available");
                dvd.setLibraryGotten(true);
            } else {

                Elements results = doc.select(".result_metadata");
                for (org.jsoup.nodes.Element result : results) {

                    Elements record_title = result.select(".record_title");
                    Matcher m = p.matcher(record_title.text().toLowerCase());
                    boolean b = m.find();

                    if (b) {
                        dvd.setStatus("Available");
                        dvd.setEvergreenLink(libraryUrl + record_title.attr("href"));

                        Elements libraries = result.select("a[typeof=\"Library\"");
                        List<Library> libraryList = new ArrayList<Library>();

                        if (libraries.size() == 0) {
                            Elements result_counts = result.select(".result_count");
                            for (org.jsoup.nodes.Element res : result_counts) {
                                Library library = new Library();

                                String library_name[] = res.text().split(" at ");
                                library.setName(library_name[1]);

                                library.setStatus("Unknown");

                                libraryList.add(library);
                                dvd.setLibraryGotten(true);
                            }
                        } else {
                            for (org.jsoup.nodes.Element a : libraries) {
                                Library library = new Library();

                                String library_name = a.children().get(0).text();
                                library.setName(library_name);

                                String status = a.parent().nextElementSibling()
                                        .nextElementSibling().nextElementSibling().text();
                                library.setStatus(status);
                                libraryList.add(library);
                                //Log.i(TAG, "Library: " + library_name + " status: " + status);
                            }
                        }

                        dvd.setLibaries(libraryList);
                        dvd.setLibraryGotten(true);
                        break;
                    } else {
                        dvd.setEvergreenLink(libraryUrl + mLibraryUrlBegin + dvd.getTitle() +
                                mLibraryUrlEnd);
                        dvd.setStatus("Not Available");
                        dvd.setLibraryGotten(true);
                    }
                }
            }
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected void onPostExecute(String res) {

        MainActivity.mDvdCustomAdapter.notifyDataSetChanged();
    }
}
