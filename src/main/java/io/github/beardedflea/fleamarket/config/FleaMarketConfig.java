package io.github.beardedflea.fleamarket.config;

import net.minecraftforge.common.config.*;
import java.io.File;
import java.io.IOException;
/**
 * @author Beardedflea
 */

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

  public void load(File configDictionary) throws IOException {
    if (!configDictionary.exists()) {
      configDictionary.mkdir();
    }
    this.loadConfig(configDictionary);
  }

  private void loadConfig(File configDictionary) throws IOException {
    final Configuration configuration = new Configuration(new File(configDictionary, "FleaMarket.cfg"));

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