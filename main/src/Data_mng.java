import java.util.Random;
import java.util.ArrayList;

public class Data_mng {
  private static final boolean DEBUG = Environment.DEBUG;
  private static final int INIT = -1;
  static int cache_data_total = 0;

  static Data_info get_data_info(ArrayList<Data_info> network_contents_list, int need_data_num){
    Data_info data = null;

    for(int i = 0; i < network_contents_list.size(); i++){
      if(need_data_num == network_contents_list.get(i).num){
        data = network_contents_list.get(i);
        break;
      }
    }
    if(data == null){
      System.out.println("Requested data: " + need_data_num + " is Not Found.");
    }

    return data;
  }

  static void fixed_init(ArrayList<Data_info> network_contents_list){
    for (int i = 0; i < Environment.CONTENTS_TYPES_MAX; i++) {
      create(network_contents_list);
    }
  }

  private static void create(ArrayList<Data_info> network_contents_list){
    Random rand = new Random();
    var hosted_by_list = new ArrayList<Integer>();
    int data_num, data_size, hosted_by_total;

    //Data Create Process
    data_num = cache_data_total;
    data_size = (rand.nextInt(39) + 1) * 5;
    hosted_by_total = 0;

    var temp_Data = new Data_info(data_num, data_size, hosted_by_total, hosted_by_list);
    network_contents_list.add(temp_Data);

    cache_data_total += 1;
  }

  private static void update(ArrayList<Fog_info> dynamic_fog_list, ArrayList<Data_info> network_contents_list, ArrayList<Integer> last_used, int dynamic_fog_num, int data_num){
    var hosted_by_list = new ArrayList<Integer>();
    var fog_stored_contents_list = new ArrayList<Integer>();
    Data_info data;
    Fog_info dynamic_fog;
    int loop_count = 0;
    int hosted_by_total, total_capacity, used_capacity;

    //Update network_contents_list Process
    data = get_data_info(network_contents_list, data_num);
    hosted_by_total = data.hosted_by_total + 1;
    for (int i = 0; i < data.hosted_by_list.size(); i++) {
      hosted_by_list.add(data.hosted_by_list.get(i));
    }
    hosted_by_list.add(dynamic_fog_num);

    dynamic_fog = Fog_mng.get_fog_info(dynamic_fog_list, dynamic_fog_num);

    //Update dynamic_fog_list Process
    try{
      for(int i = 0; i < dynamic_fog.fog_stored_contents_list.size(); i++){
        fog_stored_contents_list.add(dynamic_fog.fog_stored_contents_list.get(i));
      }
    }
    catch(Exception e){} //If fog_stored_contents_list is empty, do nothing.
    fog_stored_contents_list.add(data_num);
    total_capacity = dynamic_fog.total_capacity;
    used_capacity = Fog_mng.calc_used_capacity(network_contents_list, fog_stored_contents_list);

    //Check Used Capacity and remove files
    while(total_capacity < used_capacity){

      //Decide a file to delete and delete from fog_stored_contents_list
      delete_older_file(fog_stored_contents_list, network_contents_list, last_used, dynamic_fog_num);

      used_capacity = Fog_mng.calc_used_capacity(network_contents_list, fog_stored_contents_list);
      
      loop_count += 1;
      if(loop_count > 10){
        System.out.println("Storage management error.");
        System.out.println("Quit the program.");
        System.exit(-1);
      }
    }

    //Replace with new Info
    var temp_Data = new Data_info(data_num, data.file_size, hosted_by_total, hosted_by_list);
    network_contents_list.remove(data);
    network_contents_list.add(temp_Data);

    //Replace with new Dynamic_Fog Information
    dynamic_fog_list.remove(dynamic_fog);
    var temp_Storage = new Fog_info(dynamic_fog_num, total_capacity, used_capacity, fog_stored_contents_list);
    dynamic_fog_list.add(temp_Storage);
  }

  private static int select(){
    int need_data_num;
    Random rand = new Random();

    if(Environment.CONTENTS_TYPES_FIXED){
      need_data_num = rand.nextInt(Environment.CONTENTS_TYPES_MAX);
    }
    else{
      need_data_num = rand.nextInt(cache_data_total + 1);
    }
    return need_data_num;
  }

