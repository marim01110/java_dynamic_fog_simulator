import java.util.Random;
import java.util.Scanner;
import java.awt.geom.Point2D;

public class Mode5 {
  private static final boolean DEBUG = true;

  static final int MAX_NODES = 1;
  static final int MAX_GOALS = 1;

  static void main(Random rand, Scanner scan){
    //List<Node_info> node_list = new ArrayList<>();
    Node_info[] node_array = new Node_info[MAX_NODES];
    Point2D.Double[] goals_array = new Point2D.Double[MAX_GOALS];
    int node_leased = 0;
    int time_count;

    init(scan, node_array, goals_array);

    //Put Nodes on the Map
    for(int i=0; i<MAX_NODES; i++){
      node_leased = Node_mng.put(node_leased, rand, MAX_GOALS, node_array, goals_array);
    }

    //Simuration Start
    time_count = 0;
    while(time_count < App.time_sec){
      for(int i=0; i<node_leased; i++){
        if(node_array[i].reached == 0){
          if(node_array[i].goal_nearby_flag == 0){
            Move.decide_direction(node_array[i]);
            Node_mng.check_reach_goal(node_array[i]);
          }
          else if(node_array[i].goal_nearby_flag == 1){
            Node_mng.check_reach_goal(node_array[i]);
          }
          if(DEBUG) System.out.println("node"+ node_array[i].num + " (" + node_array[i].point.x + ", " + node_array[i].point.y + ")");
        }
      }
      time_count += 1;
    }
  }

  static void init(Scanner scan, Node_info[] node_array, Point2D.Double[] goals_array){
    int error;//Input Value Error Flag
    
    //Initialize Array
    for(int i=0; i<node_array.length; i++) node_array[i] = new Node_info();
    for(int i=0; i<goals_array.length; i++){
      goals_array[i] = new Point2D.Double();
      error = 1;
      //Goal node Set
      do{
        System.out.print("Goal-" + i+1 + "'s X coordinate is ... [0-" + App.edge_dist + "] ");
        goals_array[i].x = scan.nextInt();
        System.out.print("Goal-" + i+1 + "'s Y coordinate is ... [0-" + App.edge_dist + "] ");
        goals_array[i].y = scan.nextInt();
        if(goals_array[i].x >= 0 && goals_array[i].x <= App.edge_dist && goals_array[i].y >= 0 && goals_array[i].y <= App.edge_dist) error = 0;
      }while(error!=0);
      System.out.println("Goal-" + i+1 + " is now set!");
    }
  }
}