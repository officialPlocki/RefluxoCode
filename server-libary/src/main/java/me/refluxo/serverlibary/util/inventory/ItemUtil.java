package me.refluxo.serverlibary.util.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    private final String displayname;
    private Material material;
    private final List<String> lore = new ArrayList<>();
    private ItemStack item;
    private int amount = 1;
    boolean enchanted = false;

    public ItemUtil(final String displayname, ItemStack item, final String lore) {
        this.displayname = displayname;
        this.item = item;
        this.lore.add(lore);
    }

    public ItemUtil(final String displayname, final Material material, final String lore) {
        this.displayname = displayname;
        this.material = material;
        this.lore.add(lore);
    }

    public ItemUtil setAmount(int count) {
        amount = count;
        return this;
    }

    public ItemUtil setEnchanted(boolean enabled) {
        enchanted = enabled;
        return this;
    }

    public ItemStack buildItem() {
        ItemStack itemstack = null;
        if(item != null) {
            itemstack = item;
        } else {
            itemstack = new ItemStack(this.material);
        }
        final ItemMeta itemMeta = itemstack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayname);
        itemMeta.setLore(lore);
        if(enchanted) {
            itemMeta.addEnchant(Enchantment.DURABILITY,0,true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemstack.setItemMeta(itemMeta);
        itemstack.setAmount(amount);
        return itemstack;
    }

    public ItemStack buildSkull(final String skullowner) {
        final ItemStack itemstack = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta skullMeta = (SkullMeta)itemstack.getItemMeta();
        assert skullMeta != null;
        skullMeta.setDisplayName(displayname);
        skullMeta.setOwner(skullowner);
        skullMeta.setLore(lore);
        itemstack.setItemMeta(skullMeta);
        itemstack.setAmount(amount);
        return itemstack;
    }
}
