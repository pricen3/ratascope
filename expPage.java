import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//TODO Light durrations, exp durration
//TODO Cancel Exp button
public class expPage extends Page {

   private static class CloseReset extends WindowAdapter {
      public void windowClosing(WindowEvent e ) {
         getExpPage().resetCurPos();
      }
   }
   /*private static class TimeLocMouse extends MouseAdapter {
      private int newTimeLoc;
      private Page p;
      private Button b;
      private SubmitButtonClick tracker;
      private int numClicked = 0;
      public void setLoc(int i, Page page){
         newTimeLoc = i;
         p = page;
         b=null;
      }
      public void setB(Button but){
         b = but;
      }
      public void setTracker(SubmitButtonClick t){
         tracker = t;
      }
      public void mouseClicked(MouseEvent e) {
         /* time menu (12:00 through 11:30)
         if(numClicked == 0){
            tracker.watchOnTime(p.timeMenuHelper(440, newTimeLoc, true));
            tracker.watchOnTime(p.timeMenuHelper(520, newTimeLoc, false));
            tracker.watchOffTime(p.timeMenuHelper(440, newTimeLoc+30, true));
            tracker.watchOffTime(p.timeMenuHelper(520, newTimeLoc+30, false));
         }else if(numClicked == 1){
            tracker.watchOnTime(p.timeMenuHelper(580, newTimeLoc, true));
            tracker.watchOnTime(p.timeMenuHelper(660, newTimeLoc, false));
            tracker.watchOffTime(p.timeMenuHelper(580, newTimeLoc+30, true));
            tracker.watchOffTime(p.timeMenuHelper(660, newTimeLoc+30, false));
         }else{
            tracker.watchOnTime(p.timeMenuHelper(160, newTimeLoc, true));
            tracker.watchOnTime(p.timeMenuHelper(240, newTimeLoc, false));
            tracker.watchOffTime(p.timeMenuHelper(160, newTimeLoc+30, true));
            tracker.watchOffTime(p.timeMenuHelper(240, newTimeLoc+30, false));
            b.remove();
         }
         numClicked++;
         /*p.timeMenuHelper(500, newTimeLoc, true);
         p.timeMenuHelper(500, newTimeLoc+30, true);
          {"AM", "PM"} menus
         p.timeMenuHelper(600, newTimeLoc, false);
         p.timeMenuHelper(600, newTimeLoc+30, false);
      }
   }*/
   private static class SubmitButtonClick extends MouseAdapter {
      private Page p;

      /* The following keep track of user inputs */
      private ArrayList<JTextField> textInputs;
      //private ArrayList<JComboBox<String>> onTimeInputs, offTimeInputs;
      private ArrayList<JComboBox<String>> cageInputs;
      private JComboBox<String> startTime, startAOrP, onTimeInput, offTimeInput;
      private JTextField durration;

      public void setPage(Page page){
         p = page;
         textInputs = new ArrayList<JTextField>();
         //onTimeInputs = new ArrayList<JComboBox<String>>();
         //offTimeInputs = new ArrayList<JComboBox<String>>();
         onTimeInput = new JComboBox<String>();
         offTimeInput = new JComboBox<String>();
         //offTimeInputs = new JTextField();
         cageInputs = new ArrayList<JComboBox<String>>();
         startTime = new JComboBox<String>();
         startAOrP = new JComboBox<String>();
         //endTime = new JComboBox<String>();
         durration = new JTextField();
         //endAOrP = new JComboBox<String>();
      }
      public void watch(JTextField userInput){
         textInputs.add(userInput);
      }
      public void watchOnTime(JComboBox<String> userInput){
         onTimeInput=userInput;
      }
      public void watchOffTime(JComboBox<String> userInput){
         offTimeInput = userInput;
         //offTimeInputs.add(userInput);
      }
      public void watch(JComboBox<String> userInput){
         cageInputs.add(userInput);
      }
      public void watchStart(JComboBox<String> time, JComboBox<String> amOrPm){
         startTime = time;
         startAOrP = amOrPm;
      }
      public void watchEnd(JTextField time){
         durration = time;
      }

