import java.util.ArrayList;
//import java.util.Random;

public class Data_transfer {
  private static final boolean DEBUG = Settings.DEBUG;
  
  static void start(ArrayList<Node_info> node_list, ArrayList<Fog_info> dynamic_fog_list, ArrayList<Data_info> network_contents_list, ArrayList<Integer> last_used, int time_count){
    boolean transfer;
    Node_info current_node, nearest_dynamic_fog = null;
    int reflesh_node, need_data_num;
    var near_dynamic_fogs_list = new ArrayList<Integer>();

    reflesh_node = time_count % Settings.CONTENTS_REFLESH_TIME;
    for(int i = 0; i < node_list.size(); i++){
      transfer = false;
      current_node = node_list.get(i);

      if(reflesh_node == current_node.data_refresh_time) transfer = true;

      if(transfer){
        need_data_num = Data_mng.select();
        Data_mng.update_delete_order(network_contents_list, last_used, need_data_num);
        
        if(Settings.FOG_USE){
          near_dynamic_fogs_list = Fog_mng.search_near_dynamic_fogs(node_list, dynamic_fog_list, current_node);
          nearest_dynamic_fog = Node_mng.get_node_info(node_list, near_dynamic_fogs_list.get(0)); 
        }
        if(DEBUG){
          System.out.print("Node_num: " + current_node.num + ", Req. data: " + need_data_num);
          if(nearest_dynamic_fog != null) System.out.println(", Nearest DF: " + nearest_dynamic_fog.num);
          else System.out.println();
        }
          
        if(nearest_dynamic_fog == null){//Fog feature not used. so all files download from cloud.
          Node_mng.battery_drain(current_node, "cellular", "recv");
          Statistics.dl_from_cloud += 1;
          if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
        }
        else{
          search(node_list, dynamic_fog_list, network_contents_list, near_dynamic_fogs_list, last_used, current_node, need_data_num);
        }
        Statistics.data_transfered += 1;
      }
      //else if(DEBUG) System.out.println("There is no data in Node_num " + current_node.num + " to be transferred this time.");
    }
  }

