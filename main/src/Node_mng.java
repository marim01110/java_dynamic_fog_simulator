import java.awt.geom.Point2D;
import java.lang.ProcessHandle.Info;

import java.util.Random;

public class Node_mng {
    static void init(Random rand, Node_info node, int init_x, int init_y){
        //Initialize Node. Set num, first location, move speed.
        node.num = 1;
        node.point.setLocation(init_x, init_y);
        node.move_speed = rand.nextInt(40)+10;
    }
}