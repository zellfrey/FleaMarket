package io.github.beardedflea.fleamarket.config;

import net.minecraftforge.common.config.*;
import java.io.File;
import java.io.IOException;
/**
 * @author Beardedflea
 */

public class FleaMarketConfig{

  //Config common variables
  boolean debugMode, joinMessage;
  int BroadcastReminder, saleInterval;
  String selectionType;

  //Common defaultItemField variables
  int defaultUptime;
  String defaultSoldMessage, defaultBroadcast, defaultReward;

  private static final String[] selectionTypeArray = {"descending", "ascending", "random", "fairrandom"};
  private static final String[] selectionComments = {
          "Method of picking an item to be sold",
          "\"descending\" = Starts at the top, and selects to the bottom",
          "\"ascending\" = It's like descending but upwards",
          "\"random\" = Ignores items being selected in a list. Item X could appear twice in a row",
          "\"fairrandom\" = once and item has been used, it will not pick that item again until the list has been completed."
          };

  public void load(File configDictionary) throws IOException {
    if (!configDictionary.exists()) {
      configDictionary.mkdir();
    }
    this.loadConfig(configDictionary);
  }

  private void loadConfig(File configDictionary) throws IOException {
    final Configuration configuration = new Configuration(new File(configDictionary, "FleaMarket.cfg"));


    String category = "Common";

    loadGeneralConfig("Common", configuration);

    loadDefaultItemFieldsConfig("Default_Item_Fields", configuration);

//    configuration.setCategoryComment("Default_Item_Fields",
//            "If any of the listed fields are not included within the item.json, they will use these default fields listed below.");


    if (configuration.hasChanged()) {
      configuration.save();
    }
  }

  private void loadGeneralConfig(String category, Configuration configuration){

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

  }

  private void loadDefaultItemFieldsConfig(String category, Configuration configuration){
// getString(String name, String category, String defaultValue, String comment)
    String varComment = "placeholders: %item%, %amount%, %playerName%\n";
    this.defaultSoldMessage =
            configuration.getString("Default Sold Message", category,
            "&6%playerName%, you have sold %amount% %item% to Flea Market", varComment);

    varComment = "How long (in minutes) the item will be up for sale\n";
    this.defaultUptime =
            configuration.getInt("Default Uptime", category, 20, 0, 1000, varComment);

    varComment = "#placeholders: %item%, %amount%\n";
    this.defaultBroadcast =
            configuration.getString("Default Broadcast", category,
                    "&bFlea market is buying &l%amount% &r&b%item%", varComment);

    varComment = "placeholders: %playerName%\n";
    this.defaultReward =
            configuration.getString("Default Reward", category,
                    "/give %playerName% dirt 2", varComment);

  }

//  default_item_fields {
//    # #placeholders: %item%, %amount%
//    #  [default: &bFlea market is buying &l%amount% &r&b%item%]
//      S:"Default Broadcast"=&bFlea market is buying &l%amount% &r&b%item%
//
//    # placeholders: %playerName%
//    #  [default: /give %playerName% dirt 2]
//      S:"Default Reward"=/give %playerName% dirt 2
//
//    # placeholders: %item%, %amount%, %playerName%
//    #  [default: &6%playerName%, you have sold %amount% %item% to Flea Market]
//      S:"Default Sold Message"=&6%playerName%, you have sold %amount% %item% to Flea Market
//
//    # How long (in minutes) the item will be up for sale
//    #  [range: 0 ~ 1000, default: 20]
//      I:"Default Uptime"=20
//  }

//  #defaultItemFields - "If any of the listed fields are not included within the item.json, they will use these default fields listed below."
//
//          #placeholders: %item%, %amount%, %playerName%
//  defaultSoldMessage: "&6%playerName%, you have sold %amount% %item% to Flea Market"
//          #How long (in minutes) the item will be up for sale; Range (min = 0, max = 1000)
//  defaultUptime: 20
//          #placeholders: %item%, %amount%
//  defaultBroadcast: "&bFlea market is buying &l%amount% &r&b%item%"
//          #placeholders: %playerName%
//  defaultReward: "/give %playerName% dirt 2"

  //getter methods

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