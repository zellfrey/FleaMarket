package io.github.beardedflea.fleamarket.utils;


import io.github.beardedflea.fleamarket.FleaMarket;

public class DebugUtils {

    public static void printDebugStrConsole(String... sArgs){
        for(String line : sArgs){
            FleaMarket.getLogger().info(line);
        }
    }
}
