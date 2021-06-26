import java.util.Random;
import java.util.Scanner;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Mode5 {
  private static final boolean DEBUG = App.DEBUG;

  private static final int MAX_NODES = 5;
  private static final int MAX_GOALS = 1;

  static void main(Random rand, Scanner scan){
    var node_list = new ArrayList<Node_info>();
    Point2D.Double[] goals_array = new Point2D.Double[MAX_GOALS];
    var dynamic_fog_list = new ArrayList<Storage>();
    //var cache_data_list = new ArrayList<Data>();
    int node_leased = 0;
    int time_count;

    init(scan, goals_array);

    //Put Nodes on the Map
    for(int i = 0; i < MAX_NODES; i++){
      node_list.add(Node_mng.put(rand, node_list, node_leased, MAX_GOALS, goals_array));
      node_leased += 1;
    }

    //Simuration Start
    time_count = 0;
    while(time_count < App.TIME){
      if((time_count % App.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Node_mng.dynamic_fog_set(rand, node_list, node_leased, dynamic_fog_list);

      for(int i = 0; i < node_list.size(); i++){
        if(node_list.get(i).reached == false){
          if(node_list.get(i).goal_nearby == false){
            Move.decide_direction(node_list.get(i));
            Node_mng.check_reach_goal(node_list.get(i));
          }
          else if(node_list.get(i).goal_nearby == true){
            Node_mng.check_reach_goal(node_list.get(i));
          }
          if(DEBUG) System.out.println("Node "+ node_list.get(i).num + " (" + node_list.get(i).point.x + ", " + node_list.get(i).point.y + ")");
        }

        if(node_list.get(i).reached == true) {
          Node_mng.dynamic_fog_dead_judge(node_list, i, dynamic_fog_list);
          if(DEBUG) System.out.println("Node " + node_list.get(i).num + " is now deleteing.");
          node_list.remove(i);
          i -= 1;
        }
      }

      if(DEBUG){
        System.out.println("");
        Node_mng.dynamic_fog_print_status(node_list, dynamic_fog_list);
      }
      time_count += 1;
    }
  }

  static void init(Scanner scan, Point2D.Double[] goals_array){
    boolean error;//Input Value Error Flag
    
    //Initialize Array
    for(int i=0; i<goals_array.length; i++){
      goals_array[i] = new Point2D.Double();
      error = true;
      //Goal node Set
      do{
        System.out.print("Goal-" + i+1 + "'s X coordinate is ... [0-" + App.EDGE_DIST + "] ");
        goals_array[i].x = scan.nextInt();
        System.out.print("Goal-" + i+1 + "'s Y coordinate is ... [0-" + App.EDGE_DIST + "] ");
        goals_array[i].y = scan.nextInt();
        if(goals_array[i].x >= 0 && goals_array[i].x <= App.EDGE_DIST && goals_array[i].y >= 0 && goals_array[i].y <= App.EDGE_DIST) error = false;
      }while(error);
      System.out.println("Goal-" + i+1 + " is now set!");
    }
  }
}