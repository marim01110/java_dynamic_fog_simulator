public class Sim {
  private static final boolean DEBUG = Settings.DEBUG;
  private static int MAX_NODES = Settings.INIT_MAX_NODES;

  static void main(){
    boolean transfer;
    Node_info node;

    switch(Environment.mode){
      case 4:   Environment.init();
                Statistics.init();
                break;
      case 5:   Environment.init();
                Statistics.init();
                Environment.init_max_nodes_array();
                MAX_NODES = Environment.return_max_nodes();
                break;
      default:  System.out.println("Error: Invalid running_mode selected.");
                System.exit(-1);
                break;
    }

    /* Initialized Array on Dynamic_List */
    for(int i = 0; i < MAX_NODES; i++){
      Node_mng.spawn(Environment.node_leased);
      Environment.node_leased += 1;
    }

    if(Settings.CONTENTS_TYPES_FIXED) Data_mng.init_arraylist();

    /* Simuration Start */
    Environment.time_count = 0;

    while(Environment.time_count < Settings.SIM_TIME_HOURS * 3600){
      if(Settings.FOG_USE){
        if((Environment.time_count % Settings.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0){
          Fog_mng.register(Environment.node_leased);
        }
      }

      if(Environment.mode == 5){
        /* Change MAX_NODES value. */
        if(Environment.time_count % 3600 == 0) MAX_NODES = Environment.return_max_nodes();
      }
      
      /* Node replenishment. */
      while(Environment.node_list.size() < MAX_NODES){
        Node_mng.spawn(Environment.node_leased);
        Environment.node_leased += 1;
      }

      /* Node Keep_Alive Process (Including Move Process) */
      Node_mng.keep_alive();

      Environment.time_count += 1;
      if(Settings.FOG_USE){
        if(DEBUG){
          System.out.println("");
          Fog_mng.print_detail();
        }
      }

      /* Data Transfer Process */
      for(int i = 0, size = Environment.node_list.size(); i < size; i++){
        node = Environment.node_list.get(i);
        transfer = Data_transfer.check_contents(node);
        if(transfer) Data_transfer.main(node);
      }
      Data_mng.valid_check();

      int time_count_hour = Environment.time_count / 3600 + Settings.START_FROM;
      int time_count_min = Environment.time_count % 3600 / 60;

      if(!DEBUG) System.out.print("\033[H\033[2J");
      System.out.print("Processed time_count " + Environment.time_count + " (" +  Environment.time_count * 100 / (Settings.SIM_TIME_HOURS * 3600) + "% done.)" + ", Current time in Sim: " + time_count_hour + ":" + time_count_min /* + ", Active nodes: " + Environment.node_list.size() */);
      if(DEBUG) System.out.println();
    }
    Statistics.print_info();
  }
}