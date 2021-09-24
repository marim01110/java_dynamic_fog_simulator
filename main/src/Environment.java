import java.awt.geom.Point2D;

public class Environment {
    //------SIMULATION SETTINGS------
    static final boolean DEBUG = true;
    static final boolean FOG_USE = false;
    static final int TIME_LIMIT = 100;
    static final int EDGE_DIST = 2000;
    static final int INIT_MAX_NODES = 10;

    //------MODE 5 SETTINGS------
    private static final int STAGES = 0;
    private static final int[] max_nodes_change_time_array = {};
    private static final int[] max_nodes_array = {};

    //------FOG SETTINGS------
    static final int FOG_STORAGE_SIZE = 500;
    static final boolean CONTENTS_TYPES_FIXED = true;
    static final int CONTENTS_TYPES_MAX = 10;
    static final int DYNAMIC_FOG_RATIO_PERCENTAGE = 20;
    static final int DYNAMIC_FOG_UPDATE_INTERVAL = 5;

    //------LANDMARK SETTINGS------
    static final int LANDMARKS = 3;
    private static final int[] landmark_point_x_array = {1000, 800, 500};
    private static final int[] landmark_point_y_array = {1000, 200, 500};
  
    //------NODE MOVE SPEED SETTINGS------
    static final int MOVE_SPEEDS = 4;
    private static final int[] move_speed_array = {55, 80, 250, 330};

    /*
    http://www.yspc-ysmc.jp/ysmc/column/health-fitness/walking-2.html
    https://bike.shimano.com/ja-jp/mindswitch/lab/89/#:~:text=%E3%82%B7%E3%83%86%E3%82%A3%E3%82%B5%E3%82%A4%E3%82%AF%E3%83%AB%EF%BC%88%E3%81%84%E3%82%8F%E3%82%86%E3%82%8B%E3%83%9E%E3%83%9E%E3%83%81%E3%83%A3%E3%83%AA%EF%BC%89%E3%81%AE,%E6%98%8E%E3%82%89%E3%81%8B%E3%81%AB%E3%81%AA%E3%81%A3%E3%81%A6%E3%81%84%E3%81%BE%E3%81%99%E3%80%82
    https://www.ktr.mlit.go.jp/gaikan/pi_kouhou/jigyou_gaiyou2015/p6-7.pdf
    */

    //------BATTERY SIMULATION SETTINGS------
    static final int BATTERY_INIT_MIN_PERCENTAGE = 20;
    static final int BATTERY_INIT_MAX_PERCENTAGE = 90;
    static final int BATTERY_MIN_PERCENTAGE = 40;
    static final double BATTERY_COMSUMPTION_BT_SEND = 2.0;
    static final double BATTERY_COMSUMPTION_BT_RECV = 1.0;
    static final double BATTERY_COMSUMPTION_CELL_SEND = 4.0;
    static final double BATTERY_COMSUMPTION_CELL_RECV = 2.0;

    static Point2D.Double return_landmark_point(int index){
      var point = new Point2D.Double();

      point.setLocation(landmark_point_x_array[index], landmark_point_y_array[index]);
      return point;
    }

    static int return_move_speed(int index){
      int move_speed;

      move_speed = move_speed_array[index];
      return move_speed;
    }

    static int change_stages(int time, int stage){
      int result = stage;

      if(stage < STAGES){
        if(time == max_nodes_change_time_array[stage]){
          result = stage + 1;
        }
      }

      return result;
    }

    static int return_max_nodes(int stage){
      return max_nodes_array[stage - 1];
    }
}
