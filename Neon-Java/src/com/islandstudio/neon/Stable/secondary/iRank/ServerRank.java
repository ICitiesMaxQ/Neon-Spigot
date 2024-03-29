package com.islandstudio.neon.stable.secondary.iRank;

import org.bukkit.ChatColor;

public enum ServerRank {

    OWNER(ChatColor.AQUA + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "OWNER" + ChatColor.AQUA + "" + ChatColor.BOLD + "] "),
    VIP_PLUS(ChatColor.WHITE + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "VIP" + ChatColor.GREEN + "+" + ChatColor.WHITE + "" + ChatColor.BOLD + "] "),
    VIP(ChatColor.WHITE + "" + ChatColor.BOLD + "[" + ChatColor.GOLD + "" + ChatColor.BOLD + "VIP" + ChatColor.WHITE + "" + ChatColor.BOLD +"] "),
    MEMBER(ChatColor.WHITE + "" + ChatColor.BOLD + "[" + ChatColor.GRAY + "" + ChatColor.BOLD + "MEMBER" + ChatColor.WHITE + "" + ChatColor.BOLD + "] ");

    private final String prefix;

    ServerRank(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
