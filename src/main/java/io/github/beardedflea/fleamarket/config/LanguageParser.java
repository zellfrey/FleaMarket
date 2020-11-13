//package io.github.beardedflea.fleamarket.config;
//
//import io.github.beardedflea.fleamarket.FleaMarket;
//import io.github.beardedflea.fleamarket.utils.TextUtils;
//import net.minecraft.server.MinecraftServer;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import org.apache.commons.io.FileUtils;
//import org.yaml.snakeyaml.Yaml;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//
//public class LanguageParser {
//
//    private static File configDir;
//
//    private static HashMap <String, Object> langYamlMap = new HashMap<>();
//
//    public static void init(FMLPreInitializationEvent event) {
//        configDir = new File(event.getModConfigurationDirectory(),"fleamarket/lang.yaml");
//        FleaMarket.getLogger().info(configDir);
//        if(!configDir.exists()){
//            setupDefaultLangFile(configDir);
//        }
////        TextUtils.populateColourMap();
//        loadModLanguage();
//    }
//
//    public static void loadModLanguage(){
//        Yaml yaml = new Yaml();
//        try{
//            FileReader langFile = new FileReader(configDir);
//            langYamlMap = yaml.load(langFile);
//            langFile.close();
//        }
//        catch(IOException e){
//            FleaMarket.getLogger().error("Exception loading fleamarket language file!", e);
//        }
//
//        //check for nulls, direct yamlObjects to appropriate variables
//        for (String key : langYamlMap.keySet()) {
////          FleaMarket.getLogger().info(langYamlMap.get(key));
//            TextUtils.modLanguageMap.put(key, TextUtils.TransformModLanguage(langYamlMap.get(key)));
//        }
//    }
//
//    private static void setupDefaultLangFile(File langFile) {
//        FleaMarket.getLogger().info("Creating lang file");
//        try {
//            //noinspection ConstantConditions
//            FileUtils.copyToFile(MinecraftServer.class.getClassLoader().getResourceAsStream("assets/" + FleaMarket.MODID + "/lang.yaml"), langFile);
//        }
//        catch (IOException e) {
//            FleaMarket.getLogger().error("Exception setting up fleamarket language file!", e);
//        }
//    }
//}
