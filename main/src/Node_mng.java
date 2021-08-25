import java.util.Random;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node_mng {
  private static final boolean DEBUG = App.DEBUG;

  static Node_info generate(ArrayList<Node_info> node_list, int node_leased){
    Random rand = new Random();
    Point2D.Double start = new Point2D.Double();
    Point2D.Double destination = new Point2D.Double();
    int point_index, destination_index, move_speed, move_speed_index;

    do{
      point_index = rand.nextInt(Environment.LANDMARKS);
      destination_index = rand.nextInt(Environment.LANDMARKS);
    }while(point_index == destination_index);
    move_speed_index = rand.nextInt(Environment.MOVE_SPEEDS);

    start.setLocation(Environment.return_landmark_point(point_index));
    destination.setLocation(Environment.return_landmark_point(destination_index));
    move_speed = Environment.return_move_speed(move_speed_index);

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