package io.github.beardedflea.fleamarket.utils;

import io.github.beardedflea.fleamarket.FleaMarket;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.versioning.ComparableVersion;

import java.util.Set;


public class ModUpdateHandler {

    public static void notifyServer() {
        ForgeVersion.CheckResult result = getResult();

        FleaMarket.getLogger().info(result.status);
        FleaMarket.getLogger().info(result.url);
        FleaMarket.getLogger().info(result.target);
        Set<ComparableVersion> versionSet = result.changes.keySet();

        for(ComparableVersion version : versionSet){
            FleaMarket.getLogger().info(version.toString() + ":\n" + result.changes.get(version));
        }
    }

    public static ForgeVersion.CheckResult getResult() {
        return ForgeVersion.getResult(FMLCommonHandler.instance().findContainerFor(FleaMarket.MODID));
    }
}
