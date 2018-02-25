import java.util.*;
import java.lang.Runtime;
import java.io.InputStream;

public class Cage {
   private String cageID;
   private String lightOnTime;
   private String lightOffTime;
   private Experiment exp;
   private Mouse inhabitant;
   private String p; /* ip address of pi */
   private boolean expOngoing;

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
   public boolean testIP(){
      try{
         String stringRun = "python client.py -ip "+p+" -s hello";
         Process pr = Runtime.getRuntime().exec(new String[] {"bash", "-c" ,stringRun});
         InputStream in = pr.getInputStream();
         Scanner scan = new Scanner(in);
         if(scan.hasNext()){
            return true;
         }
      }catch(Exception e){
      }
      return false;
   }

}
