package io.github.beardedflea.fleamarket.config;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.*;
import io.github.beardedflea.fleamarket.utils.TextUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LanguageParser {

    private static File configDir;

    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(),"\\fleamarket/lang.yaml");
        FleaMarket.getLogger().info(configDir);
        if(!configDir.exists()){
            setupDefaultLangFile(configDir);
        }
        TextUtils.populateColourMap();
        loadModLanguage();
    }

    public static void loadModLanguage(){
        Yaml yaml = new Yaml();
        try{
            FileReader langFile = new FileReader(configDir);
            TextUtils.modLanguageMap = yaml.load(langFile);
            langFile.close();
        }
        catch(IOException e){
            FleaMarket.getLogger().error("Exception loading fleamarket language file!", e);
        }
        TextUtils.modLanguageMap.forEach((k,v) -> FleaMarket.getLogger().info(k + " = " + v));
    }

    private static void setupDefaultLangFile(File langFile) {
        FleaMarket.getLogger().info("Creating lang file");
        try {
            //noinspection ConstantConditions
            FileUtils.copyToFile(MinecraftServer.class.getClassLoader().getResourceAsStream("assets/" + FleaMarket.MODID + "/lang.yaml"), langFile);
        }
        catch (IOException e) {
            FleaMarket.getLogger().error("Exception setting up fleamarket language file!", e);
        }
    }
}
