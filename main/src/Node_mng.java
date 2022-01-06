import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class Node_mng {
  private static final boolean DEBUG = Settings.DEBUG;

  static void spawn(int node_leased){
    Random rand = new Random();
    var position = new Point2D.Double();
    Landmark start, destination;
    var waypoint_list = new ArrayList<Landmark>();
    var waypoint_temp_list = new ArrayList<Integer>();
    int waypoints, waypoint_candidate_index, data_refresh_time, move_speed, move_speed_index;
    double battery_remain_percentage;

    /* Set start position (Start from JR Kyoto Sta.) */
    start = Environment.return_landmark_point(0);

    /* Set Waypoints */
    for(int i = 1; i < Settings.LANDMARKS; i++) waypoint_temp_list.add(i);
    waypoints = rand.nextInt(Settings.LANDMARKS - Settings.WAYPOINT_MIN - 1) + Settings.WAYPOINT_MIN;
    for(int i = 0; i < waypoints; i++){
      waypoint_candidate_index = rand.nextInt(waypoint_temp_list.size());
      waypoint_list.add(Environment.return_landmark_point(waypoint_temp_list.get(waypoint_candidate_index)));
      waypoint_temp_list.remove(waypoint_candidate_index);
    }
    waypoint_list.add(start);// At the end, they come back to the starting point.

    /* Load first waypoint */
    destination = waypoint_list.get(0);
    waypoint_list.remove(0);

    /* Set current position */
    position.setLocation(start.point);

    move_speed_index = rand.nextInt(Settings.MOVE_SPEEDS);

    data_refresh_time = rand.nextInt(Settings.CONTENTS_RETRIEVE_FREQUENCY);
    move_speed = Environment.return_move_speed(move_speed_index);
    battery_remain_percentage = rand.nextDouble(Settings.BATTERY_INIT_MAX_PERCENTAGE - Settings.BATTERY_INIT_MIN_PERCENTAGE) + Settings.BATTERY_INIT_MIN_PERCENTAGE;

    var newnode = new Node_info(node_leased, position, start, destination, waypoint_list, data_refresh_time, false, false, false, move_speed, battery_remain_percentage, false);
    if(DEBUG) System.out.println("Node " + newnode.num + " Created. Start from " + newnode.start.name + ", Next waypoint is " + newnode.destination.name);
    Environment.node_list.add(newnode);
  }

  private static void delete(Node_info node){
    if(DEBUG) System.out.println("Node " + node.num + " is now deleteing.");
    Environment.node_list.remove(node);
  }

  static void check_reach_goal(Node_info node){
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

  private static void load_next_waypoint(Node_info node){
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

  static Node_info get_node_info(int node_num){
    Node_info result = null;

    for(int i = 0, size = Environment.node_list.size(); i < size; i++){
      if(node_num == Environment.node_list.get(i).num){
        result = Environment.node_list.get(i);
        break;
      }
    }
    /* If not found ... */
    if(result == null){
      System.out.println("Requested node: " + node_num + " is Not Found.");
    }
    return result;
  }

  static void battery_drain(Node_info node, String communication_method, String recv_or_send){
    switch(communication_method){
      case "bluetooth": switch(recv_or_send){
                          case "recv":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_BT_RECV;
                                        Statistics.power_consumption_total += Settings.BATTERY_COMSUMPTION_BT_RECV;
                                        break;
                          case "send":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_BT_SEND;
                                        Statistics.power_consumption_total += Settings.BATTERY_COMSUMPTION_BT_SEND;
                                        break;
                          default:      System.out.println("Not enough arguments given. Simulation aborted.");
                                        System.exit(-1);
                                        break;
                        }
                        break;
      case "wifi":  switch(recv_or_send){
                          case "recv":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_WIFI_RECV;
                                        Statistics.power_consumption_total += Settings.BATTERY_COMSUMPTION_WIFI_RECV;
                                        break;
                          case "send":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_WIFI_SEND;
                                        Statistics.power_consumption_total += Settings.BATTERY_COMSUMPTION_WIFI_SEND;
                                        break;
                          default:      System.out.println("Not enough arguments given. Simulation aborted.");
                                        System.exit(-1);
                                        break;
                        }
                        break;
      case "cellular":  switch(recv_or_send){
                          case "recv":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_CELL_RECV;
                                        Statistics.power_consumption_total += Settings.BATTERY_COMSUMPTION_CELL_RECV;
                                        break;
                          case "send":  node.battery_remain_percentage -= Settings.BATTERY_COMSUMPTION_CELL_SEND;
                                        Statistics.power_consumption_total += Settings.BATTERY_COMSUMPTION_CELL_SEND;
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
        if(DEBUG) System.out.println("Node "+ current_node.num + " (" + current_node.point.x + ", " + current_node.point.y + "), Waypoint is (" + current_node.destination.point.x + ", " + current_node.destination.point.y + ")");
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
          Statistics.out_of_battery += 1;
          i -= 1;
          continue;
        }
      }
      current_node = null;
    }
  }
}