import java.util.Random;
import java.util.ArrayList;

public class Mode4 {
  private static final boolean DEBUG = true;
  private static final int MAX_NODES = 20;

  static void main(Random rand){
    var node_list = new ArrayList<Node_info>();
    var dynamic_fog_list = new ArrayList<Storage>();
    //var cache_data_list = new ArrayList<Data>();
    int node_leased = 0;
    int time_count;

    //Initialized Array on Dynamic_List
    for(int i = 0; i < MAX_NODES; i++){
      node_list.add(Node_mng.init(rand, node_list, node_leased, 1000, 1000, 0, 0));
      node_leased += 1;
    }

    time_count = 0;
    while(time_count < App.TIME_SEC){
      if((time_count % App.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Node_mng.dynamic_fog_set(rand, node_list, node_leased, dynamic_fog_list);

      for(int i = 0; i < node_list.size(); i++){
        Move.random_walk(node_list.get(i), rand);
        if(DEBUG) System.out.println("node"+ node_list.get(i).num + " (" + node_list.get(i).point.x + ", " + node_list.get(i).point.y + ")");
      }
      time_count += 1;
      if(DEBUG){
        System.out.println("");
        Node_mng.dynamic_fog_print_status(node_list, dynamic_fog_list);
      }
    }
  }
}