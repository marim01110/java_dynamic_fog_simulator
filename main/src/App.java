/*
Used Java Version
openjdk 11.0.12 2021-07-20
OpenJDK Runtime Environment (build 11.0.12+7-post-Debian-2)
OpenJDK 64-Bit Server VM (build 11.0.12+7-post-Debian-2, mixed mode)
*/

import java.util.Scanner;
import java.awt.geom.Point2D;
import java.util.ArrayList;

class Node_info{
  int num;
  Point2D.Double point = new Point2D.Double();
  Point2D.Double destination = new Point2D.Double();
  boolean dynamic_fog;
  boolean goal_nearby;
  boolean reached;
  int move_speed;

  public Node_info(int num, Point2D.Double point, Point2D.Double destination, boolean dynamic_fog, boolean goal_nearby, boolean reached, int move_speed){
    this.num = num;
    this.point = point;
    this.destination = destination;
    this.dynamic_fog = dynamic_fog;
    this.goal_nearby = goal_nearby;
    this.reached = reached;
    this.move_speed = move_speed;
  }
}

class Storage{
  int node_num;
  int total_capacity;
  int used_capacity;
  ArrayList<Integer> fog_stored_contents_list = new ArrayList<>();

  public Storage(int node_num, int total_capacity, int used_capacity, ArrayList<Integer> fog_stored_contents_list){
    this.node_num = node_num;
    this.total_capacity = total_capacity;
    this.used_capacity = used_capacity;
    this.fog_stored_contents_list = fog_stored_contents_list;
  }
}

class Data{
  int num;
  int file_size;
  int cached_by_total;
  ArrayList<Integer> cached_by_list = new ArrayList<>();

  public Data(int num, int file_size, int cached_by_total, ArrayList<Integer> cached_by_list){
    this.num = num;
    this.file_size = file_size;
    this.cached_by_total = cached_by_total;
    this.cached_by_list = cached_by_list;
  }
}

public class App {
  static final boolean DEBUG = true;
  static final boolean FOG_USE = false;
  static final boolean CONTENTS_TYPES_FIXED = false;
  static final int CONTENTS_TYPES_MAX = 100;
  static final int TIME = 10;
  static final int EDGE_DIST = 2000;
  static final int DYNAMIC_FOG_RATIO_PERCENTAGE = 20;
  static final int DYNAMIC_FOG_UPDATE_INTERVAL = 5;
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(System.in);

    System.out.println("Select running mode.[4,5]");
    int runnning_mode = scan.nextInt();
    switch(runnning_mode){
      case 4:     Mode4.main();
                  break;
      case 5:     Mode5.main();
                  break;
      default:    break;
    }
    scan.close();
  }
}
