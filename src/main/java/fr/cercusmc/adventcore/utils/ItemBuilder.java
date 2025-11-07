package fr.cercusmc.adventcore.utils;

import fr.cercusmc.adventcore.utils.messages.LoggingCategory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to build custom item
 *
 */
public class ItemBuilder {

    private Material material;
    private String name;
    private List<String> description;
    private final ItemMeta meta;
    private ItemStack itemStack;
    private final Map<Enchantment, Integer> enchantments;
    private int count;
    private final List<ItemFlag> flags;

    /**
     * Create a new ItemBuilder instance
     * @param material The material of the item
     * @param count The number of items
     * @param name The name of the item
     * @param description The description of the item
     * @param enchantments The enchantments of the item
     */
    public ItemBuilder(Material material, int count, String name, List<String> description, Map<Enchantment, Integer> enchantments) {
        this.count = count;
        this.enchantments = enchantments;
        this.name = name;
        this.description = description;
        this.itemStack = new ItemStack(material, count);
        this.meta = this.itemStack.getItemMeta();
        this.flags = new ArrayList<>();
    }

    /**
     * Create a new ItemBuilder instance
     * @param material The material of the item
     * @param count The number of items
     * @param name The name of the item
     * @param description The description of the item
     */
    public ItemBuilder(Material material, int count, String name, List<String> description) {
        this(material, count, name, description, new HashMap<>());
    }

    /**
     * Create a new ItemBuilder instance
     * @param material The material of the item
     * @param count The number of items
     */
    public ItemBuilder(Material material, int count) {
        this(material, count, null, new ArrayList<>());
    }

    /**
     * Create a new ItemBuilder instance
     * @param material The material of the item
     * @param count The number of items
     * @param name The name of the item
     */
    public ItemBuilder(Material material, int count, String name) {
        this(material, count, name, new ArrayList<>(), new HashMap<>());
    }

    /**
     * Create a new ItemBuilder instance
     * @param material The material of the item
     * @return a new ItemBuilder instance
     */
    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        this.itemStack = new ItemStack(material, count);
        return this;
    }

    /**
     * Set the name of the item
     * @param name The name of the item
     * @return this ItemBuilder instance
     */
    public ItemBuilder setName(String name) {
        this.name = name;
        this.meta.setDisplayName(LoggingCategory.getUserMessage().formatMessage(name, new HashMap<>()));
        return this;
    }


    /**
     * Set the description of the item
     * @param description The description of the item
     * @return this ItemBuilder instance
     */
    public ItemBuilder setDescription(List<String> description) {
        this.description = description;
        this.meta.setLore(LoggingCategory.getUserMessage().formatMessages(description, new HashMap<>()));
        return this;
    }

    /**
     * Add an enchantment to the item
     * @param enchantment The enchantment to add
     * @param level The level of the enchantment
     * @return this ItemBuilder instance
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        this.itemStack.addEnchantment(enchantment, level);
        return this;
    }

    /**
     * Remove an enchantment from the item
     * @param enchantment The enchantment to remove
     * @return this ItemBuilder instance
     */
    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.enchantments.remove(enchantment);
        this.itemStack.removeEnchantment(enchantment);
        return this;
    }

    /**
     * Set the count of the item
     * @param count The count of the item
     * @return this ItemBuilder instance
     */
    public ItemBuilder setCount(int count) {
        this.count = count;
        this.itemStack.setAmount(count);
        return this;
    }

    /**
     * Add an item flag to the item
     * @param flag The item flag to add
     * @return this ItemBuilder instance
     */
    public ItemBuilder addFlag(ItemFlag flag) {
        this.flags.add(flag);
        this.meta.addItemFlags(flag);
        return this;
    }

    /**
     * Get the count of the item
     * @return the count of the item
     */
    public int getCount() {
        return count;
    }

    /**
     * Get the name of the item
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the item
     * @return the description of the item
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * Get the enchantments of the item
     * @return the enchantments of the item
     */
    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    /**
     * Get the material of the item
     * @return The material of the item
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Get the item stack of the item
     * @return the item stack of the item
     */
    public ItemStack toItemStack() {
        this.itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Create an ItemBuilder instance from an ItemStack
     * @param itemStack The ItemStack to convert
     * @return a new ItemBuilder instance from the given ItemStack
     */
    public static ItemBuilder toItemBuilder(ItemStack itemStack) {
        return new ItemBuilder(itemStack.getType(), itemStack.getAmount(), itemStack.hasItemMeta()? itemStack.getItemMeta().getDisplayName() : null, itemStack.hasItemMeta()? itemStack.getItemMeta().getLore() : new ArrayList<>(), itemStack.getEnchantments());
    }
}
