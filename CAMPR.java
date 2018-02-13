import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/* Command line: java -classpath ".:sqlite-jdbc-3.21.0.jar" CAMPR */

public class CAMPR {

   public static Boolean DEBUG = false;
   private static ArrayList<Experiment> ongoing = new ArrayList<Experiment>();
   private static ArrayList<Experiment> complete = new ArrayList<Experiment>();
   private static ArrayList<Cage> available = new ArrayList<Cage>();
   private static ArrayList<Cage> inUse = new ArrayList<Cage>();

   public static void main(String[] args) throws Exception{
      try{
         //TODO access database to produce lists of experiments//cages
         /* populate ArrayLists */
         available = CAMPRDatabase.findAvailable();
         inUse = CAMPRDatabase.findUnavailable();
         ongoing = CAMPRDatabase.experimentSelect("ongoing");
         complete = CAMPRDatabase.experimentSelect("completed");

         ArrayList<Experiment> cur = CAMPRDatabase.experimentSelect("completed");

         for(int i = 0; i < cur.size(); i++){
            Experiment ex = cur.get(i);

            String name = ex.getName();
            String researcher = ex.getResearcher();
            String start = ex.getStart();
            String expDur = ex.getExpDurr();
            String durOn = ex.getOnDurr();
            String durOff = ex.getOffDurr();
            ArrayList<MouseCage> cages = ex.getCages();

            for(int j = 0; j < cages.size(); j++){
               System.out.println("researcher: " + researcher +
                                    " name: " + name +
                                    " start: " + start +
                                    " dur: " + expDur +
                                    " onDur: " + durOn +
                                    " offDur: " + durOff +
                                    " cage: " + cages.get(j).getCage().getName() +
                                    " mouse: " + cages.get(j).getMouse());
            }
         }
         System.out.println("HERE");

         generateMainPage();
      }catch(Exception ex){
         System.out.println("*shrug*");
         ex.printStackTrace();
      }
   }

   /* Create all pages */
   public static void generateMainPage() throws Exception{
      Page mainPage = new Page("CAMPR", 900, 200);
      mainPage.addBackground("campr_home.png");
      /* add buttons */
      mainPage.add(new Button(28, 30, 40, 150, "Experiments", generateExpPage()));
      mainPage.add(new Button(28, 80, 40, 150, "Cages", generateCagePage()));
      mainPage.add(new Button(28, 130, 40, 150, "Help"));
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
      //TODO add some sort of database calls such that these functions are reflected in the database
      inUse.add(c);
      available.remove(c);
   }
   public static void freeCage(Cage c){
      available.add(c);
      inUse.remove(c);
   }
   public static void deleteCage(Cage c){
      //TODO remove from database
      available.remove(c);
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
