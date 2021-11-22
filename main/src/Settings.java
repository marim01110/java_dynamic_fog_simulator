public class Settings {
  //------SIMULATION SETTINGS------
  static final boolean DEBUG = true;
  static final boolean FOG_USE = true;
  static final int SIM_TIME = 43200;// Unit is seconds.
  static final int EDGE_DIST = 1800;// Unit is meters.
  static final int INIT_MAX_NODES = 4000;
  static final int CONTENTS_RETRIEVE_FREQUENCY = 60;// Unit is seconds.
  static final int STAY_MIN_TIME = 3;// Unit is minites.
  static final int STAY_MAX_TIME = 10;// Unit is minites.
  static final double RTT_DIRECT_CELLULAR = 14.13;// Unit is milliseconds.
  static final double RTT_DIRECT_BLUETOOTH = 200;// Unit is milliseconds.
  static final double RTT_CLOUD = 28.91;// Unit is milliseconds.
  static final boolean BLUETOOTH_USE = true;
  static final double BT_CONNECTION_RANGE = 50;// Unit is meters.

  //------MODE 5 SETTINGS------
  static final int STAGES = 0;
  static final int[] max_nodes_change_time_array = {};
  static final int[] max_nodes_array = {};

  //------FOG SETTINGS------
  static final int FOG_STORAGE_SIZE = 100000;// Unit is kilobytes.
  static final boolean CONTENTS_TYPES_FIXED = true;// Contents_type_dynamic feature is currently not supported (2021/11/17 8:56 p.m.).
  static final int CONTENTS_TYPES_MAX = 300;
  static final int MAX_PERCENTAGE_OF_DUPLICATION = 50;
  static final int CONTENTS_EXPIRE_AFTER = 180;// Unit is seconds.
  static final int DYNAMIC_FOG_RATIO_PERCENTAGE = 20;
  static final int DYNAMIC_FOG_UPDATE_INTERVAL = 300;// Unit is seconds.

  //------LANDMARK SETTINGS------
  static final int LANDMARKS = 3;
  static final int[] landmark_point_x_array = {1000, 1800, 0};
  static final int[] landmark_point_y_array = {1000, 1800, 0};

  //------NODE MOVE SPEED SETTINGS------
  static final int MOVE_SPEEDS = 4;
  static final int[] move_speed_array = {1, 2, 4, 6};// Unit is meters per second.

  /*
  http://www.yspc-ysmc.jp/ysmc/column/health-fitness/walking-2.html
  https://bike.shimano.com/ja-jp/mindswitch/lab/89/#:~:text=%E3%82%B7%E3%83%86%E3%82%A3%E3%82%B5%E3%82%A4%E3%82%AF%E3%83%AB%EF%BC%88%E3%81%84%E3%82%8F%E3%82%86%E3%82%8B%E3%83%9E%E3%83%9E%E3%83%81%E3%83%A3%E3%83%AA%EF%BC%89%E3%81%AE,%E6%98%8E%E3%82%89%E3%81%8B%E3%81%AB%E3%81%AA%E3%81%A3%E3%81%A6%E3%81%84%E3%81%BE%E3%81%99%E3%80%82
  https://www.ktr.mlit.go.jp/gaikan/pi_kouhou/jigyou_gaiyou2015/p6-7.pdf
  */

  //------BATTERY SIMULATION SETTINGS------
  static final int BATTERY_INIT_MIN_PERCENTAGE = 20;
  static final int BATTERY_INIT_MAX_PERCENTAGE = 90;
  static final int BATTERY_LOW_THRESHOLD_PERCENTAGE = 40;
  static final double BATTERY_COMSUMPTION_BT_SEND = 0.2;
  static final double BATTERY_COMSUMPTION_BT_RECV = 0.1;
  static final double BATTERY_COMSUMPTION_CELL_SEND = 0.4;
  static final double BATTERY_COMSUMPTION_CELL_RECV = 0.2;  
}
