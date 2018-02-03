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

public class expPage extends Page {

   private static class CloseReset extends WindowAdapter {
      public void windowClosing(WindowEvent e ) {
         getExpPage().resetCurPos();
      }
   }

   private static class SubmitButtonClick extends MouseAdapter {
      private Page p;

      /* The following keep track of user inputs */
      private ArrayList<JTextField> textInputs, mouseInputs;
      private ArrayList<JComboBox<String>> cageInputs;
      private JComboBox<String> startTime, startAOrP, onTimeInput, offTimeInput;
      private JTextField durration;

      public void setPage(Page page){
         p = page;
         textInputs = new ArrayList<JTextField>();
         mouseInputs = new ArrayList<JTextField>();
         onTimeInput = new JComboBox<String>();
         offTimeInput = new JComboBox<String>();
         cageInputs = new ArrayList<JComboBox<String>>();
         startTime = new JComboBox<String>();
         startAOrP = new JComboBox<String>();
         durration = new JTextField();
      }
      public void watch(JTextField userInput){
         textInputs.add(userInput);
      }
      public void watchOnTime(JComboBox<String> userInput){
         onTimeInput=userInput;
      }
      public void watchOffTime(JComboBox<String> userInput){
         offTimeInput = userInput;
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
      public void watchMouse(JTextField userInput){
         mouseInputs.add(userInput);
      }

      public void mouseClicked(MouseEvent e) {
         p.clearErrors();
         if(!checkErrors()){
            return;
         }
         expPage expP = getExpPage();
         int tSize = textInputs.size();
         int cSize = cageInputs.size();
         String cageName;

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

         Experiment ex = new Experiment(resName, expName, start+" "+sAP,end);

         /* get cage and mouse ID input information */
         Cage cage = new Cage();
         MouseCage mCage;
         JComboBox<String> curTime1, curTime2, cur;
         JTextField curMouse;
         String curID; /* Will hold mouse ID */
         for (int i = 0; i < cSize; i ++){
            cur = cageInputs.get(i);
            cageName = (String)cur.getSelectedItem();
            if(!cageName.equals("")){
               curMouse = mouseInputs.get(i);
               curID = curMouse.getText();
               cage = expP.getCageFromString(cageName);
               ex.setCage(new MouseCage(cage, curID));
               expP.claimCage(cage);
            }
         }
         String s;
         s = (String)onTimeInput.getSelectedItem();
         ex.addOnDurr((String)s);
         s = (String)offTimeInput.getSelectedItem();
         ex.addOffDurr((String)s);
         System.out.println(resName);
         CAMPR.addExp(ex);
         expP.addExpButton(ex);
         p.close();
         expP.resetCurPos();////////////////////////
      }

      /* check for user input errors */
      public boolean checkErrors(){
         /* test cages */
         String t, test = "";
         JComboBox<String> cur;
         int cIS =  cageInputs.size();
         int arrayPos = 0;
         String[] cageArray = new String[cIS];
         String[] mouseArray = new String[cIS];
         /* check for duplicates */
         JTextField curMouse;
         String curID; /* Will hold mouse ID */
         for(int i = 0; i < cIS; i++){
            curMouse = mouseInputs.get(i);
            curID = curMouse.getText();
            cur = cageInputs.get(i);
            t = (String)cur.getSelectedItem();
            if(!t.equals("")){
               if(curID.equals("")){
                  p.errorMessHelper("Missing Field - Mouse ID.", 600);
                  return false;
               }
               test = t;
               for(int j = 0; j < arrayPos; j++){
                  if(test.equals(cageArray[j])){
                     p.errorMessHelper("Duplicate cages.", 600);
                     return false;
                  }
                  if(curID.equals(mouseArray[j])){
                     p.errorMessHelper("Duplicate Mouse ID's.", 600);
                     return false;
                  }
               }
               cageArray[arrayPos] = test;
               mouseArray[arrayPos] = curID;
               arrayPos++;
            }
         }
         /* test for at least one valid cage */
         if(test.equals("")){
            p.errorMessHelper("At least one valid cage required.", 600);
            return false;
         }
         String end = durration.getText();
         if(end.equals("")){
            p.errorMessHelper("durration required.", 600);
            return false;
         }else{
            try{
               Integer.parseInt(end);
            }catch (Exception ex){
               p.errorMessHelper("durration must be an integer.", 600);
               return false;
            }
         }

         /* test for valid EXPERIMENT name */
         JTextField rName = textInputs.get(0);
         String resName = rName.getText();
         if(resName.equals("")){
            p.errorMessHelper("Experiment name required.", 600);
            return false;
         }else{
            ArrayList<Experiment> allCurrent = CAMPR.getOngoing();
            int curSize = allCurrent.size();
            Experiment compare;
            String compName;
            for(int i = 0; i < curSize; i++){
               compare = allCurrent.get(i);
               compName = compare.getName();
               if(resName.equals(compName)){
                  p.errorMessHelper("Experiment name invalid due to pre-existing experiment with same name.", 600);
                  return false;
               }
            }
         }
         /* test for valid researcher name */
         rName = textInputs.get(1);
         resName = rName.getText();
         if(resName.equals("")){
            p.errorMessHelper("Researcher name required.", 600);
            return false;
         }
         String lightOn = (String)onTimeInput.getSelectedItem();
         String lightOff = (String)offTimeInput.getSelectedItem();
         if(lightOn.equals("0") && lightOff.equals("0")){
            p.errorMessHelper("Invalid lighting intervals.", 600);
            return false;
         }
         return true;
      }
   }

