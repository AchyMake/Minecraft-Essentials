package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.entity.*;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EntityHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private RandomHandler getRandomHandler() {
        return getInstance().getRandomHandler();
    }
    public List<CreatureSpawnEvent.SpawnReason> getSpawnReasons() {
        return new ArrayList<>(Arrays.asList(CreatureSpawnEvent.SpawnReason.values()));
    }
    /**
     * get file of entity/entityType.yml
     * @param livingEntity livingEntity
     * @return persistentDataContainer
     * @since many moons ago
     */
    public PersistentDataContainer getData(LivingEntity livingEntity) {
        return livingEntity.getPersistentDataContainer();
    }
    /**
     * get file of entity/entityType.yml
     * @param entityType entityType
     * @return file
     * @since many moons ago
     */
    public File getFile(EntityType entityType) {
        return new File(getInstance().getDataFolder(), "entity/" + entityType.name().toUpperCase() + ".yml");
    }
    /**
     * exists
     * @param entityType entityType
     * @return true if file exists else false
     * @since many moons ago
     */
    public boolean exists(EntityType entityType) {
        return getFile(entityType).exists();
    }
    /**
     * get config
     * @param entityType entityType
     * @return config
     * @since many moons ago
     */
    public FileConfiguration getConfig(EntityType entityType) {
        return YamlConfiguration.loadConfiguration(getFile(entityType));
    }
    /**
     * get entityType
     * @param entityTypeString string
     * @return entityType else null if entityType does not exist
     * @since many moons ago
     */
    public EntityType getType(String entityTypeString) {
        return EntityType.valueOf(entityTypeString.toUpperCase());
    }
    /**
     * is hostile
     * @param entityType livingEntity
     * @return true if entityType is hostile else false
     * @since many moons ago
     */
    public boolean isHostile(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.hostile");
    }
    /**
     * is friendly this is the opposite of hostile
     * @param entityType entityType
     * @return true if entityType is friendly else false
     * @since many moons ago
     */
    public boolean isFriendly(EntityType entityType) {
        return !isHostile(entityType);
    }
    /**
     * get chunk limit of entityType
     * @param entityType entityType
     * @return integer
     * @since many moons ago
     */
    public int getChunkLimit(EntityType entityType) {
        return getConfig(entityType).getInt("settings.chunk-limit");
    }
    /**
     * get chunk limit of entityType
     * @param entity entity
     * @return true if chunk is over entity limit else false
     * @since many moons ago
     */
    public boolean isOverChunkLimit(Entity entity) {
        var type = entity.getType();
        var chunkLimit = getChunkLimit(type);
        if (chunkLimit > 0) {
            var chunk = entity.getLocation().getChunk();
            var listed = new ArrayList<Entity>();
            for (var entities : chunk.getEntities()) {
                if (entities.getType().equals(type)) {
                    listed.add(entities);
                }
            }
            return listed.size() >= chunkLimit;
        } else return false;
    }
    /**
     * is creature spawn disabled
     * @param entityType entityType
     * @return true if entityType is disabled for spawning else false
     * @since many moons ago
     */
    public boolean isCreatureSpawnDisabled(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-spawn");
    }
    /**
     * is entity block form disabled
     * @param entityType entityType
     * @return true if entityType is disabled for block forming else false
     * @since many moons ago
     */
    public boolean isEntityBlockFormDisabled(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-block-form");
    }
    /**
     * is entity change block disabled
     * @param entityType entityType
     * @return true if entityType is disabled for block changing else false
     * @since many moons ago
     */
    public boolean isEntityChangeBlockDisabled(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-change-block");
    }
    /**
     * is entity damage disabled
     * @param entityType entityType
     * @return true if entityType is disabled for block changing else false
     * @since many moons ago
     */
    public boolean isEntityDamageByEntityDisabled(EntityType entityType, EntityType targetEntityType) {
        return getConfig(entityType).getBoolean("settings.disable-entity-damage." + targetEntityType);
    }
    /**
     * is entity explode disabled
     * @param entityType entityType
     * @return true if entityType is disabled for explode else false
     * @since many moons ago
     */
    public boolean isEntityExplodeDisabled(EntityType entityType) {
        return getConfig(entityType).getBoolean("settings.disable-explode");
    }
    /**
     * is entity interact disabled
     * @param entityType entityType
     * @param material material
     * @return true if entityType is disabled for interact else false
     * @since many moons ago
     */
    public boolean isEntityInteractDisabled(EntityType entityType, Material material) {
        return getConfig(entityType).getBoolean("settings.disable-interact." + material.toString());
    }
    /**
     * is entity target disabled
     * @param entityType entityType
     * @param targetEntityType targetEntityType
     * @return true if entityType is disabled for target targetEntityType else false
     * @since many moons ago
     */
    public boolean isEntityTargetDisabled(EntityType entityType, EntityType targetEntityType) {
        return getConfig(entityType).getBoolean("settings.disable-target." + targetEntityType);
    }
    /**
     * is entity hanging break disabled
     * @param entityType entityType
     * @param hangingEntityType hangingEntityType
     * @return true if removerEntityType is disabled for breaking hangingEntityType else false
     * @since many moons ago
     */
    public boolean disableHangingBreakByEntity(EntityType entityType, EntityType hangingEntityType) {
        return getConfig(entityType).getBoolean("settings.disable-hanging-break." + hangingEntityType);
    }
    /**
     * is entity spawn reason disabled
     * @param entityType entityType
     * @param spawnReason spawnReason
     * @return true if entityType is disabled for spawn by spawnReason else false
     * @since many moons ago
     */
    public boolean isSpawnReasonDisabled(EntityType entityType, CreatureSpawnEvent.SpawnReason spawnReason) {
        return getConfig(entityType).getBoolean("settings.disable-spawn-reason." + spawnReason.toString());
    }
    /**
     * set integer
     * @param entityType entityType
     * @param path path
     * @param value integer
     * @since many moons ago
     */
    public boolean setInt(EntityType entityType, String path, int value) {
        var file = getFile(entityType);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * set boolean
     * @param entityType entityType
     * @param path path
     * @param value boolean
     * @since many moons ago
     */
    public boolean setBoolean(EntityType entityType, String path, boolean value) {
        var file = getFile(entityType);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * get dropped exp
     * @param livingEntity livingEntity
     * @return integer
     * @since many moons ago
     */
    public int getDroppedEXP(LivingEntity livingEntity) {
        var value = getData(livingEntity).get(getInstance().getKey("exp"), PersistentDataType.INTEGER);
        if (value != null) {
            return value;
        } else return 0;
    }
    /**
     * set dropped exp
     * @param livingEntity livingEntity
     * @param value integer
     * @since many moons ago
     */
    public void setDroppedEXP(LivingEntity livingEntity, int value) {
        getData(livingEntity).set(getInstance().getKey("exp"), PersistentDataType.INTEGER, value);
    }
    private ArrayList<Map.Entry<String, Double>> getChances(LivingEntity livingEntity) {
        var chances = new HashMap<String, Double>();
        var config = getConfig(livingEntity.getType());
        var section = config.getConfigurationSection("chances");
        if (section != null) {
            for (var key : section.getKeys(false)) {
                var chance = section.getDouble(key + ".chance");
                if (chance > 0) {
                    chances.put(key, chance);
                }
            }
        }
        var listed = new ArrayList<>(chances.entrySet());
        listed.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        return listed;
    }
    public boolean isNamed(LivingEntity livingEntity) {
        return getData(livingEntity).has(getInstance().getKey("named"));
    }
    public void setNamed(LivingEntity livingEntity) {
        getData(livingEntity).set(getInstance().getKey("named"), PersistentDataType.BOOLEAN, true);
    }
    /**
     * set equipment this is the level section from entity/entityType.yml
     * @param livingEntity livingEntity
     * @since many moons ago
     */
    public void setEquipment(LivingEntity livingEntity) {
        var entityType = livingEntity.getType();
        if (!exists(entityType))return;
        var chances = getChances(livingEntity);
        if (chances.isEmpty())return;
        var config = getConfig(entityType);
        for (var listed : chances) {
            var chance = listed.getValue();
            if (!getRandomHandler().isTrue(chance))return;
            var key = listed.getKey();
            var section = "chances." + key;
            var name = config.getString(section + ".name");
            if (name != null) {
                livingEntity.setCustomName(getMessage().addColor(name));
                setNamed(livingEntity);
            }
            if (config.isDouble(section + ".armor")) {
                getAttribute(livingEntity, Attribute.ARMOR).setBaseValue(config.getDouble(section + ".armor"));
            } else if (config.isDouble(section + ".armor.min") && config.isDouble(section + ".armor.max")) {
                getAttribute(livingEntity, Attribute.ARMOR).setBaseValue(getRandomHandler().nextDouble(config.getDouble(section + ".armor.min"), config.getDouble(section + ".armor.max")));
            }
            if (config.isDouble(section + ".attack_damage")) {
                getAttribute(livingEntity, Attribute.ATTACK_DAMAGE).setBaseValue(config.getDouble(section + ".attack_damage"));
            } else if (config.isDouble(section + ".attack_damage.min") && config.isDouble(section + ".attack_damage.max")) {
                getAttribute(livingEntity, Attribute.ATTACK_DAMAGE).setBaseValue(getRandomHandler().nextDouble(config.getDouble(section + ".attack_damage.min"), config.getDouble(section + ".attack_damage.max")));
            }
            if (config.isDouble(section + ".health")) {
                var health = config.getDouble(section + ".health");
                getAttribute(livingEntity, Attribute.MAX_HEALTH).setBaseValue(health);
                livingEntity.setHealth(health);
            } else if (config.isDouble(section + ".health.min") && config.isDouble(section + ".health.max")) {
                var health = getRandomHandler().nextDouble(config.getDouble(section + ".health.min"), config.getDouble(section + ".health.max"));
                getAttribute(livingEntity, Attribute.MAX_HEALTH).setBaseValue(health);
                livingEntity.setHealth(health);
            }
            if (config.isDouble(section + ".movement_speed")) {
                getAttribute(livingEntity, Attribute.MOVEMENT_SPEED).setBaseValue(config.getDouble(section + ".movement_speed"));
            } else if (config.isDouble(section + ".movement_speed.min") && config.isDouble(section + ".movement_speed.max")) {
                getAttribute(livingEntity, Attribute.MOVEMENT_SPEED).setBaseValue(getRandomHandler().nextDouble(config.getDouble(section + ".movement_speed.min"), config.getDouble(section + ".movement_speed.max")));
            }
            if (config.isDouble(section + ".scale")) {
                getAttribute(livingEntity, Attribute.SCALE).setBaseValue(config.getDouble(section + ".scale"));
            } else if (config.isDouble(section + ".scale.min") && config.isDouble(section + ".scale.max")) {
                getAttribute(livingEntity, Attribute.SCALE).setBaseValue(getRandomHandler().nextDouble(config.getDouble(section + ".scale.min"), config.getDouble(section + ".scale.max")));
            }
            if (config.isInt(section + ".exp")) {
                setDroppedEXP(livingEntity, config.getInt(section + ".exp"));
            } else if (config.isInt(section + ".exp.min") && config.isInt(section + ".exp.max")) {
                setDroppedEXP(livingEntity, getRandomHandler().nextInt(config.getInt(section + ".exp.min"), config.getInt(section + ".exp.max")));
            }
            var equipment = livingEntity.getEquipment();
            if (equipment != null) {
                if (config.isString(section + ".main-hand.type") && config.isInt(section + ".main-hand.amount")) {
                    var itemName = config.getString(section + ".main-hand.type");
                    var itemAmount = config.getInt(section + ".main-hand.amount");
                    var itemStack = getMaterialHandler().getItemStack(itemName, itemAmount);
                    if (itemStack != null) {
                        if (config.isConfigurationSection(section + ".main-hand.enchantments")) {
                            var itemMeta = itemStack.getItemMeta();
                            config.getConfigurationSection(section + ".main-hand.enchantments").getKeys(false).forEach(enchantment -> {
                                if (!config.isInt(section + ".main-hand.enchantments." + enchantment))return;
                                var enchantLvl = config.getInt(section + ".main-hand.enchantments." + enchantment);
                                itemMeta.addEnchant(getMaterialHandler().getEnchantment(enchantment), enchantLvl, true);
                            });
                            itemStack.setItemMeta(itemMeta);
                        }
                        equipment.setItemInMainHand(itemStack);
                        if (config.isDouble(section + ".main-hand.drop-chance")) {
                            equipment.setItemInMainHandDropChance((float) config.getDouble(section + ".main-hand.drop-chance"));
                        }
                    }
                }
                if (config.isString(section + ".off-hand.type") && config.isInt(section + ".off-hand.amount")) {
                    var itemName = config.getString(section + ".off-hand.type");
                    var itemAmount = config.getInt(section + ".off-hand.amount");
                    var itemStack = getMaterialHandler().getItemStack(itemName, itemAmount);
                    if (itemStack != null) {
                        if (config.isConfigurationSection(section + ".off-hand.enchantments")) {
                            var itemMeta = itemStack.getItemMeta();
                            config.getConfigurationSection(section + ".off-hand.enchantments").getKeys(false).forEach(enchantment -> {
                                if (!config.isInt(section + ".off-hand.enchantments." + enchantment))return;
                                var enchantLvl = config.getInt(section + ".off-hand.enchantments." + enchantment);
                                itemMeta.addEnchant(getMaterialHandler().getEnchantment(enchantment), enchantLvl, true);
                            });
                            itemStack.setItemMeta(itemMeta);
                        }
                        equipment.setItemInOffHand(itemStack);
                        if (config.isDouble(section + ".off-hand.drop-chance")) {
                            equipment.setItemInOffHandDropChance((float) config.getDouble(section + ".off-hand.drop-chance"));
                        }
                    }
                }
                if (config.isString(section + ".helmet.type") && config.isInt(section + ".helmet.amount")) {
                    var itemName = config.getString(section + ".helmet.type");
                    var itemAmount = config.getInt(section + ".helmet.amount");
                    var itemStack = getMaterialHandler().getItemStack(itemName, itemAmount);
                    if (itemStack != null) {
                        if (config.isConfigurationSection(section + ".helmet.enchantments")) {
                            var itemMeta = itemStack.getItemMeta();
                            config.getConfigurationSection(section + ".helmet.enchantments").getKeys(false).forEach(enchantment -> {
                                if (!config.isInt(section + ".helmet.enchantments." + enchantment))return;
                                var enchantLvl = config.getInt(section + ".helmet.enchantments." + enchantment);
                                itemMeta.addEnchant(getMaterialHandler().getEnchantment(enchantment), enchantLvl, true);
                            });
                            itemStack.setItemMeta(itemMeta);
                        }
                        equipment.setHelmet(itemStack);
                        if (config.isDouble(section + ".helmet.drop-chance")) {
                            equipment.setHelmetDropChance((float) config.getDouble(section + ".helmet.drop-chance"));
                        }
                    }
                }
                if (config.isString(section + ".chestplate.type") && config.isInt(section + ".chestplate.amount")) {
                    var itemName = config.getString(section + ".chestplate.type");
                    var itemAmount = config.getInt(section + ".chestplate.amount");
                    var itemStack = getMaterialHandler().getItemStack(itemName, itemAmount);
                    if (itemStack != null) {
                        if (config.isConfigurationSection(section + ".chestplate.enchantments")) {
                            var itemMeta = itemStack.getItemMeta();
                            config.getConfigurationSection(section + ".chestplate.enchantments").getKeys(false).forEach(enchantment -> {
                                if (!config.isInt(section + ".chestplate.enchantments." + enchantment))return;
                                var enchantLvl = config.getInt(section + ".chestplate.enchantments." + enchantment);
                                itemMeta.addEnchant(getMaterialHandler().getEnchantment(enchantment), enchantLvl, true);
                            });
                            itemStack.setItemMeta(itemMeta);
                        }
                        equipment.setChestplate(itemStack);
                        if (config.isDouble(section + ".chestplate.drop-chance")) {
                            equipment.setChestplateDropChance((float) config.getDouble(section + ".chestplate.drop-chance"));
                        }
                    }
                }
                if (config.isString(section + ".leggings.type") && config.isInt(section + ".leggings.amount")) {
                    var itemName = config.getString(section + ".leggings.type");
                    var itemAmount = config.getInt(section + ".leggings.amount");
                    var itemStack = getMaterialHandler().getItemStack(itemName, itemAmount);
                    if (itemStack != null) {
                        if (config.isConfigurationSection(section + ".leggings.enchantments")) {
                            var itemMeta = itemStack.getItemMeta();
                            config.getConfigurationSection(section + ".leggings.enchantments").getKeys(false).forEach(enchantment -> {
                                if (!config.isInt(section + ".leggings.enchantments." + enchantment))return;
                                var enchantLvl = config.getInt(section + ".leggings.enchantments." + enchantment);
                                itemMeta.addEnchant(getMaterialHandler().getEnchantment(enchantment), enchantLvl, true);
                            });
                            itemStack.setItemMeta(itemMeta);
                        }
                        equipment.setLeggings(itemStack);
                        if (config.isDouble(section + ".leggings.drop-chance")) {
                            equipment.setLeggingsDropChance((float) config.getDouble(section + ".leggings.drop-chance"));
                        }
                    }
                }
                if (config.isString(section + ".boots.type") && config.isInt(section + ".boots.amount")) {
                    var itemName = config.getString(section + ".boots.type");
                    var itemAmount = config.getInt(section + ".boots.amount");
                    var itemStack = getMaterialHandler().getItemStack(itemName, itemAmount);
                    if (itemStack != null) {
                        if (config.isConfigurationSection(section + ".boots.enchantments")) {
                            var itemMeta = itemStack.getItemMeta();
                            config.getConfigurationSection(section + ".boots.enchantments").getKeys(false).forEach(enchantment -> {
                                if (!config.isInt(section + ".boots.enchantments." + enchantment))return;
                                var enchantLvl = config.getInt(section + ".boots.enchantments." + enchantment);
                                itemMeta.addEnchant(getMaterialHandler().getEnchantment(enchantment), enchantLvl, true);
                            });
                            itemStack.setItemMeta(itemMeta);
                        }
                        equipment.setBoots(itemStack);
                        if (config.isDouble(section + ".boots.drop-chance")) {
                            equipment.setBootsDropChance((float) config.getDouble(section + ".boots.drop-chance"));
                        }
                    }
                }
            }
            break;
        }
    }
    public double getAttributeValue(LivingEntity livingEntity, Attribute attribute) {
        var value = livingEntity.getAttribute(attribute);
        if (value != null) {
            return value.getBaseValue();
        } else return 0.0;
    }
    public AttributeInstance getAttribute(LivingEntity livingEntity, Attribute attribute) {
        return livingEntity.getAttribute(attribute);
    }
    public void copyStats(LivingEntity before, LivingEntity after) {
        copyAttackDamage(before, after);
        copyScale(before, after);
        copyHealth(before, after);
    }
    public void copyAttackDamage(LivingEntity before, LivingEntity after) {
        var afterAttribute = getAttribute(after, Attribute.ATTACK_DAMAGE);
        if (afterAttribute != null) {
            var beforeValue = getAttributeValue(before, Attribute.ATTACK_DAMAGE);
            if (beforeValue > 0) {
                afterAttribute.setBaseValue(beforeValue);
            }
        }
    }
    public void copyHealth(LivingEntity before, LivingEntity after) {
        var afterAttribute = getAttribute(after, Attribute.MAX_HEALTH);
        if (afterAttribute != null) {
            var beforeValue = getAttributeValue(before, Attribute.MAX_HEALTH);
            if (beforeValue > 0) {
                afterAttribute.setBaseValue(beforeValue);
            }
        }
    }
    public void copyScale(LivingEntity before, LivingEntity after) {
        var afterAttribute = getAttribute(after, Attribute.SCALE);
        if (afterAttribute != null) {
            var beforeValue = getAttributeValue(before, Attribute.SCALE);
            if (beforeValue > 0) {
                afterAttribute.setBaseValue(beforeValue);
            }
        }
    }
    /**
     * reload entity folder
     * @since many moons ago
     */
    public void reload() {
        new ACACIA_BOAT().reload();
        new ACACIA_CHEST_BOAT().reload();
        new ALLAY().reload();
        new AREA_EFFECT_CLOUD().reload();
        new ARMADILLO().reload();
        new ARMOR_STAND().reload();
        new ARROW().reload();
        new AXOLOTL().reload();
        new BAMBOO_CHEST_RAFT().reload();
        new BAMBOO_RAFT().reload();
        new BAT().reload();
        new BEE().reload();
        new BIRCH_BOAT().reload();
        new BIRCH_CHEST_BOAT().reload();
        new BLAZE().reload();
        new BLOCK_DISPLAY().reload();
        new BOGGED().reload();
        new BREEZE().reload();
        new BREEZE_WIND_CHARGE().reload();
        new CAMEL().reload();
        new CAMEL_HUSK().reload();
        new CAT().reload();
        new CAVE_SPIDER().reload();
        new CHERRY_BOAT().reload();
        new CHERRY_CHEST_BOAT().reload();
        new CHEST_MINECART().reload();
        new CHICKEN().reload();
        new COD().reload();
        new COMMAND_BLOCK_MINECART().reload();
        new COPPER_GOLEM().reload();
        new COW().reload();
        new CREAKING().reload();
        new CREEPER().reload();
        new DARK_OAK_BOAT().reload();
        new DARK_OAK_CHEST_BOAT().reload();
        new DOLPHIN().reload();
        new DONKEY().reload();
        new DRAGON_FIREBALL().reload();
        new DROWNED().reload();
        new EGG().reload();
        new ELDER_GUARDIAN().reload();
        new END_CRYSTAL().reload();
        new ENDER_DRAGON().reload();
        new ENDER_PEARL().reload();
        new ENDERMAN().reload();
        new ENDERMITE().reload();
        new EVOKER().reload();
        new EVOKER_FANGS().reload();
        new EXPERIENCE_BOTTLE().reload();
        new EXPERIENCE_ORB().reload();
        new EYE_OF_ENDER().reload();
        new FALLING_BLOCK().reload();
        new FIREBALL().reload();
        new FIREWORK_ROCKET().reload();
        new FISHING_BOBBER().reload();
        new FOX().reload();
        new FROG().reload();
        new FURNACE_MINECART().reload();
        new GHAST().reload();
        new GIANT().reload();
        new GLOW_ITEM_FRAME().reload();
        new GLOW_SQUID().reload();
        new GOAT().reload();
        new GUARDIAN().reload();
        new HAPPY_GHAST().reload();
        new HOGLIN().reload();
        new HOPPER_MINECART().reload();
        new HORSE().reload();
        new HUSK().reload();
        new ILLUSIONER().reload();
        new INTERACTION().reload();
        new IRON_GOLEM().reload();
        new ITEM().reload();
        new ITEM_DISPLAY().reload();
        new ITEM_FRAME().reload();
        new JUNGLE_BOAT().reload();
        new JUNGLE_CHEST_BOAT().reload();
        new LEASH_KNOT().reload();
        new LIGHTNING_BOLT().reload();
        new LINGERING_POTION().reload();
        new LLAMA().reload();
        new LLAMA_SPIT().reload();
        new MAGMA_CUBE().reload();
        new MANGROVE_BOAT().reload();
        new MANGROVE_CHEST_BOAT().reload();
        new MANNEQUIN().reload();
        new MARKER().reload();
        new MINECART().reload();
        new MOOSHROOM().reload();
        new MULE().reload();
        new NAUTILUS().reload();
        new OAK_BOAT().reload();
        new OAK_CHEST_BOAT().reload();
        new OCELOT().reload();
        new OMINOUS_ITEM_SPAWNER().reload();
        new PAINTING().reload();
        new PALE_OAK_BOAT().reload();
        new PALE_OAK_CHEST_BOAT().reload();
        new PANDA().reload();
        new PARCHED().reload();
        new PARROT().reload();
        new PHANTOM().reload();
        new PIG().reload();
        new PIGLIN().reload();
        new PIGLIN_BRUTE().reload();
        new PILLAGER().reload();
        new POLAR_BEAR().reload();
        new PUFFERFISH().reload();
        new RABBIT().reload();
        new RAVAGER().reload();
        new SALMON().reload();
        new SHEEP().reload();
        new SHULKER().reload();
        new SHULKER_BULLET().reload();
        new SILVERFISH().reload();
        new SKELETON().reload();
        new SKELETON_HORSE().reload();
        new SLIME().reload();
        new SMALL_FIREBALL().reload();
        new SNIFFER().reload();
        new SNOW_GOLEM().reload();
        new SNOWBALL().reload();
        new SPAWNER_MINECART().reload();
        new SPECTRAL_ARROW().reload();
        new SPIDER().reload();
        new SPLASH_POTION().reload();
        new SPRUCE_BOAT().reload();
        new SPRUCE_CHEST_BOAT().reload();
        new SQUID().reload();
        new STRAY().reload();
        new STRIDER().reload();
        new TADPOLE().reload();
        new TEXT_DISPLAY().reload();
        new TNT().reload();
        new TNT_MINECART().reload();
        new TRADER_LLAMA().reload();
        new TRIDENT().reload();
        new TROPICAL_FISH().reload();
        new TURTLE().reload();
        new UNKNOWN().reload();
        new VEX().reload();
        new VILLAGER().reload();
        new VINDICATOR().reload();
        new WANDERING_TRADER().reload();
        new WARDEN().reload();
        new WIND_CHARGE().reload();
        new WITCH().reload();
        new WITHER().reload();
        new WITHER_SKELETON().reload();
        new WITHER_SKULL().reload();
        new WOLF().reload();
        new ZOGLIN().reload();
        new ZOMBIE().reload();
        new ZOMBIE_HORSE().reload();
        new ZOMBIE_NAUTILUS().reload();
        new ZOMBIE_VILLAGER().reload();
        new ZOMBIFIED_PIGLIN().reload();
    }
}