---
layout: post
title:  "Developing Evergreen FlixQ"
date:   2014-12-23 16:30:00
categories: dev
---

## The Idea

It may seem strange that the first time I got a library card was in my mid thirties.  I guess having a young child expands your horizons.  

Since finding out that they have "babes on blankets" story time twice a week Anden and I have been going to the local library at least once a week.  I was totally surprised, though I really shouldn't have been, that they have quite a large collection of DVDs you can check out.  Hello Game of Thrones!

Over the last six months or so I've checked out all the "good" DVDs they have and was wanting to find more good titles.  You know stuff with by the Coen brothers, or with lots of action.

I know I should develop an App to help me pick out DVDs.

## Enter the FlixQ

After the massive success of my first Android app [Barcodepost](http://barcodepost.thehoick.com) I'm hoping this latest app will actually be used by people.  In all fairness there is a ton of free apps that can scan a barcode.

The latest app [Evergreen FlixQ](http://evergreenflixq.thehoick.com) will search an [Evergreen ILS](http://evergreen-ils.org) catalog for DVDs in your Netflix DVD Queue.  All you need to do is configure the URL for your Netflix Queue's RSS feed and the base URL for your libraries Evergreen catalog.

Boom, Bob's Your Uncle!

## Developing the App

It took about two weeks to develop the app give or take.  I had initially planned on doing most of the development over a weekend where my wife and son were out of town.  This being the first weekend alone in over a year I spent most of the time watching movies and relaxing, but did manage to work on some Java classes to parse the Netflix RSS feed.

I spent a few days spinning my wheels trying to figure out a way to query Evergreen ILS using a REST API, or something that would give me data in a format other then HTML.  I wasn't able to find anything on my local libraries catalog which is part of the state of North Carolina's system.

Thankfully the awesome [jsoup](http://jsoup.org/) library makes parsing HTML as easy as... well as easy as soup.  I had to figure out what data to grab from the massive listing for each item and all the accompanying HTML.

## Problems Encountered

There wasn't a lot of major problems while developing Evergreen FlixQ, but there was one very frustrating problem that I really should have fixed quicker.  But I guess then it wouldn't have been that frustrating.

What I was doing was looping through the results of a query against Evergreen and creating a Java List containing objects I had modeled.

The problem I encountered is one I remember from first learning how to program.  That is when you create a list, or array, of something and each item is the same and it's the last item in the list you're looping through.  Usually this happens when you declare the list you're adding two inside the loop.  A simple enough problem once you've come across it once.

After staring at the code for an embarrassing amount of time I was darn sure that I was creating the List outside the loop.  Even went so far as trying multiple ways to call the list.

Eventually I found the solution.  The problem was that in the class definition for the objects that I was adding to the List, in this case a DVD object, I had declared all the member variables **static**.  It's totally down to my lack of developing real classes for actual programs (I've mostly done scripting and web apps) that I didn't quite understand what the **static** key word does.

When declaring a variable **static** the value of the variable is the same for all objects instantiated with the class.  Or at least that's my understanding of how **static** works in Java.  Once I removed the staticness things worked like I always thought they should.

## Conclusion

I had a lot of fun developing this little app, and I feel like my Java chops are getting better and better.  I might even be a real developer at some point.

If you're local library uses the Evergreen ILS system, please give [Evergreen FlixQ](https://play.google.com/store/apps/details?id=com.thehoick.evergreenflixq) a chance and let me know what you think.

Party On!
