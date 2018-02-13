
/*I am still trying to figure out the time part
   and will have that part done monday. hopefully*/

import java.util.*;
import java.time.*;

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
   //send script to pi
   }

   //public File aquireData(){
      /*aquire some sort of file?*/
      //return file;
   //}

   //public void setDuration(time t){
      //duration = t;
   //}

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
      //TODO: figure this snot out
      //send cancel script to pis
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
