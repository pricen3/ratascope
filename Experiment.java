
/*I am still trying to figure out the time part
   and will have that part done monday. hopefully*/

import java.util.*;
import java.time.*;
import java.lang.Runtime;

public class Experiment{
   private ArrayList<MouseCage> cages;
   private String researcher;
   private String name;

   //TODO: These need to be changed to some sort of time-class
   private String startTime;
   private String expDurr;
   private String onDurr;
   private String offDurr;

   public Experiment(String research, String mName, String start, String end){
      cages = new ArrayList<MouseCage>();
      researcher = research;
      name = mName;
      startTime = start;
      expDurr = end;
      //onTimes = new ArrayList<String>();
      //offTimes = new ArrayList<String>();
   }

   public Experiment(String research, String mName, String start, String end, String onD, String offD){
      cages = new ArrayList<MouseCage>();
      researcher = research;
      name = mName;
      startTime = start;
      expDurr = end;
      onDurr = onD;
      offDurr = offD;
   }

   public void run(){
   /* TODO: add stuff to make the experiment run*/
      System.out.println("IAMHERE");
      try{
         MouseCage cur;
         Cage c;
         String ip, runString, stringRun;
         for(int i = 0; i < cages.size(); i++){
            System.out.println("1");
            cur = cages.get(i);
            c = cur.getCage();
            ip = c.getIP();
            runString = getRunString(cur);
            stringRun = "python client.py -ip "+ip+" -s \""+runString+"\"";
            System.out.println(stringRun);
            Process pr = Runtime.getRuntime().exec(new String[] {"bash", "-c" ,stringRun});
            System.out.println("2");
         }
      }catch(Exception ex){
         System.out.println("*shrug*");
         ex.printStackTrace();
      }
   }

   private String getRunString(MouseCage cur){
      return startTime+" "+cur.getMouse()+" "+expDurr+" "+onDurr+" "+offDurr;
   }

   public void finishExperiment(){
   /*not sure how this is different from cancel*/
   }

   public void setCage(MouseCage c){
      cages.add(c);
   }
   public void setFakeCage(String c){
      /* For Debugging // Testing */
      Cage fCage = new Cage(c, "0");
      MouseCage fake = new MouseCage(fCage, "not a real mouse");
      cages.add(fake);
   }

   public String getName(){
      return name;
   }
   public String getResearcher(){
      return researcher;
   }
   public String getStart(){
      return startTime;
   }
   public String getExpDurr(){
      return expDurr;
   }
   public String getOnDurr(){
      return onDurr;
   }
   public String getOffDurr(){
      return offDurr;
   }

   public ArrayList<MouseCage> getCages(){
      return cages;
   }

   public void cancelExperiment(){
      //TODO: send cancel script to pis
      int numCages = cages.size();
      MouseCage m;
      for(int i = 0; i < numCages; i++){
         m = cages.get(i);
         CAMPR.freeCage(m.getCage());
      }
      CAMPR.cancelExp(this);
   }

   public void addOnDurr(String t){
      onDurr=t;
   }

   public void addOffDurr(String t){
      offDurr=t;
   }

}
