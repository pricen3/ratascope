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
         String[] sdChoices = {"12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30",
            "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30",
            "10:00", "10:30", "11:00", "11:30"};
         expPage.timeMenuHelper(p, 500, newTimeLoc, sdChoices);
         expPage.timeMenuHelper(p, 500, newTimeLoc+30, sdChoices);
         String[] sdChoices2 = {"AM", "PM"};
         expPage.timeMenuHelper(p, 600, newTimeLoc, sdChoices2);
         expPage.timeMenuHelper(p, 600, newTimeLoc+30, sdChoices2);
         b.remove();
      }
   }
   private static class SubmitButtonClick extends MouseAdapter {
      private Page p;

      /* The following keep track of user inputs */
      private ArrayList<JTextField> textInputs;
      private ArrayList<JComboBox<String>> timeInputs;
      private ArrayList<JComboBox<String>> cageInputs;
      public void setPage(Page page){
         p = page;
         textInputs = new ArrayList<JTextField>();
         timeInputs = new ArrayList<JComboBox<String>>();
         cageInputs = new ArrayList<JComboBox<String>>();
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

         /* get user input information */
         JComboBox<String> curTime, cur;
         for (int i = 0; i < cSize; i ++){
            cur = cageInputs.get(i);
            cageName[i] = (String)cur.getSelectedItem();
            for (int j = 0; j < 4; j++){
               curTime = timeInputs.get((i*4)+j);
               timeName[(i*4)+j] = (String)curTime.getSelectedItem();
               System.out.print(timeName[(i*4)+j]);
            }
            System.out.println(cageName[i]+" "+timeName[(i*4)]);
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
   private ArrayList<Button> expButtons;
   private ArrayList<Experiment> exps;
   private int expNum;
   private int curPos;
   private int newButtonY;
   private int newButtonX;


   /* CONSTRUCTORS */
   private expPage(){
      super("Experiments", 550, 900);
      expButtons = new ArrayList<Button>();
      exps = new ArrayList<Experiment>();
      expNum = 0;
      curPos = 30;
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
      }
      return thePage;
   }

   private void newExpPageCreate(){
      Page p = new Page("New Experiment", 750, 500, new CloseReset());
      //p.addBackground("campr_logo.png", 0, 0);
      descHelper(p, "Name: Experiment "+(expNum+1));
      JTextField resName = newTextInput("Researcher name:", p);

      /* Keep track of user inputs with submit button */
      SubmitButtonClick submitB = new SubmitButtonClick();
      submitB.setPage(p);
      submitB.watch(resName);

      newTimeDropDown("Select a start time:\t", p);
      newTimeDropDown("Select an end time:\t", p);
      newCageDropDown("Select Cage for Experiment:\t", p, submitB);

      p.add(new Button(540, 80, 40, 175, "Add More Cages", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            newCageDropDown("Select Cage for Experiment:\t", p, submitB);
         }
      }));
      p.add(new Button(540, 130, 40, 175, "Cancel", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            p.close();
            resetCurPos();
         }
      }));
      p.add(new Button(540, 30, 40, 175, "Submit", submitB));

      p.reveal();
   }

   /* adds drop down menu on Page p for setting a cage */
   private void newCageDropDown(String desc, Page p, SubmitButtonClick tracker){
      int newTimeLoc = curPos+60;
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,curPos,200,20);
      sd.setVisible(true);
      p.add(sd);
      String[] sdChoices = {"","Cage 1","Cage 2"};
      JComboBox<String> cb = new JComboBox<String>(sdChoices);
      cb.setBounds(300,curPos, 100, 20);
      cb.setVisible(true);
      p.add(cb);
      curPos += 30;
      TimeLocMouse mouse = new TimeLocMouse();
      mouse.setLoc(newTimeLoc, p);
      Button b = new Button(50, curPos, 20, 170, "Add Time Interval", mouse);
      mouse.setB(b);
      p.add(b);
      curPos += 30;
      tracker.watch(cb);
      newTimeLightDropDown(p, tracker);
   }

   private void descHelper(Page p, String desc){
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,curPos,200,20);
      curPos += 30;
      sd.setVisible(true);
      p.add(sd);
   }

   /* adds input text box to page */
   private JTextField newTextInput(String desc, Page p){
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,curPos,200,20);
      sd.setVisible(true);
      p.add(sd);
      JTextField tb = new JTextField();
      tb.setBounds(300,curPos, 100, 20);
      tb.setVisible(true);
      p.add(tb);
      curPos += 30;
      return tb;
   }

   /* adds drop down menu on Page p for setting a time at position pos*/
   private void newTimeDropDown(String desc, Page p){
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,curPos,200,20);
      sd.setVisible(true);
      p.add(sd);
      String[] sdChoices = {"12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30",
         "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30",
         "10:00", "10:30", "11:00", "11:30"};
      timeMenuHelper(p, 300, curPos, sdChoices);
      String[] sdChoices2 = {"AM", "PM"};
      timeMenuHelper(p, 400, curPos, sdChoices2);
      curPos+=30;
      //TODO add date stuff
   }
   public static JComboBox<String> timeMenuHelper(Page p, int xpos, int ypos, String[] Choices){
      JComboBox<String> cb = new JComboBox<String>(Choices);
      cb.setBounds(xpos,ypos, 80, 20);
      cb.setVisible(true);
      p.add(cb);
      return cb;
   }

   /* adds drop down menu on Page p for setting a time at position pos*/
   private void newTimeLightDropDown(Page p, SubmitButtonClick tracker){
      String[] sdChoices = {"12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30",
         "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30",
         "10:00", "10:30", "11:00", "11:30"};
      String[] sdChoices2 = {"AM", "PM"};
      for(int i= 0; i < 2; i++){
         tracker.watchTime(timeMenuHelper(p, 300, curPos+(i*30), sdChoices));
         tracker.watchTime(timeMenuHelper(p, 400, curPos+(i*30), sdChoices2));
      }
      descHelper(p, "      Turn Lights On:");
      descHelper(p, "      Turn Lights Off:");
   }

   public void resetCurPos(){
      curPos=30;
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
}
