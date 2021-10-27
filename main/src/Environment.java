import java.util.ArrayList;
import java.awt.geom.Point2D;

public class Environment {
    static int time_count;
    static int file_deleted = 0;
    static ArrayList<Node_info> node_list = new ArrayList<>();
    static ArrayList<Fog_info> dynamic_fog_list = new ArrayList<>();
    static ArrayList<Data_info> network_contents_list = new ArrayList<>();

    /* Macro variables */
    static final int FOG_IS_OK = 0;
    static final int FOG_WILL_BE_DELETED = 1;

    static Point2D.Double return_landmark_point(int index){
      var point = new Point2D.Double();

      point.setLocation(Settings.landmark_point_x_array[index], Settings.landmark_point_y_array[index]);
      return point;
    }

    static int return_move_speed(int index){
      int move_speed;

      move_speed = Settings.move_speed_array[index];
      return move_speed;
    }

    static int change_stages(int time, int stage){
      int result = stage;

      if(stage < Settings.STAGES){
        if(time == Settings.max_nodes_change_time_array[stage]){
          result = stage + 1;
        }
      }

      return result;
    }

    static int return_max_nodes(int stage){
      return Settings.max_nodes_array[stage - 1];
    }
}
