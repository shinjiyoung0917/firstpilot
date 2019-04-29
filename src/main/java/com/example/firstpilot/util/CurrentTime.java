package com.example.firstpilot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTime {
    private static final Logger log = LoggerFactory.getLogger(CurrentTime.class);

    public String getCurrentTime() {
        log.info("getCurrentTime 로그 - 진입");
        long currentTime = System.currentTimeMillis();
        log.info("getCurrentTime 로그 - currentTime : " + currentTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss");
        String currentTimeString = dateFormat.format(new Date(currentTime));
        log.info("getCurrentTime 로그 - currentTimeString : " + currentTimeString);

        return currentTimeString;
    }

    public SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss");
    }
}
