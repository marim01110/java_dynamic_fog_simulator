import java.util.ArrayList;

public class Statistics {
  static int moves = 0;
  static int border_acrossed = 0;

  private static int data_transfered_total = 0;
  static int data_transfered = 0;
  private static ArrayList<Integer> data_transfered_list = new ArrayList<>();

  private static int dl_from_cloud_total = 0;
  static int dl_from_cloud = 0;
  private static ArrayList<Integer> dl_from_cloud_list = new ArrayList<>();

  private static int dl_from_local_total = 0;
  static int dl_from_local = 0;
  private static ArrayList<Integer> dl_from_local_list = new ArrayList<>();

  private static int dl_from_near_df_cell_total = 0;
  static int dl_from_near_df_cell = 0;
  private static ArrayList<Integer> dl_from_near_df_cell_list = new ArrayList<>();

  private static int dl_from_near_df_wifi_total = 0;
  static int dl_from_near_df_wifi = 0;
  private static ArrayList<Integer> dl_from_near_df_wifi_list = new ArrayList<>();

  private static int dl_from_near_df_bluetooth_total = 0;
  static int dl_from_near_df_bluetooth = 0;
  private static ArrayList<Integer> dl_from_near_df_bluetooth_list = new ArrayList<>();

  static int for_calc_contents_average = 0;
  static double for_calc_latency_proposed = 0;
  static double for_calc_latency_conventional = 0;
  static long data_size_via_internet_proposed = 0;
  static long data_size_via_internet_conventional = 0;
  static double power_consumption_total = 0;
  static int out_of_battery = 0;

  static void init(){
    moves = 0;
    border_acrossed = 0;
    data_transfered_list.clear();
    dl_from_cloud_list.clear();
    dl_from_local_list.clear();
    dl_from_near_df_cell_list.clear();
    dl_from_near_df_wifi_list.clear();
    dl_from_near_df_bluetooth_list.clear();
    for_calc_latency_proposed = 0;
    for_calc_latency_conventional = 0;
    data_size_via_internet_proposed = 0;
    data_size_via_internet_conventional = 0;
    power_consumption_total = 0;
    out_of_battery = 0;
  }

  static void store(){
    /* data_transfered */
    data_transfered_list.add(data_transfered);
    data_transfered = 0;

    /* dl_from_cloud */
    dl_from_cloud_list.add(dl_from_cloud);
    dl_from_cloud = 0;

    /* dl_from_local */
    dl_from_local_list.add(dl_from_local);
    dl_from_local = 0;

    /* dl_from_near_df_cell */
    dl_from_near_df_cell_list.add(dl_from_near_df_cell);
    dl_from_near_df_cell = 0;

    /* dl_from_near_df_wifi */
    dl_from_near_df_wifi_list.add(dl_from_near_df_wifi);
    dl_from_near_df_wifi = 0;

    /* dl_from_near_df_bluetooth */
    dl_from_near_df_bluetooth_list.add(dl_from_near_df_bluetooth);
    dl_from_near_df_bluetooth = 0;
  }

