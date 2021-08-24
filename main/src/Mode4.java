import java.util.ArrayList;

public class Mode4 {
  private static final boolean DEBUG = App.DEBUG;
  private static final int MAX_NODES = 30;

  static void main(){
    var node_list = new ArrayList<Node_info>();
    var dynamic_fog_list = new ArrayList<Storage>();
    var network_contents_list = new ArrayList<Data>();
    var last_used = new ArrayList<Integer>();
    int node_leased = 0;
    int time_count;
    int need_data_num;
    int nearest_dynamic_fog;

    //Initialized Array on Dynamic_List
    for(int i = 0; i < MAX_NODES; i++){
      node_list.add(Node_mng.generate(node_list, node_leased));
      node_leased += 1;
    }

    //Simuration Start
    time_count = 0;
    //if(App.CONTENTS_TYPES_FIXED) Data_mng.add_fixed();
    while(time_count < App.TIME){
      //Node Move Process
      if(App.FOG_USE){
        if((time_count % App.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Fog_mng.dynamic_fog_set(node_list, node_leased, dynamic_fog_list);
      }

      for(int i = 0; i < node_list.size(); i++){
        Move.random_walk(node_list.get(i));
        if(DEBUG) System.out.println("Node"+ node_list.get(i).num + " (" + node_list.get(i).point.x + ", " + node_list.get(i).point.y + ")");
      }
      time_count += 1;
      if(App.FOG_USE){
        if(DEBUG){
          System.out.println("");
          Fog_mng.print_detail(node_list, dynamic_fog_list);
        }
      }

      //Data Transfer Process
      for(int i = 0; i < node_list.size(); i++){
        need_data_num = Data_mng.select();
        Data_mng.update_delete_order(network_contents_list, last_used, need_data_num);
        
        if(App.FOG_USE){
          nearest_dynamic_fog = Fog_mng.set_nearest_dynamic_fog(node_list, dynamic_fog_list, node_list.get(i).point);
        }
        else{
          nearest_dynamic_fog = INIT;
        }
        if(DEBUG) System.out.println("Node_num: " + node_list.get(i).num + ", Req. data: " + need_data_num + ", Nearest DF: " + nearest_dynamic_fog);

        Data_mng.search(dynamic_fog_list, network_contents_list, last_used, nearest_dynamic_fog, need_data_num);
      }
    }
    Statistics.print_info();
  }
}