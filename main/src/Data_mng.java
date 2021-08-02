import java.util.Random;
import java.util.ArrayList;

public class Data_mng {
  private static final boolean DEBUG = App.DEBUG;
  private static final int INIT = -1;
  static int cache_data_total = 0;

  private static int get_index_num(ArrayList<Data> stored_contents_list, int need_data_num){
    int result = INIT;

    for(int i = 0; i < stored_contents_list.size(); i++){
      if(need_data_num == stored_contents_list.get(i).num){
        result = i;
        break;
      }
    }
    return result;
  }

  private static void add(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> stored_contents_list, ArrayList<Integer> last_used, Integer dynamic_fog_num, Integer data_num){
    create(stored_contents_list);
    update(dynamic_fog_list, stored_contents_list, last_used, dynamic_fog_num, data_num);
  }

  /*static void add_fixed(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> stored_contents_list,){
    if(DATA_CREATED != true){
      for(int i = 0; i < App.CONTENTS_TYPES_MAX; i++){
        add(dynamic_fog_list, stored_contents_list, dynamic_fog_num, i);
      }
    }
  }*/

  private static void create(ArrayList<Data> stored_contents_list){
    Random rand = new Random();
    var cached_by_list = new ArrayList<Integer>();
    int data_num, data_size, cached_by_total;

    //Data Create Process
    data_num = cache_data_total;
    data_size = (rand.nextInt(39) + 1) * 5;
    cached_by_total = 0;

    var temp_Data = new Data(data_num, data_size, cached_by_total, cached_by_list);
    stored_contents_list.add(temp_Data);

    cache_data_total += 1;
  }

