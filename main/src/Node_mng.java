import java.util.Random;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node_mng {
  private static final boolean DEBUG = Settings.DEBUG;

  static Node_info spawn(int node_leased){
    Random rand = new Random();
    Point2D.Double start = new Point2D.Double();
    Point2D.Double destination = new Point2D.Double();
    int point_index, destination_index, data_refresh_time, move_speed, move_speed_index;
    double battery_remain_percentage;

    do{
      point_index = rand.nextInt(Settings.LANDMARKS);
      destination_index = rand.nextInt(Settings.LANDMARKS);
    }while(point_index == destination_index);
    move_speed_index = rand.nextInt(Settings.MOVE_SPEEDS);

    start.setLocation(Environment.return_landmark_point(point_index));
    destination.setLocation(Environment.return_landmark_point(destination_index));
    data_refresh_time = rand.nextInt(Settings.CONTENTS_REFLESH_TIME);
    move_speed = Environment.return_move_speed(move_speed_index);
    battery_remain_percentage = rand.nextDouble(Settings.BATTERY_INIT_MAX_PERCENTAGE - Settings.BATTERY_INIT_MIN_PERCENTAGE) + Settings.BATTERY_INIT_MIN_PERCENTAGE;

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

  static Node_info get_node_info(ArrayList<Node_info> node_list, int node_num){
    Node_info result = null;

    for(int i = 0; i < node_list.size(); i++){
      if(node_num == node_list.get(i).num){
        result = node_list.get(i);
        break;
      }
    }
    //If not found ...
    if(result == null){
      System.out.println("Requested node: " + node_num + " is Not Found.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }
    return result;
  }

  static void battery_drain(Node_info node, String communication_method, String recv_or_send){
    switch(communication_method){
      case "bluetooth": switch(recv_or_send){
                          case "recv":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_BT_RECV;
                                        Statistics.power_comsumption_total += Settings.BATTERY_COMSUMPTION_BT_RECV;
                                        break;
                          case "send":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_BT_SEND;
                                        Statistics.power_comsumption_total += Settings.BATTERY_COMSUMPTION_BT_SEND;
                                        break;
                          default:      System.out.println("Not enough arguments given. Simulation aborted.");
                                        System.exit(-1);
                                        break;
                        }
                        break;
      case "cellular":  switch(recv_or_send){
                          case "recv":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_CELL_RECV;
                                        Statistics.power_comsumption_total += Settings.BATTERY_COMSUMPTION_CELL_RECV;
                                        break;
                          case "send":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_CELL_SEND;
                                        Statistics.power_comsumption_total += Settings.BATTERY_COMSUMPTION_CELL_SEND;
                                        break;
                          default:      System.out.println("Not enough arguments given. Simulation aborted.");
                                        System.exit(-1);
                                        break;
                        }
                        break;
      default:          System.out.println("Not enough arguments given. Simulation aborted.");
                        System.exit(-1);
                        break;
    }
  }
}