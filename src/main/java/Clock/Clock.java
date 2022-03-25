/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clock;

import Alarm_clock.Alarm_clock;

/**
 *
 * @author Anastasiya
 */
public interface Clock {

    public String GetName();    
    public int GetCost();
    public String GetTime();
    public int [] GetListTime();
    public boolean SetStartTime(int[] ar);
    public int GetType();
    public int MovingForwardTime(int[] ar);
    public void SetAlarmClock(Alarm_clock al);
    public boolean IsAlarmExpiredNow();
    public void RemoveAlarm(int[] a);
}
