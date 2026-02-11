package org.achymake.essentials;

import org.achymake.essentials.commands.*;
import org.achymake.essentials.data.*;
import org.achymake.essentials.handlers.*;
import org.achymake.essentials.listeners.*;
import org.achymake.essentials.providers.*;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

public final class Essentials extends JavaPlugin {
    private static Essentials instance;
    private Bank bank;
    private Jail jail;
    private Kits kits;
    private Message message;
    private Skulls skulls;
    private Spawn spawn;
    private Userdata userdata;
    private Warps warps;
    private Worth worth;
    private DateHandler dateHandler;
    private EconomyHandler economyHandler;
    private EntityHandler entityHandler;
    private GameModeHandler gameModeHandler;
    private InventoryHandler inventoryHandler;
    private MaterialHandler materialHandler;
    private PaperHandler paperHandler;
    private ProjectileHandler projectileHandler;
    private RandomHandler randomHandler;
    private ScheduleHandler scheduleHandler;
    private ScoreboardHandler scoreboardHandler;
    private TablistHandler tablistHandler;
    private UUIDHandler uuidHandler;
    private VanishHandler vanishHandler;
    private WorldHandler worldHandler;
    private LuckPermsProvider luckPermsProvider;
    private UpdateChecker updateChecker;
    private BukkitScheduler bukkitScheduler;
    private PluginManager pluginManager;
    private ServicesManager servicesManager;
    @Override
    public void onEnable() {
        instance = this;
        bank = new Bank();
        jail = new Jail();
        kits = new Kits();
        message = new Message();
        skulls = new Skulls();
        spawn = new Spawn();
        userdata = new Userdata();
        warps = new Warps();
        worth = new Worth();
        dateHandler = new DateHandler();
        economyHandler = new EconomyHandler();
        entityHandler = new EntityHandler();
        gameModeHandler = new GameModeHandler();
        inventoryHandler = new InventoryHandler();
        materialHandler = new MaterialHandler();
        paperHandler = new PaperHandler();
        projectileHandler = new ProjectileHandler();
        randomHandler = new RandomHandler();
        scheduleHandler = new ScheduleHandler();
        scoreboardHandler = new ScoreboardHandler();
        tablistHandler = new TablistHandler();
        uuidHandler = new UUIDHandler();
        vanishHandler = new VanishHandler();
        worldHandler = new WorldHandler();
        luckPermsProvider = new LuckPermsProvider();
        updateChecker = new UpdateChecker();
        bukkitScheduler = getServer().getScheduler();
        pluginManager = getServer().getPluginManager();
        servicesManager = getServer().getServicesManager();
        commands();
        events();
        reload();
        new VaultEconomyProvider(this).register();
        new PlaceholderProvider().register();
        sendInfo("Enabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
        getUpdateChecker().getUpdate();
    }
    @Override
    public void onDisable() {
        getTablistHandler().disable();
        getScoreboardHandler().disable();
        getVanishHandler().disable();
        getUserdata().disable();
        getProjectileHandler().disable();
        new PlaceholderProvider().unregister();
        getScheduleHandler().disable();
        sendInfo("Disabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
    }
    private void commands() {
        new AnnouncementCommand();
        new AnvilCommand();
        new BackCommand();
        new BalanceCommand();
        new BanCommand();
        new BankCommand();
        new BoardCommand();
        new CartographyCommand();
        new ColorCommand();
        new DelHomeCommand();
        new DelWarpCommand();
        new EcoCommand();
        new EnchantCommand();
        new EnchantingCommand();
        new EnderChestCommand();
        new EntityCommand();
        new EssentialsCommand();
        new FeedCommand();
        new FlyCommand();
        new FlySpeedCommand();
        new FreezeCommand();
        new GameModeCommand();
        new GMACommand();
        new GMCCommand();
        new GMSCommand();
        new GMSPCommand();
        new GrindstoneCommand();
        new HatCommand();
        new HealCommand();
        new HelpCommand();
        new HomeCommand();
        new HomesCommand();
        new InformationCommand();
        new InventoryCommand();
        new InvulnerableCommand();
        new JailCommand();
        new KitCommand();
        new LightningCommand();
        new LoomCommand();
        new MOTDCommand();
        new MuteCommand();
        new NicknameCommand();
        new PayCommand();
        new PVPCommand();
        new RepairCommand();
        new RespondCommand();
        new RTPCommand();
        new RulesCommand();
        new SellCommand();
        new SetHomeCommand();
        new SetJailCommand();
        new SetSpawnCommand();
        new SetWarpCommand();
        new SetWorthCommand();
        new SkullCommand();
        new SmithingCommand();
        new SpawnCommand();
        new StonecutterCommand();
        new StoreCommand();
        new TimeCommand();
        new TPAcceptCommand();
        new TPACommand();
        new TPAHereCommand();
        new TPCancelCommand();
        new TPCommand();
        new TPDenyCommand();
        new TPHereCommand();
        new UnBanCommand();
        new VanishCommand();
        new WalkSpeedCommand();
        new WarpCommand();
        new WeatherCommand();
        new WhisperCommand();
        new WorkbenchCommand();
        new WorthCommand();
    }
    private void events() {
        new AsyncPlayerChat();
        new BellRing();
        new BlockBreak();
        new BlockDamage();
        new BlockDispenseLoot();
        new BlockFertilize();
        new BlockIgnite();
        new BlockPistonExtend();
        new BlockPistonRetract();
        new BlockPlace();
        new BlockReceiveGame();
        new BlockRedstone();
        new BlockSpread();
        new CreatureSpawn();
        new EntityBlockForm();
        new EntityChangeBlock();
        new EntityDamage();
        new EntityDamageByBlock();
        new EntityDamageByEntity();
        new EntityDeath();
        new EntityEnterLoveMode();
        new EntityExplode();
        new EntityInteract();
        new EntityMount();
        new EntityPlace();
        new EntityShootBow();
        new EntityTarget();
        new EntityTargetLivingEntity();
        new EntityTransform();
        new HangingBreakByEntity();
        new HangingPlace();
        new NotePlay();
        new PlayerBucketEmpty();
        new PlayerBucketEntity();
        new PlayerBucketFill();
        new PlayerChangedWorld();
        new PlayerCommandPreprocess();
        new PlayerDeath();
        new PlayerGameModeChange();
        new PlayerHarvestBlock();
        new PlayerInteract();
        new PlayerInteractEntity();
        new PlayerJoin();
        new PlayerLeashEntity();
        new PlayerLevelChange();
        new PlayerLogin();
        new PlayerMove();
        new PlayerQuit();
        new PlayerRespawn();
        new PlayerShearEntity();
        if (!isBukkit()) {
            new AsyncPlayerSpawnLocation();
            new PlayerShearBlock();
        } else new PlayerSpawnLocation();
        new PlayerTakeLecternBook();
        new PlayerTeleport();
        new PlayerToggleFlight();
        new PlayerToggleSneak();
        new PrepareAnvil();
        new ProjectileHit();
        new ProjectileLaunch();
        new SignChange();
        new VehicleCreate();
    }
    public void reload() {
        getTablistHandler().disable();
        getScoreboardHandler().disable();
        if (!new File(getDataFolder(), "config.yml").exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else reloadConfig();
        if (getConfig().getBoolean("server.motd.enable")) {
            var line1 = getMessage().addColor(getConfig().getString("server.motd.line-1"));
            var line2 = getMessage().addColor(getConfig().getString("server.motd.line-2"));
            getServer().setMotd(line1 + "\n" + line2);
        }
        getBank().reload();
        getJail().reload();
        getKits().reload();
        getMessage().reload();
        getSkulls().reload();
        getSpawn().reload();
        getWarps().reload();
        getWorth().reload();
        getScoreboardHandler().reload();
        getTablistHandler().reload();
        getEntityHandler().reload();
        getTablistHandler().enable();
        getScoreboardHandler().enable();
    }
    public void reloadUserdata() {
        getUserdata().reload();
    }
    public Collection<? extends Player> getOnlinePlayers() {
        return getServer().getOnlinePlayers();
    }
    public Collection<? extends OfflinePlayer> getOfflinePlayers() {
        return getUserdata().getOfflinePlayers();
    }
    public ServicesManager getServicesManager() {
        return servicesManager;
    }
    public PluginManager getPluginManager() {
        return pluginManager;
    }
    public BukkitScheduler getBukkitScheduler() {
        return bukkitScheduler;
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public LuckPermsProvider getLuckPermsProvider() {
        return luckPermsProvider;
    }
    public WorldHandler getWorldHandler() {
        return worldHandler;
    }
    public VanishHandler getVanishHandler() {
        return vanishHandler;
    }
    public UUIDHandler getUUIDHandler() {
        return uuidHandler;
    }
    public TablistHandler getTablistHandler() {
        return tablistHandler;
    }
    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }
    public ScheduleHandler getScheduleHandler() {
        return scheduleHandler;
    }
    public RandomHandler getRandomHandler() {
        return randomHandler;
    }
    public ProjectileHandler getProjectileHandler() {
        return projectileHandler;
    }
    public PaperHandler getPaperHandler() {
        return paperHandler;
    }
    public MaterialHandler getMaterialHandler() {
        return materialHandler;
    }
    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }
    public GameModeHandler getGameModeHandler() {
        return gameModeHandler;
    }
    public EntityHandler getEntityHandler() {
        return entityHandler;
    }
    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }
    public DateHandler getDateHandler() {
        return dateHandler;
    }
    public Worth getWorth() {
        return worth;
    }
    public Warps getWarps() {
        return warps;
    }
    public Userdata getUserdata() {
        return userdata;
    }
    public Spawn getSpawn() {
        return spawn;
    }
    public Skulls getSkulls() {
        return skulls;
    }
    public Message getMessage() {
        return message;
    }
    public Kits getKits() {
        return kits;
    }
    public Jail getJail() {
        return jail;
    }
    public Bank getBank() {
        return bank;
    }
    public static Essentials getInstance() {
        return instance;
    }
    public NamespacedKey getKey(String key) {
        return new NamespacedKey(this, key);
    }
    public void sendInfo(String message) {
        getLogger().info(message);
    }
    public void sendWarning(String message) {
        getLogger().warning(message);
    }
    public String name() {
        return getDescription().getName();
    }
    public String version() {
        return getDescription().getVersion();
    }
    public String getMinecraftVersion() {
        return getServer().getBukkitVersion();
    }
    public String getMinecraftProvider() {
        return getServer().getName();
    }
    public boolean isBukkit() {
        return getMinecraftProvider().equals("Bukkit") || getMinecraftProvider().equals("CraftBukkit");
    }
    public Player getPlayer(String username) {
        return getServer().getPlayerExact(username);
    }
    public Player getPlayer(UUID uuid) {
        return getServer().getPlayer(uuid);
    }
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return getServer().getOfflinePlayer(uuid);
    }
    public OfflinePlayer getOfflinePlayer(String username) {
        return getServer().getOfflinePlayer(username);
    }
}