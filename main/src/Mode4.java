import java.util.Random;
import java.util.ArrayList;
import java.awt.geom.Point2D;

public class Mode4 {
  private static final boolean DEBUG = App.DEBUG;
  private static final int MAX_NODES = 2;

  static void main(){
    Random rand = new Random();
    var node_list = new ArrayList<Node_info>();
    var dynamic_fog_list = new ArrayList<Storage>();
    var cache_data_list = new ArrayList<Data>();
    int node_leased = 0;
    int time_count;
    int need_data_num;
    int nearest_dynamic_fog;
    boolean data_exist;

    //Initialized Array on Dynamic_List
    for(int i = 0; i < MAX_NODES; i++){
      node_list.add(Node_mng.init(node_list, node_leased, 1000, 1000, 0, 0));
      node_leased += 1;
    }

    time_count = 0;
    while(time_count < App.TIME){
      //Node Move Process
      if((time_count % App.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Node_mng.dynamic_fog_set(node_list, node_leased, dynamic_fog_list);

      for(int i = 0; i < node_list.size(); i++){
        Move.random_walk(node_list.get(i));
        if(DEBUG) System.out.println("Node"+ node_list.get(i).num + " (" + node_list.get(i).point.x + ", " + node_list.get(i).point.y + ")");
      }
      time_count += 1;
      if(DEBUG){
        System.out.println("");
        Node_mng.dynamic_fog_print_status(node_list, dynamic_fog_list);
      }

      //Data Transfer Process
      for(int i = 0; i < node_list.size(); i++){
        need_data_num = rand.nextInt(cache_data_list.size() + 1);
        if(DEBUG) System.out.println("Need data: " + need_data_num);
        
        nearest_dynamic_fog = Fog_mng.set_nearest_dynamic_fog(node_list, dynamic_fog_list, node_list.get(i).point);
        if(DEBUG) System.out.println("Node_num: " + node_list.get(i).num + ", Nearest DF: " + nearest_dynamic_fog);
/*
        data_exist = Fog_mng.data_exist(cache_data_list, need_data_num);
        if(data_exist == true){

        }
        if(data_exist == false){
          Fog_mng.data_add(cache_data_list, need_data_num);
        }
*/
      }
    }
  }
}