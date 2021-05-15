import java.util.Random;
import java.util.Scanner;
import java.awt.geom.Point2D;

public class Mode5 {
  private static final boolean DEBUG = true;
  
  static void main(Random rand, Scanner scan){
    int MAX_NODES = 1;
    int MAX_GOALS = 1;
    //List<Node_info> node_list = new ArrayList<>();
    Node_info[] node_array = new Node_info[MAX_NODES];
    Point2D.Double[] goals_array = new Point2D.Double[MAX_GOALS];
    int node_leased = 0;
    //Initialized Array
    for(int i=0; i<node_array.length; i++) node_array[i] = new Node_info();
    for(int i=0; i<goals_array.length; i++){
      goals_array[i] = new Point2D.Double();
      int error = 1;//Input Value Error Flag
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

    //Put Nodes on the Map
    for(int i=0; i<MAX_NODES; i++){
      node_leased = Node_mng.put(node_leased, rand, MAX_GOALS, node_array, goals_array);
    }

    //Simuration Start
    int count = 0;
    while(count < App.time_sec){
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
      count += 1;
    }
  }
}