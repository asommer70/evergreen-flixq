package com.thehoick.evergreenflixq;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
public class Evergreen extends AsyncTask<Dvd, Void, String> {
    private static final String TAG = Evergreen.class.getSimpleName();

    public static String mLibraryUrlBegin = "/eg/opac/results?query=";
    public static String mLibraryUrlEnd = "&qtype=keyword&fi%3Asearch_format=dvd&locg=126&sort=";

    protected String doInBackground(Dvd... args) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.mContext);
        String libraryUrl = prefs.getString("libraryUrl", "");

        // Need to fire this AsyncTask for each DVD and not loop through each DVD in the task.
        //for (int i = 0; i < MainActivity.mDvdList.size(); i++) {

            //Dvd dvd = MainActivity.mDvdList.get(i);
            Dvd dvd = args[0];

            Log.i(TAG, "evergreen title: " + dvd.getTitle());

            Document doc = null;
            try {
                doc = Jsoup.connect(libraryUrl + mLibraryUrlBegin + dvd.getTitle() + mLibraryUrlEnd)
                        .timeout(6000).get();
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(MainActivity.mContext, "There is a problem with your Library URL.", Toast.LENGTH_LONG).show();
                Netflix.mLibraryUrlProblem = true;
            }

            Pattern p = Pattern.compile(dvd.getTitle().toLowerCase(), Pattern.CASE_INSENSITIVE);

            Elements zero_search_hits = null;
            if (doc != null) {
                zero_search_hits = doc.select("#zero_search_hits");
                if (zero_search_hits.size() != 0) {
                    dvd.setStatus("Not Available");
                    Log.i(TAG, "Sorry nothing for: " + dvd.getTitle());
                    //break;
                } else {
                    //Log.i(TAG, "We might have something for: " + dvd.getTitle());

                    Elements results = doc.select(".result_metadata");
                    for (org.jsoup.nodes.Element result : results) {

                        Elements record_title = result.select(".record_title");
                        Matcher m = p.matcher(record_title.text().toLowerCase());
                        boolean b = m.find();

                        if (b) {
                            // Check the actual status option in the table.
                            dvd.setStatus("Available");
                            // Or 'Checked Out'

                            dvd.setEvergreenLink("http://appalachian.nccardinal.org" +
                                    record_title.attr("href"));
                            //Log.i(TAG, "The library has a copy of: " + dvd.getTitle());
                            //Log.i(TAG, result.attr("href"));

                            Elements libraries = result.select("a[typeof=\"Library\"");
                            List<Library> libraryList = new ArrayList<Library>();

                            if (libraries.size() == 0) {
                                Elements result_counts = result.select(".result_count");
                                for (org.jsoup.nodes.Element res : result_counts) {
                                    Library library = new Library();

                                    String library_name[] = res.text().split(" at ");
                                    library.setName(library_name[1]);
                                    System.out.println("Library: " + library.getName());

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
                                    Log.i(TAG, "Library: " + library_name + " status: " + status);
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
        //}
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //mProgressDialog = ProgressDialog.show(MainActivity.mContext, "Loading",
        //        "Fetching information from Evergreen...", true);

    }

    protected void onPostExecute(String res) {

        //mProgressDialog.dismiss();
        //DvdAdapter dvdCustomAdapter = new DvdAdapter(MainActivity.mContext, MainActivity.mDvdList);
        //dvdCustomAdapter.notifyDataSetChanged();
        //MainActivity.mGridView.setAdapter(dvdCustomAdapter);


        /*if (mUrlProblem) {
            Toast.makeText(MainActivity.mContext, "There is a problem with your Library URL.",
                    Toast.LENGTH_LONG).show();
        }*/

        MainActivity.mDvdCustomAdapter.notifyDataSetChanged();
    }
}
