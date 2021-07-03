import java.util.Random;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node_mng {
  private static final boolean DEBUG = App.DEBUG;

  static Node_info init(ArrayList<Node_info> node_list, int node_leased, int init_x, int init_y, double dest_x, double dest_y){
    //Initialize Node. Set num, first location, move speed.
    Random rand = new Random();
    var point = new Point2D.Double();
    var destination = new Point2D.Double();

    point.setLocation(init_x, init_y);
    destination.setLocation(dest_x, dest_y);
    var new_node = new Node_info(node_leased, point, destination, false, false, false, rand.nextInt(40) + 10);
    return new_node;
  }

  static Node_info put(ArrayList<Node_info> node_list, int node_leased, int MAX_GOALS, Point2D.Double goals_array[]){
    Random rand = new Random();
    int goal;
    goal = rand.nextInt(MAX_GOALS);
    return init(node_list, node_leased, 1000, 1000, goals_array[goal].x, goals_array[goal].y);
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
        System.out.println("Node num: " + node.num + " have reached the goal point.");
      }
    }
    else if(node.point.distance(node.destination)<=node.move_speed){
      node.goal_nearby = true;
    }
    else{
      node.goal_nearby = false;
    }
  }

  static void dynamic_fog_set(ArrayList<Node_info> node_list, int node_leased, ArrayList<Storage> dynamic_fog_list){
    Random rand = new Random();
    int dynamic_fogs_required, dynamic_fog_candidate;
    boolean error;
    
    dynamic_fogs_required = node_list.size() * App.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    if(dynamic_fogs_required > dynamic_fog_list.size()){
      while(dynamic_fogs_required > dynamic_fog_list.size()){
        error = false;
        dynamic_fog_candidate = rand.nextInt(node_leased);
        if(DEBUG) System.out.println("dynamic_fog_candidate: " + dynamic_fog_candidate);
        for(int i = 0; i < dynamic_fog_list.size(); i++){
          if(dynamic_fog_candidate == dynamic_fog_list.get(i).node_num) error = true;
          if(error == true) break;
        }
        if(error == false){
          for(int i = 0; i < node_list.size(); i++){
            if(error == true){
              if(node_list.get(i).num == dynamic_fog_candidate) error = false;
            }
          }
        }

        if(error == true){
          if(DEBUG) System.out.println("The Candidate is Dupulicated.");
        }
        else if(error == false){//"error == false" means the candidate not dupulicated.
          var temp = new Storage(dynamic_fog_candidate, 0);
          dynamic_fog_list.add(temp);
          if(DEBUG) System.out.println("Node " + dynamic_fog_candidate + " becomes Dynamic_Fog node.");
        }
        if(DEBUG) System.out.println("Required: " + dynamic_fogs_required + ", Exist: " + dynamic_fog_list.size());
      }
    }
  }

  static void dynamic_fog_dead_judge(ArrayList<Node_info> node_list, int node_list_index, ArrayList<Storage> dynamic_fog_list){
    for(int j = 0; j < dynamic_fog_list.size(); j++){
      if(dynamic_fog_list.get(j).node_num == node_list.get(node_list_index).num){
        if(DEBUG) System.out.println("Dynamic_Fog Node " + dynamic_fog_list.get(j).node_num + " is now deleting.");
        dynamic_fog_list.remove(j);
      }
    }
  }

  static void dynamic_fog_print_status(ArrayList<Node_info> node_list, ArrayList<Storage> dynamic_fog_list){//For DEBUG
    int dynamic_fogs_required = node_list.size() * App.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    System.out.print(dynamic_fog_list.size() + " Dynamic Fog Node(s) exist (Minimum DF: " + dynamic_fogs_required + "), Dynamic Fog Node:");
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      if(i != 0) System.out.print(", ");
      System.out.print(dynamic_fog_list.get(i).node_num);
    }
    System.out.println("");
  }
}