package org.themajoritytwitterbot;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class dateTest {

    @Test
    void tweetWriterTest() {

        DateFormat dateFormatter = new SimpleDateFormat("hh:mm");
        //        String formattedNewDate = dateFormatter.format(currentTime);

            /*
            video posted at 7:36pm may 31st - https://twitter.com/YonceLipa/status/1267238505002020865
            */
/*
        Calendar currentDt = Calendar.getInstance();
        Date currentTime = currentDt.getTime();

        Calendar incidentDt = Calendar.getInstance();
        incidentDt.set(Calendar.HOUR_OF_DAY, 7);
        incidentDt.set(Calendar.MINUTE, 36);
        incidentDt.set(Calendar.DAY_OF_MONTH, 31);
        incidentDt.set(Calendar.MONTH, 4);
        //setting to the date of the incident
        Date incidentTime = incidentDt.getTime();

 */
        //subtract and get the amount of time that has passed



        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        LocalDateTime incident = LocalDateTime.of(2020, Month.MAY, 31, 19, 36);
        Duration duration = Duration.between(incident, now);

        System.out.println(duration);

        String durationString = duration.toString();

        String[] split = durationString.split("H");
        String[] split2 = split[1].split("M");

        System.out.println("hour" + split[0] + " minute " + split2[0]);

        String tweet = "";

        //move this to its own method


        System.out.println(duration);


        System.out.println("hour" + split[0] + " minute " + split2[0]);

        String[] hourMin = {split[0], split2[0]};
        hourMin[0] = hourMin[0].substring(2);
        String dateString = hourMin[0] + " hours and " + hourMin[1] + " minutes";



        tweet = tweet + "It's been " + dateString + " since this video surfaced and @penn_state still hasn't expelled Sean Setnick.";
        System.out.println(tweet);

    }
}