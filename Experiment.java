
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
   private String endTime;

   public Experiment(String research, String mName, String start, String end){
      cages = new ArrayList<Cage>();
      researcher = research;
      name = mName;
      startTime = start;
      endTime = end;
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

   public String getName(){
      return name;
   }
   public String getResearcher(){
      return researcher;
   }
   public String getStart(){
      return startTime;
   }
   public String getEnd(){
      return endTime;
   }
   public ArrayList<Cage> getCages(){
      return cages;
   }

   public void cancelExperiment(ArrayList<Cage> c){
   }

}