   /* FIELDS */
   private static boolean exists = false;
   private static expPage thePage;
   private ArrayList<Button> expButtons;
   private ArrayList<Experiment> exps;
   private int expNum;
   private int newButtonY;
   private int newButtonX;


   /* CONSTRUCTORS */
   private expPage(){
      super("Ongoing Experiments", 550, 900);
      expButtons = new ArrayList<Button>();
      exps = new ArrayList<Experiment>();
      expNum = 0;
      resetCurPos();
      newButtonY = 80;
      newButtonX = 28;
      add(new Button(28, 30, 40, 150, "New Experiment", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(newButtonY > 850){
               //TODO: this needs testing
               if(newButtonX + 160 > 400){
                  errorMessHelper("Max ongoing capacity. Experiments must end before new ones may be added.", 870);
                  return;
               }
            }
            newExpPageCreate();
         }
      }));
      add(new Button(188, 30, 40, 310, "Completed Experiments", completeExpPageCreate()));

      ArrayList<Experiment> savedExps = CAMPR.getOngoing();
      int savedSize = savedExps.size();
      for(int i = 0; i < savedSize; i++){
         addExpButton(savedExps.get(i));
      }

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
      Page p = new Page("New Experiment", 750, 630, new CloseReset());
      p.addBackground("campr_new_exp.png", 0, 0);
      JTextField expName = p.newTextInput("Experiment name:", 100);
      JTextField resName = p.newTextInput("Researcher name:", 100);

      /* Keep track of user inputs with submit button */
      SubmitButtonClick submitB = new SubmitButtonClick();
      submitB.setPage(p);
      submitB.watch(expName);
      submitB.watch(resName);

      newTimeDropDown("Select a start time*:\t", p, submitB);
      p.descHelper("*the experiment will begin at this time within 24 hours.");
      submitB.watchEnd(p.newTextInput("Enter a durration (hours):\t", 50));
      int position = p.getCurPos();
      p.setCurPos(position);
      newTimeLightDropDown(p, submitB);
      position += 60;
      p.setCurPos(position);
      p.descHelper("Select cage(s) for experiment. Enter ID of resident mouse in conjoined text box:");

      newCageDropDown(p, submitB, 0);

