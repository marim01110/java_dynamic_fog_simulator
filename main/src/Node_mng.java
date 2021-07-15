import java.util.Random;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Node_mng {
  private static final boolean DEBUG = App.DEBUG;
  private static final int LANDMARKS = 2;
  static Point2D.Double[] landmark_point_array = new Point2D.Double[LANDMARKS];
  
  static int[] landmark_point_x_array = {1000, 800};
  static int[] landmark_point_y_array = {1000, 200};

  static void init(){
    Point2D.Double temp = new Point2D.Double();

    for(int i = 0; i < LANDMARKS; i++){
      temp.setLocation(landmark_point_x_array[i], landmark_point_y_array[i]);
      landmark_point_array[i] = temp;
    }
  }

  static Node_info generate(ArrayList<Node_info> node_list, int node_leased){
    Random rand = new Random();
    int point_index, destination_index, move_speed;

    do{
      point_index = rand.nextInt(LANDMARKS);
      destination_index = rand.nextInt(LANDMARKS);
    }while(point_index != destination_index);
    move_speed = rand.nextInt(40) + 10;

    var newnode = new Node_info(node_leased, landmark_point_array[point_index], landmark_point_array[destination_index], false, false, false, move_speed);
    return newnode;
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