      public void mouseClicked(MouseEvent e) {
         p.clearErrors();
         if(!checkErrors()){
            return;
         }
         expPage expP = getExpPage();
         int tSize = textInputs.size();
         //int onTiSize = onTimeInputs.size();
         //int offTiSize = offTimeInputs.size();
         int cSize = cageInputs.size();
         //String resName, expName;
         String cageName;
         //String[] onTimeName = new String[onTiSize];
         //String[] offTimeName = new String[offTiSize];

         /* get experiment name */
         /* get researcher name */
         JTextField rName = textInputs.get(0);
         String expName = rName.getText();
         rName = textInputs.get(1);
         String resName = rName.getText();

         /* get time input information */
         String start = (String)startTime.getSelectedItem();
         String sAP = (String)startAOrP.getSelectedItem();
         String end = durration.getText();
         //String eAP = (String)endAOrP.getSelectedItem();

         //Experiment ex = new Experiment(resName, "Experiment "+expP.incrimentGetExpNum(), start+" "+sAP,end+" "+eAP);
         Experiment ex = new Experiment(resName, expName, start+" "+sAP,end);

         /* get cage input information */
         Cage cage = new Cage();
         JComboBox<String> curTime1, curTime2, cur;
         System.out.println(cSize);
         for (int i = 0; i < cSize; i ++){
            cur = cageInputs.get(i);
            cageName = (String)cur.getSelectedItem();
            if(!cageName.equals("")){
               cage = expP.getCageFromString(cageName);
               ex.setCage(cage);
               expP.claimCage(cage);
            }
         }
         String s;
         //for (int j = 0; j < onTimeInputs.size(); j+=2){
            /* Time lights turn on */
            //curTime1 = onTimeInputs.get(j);
            //curTime2 = onTimeInputs.get(j+1);
         s = (String)onTimeInput.getSelectedItem();
            //ex.addOnTime(s+" "+(String)curTime2.getSelectedItem());
         ex.addOnDurr((String)s);
            /* Time lights turn off */
            //curTime1 = offTimeInputs.get(j);
            //curTime2 = offTimeInputs.get(j+1);
         s = (String)offTimeInput.getSelectedItem();
         ex.addOffDurr((String)s);
         //}
         //System.out.println("2");
         System.out.println(resName);
         expP.addExpButton(ex);
         p.close();
         expP.resetCurPos();
      }

      /* check for user input errors */
      public boolean checkErrors(){
         /* test for at least one valid cage */
         String t, test = "";
         JComboBox<String> cur;
         int cIS =  cageInputs.size();
         int arrayPos = 0;
         String[] cageArray = new String[cIS];
         for(int i = 0; i < cIS; i++){
            cur = cageInputs.get(i);
            t = (String)cur.getSelectedItem();
            if(!t.equals("")){
               test = t;
               for(int j = 0; j < arrayPos; j++){
                  if(test.equals(cageArray[j])){
                     p.errorMessHelper("Duplicate cages.", 480);
                     return false;
                  }
               }
               cageArray[arrayPos] = test;
               arrayPos++;
            }
         }
         if(test.equals("")){
            p.errorMessHelper("At least one valid cage required.", 480);
            return false;
         }

         /* test for valid EXPERIMENT name */
         JTextField rName = textInputs.get(0);
         String resName = rName.getText();
         if(resName.equals("")){
            p.errorMessHelper("Experiment name required.", 480);
            return false;
         }
         /* test for valid researcher name */
         rName = textInputs.get(1);
         resName = rName.getText();
         if(resName.equals("")){
            p.errorMessHelper("Researcher name required.", 480);
            return false;
         }
         String lightOn = (String)onTimeInput.getSelectedItem();
         String lightOff = (String)offTimeInput.getSelectedItem();
         if(lightOn.equals("0") && lightOff.equals("0")){
            p.errorMessHelper("Invalid lighting intervals.", 480);
            return false;
         }
         return true;
      }
   }

   /* FIELDS */
   private static boolean exists = false;
   private static expPage thePage;
   private ArrayList<Cage> cages;
   private ArrayList<Button> expButtons;
   private ArrayList<Experiment> exps;
   private int expNum;
   private int newButtonY;
   private int newButtonX;