  static void transfer(ArrayList<Node_info> node_list, ArrayList<Fog_info> dynamic_fog_list, ArrayList<Data_info> network_contents_list, ArrayList<Integer> last_used, int time_count){
    boolean transfer;
    Node_info current_node, nearest_dynamic_fog = null;
    int need_data_num;
    var near_dynamic_fogs_list = new ArrayList<Integer>();

    for(int i = 0; i < node_list.size(); i++){
      transfer = false;
      current_node = node_list.get(i);

      if(time_count % current_node.data_refresh_time == 0) transfer = true;

      if(transfer){
        need_data_num = select();
        update_delete_order(network_contents_list, last_used, need_data_num);
        
        if(Environment.FOG_USE){
          near_dynamic_fogs_list = Fog_mng.search_near_dynamic_fogs(node_list, dynamic_fog_list, current_node);
          nearest_dynamic_fog = Node_mng.get_node_info(node_list, near_dynamic_fogs_list.get(0)); 
        }
        if(DEBUG){
          System.out.print("Node_num: " + current_node.num + ", Req. data: " + need_data_num);
          if(nearest_dynamic_fog != null) System.out.println(", Nearest DF: " + nearest_dynamic_fog.num);
          else System.out.println();
        }
          
        if(nearest_dynamic_fog == null){//Fog feature not used. so all files download from cloud.
          Node_mng.battery_drain(current_node.battery_remain_percentage, "cellular", "recv");
          Statistics.dl_from_cloud += 1;
          if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
        }
        else search(dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog, need_data_num);
  
        Statistics.data_transfered += 1;
      }
      else if(DEBUG) System.out.println("There is no data in Node_num " + current_node.num + " to be transferred this time.");
    }
  }

