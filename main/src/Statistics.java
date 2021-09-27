public class Statistics {
  static int moves = 0;
  static int border_acrossed = 0;
  static int data_transfered = 0;
  static int dl_from_cloud = 0;
  static int dl_from_local = 0;
  static int dl_from_nearest_df = 0;
  static double power_comsumption_total = 0;

  static void print_info(){
    System.out.println();
    System.out.println("------Simulation Results------");
    System.out.println("border_acrossed: " + border_acrossed + " (" + border_acrossed * 100.0 / moves + "%)");
    System.out.println("Data Transfer Count: " + data_transfered);
    System.out.println("Download from Cloud: " + dl_from_cloud + " (" + dl_from_cloud * 100.0 / data_transfered + "%)");
    System.out.println("Download from Local Network: " + dl_from_local + " (" + dl_from_local * 100.0 / data_transfered + "%)");
    System.out.println("Download from Nearest Dynamic_Fog: " + dl_from_nearest_df + " (" + dl_from_nearest_df * 100.0 / data_transfered + "%)");
    System.out.println("Total battery comsumption: " + power_comsumption_total);
  }
}
