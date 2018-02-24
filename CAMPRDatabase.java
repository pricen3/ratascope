import java.util.*;
import java.sql.*;

/*Functions:
   -dropTable(): deletes all the tables, just for testing
   -createTable(String table): creates the table given
   -cageInput(String name, String ip): creates a row in the cage table for the given cage
   -expInput(Experiment exp): creates rows in the experiment table for each of the cages in the experiment
   -cageSelect(): returns an arraylist of all the cages in the cage table
   -findAvailable(): returns an arraylist of all of the available cages
   -findUnavailable(): returns an arraylist of all the unavailable cages
   -experimentSelect(String action): returns an arraylist of the experiments with the given action
                                       Action options:
                                          *"ongoing" - returns all ongoing experiments
                                          *"completed" - returns all completed experiments
                                          *"all" - returns all experiments
   -statusUpdate(String exp): changes the status of the given experiment from "ongoing" to "completed"
   -cancel(String exp): deletes the given experiment from the database ***no way to restore, would need to reinput all info***
   -Main(): just for testing

*/


public class CAMPRDatabase{

   //deletes the tables
   //mostly just for testing
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
                     "(CAGE_NAME  TEXT     NOT NULL, " +
                      "IP         TEXT     NOT NULL, "+
                      "DELETED         TEXT     NOT NULL)";;
         }
         else if (table == "EXPERIMENT"){
            sql = "CREATE TABLE IF NOT EXISTS EXPERIMENT " +
                     "(EXP_NAME   TEXT NOT NULL, " +
                     " RESEARCHER TEXT NOT NULL, " +
                     " START_TIME TEXT NOT NULL, " +
                     " EXP_DUR    TEXT NOT NULL, " +
                     " DUR_ON     TEXT NOT NULL, " +
                     " DUR_OFF    TEXT NOT NULL, " +
                     " CAGE_NAME  STRING NOT NULL, " +
                     " MOUSE      STRING NOT NULL, " +
                     " STATUS     STRING NOT NULL)";
         }
         //probably need to delete
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
         String sql = "INSERT INTO CAGE (CAGE_NAME, IP, DELETED) " +
                        "VALUES('" + name + "', '" + ip + "', '" + "FALSE" + "')";

         stmt.executeUpdate(sql);
         System.out.println(sql);
         stmt.close();
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
               MouseCage cage = cages.get(i);
               String sql = "INSERT INTO EXPERIMENT (EXP_NAME, RESEARCHER, START_TIME, EXP_DUR, DUR_ON, DUR_OFF, CAGE_NAME, MOUSE, STATUS)" +
                              "VALUES ('" + name + "', '" +
                                 researcher + "', '" +
                                 start + "', '" +
                                 expDur + "', '" +
                                 durOn + "', '" +
                                 durOff + "', '" +
                                 cage.getCage().getName() + "', '" +
                                 cage.getMouse() + "', " +
                                 "'ONGOING')";

               stmt.executeUpdate(sql);
               System.out.println(sql);
            }
            stmt.close();
            conn.close();
         }
         catch (Exception e){
            //do something here
            System.out.println("failed in insert");
            System.err.println(e.getMessage());
         }
      }

   //Returns all of the cages in the cage table
   public static ArrayList<Cage> cageSelect(){
      Connection conn = null;
      Statement stmt = null;
      ArrayList<Cage> ret = new ArrayList<Cage>();
      try{

         createTable("CAGE");

         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM CAGE WHERE DELETED = 'FALSE'");

         while(rs.next()){
            String name = rs.getString("CAGE_NAME");
            String ip = rs.getString("IP");
            ret.add(new Cage(name, ip));

            System.out.println("NAME: " + name + ", IP: " + ip);
         }
         rs.close();
         stmt.close();
         conn.close();
      }

      catch (Exception e){
         //do some stuff
         System.out.println("failed in Cage select");
         System.err.println(e.getMessage());
      }
      return ret;

   }

   //returns an arraylist of the avaliable cages
   public static ArrayList<Cage> findAvailable(){
      Connection conn = null;
      Statement stmt = null;
      ArrayList<Cage> ret = new ArrayList<Cage>();
      try{

         createTable("CAGE");
         createTable("EXPERIMENT");

         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT DISTINCT CAGE_NAME, IP " +
                                             "FROM CAGE " +
                                             "WHERE DELETED = 'FALSE' AND CAGE_NAME NOT IN " +
                                                "(SELECT CAGE_NAME " +
                                                " FROM EXPERIMENT " +
                                                " WHERE STATUS = 'ONGOING');");
         System.out.println("avaliable cages");
         while(rs.next()){
            String name = rs.getString("CAGE_NAME");
            String ip = rs.getString("IP");
            ret.add(new Cage(name, ip));

            System.out.println("NAME: " + name + ", IP: " + ip);
         }
         rs.close();
         stmt.close();
         conn.close();
      }

      catch (Exception e){
         //do some stuff
         System.out.println("failed in available select");
         System.err.println(e.getMessage());
      }
      return ret;

   }

   //returns an arraylist of the unavaliable cages
   public static ArrayList<Cage> findUnavailable(){
      Connection conn = null;
      Statement stmt = null;
      ArrayList<Cage> ret = new ArrayList<Cage>();
      try{

         createTable("CAGE");
         createTable("EXPERIMENT");

         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT DISTINCT CAGE_NAME, IP " +
                                             "FROM CAGE " +
                                             "WHERE DELETED = 'FALSE' AND CAGE_NAME IN " +
                                                "(SELECT CAGE_NAME " +
                                                " FROM EXPERIMENT " +
                                                " WHERE STATUS = 'ONGOING');");
         System.out.println("unavaliable cages");
         while(rs.next()){
            String name = rs.getString("CAGE_NAME");
            String ip = rs.getString("IP");
            ret.add(new Cage(name, ip));

            System.out.println("NAME: " + name + ", IP: " + ip);
         }
         rs.close();
         stmt.close();
         conn.close();
      }

      catch (Exception e){
         //do some stuff
         System.out.println("failed in available select");
         System.err.println(e.getMessage());
      }
      return ret;

   }




   //returns the contents of the Experiment table
   public static ArrayList<Experiment> experimentSelect(String action){
      Connection conn = null;
      Statement stmt = null;
      ArrayList<Experiment> ret = new ArrayList<Experiment>();
      String sql = "";

      try{

         createTable("EXPERIMENT");

         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();

         if (action.toUpperCase().equals("ONGOING")){
            System.out.println("fidgetspinnersrdope");
            sql = "SELECT * FROM EXPERIMENT INNER JOIN CAGE " +
                     "ON EXPERIMENT.CAGE_NAME = CAGE.CAGE_NAME " +
                     "WHERE STATUS = 'ONGOING' ORDER BY EXP_NAME ASC;";
         }
         else if (action.toUpperCase().equals("COMPLETED")){
            sql = "SELECT * FROM EXPERIMENT INNER JOIN CAGE " +
                     "ON EXPERIMENT.CAGE_NAME = CAGE.CAGE_NAME " +
                     "WHERE STATUS = 'COMPLETED' ORDER BY EXP_NAME ASC;";
         }
         else{
            sql = "SELECT * FROM EXPERIMENT INNER JOIN CAGE " +
                     "ON EXPERIMENT.CAGE_NAME = CAGE.CAGE_NAME ORDER BY EXP_NAME ASC;";
         }

         ResultSet rs = stmt.executeQuery(sql);
         Experiment curExp;

         if(rs.next()){
            System.out.println("1");
            String expName = rs.getString("EXP_NAME");
            String researcher = rs.getString("RESEARCHER");
            String startTime = rs.getString("START_TIME");
            String expDur = rs.getString("EXP_DUR");
            String durOn = rs.getString("DUR_ON");
            String durOff = rs.getString("DUR_OFF");
            String cage = rs.getString("CAGE_NAME");
            String ip = rs.getString("IP");
            String mouse = rs.getString("MOUSE");
            curExp = new Experiment(researcher, expName, startTime, expDur, durOn, durOff);
            curExp.setCage(new MouseCage(new Cage(cage, ip), mouse));

            //rs.next(); //////// ! i think one entry in gets skipped here!
            System.out.println("2");
            while(rs.next()){
               expName = rs.getString("EXP_NAME");
               researcher = rs.getString("RESEARCHER");
               startTime = rs.getString("START_TIME");
               expDur = rs.getString("EXP_DUR");
               durOn = rs.getString("DUR_ON");
               durOff = rs.getString("DUR_OFF");
               cage = rs.getString("CAGE_NAME");
               ip = rs.getString("IP");
               mouse = rs.getString("MOUSE");
               //curExp = new Experiment(researcher, expName, startTime, expDur, durOn, durOff);
               //curExp.setFakeCage(cage);
               if(expName.equals(curExp.getName())){
                  curExp.setCage(new MouseCage(new Cage(cage, ip), mouse));
               }
               else{

                  //NOLAN NOTE: what about entries for the same exp that appear non-consecutively in rs?
                  ret.add(curExp);
                  curExp = new Experiment(researcher, expName, startTime, expDur, durOn, durOff);
                  curExp.setCage(new MouseCage(new Cage(cage, ip), mouse));
               }

               //System.out.println("experiment: " + expName + ", cage: " + cage);
            }
            ret.add(curExp);
            rs.close();
            stmt.close();
            conn.close();
         }
      }

      catch (Exception e){
         //do some stuff
         System.out.println("failed in experiment select");
         System.err.println(e.getMessage());
      }

      return ret;
   }

   //***PROBABLY DELETE***
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
            String name = rs.getString("MOUSE");
            //the note file here

            System.out.println("experiment: " + name + ", cage: ");
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

   //changes experiment status to 'COMPLETED'
   public static void statusUpdate(String exp){
      Connection conn = null;
      Statement stmt = null;

      try{
         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();

         String sql = "UPDATE EXPERIMENT SET STATUS = 'COMPLETED' " +
                  "WHERE EXP_NAME = '" + exp + "';";

         stmt.executeUpdate(sql);
         stmt.close();
         conn.close();
      }
      catch (Exception e){
         System.out.println("failed in status update");
         System.out.println(e.getMessage());
      }
   }

   //deletes a canceled experiment
   public static void cancel(String exp){
      System.out.println("Here"+exp);
      Connection conn = null;
      Statement stmt = null;

      try{
         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();

         String sql = "DELETE FROM EXPERIMENT " +
                  "WHERE EXP_NAME = '" + exp + "';";

         stmt.executeUpdate(sql);
         stmt.close();
         conn.close();
      }
      catch (Exception e){
         System.out.println("failed in cancel");
         System.out.println(e.getMessage());
      }
   }
   //deletes a canceled experiment
   public static void delete(String cage){
      System.out.println("Here"+cage);
      Connection conn = null;
      Statement stmt = null;

      try{
         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();

         String sql = "UPDATE CAGE SET DELETED = 'TRUE' " +
                  "WHERE CAGE_NAME = '" + cage + "';";

         stmt.executeUpdate(sql);
         stmt.close();
         conn.close();
      }
      catch (Exception e){
         System.out.println("failed in cancel");
         System.out.println(e.getMessage());
      }
   }


   /*public static Cage findCage(String cage, Connection conn, Statement stmt){
      Cage ret = new Cage();
      //Connection conn = null;
      //Statement stmt = null;

      try{
         conn = DriverManager.getConnection("jdbc:sqlite:CAMPR.db");
         stmt = conn.createStatement();

         String sql = "SELECT * FROM CAGE " +
                  "WHERE CAGE_NAME = '" + cage + "';";
         ResultSet rs = stmt.executeQuery(sql);

         String name = rs.getString("CAGE_NAME");
         String ip = rs.getString("IP");

         ret = new Cage(name, ip);

         rs.close();
         //stmt.close();
         //conn.close();
      }
      catch(Exception e){
         System.out.println("failed in findCage");
         System.out.println(e.getMessage());
      }

      return ret;
   }*/


   //just for testing
   public static void main(String[] args){
      dropTable();
      createTable("CAGE");
      createTable("EXPERIMENT");
      System.out.println("created table hopefully!");

      cageInput("cage1", "ip1");
      cageInput("cage2", "ip2");
      cageInput("cage3", "ip3");

      System.out.println("cage select");
      cageSelect();

      //cageSelect();

      Experiment exp = new Experiment("Bryn", "exp1", "start1", "end1", "on1", "off1");
      exp.setCage(new MouseCage(new Cage("cage1", "ip1"), "mouse1"));
      expInput(exp);

      exp = new Experiment("Nolan", "exp2", "start2", "end2", "on2", "off2");
      exp.setCage(new MouseCage(new Cage("cage2", "ip2"), "mouse2"));
      exp.setCage(new MouseCage(new Cage("cage3", "ip3"), "mouse3"));
      expInput(exp);

      /*for(int i = 3; i < 49; i++){
         exp = new Experiment("Nolan", "exp"+i, "start2", "end2", "on2", "off2");
         exp.setCage(new MouseCage(new Cage("cage"+i, "ip"+i), "mouse2"));
         expInput(exp);
      }*/

      System.out.println("expSelect");
      ArrayList<Experiment> cur = experimentSelect("all");

      for(int i = 0; i < cur.size(); i++){
         Experiment ex = cur.get(i);

         String name = ex.getName();
         String researcher = ex.getResearcher();
         String start = ex.getStart();
         String expDur = ex.getExpDurr();
         String durOn = ex.getOnDurr();
         String durOff = ex.getOffDurr();
         ArrayList<MouseCage> cages = ex.getCages();

         for(int j = 0; j < cages.size(); j++){
            System.out.println("researcher: " + researcher +
                                 " name: " + name +
                                 " start: " + start +
                                 " dur: " + expDur +
                                 " onDur: " + durOn +
                                 " offDur: " + durOff +
                                 " cage: " + cages.get(j).getCage().getName() +
                                 " mouse: " + cages.get(j).getMouse());
         }
      }

      System.out.println("find avaliable");
      ArrayList<Cage> ava = findAvailable();
      for(int i = 0; i < ava.size(); i++){
         System.out.println("Cage: " + ava.get(i).getName() + " IP: " + ava.get(i).getIP());
      }

      statusUpdate("exp1");
      //statusUpdate("exp2");
      System.out.println("find new avaliable");
      ArrayList<Cage> nwava = findAvailable();
      for(int i = 0; i < nwava.size(); i++){
         System.out.println("Cage: " + nwava.get(i).getName() + " IP: " + nwava.get(i).getIP());
      }



      System.out.println("there should be 3 sets of data from the database");

   }
}
