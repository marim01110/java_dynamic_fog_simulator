import java.util.Random;
import java.util.ArrayList;

public class Data_mng {
  private static final boolean DEBUG = Settings.DEBUG;

  static Data_info get_data_info(int need_data_num, boolean deny_null){
    Data_info data = null;

    for(int i = 0, size = Environment.network_contents_list.size(); i < size; i++){
      if(need_data_num == Environment.network_contents_list.get(i).num){
        data = Environment.network_contents_list.get(i);
        break;
      }
    }
    if(data == null){
      if(DEBUG) System.out.println("Requested data: " + need_data_num + " is Not Found.");
      if(deny_null){
        System.out.println("Error: Data_info is null.");
        System.out.println("Quit the program.");
        System.exit(-1);
      }
    }

    return data;
  }

  private static Data_info return_empty_data(int num){
    var hosted_by_list = new ArrayList<Integer>();
    var empty = new Data_info(num, 999999999, Environment.INIT, hosted_by_list);

    return empty;
  }

  static void init_arraylist(){
    for(int i = 0; i < Settings.CONTENTS_TYPES_MAX; i++){
      Environment.network_contents_list.add(return_empty_data(i));
    }
    if(DEBUG) System.out.println("Initialization of Network contents list is Completed.");
  }

  int create(int num){
    Random rand = new Random();
    var hosted_by_list = new ArrayList<Integer>();
    int data_num, data_size, data_expire_after;

    /* Data Create Process */
    data_num = num;
    data_size = (rand.nextInt(Settings.CONTENTS_FILE_SIZE_MAX / 5 - 1) + 1) * 5;
    data_expire_after = Environment.time_count + Settings.CONTENTS_EXPIRE_AFTER;

    var temp_Data = new Data_info(data_num, data_size, data_expire_after, hosted_by_list);
    Environment.network_contents_list.set(data_num, temp_Data);

    Environment.cache_data_total += 1;
    return temp_Data.num;
  }

  void update(int dynamic_fog_num, int data_num){
    Data_info data;
    Fog_info fog_node;
    var fog_mng_class = new Fog_mng();
    int loop_count = 0;

    data = get_data_info(data_num, true);
    fog_node = Fog_mng.get_fog_info(dynamic_fog_num);

    /* Update network_contents_list Process */
    data.hosted_by_list.add(dynamic_fog_num);

    /* Update dynamic_fog_list Process */
    fog_node.fog_stored_contents_list.add(data_num);
    fog_mng_class.calc_used_capacity(fog_node);

    /* Check Used Capacity and remove files */
    while(fog_node.total_capacity < fog_node.used_capacity){

      /* Decide a file to delete and delete from fog_stored_contents_list */
      delete_older_file(fog_node.fog_stored_contents_list, dynamic_fog_num);

      fog_mng_class.calc_used_capacity(fog_node);
      
      loop_count += 1;
      if(loop_count > 10){
        System.out.println("Storage management error.");
        System.out.println("Quit the program.");
        System.exit(-1);
      }
    }
  }

  private void delete(int delete_file_num){
    Data_info data;
    Fog_info fog_node;
    Object obj;
    var fog_mng_class = new Fog_mng();

    //Load data_hosted_list
    data = get_data_info(delete_file_num, true);
    for(int i = 0; i < data.hosted_by_list.size(); i++){
      /* Load Fog info */
      fog_node = Fog_mng.get_fog_info(data.hosted_by_list.get(i));

      /* Update fog_stored_contents_list */
      obj = delete_file_num;
      fog_node.fog_stored_contents_list.remove(obj);

      fog_mng_class.calc_used_capacity(fog_node);
    }
    Environment.network_contents_list.set(data.num, return_empty_data(data.num));
    Environment.file_deleted += 1;
  }

  int select(){
    int need_data_num;
    Random rand = new Random();

    if(Settings.CONTENTS_TYPES_FIXED){
      need_data_num = rand.nextInt(Settings.CONTENTS_TYPES_MAX);
    }
    else{
      System.out.println("Err: 'CONTENTS_TYPES_MAX = false' feature is currently NOT supported.");
      System.exit(-1);
      need_data_num = Environment.INIT;
    }
    return need_data_num;
  }

  boolean info_exist(Integer data_num){
    boolean result = false;

    for(int i = 0, size = Environment.network_contents_list.size(); i < size; i++){
      if(Environment.network_contents_list.get(i).num == data_num){
        result = true;
      }
      if(result == true) break;
    }
    return result;
  }

  void valid_check(){
    int current_time  = Environment.time_count;
    Data_info data;

    for(int i = 0, size = Environment.network_contents_list.size(); i < size; i++){
      data = Environment.network_contents_list.get(i);
      if(data.expire_after == Environment.INIT) continue;
      if(data.expire_after <= current_time){
        if(DEBUG){
          System.out.println("Data num: " + data.num + " is expired.");
          System.out.println("Deleting ...");
        }
        delete(data.num);
      }
    }
    Statistics.for_calc_contents_average += Environment.network_contents_list.size();
  }

  void update_delete_order(int data_num){
    for(int i = 0, size = Environment.last_used.size(); i < size; i++){
      if(Environment.last_used.get(i) == data_num){
        Environment.last_used.remove(i);
        break;
      }
    }
    Environment.last_used.add(data_num);
  }

  private void delete_older_file(ArrayList<Integer> fog_stored_contents_list, int dynamic_fog_num){
    Data_info data;
    int delete_file_num = Environment.INIT;

    /* Delete from fog_stored_contents_list */
    for(int i = 0; i < Environment.last_used.size(); i++){
      for(int j = 0; j < fog_stored_contents_list.size(); j++){
        if((Environment.last_used.get(i) - fog_stored_contents_list.get(j)) == 0){
          fog_stored_contents_list.remove(j);
          delete_file_num = Environment.last_used.get(i);
          break;
        }
      }
      if(delete_file_num != Environment.INIT) break;
    }
    if(delete_file_num == Environment.INIT){
      System.out.println("Error: Can not Find Delete file.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }

    /* Delete from hosted_by_list */
    data = get_data_info(delete_file_num, true);
    for(int i = 0; i < data.hosted_by_list.size(); i++){
      if(data.hosted_by_list.get(i) == dynamic_fog_num){
        data.hosted_by_list.remove(i);
        break;
      }
    }
  }

  static void print_detail(){
    Data_info data;

    try {
      for(int i = 0, size = Environment.network_contents_list.size(); i < size; i++){
        data = Environment.network_contents_list.get(i);
        System.out.println();
        System.out.println("Data num: " + data.num);
        System.out.println("Data size: " + data.file_size);
        System.out.println("Expire After: " + data.expire_after);
        System.out.println("Cached by total: " + data.hosted_by_list.size());
        System.out.println("Cached by: " + data.hosted_by_list);
        System.out.println();
      }
    }
    catch (Exception e) {
      System.out.println("No File exist or Error has occured.");
    }
  }
}
