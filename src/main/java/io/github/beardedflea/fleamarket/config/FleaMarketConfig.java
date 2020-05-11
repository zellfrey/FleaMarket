package io.github.beardedflea.fleamarket.config;

import net.minecraftforge.common.config.*;
// import net.minecraftforge.fml.client.event.ConfigChangedEvent;
// import net.minecraftforge.fml.common.Mod;
// import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Config.RangeInt;

import static io.github.beardedflea.fleamarket.FleaMarket.*;
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
  @Config.Comment({"How often (in minutes) between each item broadcast. Set to 0 to disable broadcasts"})
  @RangeInt(min = 0)
  public static int broadcastReminder = 10;

  @Config.RequiresWorldRestart
  @Config.Name("sales Interval")
  @Config.Comment({"How long (in minutes) between the next item to be on sale"})
  @RangeInt(min = 0)
  public static int saleInterval = 5;

  @Config.RequiresWorldRestart
  @Config.Name("Player join message")
  @Config.Comment({
      "Whether or not the player should receive the broadcast message of an item on joining a server"
    })
  public static boolean joinMessage = true;

  @Config.RequiresWorldRestart
  @Config.Name("Debug mode")
  @Config.Comment({"Outputs more plugin information onto the console "})
  public static boolean debugMode = true;
 
  @Config.RequiresMcRestart
    @Config.Name("Default Item Fields")
    @Config.Comment({
    "If any of the listed fields are not included within the item.json, they will use these default fields listed below.",
    "If a placeholder is not listed for the field, the message will display an empty message."
  })
  public static ItemFields defaultItemFields = new ItemFields();
    
  public static class ItemFields {
    
    @Config.Comment({"[placeholders: %item%, %amount%, %playerName%]"})
    public String defaultSoldMessage = "You have sold an item!";

    @RangeInt(min = 0, max = 1000)
    @Config.Comment({"How long (in minutes) the item will be up for sale"})
    public int defaultUptime = 20;

    @Config.Comment("[placeholders: %item%, %amount%]")
    public String defaultBroadcast = "This is a broadCast";

    @Config.Comment("[placeholders: %playerName%]")
    public String defaultReward = "/give %playerName% dirt 2";
  }

}