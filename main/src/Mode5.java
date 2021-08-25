import java.util.ArrayList;

public class Mode5 {
  private static final boolean DEBUG = App.DEBUG;
  private static final int MAX_NODES = 10;

  static void main(){
    var node_list = new ArrayList<Node_info>();
    var dynamic_fog_list = new ArrayList<Storage>();
    var network_contents_list = new ArrayList<Data>();
    var last_used = new ArrayList<Integer>();
    int node_leased = 0;
    int time_count;

    //Initialized Array on Dynamic_List
    for(int i = 0; i < MAX_NODES; i++){
      node_list.add(Node_mng.generate(node_list, node_leased));
      node_leased += 1;
    }

    //Simuration Start
    time_count = 0;
    if(App.CONTENTS_TYPES_FIXED) Data_mng.fixed_init(network_contents_list);

    while(time_count < App.TIME){
      if(App.FOG_USE){
        if((time_count % App.DYNAMIC_FOG_UPDATE_INTERVAL) ==  0) Fog_mng.dynamic_fog_set(node_list, node_leased, dynamic_fog_list);
      }

      //Node Move Process
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

      time_count += 1;
      if(App.FOG_USE){
        if(DEBUG){
          System.out.println("");
          Fog_mng.print_detail(node_list, dynamic_fog_list);
        }
      }

      //Data Transfer Process
      Data_mng.transfer(node_list, dynamic_fog_list, network_contents_list, last_used);

      System.out.println("Processed time_count " + time_count + " (" + time_count * 100 / App.TIME + "% done.)");
    }
    Statistics.print_info();
  }
}