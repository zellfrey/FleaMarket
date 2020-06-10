package io.github.beardedflea.fleamarket.config;

import io.github.beardedflea.fleamarket.FleaMarket;
import io.github.beardedflea.fleamarket.store.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class LanguageParser {

    public static File configDir;

    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(),"\\fleamarket/lang.yaml");
        FleaMarket.getLogger().info(configDir);
        if(!configDir.exists()){
            setupDefaultLangFile(configDir);
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