  private static void search(ArrayList<Fog_info> dynamic_fog_list, ArrayList<Data_info> network_contents_list, ArrayList<Integer> last_used, Node_info nearest_dynamic_fog, int need_data_num){
    boolean data_found = false;
    Data_info data = null;

    if(info_exist(network_contents_list, need_data_num) != true) create(network_contents_list);

    data = get_data_info(network_contents_list, need_data_num);

    //Check variable hosted_by_total.
    if(data.hosted_by_total == 0){
      update(dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog.num, need_data_num);
      data_found = true;
      Statistics.dl_from_cloud += 1;
      if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
    }

    //Search in the Nearest Dynamic Fog.
    if(data_found != true){
      for(int i = 0; i < data.hosted_by_list.size(); i++){
        if(nearest_dynamic_fog.num == data.hosted_by_list.get(i)){
          data_found = true;
          Statistics.dl_from_nearest_df += 1;
          if(DEBUG) System.out.println("Data was found in Nearest DF.");
          break;
        }
      }
    }

    //Copy from Local Network.
    if(data_found != true){
      update(dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog.num, need_data_num);
      data_found = true;
      Statistics.dl_from_local += 1;
      if(DEBUG) System.out.println("Data Copied from Local Network.");
    }

    if(data_found != true){
      System.out.println("Error: An unexpected error has occurred.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }
  }

  private static void search_new(ArrayList<Data_info> network_contents_list, Node_info current_node, Node_info nearest_dynamic_fog, int need_data_num){
    double distance_df_edge;
    Data_info need_data = null;
    boolean found_in_df = false, found_in_lan = false;
    
    //Get need_data
    for(int i = 0; i < network_contents_list.size(); i++){
      if(need_data_num == network_contents_list.get(i).num){
        need_data = network_contents_list.get(i);
        break;
      }
    }

    //Data search in the nearest Dynamic Fog
    for(int i = 0; i < need_data.hosted_by_list.size(); i++){
      if(nearest_dynamic_fog.num == need_data.hosted_by_list.get(i)){
        found_in_df = true;
        break;
      }
    }

    //Check distance the nearest DF and edge.
    distance_df_edge = current_node.point.distance(nearest_dynamic_fog.point);
    if(Environment.BT_CONNECTION_RANGE >= distance_df_edge){
      if(found_in_df){
        //The requested data is found in the nearest DF (and get by bluetooth).
        Node_mng.battery_drain(nearest_dynamic_fog.battery_remain_percentage, "bluetooth", "send");
        Node_mng.battery_drain(current_node.battery_remain_percentage, "bluetooth", "recv");
      }
      else{
        //Data search in Local Network
        if(need_data.hosted_by_total > 0) found_in_lan = true;
        if(found_in_lan){
          //Check distance the second nearest DF and edge.
          //Write the code which control file copy is another function.
          //copy_control();
        }
        else{
          //The requested data is not found in Local Network (DL from Cloud and send by bluetooth).
          Node_mng.battery_drain(nearest_dynamic_fog.battery_remain_percentage, "cellular", "recv");
          Node_mng.battery_drain(nearest_dynamic_fog.battery_remain_percentage, "bluetooth", "send");
          Node_mng.battery_drain(current_node.battery_remain_percentage, "bluetooth", "recv");
          Statistics.dl_from_cloud += 1;
          if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
        }
      }
    }
    else{
      /*if(the data in LAN){
        //Write the code which control file copy is another function.
        copy_control();
      /*}
      else{
        //The requested data is not found in Local Network (DL from Cloud and send by cellular).
        Node_mng.battery_drain(nearest_dynamic_fog.battery_remain_percentage, "cellular", "recv");
        Node_mng.battery_drain(nearest_dynamic_fog.battery_remain_percentage, "cellular", "send");
        Node_mng.battery_drain(current_node.battery_remain_percentage, "cellular", "recv");
        Statistics.dl_from_cloud += 1;
        if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
      }*/
    }
  }

  private static boolean info_exist(ArrayList<Data_info> network_contents_list, Integer data_num){
    boolean result = false;

    for(int i = 0; i < network_contents_list.size(); i++){
      if(network_contents_list.get(i).num == data_num){
        result = true;
      }
      if(result == true) break;
    }
    return result;
  }

  private static void update_delete_order(ArrayList<Data_info> network_contents_list, ArrayList<Integer> last_used, int data_num){
    for(int i = 0; i < last_used.size(); i++){
      if(last_used.get(i) == data_num){
        last_used.remove(i);
      }
    }
    last_used.add(data_num);
  }

  private static void delete_older_file(ArrayList<Integer> fog_stored_contents_list, ArrayList<Data_info> network_contents_list, ArrayList<Integer> last_used, int dynamic_fog_num){
    Data_info data;
    int delete_file_num = INIT;

    //if(DEBUG) System.out.println("Current delete order is " + last_used);

    // Delete from fog_stored_contents_list
    for(int i = 0; i < last_used.size(); i++){
      for(int j = 0; j < fog_stored_contents_list.size(); j++){
        if((last_used.get(i) - fog_stored_contents_list.get(j)) == 0){
          fog_stored_contents_list.remove(j);
          delete_file_num = last_used.get(i);
          break;
        }
      }
      if(delete_file_num != INIT) break;
    }
    if(delete_file_num == INIT){
      System.out.println("Error: Can not Find Delete file.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }

    //Delete from hosted_by_list
    data = get_data_info(network_contents_list, delete_file_num);
    for(int i = 0; i < data.hosted_by_list.size(); i++){
      if(data.hosted_by_list.get(i) == dynamic_fog_num){
        data.hosted_by_list.remove(i);
        break;
      }
    }
    data.hosted_by_total -= 1;
  }

  static void print_detail(ArrayList<Data_info> network_contents_list){
    Data_info data;

    try {
      for(int i = 0; i < network_contents_list.size(); i++){
        data = network_contents_list.get(i);
        System.out.println();
        System.out.println("Data num: " + data.num);
        System.out.println("Data size: " + data.file_size);
        System.out.println("Cached by total: " + data.hosted_by_total);
        System.out.println("Cached by: " + data.hosted_by_list);
        System.out.println();
      }
    }
    catch (Exception e) {
      System.out.println("No File exist or Error has occured.");
    }
  }
}
