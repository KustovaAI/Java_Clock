/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlClock;

import Alarm_clock.Alarm_clock;
import Clock.Clock;
import DB.DB;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anastasiya
 */
public class ClientThread extends Thread implements IObserver{

    Socket cs;
    
    InputStream is;
    OutputStream os;          
    ObjectInputStream dis;
    ObjectOutputStream dos;
    ControlClock controlc;
    DB clock_db;
    
    public ClientThread(Socket cs, ControlClock c, DB db) {

        this.cs = cs;
        this.controlc = c;
        this.clock_db = db;
        try {            
            os = cs.getOutputStream();
            dos = new ObjectOutputStream(os);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        c.addO(this);
        this.start();
    }

    @Override
    public void run() {
        try {
            is = cs.getInputStream();
            dis = new ObjectInputStream(is);
            
            while (true) {
                Object object;
                try {
                    object = dis.readObject();
                    ArrayList<String> request = (ArrayList<String>) object;
                    parseRequests(request);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send(Object o) {
        try { 
            dos.writeObject(o);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void parseRequests(ArrayList<String> request) {
        if (null != request.get(0)) switch (request.get(0)) {
            case "name":
                String _name = controlc.getName();
                ArrayList<String> Rname =  new ArrayList<>();
                Rname.add("name");
                Rname.add(_name);
                send(Rname);
                break;
            case "type":
                String _type = controlc.getType();
                ArrayList<String> Rtype =  new ArrayList<>();
                Rtype.add("type");
                Rtype.add(_type);
                send(Rtype);
                break;
            case "pause":
                controlc.pause();
                SendUpdateAlarmTable();
                break;
            case "move":
                if ("with_seconds".equals(controlc.getType())) {
                    controlc.moveTime(Integer.parseInt(request.get(1)), Integer.parseInt(request.get(2)), Integer.parseInt(request.get(3)));
                } else {
                    controlc.moveTime(Integer.parseInt(request.get(1)), Integer.parseInt(request.get(2)));
                }   controlc.start();
                break;
            case "set_alarm":
                if ("with_seconds".equals(controlc.getType())) {
                    controlc.set_alarm(clock_db, Integer.parseInt(request.get(1)), Integer.parseInt(request.get(2)), Integer.parseInt(request.get(3)));
          
                } else {
                    controlc.set_alarm(clock_db, Integer.parseInt(request.get(1)), Integer.parseInt(request.get(2)));
                }  
                break;
            case "remove_alarm":
                if ("with_seconds".equals(controlc.getType())) {
                    controlc.remove_alarm(clock_db, Integer.parseInt(request.get(1)), Integer.parseInt(request.get(2)), Integer.parseInt(request.get(3)));
          
                } else {
                    controlc.remove_alarm(clock_db, Integer.parseInt(request.get(1)), Integer.parseInt(request.get(2)));
                }  
                break;
            case "start":
                controlc.start();
                break;
            case "stop":
                controlc.stop();
                break;
            case "get_alarms":
                SendUpdateAlarmTable();
                break;
            default:
                break;
        }
    }
    @Override
    public void update(Clock Obj) {
        int[] str = Obj.GetListTime();
        ArrayList<String> arrayList =  new ArrayList<>();
        arrayList.add("time");
        for (int i = 0; i < str.length; i++) {
            arrayList.add(Integer.toString(str[i]));
        }
        send(arrayList);
    }
    @Override
    public void update_alarm_table() {
        SendUpdateAlarmTable();
//        try {
//          //  TimeUnit.SECONDS.sleep(3);
//            
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    @Override
    public void alarm (int [] time) {
        ArrayList<String> arrayList =  new ArrayList<>();
        arrayList.add("alarm");
        String time_s;
        if (time.length == 2) {
            time_s = time[0] + ":" + time[1];
            clock_db.DeleteAlarmFromDB(time[0], time[1], 0);
        } else {
            time_s = time[0] + ":" + time[1] + ":" + time[2];
            clock_db.DeleteAlarmFromDB(time[0], time[1], time[2]);            
        }
        arrayList.add(time_s);
        send(arrayList);
        SendUpdateAlarmTable();
    }
    
    public void SendUpdateAlarmTable() {
        ArrayList<Alarm_clock> alarms = clock_db.GetListAlarms();
        ArrayList<String> arrayList =  new ArrayList<>();
        arrayList.add("alarm_table");
      //  System.out.println("ttttttt!!!!!!!!!!1");
        String result = "";
        for (int i = 0; i < alarms.size(); i++) {
            result = result + alarms.get(i).GetTime() + "\n";
            
        }
        arrayList.add(result);
        send(arrayList);
    }
    
    @Override
    public void wrong_move() {
        ArrayList<String> arrayList =  new ArrayList<>();
        arrayList.add("wrong_move");
        send(arrayList);
    }
    
}
