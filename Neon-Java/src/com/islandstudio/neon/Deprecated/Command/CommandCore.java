package com.islandstudio.neon.Deprecated.Command;

import com.islandstudio.neon.Experimental.GameModes;
import com.islandstudio.neon.Experimental.GameModeHandler;
import com.islandstudio.neon.Stable.New.iCommand.SyntaxHandler;
import com.islandstudio.neon.Stable.New.Utilities.ProfileHandler;
import com.islandstudio.neon.Stable.New.Utilities.ServerCFGHandler;
import com.islandstudio.neon.Stable.New.features.iRank.ServerRanks;
import com.islandstudio.neon.Stable.New.features.EffectsManager.EffectsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class CommandCore implements Listener, TabExecutor {
    public static boolean moderation = false;

    //Prefix
    public static final String prefix = ChatColor.WHITE + "[" + ChatColor.AQUA + "SERVER" + ChatColor.WHITE + "] ";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_1.getCommandAlias())) {
                Player player = (Player) sender;

                if (player.isOp()) {
                    if (args.length == 1) {
                        String option = args[0];

                        if (option.equalsIgnoreCase("on")) {
                            if (!player.isInvulnerable()) {
                                player.setInvulnerable(true);
                                player.setAllowFlight(true);
                                if (!player.isCollidable()) {
                                    player.setCollidable(false);
                                }
                                if (!moderation) {
                                    moderation = true;
                                }
                                player.sendMessage(ChatColor.GREEN + "Moderation enabled!");
                            } else {
                                player.sendMessage(ChatColor.YELLOW + "Moderation already enabled!");
                            }
                        } else if (option.equalsIgnoreCase("off")) {
                            if (player.isInvulnerable()) {
                                player.setInvulnerable(false);
                                player.setAllowFlight(false);
                                if (player.isCollidable()) {
                                    player.setCollidable(true);
                                }
                                if (moderation) {
                                    moderation = false;
                                }
                                player.sendMessage(ChatColor.RED + "Moderation disabled!");
                            } else {
                                player.sendMessage(ChatColor.YELLOW + "Moderation already enabled!");
                            }
                        } else {
                            SyntaxHandler.sendSyntax(player, 1);
                        }
                    } else {
                        SyntaxHandler.sendSyntax(player, 1);
                    }
                } else {
                    SyntaxHandler.sendSyntax(player, 2);
                }
                return true;
            }

            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_2.getCommandAlias())) {
                Player player = (Player) sender;

                /*if (player.isOp()) {
                    if (args.length == 2) {
                        String gameModeValue = args[0];
                        String target = args[1];

                        if (gameModeValue.equalsIgnoreCase(String.valueOf(GameModes.SURVIVAL_MODE.getGameModeValue()))) {
                            for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
                                if (target.equalsIgnoreCase(onlinePlayers.getName())) {
                                    GameModeHandler.setSurvival(onlinePlayers);
                                }
                            }
                        } else if (gameModeValue.equalsIgnoreCase(String.valueOf(GameModes.CREATIVE_MODE.getGameModeValue()))) {
                            for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
                                if (target.equalsIgnoreCase(onlinePlayers.getName())) {
                                    GameModeHandler.setCreative(onlinePlayers);
                                }
                            }
                        } else if (gameModeValue.equalsIgnoreCase(String.valueOf(GameModes.ADVENTURE_MODE.getGameModeValue()))) {
                            for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
                                if (target.equalsIgnoreCase(onlinePlayers.getName())) {
                                    GameModeHandler.setAdventure(onlinePlayers);
                                }
                            }
                        } else if (gameModeValue.equalsIgnoreCase(String.valueOf(GameModes.SPECTATOR_MODE.getGameModeValue()))) {
                            for (Player onlinePlayers : Bukkit.getServer().getOnlinePlayers()) {
                                if (target.equalsIgnoreCase(onlinePlayers.getName())) {
                                    GameModeHandler.setSpectator(onlinePlayers);
                                }
                            }
                        } else {
                            player.sendRawMessage("Invalid gamemode!");
                        }
                    }
                }*/

                if (player.isOp()) {
                    if (args.length == 1) {
                        String value = args[0];

                        if (value.equalsIgnoreCase(String.valueOf(GameModes.SURVIVAL_MODE.getGameModeValue()))) {
                            GameModeHandler.setSurvival(player);
                        } else if (value.equalsIgnoreCase(String.valueOf(GameModes.CREATIVE_MODE.getGameModeValue()))) {
                            GameModeHandler.setCreative(player);
                        } else if (value.equalsIgnoreCase(String.valueOf(GameModes.ADVENTURE_MODE.getGameModeValue()))) {
                            GameModeHandler.setAdventure(player);
                        } else if (value.equalsIgnoreCase(String.valueOf(GameModes.SPECTATOR_MODE.getGameModeValue()))) {
                            GameModeHandler.setSpectator(player);
                        } else {
                            SyntaxHandler.sendSyntax(player, 1);
                        }
                    } else {
                        SyntaxHandler.sendSyntax(player, 1);
                    }
                } else {
                    SyntaxHandler.sendSyntax(player, 2);
                }
                return true;
            }

            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_3.getCommandAlias())) {
                Player player = (Player) sender;

                try {
                    String playerRank2 = (String) ProfileHandler.getValue(player).get("Rank");

                    if (player.isOp() || playerRank2.equalsIgnoreCase(ServerRanks.OWNER.toString())) {
                        if (player.getFoodLevel() <20 && player.getHealth() < 20) {
                            player.setFoodLevel(20);
                            player.setSaturation(20);
                            player.setHealth(20);
                            player.sendMessage(ChatColor.GREEN + "Your health and hunger have been filled!");
                        } else if (player.getFoodLevel() <20) {
                            player.setFoodLevel(20);
                            player.setSaturation(20);
                            player.sendMessage(ChatColor.GREEN + "Your hunger has been filled!");
                        } else if (player.getHealth() < 0) {
                            player.setHealth(20);
                            player.sendMessage(ChatColor.GREEN + "Your health has been filled!");
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "Regen only available when your health level or food level is below 20!!");
                        }
                    } else {
                        SyntaxHandler.sendSyntax(player, 2);
                    }
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }

            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_4.getCommandAlias())) {
                try {
                    Player player = (Player) sender;
                    String playerRank = (String) ProfileHandler.getValue(player).get("Rank");
                    EffectsManager effectsManager = new EffectsManager();

                    if (player.isOp() || ServerRanks.OWNER.toString().equalsIgnoreCase(playerRank) || ServerRanks.VIP_PLUS.toString().equalsIgnoreCase(playerRank)) {
                        if (!player.isSleeping()) {
                            effectsManager.openEffectManager(player);
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "You can't use Effects Manager while you're sleeping!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You need a higher rank to do that!");
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_5.getCommandAlias())) {
                Player player = (Player) sender;

                player.sendMessage(ChatColor.RED + "This command has been temporarily disabled!");
                return true;
            }

            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_6.getCommandAlias())){
                Player player = (Player) sender;

                if (player.isOp()) {
                    if (args.length == 1) {
                        String setting = args[0];

                        switch (setting) {
                            case "PVP":

                            case "TNT_Protection":

                            case "ChatLogging": {
                                player.sendMessage(ChatColor.YELLOW + "Please provide a value!");
                                break;
                            }

                            default: {
                                SyntaxHandler.sendSyntax(player, 1);
                                break;
                            }
                        }
                    } else if (args.length == 2) {
                        String setting = args[0];
                        String value = args[1];

                        try {
                            ServerCFGHandler.setValue(setting, value, player);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "Please select a configuration to modify!");
                    }
                } else {
                    SyntaxHandler.sendSyntax(player, 2);
                }
                return true;
            }

            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_7.getCommandAlias())) {
                //IWaypoints.setCommandHandler(args, (Player) sender);
                Player player = (Player) sender;
                player.sendMessage(ChatColor.RED + "This command has been temporarily disabled!");
                return true;
            }

            if (cmd.getName().equalsIgnoreCase("test")) {
                Player player = (Player) sender;
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_5.getCommandAlias())) {
//                List<String> option = new ArrayList<>();
//                List<String> playerNames = new ArrayList<>();
//                List<String> ranks = new ArrayList<>();
//
//                if (args.length == 1) {
//                    option.add("set");
//                    option.add("remove");
//                    return option;
//                }
//
//                if (args.length == 2) {
//                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
//                        playerNames.add(player.getName());
//                    }
//                    return playerNames;
//                }
//
//                if (args.length == 3) {
//                    for (ServerRanks serverRanks : ServerRanks.values()) {
//                        ranks.add(serverRanks.name());
//                    }
//                    return ranks;
//                }
            }

            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_6.getCommandAlias())) {
                if (args.length == 1) {
                    try {
                        return ServerCFGHandler.fetchConfigs();
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (args.length == 2) {
                    String value = args[0];

                    List<String> values = new ArrayList<>();

                    switch (value) {
                        case "PVP":

                        case "ChatLogging" :

                        case "iWaypoints-Cross_Dimension": {
                            values.add("true");
                            values.add("false");
                            break;
                        }

                        case "TNT_Protection" : {
                            values.add("0");
                            values.add("1");
                            values.add("2");
                            break;
                        }
                    }
                    return values;
                }
            }

            if (cmd.getName().equalsIgnoreCase(CommandAlias.CMD_7.getCommandAlias())) {
//                switch (args.length) {
//                    case 1: {
//                        List<String> options = new ArrayList<>();
//
//                        options.add("add");
//                        options.add("remove");
//
//                        return options;
//                    }
//
//                    case 2: {
//                        if (args[0].equalsIgnoreCase("remove")) {
//                            try {
//                                IWaypoints.waypointData.put(((Player) sender).getUniqueId().toString(), IWaypoints.getWaypointData());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            return IWaypoints.getWaypointNames((Player) sender);
//                        }
//                    }
//                }
            }
        }

        return null;
    }
}
