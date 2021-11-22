/*
Used Java Version
openjdk 17 2021-09-14
OpenJDK Runtime Environment (build 17+35-2724)
OpenJDK 64-Bit Server VM (build 17+35-2724, mixed mode, sharing)
*/

import java.util.Scanner;
import java.awt.geom.Point2D;
import java.util.ArrayList;

class Node_info{
  int num;
  Point2D.Double point = new Point2D.Double();
  Point2D.Double destination = new Point2D.Double();
  boolean stay;
  int stay_time;
  int data_refresh_time;
  boolean dynamic_fog;
  boolean goal_nearby;
  boolean reached;
  int move_speed;
  double battery_remain_percentage;
  boolean battery_low;

  public Node_info(int num, Point2D.Double point, Point2D.Double destination, boolean stay, int stay_time, int data_refresh_time, boolean dynamic_fog, boolean goal_nearby, boolean reached, int move_speed, double battery_remain_percentage, boolean battery_low){
    this.num = num;
    this.point = point;
    this.destination = destination;
    this.stay = stay;
    this.stay_time = stay_time;
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

public class App {
  public static void main(String[] args) throws Exception {
    Scanner scan = new Scanner(System.in);

    System.out.print("Select running mode.[0,4,5,8] ");
    int runnning_mode = scan.nextInt();
    switch(runnning_mode){
      case 4:     Environment.init();
                  Mode4.main();
                  break;
      case 5:     Environment.init();
                  Mode5.main();
                  break;
      case 8:     Environment.loop();
                  break;
      case 0:     test.main();
                  break;
      default:    break;
    }
    scan.close();
  }
}
