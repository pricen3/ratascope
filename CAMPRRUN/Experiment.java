package CAMPRRUN;

import java.util.*;
import java.time.*;
import java.lang.Runtime;
import java.lang.Integer;
import java.util.Date;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Experiment{

   private ArrayList<MouseCage> cages;
   private String researcher;
   private String name;

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

   public boolean run(){
   /* runs the experiment */
      try{
         MouseCage cur;
         Cage c;
         String ip, runString, stringRun;
		 String configs = new File("").getAbsolutePath();
	    	File configf = new File(configs + "\\config.txt");
	    	FileReader fread = new FileReader(configf);
	     BufferedReader read = new BufferedReader(fread);
	     
		String path = read.readLine();
		read.close();
         for(int i = 0; i < cages.size(); i++){
            cur = cages.get(i);
            c = cur.getCage();
            ip = c.getIP();
            runString = getRunString(cur);
   	 	
            stringRun = "py "+path+"client.py -ip "+ip+" -s \""+runString+"\"";
            Process pr = Runtime.getRuntime().exec(stringRun);
            /* check that experiment was recieved */
            InputStream in = pr.getInputStream();
            Scanner scan = new Scanner(in);
            if(!scan.hasNext()){
               return false;
            }
         }
      }catch(Exception ex){
         ex.printStackTrace();
      }
      return true;
   }

   private String getRunString(MouseCage cur){
      /*process startTime */
      char[] start = startTime.toCharArray();
      int hr = Integer.parseInt(start[0]+""+start[1]);
      int min = Integer.parseInt(start[3]+""+start[4]);
      /* Convert to military time */
      if(start[5]=='P'){
         if(hr!=12){
            hr += 12;
         }
      }
      if(start[5]=='A'){
         if(hr==12){
            hr=0;
         }
      }
      String date = start[7]+""+start[8]+""+start[9]+""+start[10]+""+start[11]+""+start[12]+""+start[13]+""+start[14]+""+start[15];
      return hr+" "+min+date+" "+cur.getMouse()+" "+expDurr+" "+onDurr+" "+offDurr;
   }

   public void finishExperiment(){
      //TODO: there is probably a better solution
      /* dispatch finished flag to all idle pis */
      int numCages = cages.size();
      MouseCage m;
      Cage c;
      String ip, finishedString;
      for(int i = 0; i < numCages; i++){
         /* dispatch finished message to all cages so that they start new cycle */
         m = cages.get(i);
         c = m.getCage();
         ip = c.getIP();
		 try {
	    		String configs = new File("").getAbsolutePath();
	    		File configf = new File(configs + "\\config.txt");
	    		FileReader fread = new FileReader(configf);
	        	BufferedReader read = new BufferedReader(fread);
	        
			String path = read.readLine();
	        	read.close();
   	 	
            finishedString = "py "+path+"client.py -ip "+ip+" -s finished";
      
            Process pr = Runtime.getRuntime().exec(finishedString);
         }catch(Exception ex){
            ex.printStackTrace();
         }
         /* free cage */
         CAMPR.freeCage(m.getCage());
      }
   }

   public void setCage(MouseCage c){
      cages.add(c);
   }
   public void setFakeCage(String c){
      /* For Debugging and Testing */
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
   public Date getEndDate(){
      /*process startTime */
      char[] start = startTime.toCharArray();
      try{
         int hr = Integer.parseInt(start[0]+""+start[1]);
         int min = Integer.parseInt(start[3]+""+start[4]);
         /* Convert to military time */
         if(start[5]=='P'){
            if(hr != 12){
               hr += 12;
            }
         }
         if(start[5]=='A'){
            if(hr==12){
               hr=0;
            }
         }
         int year = 100 + Integer.parseInt(start[8]+""+start[9]); /* current year - 1900 */
         int month = Integer.parseInt(start[11]+""+start[12]) - 1; /* subtract one for compatibility with Date class */
         int day = Integer.parseInt(start[14]+""+start[15]);
         int dur = Integer.parseInt(expDurr) + hr;
         return new Date(year, month, day, dur, min);
      }catch(Exception ex){
         /* Shouldn't be here */
      }
      return new Date();
   }

   public void cancelExperiment(){
      int numCages = cages.size();
      MouseCage m;
      Cage c;
      String ip, cancelString;
	  
      for(int i = 0; i < numCages; i++){
         m = cages.get(i);
         c = m.getCage();
         ip = c.getIP();
         try{
		 	String configs = new File("").getAbsolutePath();
	    	File configf = new File(configs + "\\config.txt");
	    	FileReader fread = new FileReader(configf);
	     BufferedReader read = new BufferedReader(fread);
	    
		String path = read.readLine();
		read.close();
         cancelString = "py "+path+"client.py -ip "+ip+" -s cancel";
            Process pr = Runtime.getRuntime().exec(cancelString);
         }catch(Exception ex){
            ex.printStackTrace();
         }
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
