package com.islandstudio.neon.Stable.New.Event;

import com.islandstudio.neon.Experimental.DeathFinder.deathFinder;
import com.islandstudio.neon.Stable.New.Utilities.PacketReceiver;
import com.islandstudio.neon.Stable.New.GUI.Initialization.GUIUtilityHandler;
import com.islandstudio.neon.Stable.New.GUI.Interfaces.iWaypoints.Handler;
import com.islandstudio.neon.Stable.New.GUI.Interfaces.iWaypoints.Handler_Removal;
import com.islandstudio.neon.Stable.New.PluginFeatures.RankSystem.RankHandler;
import com.islandstudio.neon.Stable.New.Utilities.ProfileHandler;
import com.islandstudio.neon.Stable.New.Utilities.ServerCfgHandler;
import com.islandstudio.neon.MainCore;
import com.islandstudio.neon.Stable.New.PluginFeatures.RankSystem.RankTags;
import com.islandstudio.neon.Stable.New.GUI.Interfaces.EffectsManager.EffectsManager;
import com.islandstudio.neon.Stable.New.Utilities.ServerHandler;
import com.islandstudio.neon.Stable.Old.PluginFunctions.TNTProtection.ProtectionHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Objects;

public class EventCore implements Listener {
   private final Plugin plug = MainCore.getPlugin(MainCore.class);

    @EventHandler
    public final void onPlayerJoin(PlayerJoinEvent e) throws Exception {
        Player player = e.getPlayer();

        ServerHandler.broadcastJoin(e);
        ProfileHandler.init(player);
        RankTags.setRankTags();
        PacketReceiver.playerInjection(player);
    }

    @EventHandler
    public final void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        ServerHandler.broadcastQuit(e);
        PacketReceiver.playerRemoval(player);
        GUIUtilityHandler.utilityHM.remove(player);
    }

    @EventHandler
    public final void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        Location location = player.getLocation();
        //deathFinder.deathSession(e);

        //LastDeadLocation.update(player);
