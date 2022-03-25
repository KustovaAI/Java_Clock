/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlClock;
        
import Alarm_clock.Alarm_clock;
import Clock.Clock_with_seconds;
import Clock.Clock_without_seconds;
import Clock.Clock;
import DB.DB;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Anastasiya
 */
public class ControlClock {
    Thread thread;
    Clock Obj;
    String type;
    ArrayList<IObserver> all_o = new ArrayList<>();
    
    public ControlClock(String _name, int _cost, int _h, int _m, int _s) {
        this.type = "with_seconds";
        Obj = new Clock_with_seconds(_name, _cost);
        int[] a = new int[3];
        a[0] = _h;
        a[1] = _m;
        a[2] = _s;        
        Obj.SetStartTime(a);
    }
    public ControlClock(String _name, int _cost, int _h, int _m) {
        this.type = "without_seconds";
        Obj = new Clock_without_seconds(_name, _cost);
        int[] a = new int[3];
        a[0] = _h;
        a[1] = _m;       
        Obj.SetStartTime(a);
    }
    
    void update() {
        all_o.forEach(o -> {
            o.update(Obj);
        });
    }
    
   public void start() {
        Runnable runnable = () -> {
            int interval;            
            try {
                while (true) { 
                    int [] ar = Obj.GetListTime();
                    int hours = ar[0];
                    int minutes = ar[1];
                    if ("with_seconds".equals(type)) {
                        interval = 1;
                        int seconds = ar[2];                    
                        if (seconds == 59) {
                            ar[2] = 0;
                            ar[1] = minutes + 1;
                            if (minutes == 60) {
                                ar[1] = 0;
                                ar[0] = hours + 1;
                                if (hours == 24) {
                                    ar[0] = 0;
                                }
                            }
                        }
                        else {
                            ar[2]++;
                        }                    
                    } else { 
                        interval = 60;
                        if (minutes == 59) {
                            ar[1] = 0;
                            ar[0] = hours + 1;
                            if (hours == 24) {
                                ar[0] = 0;
                            }
                        }
                        else {
                            ar[1]++;
                        }
                    }
                    int is_alarm = Obj.MovingForwardTime(ar); 
                    if (is_alarm == 1) alarm();
                    update();
                    if ("with_seconds".equals(type)) {
                        System.out.println(ar[0] + ": " + ar[1] + ": " + ar[2]);
                    } else {
                        System.out.println(ar[0] + ": " + ar[1]);
                    }                    
                    TimeUnit.SECONDS.sleep(interval);                    
                }
            }
            catch (InterruptedException e) {
            }
        };
        thread = new Thread(runnable);
        thread.start();
     //   update();
    }
   public void moveTime(int _h, int _m, int _s) {
       int[] ar = new int[3];
       ar[0] = _h;
       ar[1] = _m;
       ar[2] = _s;
       try {       
            int is_alarm = Obj.MovingForwardTime(ar); 
            if (is_alarm == 1) {
                alarm();                
            }
       } catch (Error e) {
           wrong_move();
       }
       update();
   }
   public void moveTime(int _h, int _m) {
       int[] ar = new int[3];
       ar[0] = _h;
       ar[1] = _m;      
       try {       
       int is_alarm = Obj.MovingForwardTime(ar); 
       if (is_alarm == 1) {
           alarm();
           alarm_table();
       }
       } catch (Error e) {
           wrong_move();
       }
       update();
   }
   
    public void wrong_move() {
        all_o.forEach(o -> {
            o.wrong_move();
        });
    }
   
    public void set_alarm(DB clock_db, int h, int m, int s) {
        Alarm_clock al = new Alarm_clock(h, m, s);
        Obj.SetAlarmClock(al);
        clock_db.SetAlarmToDB(h, m, s);
        alarm_table();
    }
    
    public void set_alarm(DB clock_db, int h, int m) {
        Alarm_clock al = new Alarm_clock(h, m);
        Obj.SetAlarmClock(al);
        clock_db.SetAlarmToDB(h, m, 0);
        alarm_table();
    }
   
    public void remove_alarm(DB clock_db, int h, int m, int s) {
        int [] a = new int[3];
        a[0] = h;
        a[1] = m;
        a[2] = s;
        Obj.RemoveAlarm(a);
        clock_db.DeleteAlarmFromDB(h, m, s);
        alarm_table();
    }
    public void remove_alarm(DB clock_db, int h, int m) {
        int [] a = new int[3];
        a[0] = h;
        a[1] = m;
        Obj.RemoveAlarm(a);
        clock_db.DeleteAlarmFromDB(h, m, 0);
        alarm_table();
    }
    
    public void set_alarms_from_db(ArrayList<Alarm_clock> alarms) {
        for (int i = 0; i < alarms.size(); i++) {
            Obj.SetAlarmClock(alarms.get(i));
        }
    }
    public void alarm_table() {
        int i = 0;
        System.out.println("Coun" + all_o.size());
        all_o.forEach(o -> {
            o.update_alarm_table();
        });
    }
    public void alarm() {
        all_o.forEach(o -> {
            o.alarm(Obj.GetListTime());            
        });
    }
    public void pause() {
        thread.stop();
    }
    public void stop() {
        int [] ar;
        if ("with_seconds".equals(type)) {
            moveTime(0, 0, 0);
        } else {
            moveTime(0, 0);
        }
        thread.interrupt();
    }
    public void restart(Clock ob) {
        start();
    }
   public void addO(IObserver o){
       all_o.add(o);
       update();
   }
   
   public String getName() {
       return Obj.GetName();
   }
   public String getCost() {
       return Integer.toString(Obj.GetCost());
   }
   public String getType() {
       return type;
   }
}
