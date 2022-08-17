package com.es.phoneshop.security;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {

    private static DosProtectionService instance;
    private static final long THRESHOLD = 50;
    private static Map<String, Long> countMap;
    private Timer timer;


    private DefaultDosProtectionService() {
        countMap = new ConcurrentHashMap<>();
        timer = new Timer();
    }


    public static synchronized DosProtectionService getInstance() {
        if (instance == null) {
            instance = new DefaultDosProtectionService();
        }
        return instance;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        countMap.remove(ip);
                        countMap.put(ip, 0L);
                    }
                }, 20000);
                return false;
            }
            count++;
        }
        countMap.put(ip, count);
        return true;
    }
}
