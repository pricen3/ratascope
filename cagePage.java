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


   /* CONSTRUCTORS */
   private cagePage(){
      super("Cages", 550, 900);
      cageButtons = new ArrayList<Button>();
      cages = new ArrayList<Experiment>();
      cageNum = 0;
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
      Page p = new Page("New Cage", 550, 900);
      p.addBackground("campr_logo.png", 0, 0);
      p.add(new Button(28, 32, 40, 150, "Submit", new MouseAdapter() {
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
         }}));
      return p;
   }
}
