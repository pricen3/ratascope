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

   private static class Closer extends WindowAdapter {
      public void windowClosing(WindowEvent e ) {
         System.exit(0);
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
      super("Experiments", 550, 900);
      expButtons = new ArrayList<Button>();
      exps = new ArrayList<Experiment>();
      expNum = 0;
      newButtonY = 80;
      newButtonX = 28;
      add(new Button(28, 30, 40, 150, "New Experiment", newExpPageCreate()));
      /*add(new Button(28, 32, 40, 200, "Submit", new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
      System.out.println(expNum);
   }}));*/
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

   private Page newExpPageCreate(){
      Page p = new Page("New Experiment", 550, 900);
      p.addBackground("campr_logo.png", 0, 0);
      newTimeDropDown("Select a start time:\t", p, 30);
      newTimeDropDown("Select an end time:\t", p, 60);
      p.add(new Button(28, 200, 40, 150, "Submit", new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
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
         }}));
      return p;
   }

   /* adds drop down menu on Page p for setting a time at position pos*/
   private void newTimeDropDown(String desc, Page p, int pos){
      JLabel sd = new JLabel(desc);
      sd.setBounds(28,pos,200,20);
      sd.setVisible(true);
      p.add(sd);
      String[] sdChoices = {"12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30",
         "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30",
         "10:00", "10:30", "11:00", "11:30"};
         String[] sdChoices2 = {"AM", "PM"};
      JComboBox<String> cb = new JComboBox<String>(sdChoices);
      JComboBox<String> cb2 = new JComboBox<String>(sdChoices2);
      cb.setBounds(300,pos, 80, 20);
      cb2.setBounds(400,pos, 80, 20);
      cb.setVisible(true);
      p.add(cb);
      cb2.setVisible(true);
      p.add(cb2);
   }
}
