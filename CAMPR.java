import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CAMPR {

   public static Boolean DEBUG = false;
   public static void main(String[] args) throws Exception{
      try{
         Page frame = new Page("CAMPR", 900, 550);
         frame.addBackground("mouse.jpg");
         frame.reveal();
         Page frame2 = new Page("CAMPR2", 900, 550);
         frame2.reveal();
      }catch(Exception ex){
         System.out.println("*shrug*");
         ex.printStackTrace();
      }

   }
}
