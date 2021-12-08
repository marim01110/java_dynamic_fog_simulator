import java.util.ArrayList;

public class Data_transfer {
  private static final boolean DEBUG = Settings.DEBUG;
  private static final int INIT = -1;

  static boolean check_contents(Node_info node){
    boolean transfer = false;
    int reflesh_node;

    reflesh_node = Environment.time_count % Settings.CONTENTS_RETRIEVE_FREQUENCY;

    if(reflesh_node == node.data_refresh_time) transfer = true;
    return transfer;
  }

  static void main(Node_info node){
    int need_data_num;
    Node_info nearest_dynamic_fog = null;
    var near_dynamic_fogs_list = new ArrayList<Near_DFs>();
    
    need_data_num = Data_mng.select();
    Data_mng.update_delete_order(need_data_num);

    if(Settings.FOG_USE){
      near_dynamic_fogs_list = Fog_mng.scan_near_dynamic_fogs(node);
      nearest_dynamic_fog = near_dynamic_fogs_list.get(0).dynamic_fog; 
    }
    if(DEBUG){
      System.out.print("Node_num: " + node.num + ", Req. data: " + need_data_num);
      if(nearest_dynamic_fog != null) System.out.println(", Nearest DF: " + nearest_dynamic_fog.num);
      else System.out.println();
    }

    if(nearest_dynamic_fog == null){//Fog feature not used. so all files download from cloud.
      Node_mng.battery_drain(node, "cellular", "recv");
      Statistics.dl_from_cloud += 1;
      Statistics.data_transfered += 1;
      if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
    }
    else{
      start(near_dynamic_fogs_list, node, need_data_num);
    }
  }