      p.add(new Button(540, 80, 40, 175, "Add More Cages", new MouseAdapter() {
         int clicked = 0;
         public void mouseClicked(MouseEvent e) {
            clicked ++;
            if(clicked < 8){
               newCageDropDown(p, submitB, clicked);
               return;
            }
            newCageDropDown(p, submitB, clicked);
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

   private Page completeExpPageCreate(){
      Page compPage = new Page("Completed Experiments", 900, 200);
      compPage.addBackground("campr_home.png");
      compPage.descHelper("Select an experiment and click submit to view details.");

      /* create experiment menu and add to page */
      ArrayList<Experiment> exps = CAMPR.getComplete();
      int numberOfExp = exps.size();
      String[] expChoices = new String[numberOfExp+1];
      expChoices[0] = "";
      Experiment curExp;
      for(int i = 0; i < numberOfExp; i++){
         /* add cage to menu */
         curExp = exps.get(i);
         expChoices[i+1] = curExp.getName();
      }
      JComboBox<String> cb = new JComboBox<String>(expChoices);
      cb.setBounds(28, compPage.getCurPos(), 190, 20);
      cb.setVisible(true);
      compPage.add(cb);

      /* add submit button */
      compPage.add(new Button(228, compPage.getCurPos(), 20, 150, "Submit", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            String selectedExp = (String)cb.getSelectedItem();
            Experiment ex = CAMPR.getCompletedExpFromString(selectedExp);
            if(ex == null){
               compPage.errorMessHelper("Error: ensure that valid experiment was selected.", 180);
               return;
            }
            Page displayPage = expDisplayPage(ex);
            displayPage.add(new Button(28, 130, 40, 200, "Retrieve Results", new MouseAdapter() {
               public void mouseClicked(MouseEvent e) {
                  //TODO: add retrieval Logic
               }
            }));
            displayPage.reveal();
         }
      }));
      compPage.setCurPos(compPage.getCurPos() + 30);

      return compPage;
   }

   /* adds drop down menu on Page p for setting a cage */
   private void newCageDropDown(Page p, SubmitButtonClick tracker, int numCages){
      /* obtain positions for cage dropdown and assocciated time menu */
      /* Check if column reset is needed (there can only be 9 cages per column)*/
      int resetPos = numCages%10;
      int position =  p.getCurPos();
      if(resetPos == 0 && numCages!=0){
         /* set position to top of next column */
         position -= 300;
      }
      int columnNum = numCages/10;
      if (columnNum == 3){
         /* cage capacity full */
         return;
      }

      /* create cage menu and add to page */
      ArrayList<Cage> cages = CAMPR.getAvail();
      int numberOfCages = cages.size();
      String[] sdChoices = new String[numberOfCages+1];
      sdChoices[0] = "";
      for(int i = 0; i < numberOfCages; i++){
         /* add cage to menu */
         Cage curCage = cages.get(i);
         sdChoices[i+1] = curCage.getID();
      }
      JComboBox<String> cb = new JComboBox<String>(sdChoices);
      cb.setBounds(28+(210*columnNum), position, 100, 20);
      cb.setVisible(true);
      p.add(cb);
      JTextField mouseInput = new JTextField();
      mouseInput.setBounds(128+(210*columnNum), position, 100, 20);
      mouseInput.setVisible(true);
      p.add(mouseInput);
      position += 30;
      p.setCurPos(position);
      /* add time interval options */
      tracker.watch(cb);
      tracker.watchMouse(mouseInput);
   }

   /* adds drop down menu on Page p for setting a light durration */
   private JComboBox<String> newDurrDropDown(String desc, Page p){
      /* obtain position for dropdown */

      int position =  p.getCurPos();

      /* create cage menu and add to page */
      JLabel sd = new JLabel(desc);
      sd.setBounds(28, position, 300, 20);
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
   public void newTimeDropDown(String desc, Page p, SubmitButtonClick tracker){
      int position =  p.getCurPos();
      /* set instructional text */
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,position,200,20);
      sd.setVisible(true);
      p.add(sd);

      /* watch menu as start time */
      tracker.watchStart(p.timeMenuHelper(300, position, true), p.timeMenuHelper(380, position, false));

      /* update curPos */
      position += 30;
      p.setCurPos(position);
   }

   /* adds drop down menu on Page p for setting a time */
   private void newTimeLightDropDown(Page p, SubmitButtonClick tracker){
      int position =  p.getCurPos();

      tracker.watchOnTime(newDurrDropDown("Select lights on durration (hours):", p));
      tracker.watchOffTime(newDurrDropDown("Select lights off durration (hours):", p));
   }

   /* Adds button to main Experiment page for veiwing displayPage for ongoing experiment */
   public void addExpButton(Experiment exp){
      if(newButtonY > 850){
         newButtonX += 160;
         /* The following code resizes the window to fit more experiments,
         but doing so would reintroduce the bug of dissapearing buttons.
         Potentially this logic will be
         if (newButtonX >= getXDim()-160){
            getFrame().setPreferredSize(new Dimension(newButtonX + 160, 900));
            reveal();
         } */
         newButtonY = 80;
      }

      Page displayPage = expDisplayPage(exp);
      String expString = exp.getName();

      /* Create Button */
      Button expBut = new Button(newButtonX, newButtonY, 40, 150, expString, displayPage);
      add(expBut);
      expButtons.add(expBut);
      newButtonY+=50;

      /* Add Cancelation button */
      displayPage.add(new Button(540, 30, 40, 175, "Cancel Experiment", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            exp.cancelExperiment();
            displayPage.resetCurPos();
            displayPage.close();
            expBut.remove();
            expButtons.remove(expBut);
            refreash();
            // TODO delete canceled experiment from database
         }
      }));
   }
   private void refreash(){
      int expButtonSize = expButtons.size();
      newButtonY = 80;
      newButtonX = 28;
      Button cur;
      for(int i = 0; i < expButtonSize; i++){
         if(newButtonY > 850){
            newButtonX += 160;
            newButtonY = 80;
         }
         cur = expButtons.get(i);
         cur.setBounds(newButtonX, newButtonY);
         newButtonY+=50;
      }
   }

