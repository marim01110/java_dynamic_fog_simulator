import java.util.Random;

public class Mode4 {
  static void main(Random rand){
    int MAX_NODES = 2;
    //List<Node_info> node_list = new ArrayList<>();
    Node_info[] node_array = new Node_info[MAX_NODES];
    int node_leased = 0;
    //Initialized Array
    for(int i=0; i<node_array.length; i++) node_array[i] = new Node_info();
    node_leased = Node_mng.init(rand, node_leased, node_array[node_leased], 1000, 1000, 0, 0);
    node_leased = Node_mng.init(rand, node_leased, node_array[node_leased], 1000, 1000, 0, 0);
    int count = 0;
    while(count < App.time_sec){
      Move.random_walk(node_array[0], rand);
      Move.random_walk(node_array[1], rand);
      count += 1;
    }
  }
}