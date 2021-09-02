import java.awt.geom.Point2D;

public class Environment {
    //------SIMULATION SETTINGS------
    static final boolean DEBUG = true;
    static final boolean FOG_USE = true;
    static final int TIME = 10;
    static final int EDGE_DIST = 2000;
    static final int INIT_MAX_NODES = 10;
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
}
