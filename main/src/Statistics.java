public class Statistics {
  static int area_acrossed = 0;
  static int data_transfer = 0;
  static int dl_from_cloud = 0;
  static int dl_from_local = 0;
  static int dl_from_nearest_df = 0;

  static void print_info(){
    System.out.println();
    System.out.println("------Simulation Results------");
    System.out.println("area_acrossed: " + area_acrossed);
    System.out.println("Data Transfer Count: " + data_transfer);
    System.out.println("Download from Cloud: " + dl_from_cloud + " (" + dl_from_cloud * 100.0 / data_transfer + "%)");
    System.out.println("Download from Local Network: " + dl_from_local + " (" + dl_from_local * 100.0 / data_transfer + "%)");
    System.out.println("Download from Nearest Dynamic_Fog: " + dl_from_nearest_df + " (" + dl_from_nearest_df * 100.0 / data_transfer + "%)");
  }
}
