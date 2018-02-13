import java.util.*;

public class Cage {
   private String cageID;
   private String lightOnTime;
   private String lightOffTime;
   private Experiment exp;
   private Mouse inhabitant;
   private String p; /* ip address of pi */
   private boolean expOngoing;

   /*public Cage(String cage, Experiment exper, Mouse mouse, String pi, boolean going, int lightOn, int lightOff){
      cageID = cage;
      exp = exper;
      inhabitant = mouse;
      p = pi;
      expOngoing = going;
      lightOnTime = lightOn;
      lightOffTime = lightOff;
   }*/
   public Cage(String cage, String pi){
      /*TODO add connection stuff */
      cageID = cage;
      p = pi;
   }
   public Cage(){}

   public String getID(){
      return cageID;
   }
   public String getName(){
      return cageID;
   }
   public String getIP(){
      return p;
   }

   public void setInhabitant(Mouse mouse){
      inhabitant = mouse;
   }

   public Mouse getInhabitant(){
      return inhabitant;
   }

   public void process(String fileName){

   }

   public void setLightTimes(String on, String off){
      lightOnTime = on;
      lightOffTime = off;
   }

}
