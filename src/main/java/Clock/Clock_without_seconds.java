/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clock;
import Alarm_clock.Alarm_clock;
import java.util.*;
import javax.swing.JOptionPane;
/**
 *
 * @author Anastasiya
 */
public class Clock_without_seconds implements Clock {
    String name;
    int cost;
    int hours;
    int minutes;
    int type;
    Vector<Alarm_clock> alarms;
    
    public Clock_without_seconds() {
        name = "";
        cost = 0;
        type = 2;
        this.alarms = new Vector();
    }
    public Clock_without_seconds(String _name, int _cost) {
        name = _name;
        cost = _cost;
        type = 2;
        this.alarms = new Vector();
    }
    @Override
    public int GetType() {
        return type;
    }

    /**
     *
     * @return
     */
    @Override
    public String GetTime() {
        String str = Integer.toString(this.hours) + ":" + Integer.toString(this.minutes);
        return str;
    }
    @Override
    public int [] GetListTime() {
       int[] ar = new int[2];
       ar[0] = this.hours;
       ar[1] = this.minutes;
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
        if (_hours < 0 || _minutes < 0 || _hours >= 24 || _minutes >= 60) {
        System.err.println("Error: You are going to set incorrect time");
        throw new Error("You are trying to set wrong time");
        } else {
            this.hours = _hours;
            this.minutes = _minutes;
            return true;
        }
    }
    
    @Override
    public int MovingForwardTime(int[] ar) {
        int _hours = ar[0];
        int _minutes = ar[1];
        if (_hours < 0 || _minutes < 0 || _hours >= 24 || _minutes >= 60) {
            System.err.println("Error: You are going to set incorrect time");
            throw new Error("You are trying to set wrong time");
        } else {
            int start_sum = this.hours * 60 + this.minutes;
            int current_sum = _hours * 60 + _minutes;
            if (current_sum >= start_sum || current_sum == 0) {
                this.hours = _hours;
                this.minutes = _minutes;
                boolean expired_alarm = IsAlarmExpiredNow();
                if (expired_alarm == true) {
                //    System.out.println("Alarm clock " + GetTime());
                 //   JOptionPane.showMessageDialog(null, GetTime(), "AlarmClock", JOptionPane.PLAIN_MESSAGE);
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
            if (alarms.get(i).IsAlarmExpiredNow(this.hours, this.minutes) == true) {
                return true;
            }
        }
        return false;
    } 
    
    @Override
    public void RemoveAlarm(int[] a) {
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).GetListTime()[0] == a[0] && alarms.get(i).GetListTime()[1] == a[1]) {
                alarms.remove(i);
            }
        }
    }
}
