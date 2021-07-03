import java.util.ArrayList;
import java.util.Random;

public class Fog_mng {
  private static final boolean DEBUG = true;

  static void fog_storage_attach(ArrayList<Storage> storage_List){
    var temp = new Storage(storage_List.size() + 1);
    storage_List.add(temp);
  }

  static boolean data_exist(ArrayList<Data> cache_data_list, Integer data_num){
    boolean data_exist = false;
    for(int i = 0; i < cache_data_list.size(); i++){
      if(cache_data_list.get(i).num == data_num) data_exist = true;
      if(data_exist == true) break;
    }
    return data_exist;
  }

  static void data_add(ArrayList<Data> data_list, Integer data_num){
    Random rand = new Random();
    int data_size = rand.nextInt(40) + 10;
    var temp = new Data(data_num, data_size);
    data_list.add(temp);
    if(DEBUG) System.out.println("Data Added.");
  }

  static void fog_data_copy(ArrayList<Storage> storage_list, ArrayList<Data> data_list){
    
  }
}
