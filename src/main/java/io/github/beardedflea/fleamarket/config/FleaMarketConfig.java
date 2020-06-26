package io.github.beardedflea.fleamarket.config;

import net.minecraftforge.common.config.*;
// import net.minecraftforge.fml.client.event.ConfigChangedEvent;
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
    })
  public static String selectionType = "descending";
  
  @Config.RequiresWorldRestart
  @Config.Name("Broadcast Reminder")
  @Config.Comment({"How often (in minutes) between each item broadcast. Set to 0 to disable broadcasts"})
  @RangeInt(min = 0)
  public static int broadcastReminder = 0;

  @Config.RequiresWorldRestart
  @Config.Name("sales Interval")
  @Config.Comment({"How long (in minutes) between each itemOffer on sale. Set to 0 to skip straight to next ItemOffer"})
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
  public static boolean debugMode = false;

}