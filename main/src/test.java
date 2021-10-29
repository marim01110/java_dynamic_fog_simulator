import java.util.ArrayList;
public class test {
  static void main(){
    Node_mng.spawn(0);

    var fog_stored_contents_list = new ArrayList<Integer>();
    var temp = new Fog_info(Environment.node_list.get(0).num, Environment.FOG_IS_OK, Settings.FOG_STORAGE_SIZE, 0, fog_stored_contents_list);
    Environment.dynamic_fog_list.add(temp);
    
    Fog_info fog;
    fog = Environment.dynamic_fog_list.get(0);
    fog.status = Environment.FOG_IS_OK;
    fog.fog_stored_contents_list.add(1);

    System.out.println(Environment.dynamic_fog_list.get(0).status + ", " + Environment.dynamic_fog_list.get(0).used_capacity + ", " + Environment.dynamic_fog_list.get(0).fog_stored_contents_list);
  }
}
