import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.awt.image.BufferedImage;

public class Page {

   private static class Closer extends WindowAdapter {
      public void windowClosing(WindowEvent e ) {
         System.exit(0);
      }
   }
   /* FIELDS */
   private static Boolean mainPage=false;
   private String title;
   private int x, y;
   private JFrame frame;


   /* CONSTRUCTORS */
   public Page(String t, int xDimension, int yDimension){
      title = t;
      x = xDimension;
      y = yDimension;
      frame = new JFrame();
      frame.setTitle(title);
      frame.setPreferredSize(new Dimension(x, y));
      frame.setResizable(false);
      if(mainPage==false){
         mainPage=true;
         frame.addWindowListener(new Closer());
      }
   }
   public Page(String t, int xDimension, int yDimension, boolean mainP){
      mainPage = mainP;
      title = t;
      x = xDimension;
      y = yDimension;
      frame = new JFrame();
      frame.setTitle(title);
      frame.setPreferredSize(new Dimension(x, y));
      frame.setResizable(false);
      if(mainPage==false){
         mainPage=true;
         frame.addWindowListener(new Closer());
      }
   }

   /* METHODS */
   public void reveal(){
      frame.pack();
      frame.setVisible(true);
   }

   public void add(Button b){
      frame.add(b);
   }

   public void addBackground(String img){
      /* Adds background image */
      try{
         Background back = new Background(img);
         frame.add(back);
      }catch(Exception ex){
         System.out.println("here...");
         ex.printStackTrace();
      }
   }
}
