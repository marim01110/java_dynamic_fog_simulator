//Used Java Version
//openjdk 11.0.11 2021-04-20
//OpenJDK Runtime Environment (build 11.0.11+9-post-Debian-1deb10u1)
//OpenJDK 64-Bit Server VM (build 11.0.11+9-post-Debian-1deb10u1, mixed mode, sharing)

import java.util.Random;
import java.util.Scanner;
import java.awt.geom.Point2D;
import java.util.ArrayList;

class Node_info{
  int num;
  Point2D.Double point = new Point2D.Double();
  Point2D.Double destination = new Point2D.Double();
  boolean goal_nearby;
  boolean reached;
  int move_speed;

  public Node_info(int num, Point2D.Double point, Point2D.Double destination, boolean goal_nearby, boolean reached, int move_speed){
    this.num = num;
    this.point = point;
    this.destination = destination;
    this.goal_nearby = goal_nearby;
    this.reached = reached;
    this.move_speed = move_speed;
  }
}

class Storage{
  int node_num;
  int total_capacity = 500;
  int used_capacity = 0;
  ArrayList<Integer> cache_num_list = new ArrayList<>();

  public Storage(int node_num){
    this.node_num = node_num;
  }
}

class Data{
  int num;
  int file_size;

  public Data(int num, int file_size){
    this.num = num;
    this.file_size = file_size;
  }
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
