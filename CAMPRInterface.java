import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CAMPRInterface {

   private static class Closer extends WindowAdapter {
      public void windowClosing(WindowEvent e ) {
         System.exit(0);
      }
   }
   public static Boolean DEBUG = false;
   public static void main(String[] args) throws Exception{

      Boolean quitting = false;

      JFrame frame = new JFrame();
      frame.setTitle("CAMPR");
      frame.setPreferredSize(new Dimension(1450,900));
      frame.setResizable(true);
      frame.addWindowListener(new Closer());

      frame.pack();
      frame.setVisible(true);

   }
}
