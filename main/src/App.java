import java.util.Random;
import java.util.Scanner;
import java.awt.geom.Point2D;

class Node_info{
    int num;
    Point2D.Double point = new Point2D.Double();
    int move_speed;
}

public class App {
    static int MAX_NODES = 2;
    static int time_sec = 10;
    static int edge_dist = 2000;
    public static void main(String[] args) throws Exception {
        Random rand = new Random();
        Scanner scan = new Scanner(System.in);

        System.out.println("Select running mode.[4]");
        int runnning_mode = scan.nextInt();
        switch(runnning_mode){
            case 4:     Mode4.main(rand);
                        break;
            case 5:     break;
            default:    break;
        }
    }
}
