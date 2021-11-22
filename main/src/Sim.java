public class Sim {
  private static final boolean DEBUG = Settings.DEBUG;
  private static int MAX_NODES = Settings.INIT_MAX_NODES;

  static void main(){
    boolean transfer;
    Node_info node;

    switch(Environment.mode){
      case 4:   break;
      case 5:   break;
      default:  System.out.println("Error: Invalid running_mode selected.");
                System.exit(-1);
                break;
    }

    //Initialized Array on Dynamic_List
    for(int i = 0; i < MAX_NODES; i++){
      Node_mng.spawn(Environment.node_leased);
      Environment.node_leased += 1;
    }

    //Simuration Start
    Environment.time_count = 0;

    while(Environment.time_count < Settings.SIM_TIME){
      if(Settings.FOG_USE){
        if((Environment.time_count % Settings.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0){
          Fog_mng.register(Environment.node_leased);
        }
      }

      if(Environment.mode == 5){
        //Change MAX_NODES value.
        Environment.stage = Environment.change_stages(Environment.time_count, Environment.stage);
        if(Environment.stage != 0){
          MAX_NODES = Environment.return_max_nodes(Environment.stage);
        }
      }
      
      //Node replenishment.
      while(Environment.node_list.size() < MAX_NODES){
        Node_mng.spawn(Environment.node_leased);
        Environment.node_leased += 1;
      }

      //Node Keep_Alive Process (Including Move Process)
      Node_mng.keep_alive();

      Environment.time_count += 1;
      if(Settings.FOG_USE){
        if(DEBUG){
          System.out.println("");
          Fog_mng.print_detail();
        }
      }

      //Data Transfer Process
      for(int i = 0; i < Environment.node_list.size(); i++){
        node = Environment.node_list.get(i);
        transfer = Data_transfer.check_contents(node);
        if(transfer) Data_transfer.main(node);
      }
      Data_mng.valid_check();

      if(!DEBUG) System.out.print("\033[H\033[2J");
      System.out.print("Processed time_count " + Environment.time_count + " (" +  Environment.time_count * 100 / Settings.SIM_TIME + "% done.)");
      if(DEBUG) System.out.println();
    }
    Statistics.print_info();
  }
}