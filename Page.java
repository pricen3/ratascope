import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
   private Background back;
   private ArrayList<JButton> buttons;
   private Boolean revealed;


   /* CONSTRUCTORS */
   public Page(String t, int xDimension, int yDimension){
      buttons = new ArrayList<JButton>();
      back = new Background();
      title = t;
      x = xDimension;
      y = yDimension;
      revealed = false;
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
   public JFrame getFrame(){
      return frame;
   }
   public int getXDim(){
      return x;
   }
   public void reveal(){
      if(revealed){
         frame.dispose();
         revealed = false;
      }
      revealed = true;
      frame.add(back);
      frame.pack();
      frame.setVisible(true);
      int numButtons = buttons.size();
      for (int i=0; i < numButtons; i++){
         back.add(buttons.get(i), new Integer(5));
      }
   }

   public void add(Button b){
      buttons.add(b.getImg());
      if (revealed){
         b.addToBackground(back);
      }
      //TODO add reshaping function if to many buttons...
   }
   public void add(JLabel p){
      back.add(p, new Integer(5));
   }
   public void add(JComboBox<String> p){
      back.add(p, new Integer(5));
   }
   public void add(JTextField p){
      back.add(p, new Integer(5));
   }

   public void addBackground(String img){
      /* Adds background image */
      try{
         back = new Background(img, 0, 0);
      }catch(Exception ex){
         System.out.println("Error: non-existant background image.");
         ex.printStackTrace();
      }
   }
   public void addBackground(String img, int x, int y){
      /* Adds background image at coordinates (x,y) */
      try{
         back = new Background(img, x, y);
      }catch(Exception ex){
         System.out.println("Error: non-existant background image.");
         ex.printStackTrace();
      }
   }
   public void close(){
      frame.dispose();
   }
}
