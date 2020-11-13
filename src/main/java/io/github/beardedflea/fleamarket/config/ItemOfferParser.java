package io.github.beardedflea.fleamarket.config;


import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.command.CommandBase;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import static io.github.beardedflea.fleamarket.utils.TextUtils.*;

public class ItemOfferParser {

    public static File configDir;

    private static JsonParser parser = new JsonParser();


    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "fleamarket/itemoffers");
        if (!configDir.exists()) {
            setupDefaultItemOffers(configDir);
        }
    }

    public static void loadItemOfferData() {
        FleaMarket.getLogger().info("Loading FleaMarket item offers");
        ItemOfferList.clearItemOffers();
        File[] jsonFiles = configDir.listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".json"));

        if (jsonFiles == null) {
            //if this returns null, something is seriously wrong.
            throw new IllegalStateException("error initializing fleamarket, could not list files for " + configDir.getAbsolutePath());
        } else {
            for (File jsonFile : jsonFiles) {

                if (jsonFile.length() == 0) {
                    FleaMarket.getLogger().warn("{} is empty! Skipping...", jsonFile.getName());
                } else {
                    try {
                        JsonObject root = parser.parse(new FileReader(jsonFile)).getAsJsonObject();
                        if (!root.has("itemOffers")) {
                            FleaMarket.getLogger().error("cannot parse item offers file {}!", jsonFile.getName());
                            continue;
                        }
                        JsonArray itemOfferArray = root.get("itemOffers").getAsJsonArray();
                        if (itemOfferArray.size() == 0) {
                            FleaMarket.getLogger().info("FleaMarket has found itemOffer file {}. But there appears to be 0 items on offer.\nSkipping file", jsonFile.getName());
                        } else {
                            for (int i = 0; i < itemOfferArray.size(); i++) {
                                JsonObject object = itemOfferArray.get(i).getAsJsonObject();

                                ItemOffer itemOffer = setupItemOfferObjects(object, i, jsonFile.getName());

                                if(itemOffer == null){
                                    FleaMarket.getLogger().error("error loading item offer object!");
                                }
                                else{
                                    ItemOfferList.addItemOffer(itemOffer);
                                }
                            }
                        }
                    }catch (FileNotFoundException e) {
                        FleaMarket.getLogger().error("error parsing item offer file " + jsonFile.getName() + "!", e);
                    }
                }
            }
        }
        if (ItemOfferList.getItemOfferSize() == 0) {
            FleaMarket.getLogger().error("FleaMarket found no itemOffers. Is this intentional?");
        }
        FleaMarket.getLogger().info("FleaMarket registered a total of {} itemOffers in {} files!", ItemOfferList.getItemOfferSize(), jsonFiles.length);
    }

    //Creates ItemOffers parsed from itemOffers.json files
    public static ItemOffer setupItemOfferObjects(JsonObject object, int idx, String fileName) {
        ItemOffer itemOffer;

        if (!object.has("item")) {
            FleaMarket.getLogger().info("ItemOffer object {} in {} does not contain \"item\" field. Skipping...", idx, fileName);
        } else if (!object.has("amount")) {
            FleaMarket.getLogger().info("ItemOffer object {} in {} does not contain \"amount\" field. Skipping...", idx, fileName);
        } else {
            try {
                Item item = CommandBase.getItemByText(null, object.get("item").getAsString());
                int amount = object.get("amount").getAsInt();

                //nbt is null if not included. if no metadata found, falls to 0
                int dmgValue = object.has("damage") ? object.get("damage").getAsInt() : 0;
                String nbtRaw = object.has("nbt") ? object.get("nbt").getAsString() : null;

                //get itemOffer variables. If fields aren't included, will fallback to default values in lang.yaml file and/or fleamarket.cfg
                int uptime =
                        object.has("uptime") ? object.get("uptime").getAsInt() : FleaMarket.config.defaultUptime();
                String broadcastMsg =
                        object.has("broadcastMsg") ? object.get("broadcastMsg").getAsString() : FleaMarket.config.defaultBroadcast();
                String soldMsg =
                        object.has("soldMsg") ? object.get("soldMsg").getAsString() : FleaMarket.config.defaultSoldMessage();
                String rewardCmd =
                        object.has("rewardCommand") ? object.get("rewardCommand").getAsString() : FleaMarket.config.defaultReward();

                //Iterate through strings to get MOTD colour format
                broadcastMsg = TransformModLanguage(broadcastMsg);
                soldMsg = TransformModLanguage(soldMsg);
                rewardCmd = TransformModLanguage(rewardCmd);

                itemOffer = new ItemOffer(item, dmgValue, nbtRaw, amount, soldMsg, broadcastMsg, rewardCmd, uptime);
                return itemOffer;

            } catch (NumberInvalidException e) {
                FleaMarket.getLogger().error("error finding item name " + object.get("item").getAsString() + "!", e);
            }

        }
        return null;
    }

    private static void setupDefaultItemOffers(File itemOffersDir) {
        File defaultConfig = new File(itemOffersDir, "default_item_offers.json");
        try {
            FileUtils.forceMkdir(itemOffersDir);
            //noinspection ConstantConditions
            FileUtils.copyToFile(MinecraftServer.class.getClassLoader().getResourceAsStream("assets/" + FleaMarket.MODID + "/itemoffers/default_item_offers.json"), defaultConfig);
        }
        catch (IOException e) {
            FleaMarket.getLogger().error("Exception setting up the default item offer config!", e);
        }
    }
}
