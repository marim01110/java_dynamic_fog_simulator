import java.util.Random;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node_mng {
  private static final boolean DEBUG = Environment.DEBUG;
  private static final int INIT = -1;

  static Node_info spawn(int node_leased){
    Random rand = new Random();
    Point2D.Double start = new Point2D.Double();
    Point2D.Double destination = new Point2D.Double();
    int point_index, destination_index, data_refresh_time, move_speed, move_speed_index;
    double battery_remain_percentage;

    do{
      point_index = rand.nextInt(Environment.LANDMARKS);
      destination_index = rand.nextInt(Environment.LANDMARKS);
    }while(point_index == destination_index);
    move_speed_index = rand.nextInt(Environment.MOVE_SPEEDS);

    start.setLocation(Environment.return_landmark_point(point_index));
    destination.setLocation(Environment.return_landmark_point(destination_index));
    data_refresh_time = rand.nextInt(5) + 1;
    move_speed = Environment.return_move_speed(move_speed_index);
    battery_remain_percentage = rand.nextDouble(Environment.BATTERY_INIT_MAX_PERCENTAGE - Environment.BATTERY_INIT_MIN_PERCENTAGE) + Environment.BATTERY_INIT_MIN_PERCENTAGE;

    var newnode = new Node_info(node_leased, start, destination, data_refresh_time, false, false, false, move_speed, battery_remain_percentage);
    if(DEBUG) System.out.println("Node " + newnode.num + " Created. Start from " + newnode.point + ", Goal is " + newnode.destination);
    return newnode;
  }

  static void check_reach_goal(Node_info node){
    if(node.goal_nearby == true){
      if((Math.abs(node.point.x - node.destination.x) <= node.move_speed) && (node.point.x != node.destination.x)){
          node.point.setLocation(node.destination.x, node.point.y);
      }
      else if((Math.abs(node.point.y - node.destination.y) <= node.move_speed) && (node.point.y != node.destination.y)){
        node.point.setLocation(node.point.x, node.destination.y);
      }
      if((node.point.x == node.destination.x) && (node.point.y == node.destination.y)){
        node.reached = true;
        if(DEBUG) System.out.println("Node num: " + node.num + " have reached the goal point.");
      }
    }
    else if(node.point.distance(node.destination)<=node.move_speed){
      node.goal_nearby = true;
    }
    else{
      node.goal_nearby = false;
    }
  }

  static int get_index_num(ArrayList<Node_info> node_list, int node_num){
    int result = INIT;

    for(int i = 0; i < node_list.size(); i++){
      if(node_num == node_list.get(i).num){
        result = i;
        break;
      }
    }
    if(result == INIT){
      System.out.println("Requested node: " + node_num + " is Not Found.");
    }
    return result;
  }
}