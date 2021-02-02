package io.github.beardedflea.fleamarket.utils;

import io.github.beardedflea.fleamarket.FleaMarket;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ModUpdateHandler {

    public static void notifyServer() {
        ForgeVersion.CheckResult result = getResult();
        if(hasUpdate(result)){
            StringBuilder builder = new StringBuilder();
            if(result.changes != null) {
                result.changes.forEach(
                 (version, changes) -> builder.append("\n\t").append(version.toString()).append(":\n\t\t").append(changes)
                );
            }
            String updateForMod = "There's an update available for " + FleaMarket.NAME;
            String resultChanges = StringUtils.isNullOrEmpty(result.url) ? "" : ", download version {} here: {}\nChangelog:{}";
            FleaMarket.getLogger().info(updateForMod + resultChanges, result.target, result.url, builder.toString());
        }
    }

    public static ForgeVersion.CheckResult getResult() {
        return ForgeVersion.getResult(FMLCommonHandler.instance().findContainerFor(FleaMarket.MODID));
    }

    public static boolean hasUpdate(ForgeVersion.CheckResult result) {
        ForgeVersion.Status status = result.status;
        if(status == Status.PENDING || status == Status.FAILED) {
            String statusError = "Error getting update status for {}, found status {}!";
            FleaMarket.getLogger().warn(statusError, FleaMarket.NAME, status.toString());
            return false;
        }
        else {
            return status == ForgeVersion.Status.OUTDATED;
        }
    }
}
