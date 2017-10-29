import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CFrame {

   private static class Closer extends WindowAdapter {
      public void windowClosing(WindowEvent e ) {
         System.exit(0);
      }
   }
   /* FIELDS */
   private static Boolean mainInterface=false;
   private String title;
   private int x, y;
   private JFrame frame;


   /* CONSTRUCTORS */
   public CFrame(String t, int xDimension, int yDimension){
      title = t;
      x = xDimension;
      y = yDimension;
      frame = new JFrame();
      frame.setTitle(title);
      frame.setPreferredSize(new Dimension(x, y));
      frame.setResizable(false);
      if(mainInterface==false){
         mainInterface=true;
         frame.addWindowListener(new Closer());
      }
   }

   public void reveal(){
      frame.pack();
      frame.setVisible(true);
   }

   public void add(Button b){
      frame.add(b);
   }
   public void add(Background b){
      frame.add(b);
   }
}
