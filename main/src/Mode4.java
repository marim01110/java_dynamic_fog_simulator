public class Mode4 {
  private static final boolean DEBUG = Settings.DEBUG;
  private static int MAX_NODES = Settings.INIT_MAX_NODES;

  static void main(){
    boolean transfer;
    Node_info node;

    //Initialized Array on Dynamic_List
    for(int i = 0; i < MAX_NODES; i++){
      Node_mng.spawn(Environment.node_leased);
      Environment.node_leased += 1;
    }

    //Simuration Start
    Environment.time_count = 0;

    while(Environment.time_count < Settings.SIM_TIME){
      if(Settings.FOG_USE){
        if((Environment.time_count % Settings.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Fog_mng.register(Environment.node_leased);
      }

      //Node Move Process
      for(int i = 0; i < Environment.node_list.size(); i++){
        Move.random_walk(Environment.node_list.get(i));
        if(DEBUG) System.out.println("Node"+ Environment.node_list.get(i).num + " (" + Environment.node_list.get(i).point.x + ", " + Environment.node_list.get(i).point.y + ")");
      }
      
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
      Data_mng.valid_check();

      if(!DEBUG) System.out.print("\033[H\033[2J");
      System.out.println("Processed time_count " + Environment.time_count + " (" + Environment.time_count * 100 / Settings.SIM_TIME + "% done.)");
      if(DEBUG) System.out.println();
    }
    Statistics.print_info();
  }
}