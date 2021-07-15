import java.util.Random;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node_mng {
  private static final boolean DEBUG = App.DEBUG;
  static final int LANDMARKS = 3;
  
  private static int[] landmark_point_x_array = {1000, 800, 500};
  private static int[] landmark_point_y_array = {1000, 200, 500};

  static Node_info generate(ArrayList<Node_info> node_list, int node_leased){
    Random rand = new Random();
    Point2D.Double start = new Point2D.Double();
    Point2D.Double destination = new Point2D.Double();
    int point_index, destination_index, move_speed;

    do{
      point_index = rand.nextInt(LANDMARKS);
      destination_index = rand.nextInt(LANDMARKS);
    }while(point_index == destination_index);

    start.setLocation(landmark_point_x_array[point_index], landmark_point_y_array[point_index]);
    destination.setLocation(landmark_point_x_array[destination_index], landmark_point_y_array[destination_index]);
    move_speed = rand.nextInt(40) + 10;

    var newnode = new Node_info(node_leased, start, destination, false, false, false, move_speed);
    if(DEBUG) System.out.println("Node " + newnode.num + " Created. Start from " + newnode.point + ", Goal is " + newnode.destination);
    return newnode;
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