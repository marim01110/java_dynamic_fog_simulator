import java.util.ArrayList;

public class Mode5 {
  private static final boolean DEBUG = Settings.DEBUG;
  private static int MAX_NODES = Settings.INIT_MAX_NODES;

  static void main(){
    var last_used = new ArrayList<Integer>();
    int stage = 0;
    int node_leased = 0;

    //Initialized Array on Dynamic_List
    for(int i = 0; i < MAX_NODES; i++){
      Environment.node_list.add(Node_mng.spawn(node_leased));
      node_leased += 1;
    }

    //Simuration Start
    Environment.time_count = 0;

    while(Environment.time_count < Settings.SIM_TIME){
      if(Settings.FOG_USE){
        /* Dynamic node Scan */
        //Fog_mng.keep_alive();
        if((Environment.time_count % Settings.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Fog_mng.register(node_leased);
      }
      if(Settings.CONTENTS_TYPES_FIXED) Data_mng.fixed_respawn();

      //Change MAX_NODES value.
      stage = Environment.change_stages(Environment.time_count, stage);
      if(stage != 0){
        MAX_NODES = Environment.return_max_nodes(stage);
      }

      //Node replenishment.
      while(Environment.node_list.size() < MAX_NODES){
        Environment.node_list.add(Node_mng.spawn(node_leased));
        node_leased += 1;
      }

      //Node Move Process
      for(int i = 0; i < Environment.node_list.size(); i++){
        if(Environment.node_list.get(i).reached == false){
          if(Environment.node_list.get(i).goal_nearby == false){
            Move.start(Environment.node_list.get(i));
          }
          else if(Environment.node_list.get(i).goal_nearby == true){
            Node_mng.check_reach_goal(Environment.node_list.get(i));
          }
          if(DEBUG) System.out.println("Node "+ Environment.node_list.get(i).num + " (" + Environment.node_list.get(i).point.x + ", " + Environment.node_list.get(i).point.y + ")");
        }

        if(Environment.node_list.get(i).reached == true) {
          Fog_mng.dynamic_fog_dead_judge(i);
          if(DEBUG) System.out.println("Node " + Environment.node_list.get(i).num + " is now deleteing.");
          Environment.node_list.remove(i);
          i -= 1;
        }
      }

      Environment.time_count += 1;
      if(Settings.FOG_USE){
        if(DEBUG){
          System.out.println("");
          Fog_mng.print_detail();
        }
      }

      //Data Transfer Process
      Data_transfer.start(last_used, Environment.time_count);
      Data_mng.valid_check();

      System.out.println("Processed time_count " + Environment.time_count + " (" + Environment.time_count * 100 / Settings.SIM_TIME + "% done.)");
    }
    Statistics.print_info();
  }
}