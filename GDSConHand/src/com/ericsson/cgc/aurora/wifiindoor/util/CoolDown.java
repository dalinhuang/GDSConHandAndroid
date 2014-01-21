package com.ericsson.cgc.aurora.wifiindoor.util;

import java.util.Timer;
import java.util.TimerTask;

public class CoolDown {
    class Task extends TimerTask {
        public void run() {
            valid = true;
        }
    }
    private boolean valid;
    private Timer timer;

    private long delay;
 
    public CoolDown(long delay) {
        timer = new Timer();
        valid = true;
        this.delay = delay;
    }
 
    public boolean checkValidity() {
        if (valid) {
            valid = false;
            timer.schedule(new Task(), delay);
            return true;
        }
        return false;
    }
}
