import java.lang.Object;
import java.util.Random;
import java.awt.geom.Point2D;

public class App {
    public static void main(String[] args) throws Exception {
        Point2D.Double point_test = new Point2D.Double();
        Random rand = new Random();
        int dist;

        point_test.setLocation();
        dist = rand.nextInt(100);

        switch(rand.nextInt(3)){
            case 0:     Move.positive_x(point_test, dist);
                        break;
            case 1:     Move.negative_x(point_test, dist);
                        break;
            case 2:     Move.positive_y(point_test, dist);
                        break;
            case 3:     Move.negative_y(point_test, dist);
                        break;
            default:    break;
        }

        System.out.println(point_test.toString());
    }
}
