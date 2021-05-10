import java.awt.geom.Point2D;
import java.util.Random;

public class Move {
  static void negative_x(Point2D.Double point, int x){
    point.setLocation(point.x-x, point.y);
  }
  static void positive_x(Point2D.Double point, int x){
    point.setLocation(point.x+x, point.y);
  }
  static void positive_y(Point2D.Double point, int y){
    point.setLocation(point.x, point.y+y);
  }
  static void negative_y(Point2D.Double point, int y){
    point.setLocation(point.x, point.y-y);
  }

  static void random_walk(Node_info node, Random rand){
      int dist = node.move_speed;
      switch(rand.nextInt(4)){
          case 0:     Move.positive_x(node.point, dist);
                      break;
          case 1:     Move.negative_x(node.point, dist);
                      break;
          case 2:     Move.positive_y(node.point, dist);
                      break;
          case 3:     Move.negative_y(node.point, dist);
                      break;
          default:    break;
      }
      if(area_judge(node.point)!=0) random_walk(node, rand);

      System.out.println("node"+ node.num + " (" + node.point.x + ", " + node.point.y + ")");
  }

  static int decide_direction(Node_Info node){
      double diff_x, diff_y;
      diff_x = node.destination.x - node.point.x;
      diff_y = node.destination.y - node.point.y;
      if(Math.abs(diff_x)<=Math.abs(diff_y)){
        if()
      }
      return 0;
  }

  static int area_judge(Point2D.Double point){
    int error = 0;
    if(point.x < 0) error += 1;
    if(point.x > App.edge_dist) error += 10;
    if(point.y < 0) error += 100;
    if(point.y > App.edge_dist) error += 1000;
    if (error != 0) return 1;
    else return 0;
  }
}
