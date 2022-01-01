package me.refluxo.serverlibrary.util.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ItemUtil {
    private final String displayName;
    private Material material;
    private final List<String> lore = new ArrayList<>();
    private ItemStack item;
    private int amount = 1;
    boolean enchanted = false;

    public ItemUtil(final String displayName, ItemStack item, final String lore) {
        this.displayName = displayName;
        this.item = item;
        if(!Objects.equals(lore, "")) {
            this.lore.add("\n§eBeschreibung:\n\n\n");
        }
        this.lore.add(lore);
    }

    public ItemUtil(final String displayName, final Material material, final String lore) {
        this.displayName = displayName;
        this.material = material;
        if(!Objects.equals(lore, "")) {
            this.lore.add("\n§eBeschreibung:\n\n\n");
        }
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
        ItemStack itemstack;
        itemstack = Objects.requireNonNullElseGet(item, () -> new ItemStack(this.material));
        final ItemMeta itemMeta = itemstack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        if(enchanted) {
            itemMeta.addEnchant(Enchantment.DURABILITY,0,true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemstack.setItemMeta(itemMeta);
        itemstack.setAmount(amount);
        return itemstack;
    }

    @SuppressWarnings("deprecation")
    public ItemStack buildSkull(final String skullOwner) {
        final ItemStack itemstack = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta skullMeta = (SkullMeta)itemstack.getItemMeta();
        assert skullMeta != null;
        skullMeta.setDisplayName(displayName);
        skullMeta.setOwner(skullOwner);
        skullMeta.setLore(lore);
        itemstack.setItemMeta(skullMeta);
        itemstack.setAmount(amount);
        return itemstack;
    }
}
