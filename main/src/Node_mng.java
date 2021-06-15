import java.util.Random;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node_mng {
  private static final boolean DEBUG = false;

  static int init(Random rand, int node_leased, Node_info node, int init_x, int init_y, double dest_x, double dest_y){
    //Initialize Node. Set num, first location, move speed.
    node.num = node_leased;
    node.point.setLocation(init_x, init_y);
    node.destination.setLocation(dest_x, dest_y);
    node.goal_nearby = false;
    node.dynamic_fog = false;
    node.reached = false;
    node.move_speed = rand.nextInt(40)+10;
    return node.num + 1;
  }

  static int put(int node_leased, Random rand, int MAX_GOALS, Node_info node_array[], Point2D.Double goals_array[]){
    int goal;
    if(MAX_GOALS!=0){
      goal = rand.nextInt(MAX_GOALS);
      node_leased = init(rand, node_leased, node_array[node_leased], 1000, 1000, goals_array[goal].x, goals_array[goal].y);
    }
    else{
      node_leased = init(rand, node_leased, node_array[node_leased], 1000, 1000, 0, 0);
    }
    return node_leased;
  }

  static void check_reach_goal(Node_info node){
    if(node.goal_nearby == true){
      if((Math.abs(node.point.x - node.destination.x) <= node.move_speed) && (node.point.x != node.destination.x)){
          //if(DEBUG) System.out.println("node destination x is " + node.destination.x);
          node.point.setLocation(node.destination.x, node.point.y);
          //if(DEBUG) System.out.println("cur_point.x is changed to dest.x");
      }
      else if((Math.abs(node.point.y - node.destination.y) <= node.move_speed) && (node.point.y != node.destination.y)){
        //if(DEBUG) System.out.println("node destination y is " + node.destination.y);
        node.point.setLocation(node.point.x, node.destination.y);
        //if(DEBUG) System.out.println("cur_point.y is changed to dest.y");
      }
      if((node.point.x == node.destination.x) && (node.point.y == node.destination.y)){
        node.reached = true;
        System.out.println("Node num: " + node.num + " is reach the goal point.");
      }
    }
    else if(node.point.distance(node.destination)<=node.move_speed){
      node.goal_nearby = true;
    }
    else{
      node.goal_nearby = false;
    }
  }

  static void dynamic_fog_set(Random rand, ArrayList<Integer> dynamic_fog_list, ArrayList<Integer> node_active_list){
    int dynamic_fogs_required, dynamic_fog_candidate;
    boolean error;
    dynamic_fogs_required = node_active_list.size() * App.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    if(dynamic_fogs_required > dynamic_fog_list.size()){
      do{
        error = false;
        dynamic_fog_candidate = rand.nextInt(node_active_list.size());
        if(DEBUG) System.out.println("dynamic_fog_candidate: " + dynamic_fog_candidate);
        for(int i = 0; i < dynamic_fog_list.size(); i++){
          if(dynamic_fog_candidate == dynamic_fog_list.get(i)) error = true;
          if(error == true) break;
        }
        if(error == false) dynamic_fog_list.add(dynamic_fog_candidate);
      }while((dynamic_fogs_required - 1 >= dynamic_fog_list.size()));
    }

    if(DEBUG) dynamic_fog_print_status(dynamic_fog_list, node_active_list);
  }

  static void dynamic_fog_dead_judge(Node_info[] node_array, ArrayList<Integer> dynamic_fog_list, ArrayList<Integer> node_active_list){
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      if(node_array[dynamic_fog_list.get(i)].reached == true) dynamic_fog_list.remove(i);
    }
    if(DEBUG) dynamic_fog_print_status(dynamic_fog_list, node_active_list);
  }

  static void dynamic_fog_print_status(ArrayList<Integer> dynamic_fog_list, ArrayList<Integer> node_active_list){
    int dynamic_fogs_required = node_active_list.size() * App.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    System.out.print(dynamic_fog_list.size() + " Dynamic Fog Node(s) exist (Minimum DF: " + dynamic_fogs_required + "), Dynamic Fog Node:");
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      if(i != 0) System.out.print(", ");
      System.out.print(dynamic_fog_list.get(i));
    }
    System.out.println("");
  }
}