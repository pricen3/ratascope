import java.util.*;
import java.lang.Runtime;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Cage {
   private String cageID;
   private String lightOnTime;
   private String lightOffTime;
   private Experiment exp;
   private Mouse inhabitant;
   private String ip; /* ip address of pi */
   private boolean expOngoing;

   public Cage(String cage, String pi){
      cageID = cage;
      ip = pi;
   }
   public Cage(){}

   public String getID(){
      return cageID;
   }
   public String getName(){
      return cageID;
   }
   public String getIP(){
      return ip;
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
	    String configs = new File("").getAbsolutePath();
	    	File configf = new File(configs + "\\config.txt");
	    	FileReader fread = new FileReader(configf);
	     BufferedReader read = new BufferedReader(fread);
		String path = read.readLine();
		read.close();
         String stringRun = "py "+path+"client.py -ip "+ip+" -s hello";
		 System.out.println(stringRun);
         Process pr = Runtime.getRuntime().exec(stringRun); //new String[] {"bash", "-c" ,stringRun};
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
