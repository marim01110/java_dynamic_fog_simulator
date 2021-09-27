import java.util.ArrayList;

public class Data_transfer {
  private static final boolean DEBUG = Environment.DEBUG;
  
  static void start(ArrayList<Node_info> node_list, ArrayList<Fog_info> dynamic_fog_list, ArrayList<Data_info> network_contents_list, ArrayList<Integer> last_used, int time_count){
    boolean transfer;
    Node_info current_node, nearest_dynamic_fog = null;
    int need_data_num;
    var near_dynamic_fogs_list = new ArrayList<Integer>();

    for(int i = 0; i < node_list.size(); i++){
      transfer = false;
      current_node = node_list.get(i);

      if(time_count % current_node.data_refresh_time == 0) transfer = true;

      if(transfer){
        need_data_num = Data_mng.select();
        Data_mng.update_delete_order(network_contents_list, last_used, need_data_num);
        
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

    if(Data_mng.info_exist(network_contents_list, need_data_num) != true) Data_mng.create(network_contents_list);

    data = Data_mng.get_data_info(network_contents_list, need_data_num);

    //Check variable hosted_by_total.
    if(data.hosted_by_total == 0){
      Data_mng.update(dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog.num, need_data_num);
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
      Data_mng.update(dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog.num, need_data_num);
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

  private static void search_beta(ArrayList<Data_info> network_contents_list, Node_info current_node, Node_info nearest_dynamic_fog, int need_data_num){
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
}