  private static void search(ArrayList<Node_info> node_list, ArrayList<Fog_info> dynamic_fog_list, ArrayList<Data_info> network_contents_list, ArrayList<Integer> near_dynamic_fogs_list, ArrayList<Integer> last_used, Node_info current_node, int need_data_num){
    double distance_df_edge;
    Data_info need_data = null;
    Node_info nearest_dynamic_fog = Node_mng.get_node_info(node_list, near_dynamic_fogs_list.get(0));
    boolean found_in_df = false, found_in_lan = false, data_downloaded = false;
    
    //Get need_data
    for(int i = 0; i < network_contents_list.size(); i++){
      if(need_data_num == network_contents_list.get(i).num){
        need_data = network_contents_list.get(i);
        break;
      }
    }

    if(need_data == null){
      Data_mng.create(network_contents_list);
      System.out.println("ERROR!! YEAH!");
    }

    //Data search in the nearest Dynamic Fog
    for(int i = 0; i < need_data.hosted_by_list.size(); i++){
      if(nearest_dynamic_fog.num == need_data.hosted_by_list.get(i)){
        found_in_df = true;
        break;
      }
    }

    //Data search in Local Network
    if(need_data.hosted_by_total > 0) found_in_lan = true;

    //Check distance the nearest DF and edge.
    distance_df_edge = current_node.point.distance(nearest_dynamic_fog.point);
    if(Settings.BT_CONNECTION_RANGE >= distance_df_edge){
      if(found_in_df){
        //The requested data is found in the nearest DF (and get by bluetooth).
        Node_mng.battery_drain(nearest_dynamic_fog, "bluetooth", "send");//UL to Edge.
        Node_mng.battery_drain(current_node, "bluetooth", "recv");//DL from DF.
        data_downloaded = true;
        Statistics.dl_from_nearest_df += 1;
        if(DEBUG) System.out.println("Data was found in Nearest DF.");
      }
      else{
        if(found_in_lan){
          if(near_dynamic_fogs_list.size() > 1){
            //Search Dynamic Fogs which in BT connection's range.
            for(int i = 0; i < near_dynamic_fogs_list.size(); i++){
              Node_info temp_dynamic_fog = Node_mng.get_node_info(node_list, near_dynamic_fogs_list.get(i));
              //Data search in the Dynamic Fog
              for(int j = 0; j < need_data.hosted_by_list.size(); j++){
                if(temp_dynamic_fog.num == need_data.hosted_by_list.get(j)){
                  found_in_df = true;
                  break;
                }
              }
              if(found_in_df){
                //The requested data is found in the neighbor DF (and get by bluetooth).
                Node_mng.battery_drain(nearest_dynamic_fog, "bluetooth", "send");//UL to Edge.
                Node_mng.battery_drain(current_node, "bluetooth", "recv");//DL from DF.
                data_downloaded = true;
                Statistics.dl_from_nearest_df += 1;
                if(DEBUG) System.out.println("Data was found in Nearest DF.");
              }
              if(data_downloaded == true) break;
            }
          }
          if(data_downloaded != true){
            data_downloaded = search_with_copy_controled(node_list, dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog, current_node, need_data);
          }
        }
        else{
          //The requested data is not found in Local Network (DL from Cloud and send by bluetooth).
          Data_mng.update(dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog.num, need_data_num);//Maintainance required (2021/9/28 12:46 a.m.)
          Node_mng.battery_drain(nearest_dynamic_fog, "cellular", "recv");//DL from Cloud.
          Node_mng.battery_drain(nearest_dynamic_fog, "bluetooth", "send");//UL to Edge.
          Node_mng.battery_drain(current_node, "bluetooth", "recv");//DL from DF.
          data_downloaded = true;
          Statistics.dl_from_cloud += 1;
          if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
        }
      }
    }
    else{
      if(found_in_lan){
        data_downloaded = search_with_copy_controled(node_list, dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog, current_node, need_data);
      }
      else{
        //The requested data is not found in Local Network (DL from Cloud and send by cellular).
        Data_mng.update(dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog.num, need_data_num);//Maintainance required (2021/9/28 12:46 a.m.)
        Node_mng.battery_drain(nearest_dynamic_fog, "cellular", "recv");//DL from Cloud.
        Node_mng.battery_drain(nearest_dynamic_fog, "cellular", "send");//UL to Edge.
        Node_mng.battery_drain(current_node, "cellular", "recv");//DL from DF.
        data_downloaded = true;
        Statistics.dl_from_cloud += 1;
        if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
      }
    }

    if(data_downloaded != true){
      System.out.println("Error: An unexpected error has occurred.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }
  }

  private static boolean search_with_copy_controled(ArrayList<Node_info> node_list, ArrayList<Fog_info> dynamic_fog_list, ArrayList<Data_info> network_contents_list, ArrayList<Integer> last_used, Node_info nearest_dynamic_fog, Node_info current_node, Data_info need_data){
    //var rand = new Random();
    //Node_info dynamic_fog_has_data_node_info;
    //Fog_info dynamic_fog_has_data_fog_info;
    boolean data_downloaded = false;
    //int dynamic_fog_has_data_num;

    //Search DF which has requested data
/*
    System.out.println(need_data.hosted_by_list);
    dynamic_fog_has_data_num = need_data.hosted_by_list.get(rand.nextInt(need_data.hosted_by_total));
    System.out.println(dynamic_fog_has_data_num);
    dynamic_fog_has_data_fog_info = Fog_mng.get_fog_info(dynamic_fog_list, dynamic_fog_has_data_num);
    dynamic_fog_has_data_node_info = Node_mng.get_node_info(node_list, dynamic_fog_has_data_fog_info.node_num);
    
    //Data copy from Local Network to nearest Dynamic Fog
    Node_mng.battery_drain(dynamic_fog_has_data_node_info, "cellular", "send");//UL to the nearest DF.
    //A bug found. (2021/9/28 2:32 a.m.)
    */
    Node_mng.battery_drain(nearest_dynamic_fog, "cellular", "recv");//DL from DF on Local Network.

    Data_mng.update(dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog.num, need_data.num);//Maintainance required (2021/9/28 12:46 a.m.)

    //Data transfer to Edge
    Node_mng.battery_drain(nearest_dynamic_fog, "bluetooth", "send");//UL to Edge.
    Node_mng.battery_drain(current_node, "bluetooth", "recv");//DL from DF.
    data_downloaded = true;
    Statistics.dl_from_local += 1;
    if(DEBUG) System.out.println("Data Copied from Local Network.");

    return data_downloaded;
  }
}
