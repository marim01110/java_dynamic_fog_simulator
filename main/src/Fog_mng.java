import java.util.ArrayList;
import java.util.Random;

import jdk.nashorn.api.tree.TryTree;

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

  static void data_add(ArrayList<Data> cache_data_list, Integer data_num){
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
      cached_by_list.add("DF_node_num");
    }

    var temp = new Data(data_num, data_size, cached_by_total, cached_by_list);
    cache_data_list.add(temp);
    if(DEBUG) System.out.println("Data Added.");
  }

  static void fog_data_copy(ArrayList<Storage> storage_list, ArrayList<Data> data_list){
    
  }
}
