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
   private int curPos;
   private JLabel error;


   /* CONSTRUCTORS */
   public Page(String t, int xDimension, int yDimension){
      curPos = 30;
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
   public Page(String t, int xDimension, int yDimension, WindowAdapter close){
      curPos = 30;
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
      frame.addWindowListener(close);

   }

   /* METHODS */

   /* Retrieving Field Information */

   public JFrame getFrame(){
      return frame;
   }

   public int getXDim(){
      return x;
   }

   public int getCurPos(){
      return curPos;
   }

   /* Page Functions */

   /* open page */
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
         System.out.println(buttons.get(i).getText());
         back.add(buttons.get(i), new Integer(5));
      }
      back.resetBack();
   }

   public void close(){
      frame.dispose();
   }

   public void setCurPos(int cur){
      /* sets current position to indicated integer */
      curPos = cur;
   }
   public void resetCurPos(){
      /* resets current position to top of page */
      curPos = 30;
   }

   /* Functions for Adding Page Elements */

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

   /* helper function for adding text to page
   * Note: can only be 500 char long */
   public void descHelper(String desc){
      int position =  getCurPos();
      JLabel sd = new JLabel(desc);
      sd.setBounds(28, position, 500, 20);
      position += 30;
      sd.setVisible(true);
      this.add(sd);
      setCurPos(position);
   }

   /* helper function for adding error messages to page */
   public void errorMessHelper(String desc){
      int position =  getCurPos();
      error = new JLabel("<html><font color='red'>*"+desc+"</font></html>");
      error.setBounds(28, position, 600, 20);
      position += 30;
      error.setVisible(true);
      this.add(error);
      //setCurPos(position);
   }
   public void clearErrors(){
      if(error==null){
         return;
      }
      back.remove(error);
      back.revalidate();
      back.repaint();
   }

   /* adds input text box to page */
   public JTextField newTextInput(String desc, int len){
      int position =  getCurPos();
      JLabel sd = new JLabel(desc);
      sd.setBounds(28, position,200,20);
      sd.setVisible(true);
      this.add(sd);
      JTextField tb = new JTextField();
      tb.setBounds(300, position, len, 20);
      tb.setVisible(true);
      this.add(tb);
      position += 30;
      setCurPos(position);
      return tb;
   }

   /* Creates time menu drop down: if numberedTimes is false, menu only says AM and PM */
   public JComboBox<String> timeMenuHelper(int xpos, int ypos, boolean numberedTimes){
      String[] choices1 = {"AM", "PM"};
      String[] choices2 = {"12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30",
         "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30",
         "10:00", "10:30", "11:00", "11:30"};
      String[] choices = choices1;
      if(numberedTimes){
         choices = choices2;
      }
      JComboBox<String> cb = new JComboBox<String>(choices);
      cb.setBounds(xpos,ypos, 80, 20);
      cb.setVisible(true);
      this.add(cb);
      return cb;
   }

   /* Creates date menu drop down */
   /*public JComboBox<String> dateMenuHelper(int xpos, int ypos, boolean numberedTimes){
      String[] month = {"January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
      String[] day = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28"};
      String[] choices = choices1;
      if(numberedTimes){
         choices = choices2;
      }
      JComboBox<String> cb = new JComboBox<String>(choices);
      cb.setBounds(xpos,ypos, 80, 20);
      cb.setVisible(true);
      this.add(cb);
      return cb;
   }*/
}
