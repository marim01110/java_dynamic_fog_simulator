public class test {
  static void main(){
    Node_info node = null;

    Environment.node_list.add(Node_mng.spawn(0));
    node = Environment.node_list.get(0);
    System.out.println(node.point);
    Move.start(node);
    System.out.println(Environment.node_list.get(0).point);
  }
}
