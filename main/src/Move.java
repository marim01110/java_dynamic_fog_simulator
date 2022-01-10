import java.awt.geom.Point2D;
import java.util.Random;

public class Move {
  private static final boolean DEBUG = Settings.DEBUG;

  void start(Node_info node){
    if(node.goal_nearby == false) decide_direction(node);
    check_reach_goal(node);
  }

  private void negative_x(Point2D.Double point, int x){
    point.setLocation(point.x-x, point.y);
  }
  private void positive_x(Point2D.Double point, int x){
    point.setLocation(point.x+x, point.y);
  }
  private void positive_y(Point2D.Double point, int y){
    point.setLocation(point.x, point.y+y);
  }
  private void negative_y(Point2D.Double point, int y){
    point.setLocation(point.x, point.y-y);
  }

  private void move(Node_info node, int direc){
    int dist = node.move_speed;
    switch(direc){
      case 0:     positive_x(node.point, dist);
                  Statistics.moves += 1;
                  break;
      case 1:     negative_x(node.point, dist);
                  Statistics.moves += 1;
                  break;
      case 2:     positive_y(node.point, dist);
                  Statistics.moves += 1;
                  break;
      case 3:     negative_y(node.point, dist);
                  Statistics.moves += 1;
                  break;
      default:    break;
    }
  }

  void random_walk(Node_info node){
    Random rand = new Random();
    int direction, area_judge_data;

    direction = rand.nextInt(4);
    move(node, direction);
    
    area_judge_data = area_judge(node);
    if(area_judge_data / 1000 == 1){
      negative_y(node.point, Settings.EDGE_DIST);
      area_judge_data -= 1000;
      Statistics.border_acrossed += 1;
    }
    if(area_judge_data / 100 == 1){
      negative_x(node.point, Settings.EDGE_DIST);
      area_judge_data -= 100;
      Statistics.border_acrossed += 1;
    }
    if(area_judge_data / 10 == 1){
      positive_y(node.point, Settings.EDGE_DIST);
      area_judge_data -= 10;
      Statistics.border_acrossed += 1;
    }
    if(area_judge_data / 1 == 1){
      positive_x(node.point, Settings.EDGE_DIST);
      area_judge_data -= 1;
      Statistics.border_acrossed += 1;
    }
  }

  private void decide_direction(Node_info node){
    double diff_x, diff_y;
    diff_x = node.destination.point.x - node.point.x;
    diff_y = node.destination.point.y - node.point.y;
    if(Math.abs(diff_x)<=Math.abs(diff_y)){
      if(diff_y>=0) move(node, 2);//positive y
      else move(node, 3);//negative y
    }
    else{
      if(diff_x>=0) move(node, 0);//positive x
      else move(node, 1);//negative x
    }
  }

  private void check_reach_goal(Node_info node){
    if(node.goal_nearby == true){
      if((Math.abs(node.point.x - node.destination.point.x) <= node.move_speed) && (node.point.x != node.destination.point.x)){
          node.point.setLocation(node.destination.point.x, node.point.y);
      }
      else if((Math.abs(node.point.y - node.destination.point.y) <= node.move_speed) && (node.point.y != node.destination.point.y)){
        node.point.setLocation(node.point.x, node.destination.point.y);
      }
      if((node.point.x == node.destination.point.x) && (node.point.y == node.destination.point.y)){
        if(node.waypoint_list.isEmpty()){
          node.reached = true;
          if(DEBUG) System.out.println("Node num: " + node.num + " have reached the goal point (" + node.destination.name + ").");  
        }
        else{
          if(DEBUG) System.out.println("Node num: " + node.num + " have reached waypoint (" + node.destination.name + ").");
          load_next_waypoint(node);
        }
      }
    }
    else if(node.point.distance(node.destination.point)<=node.move_speed){
      node.goal_nearby = true;
    }
    else{
      node.goal_nearby = false;
    }
  }

  private void load_next_waypoint(Node_info node){
    Landmark next_destination;

    /* Change start point */
    node.start = node.destination;

    /* Set next waypoint */
    next_destination = node.waypoint_list.get(0);
    node.waypoint_list.remove(0);
    node.destination = next_destination;
    if(DEBUG) System.out.println("Node " + node.num + " restart from " + node.start.name + ", Next waypoint is " + node.destination.name);

    /* Reset flags */
    node.goal_nearby = false;
    node.reached = false;
  }

  private int area_judge(Node_info node){
    int error = 0;
    if(node.point.x < 0) error += 1;
    if(node.point.y < 0) error += 10;
    if(node.point.x > Settings.EDGE_DIST) error += 100;
    if(node.point.y > Settings.EDGE_DIST) error += 1000;
    return error;
  }
}
