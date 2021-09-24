import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Point2D;

public class Fog_mng {
  private static final boolean DEBUG = Environment.DEBUG;
  private static final int INIT = -1;

  static void dynamic_fog_set(ArrayList<Node_info> node_list, int node_leased, ArrayList<Storage> dynamic_fog_list){
    Random rand = new Random();
    int dynamic_fogs_required, counter, dynamic_fog_candidate;
    boolean error;
    
    dynamic_fogs_required = node_list.size() * Environment.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    if(dynamic_fogs_required > dynamic_fog_list.size()){
      while(dynamic_fogs_required > dynamic_fog_list.size()){
        error = false;
        counter = rand.nextInt(node_list.size());
        dynamic_fog_candidate = node_list.get(counter).num;
        if(DEBUG) System.out.println("dynamic_fog_candidate: " + dynamic_fog_candidate);

        //Verify the candidate.
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
          var fog_stored_contents_list = new ArrayList<Integer>();
          var temp = new Storage(dynamic_fog_candidate, Environment.FOG_STORAGE_SIZE, 0, fog_stored_contents_list);
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

  static int set_nearest_dynamic_fog(ArrayList<Node_info> node_list, ArrayList<Storage> dynamic_fog_list, Point2D.Double current_node){
    double distance = 9999;//Initialze distance
    double temp_distance;
    Node_info dynamic_fog_node;
    int dynamic_fog_result = INIT;

    for(int i = 0; i < dynamic_fog_list.size(); i++){
      dynamic_fog_node = Node_mng.get_node_info(node_list, dynamic_fog_list.get(i).node_num);
      temp_distance = current_node.distance(dynamic_fog_node.point);
      if(distance > temp_distance){
        distance = temp_distance;
        dynamic_fog_result = dynamic_fog_list.get(i).node_num;
      }
    }
    return dynamic_fog_result;
  }

  static int get_dynamic_fog_index_num(ArrayList<Storage> dynamic_fog_list, int dynamic_fog_num){
    int dynamic_fog_index_num = INIT;

    for(int i = 0; i < dynamic_fog_list.size(); i++){
      if(dynamic_fog_list.get(i).node_num == dynamic_fog_num){
        dynamic_fog_index_num = i;
        break;
      }
    }
    if(dynamic_fog_index_num == INIT){
      System.out.println("Requested fog: " + dynamic_fog_num + " is Not Found.");
    }
    return dynamic_fog_index_num;
  }

  static int calc_used_capacity(ArrayList<Data> network_contents_list, ArrayList<Integer> fog_stored_contents_list){
    int used_capacity = 0;
    int file_num, file_index_num;
    
    for(int i = 0; i < fog_stored_contents_list.size(); i++){
      file_num = fog_stored_contents_list.get(i);
      file_index_num = Data_mng.get_index_num(network_contents_list, file_num);
      used_capacity += network_contents_list.get(file_index_num).file_size;
    }
    return used_capacity;
  }

  static void print_detail(ArrayList<Node_info> node_list, ArrayList<Storage> dynamic_fog_list){
    int dynamic_fogs_required;
    Storage node;

    dynamic_fogs_required = node_list.size() * Environment.DYNAMIC_FOG_RATIO_PERCENTAGE / 100;
    System.out.print(dynamic_fog_list.size() + " Dynamic Fog Node(s) exist (Minimum DF: " + dynamic_fogs_required + "), Dynamic Fog Node: ");
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      if(i != 0) System.out.print(", ");
      System.out.print(dynamic_fog_list.get(i).node_num);
    }
    System.out.println();

    try{
      for(int i = 0; i < dynamic_fog_list.size(); i++){
        node = dynamic_fog_list.get(i);
        System.out.println();
        System.out.println("Dynamic_Fog_index: " + i + ", Node_num: " + node.node_num);
        System.out.println("Total cap. " + node.total_capacity + ", Used cap. " + node.used_capacity);
        System.out.println("Cached Data Num: " + node.fog_stored_contents_list);
        System.out.println();
      }
    }
    catch(Exception e){
      System.out.println("No Dynamic Fog exist or Error has occured.");
    }
  }
}
