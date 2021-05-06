import java.util.Random;
import java.awt.geom.Point2D;

class Node_info{
    int num;
    Point2D.Double point = new Point2D.Double();
    int move_speed;
}

public class App {
    static int time_sec = 10;
    static int edge_dist = 2000;
    public static void main(String[] args) throws Exception {
        Random rand = new Random();
        Node_info[] node_array = new Node_info[2];

        for(int i=0; i<node_array.length; i++) node_array[i] = new Node_info();

        Node_mng.init(rand, node_array[0], 1000, 1000);

        int count = 0;
        while(count < time_sec){
            Move.random_walk(node_array[0], rand); 
            count += 1;
        }  
    }
}
