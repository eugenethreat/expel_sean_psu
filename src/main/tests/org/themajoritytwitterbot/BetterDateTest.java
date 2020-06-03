package org.themajoritytwitterbot;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class BetterDateTest {

    @Test
    void getDateDifference() {
        LocalDateTime now = LocalDateTime.now();
        //LocalDateTime incident = LocalDateTime.of(2020, Month.MAY, 31, 19, 36);
        LocalDateTime nothingBurger = LocalDateTime.of(2020, Month.JUNE, 02, 20, 44);

        Duration duration = Duration.between(nothingBurger, now);
        String durationString = duration.toString();
        //gets the difference as a duration object and converts to string.

        String hours = durationString.split("H")[0].substring(2);
        String minutes = durationString.split("M")[0].substring(5);

        System.out.println(hours + minutes );

    }
}