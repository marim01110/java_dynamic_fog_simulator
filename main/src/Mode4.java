import java.util.Random;
import java.util.ArrayList;

public class Mode4 {
  private static final boolean DEBUG = false;
  private static final int MAX_NODES = 1;

  static void main(Random rand){
    Node_info[] node_array = new Node_info[MAX_NODES];
    ArrayList<Integer> dynamic_fog_list = new ArrayList<>();
    int node_leased = 0;
    int time_count = 0;

    //Initialized Array
    for(int i=0; i<MAX_NODES; i++){
      node_array[i] = new Node_info();
      node_leased = Node_mng.init(rand, node_leased, node_array[node_leased], 1000, 1000, 0, 0);
    }

    while(time_count < App.TIME_SEC){
      Node_mng.dynamic_fog_dead_judge(node_array, dynamic_fog_list, node_leased);
      if((time_count % App.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Node_mng.dynamic_fog_set(rand, dynamic_fog_list, node_leased);
      for(int i=0; i<node_leased; i++){
        Move.random_walk(node_array[i], rand);
        if(DEBUG) System.out.println("node"+ node_array[i].num + " (" + node_array[i].point.x + ", " + node_array[i].point.y + ")");
      }
      time_count += 1;
    }
  }
}