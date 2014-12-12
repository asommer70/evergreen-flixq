package com.thehoick.evergreenflixq;

import android.util.Log;

import org.jdom2.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adam on 12/4/14.
 * Class for parsing HTML from Evergreen OPAC search results.
 */
public class Evergreen implements Runnable {
    private static final String TAG = Evergreen.class.getSimpleName();

    public void run() {
        Document doc = null;
        try {
            doc = Jsoup.connect(Netflix.mCardUrlBegin + Netflix.mTitle + Netflix.mCardUrlEnd).timeout(6000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pattern p = Pattern.compile(Netflix.mTitle.toLowerCase(), Pattern.CASE_INSENSITIVE);

        Elements zero_search_hits = null;
        if (doc != null) {
            zero_search_hits = doc.select("#zero_search_hits");
            if (zero_search_hits.size() != 0) {
                Netflix.mDvd.setStatus("Not Available");
                Log.i(TAG, "Sorry nothing for: " + Netflix.mTitle);
            } else {
                Log.i(TAG, "We might have something for: " + Netflix.mTitle);

                Elements record_titles = doc.select(".record_title");
                for (int i = 0; i < record_titles.size(); i++) {

                    Matcher m = p.matcher(record_titles.get(i).text().toLowerCase());
                    boolean b = m.find();

                    if (b) {
                        // Check the actual status option in the table.
                        Netflix.mDvd.setStatus("Available");
                        // Or 'Checked Out'

                        Netflix.mDvd.setLink(record_titles.get(i).attr("href"));
                        Log.i(TAG, "The library has a copy of: " + Netflix.mTitle);
                        Log.i(TAG, record_titles.get(i).attr("href"));
                    }
                }
            }
        }

         /*Elements barcodeImages = doc.select(".info_image a img");
         String barcodeImgUrl = barcodeImages.get(0).attr("src");

         Elements barcodeLinks = doc.select(".info_image a");
         String barcodeTitle = barcodeLinks.get(0).attr("title");

         System.out.println(barcodeLinks.get(0).attr("title"));
*/
    }
}
