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
import twitter4j.api.TweetsResources;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
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
        cb.setDebugEnabled(true);

        File api = null;
        try {
            api = File.createTempFile("api", ".txt");
            //https://www.tutorialspoint.com/java/io/file_createtempfile_directory.htm

            InputStream apiStream = App.class.getClassLoader().getResourceAsStream("api.txt");
            FileUtils.copyInputStreamToFile(apiStream, api);
            BufferedReader rdr = new BufferedReader(new FileReader(api));
            String oack = rdr.readLine();
            String oacks = rdr.readLine();
            String oaat = rdr.readLine();
            String oaas = rdr.readLine();

            cb.setOAuthConsumerKey(oack);
            cb.setOAuthConsumerSecret(oacks);
            cb.setOAuthAccessToken(oaat);
            cb.setOAuthAccessTokenSecret(oaas);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return cb;
    }

    public void reply() {
        //TODO: Add function to reply to new tweets
    }


    public static void tweetWriter(TweetsResources tr) {

        try {
            String tweet = "";

            InputStream seanPicStream = App.class.getClassLoader().getResourceAsStream("seansetnick.PNG");
            File seanPic = File.createTempFile("seansetnick", ".PNG");
            FileUtils.copyInputStreamToFile(seanPicStream, seanPic);

            //move this to its own method
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime incident = LocalDateTime.of(2020, Month.MAY, 31, 19, 36);
            Duration duration = Duration.between(incident, now);

            String durationString = duration.toString();

            String[] split = durationString.split("H");
            String[] split2 = split[1].split("M");
            String[] hourMin = {split[0], split2[0]};
            hourMin[0] = hourMin[0].substring(2);

            String dateString = hourMin[0] + " hours and " + hourMin[1] + " minutes";

            tweet = tweet + "It's been " + dateString + " since this video surfaced and @penn_state still hasn't expelled Sean Setnick.";
            /*
            video posted at 7:36pm may 31st - https://twitter.com/YonceLipa/status/1267238505002020865
             */

            StatusUpdate newTweet = new StatusUpdate(tweet);
            newTweet.media(seanPic); //adds the picture to be tweeted

            tr.updateStatus(newTweet);

            System.out.println(dateString + "new tweet posted");

        } catch (TwitterException ex) {
            Logger.getLogger(MajorityTwitterBot.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());

            //from http://twitter4j.org/javadoc/twitter4j/api/TweetsResources.html
        }
    }

    public static void timeToTweet(TweetsResources tr) {

        System.out.println("time to tweet");

        Runnable runningTweetWriter = new Runnable() {
            @Override
            public void run() {
                tweetWriter(tr);
            }
        };

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(runningTweetWriter, 0, 13, TimeUnit.MINUTES);

        //sends a new tweet every 13 minutes

        //https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html
        //https://stackoverflow.com/questions/33073671/how-to-execute-a-method-every-minute

    }

}