   /* CONSTRUCTORS */
   private expPage(){
      super("Experiments", 550, 900);
      expButtons = new ArrayList<Button>();
      cages = new ArrayList<Cage>();
      exps = new ArrayList<Experiment>();
      expNum = 0;
      resetCurPos();
      newButtonY = 80;
      newButtonX = 28;
      add(new Button(28, 30, 40, 150, "New Experiment", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            newExpPageCreate();
         }
      }));
      exists = true;
   }

   /* METHODS */
   public static expPage getExpPage(){
      if (exists){
         /* do nothing */
      }else{
         thePage = new expPage();
         thePage.addBackground("campr_exp_home.png");
      }
      return thePage;
   }

   public int incrimentGetExpNum(){
      expNum++;
      return expNum;
   }

   private void newExpPageCreate(){
      Page p = new Page("New Experiment", 750, 500, new CloseReset());
      p.addBackground("campr_new_exp.png", 0, 0);
      //p.descHelper("Name: Experiment "+(expNum+1));
      JTextField expName = p.newTextInput("Experiment name:", 100);
      JTextField resName = p.newTextInput("Researcher name:", 100);

      /* Keep track of user inputs with submit button */
      SubmitButtonClick submitB = new SubmitButtonClick();
      submitB.setPage(p);
      submitB.watch(expName);
      submitB.watch(resName);

      newTimeDropDown("Select a start time:\t", p, submitB, true);
      submitB.watchEnd(p.newTextInput("Enter a durration (hours):\t", 100));
      int position = p.getCurPos();
      //int position = newTimeLoc + 30;
      //TimeLocMouse mouse = new TimeLocMouse();
      //mouse.setLoc(position, p);
      //mouse.setTracker(submitB);
      //Button b = new Button(28, newTimeLoc, 20, 170, "Add Time Interval", mouse);
      //mouse.setB(b);
      //p.add(b);
      p.setCurPos(position);
      newTimeLightDropDown(p, submitB);
      position += 60;
      p.setCurPos(position);

      newCageDropDown("Select Cage for Experiment:\t", p, submitB, 0);

      p.add(new Button(540, 80, 40, 175, "Add More Cages", new MouseAdapter() {
         int clicked = 0;
         public void mouseClicked(MouseEvent e) {
            clicked ++;
            if(clicked < 8){
               newCageDropDown("Select Cage for Experiment:\t", p, submitB, clicked);
               return;
            }
            newCageDropDown("", p, submitB, clicked);
         }
      }));
      p.add(new Button(540, 130, 40, 175, "Cancel", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            p.resetCurPos();
            p.close();
         }
      }));
      p.add(new Button(540, 30, 40, 175, "Submit", submitB));

      p.reveal();
   }

   /* adds drop down menu on Page p for setting a cage */
   private void newCageDropDown(String desc, Page p, SubmitButtonClick tracker, int numCages){
      /* obtain positions for cage dropdown and assocciated time menu */
      /* Check if column reset is needed (there can only be 9 cages per column)*/
      int resetPos = numCages%8;
      int position =  p.getCurPos();
      if(resetPos == 0 && numCages!=0){
         /* set position to top of next column */
         position -= 240;
      }
      int columnNum = numCages/8;
      if (columnNum == 4){
         /* cage capacity full */
         return;
      }

      /* create cage menu and add to page */
      JLabel sd = new JLabel(desc);
      sd.setBounds(28, position, 200, 20);
      sd.setVisible(true);
      p.add(sd);
      int numberOfCages = cages.size();
      String[] sdChoices = new String[numberOfCages+1];
      sdChoices[0] = "";
      for(int i = 0; i < numberOfCages; i++){
         /* add cage to menu */
         Cage curCage = cages.get(i);
         sdChoices[i+1] = curCage.getID();
      }
      JComboBox<String> cb = new JComboBox<String>(sdChoices);
      cb.setBounds(300+(110*columnNum), position, 100, 20);
      cb.setVisible(true);
      p.add(cb);
      position += 30;
      p.setCurPos(position);
      /* add time interval options */
      tracker.watch(cb);
   }

   /* adds drop down menu on Page p for setting a light durration */
   private JComboBox<String> newDurrDropDown(String desc, Page p){
      /* obtain position for dropdown */

      int position =  p.getCurPos();

      /* create cage menu and add to page */
      JLabel sd = new JLabel(desc);
      sd.setBounds(28, position, 200, 20);
      sd.setVisible(true);
      p.add(sd);

      String[] sdChoices = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};

      JComboBox<String> cb = new JComboBox<String>(sdChoices);
      cb.setBounds(300, position, 100, 20);
      cb.setVisible(true);
      p.add(cb);
      position += 30;
      p.setCurPos(position);
      return cb;
   }


   /* adds drop down menu on Page p for setting a time at position pos */
   public void newTimeDropDown(String desc, Page p, SubmitButtonClick tracker, boolean Start){
      int position =  p.getCurPos();
      /* set instructional text */
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,position,200,20);
      sd.setVisible(true);
      p.add(sd);
      /* watch menu as start time or end time depending on input boolean */
      if(Start){
         tracker.watchStart(p.timeMenuHelper(300, position, true), p.timeMenuHelper(380, position, false));
      }else{
         //tracker.watchEnd(p.timeMenuHelper(300, position, true), p.timeMenuHelper(380, position, false));
      }
      /* update curPos */
      position += 30;
      p.setCurPos(position);
      //TODO add date stuff
   }

   /* adds drop down menu on Page p for setting a time */
   private void newTimeLightDropDown(Page p, SubmitButtonClick tracker){
      int position =  p.getCurPos();

      //tracker.watchOnTime(p.timeMenuHelper(300, position, true));
      //tracker.watchOnTime(p.timeMenuHelper(380, position, false));
      //tracker.watchOffTime(p.timeMenuHelper(300, position+30, true));
      //tracker.watchOffTime(p.timeMenuHelper(380, position+30, false));
      tracker.watchOnTime(newDurrDropDown("Select lights on durration", p));
      tracker.watchOffTime(newDurrDropDown("Select lights off durration", p));

      //p.descHelper("Turn Lights On:");
      //p.descHelper("Turn Lights Off:");
   }

   public void addExpButton(Experiment exp){
      if(newButtonY > 850){
         newButtonX += 160;
         if (newButtonX >= getXDim()-160){
            getFrame().setPreferredSize(new Dimension(newButtonX + 160, 900));
            reveal();
         }
         newButtonY = 80;
      }
      /* write list of cages */
      ArrayList<Cage> cages = exp.getCages();
      String cageString = "Assocciated Cages: ";
      Cage cur;
      for(int i = 0; i < cages.size(); i++){
         cur = cages.get(i);
         if(i > 0){
            cageString += ", "+cur.getName();
         }else{
            /* edge case */
            cageString += cur.getName();
         }
      }
      /* write list of on and off times */
      String onString = "Lights on durration: "+exp.getOnDurr();
      String offString = "Lights off durration: "+exp.getOffDurr();
      String time;
      /*ArrayList<String> ons = exp.getOnTimes();
      ArrayList<String> offs = exp.getOffTimes();
      for(int i = 0; i < ons.size(); i++){
         time = ons.get(i);
         if(i > 0){
            onString += ", "+time;
            time = offs.get(i);
            offString += ", "+time;
         }else{
            /* edge case
            onString += time;
            time = offs.get(i);
            offString += time;
         }
      }*/
      //expNum++;
      String expString = exp.getName();

      /* make display page */
      Page displayPage = new Page(expString, 800, 800);
      displayPage.resetCurPos();
      displayPage.descHelper("Experiment ID: "+expString);
      displayPage.descHelper("Researcher: "+exp.getResearcher());
      displayPage.descHelper(cageString);
      displayPage.descHelper("Start Time: "+exp.getStart());
      displayPage.descHelper("Experiment Durration: "+exp.getEnd());
      displayPage.descHelper(onString);
      displayPage.descHelper(offString);
      displayPage.resetCurPos();

      add(new Button(newButtonX, newButtonY, 40, 150, expString, displayPage));
      newButtonY+=50;
   }

   public void addCage(Cage c){
      cages.add(c);
   }
   public Cage getCageFromString(String cageName){
      int cSize = cages.size();
      for(int i = 0; i < cSize; i++){
         if(cageName == cages.get(i).getID()){
            return cages.get(i);
         }
      }
      return new Cage();
   }
   /* Removes cage from list of available cages */
   public void claimCage(String cageName){
      Cage forRemoval = getCageFromString(cageName);
      cages.remove(forRemoval);
   }
   public void claimCage(Cage forRemoval){
      cages.remove(forRemoval);
   }
}
