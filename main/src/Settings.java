public class Settings {
  /* ------SIMULATION SETTINGS------ */
  static final boolean DEBUG = false;
  static final boolean FOG_USE = true;
  static final int SIM_TIME_HOURS = 18;// Unit is hours.
  static final int EDGE_DIST = 1700;// Unit is meters.
  static final int INIT_MAX_NODES = 4000;// Used only in mode 4 (random walk).
  static final int CONTENTS_RETRIEVE_FREQUENCY = 60;// Unit is seconds.
  static final int LANDMARKS = 10;// Enter the total number of landmarks set in init_landmark in Environment.java.
  static final int WAYPOINT_MIN = 3;// Sets the minimum number of landmarks to be traversed. This value must be less than LANDMARKS.
  static final double RTT_DIRECT_CELLULAR = 14.13;// RTT (Round Trip Time) for local communication through a base station. Unit is milliseconds.
  static final double RTT_DIRECT_WIFI = 14.13;// RTT for local communication by Wi-Fi Direct. Unit is milliseconds.
  static final double RTT_DIRECT_BLUETOOTH = 200;// RTT for local communication by Bluetooth. Unit is milliseconds.
  static final double RTT_CLOUD = 28.91;// RTT for Cloud communication by Optic Fiber. Unit is milliseconds.
  static final boolean BLUETOOTH_USE = true;
  static final double BT_CONNECTION_RANGE = 50;// Unit is meters.
  static final boolean WIFI_USE = false;
  static final double WIFI_CONNECTION_RANGE = 200;// Unit is meters.

  /* ------MODE 5 SETTINGS------ */
  static final int START_FROM = 5;// 24-hour notation.
  static final int DF_SYSTEM_PARTICIPATION_RATIO = 1;// Unit is percents.
  static final int[] nodes_model_array = {95405, 94750, 89750, 88580, 89975, 96950, 103675, 116200, 139050, 153700, 175000, 190450, 215450, 221350, 207300, 193530, 197100, 205200, 194950, 176500, 159550, 129100, 109950, 99030};// Estimated number of mobile terminals for 1.7 km around JR Kyoto Station.

  /* ------FOG SETTINGS------ */
  static final int FOG_STORAGE_SIZE = 100000;// Capacity available as Fog system on user terminal side. Unit is kilobytes.
  static final boolean CONTENTS_TYPES_FIXED = true;// Contents_type_dynamic feature is currently not supported (2022/1/6 5:11 p.m.).
  static final int CONTENTS_TYPES_MAX = 300;
  static final int CONTENTS_FILE_SIZE_MAX = 200;// Unit is kilobytes.
  static final int CONTENTS_EXPIRE_AFTER = 180;// Unit is seconds.
  static final int DYNAMIC_FOG_RATIO_PERCENTAGE = 10;
  static final int DYNAMIC_FOG_UPDATE_INTERVAL = 300;// Interval at which the number of DFs is checked, replenished, and deleted. Unit is seconds.

  /* ------NODE MOVE SPEED SETTINGS------ */
  static final int MOVE_SPEEDS = 4;// Enter the total number of options for the movement speed.
  static final int[] move_speed_array = {1, 2, 4, 6};// Unit is meters per second.

  /* ------BATTERY SIMULATION SETTINGS------ */
  static final int BATTERY_INIT_MIN_PERCENTAGE = 20;// Upper limit of remaining battery capacity at node creation.
  static final int BATTERY_INIT_MAX_PERCENTAGE = 90;// Minimum battery remaining level at the time of node creation.
  static final int BATTERY_LOW_THRESHOLD_PERCENTAGE = 40;// The lowest remaining battery level of the DF node. When it falls below this level, it leaves the DF node.
  static final double BATTERY_COMSUMPTION_BT_SEND = 0.0007837;
  static final double BATTERY_COMSUMPTION_BT_RECV = 0.0006590;
  static final double BATTERY_COMSUMPTION_WIFI_SEND = 0.0017398;
  static final double BATTERY_COMSUMPTION_WIFI_RECV = 0.0008699;
  static final double BATTERY_COMSUMPTION_CELL_SEND = 0.0069671;
  static final double BATTERY_COMSUMPTION_CELL_RECV = 0.0018519;
}
