import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

public class Background extends JLayeredPane {
   private JLabel back;

   public Background(String img) throws Exception {
      back = new JLabel();
      Class cls = getClass();
      ImageIcon icon = new ImageIcon(ImageIO.read(cls.getResourceAsStream(img)));
      back.setIcon(icon);
      add(back,new Integer(0));
      back.setBounds(0,0, icon.getIconWidth(), icon.getIconHeight());
   }

   public Background(String img, int x, int y) throws Exception {
      /* x and y are the coordinates where the background should appear */
      back = new JLabel();
      Class cls = getClass();
      ImageIcon icon = new ImageIcon(ImageIO.read(cls.getResourceAsStream(img)));
      back.setIcon(icon);
      add(back,new Integer(0));
      back.setBounds(x,y, icon.getIconWidth(), icon.getIconHeight());
   }
	public Background(){
      back = new JLabel();
   }

   public void resetBack(){
      moveToBack(back);
   }
}
