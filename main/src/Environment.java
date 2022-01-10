import java.util.ArrayList;
import java.util.Scanner;

public class Environment {
    static int mode;
    static int time_count;
    static int cache_data_total;
    static int file_deleted;
    static int node_leased;
    static int stage;
    static final int INIT = -1;
    static int[] max_nodes_array = new int[24];
    static Landmark landmark_array[] = new Landmark[Settings.LANDMARKS];
    static ArrayList<Node_info> node_list = new ArrayList<>();
    static ArrayList<Fog_info> dynamic_fog_list = new ArrayList<>(2000);
    static ArrayList<Data_info> network_contents_list = new ArrayList<>(Settings.CONTENTS_TYPES_MAX);
    static ArrayList<Integer> last_used = new ArrayList<>(Settings.CONTENTS_TYPES_MAX);

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

    void loop(){
      Scanner scan = new Scanner(System.in);
      int loop;
      var sim = new Simulator();

      System.out.print("Specify the number of repetitions: ");
      loop = scan.nextInt();
      if(loop <= 0) System.exit(-1);

      System.out.print("Specify runnning_mode [4,5]: ");
      Environment.mode = scan.nextInt();

      for(int i = 0; i < loop; i++){
        System.out.println("loop_count " + i);
        init();
        sim.main();
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

    static void init_max_nodes_array(){
      for(int i = 0; i < 24; i++){
        Environment.max_nodes_array[i] = Settings.nodes_model_array[i] * Settings.NODES_REALITY / 100;
      }
    }

    static Landmark return_landmark_point(int index){
      return landmark_array[index];
    }

    static int return_move_speed(int index){
      int move_speed;

      move_speed = Settings.move_speed_array[index];
      return move_speed;
    }

    static int return_max_nodes(){
      int time_now, max_nodes;
      
      time_now = time_count / 3600 + Settings.START_FROM;
      max_nodes = max_nodes_array[time_now];

      node_list.ensureCapacity(max_nodes);
      dynamic_fog_list.ensureCapacity(max_nodes * Settings.DYNAMIC_FOG_RATIO_PERCENTAGE / 100);

      return max_nodes;
    }
}
