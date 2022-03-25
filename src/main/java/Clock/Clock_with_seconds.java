/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clock;
import Alarm_clock.Alarm_clock;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JOptionPane;
/**
 *
 * @author Anastasiya
 */
public class Clock_with_seconds implements Clock {
    String name;
    int cost;    
    int hours;
    int minutes;
    int seconds;
    int type;
    Vector<Alarm_clock> alarms;
    public Clock_with_seconds() {
        super();
        this.alarms = new Vector();
        type = 1;
    }
    @Override
    public int GetType() {
        return type;
    }
    public Clock_with_seconds(String _name, int _cost) {
        this.name = _name;
        this.cost= _cost;
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.alarms = new Vector();
        type = 1;
    }
    
    @Override
    public String GetTime() {
        String str = Integer.toString(this.hours) + ":" + Integer.toString(this.minutes) +
                ":" + Integer.toString(this.seconds);
        return str;
    }
    @Override
    public int [] GetListTime() {
       int[] ar = new int[3];
       ar[0] = this.hours;
       ar[1] = this.minutes;
       ar[2] = this.seconds;
       return ar;
    }
    
    @Override
    public String GetName() {
        return name;
    }
    
    @Override
    public int GetCost() {
        return cost;
    }
    
    @Override
    public boolean SetStartTime(int[] ar) {
        int _hours = ar[0];
        int _minutes = ar[1];
        int _seconds = ar[2];
        if (_hours < 0 || _minutes < 0 || _seconds < 0 || _hours >= 24 
            || _minutes >= 60 || _seconds >= 60) {
        System.err.println("Error: You are going to set incorrect time");       
        throw new Error("You are trying to set wrong time");
        } else {
            this.hours = _hours;
            this.minutes = _minutes;
            this.seconds = _seconds;
            return true;
        }
    }
    
    @Override
    public int MovingForwardTime(int[] ar) {
        int _hours = ar[0];
        int _minutes = ar[1];
        int _seconds = ar[2];
        if (_hours < 0 || _minutes < 0 || _seconds < 0 || _hours >= 24 
                || _minutes >= 60 || _seconds >= 60) {
            System.err.println("Error: You are going to set incorrect time");
            throw new Error("You are trying to set wrong time");
        } else {
            int start_sum = this.hours * 60 * 60 + this.minutes * 60 + this.seconds;
            int current_sum = _hours * 60 * 60 + _minutes * 60 + _seconds;
            if (current_sum >= start_sum || current_sum == 0) {
                this.hours = _hours;
                this.minutes = _minutes;
                this.seconds = _seconds;
                boolean expired_alarm = IsAlarmExpiredNow();
                if (expired_alarm == true) {
                   // System.out.println("Alarm clock " + GetTime());
                  //  JOptionPane.showMessageDialog(null, GetTime(), "AlarmClock", JOptionPane.PLAIN_MESSAGE);
                    return 1;
                } else {
                    return 0;
                }               
        
            } else {
                System.err.println("Error: You are going to move the time back.");
                throw new Error("You are trying to set wrong time");
            }
        }
    }
    
    @Override
    public void SetAlarmClock(Alarm_clock al) {
        this.alarms.add(al);
    }
    @Override
    public boolean IsAlarmExpiredNow() {
        
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).IsAlarmExpiredNow(this.hours, this.minutes, this.seconds) == true) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void RemoveAlarm(int[] a) {
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).GetListTime() == a) {
                alarms.remove(i);
            }
        }
    }
}
