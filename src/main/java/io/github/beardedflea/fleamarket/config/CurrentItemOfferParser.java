package io.github.beardedflea.fleamarket.config;

import com.google.gson.*;
import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.event.EventTickHandler;
import io.github.beardedflea.fleamarket.store.ItemOffer;
import io.github.beardedflea.fleamarket.store.ItemOfferList;
import io.github.beardedflea.fleamarket.utils.TextUtils;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class CurrentItemOfferParser {

    private static File configDir;

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
        FleaMarket.getLogger().info("Loading current Item Offer...");
        File[] currentItemOfferFolder = configDir.listFiles(
                (dir, name) -> name.startsWith("currentItemOffer") && name.endsWith(".json")
        );

        if(currentItemOfferFolder== null) {
            FleaMarket.getLogger().info("No file was found in currentItemOffer folder.");
        }
        else{
            File currentItemOfferFile = currentItemOfferFolder[0];

            if (currentItemOfferFile.length() == 0) {
                FleaMarket.getLogger().warn("currentItemOffer.json is empty! Skipping...");
            }
            else{
                try{
                    JsonObject currentItemObject = parser.parse(new FileReader(currentItemOfferFile)).getAsJsonObject();

                    ItemOffer currentItem = ItemOfferParser.setupItemOfferObjects(currentItemObject, 0, "currentItemOffer.json");
                    if(currentItem == null){
                        FleaMarket.getLogger().error("error loading current item offer object!");
                    }
                    else{
                        ItemOfferList.currentItemOffer = currentItem;
                        ItemOfferList.itemOfferIndex = currentItemObject.get("itemIndex").getAsInt();
                        ItemOfferList.itemOfferUptime = currentItemObject.get("uptimeLeft").getAsInt();

                        EventTickHandler.salesInterval = currentItemObject.get("saleTimeLeft").getAsInt();

                        JsonArray playerIds = currentItemObject.get("playerTransactionList").getAsJsonArray();

                        //it just works: https://stackoverflow.com/questions/18544133/parsing-json-array-into-java-util-list-with-gson
                        String[] idsArray = new Gson().fromJson(playerIds, String[].class);

                        if(idsArray.length !=0){
                            for (String uuid : idsArray) {
                                ItemOfferList.addPlayerTransactionUUID(uuid);
                            }
                        }

                        if(currentItemObject.has("fairRandomArray")){
                            JsonArray fairRandInts = currentItemObject.get("fairRandomArray").getAsJsonArray();
                            int[] fairRandArray = new Gson().fromJson(fairRandInts, int[].class);

                            if(fairRandArray.length !=0){
                                for (int itemIndex : fairRandArray) {
                                    ItemOfferList.addFairRandomArray(itemIndex);
                                }
                            }
                        }

                    }
                }catch (FileNotFoundException e) {
                    FleaMarket.getLogger().error("error parsing current item offer file!", e);
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
            ItemObject.addProperty("saleTimeLeft", EventTickHandler.salesInterval);

            //add playerTransactionList to Json file
            JsonArray playerTransactionArray = new Gson().toJsonTree(ItemOfferList.getPlayerTransactionList()).getAsJsonArray();
            ItemObject.add("playerTransactionList", playerTransactionArray);

            //add fairRandomArray if "fairrandom" is enabled
            if(FleaMarket.config.selectionType().equals("fairrandom")){
                JsonArray fairRandomArray = new Gson().toJsonTree(ItemOfferList.getFairRandomArray()).getAsJsonArray();
                ItemObject.add("fairRandomArray", fairRandomArray);
            }

            try {
                FileWriter file = new FileWriter(configDir+"/currentItemOffer.json");
                file.write(new GsonBuilder().setPrettyPrinting().create().toJson(ItemObject));
                file.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

            if(FleaMarket.isDebugMode()){FleaMarket.getLogger().info(ItemObject.toString());}
        }
    }
}
