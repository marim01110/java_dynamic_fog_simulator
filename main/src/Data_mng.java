import java.util.Random;
import java.util.ArrayList;

public class Data_mng {
  private static final boolean DEBUG = Settings.DEBUG;
  private static final int INIT = -1;
  static int cache_data_total = 0;

  static Data_info get_data_info(int need_data_num){
    Data_info data = null;

    for(int i = 0; i < Environment.network_contents_list.size(); i++){
      if(need_data_num == Environment.network_contents_list.get(i).num){
        data = Environment.network_contents_list.get(i);
        break;
      }
    }
    if(data == null){
      System.out.println("Requested data: " + need_data_num + " is Not Found.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }

    return data;
  }

  static void fixed_respawn(){
    int contents = Environment.network_contents_list.size();

    for (int i = contents; i < Settings.CONTENTS_TYPES_MAX; i++) {
      create();
    }
  }

  static void create(){
    Random rand = new Random();
    var hosted_by_list = new ArrayList<Integer>();
    int data_num, data_size, data_expire_after, hosted_by_total;

    //Data Create Process
    data_num = cache_data_total;
    data_size = (rand.nextInt(39) + 1) * 5;
    data_expire_after = Environment.time_count + Settings.CONTENTS_EXPIRE_AFTER;
    
    hosted_by_total = 0;

    var temp_Data = new Data_info(data_num, data_size, data_expire_after, hosted_by_total, hosted_by_list);
    Environment.network_contents_list.add(temp_Data);

    cache_data_total += 1;
  }

  static void update(ArrayList<Integer> last_used, int dynamic_fog_num, int data_num){
    var hosted_by_list = new ArrayList<Integer>();
    var fog_stored_contents_list = new ArrayList<Integer>();
    Data_info data;
    Fog_info dynamic_fog;
    int loop_count = 0;
    int hosted_by_total, total_capacity, used_capacity;

    //Update network_contents_list Process
    data = get_data_info(data_num);
    hosted_by_total = data.hosted_by_total + 1;
    for (int i = 0; i < data.hosted_by_list.size(); i++) {
      hosted_by_list.add(data.hosted_by_list.get(i));
    }
    hosted_by_list.add(dynamic_fog_num);

    dynamic_fog = Fog_mng.get_fog_info(dynamic_fog_num);

    //Update dynamic_fog_list Process
    try{
      for(int i = 0; i < dynamic_fog.fog_stored_contents_list.size(); i++){
        fog_stored_contents_list.add(dynamic_fog.fog_stored_contents_list.get(i));
      }
    }
    catch(Exception e){} //If fog_stored_contents_list is empty, do nothing.
    fog_stored_contents_list.add(data_num);
    total_capacity = dynamic_fog.total_capacity;
    used_capacity = Fog_mng.calc_used_capacity(fog_stored_contents_list);

    //Check Used Capacity and remove files
    while(total_capacity < used_capacity){

      //Decide a file to delete and delete from fog_stored_contents_list
      delete_older_file(fog_stored_contents_list, last_used, dynamic_fog_num);

      used_capacity = Fog_mng.calc_used_capacity(fog_stored_contents_list);
      
      loop_count += 1;
      if(loop_count > 10){
        System.out.println("Storage management error.");
        System.out.println("Quit the program.");
        System.exit(-1);
      }
    }

    //Replace with new Info
    var temp_Data = new Data_info(data_num, data.file_size, data.expire_after, hosted_by_total, hosted_by_list);//Incomplete
    Environment.network_contents_list.remove(data);
    Environment.network_contents_list.add(temp_Data);

    //Replace with new Dynamic_Fog Information
    Environment.dynamic_fog_list.remove(dynamic_fog);
    var temp_Storage = new Fog_info(dynamic_fog_num, total_capacity, used_capacity, fog_stored_contents_list);
    Environment.dynamic_fog_list.add(temp_Storage);
  }

  private static void delete(int delete_file_num){
    Data_info data;
    Fog_info fog_node;
    var new_fog_stored_contents_list = new ArrayList<Integer>();
    Object obj;
    int new_used_capacity;

    //Load data_hosted_list
    data = get_data_info(delete_file_num);
    for(int i = 0; i < data.hosted_by_list.size(); i++){
      //Load Fog info
      fog_node = Fog_mng.get_fog_info(data.hosted_by_list.get(i));

      //Create new Fog info
      new_fog_stored_contents_list = fog_node.fog_stored_contents_list;
      obj = delete_file_num;
      new_fog_stored_contents_list.remove(obj);

      new_used_capacity = Fog_mng.calc_used_capacity(new_fog_stored_contents_list);
      var new_fog_info = new Fog_info(fog_node.node_num, fog_node.total_capacity, new_used_capacity, new_fog_stored_contents_list);

      //Replace with new info
      Environment.dynamic_fog_list.remove(fog_node);
      Environment.dynamic_fog_list.add(new_fog_info);
    }
    Environment.network_contents_list.remove(data);
    Environment.file_deleted += 1;
  }

  static int select(){
    int need_data_num;
    Random rand = new Random();

    if(Settings.CONTENTS_TYPES_FIXED){
      need_data_num = rand.nextInt(Settings.CONTENTS_TYPES_MAX) + Environment.file_deleted;
    }
    else{
      need_data_num = rand.nextInt(cache_data_total + 1);
    }
    return need_data_num;
  }

  static boolean info_exist(Integer data_num){
    boolean result = false;

    for(int i = 0; i < Environment.network_contents_list.size(); i++){
      if(Environment.network_contents_list.get(i).num == data_num){
        result = true;
      }
      if(result == true) break;
    }
    return result;
  }

  static void valid_check(){
    int current_time  = Environment.time_count;
    Data_info data;

    for(int i = 0; i < Environment.network_contents_list.size(); i++){
      data = Environment.network_contents_list.get(i);
      if(data.expire_after <= current_time){
        if(DEBUG){
          System.out.println("Data num: " + data.num + " is not valid.");
          System.out.println("Deleting ...");
        }
        delete(data.num);
        i -= 1;
      }
    }
  }

  static void update_delete_order(ArrayList<Integer> last_used, int data_num){
    for(int i = 0; i < last_used.size(); i++){
      if(last_used.get(i) == data_num){
        last_used.remove(i);
      }
    }
    last_used.add(data_num);
  }

  private static void delete_older_file(ArrayList<Integer> fog_stored_contents_list, ArrayList<Integer> last_used, int dynamic_fog_num){
    Data_info data;
    int delete_file_num = INIT;

    //if(DEBUG) System.out.println("Current delete order is " + last_used);

    // Delete from fog_stored_contents_list
    for(int i = 0; i < last_used.size(); i++){
      for(int j = 0; j < fog_stored_contents_list.size(); j++){
        if((last_used.get(i) - fog_stored_contents_list.get(j)) == 0){
          fog_stored_contents_list.remove(j);
          delete_file_num = last_used.get(i);
          break;
        }
      }
      if(delete_file_num != INIT) break;
    }
    if(delete_file_num == INIT){
      System.out.println("Error: Can not Find Delete file.");
      System.out.println("Quit the program.");
      System.exit(-1);
    }

    //Delete from hosted_by_list
    data = get_data_info(delete_file_num);
    for(int i = 0; i < data.hosted_by_list.size(); i++){
      if(data.hosted_by_list.get(i) == dynamic_fog_num){
        data.hosted_by_list.remove(i);
        break;
      }
    }
    data.hosted_by_total -= 1;
  }

  static void print_detail(){
    Data_info data;

    try {
      for(int i = 0; i < Environment.network_contents_list.size(); i++){
        data = Environment.network_contents_list.get(i);
        System.out.println();
        System.out.println("Data num: " + data.num);
        System.out.println("Data size: " + data.file_size);
        System.out.println("Cached by total: " + data.hosted_by_total);
        System.out.println("Cached by: " + data.hosted_by_list);
        System.out.println();
      }
    }
    catch (Exception e) {
      System.out.println("No File exist or Error has occured.");
    }
  }
}
