import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Fog_mng {
  private static final boolean DEBUG = Settings.DEBUG;
  private static final int INIT = -1;

  static Fog_info get_fog_info(int dynamic_fog_node_num){
    Fog_info dynamic_fog = null;

    for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
      if(Environment.dynamic_fog_list.get(i).node_num == dynamic_fog_node_num){
        dynamic_fog = Environment.dynamic_fog_list.get(i);
        break;
      }
    }
    if(dynamic_fog == null){
      System.out.println("Requested fog: " + dynamic_fog_node_num + " is Not Found.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }

    return dynamic_fog;
  }

  static void register(int node_leased){
    Random rand = new Random();
    Node_info dynamic_fog_candidate;
    int dynamic_fogs_required, counter;
    boolean error;
    
    dynamic_fogs_required = Environment.node_list.size() * Settings.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    if(dynamic_fogs_required > Environment.dynamic_fog_list.size()){
      while(dynamic_fogs_required > Environment.dynamic_fog_list.size()){
        error = false;
        counter = rand.nextInt(Environment.node_list.size());
        dynamic_fog_candidate = Environment.node_list.get(counter);
        if(DEBUG) System.out.println("dynamic_fog_candidate: " + dynamic_fog_candidate.num);

        //Verify the candidate.
        if(dynamic_fog_candidate.battery_low == true) error = true;
        else for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
          if(dynamic_fog_candidate.num == Environment.dynamic_fog_list.get(i).node_num) error = true;
          if(error == true) break;
        }
        if(error == false){
          for(int i = 0; i < Environment.node_list.size(); i++){
            if(error == true){
              if(Environment.node_list.get(i).num == dynamic_fog_candidate.num) error = false;
            }
          }
        }

        if(error == true){
          if(DEBUG) System.out.println("The Candidate is incompatible.");
        }
        else if(error == false){//"error == false" means the candidate not dupulicated.
          var fog_stored_contents_list = new ArrayList<Integer>();
          var temp = new Fog_info(dynamic_fog_candidate.num, Environment.FOG_IS_OK, Settings.FOG_STORAGE_SIZE, 0, fog_stored_contents_list);
          Environment.dynamic_fog_list.add(temp);
          dynamic_fog_candidate.dynamic_fog = true;
          
          if(DEBUG) System.out.println("Node " + dynamic_fog_candidate.num + " becomes Dynamic_Fog node.");
        }
        if(DEBUG) System.out.println("Required: " + dynamic_fogs_required + ", Exist: " + Environment.dynamic_fog_list.size());
      }
    }
  }

  static void unregister(Fog_info delete_df){
    Data_info data;
    Object obj;
    Node_info node;

    if(DEBUG) System.out.println("Dynamic_Fog Node " + delete_df.node_num + " is now deleting.");
    for(int i = 0; i < delete_df.fog_stored_contents_list.size(); i++){
      data = null;
      data = Data_mng.get_data_info(delete_df.fog_stored_contents_list.get(i), true);

      //Update Data_info
      obj = delete_df.node_num;
      data.hosted_by_list.remove(obj);
    }
    node = Node_mng.get_node_info(delete_df.node_num);
    Environment.dynamic_fog_list.remove(delete_df);
    node.dynamic_fog = false;
  }

  static void keep_alive(){//Unused.
    Node_info node;

    for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
      node = Node_mng.get_node_info(Environment.dynamic_fog_list.get(i).node_num);
      if(node.goal_nearby == true){
        
      }
    }
  }

  static ArrayList<Near_DFs> scan_near_dynamic_fogs(Node_info current_node){
    var result = new ArrayList<Near_DFs>();
    Node_info dynamic_fog_node, nearest_dynamic_fog = null;
    double distance, min_distance = 9999;// Initialize distance

    if(Settings.BLUETOOTH_USE || Settings.WIFI_USE){
      for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
        dynamic_fog_node = Node_mng.get_node_info(Environment.dynamic_fog_list.get(i).node_num);
        distance = current_node.point.distance(dynamic_fog_node.point);
        if(Settings.BLUETOOTH_USE){
          if(Settings.WIFI_USE){
            if(distance <= Settings.WIFI_CONNECTION_RANGE) result.add(new Near_DFs(dynamic_fog_node, distance));
          }
          else if(distance <= Settings.BT_CONNECTION_RANGE) result.add(new Near_DFs(dynamic_fog_node, distance));
        }
        if(distance < min_distance){
          min_distance = distance;
          nearest_dynamic_fog = dynamic_fog_node;
        }
      }
    }

    if(result.size() == 0) result.add(new Near_DFs(nearest_dynamic_fog, min_distance));
    else{
      /* Sort */
      Collections.sort(result, new Comparator<Near_DFs>() {
        @Override
        public int compare(Near_DFs df1, Near_DFs df2){
          return df1.distance < df2.distance ? -1 : 1;
        }
      });
    }

    return result;
  }

  static void calc_used_capacity(Fog_info dynamic_fog){
    Data_info data;
    int used_capacity = 0;
    int data_num;
    
    for(int i = 0; i < dynamic_fog.fog_stored_contents_list.size(); i++){
      data_num = dynamic_fog.fog_stored_contents_list.get(i);
      data = Data_mng.get_data_info(data_num, true);
      used_capacity += data.file_size;
    }
    dynamic_fog.used_capacity = used_capacity;
  }

  static void print_detail(){
    int dynamic_fogs_required;
    Node_info node_info;
    Fog_info node;

    System.out.println("Nodes active: " + Environment.node_list.size());
    dynamic_fogs_required = Environment.node_list.size() * Settings.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    System.out.print(Environment.dynamic_fog_list.size() + " Dynamic Fog Node(s) exist (Minimum DF: " + dynamic_fogs_required + "), Dynamic Fog Node: ");
    for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
      if(i != 0) System.out.print(", ");
      System.out.print(Environment.dynamic_fog_list.get(i).node_num);
    }
    System.out.println();

    try{
      for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
        node = Environment.dynamic_fog_list.get(i);
        node_info = Node_mng.get_node_info(node.node_num);
        System.out.println();
        System.out.println("Dynamic_Fog_index: " + i + ", Node_num: " + node.node_num);
        System.out.println("Total cap. " + node.total_capacity + ", Used cap. " + node.used_capacity + ", Battery remain: " + node_info.battery_remain_percentage + "%");
        System.out.println("Cached Data Num: " + node.fog_stored_contents_list);
        System.out.println();
      }
    }
    catch(Exception e){
      System.out.println("No Dynamic Fog exist or Error has occured.");
    }
  }
}
