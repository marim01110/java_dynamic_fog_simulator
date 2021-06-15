import java.util.ArrayList;

public class Fog_mng {
  private static final boolean DEBUG = true;

  static void fog_storage_attach(ArrayList<Storage> storage_List){
    var temp = new Storage(storage_List.size() + 1);
    storage_List.add(temp);
  }

  static void fog_data_add(ArrayList<Data> data_list){
    var temp = new Data(data_list.size() + 1, 100);
    data_list.add(temp);
    if(DEBUG) System.out.println("Data Added.");
  }

  static void fog_data_copy(ArrayList<Storage> storage_list, ArrayList<Data> data_list){
    
  }
}
