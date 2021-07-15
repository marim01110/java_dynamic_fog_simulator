import java.util.ArrayList;

public class Mode5 {
  private static final boolean DEBUG = App.DEBUG;

  private static final int MAX_NODES = 5;

  static void main(){
    var node_list = new ArrayList<Node_info>();
    var dynamic_fog_list = new ArrayList<Storage>();
    //var cache_data_list = new ArrayList<Data>();
    int node_leased = 0;
    int time_count;

    //Put Nodes on the Map
    Node_mng.init();
    for(int i = 0; i < MAX_NODES; i++){
      node_list.add(Node_mng.generate(node_list, node_leased));
      //node_list.add(Node_mng.put(node_list, node_leased, MAX_GOALS, goals_array));
      node_leased += 1;
    }

    //Simuration Start
    time_count = 0;
    while(time_count < App.TIME){
      if((time_count % App.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Fog_mng.dynamic_fog_set(node_list, node_leased, dynamic_fog_list);

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

      if(DEBUG){
        System.out.println("");
        Fog_mng.dynamic_fog_print_status(node_list, dynamic_fog_list);
      }
      time_count += 1;
    }
  }
}