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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class ItemOfferParser{

    public static File configDir;

    public static JsonParser parser = new JsonParser();

    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "fleamarket/itemoffers");
        if(!configDir.exists()) {
            setupDefaultItemOffers(configDir);
        }
        loadItemOfferData();
    }

    public static void loadItemOfferData(){
        FleaMarket.getLogger().info("Loading FleaMarket item offers");
        ItemOfferList.clearItemOffers();
        File[] jsonFiles = configDir.listFiles((dir, name) -> name.toLowerCase(Locale.ROOT).endsWith(".json"));
        int regCount = 0;

        if(jsonFiles == null) {
            //if this returns null, something is seriously wrong.
            throw new IllegalStateException("error initializing fleamarket, could not list files for " + configDir.getAbsolutePath());

        }
        else if(configDir.listFiles().length == 0){

            FleaMarket.getLogger().error("There were no files in " + configDir.getAbsolutePath());
        }
        else{
            for(File jsonFile : jsonFiles){
                try {
                    JsonObject root = parser.parse(new FileReader(jsonFile)).getAsJsonObject();
                    if(!root.has("itemOffers")){
                        FleaMarket.getLogger().error("cannot parse item offers file {}!", jsonFile.getName());
                        continue;
                    }
                    JsonArray itemOfferArray = root.get("itemOffers").getAsJsonArray();
                    if(itemOfferArray.size() == 0){
                        FleaMarket.getLogger().info("FleaMarket has found itemOffer file {}. But there appears to be 0 items on offer.\nSkipping file", jsonFile.getName());
                    }
                    else{
                        for(int i = 0; i < itemOfferArray.size(); i++){
                            JsonObject object = itemOfferArray.get(i).getAsJsonObject();
                            String item = object.get("item").getAsString();
                            FleaMarket.getLogger().info(item);
                        }

                    }

                }
                catch (FileNotFoundException e) {
                    FleaMarket.getLogger().error("error parsing item offer file " + jsonFile.getName() + "!", e);
                }
            }

        }

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
