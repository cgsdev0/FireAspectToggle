package dev.cgs.fireaspecttoggle;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class FireAspectToggle extends JavaPlugin implements Listener {

    public static boolean isSword(Material m) {
        return switch (m) {
            case DIAMOND_SWORD, WOODEN_SWORD, IRON_SWORD, GOLDEN_SWORD, NETHERITE_SWORD, STONE_SWORD -> true;
            default -> false;
        };
    }

    @EventHandler
    public void onStuffHappened(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (e.getItem() == null) {
            return;
        }
        if (!isSword(e.getItem().getType())) {
            return;
        }
        if(e.getPlayer().getInventory().getItemInOffHand().getType() != Material.AIR) {
            return;
        }
        if (!e.getPlayer().isSneaking()) {
            return;
        }
        ItemMeta meta = e.getItem().getItemMeta();
        if (meta == null) {
            return;
        }
        if (meta.hasEnchant(Enchantment.FIRE_ASPECT)) {
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            assert lore != null;
            lore.add(0, "§r§7Fire Aspect "+(meta.getEnchantLevel(Enchantment.FIRE_ASPECT) == 1 ? "I" : "II") + " (Off)");
            meta.setLore(lore);
            meta.removeEnchant(Enchantment.FIRE_ASPECT);
            e.getItem().setItemMeta(meta);
            e.getPlayer().sendMessage("§6Fire Aspect: Toggled §cOFF");
        }
        else if(meta.hasLore()) {
            List<String> lore = meta.getLore();
            assert lore != null;
            boolean fa2 = lore.removeIf(f -> f.contains("Fire Aspect II (Off)"));
            boolean fa1 = lore.removeIf(f -> f.contains("Fire Aspect I (Off)"));
            if(fa1 || fa2) {
                meta.addEnchant(Enchantment.FIRE_ASPECT, fa1 ? 1 : 2, true);
                meta.setLore(lore);
                e.getItem().setItemMeta(meta);
                e.getPlayer().sendMessage("§6Fire Aspect: Toggled §aON");
            }

        }

    }

    @Override
    public void onEnable(){
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("FireAspectToggle is now enabled!");
    }

    @Override
    public void onDisable(){
        this.getLogger().info("FireAspectToggle is now disabled.");
    }
}
