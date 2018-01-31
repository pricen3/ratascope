import java.util.*;
import java.sql.*;

public class CAMPRDatabase{

   public static void dropTable(){
      Connection conn = null;
      Statement stmt = null;

      try{
         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");

         stmt = conn.createStatement();
         stmt.executeUpdate("drop table if exists CAGE");
         stmt.executeUpdate("drop table if exists EXPERIMENT");
         stmt.close();
         conn.close();
      }
      catch (Exception e){
         System.out.println("failed in drop table");
      }
   }

   public static void createTable(String table){
      Connection conn = null;
      Statement stmt = null;
      String sql = "";

      try{
         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");

         stmt = conn.createStatement();
         if (table == "CAGE"){
            sql = "CREATE TABLE IF NOT EXISTS CAGE " +
                  // "(ID   INT PRIMARY KEY NOT NULL, " +
                     "(CAGE_NAME  TEXT     NOT NULL, " +
                      "IP         TEXT     NOT NULL UNIQUE)";
         }
         else if (table == "EXPERIMENT"){
            sql = "CREATE TABLE IF NOT EXISTS EXPERIMENT " +
                     "(EXP_NAME   TEXT NOT NULL, " +
                     " RESEARCHER TEXT NOT NULL, " +
                     " START_TIME TEXT NOT NULL, " +
                     " EXP_DUR    TEXT NOT NULL, " +
                     " DUR_ON     TEXT NOT NULL, " +
                     " DUR_OFF    TEXT NOT NULL, " +
                     " CAGE_NAME  TEXT NOT NULL)";
         }
         else{
            sql = "CREATE TABLE IF NOT EXISTS MOUSE " +
                   "(MOUSE_NAME TEXT NOT NULL, " +
                    "NOTES     BLOB)";
         }
         stmt.executeUpdate(sql);
         stmt.close();
         conn.close();
      }

      catch (Exception e){
      //do something at somepoint
      System.out.println("failed in create table");
      }
   }

   public static void cageInput(String name, String ip){
      //String name = cage.getName();
      //String = cage.getip();
      //System.out.println(name);
      Connection conn = null;
      Statement stmt = null;

      try{

         createTable("CAGE");

         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();
         String sql = "INSERT INTO CAGE (CAGE_NAME, IP) " +
                        "VALUES('" + name + "', '" + ip + "')";

         stmt.executeUpdate(sql);
         System.out.println(sql);
         stmt.close();
         //conn.commit();
         conn.close();
      }
      catch (Exception e){
      //do something here
      System.out.println("failed in insert");
      System.err.println(e.getMessage());
      }
   }

   //experiment input
   public static void expInput(Experiment exp){

         Connection conn = null;
         Statement stmt = null;

         String name = exp.getName();
         String researcher = exp.getResearcher();
         String start = exp.getStart();
         String expDur = exp.getExpDurr();
         String durOn = exp.getOnDurr();
         String durOff = exp.getOffDurr();
         ArrayList<MouseCage> cages = exp.getCages();

         try{

            createTable("EXPERIMENT");

            conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
            stmt = conn.createStatement();

            for(int i = 0; i < cages.size(); i++){
               //Cage cage = cages.get(i);
               String sql = "INSERT INTO EXPERIMENT (EXP_NAME, RESEARCHER, START_TIME, EXP_DUR, DUR_ON, DUR_OFF, CAGE_NAME)" +
                              "VALUES ('" + name + "', '" +
                                 researcher + "', '" +
                                 start + "', '" +
                                 expDur + "', '" +
                                 durOn + "', '" +
                                 durOff + "', '" +
                                 cages.get(i) + "')"; /* TODO: this is returning memory addresses, try Cage.getName() */

               stmt.executeUpdate(sql);
               System.out.println(sql);
            }
            stmt.close();
            //conn.commit();
            conn.close();
         }
         catch (Exception e){
         //do something here
         System.out.println("failed in insert");
         System.err.println(e.getMessage());
         }
      }

   //mouse input

   //change return when actually using
   public static ArrayList<Cage> cageSelect(){
      Connection conn = null;
      Statement stmt = null;
      ArrayList<Cage> ret = new ArrayList<Cage>();
      try{

         createTable("CAGE");

         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM CAGE");

         while(rs.next()){
            //int id = rs.getInt("ID");
            String name = rs.getString("CAGE_NAME");
            String ip = rs.getString("IP");
            ret.add(new Cage(name, ip));

            System.out.println(/*"ID: " + id + */"NAME: " + name + ", IP: " + ip);
         }
         rs.close();
         stmt.close();
         conn.close();
      }

      catch (Exception e){
      //do some stuff
      System.out.println("failed in select");
      System.err.println(e.getMessage());
      }
      return ret;

   }

   //change return when actually using
   public static ArrayList<Experiment> experimentSelect(){
      Connection conn = null;
      Statement stmt = null;
      ArrayList<Experiment> ret = new ArrayList<Experiment>();

      try{

         createTable("EXPERIMENT");

         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM EXPERIMENT");
         Experiment curExp;

         while(rs.next()){
            //int id = rs.getInt("ID");
            /* TODO: this logic will return a list of experiments with only one cage per experiment.
            i.e. if I have an experiment with two cages named "1" and "2", this will return two experiments that are
            copies of eachother except one has cage "1" while the other has "2".
            Instead, it needs to return experiments with the whole list of cages. i.e. one experiment with cages "1" and "2" in a list */
            String expName = rs.getString("EXP_NAME");
            String researcher = rs.getString("RESEARCHER");
            String startTime = rs.getString("START_TIME");
            String expDur = rs.getString("EXP_DUR");
            String durOn = rs.getString("DUR_ON");
            String durOff = rs.getString("DUR_OFF");
            /* TODO: retrieve information necessary to make a Cage oblect to add to the created Experiment object */
            String cage = rs.getString("CAGE_NAME");
            //String mouse = rs.getString("MOUSE");
            curExp = new Experiment(researcher, expName, startTime, expDur, durOn, durOff);
            curExp.setFakeCage(cage); /* TODO: once Cage objects are being created, change this to setCage() */
            ret.add(curExp);
            System.out.println(/*"ID: " + id + */"experiment: " + expName + ", cage: " + cage);
         }
         rs.close();
         stmt.close();
         conn.close();
      }

      catch (Exception e){
      //do some stuff
      System.out.println("failed in select");
      System.err.println(e.getMessage());
      }

      return ret;
   }

   //change return when actually using
   public static void mouseSelect(){
      Connection conn = null;
      Statement stmt = null;

      try{

         createTable("MOUSE");

         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM MOUSE");

         while(rs.next()){
            //int id = rs.getInt("ID");
            String name = rs.getString("MOUSE");
            //the note file here

            System.out.println(/*"ID: " + id + */"experiment: " + name + ", cage: ");
         }
         rs.close();
         stmt.close();
         conn.close();
      }

      catch (Exception e){
      //do some stuff
      System.out.println("failed in select");
      System.err.println(e.getMessage());
      }
   }



   public static void main(String[] args){
      dropTable();
      createTable("CAGE");
      createTable("EXPERIMENT");
      System.out.println("created table hopefully!");

      cageInput("cage1", "ip1");
      cageInput("cage2", "ip2");
      cageInput("cage3", "ip3");

      cageSelect();

      System.out.println("there should be 3 sets of data from the database");

   }
}
