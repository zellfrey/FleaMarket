package com.beardedflea.fleamarket.config;

import net.minecraftforge.common.config.*;
// import net.minecraftforge.fml.client.event.ConfigChangedEvent;
// import net.minecraftforge.fml.common.Mod;
// import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Config.RangeInt;

import static com.beardedflea.fleamarket.FleaMarket.*;
/**
 * @author Beardedflea
 */

@Config(modid = MODID, name = "fleamarket/FleaMarket") //--> /config/fleamarket/FleaMarket.cfg
public class FleaMarketConfig{

  @Config.RequiresWorldRestart
  @Config.Name("Reward selection type")
  @Config.Comment({
      "Method of picking an item to be sold",
      "\"descending\" = Starts at the top, and selects to the bottom",
      "\"ascending\" = It's like descending but upwards",
      "\"random\" = Ignores items being selected in a list. Item X could appear twice in a row",
      "\"fairrandom\" = once and item has been used, it will not pick that item again until the list has been completed.",
    })
  public static String selectionType = "descending";
  
  @Config.RequiresWorldRestart
  @Config.Name("Broadcast Reminder")
  @Config.Comment({
      "How often (in minutes) between each item broadcast"
    })
  @RangeInt(min = 0)
  public static int broadCastReminder = 10;

  @Config.RequiresWorldRestart
  @Config.Name("Player join message")
  @Config.Comment({
      "Whether or not the player should receive the broadcast message on joining a server"
    })
  public static boolean joinMessage = true;

  @Config.RequiresWorldRestart
  @Config.Name("Default Reward")
  @Config.Comment({
      "If a reward object is not used for selling an item, the reward listed below will be used as default",
      "[placeholders: %playerName%]"
    })
  public static String defaultReward = "/give %playerName% 3 2";
  
  @Config.RequiresWorldRestart
  @Config.Name("Default Broadcast")
  @Config.Comment({
      "If a broadcast object is not used for selling an item, the broadcast listed below will be used as default",
      "[placeholders: %item%, %amount%]"
    })
  public static String defaultBroadcast = "This is a config test";
  
  @Config.RequiresWorldRestart
  @Config.Name("Default Uptime")
  @Config.Comment({
      "If a uptime object is not used for selling an item, the uptime listed below will be used as default",
      "[placeholders: %item%]"
    })
  @RangeInt(min = 0)
  public static int defaultUptime = 20;

  @Config.RequiresWorldRestart
  @Config.Name("Default sell message")
  @Config.Comment({
      "If a message object is not used for selling an item, the message listed below will be used as default",
      "[placeholders: %item%]"
    })
  public static String defaultSellMessage = "This is a config test";

  
  // public static SubCategory subCatTest = new SubCategory();

  // private static class SubCategory {
  //     public boolean someBool = true; 
  //     public int relatedInt = 10;
  // }

}