package io.github.beardedflea.fleamarket.config;

import net.minecraftforge.common.config.*;
import java.io.File;
import java.io.IOException;
/**
 * @author Beardedflea
 */

//@Config(modid = MODID, name = "fleamarket/FleaMarket") //--> /config/fleamarket/FleaMarket.cfg
public class FleaMarketConfig{

  //Config variables
  boolean debugMode, joinMessage;
  int BroadcastReminder, saleInterval;
  String selectionType;

  private static final String[] selectionTypeArray = {"descending", "ascending", "random", "fairrandom"};
  private static final String[] selectionComments = {
          "Method of picking an item to be sold",
          "\"descending\" = Starts at the top, and selects to the bottom",
          "\"ascending\" = It's like descending but upwards",
          "\"random\" = Ignores items being selected in a list. Item X could appear twice in a row",
          "\"fairrandom\" = once and item has been used, it will not pick that item again until the list has been completed."
          };

//  @Config.RequiresWorldRestart
//  @Config.Name("Reward selection type")
//  @Config.Comment({
//      "Method of picking an item to be sold",
//      "\"descending\" = Starts at the top, and selects to the bottom",
//      "\"ascending\" = It's like descending but upwards",
//      "\"random\" = Ignores items being selected in a list. Item X could appear twice in a row",
//      "\"fairrandom\" = once and item has been used, it will not pick that item again until the list has been completed."
//    })
//  public static String selectionType = "descending";
//
//  @Config.RequiresWorldRestart
//  @Config.Name("Broadcast Reminder")
//  @Config.Comment({"How often (in minutes) between each item broadcast. Set to 0 to disable broadcasts"})
//  @RangeInt(min = 0)
//  public static int broadcastReminder = 0;
//
//  @Config.RequiresWorldRestart
//  @Config.Name("sales Interval")
//  @Config.Comment({"How long (in minutes) between each itemOffer on sale. Set to 0 to skip straight to next ItemOffer"})
//  @RangeInt(min = 0)
//  public static int saleInterval = 5;
//
//  @Config.RequiresWorldRestart
//  @Config.Name("Player join message")
//  @Config.Comment({
//      "Whether or not the player should receive the broadcast message of an item on joining a server"
//    })
//  public static boolean joinMessage = true;
//
//  @Config.RequiresWorldRestart
//  @Config.Name("Debug mode")
//  @Config.Comment({"Outputs more plugin information onto the console "})
//  public static boolean debugMode = false;

  public void load(File configDictionary) throws IOException {
    if (!configDictionary.exists()) {
      configDictionary.mkdir();
    }
    this.loadConfig(configDictionary);
  }

  private void loadConfig(File configDictionary) throws IOException {
    final Configuration configuration = new Configuration(new File(configDictionary, "FleaMarket.cfg"));

//    public Property get(String category, String key, int defaultValue, String comment, int minValue, int maxValue)
    //    configuration.getCategory("General").setComment("This is a test, where will it go\n Hey babe");
    String category = "General";
    String varComment = "Outputs more plugin information onto the console\n";

    this.debugMode =
            configuration.getBoolean("Debug mode",category,  false, varComment);

    varComment = "Whether or not the player should receive the broadcast message of an item on joining a server\n";
    this.joinMessage =
            configuration.getBoolean("Player join message", category,  true, varComment);

    varComment = "How often (in minutes) between each item broadcast. Set to 0 to disable broadcasts\n";
    this.BroadcastReminder =
            configuration.getInt("Broadcast Reminder", category, 0, 0, 1000, varComment);

    varComment = "How long (in minutes) between each itemOffer on sale. Set to 0 to skip straight to next ItemOffer\n";
    this.saleInterval =
            configuration.getInt("Sales Interval",category,  5,  0, 1000,varComment);

    varComment= "";
    for(String comment : selectionComments){
      varComment += comment + "\n";
    }
    this.selectionType =
            configuration.getString("Reward Selection Type", category,"descending", varComment, selectionTypeArray);

    if (configuration.hasChanged()) {
      configuration.save();
    }
  }

  public boolean debugMode(){
    return this.debugMode;
  }

  public boolean joinMessage(){
    return this.joinMessage;
  }

  public int broadcastReminder(){
    return this.BroadcastReminder;
  }

  public int saleInterval(){
    return this.saleInterval;
  }

  public String selectionType(){
    return this.selectionType;
  }

}
//
//# Configuration file
//
//        general {
//        # How often (in minutes) between each item broadcast. Set to 0 to disable broadcasts
//        # Min: 0
//        # Max: 2147483647
//        I:"Broadcast Reminder"=15
//
//        # Outputs more plugin information onto the console
//        B:"Debug mode"=false
//
//        # Whether or not the player should receive the broadcast message of an item on joining a server
//        B:"Player join message"=true
//
//        # Method of picking an item to be sold
//        # "descending" = Starts at the top, and selects to the bottom
//        # "ascending" = It's like descending but upwards
//        # "random" = Ignores items being selected in a list. Item X could appear twice in a row
//        # "fairrandom" = once and item has been used, it will not pick that item again until the list has been completed.
//        S:"Reward selection type"=fairrandom
//
//        # How long (in minutes) between each itemOffer on sale. Set to 0 to skip straight to next ItemOffer
//        # Min: 0
//        # Max: 2147483647
//        I:"sales Interval"=35
//        }

//# Configuration file
//
//        general {
//        # How often (in minutes) between each item broadcast. Set to 0 to disable broadcasts
//        #  [range: 0 ~ 2147483647, default: 0]
//        I:"Broadcast Reminder"=0
//
//        # Outputs more plugin information onto the console
//        #  [default: false]
//        B:"Debug mode"=false
//
//        # Whether or not the player should receive the broadcast message of an item on joining a server
//        #  [default: true]
//        B:"Player join message"=true
//
//        # Method of picking an item to be sold
//        # "descending" = Starts at the top, and selects to the bottom
//        # "ascending" = It's like descending but upwards
//        # "random" = Ignores items being selected in a list. Item X could appear twice in a row
//        # "fairrandom" = once and item has been used, it will not pick that item again until the list has been completed.
//        #  [default: descending]
//        S:"Reward Selection Type"=descending
//
//        # How long (in minutes) between each itemOffer on sale. Set to 0 to skip straight to next ItemOffer
//        #  [range: 0 ~ 2147483647, default: 5]
//        I:"Sales Interval"=5
//        }
