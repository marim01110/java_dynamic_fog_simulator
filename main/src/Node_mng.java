import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Node_mng {
  private static final boolean DEBUG = Settings.DEBUG;
  protected static ArrayList<Integer> delete_node_temp_list = new ArrayList<>();

  void spawn(int node_leased){
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

  private void delete(Node_info node){
    if(DEBUG) System.out.println("Node " + node.num + " is now deleteing.");
    Environment.node_list.remove(node);
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

  void battery_drain(Node_info node, String communication_method, String recv_or_send){
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

  protected void battery_check(Node_info node){
    if(node.battery_remain_percentage <= Settings.BATTERY_LOW_THRESHOLD_PERCENTAGE){
      if(DEBUG) System.out.println("Node " + node.num + "'s battery turns to low state.");
      node.battery_low = true;
    }
    else node.battery_low = false;
  }

  void keep_alive() throws Exception {
    Node_info current_node;
    ExecutorService executor = Executors.newFixedThreadPool(4);
    var list = new ArrayList<Future<?>>();

    for(int i = 0; i < Environment.node_list.size(); i++){
      current_node = Environment.node_list.get(i);

      Future<?> future = executor.submit(new keep_alive_multi_threads(current_node));
      list.add(future);
    }
    executor.shutdown();

    for (Future<?> future : list) {
      future.get();
    }

    if(delete_node_temp_list.isEmpty() == false){
      for(int i = 0, size = delete_node_temp_list.size(); i < size; i++){
        Node_info delete_node;
  
        delete_node = get_node_info(delete_node_temp_list.get(i));
        if(delete_node == null){
          System.exit(-1);
        }
  
        if(delete_node.dynamic_fog == true){
          var fog_mng_class = new Fog_mng();
          fog_mng_class.unregister(Fog_mng.get_fog_info(delete_node.num));
        }
        delete(delete_node);
      }
    }

    delete_node_temp_list.clear();
  }


  void keep_alive_old(){
    Node_info current_node;
    Fog_info fog_info;

    for(int i = 0; i < Environment.node_list.size(); i++){
      current_node = Environment.node_list.get(i);
      /* Node Move Process */
      if(current_node.reached == false){
        var move_class = new Move();
        if(Environment.mode == 4) move_class.random_walk(current_node);
        if(Environment.mode == 5) move_class.start(current_node);
        if(DEBUG) System.out.println("Node "+ current_node.num + " (" + current_node.point.x + ", " + current_node.point.y + "), Waypoint is (" + current_node.destination.point.x + ", " + current_node.destination.point.y + ")");
      }

      /* Node reach detection */
      if(current_node.reached == true) {
        var fog_mng_class = new Fog_mng();
        if(current_node.dynamic_fog == true) fog_mng_class.unregister(Fog_mng.get_fog_info(current_node.num));
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
          var fog_mng_class = new Fog_mng();
          fog_info = Fog_mng.get_fog_info(current_node.num);
          fog_mng_class.unregister(fog_info);
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

class keep_alive_multi_threads implements Runnable{
  Node_info current_node;

  public keep_alive_multi_threads(Node_info current_node){
    this.current_node = current_node;
  }

  public void run(){
    boolean delete_node = false;

    /* Node Move Process */
    if(current_node.reached == false){
      var move_class = new Move();
      if(Environment.mode == 4) move_class.random_walk(current_node);
      if(Environment.mode == 5) move_class.start(current_node);
      if(Settings.DEBUG) System.out.println("Node "+ current_node.num + " (" + current_node.point.x + ", " + current_node.point.y + "), Waypoint is (" + current_node.destination.point.x + ", " + current_node.destination.point.y + ")");
    }

    /* Node reach detection */
    if(current_node.reached == true) delete_node = true;

    /* Share device's location data */
    /*
     * Incomplete, disabled. (2021/11/22 11.29 p.m.)
    battery_drain(current_node, "cellular", "send");
    */

    var node_mng_class = new Node_mng();
    node_mng_class.battery_check(current_node);
    if(current_node.battery_low == true){
      if(current_node.battery_remain_percentage <= 0){
        delete_node = true;
        Statistics.out_of_battery += 1;
      }
    }

    // Need to write code for node deletion.
    if(delete_node == true){
      Node_mng.delete_node_temp_list.add(current_node.num);
    }
  }
}