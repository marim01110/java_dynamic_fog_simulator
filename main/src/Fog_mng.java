import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Point2D;

public class Fog_mng {
  private static final boolean DEBUG = true;

  static void fog_storage_attach(ArrayList<Storage> storage_List){
    var temp = new Storage(storage_List.size() + 1);
    storage_List.add(temp);
  }

  static boolean data_exist(ArrayList<Data> cache_data_list, Integer data_num){
    boolean result = false;
    for(int i = 0; i < cache_data_list.size(); i++){
      if(cache_data_list.get(i).num == data_num){
        result = true;
        if(DEBUG) System.out.println("Data Found on Local Network.");
      }
      if(result == true) break;
    }
    return result;
  }

  static void data_add(ArrayList<Data> cache_data_list, Integer dynamic_fog_num, Integer data_num){
    Random rand = new Random();
    var cached_by_list = new ArrayList<Integer>();

    int data_size = rand.nextInt(40) + 10;
    int cached_by_total;
    try{
      Data data = cache_data_list.get(data_num);
      cached_by_total = data.cached_by_total + 1;
      for (int i = 0; i < data.cached_by_list.size(); i++) {
        cached_by_list.add(data.cached_by_list.get(i));
      }
    }
    catch(Exception e){
      cached_by_total = 1;
    }
    cached_by_list.add(dynamic_fog_num);

    var temp = new Data(data_num, data_size, cached_by_total, cached_by_list);
    cache_data_list.add(temp);
    if(DEBUG) System.out.println("Data Added.");
  }

  static int set_nearest_dynamic_fog(ArrayList<Node_info> node_list, ArrayList<Storage> dynamic_fog_list, Point2D.Double current_node){
    double distance = 9999;//Initialze distance
    double temp_distance;
    int dynamic_fog_result = -1;
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      temp_distance = current_node.distance(node_list.get(dynamic_fog_list.get(i).node_num).point);
      if(distance > temp_distance){
        distance = temp_distance;
        dynamic_fog_result = dynamic_fog_list.get(i).node_num;
      }
    }
    return dynamic_fog_result;
  }
}
