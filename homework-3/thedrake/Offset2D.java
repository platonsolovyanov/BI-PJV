package thedrake;
public class Offset2D {
    public final int x, y;
    public Offset2D(int x, int y){
        this.x = x;
        this.y = y;
    }
    public boolean equalsTo(int x, int y){
        if(this.x == x && this.y == y){
            return true;
        }else{
            return false;
        }
    }

    public Offset2D yFlipped(){
        return new Offset2D(x,-y);
    }


}
