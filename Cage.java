import java.util.*;

public class Cage {
   private int cageID;
   private int lightOnTime;
   private int lightOffTime;
   private Experiment exp;
   private Mouse inhabitant;
   private Pi p;
   private boolean expOngoing;
   
   public Cage(int cage, Experiment exper, Mouse mouse, Pi pi, boolean going, int lightOn, int lightOff){
      cageID = cage;
      exp = exper;
      inhabitant = mouse;
      p = pi;
      expOngoing = going;
      lightOnTime = lightOn;
      lightOffTime = lightOff;
   }
   
   public int getID(){
      return cageID;
   }
   
   public void setInhabitant(Mouse mouse){
      inhabitant = mouse;
   }
   
   public Mouse getInhabitant(){
      return inhabitant;
   }
   
   public void process(String fileName){
   
   }
   
   public void setLightTimes(int on, int off){
      lightOnTime = on;
      lightOffTime = off;
   }
   
}
