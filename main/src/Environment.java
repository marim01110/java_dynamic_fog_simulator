import java.util.ArrayList;
import java.util.Scanner;
import java.awt.geom.Point2D;

public class Environment {
    static int time_count;
    static int file_deleted;
    static int node_leased;
    static int stage;
    static ArrayList<Node_info> node_list = new ArrayList<>();
    static ArrayList<Fog_info> dynamic_fog_list = new ArrayList<>();
    static ArrayList<Data_info> network_contents_list = new ArrayList<>();
    static ArrayList<Integer> last_used = new ArrayList<>();

    /* Macro variables */
    static final int FOG_IS_OK = 0;
    static final int FOG_WILL_BE_DELETED = 1;

    static void init(){
      time_count = 0;
      file_deleted = 0;
      node_leased = 0;
      stage = 0;
      node_list.clear();
      dynamic_fog_list.clear();
      network_contents_list.clear();
      last_used.clear();
    }

    static void loop(){
      Scanner scan = new Scanner(System.in);
      int loop, mode;

      System.out.print("Specify runnning_mode [4,5]: ");
      mode = scan.nextInt();

      System.out.print("Specify the number of repetitions: ");
      loop = scan.nextInt();
      if(loop <= 0) System.exit(-1);

      for(int i = 0; i < loop; i++){
        System.out.println("loop_count " + i);
        init();
        switch(mode){
          case 4:   Mode4.main();
                    break;
          case 5:   Mode5.main();
                    break;
          default:  break;
        }
        System.out.println();
      }
      scan.close();
    }

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
