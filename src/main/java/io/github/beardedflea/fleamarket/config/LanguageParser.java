package io.github.beardedflea.fleamarket.config;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LanguageParser {

    public static File configDir;

    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(),"\\fleamarket/lang.yaml");
        FleaMarket.getLogger().info(configDir);
        if(!configDir.exists()){
            setupDefaultLangFile(configDir);
        }
        loadModLanguage();
    }

    private static void loadModLanguage(){
//        Yaml yaml = new Yaml();
//        Yaml yamlConstruct = new Yaml(new Constructor(LanguageParser.class));
//        Yaml yamlClassLoader = new Yaml(new CustomClassLoaderConstructor(LanguageParser.class.getClassLoader()));
        try{
            FileReader langFile = new FileReader(configDir);
//            HashMap<String, Object> modLangMap = yaml.load(langFile);
            langFile.close();

//            modLangMap.forEach((k,v) -> FleaMarket.getLogger().info("Key = "
//                    + k + ", Value = " + v));
//            FleaMarket.getLogger().info(modLangMap.get("nested").getClass().getSimpleName());
//            HashMap<String, HashMap<String, Object>>
            FleaMarket.getLogger().info("file load test");
        }

        catch(IOException e){
            FleaMarket.getLogger().error("Exception loading fleamarket language file!", e);
        }

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
