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
                     "(EXP_NUM    INT    NOT NULL, " +
                     " RESEARCHER STRING NOT NULL, " +
                     " START_TIME STRING NOT NULL, " +
                     " DURATION   INT    NOT NULL, " +
                     " CAGE_NAME  STRING NOT NULL, " +
                     " MOUSE_NAME STRING NOT NULL)";
         }
         else{
            sql = "CREATE TABLE IF NOT EXISTS MOUSE " +
                   "(MOUSE_NAME STRING NOT NULL, " +
                    "NOTESS     BLOB)";
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
      //String name = cage.name();
      //String = cage.ip();
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
   public static void experimentSelect(){
      Connection conn = null;
      Statement stmt = null;

      try{

         createTable("EXPERIMENT");

         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM EXPERIMENT");

         while(rs.next()){
            //int id = rs.getInt("ID");
            int expNum = rs.getInt("EXP_NUM");
            String researcher = rs.getString("RESEARCHER");
            String startTime = rs.getString("START_TIME");
            int duration = rs.getInt("DURATION");
            String cage = rs.getString("CAGE_NAME");
            String mouse = rs.getString("MOUSE");

            System.out.println(/*"ID: " + id + */"experiment: " + expNum + ", cage: " + cage);
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
