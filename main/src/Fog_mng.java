import java.util.ArrayList;
import java.util.Random;

public class Fog_mng {
  private static final boolean DEBUG = Settings.DEBUG;
  private static final int INIT = -1;

  static Fog_info get_fog_info(int dynamic_fog_num){
    Fog_info dynamic_fog = null;

    for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
      if(Environment.dynamic_fog_list.get(i).node_num == dynamic_fog_num){
        dynamic_fog = Environment.dynamic_fog_list.get(i);
        break;
      }
    }
    if(dynamic_fog == null){
      System.out.println("Requested fog: " + dynamic_fog_num + " is Not Found.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }

    return dynamic_fog;
  }

  static void register(int node_leased){
    Random rand = new Random();
    int dynamic_fogs_required, counter, dynamic_fog_candidate;
    boolean error;
    
    dynamic_fogs_required = Environment.node_list.size() * Settings.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    if(dynamic_fogs_required > Environment.dynamic_fog_list.size()){
      while(dynamic_fogs_required > Environment.dynamic_fog_list.size()){
        error = false;
        counter = rand.nextInt(Environment.node_list.size());
        dynamic_fog_candidate = Environment.node_list.get(counter).num;
        if(DEBUG) System.out.println("dynamic_fog_candidate: " + dynamic_fog_candidate);

        //Verify the candidate.
        for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
          if(dynamic_fog_candidate == Environment.dynamic_fog_list.get(i).node_num) error = true;
          if(error == true) break;
        }
        if(error == false){
          for(int i = 0; i < Environment.node_list.size(); i++){
            if(error == true){
              if(Environment.node_list.get(i).num == dynamic_fog_candidate) error = false;
            }
          }
        }

        if(error == true){
          if(DEBUG) System.out.println("The Candidate is Dupulicated.");
        }
        else if(error == false){//"error == false" means the candidate not dupulicated.
          var fog_stored_contents_list = new ArrayList<Integer>();
          var temp = new Fog_info(dynamic_fog_candidate, Environment.FOG_IS_OK, Settings.FOG_STORAGE_SIZE, 0, fog_stored_contents_list);
          Environment.dynamic_fog_list.add(temp);
          if(DEBUG) System.out.println("Node " + dynamic_fog_candidate + " becomes Dynamic_Fog node.");
        }
        if(DEBUG) System.out.println("Required: " + dynamic_fogs_required + ", Exist: " + Environment.dynamic_fog_list.size());
      }
    }
  }

  private static void unregister(Fog_info delete_df){
    Data_info data;
    Object obj;

    if(DEBUG) System.out.println("Dynamic_Fog Node " + delete_df.node_num + " is now deleting.");
    for(int i = 0; i < delete_df.fog_stored_contents_list.size(); i++){
      data = null;
      data = Data_mng.get_data_info(delete_df.fog_stored_contents_list.get(i));

      //Update Data_info
      obj = delete_df.node_num;
      data.hosted_by_list.remove(obj);
}
    Environment.dynamic_fog_list.remove(delete_df);
  }

  static void keep_alive(){
    Node_info node;

    for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
      node = Node_mng.get_node_info(Environment.dynamic_fog_list.get(i).node_num);
      if(node.goal_nearby == true){
        
      }
    }
  }

  static void dynamic_fog_dead_judge(int node_list_index){
    for(int j = 0; j < Environment.dynamic_fog_list.size(); j++){
      if(Environment.dynamic_fog_list.get(j).node_num == Environment.node_list.get(node_list_index).num) unregister(Environment.dynamic_fog_list.get(j));
    }
  }

  static ArrayList<Integer> search_near_dynamic_fogs(Node_info current_node){
    var result = new ArrayList<Integer>();
    double /*distance_calc_min, */temp_distance, distance = 9999;//Initialize distance
    Node_info dynamic_fog_node;
    int nearest_dynamic_fog_node_num = INIT;
    //boolean reset;

    for(int i = 0; i < Environment.dynamic_fog_list.size(); i++){
      dynamic_fog_node = Node_mng.get_node_info(Environment.dynamic_fog_list.get(i).node_num);
      temp_distance = current_node.point.distance(dynamic_fog_node.point);
      if(distance > temp_distance){
        distance = temp_distance;
        nearest_dynamic_fog_node_num = dynamic_fog_node.num;
      }
      if(temp_distance <= Settings.BT_CONNECTION_RANGE){
        result.add(dynamic_fog_node.num);
      }
    }

    dynamic_fog_node = null;

    if(result.size() == 0){
      result.add(nearest_dynamic_fog_node_num);
    }/*
    else{
      //Sorting ascending order
      do{
        distance_calc_min = 0;
        reset = false;
        for(int i = 0; i < result.size(); i++){
          dynamic_fog_node = Node_mng.get_node_info(node_list, result.get(i));
          temp_distance = current_node.point.distance(dynamic_fog_node.point);
          if(distance_calc_min > temp_distance){
            result.add(result.get(i));
            result.remove(i);
            reset = true;
            break;
          }
          else{
            distance_calc_min = temp_distance;
          }
        }
      }while(reset);
    }*/
    return result;
  }

  static void calc_used_capacity(Fog_info dynamic_fog){
    Data_info data;
    int used_capacity = 0;
    int data_num;
    
    for(int i = 0; i < dynamic_fog.fog_stored_contents_list.size(); i++){
      data_num = dynamic_fog.fog_stored_contents_list.get(i);
      data = Data_mng.get_data_info(data_num);
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
