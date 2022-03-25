/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlClock;
import Clock.Clock;
/**
 *
 * @author Anastasiya
 */
public interface IObserver {
    void update(Clock Obj);
    void alarm (int [] time);
    void update_alarm_table ();
    void wrong_move();
}
