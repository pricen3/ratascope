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

//TODO refreash buttons on both this and exp page

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
         String cName = name.getText();
         String ipAdd = ip.getText();
         if(cName.equals("")){
            p.errorMessHelper("Name field required.");
         }else if(ipAdd.equals("")){
            p.errorMessHelper("IP Address field required.");
         }else{
            /* check for duplicates */
            ArrayList<Cage> avail = CAMPR.getAvail();
            ArrayList<Cage> inUse = CAMPR.getInUse();
            int availSize = avail.size();
            int inUseSize = inUse.size();
            int i = max(availSize, inUseSize);
            while(i > 0){
               i--;
               if(i<availSize){
                  if(isDuplicate(cName, ipAdd, avail.get(i), p)){
                     return;
                  }
               }
               if(i<inUseSize){
                  if(isDuplicate(cName, ipAdd, inUse.get(i), p)){
                     return;
                  }
               }
            }
            Cage c = new Cage(cName, ipAdd);
            CAMPR.addCage(c);
            cP.addCageButton(c);
            p.close();
            cP.resetCurPos();
         }
      }
      private int max(int x, int y){
         if(x > y){
            return x;
         }
         return y;
      }
      private boolean isDuplicate(String cName, String ipAdd, Cage cur, Page p){
         if(cName.equals(cur.getName())){
            p.errorMessHelper("Duplicate name.");
            return true;
         }
         if(ipAdd.equals(cur.getIP())){
            p.errorMessHelper("Duplicate IP address.");
            return true;
         }
         return false;
      }
   }

   /* FIELDS */
   private static boolean exists = false;
   private static cagePage thePage;
   private ArrayList<Button> cageButtons;
   private int cageNum;
   private int newButtonY;
   private int newButtonX;


   /* CONSTRUCTORS */
   private cagePage(){
      super("Cages", 550, 900);
      cageButtons = new ArrayList<Button>();
      cageNum = 0;
      newButtonY = 80;
      newButtonX = 28;
      add(new Button(28, 30, 40, 150, "New Cage", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            newCagePageCreate();
         }
      }));
      exists = true;
      ArrayList<Cage> savedCages = CAMPR.getAvail();
      int savedSize = savedCages.size();
      for(int i = 0; i < savedSize; i++){
         addCageButton(savedCages.get(i));
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
      p.add(new Button(540, 30, 40, 175, "Submit", submitB));
      p.add(new Button(540, 80, 40, 175, "Get Notes"));
      p.add(new Button(540, 130, 40, 175, "Cancel", new MouseAdapter() {
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
      Page displayPage = new Page(cagestring, 750, 500);
      displayPage.addBackground("campr_new_cage.png", 0, 0);
      displayPage.resetCurPos();
      displayPage.descHelper("Cage ID: Cage "+cageNum);
      displayPage.descHelper("Cage Name: "+cagestring);
      displayPage.descHelper("Cage IP: "+c.getIP());
      //displayPage.resetCurPos();

      Button displayButton = new Button(newButtonX, newButtonY, 40, 150, cagestring, displayPage);
      cageButtons.add(displayButton);
      add(displayButton);
      newButtonY+=50;

      /* Add Deletion button */
      // TODO add cancelation logic and background
      displayPage.add(new Button(540, 30, 40, 175, "Delete Cage", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            //Delete Cage from database
            /* confirm cage is not in use */
            ArrayList<Cage> inUse = CAMPR.getInUse();
            if(inUse.contains(c)){
               displayPage.errorMessHelper("Cage can not be deleted until it is no longer in use");
               return;
            }

            displayPage.resetCurPos();
            displayPage.close();
            displayButton.remove();
            cageButtons.remove(displayButton);
            refreash();
         }
      }));
   }
   private void refreash(){
      int cageButtonSize = cageButtons.size();
      newButtonY = 80;
      newButtonX = 28;
      Button cur;
      for(int i = 0; i < cageButtonSize; i++){
         if(newButtonY > 850){
            newButtonX += 160;
            newButtonY = 80;
         }
         cur = cageButtons.get(i);
         cur.setBounds(newButtonX, newButtonY);
         newButtonY+=50;
      }
   }
}
