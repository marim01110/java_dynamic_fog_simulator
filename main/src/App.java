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
  int data_refresh_time;
  boolean dynamic_fog;
  boolean goal_nearby;
  boolean reached;
  int move_speed;
  double battery_remain_percentage;

  public Node_info(int num, Point2D.Double point, Point2D.Double destination, int data_refresh_time, boolean dynamic_fog, boolean goal_nearby, boolean reached, int move_speed, double battery_remain_percentage){
    this.num = num;
    this.point = point;
    this.destination = destination;
    this.data_refresh_time = data_refresh_time;
    this.dynamic_fog = dynamic_fog;
    this.goal_nearby = goal_nearby;
    this.reached = reached;
    this.move_speed = move_speed;
    this.battery_remain_percentage = battery_remain_percentage;
  }
}

class Fog_info{
  int node_num;
  int total_capacity;
  int used_capacity;
  ArrayList<Integer> fog_stored_contents_list = new ArrayList<>();

  public Fog_info(int node_num, int total_capacity, int used_capacity, ArrayList<Integer> fog_stored_contents_list){
    this.node_num = node_num;
    this.total_capacity = total_capacity;
    this.used_capacity = used_capacity;
    this.fog_stored_contents_list = fog_stored_contents_list;
  }
}

class Data_info{
  int num;
  int file_size;
  int hosted_by_total;
  ArrayList<Integer> hosted_by_list = new ArrayList<>();

  public Data_info(int num, int file_size, int hosted_by_total, ArrayList<Integer> hosted_by_list){
    this.num = num;
    this.file_size = file_size;
    this.hosted_by_total = hosted_by_total;
    this.hosted_by_list = hosted_by_list;
  }
}

public class App {
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
