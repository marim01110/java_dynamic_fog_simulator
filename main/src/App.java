/*
Used Java Version
openjdk 17.0.1 2021-10-19
OpenJDK Runtime Environment (build 17.0.1+12-39)
OpenJDK 64-Bit Server VM (build 17.0.1+12-39, mixed mode, sharing)
*/

import java.util.Scanner;
import java.awt.geom.Point2D;
import java.util.ArrayList;

class Node_info{
  int num;
  Point2D.Double point = new Point2D.Double();
  Landmark start;
  Landmark destination;
  ArrayList<Landmark> waypoint_list = new ArrayList<>();
  int data_refresh_time;
  boolean dynamic_fog;
  boolean goal_nearby;
  boolean reached;
  int move_speed;
  double battery_remain_percentage;
  boolean battery_low;

  public Node_info(int num, Point2D.Double point, Landmark start, Landmark destination, ArrayList<Landmark> waypoint_list, int data_refresh_time, boolean dynamic_fog, boolean goal_nearby, boolean reached, int move_speed, double battery_remain_percentage, boolean battery_low){
    this.num = num;
    this.point = point;
    this.start = start;
    this.destination = destination;
    this.waypoint_list = waypoint_list;
    this.data_refresh_time = data_refresh_time;
    this.dynamic_fog = dynamic_fog;
    this.goal_nearby = goal_nearby;
    this.reached = reached;
    this.move_speed = move_speed;
    this.battery_remain_percentage = battery_remain_percentage;
    this.battery_low = battery_low;
  }
}

class Fog_info{
  int node_num;
  int status;
  int total_capacity;
  int used_capacity;
  ArrayList<Integer> fog_stored_contents_list = new ArrayList<>();

  public Fog_info(int node_num, int status, int total_capacity, int used_capacity, ArrayList<Integer> fog_stored_contents_list){
    this.node_num = node_num;
    this.status = status;
    this.total_capacity = total_capacity;
    this.used_capacity = used_capacity;
    this.fog_stored_contents_list = fog_stored_contents_list;
  }
}

class Data_info{
  int num;
  int file_size;
  int expire_after;
  ArrayList<Integer> hosted_by_list = new ArrayList<>();

  public Data_info(int num, int file_size, int expire_after, ArrayList<Integer> hosted_by_list){
    this.num = num;
    this.file_size = file_size;
    this.expire_after = expire_after;
    this.hosted_by_list = hosted_by_list;
  }
}

class Landmark{
  int num;
  String name;
  Point2D.Double point = new Point2D.Double();
  
  public Landmark(int num, String name, double x, double y){
    this.num = num;
    this.name = name;
    this.point.setLocation(x, y);
  }
}

class Near_DFs{
  Node_info dynamic_fog;
  double distance;

  public Near_DFs(Node_info dynamic_fog, double distance){
    this.dynamic_fog = dynamic_fog;
    this.distance = distance;
  }
}

public class App {
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(System.in);

    System.out.println("Detected " + Settings.AVAILABLE_CORES_ON_HOST + " cores on current host.");
    System.out.println();

    System.out.println("Mode 4: Random-walk");
    System.out.println("Mode 5: Destination");
    System.out.println("Mode 8: Loop");
    
    System.out.print("Select running mode.[4,5,8] ");
    Environment.mode = scan.nextInt();
    
    if(Environment.mode == 8){
      var env = new Environment();
      env.loop();
    }
    else{
      var sim = new Simulator();
      sim.main();
    }
    scan.close();
  }
}
