import java.util.Random;
import java.awt.geom.Point2D;

public class Node_mng {
  private static final boolean DEBUG = false;

  static int init(Random rand, int node_leased, Node_info node, int init_x, int init_y, double dest_x, double dest_y){
    //Initialize Node. Set num, first location, move speed.
    node.num = node_leased + 1;
    node.point.setLocation(init_x, init_y);
    node.destination.setLocation(dest_x, dest_y);
    node.goal_nearby_flag = 0;
    node.reached = 0;
    node.move_speed = rand.nextInt(40)+10;
    return node.num;
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
    if(node.goal_nearby_flag == 1){
      if((Math.abs(node.point.x - node.destination.x) <= node.move_speed) && (node.point.x != node.destination.x)){
          if(DEBUG) System.out.println("node destination x is " + node.destination.x);
          node.point.setLocation(node.destination.x, node.point.y);
          if(DEBUG) System.out.println("cur_point.x is changed to dest.x");
      }
      else if((Math.abs(node.point.y - node.destination.y) <= node.move_speed) && (node.point.y != node.destination.y)){
        if(DEBUG) System.out.println("node destination y is " + node.destination.y);
        node.point.setLocation(node.point.x, node.destination.y);
        if(DEBUG) System.out.println("cur_point.y is changed to dest.y");
      }
      if((node.point.x == node.destination.x) && (node.point.y == node.destination.y)){
        node.reached = 1;
        if(DEBUG) System.out.println("Reach the goal point.");
      }
    }
    else if(node.point.distance(node.destination)<=node.move_speed){
      node.goal_nearby_flag = 1;
      if(DEBUG) System.out.println("Flag is changed to 1");
    }
    else{
      node.goal_nearby_flag = 0;
      if(DEBUG) System.out.println("Flag is changed to 0");
    }
  }
}