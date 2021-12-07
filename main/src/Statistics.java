public class Statistics {
  static int moves = 0;
  static int border_acrossed = 0;
  static int data_transfered = 0;
  static int dl_from_cloud = 0;
  static int dl_from_local = 0;
  static int dl_from_nearest_df_cell = 0;
  static int dl_from_nearest_df_bluetooth = 0;
  static int for_calc_contents_average = 0;
  static double for_calc_latency_proposed = 0;
  static double for_calc_latency_conventional = 0;
  static long data_size_via_internet_proposed = 0;
  static long data_size_via_internet_conventional = 0;
  static double power_consumption_total = 0;

  static void print_info(){
    System.out.println();
    System.out.println("------Simulation Results------");
    System.out.println("border_acrossed: " + border_acrossed + " (" + border_acrossed * 100.0 / moves + "%)");
    System.out.println("Data Transfer Count: " + data_transfered);
    System.out.println("Download from Cloud: " + dl_from_cloud + " (" + dl_from_cloud * 100.0 / data_transfered + "%)");
    System.out.println("Download from Local Network: " + dl_from_local + " (" + dl_from_local * 100.0 / data_transfered + "%)");
    System.out.println("Download from Nearest Dynamic_Fog by Cellular: " + dl_from_nearest_df_cell + " (" + dl_from_nearest_df_cell * 100.0 / data_transfered + "%)");
    System.out.println("Download from Nearest Dynamic_Fog by Bluetooth: " + dl_from_nearest_df_bluetooth + " (" + dl_from_nearest_df_bluetooth * 100.0 / data_transfered + "%)"); 
    System.out.println("Average content types: " + for_calc_contents_average / (Settings.SIM_TIME_HOURS * 3600));
    System.out.println();
    System.out.println("------Evaluations------");
    System.out.println("Average latency");
    System.out.println("  Proposed: " + for_calc_latency_proposed / data_transfered + " [ms], Conventional: " + for_calc_latency_conventional / data_transfered + " [ms]");
    System.out.println("Data size which through the Internet");
    System.out.println("  Proposed: " + data_size_via_internet_proposed + " [kB], Conventional: " + data_size_via_internet_conventional + " [kB]");
    System.out.println("Battery consumption");
    System.out.println("  Proposed: " + power_consumption_total + ", Conventional: " + data_transfered * Settings.BATTERY_COMSUMPTION_CELL_RECV);
    System.out.println();
  }
}
