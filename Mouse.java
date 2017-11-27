import java.util.*;

public class Mouse{
   private String name;
   private int ID;
   private String notes;
   
   public Mouse(String mName, int id, String mouseNotes){
      name = mName;
      ID = id;
      notes = mouseNotes;
   }
   
   public String getName(){
      return name;
   }

   public int getID(){
      return ID;
   }
   
   public String getNotes(){
      return notes;
   }
}
