/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.themajoritytwitterbot;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.TweetsResources;
import twitter4j.conf.ConfigurationBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author eugene
 */
class MajorityTwitterBot {

    public MajorityTwitterBot() {
        /*
        generating the "bot" itself; oauth token and such
         */
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb = cbProperties(cb); //all the properties in their own method

        TwitterFactory tf = new TwitterFactory(cb.build());

        TweetsResources tr = tf.getInstance().tweets();

        timeToTweet(tr);

    }

    private ConfigurationBuilder cbProperties(ConfigurationBuilder cb) {
        cb.setDebugEnabled(true);

        try {
            File api = File.createTempFile("api", ".txt");
            //https://www.tutorialspoint.com/java/io/file_createtempfile_directory.htm
            try {
                InputStream apiStream = getClass().getClassLoader().getResourceAsStream("api.txt");
                FileUtils.copyInputStreamToFile(apiStream, api);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //my key is NOT in a file called api.txt
            try {
                BufferedReader rdr = new BufferedReader(new FileReader(api));
                String oack = rdr.readLine();
                String oacks = rdr.readLine();
                String oaat = rdr.readLine();
                String oaas = rdr.readLine();

                cb.setOAuthConsumerKey(oack);
                cb.setOAuthConsumerSecret(oacks);
                cb.setOAuthAccessToken(oaat);
                cb.setOAuthAccessTokenSecret(oaas);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(MajorityTwitterBot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MajorityTwitterBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return cb;
    }

    private void reply() {

    }


    private void tweetWriter(TweetsResources tr) {
        try {
            String tweet = "";
            DateFormat df = new SimpleDateFormat("hh:mm");
            Date dateobj = new Date();

            //String picture = "src/main/resources/seansetnick.png";

            //File seanPic = new File(picture);
            InputStream seanPicStream = getClass().getClassLoader().getResourceAsStream("seansetnick.png");
            File seanPic = File.createTempFile("seansetnick", ".png");

            //https://stackoverflow.com/questions/26347415/inputstream-getresourceasstream-giving-null-pointer-exception

            try {
                FileUtils.copyInputStreamToFile(seanPicStream, seanPic);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String dateString = df.format(dateobj);
            //https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html date time object

            tweet = tweet + "It's " + dateString + " and @penn_state still hasn't expelled Sean Setnick.";

            StatusUpdate newTweet = new StatusUpdate(tweet);
            newTweet.media(seanPic); //adds the picture to be tweeted

            tr.updateStatus(newTweet);

            System.out.println(dateString + "new tweet posted");

        } catch (TwitterException ex) {
            Logger.getLogger(MajorityTwitterBot.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //from http://twitter4j.org/javadoc/twitter4j/api/TweetsResources.html 
    }

    private void timeToTweet(TweetsResources tr) {

        Runnable runningTweetWriter = new Runnable() {
            @Override
            public void run() {
                tweetWriter(tr);
            }
        };

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        //exec.scheduleAtFixedRate(runningTweetWriter, 0, 1, TimeUnit.HOURS);
        exec.scheduleAtFixedRate(runningTweetWriter, 0, 13, TimeUnit.MINUTES);

        //sends a new tweet every 13 minutes

        //https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html
        //https://stackoverflow.com/questions/33073671/how-to-execute-a-method-every-minute
        /*
        need to figure out how to keep this running forever so it tweets repeatedly 
         */
    }
}
