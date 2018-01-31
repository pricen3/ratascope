class MouseCage{

   /* FIELDS */

   private Cage c;
   private String mouseID;

   /* CONSTRUCTORS */

   public MouseCage(Cage inputCage, String inputID){
      c = inputCage;
      mouseID = inputID;
   }

   /* METHODS */

   public Cage getCage(){
      return c;
   }
   public String getMouse(){
      return mouseID;
   }
}
