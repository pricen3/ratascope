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
//TODO add functionality to save default cage names if no cage name entered

public class cagePage extends Page {

   private static class CloseReset extends WindowAdapter {
      public void windowClosing(WindowEvent e) {
         getCagePage().resetCurPos();
      }
   }

   private static class SubmitButtonClick extends MouseAdapter {
      private Page p;
      private JTextField name;
      private JTextField ip;

      public void setPage(Page page){
         p = page;
      }
      public void watchName(JTextField userInput){
         name = userInput;
      }
      public void watchIP(JTextField userInput){
         ip = userInput;
      }

      public void mouseClicked(MouseEvent e) {
         p.clearErrors();
         cagePage cP = cagePage.getCagePage();
         expPage eP = expPage.getExpPage();
         String cName = name.getText();
         String ipAdd = ip.getText();
         if(cName.equals("")){
            p.errorMessHelper("Name field required.");
         }else if(ipAdd.equals("")){
            p.errorMessHelper("IP Address field required.");
         }else{
            Cage c = new Cage(cName, ipAdd);
            CAMPRDatabase.cageInput(cName, ipAdd);
            cP.addCageButton(c);
            eP.addCage(c);
            p.close();
            cP.resetCurPos();
         }
      }
   }

   /* FIELDS */
   private static boolean exists = false;
   private static cagePage thePage;
   private ArrayList<Button> cageButtons;
   private ArrayList<Experiment> cages;
   private int cageNum;
   private int newButtonY;
   private int newButtonX;


   /* CONSTRUCTORS */
   private cagePage(){
      super("Cages", 550, 900);
      cageButtons = new ArrayList<Button>();
      cages = new ArrayList<Experiment>();
      cageNum = 0;
      newButtonY = 80;
      newButtonX = 28;
      add(new Button(28, 30, 40, 150, "New Cage", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            newCagePageCreate();
         }
      }));
      exists = true;
      ArrayList<Cage> savedCages = CAMPRDatabase.cageSelect();
      int savedSize = savedCages.size();
      expPage eP = expPage.getExpPage(); //TODO change this later for used cages in db
      for(int i = 0; i < savedSize; i++){
         addCageButton(savedCages.get(i));
         eP.addCage(savedCages.get(i));
      }

   }

   /* METHODS */
   public static cagePage getCagePage(){
      if (exists){
         /* do nothing */
      }else{
         thePage = new cagePage();
         thePage.addBackground("campr_cage_home.png");
      }
      return thePage;
   }

   private void newCagePageCreate(){
      Page p = new Page("New Cage", 750, 500, new CloseReset());
      p.addBackground("campr_new_cage.png", 0, 0);
      p.descHelper("ID: Cage "+(cageNum+1));

      /* Keep track of user inputs with submit button */
      SubmitButtonClick submitB = new SubmitButtonClick();
      submitB.setPage(p);

      submitB.watchName(p.newTextInput("Name of Cage: ", 150));
      submitB.watchIP(p.newTextInput("IP Address of Cage: ", 150));
      p.add(new Button(540, 60, 40, 175, "Submit", submitB));
      p.add(new Button(540, 110, 40, 175, "Get Notes"));
      p.add(new Button(540, 160, 40, 175, "Cancel", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            p.resetCurPos();
            p.close();
         }
      }));
      p.reveal();
   }

   /* Add a button that reveals stored cage information upon click */
   public void addCageButton(Cage c){
      if(newButtonY > 850){
         newButtonX += 160;
         if (newButtonX >= getXDim()-160){
            getFrame().setPreferredSize(new Dimension(newButtonX + 160, 900));
            reveal();
         }
         newButtonY = 80;
      }
      cageNum++;
      String cagestring = c.getID();
      if(cagestring.equals("")){
         cagestring = "Cage "+cageNum;
      }

      /* make display page */
      Page displayPage = new Page(cagestring, 800, 800);
      displayPage.resetCurPos();
      displayPage.descHelper("Cage ID: Cage "+cageNum);
      displayPage.descHelper("Cage Name: "+cagestring);
      displayPage.descHelper("Cage IP: "+c.getIP());
      displayPage.resetCurPos();

      add(new Button(newButtonX, newButtonY, 40, 150, cagestring, displayPage));
      newButtonY+=50;
   }

}
