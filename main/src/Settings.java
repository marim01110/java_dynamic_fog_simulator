public class Settings {
  //------SIMULATION SETTINGS------
  static final boolean DEBUG = false;
  static final boolean FOG_USE = true;
  static final int SIM_TIME_HOURS = 18;// Unit is hours.
  static final int EDGE_DIST = 1700;// Unit is meters.
  static final int INIT_MAX_NODES = 4000;
  static final int CONTENTS_RETRIEVE_FREQUENCY = 60;// Unit is seconds.
  static final int LANDMARKS = 10;
  static final int WAYPOINT_MIN = 3;
  static final double RTT_DIRECT_CELLULAR = 14.13;// Unit is milliseconds.
  static final double RTT_DIRECT_BLUETOOTH = 200;// Unit is milliseconds.
  static final double RTT_CLOUD = 28.91;// Unit is milliseconds.
  static final boolean BLUETOOTH_USE = true;
  static final double BT_CONNECTION_RANGE = 50;// Unit is meters.

  //------MODE 5 SETTINGS------
  static final int START_FROM = 5;// 24-hour notation
  static final int NODES_REALITY = 1;// Unit is percents.
  static final int[] nodes_model_array = {95405, 94750, 89750, 88580, 89975, 96950, 103675, 116200, 139050, 153700, 175000, 190450, 215450, 221350, 207300, 193530, 197100, 205200, 194950, 176500, 159550, 129100, 109950, 99030};

  //------FOG SETTINGS------
  static final int FOG_STORAGE_SIZE = 100000;// Unit is kilobytes.
  static final boolean CONTENTS_TYPES_FIXED = true;// Contents_type_dynamic feature is currently not supported (2021/11/17 8:56 p.m.).
  static final int CONTENTS_TYPES_MAX = 300;
  static final int MAX_PERCENTAGE_OF_DUPLICATION = 50;
  static final int CONTENTS_EXPIRE_AFTER = 180;// Unit is seconds.
  static final int DYNAMIC_FOG_RATIO_PERCENTAGE = 20;
  static final int DYNAMIC_FOG_UPDATE_INTERVAL = 300;// Unit is seconds.

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
  static final double BATTERY_COMSUMPTION_BT_SEND = 0.002;
  static final double BATTERY_COMSUMPTION_BT_RECV = 0.001;
  static final double BATTERY_COMSUMPTION_CELL_SEND = 0.004;
  static final double BATTERY_COMSUMPTION_CELL_RECV = 0.002;  
}
