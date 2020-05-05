package io.github.beardedflea.fleamarket;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
// import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.beardedflea.fleamarket.command.*;
import io.github.beardedflea.fleamarket.config.ItemOfferParser;
import io.github.beardedflea.fleamarket.config.FleaMarketConfig;

@Mod(modid = FleaMarket.MODID, name = FleaMarket.NAME, version = FleaMarket.VERSION, acceptedMinecraftVersions = FleaMarket.MCVERSIONS, acceptableRemoteVersions = "*", serverSideOnly = true)
public class FleaMarket
{
    public static final String MODID = "fleamarket";
    public static final String NAME = "Flea Market";
    public static final String MCVERSIONS = "[1.12, 1.13)";
    public static final String VERSION = "0.12";

    private static final Logger log = LogManager.getLogger(MODID);


    public static Logger getLogger() {
        return log;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ItemOfferParser.init(event);
        log.info("Pre int of Flea market");
    }

    public static boolean isDebugMode(){
        return FleaMarketConfig.debugMode;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        log.info("Flea market test");
    }
    
    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandFleaMarket());
        event.registerServerCommand(new CommandOPFleaMarket());
    }
}