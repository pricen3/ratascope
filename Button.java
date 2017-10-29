import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Integer;


class Button extends JPanel{

   private int x, y, h, w;

   public Button (int x1, int y1, int h1, int w1) {
      x=x1;
      y=y1;
      h=h1;
      w=w1;
      setBounds(x,y,w,h);
      setOpaque(true);
      /*addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            clicked();
         }});*/
   }

   public void addToBackground(Background b){
      try{
         b.add(this, new Integer(5));
      }catch (Exception ex){
         ex.printStackTrace();
      }
   }

   /*public void addToBoardCard(int x1, int y1){
      try{
         BoardView board = BoardView.build();
         setBounds(x1+x,y1+y,w,h);
         board.add(this, new Integer(5));
      }catch (Exception ex){
         ex.printStackTrace();
      }
   }*/

   public void remove(){
     setBounds(-100,-100, w, h);
   }

   /*private void clicked() {
      GameManager gm = GameManager.getGM();
      String cmd[]={"work", role.getName()};
      gm.work(cmd);
   }*/
}
