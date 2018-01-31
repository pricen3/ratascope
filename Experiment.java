//TODO get rid of list that are now durrations
/*I am still trying to figure out the time part
   and will have that part done monday. hopefully*/

import java.util.*;
import java.time.*;

public class Experiment{
   private ArrayList<Cage> cages;
   private String researcher;
   private String name;

   //TODO: These need to be changed to some sort of time-class
   private String startTime;
   private String expDurr;
   private String onDurr;
   private String offDurr;

   public Experiment(String research, String mName, String start, String end){
      cages = new ArrayList<Cage>();
      researcher = research;
      name = mName;
      startTime = start;
      expDurr = end;
      //onTimes = new ArrayList<String>();
      //offTimes = new ArrayList<String>();
   }

   public Experiment(String research, String mName, String start, String end, String onD, String offD){
      cages = new ArrayList<Cage>();
      researcher = research;
      name = mName;
      startTime = start;
      expDurr = end;
      onDurr = onD;
      offDurr = offD;
   }

   public void run(){
   /*add stuff to make the experiment run*/
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

   public void setCage(Cage c){
      cages.add(c);
   }
   public void setFakeCage(String c){
      /* For Debugging // Testing */
      Cage fakeCage = new Cage(c, "0");
      cages.add(fakeCage);
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

   public ArrayList<Cage> getCages(){
      return cages;
   }

   public void cancelExperiment(ArrayList<Cage> c){
   }

   public void addOnDurr(String t){
      onDurr=t;
   }

   public void addOffDurr(String t){
      offDurr=t;
   }

}
