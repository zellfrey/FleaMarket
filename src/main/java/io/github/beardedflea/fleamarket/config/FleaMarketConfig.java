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
            configuration.get(category,"Default Sold Message",
            "&6%playerName%, you have sold %amount% %item% to Flea Market", varComment).getString();

    varComment = "How long (in minutes) the item will be up for sale\n";
    this.defaultUptime =
            configuration.getInt("Default Uptime", category, 20, 0, 1000, varComment);

    varComment = "#Placeholders: %item%, %amount%\n";
    this.defaultBroadcast =
            configuration.get(category,"Default Broadcast",
                    "&bFlea market is buying &l%amount% &r&b%item%", varComment).getString();

    varComment = "As this is a command, color coding does not work. Placeholders: %playerName%\n";
    this.defaultReward =
            configuration.get(category,"Default Reward",
                    "/give %playerName% dirt 2", varComment).getString();

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

      //op Commands

      this.messagesConfigMap.put("opfmhelp",
              configuration.get(category + ".opCommands", "opfmhelp",
                      "/opfm help - Shows a list of commands for operators").getString());

      this.messagesConfigMap.put("opfmstart",
              configuration.get(category + ".opCommands", "opfmstart",
                      "/opfm start - starts the cycle of item offers").getString());

      this.messagesConfigMap.put("opfmpause",
              configuration.get(category + ".opCommands", "opfmpause",
                      "/opfm pause - Pauses the cycle. Players can still offer the item").getString());

      this.messagesConfigMap.put("opfmskip",
              configuration.get(category + ".opCommands", "opfmskip",
                      "/opfm skip - Moves to the next item on the list").getString());

      this.messagesConfigMap.put("opfmreload",
              configuration.get(category + ".opCommands", "opfmreload",
                      "/opfm reload - Reloads ItemOffers folder & lang.json").getString());

    // op Messages

    this.messagesConfigMap.put("cycleStart",
            configuration.get(category + ".opMessages", "cycleStart",
                    "&aStarting cycle of itemOffers").getString());

    this.messagesConfigMap.put("cycleInProgress",
            configuration.get(category + ".opMessages", "cycleInProgress",
                    "&4ItemOffers cycle has already started").getString());

    this.messagesConfigMap.put("skipItemOffer",
            configuration.get(category + ".opMessages", "skipItemOffer",
                    "&7Moving to next time offer").getString());

    this.messagesConfigMap.put("reloadMod",
            configuration.get(category + ".opMessages", "reloadMod",
                    "&7Reloading Flea Market...").getString());

    this.messagesConfigMap.put("noItemsFound",
            configuration.get(category + ".opMessages", "noItemsFound",
                    "&c&lFound 0 ItemOffers!").getString());

    this.messagesConfigMap.put("noFilesFound",
            configuration.get(category + ".opMessages", "noFilesFound",
                    "&c&lFound 0 ItemOffer files!").getString());

    this.messagesConfigMap.put("reloadFinished",
            configuration.get(category + ".opMessages", "reloadFinished",
                    "&2Finished reloading").getString());

  }

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

  public String defaultSoldMessage(){return this.defaultSoldMessage; }

  public String defaultBroadcast(){return this.defaultBroadcast; }

  public String defaultReward(){return this.defaultReward; }

  //Config mod Message map
  public HashMap<String, String> messagesConfigMap() {return this.messagesConfigMap;}
}