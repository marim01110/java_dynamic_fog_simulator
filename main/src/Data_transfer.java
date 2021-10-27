import java.util.ArrayList;
import java.util.Random;

public class Data_transfer {
  private static final boolean DEBUG = Settings.DEBUG;
  
  static void start(ArrayList<Integer> last_used, int time_count){
    boolean transfer;
    Node_info current_node, nearest_dynamic_fog = null;
    int reflesh_node, need_data_num;
    var near_dynamic_fogs_list = new ArrayList<Integer>();

    reflesh_node = time_count % Settings.CONTENTS_REFLESH_TIME;
    for(int i = 0; i < Environment.node_list.size(); i++){
      transfer = false;
      current_node = Environment.node_list.get(i);

      if(reflesh_node == current_node.data_refresh_time) transfer = true;

      if(transfer){
        need_data_num = Data_mng.select();
        Data_mng.update_delete_order(last_used, need_data_num);
        
        if(Settings.FOG_USE){
          near_dynamic_fogs_list = Fog_mng.search_near_dynamic_fogs(current_node);
          nearest_dynamic_fog = Node_mng.get_node_info(near_dynamic_fogs_list.get(0)); 
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
          search(near_dynamic_fogs_list, last_used, current_node, need_data_num);
        }
        Statistics.data_transfered += 1;
      }
    }
  }

  private static void search(ArrayList<Integer> near_dynamic_fogs_list, ArrayList<Integer> last_used, Node_info current_node, int need_data_num){
    double distance_df_edge;
    Data_info need_data = null;
    Node_info nearest_dynamic_fog = Node_mng.get_node_info(near_dynamic_fogs_list.get(0));
    boolean found_in_df = false, found_in_lan = false, data_downloaded = false;
    
    //Get need_data
    for(int i = 0; i < Environment.network_contents_list.size(); i++){
      if(need_data_num == Environment.network_contents_list.get(i).num){
        need_data = Environment.network_contents_list.get(i);
        break;
      }
    }

    if(need_data == null){
      Data_mng.create();
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
    if(need_data.hosted_by_list.size() > 0) found_in_lan = true;

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
              Node_info temp_dynamic_fog = Node_mng.get_node_info(near_dynamic_fogs_list.get(i));
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
            data_downloaded = search_with_copy_controled(last_used, nearest_dynamic_fog, current_node, need_data);
          }
        }
        else{
          //The requested data is not found in Local Network (DL from Cloud and send by bluetooth).
          Data_mng.update(last_used, nearest_dynamic_fog.num, need_data_num);//Maintainance required (2021/9/28 12:46 a.m.)
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
        data_downloaded = search_with_copy_controled(last_used, nearest_dynamic_fog, current_node, need_data);
      }
      else{
        //The requested data is not found in Local Network (DL from Cloud and send by cellular).
        Data_mng.update(last_used, nearest_dynamic_fog.num, need_data_num);//Maintainance required (2021/9/28 12:46 a.m.)
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

  private static boolean search_with_copy_controled(ArrayList<Integer> last_used, Node_info nearest_dynamic_fog, Node_info current_node, Data_info need_data){
    var rand = new Random();
    int need_data_hosted_by_total, dynamic_fog_total_nodes;
    Fog_info another_dynamic_fog = null;
    boolean data_downloaded = false;

    need_data_hosted_by_total = need_data.hosted_by_list.size();
    dynamic_fog_total_nodes = Environment.dynamic_fog_list.size();

    if(DEBUG) System.out.println("hosted by " + need_data_hosted_by_total + " nodes, Max dupulication is " + dynamic_fog_total_nodes * Settings.MAX_PERCENTAGE_OF_DUPLICATION / 100);

    //another_dynamic_fog = Fog_mng.get_fog_info(rand.nextInt(dynamic_fog_total_nodes));/* Set dynamic fog which has need_data */
    //System.out.println(another_dynamic_fog.node_num);

    if(need_data_hosted_by_total <= dynamic_fog_total_nodes * Settings.MAX_PERCENTAGE_OF_DUPLICATION / 100){
      Data_mng.update(last_used, nearest_dynamic_fog.num, need_data.num);//Maintainance required (2021/9/28 12:46 a.m.)
      /* Need to add power comsumption code */
      if(DEBUG) System.out.println("Dupulicated");
    }
    else{
      /* Need to add power comsumption code */
      if(DEBUG) System.out.println("Dupulication cancelled");
    }

    data_downloaded = true;
    Statistics.dl_from_local += 1;
    if(DEBUG) System.out.println("Data Copied from Local Network.");

    return data_downloaded;
  }
}
