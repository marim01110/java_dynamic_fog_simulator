import java.util.ArrayList;
import java.util.Random;

public class Data_mng {
  private static final boolean DEBUG = App.DEBUG;
  static int cache_data_total = 0;

  static int select(){
    int need_data_num;
    Random rand = new Random();

    if(App.CONTENTS_TYPES_FIXED){
      need_data_num = rand.nextInt(App.CONTENTS_TYPES_MAX);
    }
    else{
      need_data_num = rand.nextInt(cache_data_total + 1);
    }
    System.out.println(need_data_num);
    return need_data_num;
  }

  static void search(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> cache_data_list, int nearest_dynamic_fog, int need_data_num){
    boolean data_exist;
    boolean data_found = false;

    data_exist = exist(cache_data_list, need_data_num);
    if(data_exist == true){
      for(int j = 0; j < cache_data_list.get(need_data_num).cached_by_list.size(); j++){
        if(nearest_dynamic_fog == cache_data_list.get(need_data_num).cached_by_list.get(j)) data_found = true;
      }
      if(data_found == false){
        //Data Copy Process
        add(dynamic_fog_list, cache_data_list, nearest_dynamic_fog, need_data_num);
        if(DEBUG) System.out.println("Cache Data Copied.");
      }
    }
    else if(data_exist == false){
      add(dynamic_fog_list, cache_data_list, nearest_dynamic_fog, need_data_num);
      data_found = true;
    }
    if(data_found == true) System.out.println("Data Found.");
  }

  private static void add(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> cache_data_list, Integer dynamic_fog_num, Integer data_num){
    Random rand = new Random();
    var cached_by_list = new ArrayList<Integer>();
    var cache_index_list = new ArrayList<Integer>();

    int data_size = rand.nextInt(40) + 10;
    int cached_by_total;

    int dynamic_fog_index_num = -1;// Initialize variable
    int used_capacity;
    int total_capacity;

    //Update cache_data_list Process
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

    var temp_Data = new Data(data_num, data_size, cached_by_total, cached_by_list);
    cache_data_list.add(temp_Data);
    cache_data_total += 1;
    if(DEBUG) System.out.println("Data Added.");

    //Update dynamic_fog_list Process
    if(DEBUG) System.out.println("Looking for index_num for Dynamic Fog Node: " + dynamic_fog_num);
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      if(dynamic_fog_list.get(i).node_num == dynamic_fog_num){
        dynamic_fog_index_num = i;
      }
    }
    try{
      for(int i = 0; i < dynamic_fog_list.get(dynamic_fog_index_num).cache_index_list.size(); i++){
        cache_index_list.add(dynamic_fog_list.get(dynamic_fog_index_num).cache_index_list.get(i));
      }
    }
    catch(Exception e){}
    cache_index_list.add(data_num);
    total_capacity = dynamic_fog_list.get(dynamic_fog_index_num).total_capacity;
    used_capacity = Fog_mng.calc_used_capacity(cache_data_list, cache_index_list);

    //Replace Dynamic_Fog Information
    dynamic_fog_list.remove(dynamic_fog_index_num);
    var temp_Storage = new Storage(dynamic_fog_num, total_capacity, used_capacity, cache_index_list);
    dynamic_fog_list.add(temp_Storage);
  }

  private static boolean exist(ArrayList<Data> cache_data_list, Integer data_num){
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
}
