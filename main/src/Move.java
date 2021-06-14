import java.awt.geom.Point2D;
import java.util.Random;

public class Move {
  private static final boolean DEBUG = false;

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

  static void move(Node_info node, int direc){
    int dist = node.move_speed;
    switch(direc){
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
  }

  static void random_walk(Node_info node, Random rand){
    Move.move(node, rand.nextInt(4));
    if(area_judge(node.point)==false){
      if(DEBUG) System.out.println("Rerunning random_walk process ...");
      random_walk(node, rand);
    }
  }

  static void decide_direction(Node_info node){
    double diff_x, diff_y;
    diff_x = node.destination.x - node.point.x;
    diff_y = node.destination.y - node.point.y;
    if(Math.abs(diff_x)<=Math.abs(diff_y)){
      if(diff_y>=0) Move.move(node, 2);//positive y
      else Move.move(node, 3);//negative y
    }
    else{
      if(diff_x>=0) Move.move(node, 0);//positive x
      else Move.move(node, 1);//negative x
    }
  }

  static boolean area_judge(Point2D.Double point){
    int error = 0;
    if(point.x < 0) error += 1;
    if(point.x > App.EDGE_DIST) error += 10;
    if(point.y < 0) error += 100;
    if(point.y > App.EDGE_DIST) error += 1000;
    if (error != 0) return false;//Means Node went outside the area.
    else return true;//Means Node still inside the area.
  }
}
