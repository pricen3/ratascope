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
//TODO fix overlay background bug

public class expPage extends Page {

   private static class CloseReset extends WindowAdapter {
      public void windowClosing(WindowEvent e ) {
         getExpPage().resetCurPos();
      }
   }
   private static class TimeLocMouse extends MouseAdapter {
      private int newTimeLoc;
      private Page p;
      private Button b;
      public void setLoc(int i, Page page){
         newTimeLoc = i;
         p = page;
         b=null;
      }
      public void setB(Button but){
         b = but;
      }
      public void mouseClicked(MouseEvent e) {
         /* time menu (12:00 through 11:30)*/
         p.timeMenuHelper(500, newTimeLoc, true);
         p.timeMenuHelper(500, newTimeLoc+30, true);
         /* {"AM", "PM"} menus */
         p.timeMenuHelper(600, newTimeLoc, false);
         p.timeMenuHelper(600, newTimeLoc+30, false);
         b.remove();
      }
   }
   private static class SubmitButtonClick extends MouseAdapter {
      private Page p;

      /* The following keep track of user inputs */
      private ArrayList<JTextField> textInputs;
      private ArrayList<JComboBox<String>> timeInputs;
      private ArrayList<JComboBox<String>> cageInputs;
      private JComboBox<String> startTime, startAOrP, endTime, endAOrP;

      public void setPage(Page page){
         p = page;
         textInputs = new ArrayList<JTextField>();
         timeInputs = new ArrayList<JComboBox<String>>();
         cageInputs = new ArrayList<JComboBox<String>>();
         startTime = new JComboBox<String>();
         startAOrP = new JComboBox<String>();
         endTime = new JComboBox<String>();
         endAOrP = new JComboBox<String>();
      }
      public void watch(JTextField userInput){
         textInputs.add(userInput);
      }
      public void watchTime(JComboBox<String> userInput){
         timeInputs.add(userInput);
      }
      public void watch(JComboBox<String> userInput){
         cageInputs.add(userInput);
      }
      public void watchStart(JComboBox<String> time, JComboBox<String> amOrPm){
         startTime = time;
         startAOrP = amOrPm;
      }
      public void watchEnd(JComboBox<String> time, JComboBox<String> amOrPm){
         endTime = time;
         endAOrP = amOrPm;
      }

      public void mouseClicked(MouseEvent e) {
         expPage expP = getExpPage();
         int tSize = textInputs.size();
         int tiSize = timeInputs.size();
         int cSize = cageInputs.size();
         String resName="";
         String[] cageName = new String[cSize];
         String[] timeName = new String[tiSize];

         /* get researcher name */
         JTextField rName = textInputs.get(0);
         resName = rName.getText();

         /* get time input information */
         String start = (String)startTime.getSelectedItem();
         String sAP = (String)startAOrP.getSelectedItem();
         String end = (String)endTime.getSelectedItem();
         String eAP = (String)endAOrP.getSelectedItem();

         Experiment ex = new Experiment(resName, "Experiment 1", start+" "+sAP,end+" "+eAP); //TODO make Exp 1 not hardcoded

         /* get cage input information */
         Cage cage = new Cage();
         JComboBox<String> curTime, cur;
         for (int i = 0; i < cSize; i ++){
            cur = cageInputs.get(i);
            cageName[i] = (String)cur.getSelectedItem();
            cage = expP.getCageFromString(cageName[i]); //TODO: handle cages w same name
            for (int j = 0; j < 4; j++){
               curTime = timeInputs.get((i*4)+j);
               timeName[(i*4)+j] = (String)curTime.getSelectedItem();
               System.out.print(timeName[(i*4)+j]);
            }
            cage.setLightTimes(timeName[(i*4)]+" "+timeName[(i*4)+1], timeName[(i*4)+2]+" "+timeName[(i*4)+3]);
            ex.setCage(cage);
         }
         System.out.println(resName);
         expP.addExpButton();
         p.close();
         expP.resetCurPos();
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

   private void newExpPageCreate(){
      Page p = new Page("New Experiment", 750, 500, new CloseReset());
      p.addBackground("campr_new_exp.png", 0, 0);
      p.descHelper("Name: Experiment "+(expNum+1));
      JTextField resName = p.newTextInput("Researcher name:", 100);

      /* Keep track of user inputs with submit button */
      SubmitButtonClick submitB = new SubmitButtonClick();
      submitB.setPage(p);
      submitB.watch(resName);

      newTimeDropDown("Select a start time:\t", p, submitB, true);
      newTimeDropDown("Select an end time:\t", p, submitB, false);
      newCageDropDown("Select Cage for Experiment:\t", p, submitB);

      p.add(new Button(540, 80, 40, 175, "Add More Cages", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            newCageDropDown("Select Cage for Experiment:\t", p, submitB);
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
   private void newCageDropDown(String desc, Page p, SubmitButtonClick tracker){
      /* obtain positions for cage dropdown and assocciated time menu */
      int position =  p.getCurPos();
      int newTimeLoc = position + 60;
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
      cb.setBounds(300, position, 100, 20);
      cb.setVisible(true);
      p.add(cb);
      /* Add additional time interval menu option for cage */
      position += 30;
      TimeLocMouse mouse = new TimeLocMouse();
      mouse.setLoc(newTimeLoc, p);
      Button b = new Button(50, position, 20, 170, "Add Time Interval", mouse);
      mouse.setB(b);
      p.add(b);
      /* update curPos for page */
      position += 30;
      p.setCurPos(position);
      /* add time interval options */
      tracker.watch(cb);
      newTimeLightDropDown(p, tracker);
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
         tracker.watchStart(p.timeMenuHelper(300, position, true), p.timeMenuHelper(400, position, false));
      }else{
         tracker.watchEnd(p.timeMenuHelper(300, position, true), p.timeMenuHelper(400, position, false));
      }
      /* update curPos */
      position += 30;
      p.setCurPos(position);
      //TODO add date stuff
   }

   /* adds drop down menu on Page p for setting a time at position pos*/
   private void newTimeLightDropDown(Page p, SubmitButtonClick tracker){
      //TODO: distinguish between light on and off times
      int position =  p.getCurPos();
      for(int i= 0; i < 2; i++){
         tracker.watchTime(p.timeMenuHelper(300, position+(i*30), true));
         tracker.watchTime(p.timeMenuHelper(400, position+(i*30), false));
      }
      p.descHelper("      Turn Lights On:");
      p.descHelper("      Turn Lights Off:");
   }

   public void addExpButton(){
      if(newButtonY > 850){
         newButtonX += 160;
         if (newButtonX >= getXDim()-160){
            getFrame().setPreferredSize(new Dimension(newButtonX + 160, 900));
            reveal();
         }
         newButtonY = 80;
      }
      expNum++;
      String expString = "Experiment "+expNum;
      add(new Button(newButtonX, newButtonY, 40, 150, expString));
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
}
