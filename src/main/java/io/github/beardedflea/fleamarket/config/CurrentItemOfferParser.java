package io.github.beardedflea.fleamarket.config;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.ItemOffer;
import io.github.beardedflea.fleamarket.store.ItemOfferList;
import io.github.beardedflea.fleamarket.utils.TextUtils;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Locale;

public class CurrentItemOfferParser {

    public static File configDir;

    private static JsonParser parser = new JsonParser();

    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "fleamarket/currentItemOffer");
        if(!configDir.exists()){
            try {
                FileUtils.forceMkdir(configDir);
            }
            catch (IOException e) {
                FleaMarket.getLogger().error("Exception setting up the current item offer folder!", e);
            }
        }
    }

    public static void loadCurrentItemOffer(){
        File[] currentItemOfferFile = configDir.listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".json"));
        FleaMarket.getLogger().info("Loading current Item Offer...");
        if(currentItemOfferFile == null) {
            //if this returns null, something is seriously wrong.
            FleaMarket.getLogger().info("No current item offer file was found.");
        }
        else{
            for(File jsonFile : currentItemOfferFile) {

                if (jsonFile.length() == 0) {
                    FleaMarket.getLogger().warn("{} is empty! Skipping...", jsonFile.getName());
                }
                else{
                    try{
                        JsonObject currentItemObject = parser.parse(new FileReader(jsonFile)).getAsJsonObject();

                        ItemOffer currentItem = ItemOfferParser.setupItemOfferObjects(currentItemObject, 0, jsonFile.getName());
                        if(currentItem == null){
                            FleaMarket.getLogger().error("error loading current item offer object!");
                        }
                        else{
                            ItemOfferList.currentItemOffer = currentItem;
                            ItemOfferList.itemOfferIndex = currentItemObject.get("itemIndex").getAsInt();
                            ItemOfferList.itemOfferUptime = currentItemObject.get("uptimeLeft").getAsInt();

                            JsonArray playerIds = currentItemObject.get("playerTransactionList").getAsJsonArray();

                            //it just works: https://stackoverflow.com/questions/18544133/parsing-json-array-into-java-util-list-with-gson
                            String[] idsArray = new Gson().fromJson(playerIds, String[].class);

                            if(idsArray.length !=0){

                                for (String uuid : idsArray) {
                                    ItemOfferList.addPlayerTransactionUUID(uuid);
                                }
                            }
                        }
                    }catch (FileNotFoundException e) {
                        FleaMarket.getLogger().error("error parsing current item offer file " + jsonFile.getName() + "!", e);
                    }
                }
            }
        }
    }

    public static void saveCurrentItemOffer(){

        if(ItemOfferList.currentItemOffer != null){
            if(FleaMarket.isDebugMode()){TextUtils.printDebugStrConsole("Saving current Item offer");}

            ItemOffer item = ItemOfferList.currentItemOffer;
            String itemName = item.getItemStack().getItem().getRegistryName().toString();
            int itemMeta = item.getItemStack().getItemDamage();

            JsonObject ItemObject = new JsonObject();

            //Get currentItemOffer values
            ItemObject.addProperty("item", itemName);
            ItemObject.addProperty("damage", itemMeta);
            ItemObject.addProperty("nbt", item.getNbtRaw());
            ItemObject.addProperty("amount", item.getItemAmount());
            ItemObject.addProperty("uptime", item.getUpTime());
            ItemObject.addProperty("broadcastMsg", item.getBroadcastMsg());
            ItemObject.addProperty("soldMsg", item.getSoldMsg());
            ItemObject.addProperty("rewardCommand", item.getRewardCommand());

            //wow thats a lot. Okay we cool, lets move on
            ItemObject.addProperty("uptimeLeft", ItemOfferList.itemOfferUptime);
            ItemObject.addProperty("itemIndex", ItemOfferList.itemOfferIndex);

            //add playerTransactionList to Json file
            JsonArray playerTransactionArray = new Gson().toJsonTree(ItemOfferList.getPlayerTransactionList()).getAsJsonArray();
            ItemObject.add("playerTransactionList", playerTransactionArray);

            try {
                FileWriter file = new FileWriter(configDir+"/currentItemOffer.json");
                file.write(new GsonBuilder().setPrettyPrinting().create().toJson(ItemObject));
                file.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
