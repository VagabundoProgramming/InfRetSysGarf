package Config;

// NO se pa q usar esto

public class Variables {
    int DebugLevel = 0; // 0: no comments, 1: general action comments, 2: detailed comments


    public void setDebugLevel(int newDebugLevel){
        this.DebugLevel = newDebugLevel;
    }
    public int getDebugLevel(){
        return this.DebugLevel;
    }
}
