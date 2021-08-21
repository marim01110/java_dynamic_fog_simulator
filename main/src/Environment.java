import java.awt.geom.Point2D;
public class Environment {
    //------LANDMARK SETTING------
    static final int LANDMARKS = 3;
    private static int[] landmark_point_x_array = {1000, 800, 500};
    private static int[] landmark_point_y_array = {1000, 200, 500};
  
    //------NODE MOVE SPEED SETTING------
    private static final int MOVE_SPEEDS = 0;
    private static int[] move_speed_array = {};

    static Point2D.Double return_landmark_point(int index){
      var point = new Point2D.Double();

      point.setLocation(landmark_point_x_array[index], landmark_point_y_array[index]);
      return point;
    }
}
