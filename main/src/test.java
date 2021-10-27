public class test {
  static void main(){
    Node_mng.spawn(0);
    System.out.println(Environment.node_list.get(0).point);
    Move.start(Environment.node_list.get(0));
    System.out.println(Environment.node_list.get(0).point);
  }
}