   /* Creates display page for experiment */
   public Page expDisplayPage(Experiment exp){
      /* write list of cages */
      ArrayList<MouseCage> cages = exp.getCages();
      String cageString = "Assocciated Cages: ";
      Cage cur;
      MouseCage curMC;
      for(int i = 0; i < cages.size(); i++){
         curMC = cages.get(i);
         cur = curMC.getCage();
         if(i > 0){
            cageString += ", "+cur.getName()+" ("+curMC.getMouse()+")";
         }else{
            /* edge case */
            cageString += cur.getName();
         }
      }
      /* write list of on and off times */
      String onString = "Lights on durration: "+exp.getOnDurr()+" hours";
      String offString = "Lights off durration: "+exp.getOffDurr()+" hours";
      String time;
      String expString = exp.getName();

      /* make display page */
      Page displayPage = new Page(expString, 750, 500);
      displayPage.addBackground("campr_new_exp.png", 0, 0);
      displayPage.resetCurPos();
      displayPage.descHelper("Experiment ID: "+expString);
      displayPage.descHelper("Researcher: "+exp.getResearcher());
      displayPage.descHelper(cageString);
      displayPage.descHelper("Start Time: "+exp.getStart());
      displayPage.descHelper("Experiment Durration: "+exp.getExpDurr()+" hours");
      displayPage.descHelper(onString);
      displayPage.descHelper(offString);
      displayPage.resetCurPos(); //TODO do i need//want this?

      return displayPage;
   }

   public Cage getCageFromString(String cageName){
      ArrayList<Cage> cages = CAMPR.getAvail();
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
      Cage inUse = getCageFromString(cageName);
      CAMPR.useCage(inUse);
   }
   public void claimCage(Cage inUse){
      CAMPR.useCage(inUse);
   }
}
