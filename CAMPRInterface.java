import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CAMPRInterface {

   public static Boolean DEBUG = false;
   public static void main(String[] args) throws Exception{
      try{
         Boolean quitting = false;
         Background b = new Background("mouse.jpg");
         Button butt = new Button(1,1,100,100);
         butt.addToBackground(b);
         CFrame frame = new CFrame("CAMPR", 900, 550);
         frame.add(b);
         frame.reveal();
         CFrame frame2 = new CFrame("CAMPR2", 900, 550);
         frame2.reveal();
      }catch(Exception ex){
         System.out.println("*shrug*");
         ex.printStackTrace();
      }

   }
}
