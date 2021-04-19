import java.awt.geom.Point2D;

public class Move {
  static void negative_x(Point2D.Double point, int x){
    point.setLocation(point.x-x, point.y);
    System.out.println("Done.");
  }
  static void positive_x(Point2D.Double point, int x){
    point.setLocation(point.x+x, point.y);
    System.out.println("Done.");
  }
  static void positive_y(Point2D.Double point, int y){
    point.setLocation(point.x, point.y+y);
    System.out.println("Done.");
  }
  static void negative_y(Point2D.Double point, int y){
    point.setLocation(point.x, point.y-y);
    System.out.println("Done.");
  }
}
