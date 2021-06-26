import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class Move {
  private static final boolean DEBUG = App.DEBUG;

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
    int candidate, area_judge_data;
    var direction_list = new ArrayList<Integer>();

    //Scan the surroundings
    area_judge_data = area_judge(node);
    if(area_judge_data / 1000 != 1) direction_list.add(2);
    else area_judge_data -= 1000;
    if(area_judge_data / 100 != 1) direction_list.add(3);
    else area_judge_data -= 100;
    if(area_judge_data / 10 != 1) direction_list.add(0);
    else area_judge_data -= 10;
    if(area_judge_data / 1 != 1) direction_list.add(1);
    else area_judge_data -= 1;

    candidate = direction_list.get(rand.nextInt(direction_list.size()));
    Move.move(node, candidate);
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

  static int area_judge(Node_info node){
    int error = 0;
    if(node.point.x - node.move_speed < 0) error += 1;
    if(node.point.x + node.move_speed > App.EDGE_DIST) error += 10;
    if(node.point.y - node.move_speed < 0) error += 100;
    if(node.point.y + node.move_speed > App.EDGE_DIST) error += 1000;
    return error;
  }
}
