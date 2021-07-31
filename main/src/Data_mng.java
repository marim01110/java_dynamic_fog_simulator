import java.util.Random;
import java.util.ArrayList;

public class Data_mng {
  private static final boolean DEBUG = App.DEBUG;
  private static final int INIT = -1;
  static int cache_data_total = 0;

  private static int get_index_num(ArrayList<Data> cache_data_list, int need_data_num){
    int result = INIT;

    for(int i = 0; i < cache_data_list.size(); i++){
      if(need_data_num == cache_data_list.get(i).num){
        result = i;
        break;
      }
    }
    return result;
  }

  private static void add(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> cache_data_list, Integer dynamic_fog_num, Integer data_num){
    create(cache_data_list);
    update(dynamic_fog_list, cache_data_list, dynamic_fog_num, data_num);
  }

  /*static void add_fixed(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> cache_data_list,){
    if(DATA_CREATED != true){
      for(int i = 0; i < App.CONTENTS_TYPES_MAX; i++){
        add(dynamic_fog_list, cache_data_list, dynamic_fog_num, i);
      }
    }
  }*/

  private static void create(ArrayList<Data> cache_data_list){
    Random rand = new Random();
    var cached_by_list = new ArrayList<Integer>();
    int data_num, data_size, cached_by_total;

    //Data Create Process
    data_num = cache_data_total;
    data_size = rand.nextInt(40) + 10;
    cached_by_total = 0;

    var temp_Data = new Data(data_num, data_size, cached_by_total, cached_by_list);
    cache_data_list.add(temp_Data);

    cache_data_total += 1;
  }

  private static void update(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> cache_data_list, int dynamic_fog_num, int data_num){
    var cached_by_list = new ArrayList<Integer>();
    var cache_index_list = new ArrayList<Integer>();
    int data_index_num = INIT;
    int dynamic_fog_index_num = INIT;
    int cached_by_total, total_capacity, used_capacity;

    data_index_num = get_index_num(cache_data_list, data_num);

    //Update cache_data_list Process
    Data data = cache_data_list.get(data_index_num);
    cached_by_total = data.cached_by_total + 1;
    for (int i = 0; i < data.cached_by_list.size(); i++) {
      cached_by_list.add(data.cached_by_list.get(i));
    }
    cached_by_list.add(dynamic_fog_num);

    //Replace with new Info
    var temp_Data = new Data(data_num, data.file_size, cached_by_total, cached_by_list);
    cache_data_list.remove(data_index_num);
    cache_data_list.add(temp_Data);

    //Get DF index_num
    for(int i = 0; i < dynamic_fog_list.size(); i++){
      if(dynamic_fog_list.get(i).node_num == dynamic_fog_num){
        dynamic_fog_index_num = i;
      }
    }

    //Update dynamic_fog_list Process
    try{
      for(int i = 0; i < dynamic_fog_list.get(dynamic_fog_index_num).cache_index_list.size(); i++){
        cache_index_list.add(dynamic_fog_list.get(dynamic_fog_index_num).cache_index_list.get(i));
      }
    }
    catch(Exception e){} //If cache_index_list is empty, do nothing.
    cache_index_list.add(data_num);
    total_capacity = dynamic_fog_list.get(dynamic_fog_index_num).total_capacity;
    used_capacity = Fog_mng.calc_used_capacity(cache_data_list, cache_index_list);

    //Replace with new Dynamic_Fog Information
    dynamic_fog_list.remove(dynamic_fog_index_num);
    var temp_Storage = new Storage(dynamic_fog_num, total_capacity, used_capacity, cache_index_list);
    dynamic_fog_list.add(temp_Storage);
  }

  static int select(){
    int need_data_num;
    Random rand = new Random();

    if(App.CONTENTS_TYPES_FIXED){
      need_data_num = rand.nextInt(App.CONTENTS_TYPES_MAX);
    }
    else{
      need_data_num = rand.nextInt(cache_data_total + 1);
    }
    return need_data_num;
  }

  static void search(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> cache_data_list, int nearest_dynamic_fog, int need_data_num){
    boolean data_exist;
    boolean data_found = false;
    int need_data_index_num = INIT;

    if(App.CONTENTS_TYPES_FIXED) data_exist = true;
    else data_exist = exist(cache_data_list, need_data_num);

    need_data_index_num = get_index_num(cache_data_list, need_data_num);
    Statistics.data_transfer += 1;

    if(data_exist == true){
      //Data Information Found in index_list
      for(int j = 0; j < cache_data_list.get(need_data_index_num).cached_by_list.size(); j++){
        if(nearest_dynamic_fog == cache_data_list.get(need_data_index_num).cached_by_list.get(j)){
          data_found = true;
          Statistics.dl_from_nearest_df += 1;
          if(DEBUG) System.out.println("Data was found in Nearest DF.");
          break;
        }
      }
      if(data_found == false){
        //Data Copy Process
        update(dynamic_fog_list, cache_data_list, nearest_dynamic_fog, need_data_num);
        data_found = true;
        Statistics.dl_from_local += 1;
        if(DEBUG) System.out.println("Data Copied from Local Network.");
      }
    }
    else if(data_exist == false){
      add(dynamic_fog_list, cache_data_list, nearest_dynamic_fog, need_data_num);
      data_found = true;
      Statistics.dl_from_cloud += 1;
      if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
    }
  }

  private static boolean exist(ArrayList<Data> cache_data_list, Integer data_num){
    boolean found = false;
    boolean result = false;
    for(int i = 0; i < cache_data_list.size(); i++){
      if(cache_data_list.get(i).num == data_num){
        found = true;
        if(cache_data_list.get(i).cached_by_list.size() > 0) result = true;
        if(DEBUG) System.out.println("Data seems to be in Local Network.");
      }
      if(found == true) break;
    }
    return result;
  }

  static void print_detail(ArrayList<Data> cache_data_list){
    Data data;

    try {
      for(int i = 0; i < cache_data_list.size(); i++){
        data = cache_data_list.get(i);
        System.out.println();
        System.out.println("Data num: " + data.num);
        System.out.println("Data size: " + data.file_size);
        System.out.println("Cached by total: " + data.cached_by_total);
        System.out.println("Cached by: " + data.cached_by_list);
        System.out.println();
      }
    } catch (Exception e) {
      System.out.println("No File exist or Error has occured.");
    }
  }
}