  private static boolean calc(){
    boolean error = false;

    /* Calc data_transfered */
    data_transfered_total = 0;
    for(int i = 0, size = data_transfered_list.size(); i < size; i++){
      data_transfered_total += data_transfered_list.get(i);
    }
    if(data_transfered != 0) error = true;

    /* Calc dl_from_cloud_total */
    dl_from_cloud_total = 0;
    for(int i = 0, size = dl_from_cloud_list.size(); i < size; i++){
      dl_from_cloud_total += dl_from_cloud_list.get(i);
    }
    if(dl_from_cloud != 0) error = true;

    /* Calc dl_from_local_total */
    dl_from_local_total = 0;
    for(int i = 0, size = dl_from_local_list.size(); i < size; i++){
      dl_from_local_total += dl_from_local_list.get(i);
    }
    if(dl_from_local != 0) error = true;

    /* Calc dl_from_near_df_cell_total */
    dl_from_near_df_cell_total = 0;
    for(int i = 0, size = dl_from_near_df_cell_list.size(); i < size; i++){
      dl_from_near_df_cell_total += dl_from_near_df_cell_list.get(i);
    }
    if(dl_from_near_df_cell != 0) error = true;

    /* Calc dl_from_near_df_wifi_total */
    dl_from_near_df_wifi_total = 0;
    for(int i = 0, size = dl_from_near_df_wifi_list.size(); i < size; i++){
      dl_from_near_df_wifi_total += dl_from_near_df_wifi_list.get(i);
    }
    if(dl_from_near_df_wifi != 0) error = true;

    /* Calc dl_from_near_df_bluetooth_total */
    dl_from_near_df_bluetooth_total = 0;
    for(int i = 0, size = dl_from_near_df_bluetooth_list.size(); i < size; i++){
      dl_from_near_df_bluetooth_total += dl_from_near_df_bluetooth_list.get(i);
    }
    if(dl_from_near_df_bluetooth != 0) error = true;

    return error;
  }

  static void print_info(){
    if(calc()) System.out.println("Result may include incorrect values.");

    System.out.println();
    System.out.println("------Simulation Results------");
    System.out.println("border_acrossed: " + border_acrossed + " (" + border_acrossed * 100.0 / moves + "%)");
    System.out.println("Data Transfer Count: " + data_transfered_total);
    System.out.println("Download from Cloud: " + dl_from_cloud_total + " (" + dl_from_cloud_total * 100.0 / data_transfered_total + "%)");
    System.out.println("Download from Local Network: " + dl_from_local_total + " (" + dl_from_local_total * 100.0 / data_transfered_total + "%)");
    System.out.println("Download from Near Dynamic_Fog by Cellular: " + dl_from_near_df_cell_total + " (" + dl_from_near_df_cell_total * 100.0 / data_transfered_total + "%)");
    System.out.println("Download from Near Dynamic_Fog by Wi-Fi: " + dl_from_near_df_wifi_total + " (" + dl_from_near_df_wifi_total * 100.0 / data_transfered_total + "%)");
    System.out.println("Download from Near Dynamic_Fog by Bluetooth: " + dl_from_near_df_bluetooth_total + " (" + dl_from_near_df_bluetooth_total * 100.0 / data_transfered_total + "%)"); 
    System.out.println("Average content types: " + for_calc_contents_average / (Settings.SIM_TIME_HOURS * 3600));
    System.out.println("Numbers of out of battery: " + out_of_battery);
    System.out.println();
    System.out.println("------Evaluations------");
    System.out.println("Average latency");
    System.out.println("  Proposed: " + for_calc_latency_proposed / data_transfered_total + " [ms], Conventional: " + for_calc_latency_conventional / data_transfered_total + " [ms]");
    System.out.println("Data size which through the Internet");
    System.out.println("  Proposed: " + data_size_via_internet_proposed + " [kB], Conventional: " + data_size_via_internet_conventional + " [kB]");
    System.out.println("Battery consumption");
    System.out.println("  Proposed: " + power_consumption_total + ", Conventional: " + data_transfered_total * Settings.BATTERY_COMSUMPTION_CELL_RECV);
    System.out.println();
    print_detail();
  }

  static void print_detail(){
    System.out.println("------Details------");
    System.out.println("Download from Cloud: " + dl_from_cloud_list);
    System.out.println("Download from Local Network: " + dl_from_local_list);
    System.out.println("Download from Near Dynamic_Fog by Cellular: " + dl_from_near_df_cell_list);
    System.out.println("Download from Near Dynamic_Fog by Wi-Fi: " + dl_from_near_df_wifi_list);
    System.out.println("Download from Near Dynamic_Fog by Bluetooth: " + dl_from_near_df_bluetooth_list);
  }
}
