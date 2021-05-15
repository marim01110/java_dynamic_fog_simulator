import java.util.Random;
import java.awt.geom.Point2D;

public class Node_mng {
    static int init(Random rand, int node_leased, Node_info node, int init_x, int init_y, double dest_x, double dest_y){
        //Initialize Node. Set num, first location, move speed.
        node.num = node_leased + 1;
        node.point.setLocation(init_x, init_y);
        node.destination.setLocation(dest_x, dest_y);
        node.goal_nearby_flag = 0;
        node.move_speed = rand.nextInt(40)+10;
        return node.num;
    }

    static void put(int node_leased, Random rand, int MAX_GOALS, Node_info node_array[], Point2D.Double goals_array[]){
        int goal;
        if(MAX_GOALS!=0){
            goal = rand.nextInt(MAX_GOALS);
            node_leased = init(rand, node_leased, node_array[node_leased], 1000, 1000, goals_array[goal].x, goals_array[goal].y);
        }
        else{
            goal = 0;
            node_leased = init(rand, node_leased, node_array[node_leased], 1000, 1000, 0, 0);
        }
    }

    static int check_reach_goal(Node_info node){
        int result;
        if(node.point.distance(node.destination)<=node.move_speed){
            node.goal_nearby_flag = 1;
        }
        else node.goal_nearby_flag = 0;
        return result;
    }
}