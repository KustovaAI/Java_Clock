/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import Alarm_clock.Alarm_clock;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anastasiya
 */
public class DB {
    Connection c;
    
    public void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(
                    "jdbc:sqlite:D:\\Nastya\\3_course_2_semester\\java\\clock.db");
            System.out.println("Opened database successfully");
        } catch (ClassNotFoundException ex) {
            System.out.println("Нет драйвера");
        } catch (SQLException ex) {
            System.out.println("Проблема с подключением");
        }
    }
    public ArrayList<Alarm_clock> GetListAlarms() {
        ArrayList<Alarm_clock> resList = new ArrayList<>();
        try {
            Statement st = c.createStatement(); 
            ResultSet res = st.executeQuery("SELECT * FROM alarms");
            while (res.next()) {
                int hour = Integer.parseInt(res.getString("hour"));
                int minute = Integer.parseInt(res.getString("minute"));
                int second = Integer.parseInt(res.getString("seconds"));
                Alarm_clock al = new Alarm_clock(hour, minute, second);
                resList.add(al);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return resList;
    }
    
    public void SetAlarmToDB(int hour, int minute, int second){
        try {
            PreparedStatement pst = c.prepareStatement("INSERT INTO alarms(hour, minute, seconds) VALUES(?, ?, ?)");
            pst.setInt(1, hour);
            pst.setInt(2, minute);
            pst.setInt(3, second);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void DeleteAlarmFromDB(int hour, int minute, int second){
        try {
            PreparedStatement pst = c.prepareStatement("DELETE FROM alarms WHERE hour = ? and minute = ? and seconds = ?");
            pst.setInt(1, hour);
            pst.setInt(2, minute);
            pst.setInt(3, second);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