//        LastDeadLocation.playerLocation().clear();
//        LastDeadLocation.sendLocation(player);
//        LastDeadLocation.setDeadPlayer(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        //deathFinder.testEvent4(e);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        //deathFinder.movementSession(e);
        //InvisibleConfiguration configuration = new InvisibleConfiguration();
        //configuration.interactDetection();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //deathFinder.chestInteraction(e);
    }

    @EventHandler
    public void onPlayerChatting (AsyncPlayerChatEvent e) throws IOException, ParseException {
        Player player = e.getPlayer();
        String messages = e.getMessage();

        e.setCancelled(true);

        RankHandler.setChatTag(player, player.getName(), messages);
    }

    @EventHandler
    public final void onBlockPlaced(BlockPlaceEvent e) throws IOException, ParseException {
        Block blockPlaced = e.getBlockPlaced();

        if (ServerCfgHandler.getValue().get("TNT_Protection").equals(2L)) {
            if (blockPlaced.getType().equals(Material.TNT)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void onEntityExplode(EntityExplodeEvent e) throws IOException, ParseException {
        ProtectionHandler.detectExplosion(e);
    }

    @EventHandler
    public final void entityPlaced(EntityPlaceEvent e) throws IOException, ParseException {
        if (ServerCfgHandler.getValue().get("TNT_Protection").equals(2L)) {
            if (e.getEntityType().equals(EntityType.MINECART_TNT)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public final void entityDamage(EntityDamageByEntityEvent e) throws IOException, ParseException {
        ProtectionHandler.setNoDamage(e);

        /*if ((e.getEntity().getName().equalsIgnoreCase("iDefault")) && (e.getDamager().getName().equalsIgnoreCase("ICities"))) {
            if (e.getDamager().isOp()) {
            }
            System.out.println("Test");
        }*/
    }

    @EventHandler
    public final void itemDrop(PlayerDropItemEvent e) {
        Item droppedItem = e.getItemDrop();
        ItemStack itemStack = droppedItem.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        EffectsManager effectsManager = new EffectsManager();

        if (itemMeta != null) {
            if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_1) || itemMeta.getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_2)
            || itemMeta.getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_3) || itemMeta.getDisplayName().equalsIgnoreCase(effectsManager.REMOVE_BUTTON)) {
                droppedItem.remove();
            }
        }

    }

    @EventHandler
    public final void onInventoryClick(InventoryClickEvent e) {
        Handler.setEventHandler(e);
        Handler_Removal.setEventHandler(e);

        EffectsManager effectsManager = new EffectsManager();
        Player player = (Player) e.getWhoClicked();
        Inventory clickedInventory = e.getClickedInventory();
        Inventory playerInventory = player.getInventory();
        ItemStack itemStack = e.getCurrentItem();

        if (e.getView().getTitle().equalsIgnoreCase(effectsManager.inventoryName)) {
            if (clickedInventory != null) {
                if (clickedInventory.equals(playerInventory)) {
                    e.setCancelled(true);
                }
            }

            if (itemStack == null || !itemStack.hasItemMeta()) {
                e.setCancelled(true);
            }

            if (itemStack != null) {
                switch (itemStack.getType()) {
                    case DIAMOND_PICKAXE: {
                        if (Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_1) && itemStack.getItemMeta().isUnbreakable()) {
                            if (!player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 150, true, true));
                                player.sendMessage(ChatColor.GREEN + "Effect Applied!");
                                e.setCancelled(true);
                            } else {
                                e.setCancelled(true);
                                player.sendMessage(ChatColor.YELLOW + "You already have that effect!!");
                            }

                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plug, player::closeInventory, 0L);
                        } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_2) && itemStack.getItemMeta().isUnbreakable()) {
                            if (!player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 300, true, true));
                                player.sendMessage(ChatColor.GREEN + "Effect Applied!");
                                e.setCancelled(true);
                            } else {
                                e.setCancelled(true);
                                player.sendMessage(ChatColor.YELLOW + "You already have that effect!!");
                            }

                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plug, player::closeInventory, 0L);
                        } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_3) && itemStack.getItemMeta().isUnbreakable()) {
                            if (!player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE,600,true,true));
                                player.sendMessage(ChatColor.GREEN + "Effect Applied!");
                                e.setCancelled(true);
                            } else {
                                e.setCancelled(true);
                                player.sendMessage(ChatColor.YELLOW + "You already have that effect!!");
                            }

                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plug, player::closeInventory, 0L);
                        }
                        break;
                    }

                    case BARRIER: {
                        if (Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.REMOVE_BUTTON) && itemStack.getItemMeta().isUnbreakable()) {
                            if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                player.sendMessage(ChatColor.RED + "Effect Removed!");
                                e.setCancelled(true);
                            } else {
                                e.setCancelled(true);
                                player.sendMessage(ChatColor.YELLOW + "Effect may removed or you don't have effect to be removed!!");
                            }

                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plug, player::closeInventory, 0L);
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public final void onInventoryClose(InventoryCloseEvent e) {
        String name = e.getView().getTitle();
        Player player = (Player) e.getPlayer();

        if (name.equalsIgnoreCase(new Handler_Removal(GUIUtilityHandler.getGUIUtility(player)).getName())) {
            if (Handler_Removal.isClicked) {
                Handler_Removal.isClicked = false;
            } else {
                Handler_Removal.removalListSeparator.remove(player.getUniqueId().toString());
                GUIUtilityHandler.utilityHM.remove(player);
            }
        }

        if (name.equalsIgnoreCase(new Handler(GUIUtilityHandler.getGUIUtility(player)).getName())) {
            if (Handler.isClicked) {
                Handler.isClicked = false;
            } else {
                GUIUtilityHandler.utilityHM.remove(player);
            }
        }

        EffectsManager effectsManager = new EffectsManager();
        Inventory playerInventory = player.getInventory();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plug, () -> {
            ItemStack[] inventoryContents;

            for (int length = (inventoryContents = playerInventory.getContents()).length, i = 0; i < length; ++i) {
                ItemStack contents = inventoryContents[i];

                for (int j = 0; j < 9; ++j) {
                    ItemStack items = playerInventory.getItem(j);

                    if (items == contents && (contents != null && contents.getType().equals(Material.DIAMOND_PICKAXE) && Objects.requireNonNull(contents.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_1)
                    && contents.getItemMeta().isUnbreakable() || (contents != null && contents.getType().equals(Material.DIAMOND_PICKAXE) && Objects.requireNonNull(contents.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_2)
                    && contents.getItemMeta().isUnbreakable() || (contents != null && contents.getType().equals(Material.DIAMOND_PICKAXE) && Objects.requireNonNull(contents.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_3)
                    && contents.getItemMeta().isUnbreakable() || (contents != null && contents.getType().equals(Material.BARRIER) && Objects.requireNonNull(contents.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.REMOVE_BUTTON)
                    && contents.getItemMeta().isUnbreakable())))))
                    {
                        playerInventory.remove(contents);
                        player.updateInventory();
                    }
                }

                if (contents != null && contents.getType().equals(Material.DIAMOND_PICKAXE) && Objects.requireNonNull(contents.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_1)
                        && contents.getItemMeta().isUnbreakable() || (contents != null && contents.getType().equals(Material.DIAMOND_PICKAXE) && Objects.requireNonNull(contents.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_2)
                        && contents.getItemMeta().isUnbreakable() || (contents != null && contents.getType().equals(Material.DIAMOND_PICKAXE) && Objects.requireNonNull(contents.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.EFFECT_3)
                        && contents.getItemMeta().isUnbreakable() || (contents != null && contents.getType().equals(Material.BARRIER) && Objects.requireNonNull(contents.getItemMeta()).getDisplayName().equalsIgnoreCase(effectsManager.REMOVE_BUTTON)
                        && contents.getItemMeta().isUnbreakable()))))
                {
                    playerInventory.remove(contents);
                    player.updateInventory();
                }
            }
        }, 0L);
    }

}
