import java.util.Random;
import java.util.Scanner;
import java.awt.geom.Point2D;

class Node_info{
  int num;
  Point2D.Double point = new Point2D.Double();
  Point2D.Double destination = new Point2D.Double();
  boolean goal_nearby;
  boolean dynamic_fog;
  boolean reached;
  int move_speed;
}

public class App {
  static final int TIME_SEC = 100;
  static final int EDGE_DIST = 2000;
  static final int DYNAMIC_FOG_RATIO_PERCENTAGE = 10;
  static final int DYNAMIC_FOG_UPDATE_INTERVAL = 60;
  public static void main(String[] args) throws Exception {
    Random rand = new Random();
    Scanner scan = new Scanner(System.in);

    System.out.println("Select running mode.[4,5]");
    int runnning_mode = scan.nextInt();
    switch(runnning_mode){
      case 4:     Mode4.main(rand);
                  break;
      case 5:     Mode5.main(rand, scan);
                  break;
      default:    break;
    }
  }
}
