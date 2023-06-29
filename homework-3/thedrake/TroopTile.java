package thedrake;

 public class TroopTile implements Tile{
     private final TroopFace face;
     private final Troop troop;
     private final PlayingSide side;
     public TroopTile(Troop troop, PlayingSide side, TroopFace face){
         this.face = face;
         this.side = side;
         this.troop = troop;
     }

     public PlayingSide side(){
         return this.side;
     }

     public TroopFace face(){
        return this.face;
     }
     public Troop troop(){
         return this.troop;
     }
     public boolean canStepOn(){
         return false;
     }
     public boolean hasTroop() {
         return true;
     }
     public TroopTile flipped(){
         if(this.face == TroopFace.AVERS){
             return new TroopTile(this.troop, this.side, TroopFace.REVERS);
         }else{
             return new TroopTile(this.troop, this.side, TroopFace.AVERS);
         }
     }
 }
