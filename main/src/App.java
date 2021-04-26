import java.util.Random;
import java.awt.geom.Point2D;

public class App {
    static int time_sec = 10;
    public static void main(String[] args) throws Exception {
        Point2D.Double point_test = new Point2D.Double();
        Random rand = new Random();

        point_test.setLocation(0, 0);

        int count = 0;
        while(count < time_sec){
            Move.random_walk(point_test, rand); 
            count += 1;
        }

  
    }
}