  private static void update(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> stored_contents_list, ArrayList<Integer> last_used, int dynamic_fog_num, int data_num){
    var cached_by_list = new ArrayList<Integer>();
    var contents_list = new ArrayList<Integer>();
    int data_index_num = INIT;
    int dynamic_fog_index_num = INIT;
    int loop_count = 0;
    int cached_by_total, total_capacity, used_capacity;

    data_index_num = get_index_num(stored_contents_list, data_num);

    //Update stored_contents_list Process
    Data data = stored_contents_list.get(data_index_num);
    cached_by_total = data.cached_by_total + 1;
    for (int i = 0; i < data.cached_by_list.size(); i++) {
      cached_by_list.add(data.cached_by_list.get(i));
    }
    cached_by_list.add(dynamic_fog_num);

/*
    //Replace with new Info
    var temp_Data = new Data(data_num, data.file_size, cached_by_total, cached_by_list);
    stored_contents_list.remove(data_index_num);
    stored_contents_list.add(temp_Data);
*/

    dynamic_fog_index_num = Fog_mng.get_dynamic_fog_index_num(dynamic_fog_list, dynamic_fog_num);

    //Update dynamic_fog_list Process
    try{
      for(int i = 0; i < dynamic_fog_list.get(dynamic_fog_index_num).contents_list.size(); i++){
        contents_list.add(dynamic_fog_list.get(dynamic_fog_index_num).contents_list.get(i));
      }
    }
    catch(Exception e){} //If contents_list is empty, do nothing.
    contents_list.add(data_num);
    total_capacity = dynamic_fog_list.get(dynamic_fog_index_num).total_capacity;
    used_capacity = Fog_mng.calc_used_capacity(stored_contents_list, contents_list);

    //Check Used Capacity and remove files
    while(total_capacity < used_capacity){

      //Decide a file to delete and delete from contents_list
      if(true){
        System.out.println("Used: " + used_capacity);//DL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        System.out.println(stored_contents_list);
      }
      delete_older_file(dynamic_fog_list, stored_contents_list, last_used, dynamic_fog_num);

      used_capacity = Fog_mng.calc_used_capacity(stored_contents_list, contents_list);
      

      if(DEBUG) System.out.println("Used: " + used_capacity);
      loop_count += 1;
      if(loop_count > 10) System.exit(-1);
    }

    //Replace with new Info
    var temp_Data = new Data(data_num, data.file_size, cached_by_total, cached_by_list);
    stored_contents_list.remove(data_index_num);
    stored_contents_list.add(temp_Data);

    //Replace with new Dynamic_Fog Information
    dynamic_fog_list.remove(dynamic_fog_index_num);
    var temp_Storage = new Storage(dynamic_fog_num, total_capacity, used_capacity, contents_list);
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

  static void search(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> stored_contents_list, ArrayList<Integer> last_used, int nearest_dynamic_fog, int need_data_num){
    boolean data_exist;
    boolean data_found = false;
    int need_data_index_num = INIT;

    if(App.CONTENTS_TYPES_FIXED) data_exist = true;
    else data_exist = exist(stored_contents_list, need_data_num);

    need_data_index_num = get_index_num(stored_contents_list, need_data_num);
    Statistics.data_transfer += 1;

    if(data_exist == true){
      //Data Information Found in index_list
      for(int j = 0; j < stored_contents_list.get(need_data_index_num).cached_by_list.size(); j++){
        if(nearest_dynamic_fog == stored_contents_list.get(need_data_index_num).cached_by_list.get(j)){
          data_found = true;
          Statistics.dl_from_nearest_df += 1;
          if(DEBUG) System.out.println("Data was found in Nearest DF.");
          break;
        }
      }
      if(data_found == false){
        //Data Copy Process
        update(dynamic_fog_list, stored_contents_list, last_used, nearest_dynamic_fog, need_data_num);
        data_found = true;
        Statistics.dl_from_local += 1;
        if(DEBUG) System.out.println("Data Copied from Local Network.");
      }
    }
    else if(data_exist == false){
      add(dynamic_fog_list, stored_contents_list, last_used, nearest_dynamic_fog, need_data_num);
      data_found = true;
      Statistics.dl_from_cloud += 1;
      if(DEBUG) System.out.println("Data was Downloaded from Cloud.");
    }
  }

  private static boolean exist(ArrayList<Data> stored_contents_list, Integer data_num){
    boolean found = false;
    boolean result = false;

    for(int i = 0; i < stored_contents_list.size(); i++){
      if(stored_contents_list.get(i).num == data_num){
        found = true;
        if(stored_contents_list.get(i).cached_by_list.size() > 0) result = true;
        if(DEBUG) System.out.println("Data seems to be in Local Network.");
      }
      if(found == true) break;
    }
    return result;
  }

  static void update_delete_order(ArrayList<Data> stored_contents_list, ArrayList<Integer> last_used, int data_num){
    for(int i = 0; i < last_used.size(); i++){
      if(last_used.get(i) == data_num){
        last_used.remove(i);
      }
    }
    last_used.add(data_num);
  }

  private static void delete_older_file(ArrayList<Storage> dynamic_fog_list, ArrayList<Data> stored_contents_list, ArrayList<Integer> last_used, int dynamic_fog_num){
    Storage storage;
    Data data;
    int dynamic_fog_index_num = INIT;
    int delete_file_num = INIT;
    int delete_file_index_num = INIT;

    dynamic_fog_index_num = Fog_mng.get_dynamic_fog_index_num(dynamic_fog_list, dynamic_fog_num);
    if(DEBUG) System.out.println("Current delete order is " + last_used);

    // Delete from contents_list
    storage = dynamic_fog_list.get(dynamic_fog_index_num);
    for(int i = 0; i < last_used.size(); i++){
      for(int j = 0; j < storage.contents_list.size(); j++){
        if(last_used.get(i) == storage.contents_list.get(j)){
          storage.contents_list.remove(j);
          delete_file_num = last_used.get(i);
          System.out.println("File num: " + delete_file_num + " has deleted.");
          break;
        }
      }
      if(delete_file_num != INIT) break;
    }

    delete_file_index_num = get_index_num(stored_contents_list, delete_file_num);

    //Delete from cached_by_list
    data = stored_contents_list.get(delete_file_index_num);
    for(int i = 0; i < data.cached_by_list.size(); i++){
      if(data.cached_by_list.get(i) == dynamic_fog_num){
        data.cached_by_list.remove(i);
        break;
      }
    }
    data.cached_by_total -= 1;
  }

  static void print_detail(ArrayList<Data> stored_contents_list){
    Data data;

    try {
      for(int i = 0; i < stored_contents_list.size(); i++){
        data = stored_contents_list.get(i);
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
