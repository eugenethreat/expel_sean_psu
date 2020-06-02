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
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author eugene
 */
public class App {

    public static void main(String args[]) {

        //MajorityTwitterBot tb = new MajorityTwitterBot();
        System.out.println("HELP! ");

           /*
        generating the "bot" itself; oauth token and such
         */

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb = cbProperties(cb); //all the properties in their own method

        TwitterFactory tf = new TwitterFactory(cb.build());

        TweetsResources tr = tf.getInstance().tweets();

        timeToTweet(tr);


    }

    private static ConfigurationBuilder cbProperties(ConfigurationBuilder cb) {
        cb.setDebugEnabled(true);

        System.out.println("cbproper");

        try {
            File api = File.createTempFile("api", ".txt");
            //https://www.tutorialspoint.com/java/io/file_createtempfile_directory.htm
            try {
                InputStream apiStream = App.class.getClassLoader().getResourceAsStream("api.txt");
                FileUtils.copyInputStreamToFile(apiStream, api);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
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
                System.out.println(ex.toString());
            } catch (IOException ex) {
                Logger.getLogger(MajorityTwitterBot.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }


        return cb;
    }

    private void reply() {

    }


    private static void tweetWriter(TweetsResources tr) {

        System.out.println("tweetwriter");

        try {
            String tweet = "";
            DateFormat df = new SimpleDateFormat("hh:mm");
            Date dateobj = new Date();

            System.out.println("try catch");

            //String picture = "src/main/resources/seansetnick.png";

            //File seanPic = new File(picture);
            InputStream seanPicStream = App.class.getClassLoader().getResourceAsStream("seansetnick.PNG");
            File seanPic = File.createTempFile("seansetnick", ".PNG");
            //                InputStream apiStream = App.class.getClassLoader().getResourceAsStream("api.txt");

            System.out.println("seanpic");

            //https://stackoverflow.com/questions/26347415/inputstream-getresourceasstream-giving-null-pointer-exception

            try {
                FileUtils.copyInputStreamToFile(seanPicStream, seanPic);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.toString());
            }

            System.out.println("datestring");

            String dateString = df.format(dateobj);
            //https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html date time object

            tweet = tweet + "It's " + dateString + " and @penn_state still hasn't expelled Sean Setnick.";

            System.out.println("tweet made");

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

    private static void timeToTweet(TweetsResources tr) {

        System.out.println("time to tweet");

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
