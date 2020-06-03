/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.themajoritytwitterbot;

import org.apache.commons.io.FileUtils;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.api.TweetsResources;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static void main(String args[]) {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb = cbProperties(cb); //all the properties in their own method
        TwitterFactory tf = new TwitterFactory(cb.build());
        TweetsResources tr = tf.getInstance().tweets();
        timeToTweet(tr);

    }

    public static ConfigurationBuilder cbProperties(ConfigurationBuilder cb) {
        /*
            Builds the object that will interact with the Twitter API
        */
        try {
            System.out.println("Loading api keys...");
            cb.setDebugEnabled(true);

            File api = File.createTempFile("api", ".txt");
            InputStream apiStream = App.class.getClassLoader().getResourceAsStream("api.txt");
            FileUtils.copyInputStreamToFile(apiStream, api);
            //https://www.tutorialspoint.com/java/io/file_createtempfile_directory.htm
            //reads twitter api credentials

            BufferedReader rdr = new BufferedReader(new FileReader(api));
            String oack = rdr.readLine();
            String oacks = rdr.readLine();
            String oaat = rdr.readLine();
            String oaas = rdr.readLine();
            //naming conventions :)

            rdr.close();
            apiStream.close(); //cleaning up

            cb.setOAuthConsumerKey(oack);
            cb.setOAuthConsumerSecret(oacks);
            cb.setOAuthAccessToken(oaat);
            cb.setOAuthAccessTokenSecret(oaas);

            System.out.println("API key loaded!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cb;
    }

    public void reply() {
        //TODO: Add function to reply to new tweets
        /*
            every 5 minutes, check if psu has tweeted and store the most recent tweet
            if its different than the currently stored one, reply.
         */
    }

    public static String getDateDifference() {
         /*
            Calculates the difference between the video's emergence and current time.
            video posted at 7:36pm may 31st - https://twitter.com/YonceLipa/status/1267238505002020865

            date psu decided to do nothing - https://twitter.com/penn_state/status/1267980418256711680/photo/1
         */

        LocalDateTime now = LocalDateTime.now();
        //LocalDateTime incident = LocalDateTime.of(2020, Month.MAY, 31, 19, 36);
        LocalDateTime nothingBurger = LocalDateTime.of(2020, Month.JUNE, 2, 20, 44);

        Duration duration = Duration.between(nothingBurger, now);
        String durationString = duration.toString();
        //gets the difference as a duration object and converts to string.

        String hours = durationString.split("H")[0].substring(2);
        String minutes = durationString.split("M")[0].substring(5);

        return hours + " hours and " + minutes + " minutes";

    }

    public static void tweetWriter(TweetsResources tr) {
        /*
            Handles writing and posting the tweet via API calls.
        */
        try {
            System.out.println("buliding tweet...");

            InputStream seanPicStream = App.class.getClassLoader().getResourceAsStream("seansetnick.PNG");
            File seanPic = File.createTempFile("seansetnick", ".PNG");
            FileUtils.copyInputStreamToFile(seanPicStream, seanPic);
            //creating the picture to be attached
            seanPicStream.close(); //cleaning up

            InputStream nothingStream = App.class.getClassLoader().getResourceAsStream("statement.JPG");
            File nothingBurger = File.createTempFile("statement", ".JPG");
            FileUtils.copyInputStreamToFile(nothingStream, nothingBurger);
            //creating the picture to be attached
            nothingStream.close(); //cleaning up

            String dateString = getDateDifference();
            //calling method that gets the date diff to be added
            //String tweet = "It's been " + dateString + " since this video surfaced and @penn_state still hasn't expelled Sean Setnick.";
            String tweet = "It's been " + dateString + " since @penn_state forgot about their Code of Conduct, the Smeal Honor Code, and the comfort and safety of students of color.";

            long[] mediaIds = new long[2];
            UploadedMedia media1 = tr.uploadMedia(seanPic);
            mediaIds[0] = media1.getMediaId();
            UploadedMedia media2 = tr.uploadMedia(nothingBurger);
            mediaIds[1] = media2.getMediaId();

            StatusUpdate newTweet = new StatusUpdate(tweet);
            newTweet.setMediaIds(mediaIds);
            tr.updateStatus(newTweet); //calls to the api

            System.out.println(dateString + "new tweet posted");

        } catch (TwitterException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            e.printStackTrace();
            //from http://twitter4j.org/javadoc/twitter4j/api/TweetsResources.html
        }
    }

    public static void timeToTweet(TweetsResources tr) {
        /*
            Repeats the task as necessary at minute intervals, as a runnable.
        */

        System.out.println("About to tweet...");

        Runnable runningTweetWriter = new Runnable() {
            @Override
            public void run() {
                tweetWriter(tr);
            }
        };

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(runningTweetWriter, 0, 13, TimeUnit.MINUTES);
        //sends a new tweet every 13 minutes indefinitely

        //https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html
        //https://stackoverflow.com/questions/33073671/how-to-execute-a-method-every-minute

    }

}
