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

   private static class Closer extends WindowAdapter {
      public void windowClosing(WindowEvent e ) {
         System.exit(0);
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
      add(new Button(28, 30, 40, 150, "New Cage", newExpPageCreate()));
      /*add(new Button(28, 32, 40, 200, "Submit", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            System.out.println(cageNum);
         }}));*/
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

   private Page newExpPageCreate(){
      Page p = new Page("New Cage", 750, 900);
      p.addBackground("campr_logo.png", 0, 0);
      descHelper(p, "ID number: "+(cageNum+1));
      newTimeDropDown(p);
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
         }
      }));
      p.add(new Button(540, 110, 40, 175, "Add Time Interval", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            newTimeDropDown(p);
         }
      }));
      p.add(new Button(540, 160, 40, 175, "Get Notes"));
      return p;
   }

   /* adds drop down menu on Page p for setting a time at position pos*/
   private void newTimeDropDown(Page p){
      String[] sdChoices = {"12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30",
         "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30",
         "10:00", "10:30", "11:00", "11:30"};
      String[] sdChoices2 = {"AM", "PM"};
      for(int i= 0; i <= 30; i += 30){
         timeMenuHelper(p, 300, curPos+i, sdChoices);
         timeMenuHelper(p, 400, curPos+i, sdChoices2);
      }
      descHelper(p, "Turn Lights On");
      descHelper(p, "Turn Lights Off");
   }
   private void descHelper(Page p, String desc){
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,curPos,200,20);
      curPos += 30;
      sd.setVisible(true);
      p.add(sd);
   }
   private void timeMenuHelper(Page p, int xpos, int ypos, String[] Choices){
      JComboBox<String> cb = new JComboBox<String>(Choices);
      cb.setBounds(xpos, ypos, 80, 20);
      cb.setVisible(true);
      p.add(cb);
   }

}
