package com.beardedflea.fleamarket;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = FleaMarket.MODID, name = FleaMarket.NAME, version = FleaMarket.VERSION, serverSideOnly = true)
public class FleaMarket
{
    public static final String MODID = "fleamarket";
    public static final String NAME = "Flea Market";
    public static final String VERSION = "0.03";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        logger.info("Flea market test");
    }
}
