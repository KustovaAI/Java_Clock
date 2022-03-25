/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Alarm_clock;

/**
 *
 * @author Anastasiya
 */
public class Alarm_clock {
    int hours;
    int minutes;
    int seconds;
    boolean is_expired;
    
    public Alarm_clock() {
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.is_expired = false;
    }    
    public String GetTime() {
        String time = Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds);
        return time;
    }
    public int [] GetListTime() {
        int [] time = new int[3];
        time[0] = this.hours;
        time[1] = this.minutes;
        time[2] = this.seconds;
        return time;
    }
    
    public Alarm_clock(int _hours, int _minutes, int _seconds) {
        if (_hours < 0 || _minutes < 0 || _seconds < 0 || _hours >= 24 
            || _minutes >= 60 || _seconds >= 60) {
        throw new Error("You are trying to set wrong alarm time");
        }
        this.hours = _hours;
        this.minutes = _minutes;
        this.seconds = _seconds;
        this.is_expired = false;
    }    
    public Alarm_clock(int _hours, int _minutes) {
        this.hours = _hours;
        this.minutes = _minutes;
        this.seconds = 0;
        this.is_expired = false;
    }
    
    public boolean IsAlarmExpired(int _hours, int _minutes, int _seconds) {
        if (this.is_expired == true)
            return true;
        else if (this.hours == _hours && this.minutes == _minutes && this.seconds == _seconds) {
            this.is_expired = true;
            return true;
        }
        else
            return false;
    }
    public boolean IsAlarmExpiredNow(int _hours, int _minutes, int _seconds) {
        if (this.is_expired == false &&this.hours == _hours && 
                this.minutes == _minutes && this.seconds == _seconds) {
            this.is_expired = true;
            return true;
        }
        else
            return false;
    }
    public boolean IsAlarmExpiredNow(int _hours, int _minutes) {
        if (this.is_expired == false &&this.hours == _hours && 
                this.minutes == _minutes) {
            this.is_expired = true;
            return true;
        }
        else
            return false;
    }
}
