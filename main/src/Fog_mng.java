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

  static int calc_used_capacity(ArrayList<Data> cache_data_list, ArrayList<Integer> cache_index_list){
    int used_capacity = 0;
    
    for(int i = 0; i < cache_index_list.size(); i++){
      used_capacity += cache_data_list.get(i).file_size;
    }
    
    return used_capacity;
  }

  static void print_detail(ArrayList<Storage> dynamic_fog_list){
    Storage node;

    try{
      for(int i = 0; i < dynamic_fog_list.size(); i++){
        node = dynamic_fog_list.get(i);
        System.out.println();
        System.out.println("Dynamic_Fog_index: " + i + ", Node_num: " + node.node_num);
        System.out.println("Total cap. " + node.total_capacity + ", Used cap. " + node.used_capacity);
        System.out.println("Cached Data Num: " + node.cache_index_list);
        System.out.println();
      }
    }
    catch(Exception e){
      System.out.println("No Dynamic Fog exist or Error has occured.");
    }
  }
}
