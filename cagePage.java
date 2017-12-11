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

public class cagePage extends Page {

   private static class CloseReset extends WindowAdapter {
      public void windowClosing(WindowEvent e ) {
         getCagePage().resetCurPos();
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
   private int curPos;


   /* CONSTRUCTORS */
   private cagePage(){
      super("Cages", 550, 900);
      cageButtons = new ArrayList<Button>();
      cages = new ArrayList<Experiment>();
      cageNum = 0;
      curPos = 30;
      newButtonY = 80;
      newButtonX = 28;
      add(new Button(28, 30, 40, 150, "New Cage", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            newCagePageCreate();
         }
      }));
      exists = true;
   }

   /* METHODS */
   public static cagePage getCagePage(){
      if (exists){
         /* do nothing */
      }else{
         thePage = new cagePage();
      }
      return thePage;
   }

   private void newCagePageCreate(){
      Page p = new Page("New Cage", 750, 900, new CloseReset());
      p.addBackground("campr_logo.png", 0, 0);
      descHelper(p, "ID: Cage "+(cageNum+1));
      newTextInput("Name of Cage: ", p);
      newTextInput("IP Address of Cage: ", p);
      p.add(new Button(540, 60, 40, 175, "Submit", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(newButtonY > 850){
               newButtonX += 160;
               if (newButtonX >= getXDim()-160){
                  getFrame().setPreferredSize(new Dimension(newButtonX + 160, 900));
                  reveal();
               }
               newButtonY = 80;
            }
            cageNum++;
            String cagestring = "Cage "+cageNum;
            add(new Button(newButtonX, newButtonY, 40, 150, cagestring));
            newButtonY+=50;
            p.close();
            resetCurPos();
         }
      }));
      p.add(new Button(540, 110, 40, 175, "Get Notes"));
      p.add(new Button(540, 160, 40, 175, "Cancel", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            p.close();
            resetCurPos();
         }
      }));
      p.reveal();
   }

   /* adds input text box to page */
   private void newTextInput(String desc, Page p){
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,curPos,200,20);
      sd.setVisible(true);
      p.add(sd);
      JTextField tb = new JTextField();
      tb.setBounds(300,curPos, 100, 20);
      tb.setVisible(true);
      p.add(tb);
      curPos += 30;
   }

   private void descHelper(Page p, String desc){
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,curPos,200,20);
      curPos += 30;
      sd.setVisible(true);
      p.add(sd);
   }

   public void resetCurPos(){
      curPos=30;
   }

}
