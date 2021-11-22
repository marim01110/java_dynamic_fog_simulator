import java.util.Random;
import java.awt.geom.Point2D;

public class Node_mng {
  private static final boolean DEBUG = Settings.DEBUG;

  static void spawn(int node_leased){
    Random rand = new Random();
    Point2D.Double start = new Point2D.Double();
    Point2D.Double destination = new Point2D.Double();
    boolean stay;
    int point_index, destination_index, stay_time, data_refresh_time, move_speed, move_speed_index;
    double battery_remain_percentage;

    do{
      point_index = rand.nextInt(Settings.LANDMARKS);
      destination_index = rand.nextInt(Settings.LANDMARKS);
    }while(point_index == destination_index);
    move_speed_index = rand.nextInt(Settings.MOVE_SPEEDS);

    start.setLocation(Environment.return_landmark_point(point_index));
    destination.setLocation(Environment.return_landmark_point(destination_index));
    stay = rand.nextBoolean();
    stay_time = 0;
    if(stay == true) stay_time = Settings.STAY_MIN_TIME * 60 + rand.nextInt(Settings.STAY_MAX_TIME - Settings.STAY_MIN_TIME) * 60;
    data_refresh_time = rand.nextInt(Settings.CONTENTS_RETRIEVE_FREQUENCY);
    move_speed = Environment.return_move_speed(move_speed_index);
    battery_remain_percentage = rand.nextDouble(Settings.BATTERY_INIT_MAX_PERCENTAGE - Settings.BATTERY_INIT_MIN_PERCENTAGE) + Settings.BATTERY_INIT_MIN_PERCENTAGE;

    var newnode = new Node_info(node_leased, start, destination, stay, stay_time, data_refresh_time, false, false, false, move_speed, battery_remain_percentage, false);
    if(DEBUG) System.out.println("Node " + newnode.num + " Created. Start from " + newnode.point + ", Goal is " + newnode.destination);
    Environment.node_list.add(newnode);
  }

  private static void delete(Node_info node){
    if(DEBUG) System.out.println("Node " + node.num + " is now deleteing.");
    Environment.node_list.remove(node);
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

  static Node_info get_node_info(int node_num){
    Node_info result = null;

    for(int i = 0; i < Environment.node_list.size(); i++){
      if(node_num == Environment.node_list.get(i).num){
        result = Environment.node_list.get(i);
        break;
      }
    }
    //If not found ...
    if(result == null){
      System.out.println("Requested node: " + node_num + " is Not Found.");
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

  private static void battery_check(Node_info node){
    if(node.battery_remain_percentage <= Settings.BATTERY_LOW_THRESHOLD_PERCENTAGE){
      if(DEBUG) System.out.println("Node " + node.num + "'s battery turns to low state.");
      node.battery_low = true;
    }
    else node.battery_low = false;
  }

  static void keep_alive(){
    Node_info current_node;
    Fog_info fog_info;

    for(int i = 0; i < Environment.node_list.size(); i++){
      current_node = Environment.node_list.get(i);
      /* Node Move Process */
      if(current_node.reached == false){
        if(Environment.mode == 4) Move.random_walk(current_node);
        if(Environment.mode == 5) Move.start(current_node);
        if(DEBUG) System.out.println("Node "+ current_node.num + " (" + current_node.point.x + ", " + current_node.point.y + ")");
      }

      /* Node reach detection */
      if(current_node.reached == true) {
        if(current_node.dynamic_fog == true) Fog_mng.unregister(Fog_mng.get_fog_info(current_node.num));
        delete(current_node);
        i -= 1;
        continue;
      }

      /* Share device's location data */
      /*
       * Incomplete, disabled. (2021/11/22 11.29 p.m.)
      battery_drain(current_node, "cellular", "send");
      */

      battery_check(current_node);
      if(current_node.battery_low == true){
        if(current_node.dynamic_fog == true){
          fog_info = Fog_mng.get_fog_info(current_node.num);
          Fog_mng.unregister(fog_info);
        }
        if(current_node.battery_remain_percentage <= 0){
          delete(current_node);
          i -= 1;
          continue;
        }
      }
    }
  }
}