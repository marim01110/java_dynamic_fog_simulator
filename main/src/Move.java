import java.awt.geom.Point2D;
import java.util.Random;

public class Move {
  static void negative_x(Point2D.Double point, int x){
    point.setLocation(point.x-x, point.y);
    //System.out.println("Done.");
  }
  static void positive_x(Point2D.Double point, int x){
    point.setLocation(point.x+x, point.y);
    //System.out.println("Done.");
  }
  static void positive_y(Point2D.Double point, int y){
    point.setLocation(point.x, point.y+y);
    //System.out.println("Done.");
  }
  static void negative_y(Point2D.Double point, int y){
    point.setLocation(point.x, point.y-y);
    //System.out.println("Done.");
  }
  static void random_walk(Point2D.Double point, Random rand){
      int dist = rand.nextInt(100);

      switch(rand.nextInt(4)){
          case 0:     Move.positive_x(point, dist);
                      break;
          case 1:     Move.negative_x(point, dist);
                      break;
          case 2:     Move.positive_y(point, dist);
                      break;
          case 3:     Move.negative_y(point, dist);
                      break;
          default:    break;
      }
      System.out.println("point (" + point.x + ", " + point.y + ")");
  }
}
