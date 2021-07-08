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
}