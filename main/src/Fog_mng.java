import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Point2D;

public class Fog_mng {
  private static final boolean DEBUG = App.DEBUG;

  static void dynamic_fog_set(ArrayList<Node_info> node_list, int node_leased, ArrayList<Storage> dynamic_fog_list){
    Random rand = new Random();
    int dynamic_fogs_required, dynamic_fog_candidate;
    boolean error;
    
    dynamic_fogs_required = node_list.size() * App.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    if(dynamic_fogs_required > dynamic_fog_list.size()){
      while(dynamic_fogs_required > dynamic_fog_list.size()){
        error = false;
        dynamic_fog_candidate = rand.nextInt(node_leased);
        if(DEBUG) System.out.println("dynamic_fog_candidate: " + dynamic_fog_candidate);
        for(int i = 0; i < dynamic_fog_list.size(); i++){
          if(dynamic_fog_candidate == dynamic_fog_list.get(i).node_num) error = true;
          if(error == true) break;
        }
        if(error == false){
          for(int i = 0; i < node_list.size(); i++){
            if(error == true){
              if(node_list.get(i).num == dynamic_fog_candidate) error = false;
            }
          }
        }

        if(error == true){
          if(DEBUG) System.out.println("The Candidate is Dupulicated.");
        }
        else if(error == false){//"error == false" means the candidate not dupulicated.
          var cache_index_list = new ArrayList<Integer>();
          var temp = new Storage(dynamic_fog_candidate, 500, 0, cache_index_list);
          dynamic_fog_list.add(temp);
          if(DEBUG) System.out.println("Node " + dynamic_fog_candidate + " becomes Dynamic_Fog node.");
        }
        if(DEBUG) System.out.println("Required: " + dynamic_fogs_required + ", Exist: " + dynamic_fog_list.size());
      }
    }
  }

  static void dynamic_fog_dead_judge(ArrayList<Node_info> node_list, int node_list_index, ArrayList<Storage> dynamic_fog_list){
    for(int j = 0; j < dynamic_fog_list.size(); j++){
      if(dynamic_fog_list.get(j).node_num == node_list.get(node_list_index).num){
        if(DEBUG) System.out.println("Dynamic_Fog Node " + dynamic_fog_list.get(j).node_num + " is now deleting.");
        dynamic_fog_list.remove(j);
      }
    }
  }

  static void dynamic_fog_print_status(ArrayList<Node_info> node_list, ArrayList<Storage> dynamic_fog_list){//For DEBUG
    int dynamic_fogs_required = node_list.size() * App.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    System.out.print(dynamic_fog_list.size() + " Dynamic Fog Node(s) exist (Minimum DF: " + dynamic_fogs_required + "), Dynamic Fog Node:");
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      if(i != 0) System.out.print(", ");
      System.out.print(dynamic_fog_list.get(i).node_num);
    }
    System.out.println("");
  }

  private static boolean data_exist(ArrayList<Data> cache_data_list, Integer data_num){
    boolean result = false;
    for(int i = 0; i < cache_data_list.size(); i++){
      if(cache_data_list.get(i).num == data_num){
        result = true;
        if(DEBUG) System.out.println("Data Found on Local Network.");
      }
      if(result == true) break;
    }
    return result;
  }

  private static void data_add(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> cache_data_list, Integer dynamic_fog_num, Integer data_num){
    Random rand = new Random();
    var cached_by_list = new ArrayList<Integer>();
    var cache_index_list = new ArrayList<Integer>();

    int data_size = rand.nextInt(40) + 10;
    int cached_by_total;

    int dynamic_fog_index_num = -1;// Initialize variable
    int used_capacity;
    int total_capacity;

    //Update cache_data_list Process
    try{
      Data data = cache_data_list.get(data_num);
      cached_by_total = data.cached_by_total + 1;
      for (int i = 0; i < data.cached_by_list.size(); i++) {
        cached_by_list.add(data.cached_by_list.get(i));
      }
    }
    catch(Exception e){
      cached_by_total = 1;
    }
    cached_by_list.add(dynamic_fog_num);

    var temp_Data = new Data(data_num, data_size, cached_by_total, cached_by_list);
    cache_data_list.add(temp_Data);
    if(DEBUG) System.out.println("Data Added.");

    //Update dynamic_fog_list Process
    if(DEBUG) System.out.println("Lookinig for index_num for Dynamic Fog Node: " + dynamic_fog_num);
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      if(dynamic_fog_list.get(i).node_num == dynamic_fog_num){
        dynamic_fog_index_num = i;
      }
    }
    try{
      for(int i = 0; i < dynamic_fog_list.get(dynamic_fog_index_num).cache_index_list.size(); i++){
        cache_index_list.add(dynamic_fog_list.get(dynamic_fog_index_num).cache_index_list.get(i));
      }
    }
    catch(Exception e){}
    //total_capacity = dynamic_fog_list.get(dynamic_fog_index_num).total_capacity;
    cache_index_list.add(data_num);
    total_capacity = dynamic_fog_list.get(dynamic_fog_index_num).total_capacity;
    used_capacity = calc_used_capacity(cache_data_list, cache_index_list);

    //Replace Dynamic_Fog Information
    dynamic_fog_list.remove(dynamic_fog_index_num);
    var temp_Storage = new Storage(dynamic_fog_num, total_capacity, used_capacity, cache_index_list);
    dynamic_fog_list.add(temp_Storage);
  }

  static int set_nearest_dynamic_fog(ArrayList<Node_info> node_list, ArrayList<Storage> dynamic_fog_list, Point2D.Double current_node){
    double distance = 9999;//Initialze distance
    double temp_distance;
    int dynamic_fog_result = -1;
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      temp_distance = current_node.distance(node_list.get(dynamic_fog_list.get(i).node_num).point);
      if(distance > temp_distance){
        distance = temp_distance;
        dynamic_fog_result = dynamic_fog_list.get(i).node_num;
      }
    }
    return dynamic_fog_result;
  }

  static void data_search(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> cache_data_list, int nearest_dynamic_fog, int need_data_num){
    boolean data_exist;
    boolean data_found = false;

    data_exist = data_exist(cache_data_list, need_data_num);
    if(data_exist == true){
      for(int j = 0; j < cache_data_list.get(need_data_num).cached_by_list.size(); j++){
        if(nearest_dynamic_fog == cache_data_list.get(need_data_num).cached_by_list.get(j)) data_found = true;
      }
      if(data_found == false){
        //Data Copy Process
        data_add(dynamic_fog_list, cache_data_list, nearest_dynamic_fog, need_data_num);
        if(DEBUG) System.out.println("Cache Data Copied.");
      }
    }
    else if(data_exist == false){
      data_add(dynamic_fog_list, cache_data_list, nearest_dynamic_fog, need_data_num);
      data_found = true;
    }
    if(data_found == true) System.out.println("Data Found.");
  }

  static int calc_used_capacity(ArrayList<Data> cache_data_list, ArrayList<Integer> cache_index_list){
    int used_capacity = 0;
    
    for(int i = 0; i < cache_index_list.size(); i++){
      used_capacity += cache_data_list.get(i).file_size;
    }
    
    return used_capacity;
  }
}
