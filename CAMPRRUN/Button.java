package CAMPRRUN;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.lang.Integer;

class Button extends JPanel{

   /*FIELDS*/
   private int x, y, h, w;
   private JButton buttonImg;

   /*CONSTRUCTORS*/
   public Button (int x1, int y1, int h1, int w1, String txt) {
      x=x1;
      y=y1;
      h=h1;
      w=w1;
      setBounds(x,y,w,h);
      buttonImg = new JButton(txt);
      buttonImg.setBounds(x,y,w,h);
      setOpaque(false);
      buttonImg.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            clicked();
         }});
   }

   /* Button that links to Page p */
   public Button (int x1, int y1, int h1, int w1, String txt, Page p) {
      x=x1;
      y=y1;
      h=h1;
      w=w1;
      setBounds(x,y,w,h);
      buttonImg = new JButton(txt);
      buttonImg.setBounds(x,y,w,h);
      setOpaque(false);
      buttonImg.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            p.reveal();
            clicked();
         }});
   }

   /* custom action */
   public Button (int x1, int y1, int h1, int w1, String txt, MouseAdapter func) {
      x=x1;
      y=y1;
      h=h1;
      w=w1;
      setBounds(x,y,w,h);
      buttonImg = new JButton(txt);
      buttonImg.setBounds(x,y,w,h);
      setOpaque(false);
      buttonImg.addMouseListener(func);
   }

   /*METHODS*/
   public void addToBackground(Background b){
      try{
         b.add(buttonImg, new Integer(5));
      }catch (Exception ex){
         ex.printStackTrace();
      }
   }

   public int getX(){
      return x;
   }

   public void remove(){
      x=-100;
      y=-100;
      buttonImg.setBounds(-100,-100, w, h);
   }

   public void setBounds(int xNew, int yNew){
      x= xNew;
      y= yNew;
      buttonImg.setBounds(x, y, w, h);
   }
   public JButton getImg(){
      return buttonImg;
   }

   private void clicked() {

   }
}
