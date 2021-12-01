import java.util.ArrayList;
import java.util.Scanner;
import java.awt.geom.Point2D;

public class Environment {
    static int mode;
    static int time_count;
    static int cache_data_total;
    static int file_deleted;
    static int node_leased;
    static int stage;
    static Landmark landmark_array[] = new Landmark[Settings.LANDMARKS];
    static ArrayList<Node_info> node_list = new ArrayList<>();
    static ArrayList<Fog_info> dynamic_fog_list = new ArrayList<>();
    static ArrayList<Data_info> network_contents_list = new ArrayList<>();
    static ArrayList<Integer> last_used = new ArrayList<>();

    /* Macro variables */
    static final int FOG_IS_OK = 0;
    static final int FOG_WILL_BE_DELETED = 1;

    static void init(){
      time_count = 0;
      cache_data_total = 0;
      file_deleted = 0;
      node_leased = 0;
      stage = 0;
      node_list.clear();
      dynamic_fog_list.clear();
      network_contents_list.clear();
      last_used.clear();
      init_landmark();
    }

    static void loop(){
      Scanner scan = new Scanner(System.in);
      int loop;

      System.out.print("Specify the number of repetitions: ");
      loop = scan.nextInt();
      if(loop <= 0) System.exit(-1);

      System.out.print("Specify runnning_mode [4,5]: ");
      Environment.mode = scan.nextInt();

      for(int i = 0; i < loop; i++){
        System.out.println("loop_count " + i);
        init();
        Sim.main();
        System.out.println();
      }
      scan.close();
    }

    static void init_landmark(){
      landmark_array[0] = new Landmark(1, "JR Kyoto Station", 640, 890);
      landmark_array[1] = new Landmark(2, "To-ji Temple", 225, 685);
      landmark_array[2] = new Landmark(3, "Higashi-Honganji Temple", 640, 1120);
      landmark_array[3] = new Landmark(4, "Kyoto Railway Museum", 30, 960);
      landmark_array[4] = new Landmark(5, "Nintendo Co., Ltd Headquarter", 550, 200);
      landmark_array[5] = new Landmark(6, "Komyo-in Temple", 1180, 415);
      landmark_array[6] = new Landmark(7, "Fushimi Kandakara Shrine", 1290, 80);
      landmark_array[7] = new Landmark(8, "Sennyu-ji Temple", 1420, 590);
      landmark_array[8] = new Landmark(9, "Rengeo-in Sanjusangen-do", 1100, 1030);
      landmark_array[9] = new Landmark(10, "Kiyomizu-dera Temple", 1550, 1300);
    }

    static Point2D.Double return_landmark_point(int index){
      var point = new Point2D.Double();

      point.setLocation(landmark_array[index].point);
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
