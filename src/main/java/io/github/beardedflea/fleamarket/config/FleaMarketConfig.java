package io.github.beardedflea.fleamarket.config;

import net.minecraftforge.common.config.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
/**
 * @author Beardedflea
 */

public class FleaMarketConfig{

  //Config common variables
  boolean debugMode, joinMessage;
  int BroadcastReminder, saleInterval;
  String selectionType;

  //Config defaultItemField variables
  int defaultUptime;
  String defaultSoldMessage, defaultBroadcast, defaultReward;

  HashMap<String,String> messagesConfigMap = new HashMap<>();

  private static final String defaultItemFieldComment =
          "If any of the listed fields are not included within the item.json, they will use these default fields listed below.";

    private static final String messageFieldComment =
            "Colour coding is possible. For the sake of convenience, bukkit colour codes will be used\n" +
                    "E.g \"&a\" = green. Click the link for more information ---> https://wiki.ess3.net/mc/\n";

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

    loadGeneralConfig("Common", configuration);

    loadDefaultItemFieldsConfig("Default_Item_Fields", configuration);

    configuration.setCategoryComment("Default_Item_Fields", defaultItemFieldComment + "\n" + messageFieldComment);

    loadModMessagesConfig("Messages", configuration);
    configuration.setCategoryComment("Messages", messageFieldComment);

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

    String varComment = "Placeholders: %item%, %amount%, %playerName%\n";
    this.defaultSoldMessage =
            configuration.getString("Default Sold Message", category,
            "&6%playerName%, you have sold %amount% %item% to Flea Market", varComment);

    varComment = "How long (in minutes) the item will be up for sale\n";
    this.defaultUptime =
            configuration.getInt("Default Uptime", category, 20, 0, 1000, varComment);

    varComment = "#Placeholders: %item%, %amount%\n";
    this.defaultBroadcast =
            configuration.getString("Default Broadcast", category,
                    "&bFlea market is buying &l%amount% &r&b%item%", varComment);

    varComment = "As this is a command, color coding does not work. Placeholders: %playerName%\n";
    this.defaultReward =
            configuration.getString("Default Reward", category,
                    "/give %playerName% dirt 2", varComment);

  }

  private void loadModMessagesConfig(String category, Configuration configuration){

    //player Commands
    this.messagesConfigMap.put("fmhelp",
            configuration.get(category + ".playerCommands", "fmhelp",
                    "/fm help - Shows a list of commands for players").getString());

    this.messagesConfigMap.put("fmcheck",
            configuration.get(category + ".playerCommands", "fmcheck",
                    "/fm check - checks the current item on offer").getString());

    this.messagesConfigMap.put("fmsell",
            configuration.get(category + ".playerCommands", "fmsell",
                    "/fm sell - Current method of selling items").getString());

    //player Messages

    this.messagesConfigMap.put("itemOfferNoneMsg",
            configuration.get(category + ".playerMessages", "itemOfferNoneMsg",
                    "&2There is no item offer available at this time").getString());

    this.messagesConfigMap.put("itemOfferFindingMsg",
            configuration.get(category + ".playerMessages", "itemOfferFindingMsg",
                    "Flea market is finding another item to offer").getString());

    this.messagesConfigMap.put("alreadySoldMsg",
            configuration.get(category + ".playerMessages", "alreadySoldMsg",
                    "You have already sold the current item offer to Flea Market").getString());

    this.messagesConfigMap.put("noItemFoundMsg",
            configuration.get(category + ".playerMessages", "noItemFoundMsg",
                    "&cYou do not have the required item on offer").getString());

  }

//  #-------------------------------------------------
//
//          #playerCommands
//  fmhelp: "\n/fm help - Shows a list of commands for players\n"
//  fmcheck: "/fm check - checks the current item on offer\n"
//  fmsell: "/fm sell - Current method of selling items\n"
//
//          #playerMessages - messages displayed to players
//
//  itemOfferNoneMsg: "&2There is no item offer available at this time"
//  itemOfferFindingMsg: "Flea market is finding another item to offer"
//  alreadySoldMsg: "You have already sold the current item offer to Flea Market"
//  noItemFoundMsg: "&cYou do not have the required item on offer"

//  ##########################################################################################################
//          # messages
//#--------------------------------------------------------------------------------------------------------#
//        # Colour coding is possible. For the sake of convenience, bukkit colour codes will be used
//# E.g "&a" = green. Click the link for more information ---> https://wiki.ess3.net/mc/
//          #
//          ##########################################################################################################
//
//  messages {
//
//    playercommands {
//      S:fmcheck=/fm check - checks the current item on offer
//      S:fmhelp=/fm help - Shows a list of commands for players
//      S:fmsell=/fm sell - Current method of selling items
//    }
//
//    playermessages {
//      S:alreadySoldMsg=You have already sold the current item offer to Flea Market
//      S:itemOfferFindingMsg=Flea market is finding another item to offer
//      S:itemOfferNoneMsg=&2There is no item offer available at this time
//      S:noItemFoundMsg=&cYou do not have the required item on offer
//    }
//
//  }


  //Config Common category getter methods

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

  //Config Default Item Fields category getter methods

  public int defaultUptime() { return this.defaultUptime; }

  //Config mod Message map
  public HashMap<String, String> messagesConfigMap() {return this.messagesConfigMap;}
}