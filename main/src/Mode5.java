import java.util.ArrayList;

public class Mode5 {
  private static final boolean DEBUG = Settings.DEBUG;
  private static int MAX_NODES = Settings.INIT_MAX_NODES;

  static void main(){
    var node_list = new ArrayList<Node_info>();
    var dynamic_fog_list = new ArrayList<Fog_info>();
    var network_contents_list = new ArrayList<Data_info>();
    var last_used = new ArrayList<Integer>();
    int stage = 0;
    int node_leased = 0;

    //Initialized Array on Dynamic_List
    for(int i = 0; i < MAX_NODES; i++){
      node_list.add(Node_mng.spawn(node_leased));
      node_leased += 1;
    }

    //Simuration Start
    Environment.time_count = 0;
    if(Settings.CONTENTS_TYPES_FIXED) Data_mng.fixed_init(network_contents_list);

    while(Environment.time_count < Settings.SIM_TIME){
      if(Settings.FOG_USE){
        if((Environment.time_count % Settings.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Fog_mng.dynamic_fog_set(node_list, node_leased, dynamic_fog_list);
      }

      //Change MAX_NODES value.
      stage = Environment.change_stages(Environment.time_count, stage);
      if(stage != 0){
        MAX_NODES = Environment.return_max_nodes(stage);
      }

      //Node replenishment.
      while(node_list.size() < MAX_NODES){
        node_list.add(Node_mng.spawn(node_leased));
        node_leased += 1;
      }

      //Node Move Process
      for(int i = 0; i < node_list.size(); i++){
        if(node_list.get(i).reached == false){
          if(node_list.get(i).goal_nearby == false){
            Move.decide_direction(node_list.get(i));
            Node_mng.check_reach_goal(node_list.get(i));
          }
          else if(node_list.get(i).goal_nearby == true){
            Node_mng.check_reach_goal(node_list.get(i));
          }
          if(DEBUG) System.out.println("Node "+ node_list.get(i).num + " (" + node_list.get(i).point.x + ", " + node_list.get(i).point.y + ")");
        }

        if(node_list.get(i).reached == true) {
          Fog_mng.dynamic_fog_dead_judge(node_list, i, dynamic_fog_list);
          if(DEBUG) System.out.println("Node " + node_list.get(i).num + " is now deleteing.");
          node_list.remove(i);
          i -= 1;
        }
      }

      Environment.time_count += 1;
      if(Settings.FOG_USE){
        if(DEBUG){
          System.out.println("");
          Fog_mng.print_detail(node_list, dynamic_fog_list);
        }
      }

      //Data Transfer Process
      Data_transfer.start(node_list, dynamic_fog_list, network_contents_list, last_used, Environment.time_count);
      Data_mng.valid_check(network_contents_list);

      System.out.println("Processed time_count " + Environment.time_count + " (" + Environment.time_count * 100 / Settings.SIM_TIME + "% done.)");
    }
    Statistics.print_info();
  }
}