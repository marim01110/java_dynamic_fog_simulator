import java.util.Random;

public class Mode4 {
  private static final boolean DEBUG = true;
  private static final int MAX_NODES = 1;

  static void main(Random rand){
    //List<Node_info> node_list = new ArrayList<>();
    Node_info[] node_array = new Node_info[MAX_NODES];
    int node_leased = 0;
    int time_count = 0;

    //Initialized Array
    for(int i=0; i<MAX_NODES; i++){
      node_array[i] = new Node_info();
      node_leased = Node_mng.init(rand, node_leased, node_array[node_leased], 1000, 1000, 0, 0);
    }

    while(time_count < App.time_sec){
      for(int i=0; i<node_leased; i++){
        Move.random_walk(node_array[i], rand);
        if(DEBUG) System.out.println("node"+ node_array[i].num + " (" + node_array[i].point.x + ", " + node_array[i].point.y + ")");
      }
      time_count += 1;
    }
  }
}