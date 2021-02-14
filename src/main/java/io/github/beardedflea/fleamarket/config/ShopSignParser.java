package io.github.beardedflea.fleamarket.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.ShopSign;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;

public class ShopSignParser {

    private static JsonArray shopSignsArray = new JsonArray();

    public static void saveShopSignsData(){

        for(ShopSign sign : ShopSign.shopSigns){
            JsonObject shopSignObject = new JsonObject();

            shopSignObject.addProperty("signID", sign.getSignID());
            shopSignObject.addProperty("creator", sign.getCreator());
            shopSignObject.addProperty("dim", sign.getDimID());
            shopSignObject.addProperty("x", sign.getPos().getX());
            shopSignObject.addProperty("y", sign.getPos().getY());
            shopSignObject.addProperty("z", sign.getPos().getZ());

            shopSignsArray.add(shopSignObject);
        }

        try {
            FileWriter file = new FileWriter(FleaMarket.configDataDir+"/shopsigns.json");
            file.write(new GsonBuilder().setPrettyPrinting().create().toJson(shopSignsArray));
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        shopSignsArray = new JsonArray();
    }

    public static void loadShopSignData(){
        FleaMarket.getLogger().info("Loading shop signs file...");

        File[] shopsignsFolder = FleaMarket.configDataDir.listFiles(
                (dir, name) -> name.startsWith("shopsigns") && name.endsWith(".json")
        );

        if(shopsignsFolder == null || shopsignsFolder.length == 0) {
            FleaMarket.getLogger().info("No file was found in fleamarket data folder.");
        }else{
            try{
                FileReader fileShopSigns = new FileReader(shopsignsFolder[0]);
                JsonArray shopSignsArray = FleaMarket.jsonParser.parse(fileShopSigns).getAsJsonArray();
                FleaMarket.getLogger().info(shopSignsArray.size());
                for(JsonElement element : shopSignsArray){

                    JsonObject shopSignObject = element.getAsJsonObject();

                    int dimID = shopSignObject.get("dim").getAsInt();
                    int posX = shopSignObject.get("x").getAsInt();
                    int posY = shopSignObject.get("y").getAsInt();
                    int posZ = shopSignObject.get("z").getAsInt();
                    boolean doesSignExist = signBlockExists(posX, posY, posZ, dimID);

                    if(doesSignExist){
                        int signID = shopSignObject.get("signID").getAsInt();
                        String signCreator = shopSignObject.get("creator").getAsString();

                        BlockPos signPos = new BlockPos(posX, posY, posZ);
                        ShopSign loadedSign = new ShopSign(signCreator, signID, dimID, signPos);

                        ShopSign.shopSigns.add(loadedSign);
                    }
                }
                ShopSignParser.saveShopSignsData();

            }catch (FileNotFoundException e) {
                FleaMarket.getLogger().error("error parsing current item offer file!", e);
            }
        }
        ShopSign.updateShopSigns();
    }

    private static boolean signBlockExists(int x, int y, int z, int dim){
        MinecraftServer server =  FMLCommonHandler.instance().getMinecraftServerInstance();
        TileEntitySign locatedSign  = (TileEntitySign)server.getWorld(dim).getTileEntity(new BlockPos(x, y, z));

        if(locatedSign != null){
            return true;
        }
        return false;
    }
}
