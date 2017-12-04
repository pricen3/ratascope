import javax.swing.*;
import java.lang.Integer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CAMPR {

   public static Boolean DEBUG = false;
   public static void main(String[] args) throws Exception{
      try{
         generateMainPage();
      }catch(Exception ex){
         System.out.println("*shrug*");
         ex.printStackTrace();
      }
   }

   /* Create all pages */
   public static void generateMainPage() throws Exception{
      Page mainPage = new Page("CAMPR", 900, 200);
      mainPage.addBackground("campr_logo.png");
      /* add buttons */
      mainPage.add(new Button(28, 30, 40, 150, "Experiments", generateExpPage()));
      mainPage.add(new Button(28, 80, 40, 150, "Cages", generateCagePage()));
      mainPage.add(new Button(28, 130, 40, 150, "Help"));
      mainPage.reveal();
   }
   public static Page generateExpPage() throws Exception{
      expPage ePage = expPage.getExpPage();
      ePage.addBackground("campr_logo.png");
      return ePage;
   }
   public static Page generateCagePage() throws Exception{
      cagePage cPage = cagePage.getCagePage();
      cPage.addBackground("campr_logo.png");
      return cPage;
   }
   /*public static Page generateCagePage() throws Exception{
      Page cagePage = new Page("Cages", 550, 900);
      cagePage.addBackground("campr_logo.png", 0, 0);
      Button newCage = new Button(28, 32, 40, 200, "Configure New Cage", new Page("New Cage", 900, 550));
      cagePage.add(newCage);
      return cagePage;
   }*/
}