  private static void start(ArrayList<Near_DFs> near_dynamic_fogs_list, Node_info request_node, int need_data_num){
    Data_info need_data = null;
    Node_info nearest_dynamic_fog = null;
    Node_info sender_node = null;// Maintainance required (2021/11/17 8:16 p.m.)
    boolean bluetooth_range = false, wifi_range = false, found_in_df = false, found_in_lan = false, data_downloaded = false;
    int nearest_dynamic_fog_index_num = INIT;
    double distance;

    /* Get need_data information */
    need_data = Data_mng.get_data_info(need_data_num, false);
    if(need_data == null){
      /* Create new data */
      int temp = Data_mng.create();
      if(DEBUG) System.out.println("Data created.");
      need_data = Data_mng.get_data_info(temp, true);
    }

    /* Data search in the nearest Dynamic Fog */
    for(int i = 0; i < need_data.hosted_by_list.size(); i++){
      for(int j = 0; j < near_dynamic_fogs_list.size(); j++){
        if(need_data.hosted_by_list.get(i) == near_dynamic_fogs_list.get(j).dynamic_fog.num){
          found_in_df = true;
          nearest_dynamic_fog_index_num = j;
          break;
        }
      }
      if(found_in_df == true) break;
    }
    if(nearest_dynamic_fog_index_num == INIT) nearest_dynamic_fog_index_num = 0;

    /* Load Dynamic_Fog info and distance */
    nearest_dynamic_fog = near_dynamic_fogs_list.get(nearest_dynamic_fog_index_num).dynamic_fog;
    distance = near_dynamic_fogs_list.get(nearest_dynamic_fog_index_num).distance;

    if(Settings.BLUETOOTH_USE){
      if(distance <= Settings.BT_CONNECTION_RANGE) bluetooth_range = true;
    }
    else bluetooth_range = false;
    if(Settings.WIFI_USE){
      if(distance <= Settings.WIFI_CONNECTION_RANGE) wifi_range = true;
    }
    else wifi_range = false;

    if(found_in_df){
      if(bluetooth_range){
        from_nearest_df_bluetooth(nearest_dynamic_fog, request_node);
        data_downloaded = true;
        Statistics.dl_from_near_df_bluetooth += 1;
        if(DEBUG) System.out.println("Data was downloaded from the Nearest DF by Bluetooth.");
      }
      else if(wifi_range){
        from_local_wifi(nearest_dynamic_fog, request_node);
        data_downloaded = true;
        Statistics.dl_from_near_df_wifi += 1;
        if(DEBUG) System.out.println("Data was downloaded from the Nearest DF by Wi-Fi.");
      }
      else{
        from_local_cellular(nearest_dynamic_fog, request_node);
        data_downloaded = true;
        Statistics.dl_from_near_df_cell += 1;
        if(DEBUG) System.out.println("Data was downloaded from the Nearest DF by Cellular.");
      }
    }

    if(data_downloaded == false){
      if(need_data.hosted_by_list.size() > 0){
        found_in_lan = true;
        sender_node = Node_mng.get_node_info(need_data.hosted_by_list.get(0));
      }
    }

    if(found_in_lan){
      if(bluetooth_range){
        from_local_cellular(sender_node, nearest_dynamic_fog);
        Data_mng.update(nearest_dynamic_fog.num, need_data_num);//Maintainance required (2021/9/28 12:46 a.m.)
        from_nearest_df_bluetooth(nearest_dynamic_fog, request_node);
        data_downloaded = true;
        Statistics.dl_from_local += 1;
        if(DEBUG) System.out.println("Data was downloaded from Local Network and copied to the Nearest DF.");
      }
      else{
        if(copy_control(need_data)){
          from_local_cellular(sender_node, nearest_dynamic_fog);
          Data_mng.update(nearest_dynamic_fog.num, need_data_num);//Maintainance required (2021/9/28 12:46 a.m.)
          from_local_cellular(nearest_dynamic_fog, request_node);
        }
        else from_local_cellular(sender_node, request_node);
        data_downloaded = true;
        Statistics.dl_from_local += 1;
        if(DEBUG) System.out.println("Data was downloaded from Local Network.");
      }
    }

    if(data_downloaded == false){
      from_cloud_cellular(nearest_dynamic_fog);
      Statistics.data_size_via_internet_proposed += need_data.file_size;
      Data_mng.update(nearest_dynamic_fog.num, need_data_num);//Maintainance required (2021/9/28 12:46 a.m.)
      if(bluetooth_range){
        from_nearest_df_bluetooth(nearest_dynamic_fog, request_node);
        data_downloaded = true;
        Statistics.dl_from_cloud += 1;
        if(DEBUG) System.out.println("Data was downloaded from Cloud and copied to the Nearest DF.");
      }
      else{
        from_local_cellular(nearest_dynamic_fog, request_node);
        data_downloaded = true;
        Statistics.dl_from_cloud += 1;
        if(DEBUG) System.out.println("Data was downloaded from Cloud and copied to the Nearest DF.");
      }
    }

    if(data_downloaded != true){
      System.out.println("Error: An unexpected error has occurred.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }

    Statistics.data_size_via_internet_conventional += need_data.file_size;
    Statistics.for_calc_latency_conventional += Settings.RTT_CLOUD;
    Statistics.data_transfered += 1;
  }

  private static void from_nearest_df_bluetooth(Node_info sender_node, Node_info request_node){
    Node_mng.battery_drain(sender_node, "bluetooth", "send");//UL to Edge.
    Node_mng.battery_drain(request_node, "bluetooth", "recv");//DL from DF.
    Statistics.for_calc_latency_proposed += Settings.RTT_DIRECT_BLUETOOTH;
  }

  private static void from_local_wifi(Node_info sender_node, Node_info request_node){
    Node_mng.battery_drain(sender_node, "wifi", "send");// UL to Edge.
    Node_mng.battery_drain(request_node, "wifi", "recv");// DL from Local.
    Statistics.for_calc_latency_proposed += Settings.RTT_DIRECT_WIFI;
  }

  private static void from_local_cellular(Node_info sender_node, Node_info request_node){
    Node_mng.battery_drain(sender_node, "cellular", "send");//UL to Edge.
    Node_mng.battery_drain(request_node, "cellular", "recv");//DL from DF.
    Statistics.for_calc_latency_proposed += Settings.RTT_DIRECT_CELLULAR;
  }

  private static void from_cloud_cellular(Node_info request_node){
    Node_mng.battery_drain(request_node, "cellular", "recv");//DL from DF.
    Statistics.for_calc_latency_proposed += Settings.RTT_CLOUD;
  }

  private static boolean copy_control(Data_info need_data){
    boolean copy = false;
    int need_data_hosted_by_total, dynamic_fog_total_nodes;

    need_data_hosted_by_total = need_data.hosted_by_list.size();
    dynamic_fog_total_nodes = Environment.dynamic_fog_list.size();

    if(DEBUG) System.out.println("hosted by " + need_data_hosted_by_total + " nodes, Max dupulication is " + dynamic_fog_total_nodes * Settings.MAX_PERCENTAGE_OF_DUPLICATION / 100);

    if(need_data_hosted_by_total <= dynamic_fog_total_nodes * Settings.MAX_PERCENTAGE_OF_DUPLICATION / 100) copy = true;
    else copy = false;

    return copy;
  }
}
