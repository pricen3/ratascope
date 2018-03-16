package CAMPRRUN;
import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
/* Command line: java -classpath ".:sqlite-jdbc-3.21.0.jar" CAMPR */
//TODO: open completed exp CSV files
//TODO: check for input decimal durration hours

public class CAMPR {

   public static Boolean DEBUG = false;
   private static ArrayList<Experiment> ongoing = new ArrayList<Experiment>();
   private static ArrayList<Experiment> complete = new ArrayList<Experiment>();
   private static ArrayList<Cage> available = new ArrayList<Cage>();
   private static ArrayList<Cage> inUse = new ArrayList<Cage>();

   public static void main(String[] args) throws Exception{
      try{
         /* populate ArrayLists */
         available.clear();
         inUse.clear();
         available = CAMPRDatabase.findAvailable();
         inUse = CAMPRDatabase.findUnavailable();
         ongoing = CAMPRDatabase.experimentSelect("ongoing");
         complete = CAMPRDatabase.experimentSelect("completed");
         checkForCompletion(); /* check for completed experiments */
         generateMainPage();
      }catch(Exception ex){
         ex.printStackTrace();
      }
   }

   /* Create all pages */
   public static void generateMainPage() throws Exception{
      Page mainPage = new Page("CAMPR", 900, 200);
      mainPage.addBackground("campr_home.png");
      /* add buttons */
      mainPage.add(new Button(28, 30, 40, 150, "Experiments", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            try{
               generateExpPage().reveal();
            }catch(Exception ex){
               /* should not be here */
            }
         }
      }));
      mainPage.add(new Button(28, 80, 40, 150, "Cages", generateCagePage()));
      mainPage.add(new Button(28, 130, 40, 150, "Help", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            try{
		 	      String configs = new File("").getAbsolutePath();
	    	      File configf = new File(configs + "\\config.txt");
	    	      FileReader fread = new FileReader(configf);
	            BufferedReader read = new BufferedReader(fread);   
		         String path = read.readLine();
		         read.close();
               mainPage.errorMessHelper("please see user documentation in "+path, 170);
            }catch(Exception ex){
               /* should not be here */
            }
         }
      }));
      mainPage.reveal();
   }
   public static Page generateExpPage() throws Exception{
      expPage ePage = expPage.getExpPage();
      return ePage;
   }
   public static Page generateCagePage() throws Exception{
      cagePage cPage = cagePage.getCagePage();
      return cPage;
   }

   public static ArrayList<Experiment> getOngoing(){
      return ongoing;
   }
   public static ArrayList<Experiment> getComplete(){
      return complete;
   }
   public static ArrayList<Experiment> getAllExps(){
      return CAMPRDatabase.experimentSelect("all");
   }
   public static ArrayList<Cage> getAvail(){
      return available;
   }
   public static ArrayList<Cage> getInUse(){
      return inUse;
   }
   public static ArrayList<Cage> getCages(){
      return CAMPRDatabase.cageSelect();
   }
   public static void addExp(Experiment e){
      CAMPRDatabase.expInput(e);
      ongoing.add(e);
   }
   public static void completeExp(Experiment e){
      CAMPRDatabase.statusUpdate(e.getName());
      complete.add(e);
      ongoing.remove(e);
   }
   public static void cancelExp(Experiment e){
      CAMPRDatabase.cancel(e.getName());
      ongoing.remove(e);
   }
   public static void addCage(Cage c){
      CAMPRDatabase.cageInput(c.getName(), c.getIP());
      available.add(c);
   }
   public static void useCage(Cage c){
      inUse.add(c);
      available.remove(c);
   }
   public static void freeCage(Cage c){
      available.add(c);
      inUse.remove(c);
   }
   public static boolean deleteCage(Cage c){
      /* check if cage is in use */
      int uSize = inUse.size();
      String cName = c.getName();
      for(int i = 0; i < uSize; i++){
         if(cName.equals(inUse.get(i).getName())){
            return false;
         }
      }
      int aSize = available.size();
      for(int i = 0; i < aSize; i++){
         if(cName.equals(available.get(i).getName())){
            available.remove(i);
            break;
         }
      }
      CAMPRDatabase.delete(c.getName());
      return true;

   }

   /* Check if any experiments have timed out, then moves those experiments to completed */
   public static void checkForCompletion(){
      int size = ongoing.size();
      Date currentDate = new Date(); /* gets current date to nearest milisecond */
      ArrayList<Experiment> toBeMoved = new ArrayList<Experiment>();
      Experiment cur;
      for(int i = 0; i < size; i++){
         cur = ongoing.get(i);
         if(currentDate.after(cur.getEndDate())){
            cur.finishExperiment();
            toBeMoved.add(cur);
         }
      }
      size = toBeMoved.size();
      for(int i = 0; i < size; i++){
         cur = toBeMoved.get(i);
         completeExp(cur);
      }
   }

   /* returns completed experiment with given unique name */
   public static Experiment getCompletedExpFromString(String expName){
      int eSize = complete.size();
      for(int i = 0; i < eSize; i++){
         if(expName.equals(complete.get(i).getName())){
            return complete.get(i);
         }
      }
      return null;
   }
}
