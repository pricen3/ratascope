
/*I am still trying to figure out the time part
   and will have that part done monday. hopefully*/

import java.util.*;
import java.time.*;

public class Experiment{
   private ArrayList<Cage> cages;
   private String researcher;
   //private time duration;
   private String name;
   //private time startTime;

   public Experiment( ArrayList<Cage> cage, String research, String mName){
      cages = cage;
      researcher = research;
      //duration = dur;
      name = mName;
      //startTime = start;
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

   public void cancelExperiment(ArrayList<Cage> c){
   }

}
