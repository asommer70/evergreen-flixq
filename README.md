evergreen-flixq
===============

# Overview
Lookup DVDs in your [Netflix DVD Queue](http://dvd.netflix.com/Queue?lnkctr=mhbque&qtype=DD) at your local library.  If they use the [Evergreen ILS](http://evergreen-ils.org/) that is.


Configure the DVD Queue RSS feed and the URL for your local library catalog and the lookup will commence.


# Configuration

## Netflix DVD Queue RSS URL

* To find the RSS feed URL for your Netflix DVD Queue open your browser and go to [netflix.com](http://netflix.com). If you are on a phone you may see the mobile version of the website, but we want the full version so scroll down to the bottom and click the **"View Full Site"** link.
* Scroll to the bottom of the full site and click the **"RSS Feeds"** link.
* On the **Feeds** page the link we are looking for is the **"Queue"** link.  Go ahead and give it a clicky click (or touchy touch as the case may be).
* You should now see a page with a lot of funny text, this is exactly the page we are looking for. Go to the address bar of the browser and **copy** the entire URL.
* Open Evergreen FlixQ back up and **paste** the URL into the Netflix RSS URL text box.


## Evergreen ILS URL

* To find the URL for your local, or statewide, library open a web browser and go to your libraries website. There is usually a link for the *"Search Catalog"*, *"Library Catalog"*, or wherever you can search for things.
* **Note:** your library has to use the Evergreen ILS system for this app to work.
* Once you have found the link to your library's Evergreen **copy** the base part of the URL.  That is everything before the first forward slash (**"/"**).
* Now just **paste** that sweet text into the **"Library Search URL"** text box back in the Evergreen FlixQ app.
* 

# Things that could be improved.

* Could follow DVD links in Evergreen if the search results don't give the status (Available, Checked Out, etc) in the search results.
* The search result match code is pretty basic and creates more false positives then necessary.  The problem is libraries add additional descriptive text to the DVD's description.
* Would be awesome to figure out a query API for Evergreen ILS so the app wouldn't have to rely on parsing the returned HTML.  Might also make DVD lookup a lot faster.
* A better launcher icon for the app would be great.


