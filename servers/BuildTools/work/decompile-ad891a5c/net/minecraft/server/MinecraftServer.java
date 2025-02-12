package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.longs.LongIterator;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MinecraftServer extends IAsyncTaskHandlerReentrant<TickTask> implements IMojangStatistics, ICommandListener, AutoCloseable, Runnable {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final File b = new File("usercache.json");
    public static final WorldSettings c = (new WorldSettings((long) "North Carolina".hashCode(), EnumGamemode.SURVIVAL, true, false, WorldType.NORMAL)).a();
    public Convertable convertable;
    private final MojangStatisticsGenerator snooper = new MojangStatisticsGenerator("server", this, SystemUtils.getMonotonicMillis());
    public File universe;
    private final List<Runnable> tickables = Lists.newArrayList();
    private final GameProfiler methodProfiler = new GameProfiler(this::ai);
    private ServerConnection serverConnection;
    public final WorldLoadListenerFactory worldLoadListenerFactory;
    private final ServerPing serverPing = new ServerPing();
    private final Random q = new Random();
    public final DataFixer dataConverterManager;
    private String serverIp;
    private int serverPort = -1;
    public final Map<DimensionManager, WorldServer> worldServer = Maps.newIdentityHashMap();
    private PlayerList playerList;
    private volatile boolean isRunning = true;
    private boolean isStopped;
    private int ticks;
    protected final Proxy proxy;
    private boolean onlineMode;
    private boolean A;
    private boolean spawnAnimals;
    private boolean spawnNPCs;
    private boolean pvpMode;
    private boolean allowFlight;
    @Nullable
    private String motd;
    private int G;
    private int H;
    public final long[] f = new long[100];
    protected final Map<DimensionManager, long[]> g = Maps.newIdentityHashMap();
    @Nullable
    private KeyPair I;
    @Nullable
    private String J;
    private final String K;
    private boolean demoMode;
    private boolean bonusChest;
    private String O = "";
    private String P = "";
    private volatile boolean hasTicked;
    private long lastOverloadTime;
    @Nullable
    private IChatBaseComponent S;
    private boolean T;
    private boolean U;
    @Nullable
    private final YggdrasilAuthenticationService yggdrasilAuthenticationService;
    private final MinecraftSessionService minecraftSessionService;
    private final GameProfileRepository gameProfileRepository;
    private final UserCache userCache;
    private long Z;
    public final Thread serverThread = (Thread) SystemUtils.a((Object) (new Thread(this, "Server thread")), (thread) -> {
        thread.setUncaughtExceptionHandler((thread1, throwable) -> {
            MinecraftServer.LOGGER.error(throwable);
        });
    });
    private long nextTick = SystemUtils.getMonotonicMillis();
    private long ab;
    private boolean ac;
    private final IReloadableResourceManager ae;
    private final ResourcePackRepository<ResourcePackLoader> resourcePackRepository;
    @Nullable
    private ResourcePackSourceFolder resourcePackFolder;
    public CommandDispatcher commandDispatcher;
    private final CraftingManager ai;
    private final TagRegistry aj;
    private final ScoreboardServer ak;
    private final BossBattleCustomData al;
    private final LootTableRegistry am;
    private final AdvancementDataWorld an;
    private final CustomFunctionData ao;
    private final CircularTimer ap;
    private boolean aq;
    private boolean forceUpgrade;
    private boolean as;
    private float at;
    public final Executor executorService;
    @Nullable
    private String av;

    public MinecraftServer(File file, Proxy proxy, DataFixer datafixer, CommandDispatcher commanddispatcher, YggdrasilAuthenticationService yggdrasilauthenticationservice, MinecraftSessionService minecraftsessionservice, GameProfileRepository gameprofilerepository, UserCache usercache, WorldLoadListenerFactory worldloadlistenerfactory, String s) {
        super("Server");
        this.ae = new ResourceManager(EnumResourcePackType.SERVER_DATA, this.serverThread);
        this.resourcePackRepository = new ResourcePackRepository<>(ResourcePackLoader::new);
        this.ai = new CraftingManager();
        this.aj = new TagRegistry();
        this.ak = new ScoreboardServer(this);
        this.al = new BossBattleCustomData(this);
        this.am = new LootTableRegistry();
        this.an = new AdvancementDataWorld();
        this.ao = new CustomFunctionData(this);
        this.ap = new CircularTimer();
        this.proxy = proxy;
        this.commandDispatcher = commanddispatcher;
        this.yggdrasilAuthenticationService = yggdrasilauthenticationservice;
        this.minecraftSessionService = minecraftsessionservice;
        this.gameProfileRepository = gameprofilerepository;
        this.userCache = usercache;
        this.universe = file;
        this.serverConnection = new ServerConnection(this);
        this.worldLoadListenerFactory = worldloadlistenerfactory;
        this.convertable = new Convertable(file.toPath(), file.toPath().resolve("../backups"), datafixer);
        this.dataConverterManager = datafixer;
        this.ae.a((IReloadListener) this.aj);
        this.ae.a((IReloadListener) this.ai);
        this.ae.a((IReloadListener) this.am);
        this.ae.a((IReloadListener) this.ao);
        this.ae.a((IReloadListener) this.an);
        this.executorService = SystemUtils.e();
        this.K = s;
    }

    private void initializeScoreboards(WorldPersistentData worldpersistentdata) {
        PersistentScoreboard persistentscoreboard = (PersistentScoreboard) worldpersistentdata.a(PersistentScoreboard::new, "scoreboard");

        persistentscoreboard.a((Scoreboard) this.getScoreboard());
        this.getScoreboard().a((Runnable) (new RunnableSaveScoreboard(persistentscoreboard)));
    }

    protected abstract boolean init() throws IOException;

    public void convertWorld(String s) {
        if (this.getConvertable().isConvertable(s)) {
            MinecraftServer.LOGGER.info("Converting map!");
            this.b((IChatBaseComponent) (new ChatMessage("menu.convertingLevel", new Object[0])));
            this.getConvertable().convert(s, new IProgressUpdate() {
                private long b = SystemUtils.getMonotonicMillis();

                @Override
                public void a(IChatBaseComponent ichatbasecomponent) {}

                @Override
                public void a(int i) {
                    if (SystemUtils.getMonotonicMillis() - this.b >= 1000L) {
                        this.b = SystemUtils.getMonotonicMillis();
                        MinecraftServer.LOGGER.info("Converting... {}%", i);
                    }

                }

                @Override
                public void c(IChatBaseComponent ichatbasecomponent) {}
            });
        }

        if (this.forceUpgrade) {
            MinecraftServer.LOGGER.info("Forcing world upgrade!");
            WorldData worlddata = this.getConvertable().b(this.getWorld());

            if (worlddata != null) {
                WorldUpgrader worldupgrader = new WorldUpgrader(this.getWorld(), this.getConvertable(), worlddata, this.as);
                IChatBaseComponent ichatbasecomponent = null;

                while (!worldupgrader.b()) {
                    IChatBaseComponent ichatbasecomponent1 = worldupgrader.g();

                    if (ichatbasecomponent != ichatbasecomponent1) {
                        ichatbasecomponent = ichatbasecomponent1;
                        MinecraftServer.LOGGER.info(worldupgrader.g().getString());
                    }

                    int i = worldupgrader.d();

                    if (i > 0) {
                        int j = worldupgrader.e() + worldupgrader.f();

                        MinecraftServer.LOGGER.info("{}% completed ({} / {} chunks)...", MathHelper.d((float) j / (float) i * 100.0F), j, i);
                    }

                    if (this.isStopped()) {
                        worldupgrader.a();
                    } else {
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException interruptedexception) {
                            ;
                        }
                    }
                }
            }
        }

    }

    protected synchronized void b(IChatBaseComponent ichatbasecomponent) {
        this.S = ichatbasecomponent;
    }

    protected void a(String s, String s1, long i, WorldType worldtype, JsonElement jsonelement) {
        this.convertWorld(s);
        this.b((IChatBaseComponent) (new ChatMessage("menu.loadingLevel", new Object[0])));
        WorldNBTStorage worldnbtstorage = this.getConvertable().a(s, this);

        this.a(this.getWorld(), worldnbtstorage);
        WorldData worlddata = worldnbtstorage.getWorldData();
        WorldSettings worldsettings;

        if (worlddata == null) {
            if (this.isDemoMode()) {
                worldsettings = MinecraftServer.c;
            } else {
                worldsettings = new WorldSettings(i, this.getGamemode(), this.getGenerateStructures(), this.isHardcore(), worldtype);
                worldsettings.setGeneratorSettings(jsonelement);
                if (this.bonusChest) {
                    worldsettings.a();
                }
            }

            worlddata = new WorldData(worldsettings, s1);
        } else {
            worlddata.a(s1);
            worldsettings = new WorldSettings(worlddata);
        }

        this.a(worldnbtstorage.getDirectory(), worlddata);
        WorldLoadListener worldloadlistener = this.worldLoadListenerFactory.create(11);

        this.a(worldnbtstorage, worlddata, worldsettings, worldloadlistener);
        this.a(this.getDifficulty(), true);
        this.loadSpawn(worldloadlistener);
    }

    protected void a(WorldNBTStorage worldnbtstorage, WorldData worlddata, WorldSettings worldsettings, WorldLoadListener worldloadlistener) {
        if (this.isDemoMode()) {
            worlddata.a(MinecraftServer.c);
        }

        WorldServer worldserver = new WorldServer(this, this.executorService, worldnbtstorage, worlddata, DimensionManager.OVERWORLD, this.methodProfiler, worldloadlistener);

        this.worldServer.put(DimensionManager.OVERWORLD, worldserver);
        this.initializeScoreboards(worldserver.getWorldPersistentData());
        worldserver.getWorldBorder().b(worlddata);
        WorldServer worldserver1 = this.getWorldServer(DimensionManager.OVERWORLD);

        if (!worlddata.v()) {
            try {
                worldserver1.a(worldsettings);
                if (worlddata.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
                    this.a(worlddata);
                }

                worlddata.d(true);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Exception initializing level");

                try {
                    worldserver1.a(crashreport);
                } catch (Throwable throwable1) {
                    ;
                }

                throw new ReportedException(crashreport);
            }

            worlddata.d(true);
        }

        this.getPlayerList().setPlayerFileData(worldserver1);
        if (worlddata.getCustomBossEvents() != null) {
            this.getBossBattleCustomData().a(worlddata.getCustomBossEvents());
        }

        Iterator iterator = DimensionManager.a().iterator();

        while (iterator.hasNext()) {
            DimensionManager dimensionmanager = (DimensionManager) iterator.next();

            if (dimensionmanager != DimensionManager.OVERWORLD) {
                this.worldServer.put(dimensionmanager, new SecondaryWorldServer(worldserver1, this, this.executorService, worldnbtstorage, dimensionmanager, this.methodProfiler, worldloadlistener));
            }
        }

    }

    private void a(WorldData worlddata) {
        worlddata.f(false);
        worlddata.c(true);
        worlddata.setStorm(false);
        worlddata.setThundering(false);
        worlddata.g(1000000000);
        worlddata.setDayTime(6000L);
        worlddata.setGameType(EnumGamemode.SPECTATOR);
        worlddata.g(false);
        worlddata.setDifficulty(EnumDifficulty.PEACEFUL);
        worlddata.e(true);
        worlddata.w().set("doDaylightCycle", "false", this);
    }

    protected void a(File file, WorldData worlddata) {
        this.resourcePackRepository.a((ResourcePackSource) (new ResourcePackSourceVanilla()));
        this.resourcePackFolder = new ResourcePackSourceFolder(new File(file, "datapacks"));
        this.resourcePackRepository.a((ResourcePackSource) this.resourcePackFolder);
        this.resourcePackRepository.a();
        List<ResourcePackLoader> list = Lists.newArrayList();
        Iterator iterator = worlddata.P().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            ResourcePackLoader resourcepackloader = this.resourcePackRepository.a(s);

            if (resourcepackloader != null) {
                list.add(resourcepackloader);
            } else {
                MinecraftServer.LOGGER.warn("Missing data pack {}", s);
            }
        }

        this.resourcePackRepository.a((Collection) list);
        this.b(worlddata);
    }

    public void loadSpawn(WorldLoadListener worldloadlistener) {
        this.b((IChatBaseComponent) (new ChatMessage("menu.generatingTerrain", new Object[0])));
        WorldServer worldserver = this.getWorldServer(DimensionManager.OVERWORLD);

        MinecraftServer.LOGGER.info("Preparing start region for dimension " + DimensionManager.a(worldserver.worldProvider.getDimensionManager()));
        BlockPosition blockposition = worldserver.getSpawn();

        worldloadlistener.a(new ChunkCoordIntPair(blockposition));
        ChunkProviderServer chunkproviderserver = worldserver.getChunkProvider();

        chunkproviderserver.getLightEngine().a(500);
        this.nextTick = SystemUtils.getMonotonicMillis();
        chunkproviderserver.addTicket(TicketType.START, new ChunkCoordIntPair(blockposition), 11, Unit.INSTANCE);

        while (chunkproviderserver.b() != 441) {
            this.nextTick += 100L;
            this.sleepForTick();
        }

        this.nextTick += 100L;
        this.sleepForTick();
        Iterator iterator = DimensionManager.a().iterator();

        while (iterator.hasNext()) {
            DimensionManager dimensionmanager = (DimensionManager) iterator.next();
            ForcedChunk forcedchunk = (ForcedChunk) this.getWorldServer(dimensionmanager).getWorldPersistentData().b(ForcedChunk::new, "chunks");

            if (forcedchunk != null) {
                WorldServer worldserver1 = this.getWorldServer(dimensionmanager);
                LongIterator longiterator = forcedchunk.a().iterator();

                while (longiterator.hasNext()) {
                    long i = longiterator.nextLong();
                    ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(i);

                    worldserver1.getChunkProvider().a(chunkcoordintpair, true);
                }
            }
        }

        this.nextTick += 100L;
        this.sleepForTick();
        worldloadlistener.b();
        chunkproviderserver.getLightEngine().a(5);
    }

    protected void a(String s, WorldNBTStorage worldnbtstorage) {
        File file = new File(worldnbtstorage.getDirectory(), "resources.zip");

        if (file.isFile()) {
            try {
                this.setResourcePack("level://" + URLEncoder.encode(s, StandardCharsets.UTF_8.toString()) + "/" + "resources.zip", "");
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                MinecraftServer.LOGGER.warn("Something went wrong url encoding {}", s);
            }
        }

    }

    public abstract boolean getGenerateStructures();

    public abstract EnumGamemode getGamemode();

    public abstract EnumDifficulty getDifficulty();

    public abstract boolean isHardcore();

    public abstract int j();

    public abstract boolean k();

    public boolean saveChunks(boolean flag, boolean flag1, boolean flag2) {
        boolean flag3 = false;

        for (Iterator iterator = this.getWorlds().iterator(); iterator.hasNext(); flag3 = true) {
            WorldServer worldserver = (WorldServer) iterator.next();

            if (!flag) {
                MinecraftServer.LOGGER.info("Saving chunks for level '{}'/{}", worldserver.getWorldData().getName(), DimensionManager.a(worldserver.worldProvider.getDimensionManager()));
            }

            try {
                worldserver.save((IProgressUpdate) null, flag1, worldserver.savingDisabled && !flag2);
            } catch (ExceptionWorldConflict exceptionworldconflict) {
                MinecraftServer.LOGGER.warn(exceptionworldconflict.getMessage());
            }
        }

        WorldServer worldserver1 = this.getWorldServer(DimensionManager.OVERWORLD);
        WorldData worlddata = worldserver1.getWorldData();

        worldserver1.getWorldBorder().a(worlddata);
        worlddata.c(this.getBossBattleCustomData().c());
        worldserver1.getDataManager().saveWorldData(worlddata, this.getPlayerList().r());
        return flag3;
    }

    @Override
    public void close() {
        this.stop();
    }

    protected void stop() {
        MinecraftServer.LOGGER.info("Stopping server");
        if (this.getServerConnection() != null) {
            this.getServerConnection().b();
        }

        if (this.playerList != null) {
            MinecraftServer.LOGGER.info("Saving players");
            this.playerList.savePlayers();
            this.playerList.shutdown();
        }

        MinecraftServer.LOGGER.info("Saving worlds");
        Iterator iterator = this.getWorlds().iterator();

        WorldServer worldserver;

        while (iterator.hasNext()) {
            worldserver = (WorldServer) iterator.next();
            if (worldserver != null) {
                worldserver.savingDisabled = false;
            }
        }

        this.saveChunks(false, true, false);
        iterator = this.getWorlds().iterator();

        while (iterator.hasNext()) {
            worldserver = (WorldServer) iterator.next();
            if (worldserver != null) {
                try {
                    worldserver.close();
                } catch (IOException ioexception) {
                    MinecraftServer.LOGGER.error("Exception closing the level", ioexception);
                }
            }
        }

        if (this.snooper.d()) {
            this.snooper.e();
        }

    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void b(String s) {
        this.serverIp = s;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void safeShutdown(boolean flag) {
        this.isRunning = false;
        if (flag) {
            try {
                this.serverThread.join();
            } catch (InterruptedException interruptedexception) {
                MinecraftServer.LOGGER.error("Error while shutting down", interruptedexception);
            }
        }

    }

    public void run() {
        try {
            if (this.init()) {
                this.nextTick = SystemUtils.getMonotonicMillis();
                this.serverPing.setMOTD(new ChatComponentText(this.motd));
                this.serverPing.setServerInfo(new ServerPing.ServerData(SharedConstants.a().getName(), SharedConstants.a().getProtocolVersion()));
                this.a(this.serverPing);

                while (this.isRunning) {
                    long i = SystemUtils.getMonotonicMillis() - this.nextTick;

                    if (i > 2000L && this.nextTick - this.lastOverloadTime >= 15000L) {
                        long j = i / 50L;

                        MinecraftServer.LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", i, j);
                        this.nextTick += j * 50L;
                        this.lastOverloadTime = this.nextTick;
                    }

                    this.nextTick += 50L;
                    if (this.T) {
                        this.T = false;
                        this.methodProfiler.d().d();
                    }

                    this.methodProfiler.a();
                    this.methodProfiler.enter("tick");
                    this.a(this::canSleepForTick);
                    this.methodProfiler.exitEnter("nextTickWait");
                    this.ac = true;
                    this.ab = Math.max(SystemUtils.getMonotonicMillis() + 50L, this.nextTick);
                    this.sleepForTick();
                    this.methodProfiler.exit();
                    this.methodProfiler.b();
                    this.hasTicked = true;
                }
            } else {
                this.a((CrashReport) null);
            }
        } catch (Throwable throwable) {
            MinecraftServer.LOGGER.error("Encountered an unexpected exception", throwable);
            CrashReport crashreport;

            if (throwable instanceof ReportedException) {
                crashreport = this.b(((ReportedException) throwable).a());
            } else {
                crashreport = this.b(new CrashReport("Exception in server tick loop", throwable));
            }

            File file = new File(new File(this.s(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.a(file)) {
                MinecraftServer.LOGGER.error("This crash report has been saved to: {}", file.getAbsolutePath());
            } else {
                MinecraftServer.LOGGER.error("We were unable to save this crash report to disk.");
            }

            this.a(crashreport);
        } finally {
            try {
                this.isStopped = true;
                this.stop();
            } catch (Throwable throwable1) {
                MinecraftServer.LOGGER.error("Exception stopping the server", throwable1);
            } finally {
                this.exit();
            }

        }

    }

    private boolean canSleepForTick() {
        return this.isEntered() || SystemUtils.getMonotonicMillis() < (this.ac ? this.ab : this.nextTick);
    }

    protected void sleepForTick() {
        this.executeAll();
        this.awaitTasks(() -> {
            return !this.canSleepForTick();
        });
    }

    @Override
    protected TickTask postToMainThread(Runnable runnable) {
        return new TickTask(this.ticks, runnable);
    }

    protected boolean canExecute(TickTask ticktask) {
        return ticktask.a() + 3 < this.ticks || this.canSleepForTick();
    }

    @Override
    public boolean executeNext() {
        boolean flag = this.aW();

        this.ac = flag;
        return flag;
    }

    private boolean aW() {
        if (super.executeNext()) {
            return true;
        } else {
            if (this.canSleepForTick()) {
                Iterator iterator = this.getWorlds().iterator();

                while (iterator.hasNext()) {
                    WorldServer worldserver = (WorldServer) iterator.next();

                    if (worldserver.getChunkProvider().runTasks()) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public void a(ServerPing serverping) {
        File file = this.d("server-icon.png");

        if (!file.exists()) {
            file = this.getConvertable().b(this.getWorld(), "icon.png");
        }

        if (file.isFile()) {
            ByteBuf bytebuf = Unpooled.buffer();

            try {
                BufferedImage bufferedimage = ImageIO.read(file);

                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                ByteBuffer bytebuffer = Base64.getEncoder().encode(bytebuf.nioBuffer());

                serverping.setFavicon("data:image/png;base64," + StandardCharsets.UTF_8.decode(bytebuffer));
            } catch (Exception exception) {
                MinecraftServer.LOGGER.error("Couldn't load server icon", exception);
            } finally {
                bytebuf.release();
            }
        }

    }

    public File s() {
        return new File(".");
    }

    protected void a(CrashReport crashreport) {}

    protected void exit() {}

    protected void a(BooleanSupplier booleansupplier) {
        long i = SystemUtils.getMonotonicNanos();

        ++this.ticks;
        this.b(booleansupplier);
        if (i - this.Z >= 5000000000L) {
            this.Z = i;
            this.serverPing.setPlayerSample(new ServerPing.ServerPingPlayerSample(this.getMaxPlayers(), this.getPlayerCount()));
            GameProfile[] agameprofile = new GameProfile[Math.min(this.getPlayerCount(), 12)];
            int j = MathHelper.nextInt(this.q, 0, this.getPlayerCount() - agameprofile.length);

            for (int k = 0; k < agameprofile.length; ++k) {
                agameprofile[k] = ((EntityPlayer) this.playerList.getPlayers().get(j + k)).getProfile();
            }

            Collections.shuffle(Arrays.asList(agameprofile));
            this.serverPing.b().a(agameprofile);
        }

        if (this.ticks % 6000 == 0) {
            MinecraftServer.LOGGER.debug("Autosave started");
            this.methodProfiler.enter("save");
            this.playerList.savePlayers();
            this.saveChunks(true, false, false);
            this.methodProfiler.exit();
            MinecraftServer.LOGGER.debug("Autosave finished");
        }

        this.methodProfiler.enter("snooper");
        if (!this.snooper.d() && this.ticks > 100) {
            this.snooper.a();
        }

        if (this.ticks % 6000 == 0) {
            this.snooper.b();
        }

        this.methodProfiler.exit();
        this.methodProfiler.enter("tallying");
        long l = this.f[this.ticks % 100] = SystemUtils.getMonotonicNanos() - i;

        this.at = this.at * 0.8F + (float) l / 1000000.0F * 0.19999999F;
        long i1 = SystemUtils.getMonotonicNanos();

        this.ap.a(i1 - i);
        this.methodProfiler.exit();
    }

    protected void b(BooleanSupplier booleansupplier) {
        this.methodProfiler.enter("commandFunctions");
        this.getFunctionData().tick();
        this.methodProfiler.exitEnter("levels");

        WorldServer worldserver;
        long i;

        for (Iterator iterator = this.getWorlds().iterator(); iterator.hasNext();((long[]) this.g.computeIfAbsent(worldserver.worldProvider.getDimensionManager(), (dimensionmanager) -> {
            return new long[100];
        }))[this.ticks % 100] = SystemUtils.getMonotonicNanos() - i) {
            worldserver = (WorldServer) iterator.next();
            i = SystemUtils.getMonotonicNanos();
            if (worldserver.worldProvider.getDimensionManager() == DimensionManager.OVERWORLD || this.getAllowNether()) {
                this.methodProfiler.a(() -> {
                    return worldserver.getWorldData().getName() + " " + IRegistry.DIMENSION_TYPE.getKey(worldserver.worldProvider.getDimensionManager());
                });
                if (this.ticks % 20 == 0) {
                    this.methodProfiler.enter("timeSync");
                    this.playerList.a((Packet) (new PacketPlayOutUpdateTime(worldserver.getTime(), worldserver.getDayTime(), worldserver.getGameRules().getBoolean("doDaylightCycle"))), worldserver.worldProvider.getDimensionManager());
                    this.methodProfiler.exit();
                }

                this.methodProfiler.enter("tick");

                try {
                    worldserver.doTick(booleansupplier);
                } catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.a(throwable, "Exception ticking world");

                    worldserver.a(crashreport);
                    throw new ReportedException(crashreport);
                }

                this.methodProfiler.exit();
                this.methodProfiler.exit();
            }
        }

        this.methodProfiler.exitEnter("connection");
        this.getServerConnection().c();
        this.methodProfiler.exitEnter("players");
        this.playerList.tick();
        this.methodProfiler.exitEnter("server gui refresh");

        for (int j = 0; j < this.tickables.size(); ++j) {
            ((Runnable) this.tickables.get(j)).run();
        }

        this.methodProfiler.exit();
    }

    public boolean getAllowNether() {
        return true;
    }

    public void b(Runnable runnable) {
        this.tickables.add(runnable);
    }

    public static void main(String[] astring) {
        OptionParser optionparser = new OptionParser();
        OptionSpec<Void> optionspec = optionparser.accepts("nogui");
        OptionSpec<Void> optionspec1 = optionparser.accepts("initSettings", "Initializes 'server.properties' and 'eula.txt', then quits");
        OptionSpec<Void> optionspec2 = optionparser.accepts("demo");
        OptionSpec<Void> optionspec3 = optionparser.accepts("bonusChest");
        OptionSpec<Void> optionspec4 = optionparser.accepts("forceUpgrade");
        OptionSpec<Void> optionspec5 = optionparser.accepts("eraseCache");
        OptionSpec<Void> optionspec6 = optionparser.accepts("help").forHelp();
        OptionSpec<String> optionspec7 = optionparser.accepts("singleplayer").withRequiredArg();
        OptionSpec<String> optionspec8 = optionparser.accepts("universe").withRequiredArg().defaultsTo(".", new String[0]);
        OptionSpec<String> optionspec9 = optionparser.accepts("world").withRequiredArg();
        OptionSpec<Integer> optionspec10 = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(-1, new Integer[0]);
        OptionSpec<String> optionspec11 = optionparser.accepts("serverId").withRequiredArg();
        NonOptionArgumentSpec nonoptionargumentspec = optionparser.nonOptions();

        try {
            OptionSet optionset = optionparser.parse(astring);

            if (optionset.has(optionspec6)) {
                optionparser.printHelpOn(System.err);
                return;
            }

            java.nio.file.Path java_nio_file_path = Paths.get("server.properties");
            DedicatedServerSettings dedicatedserversettings = new DedicatedServerSettings(java_nio_file_path);

            dedicatedserversettings.save();
            java.nio.file.Path java_nio_file_path1 = Paths.get("eula.txt");
            EULA eula = new EULA(java_nio_file_path1);

            if (optionset.has(optionspec1)) {
                MinecraftServer.LOGGER.info("Initialized '" + java_nio_file_path.toAbsolutePath().toString() + "' and '" + java_nio_file_path1.toAbsolutePath().toString() + "'");
                return;
            }

            if (!eula.a()) {
                MinecraftServer.LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
                return;
            }

            DispenserRegistry.init();
            DispenserRegistry.c();
            String s = (String) optionset.valueOf(optionspec8);
            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            UserCache usercache = new UserCache(gameprofilerepository, new File(s, MinecraftServer.b.getName()));
            String s1 = (String) Optional.ofNullable(optionset.valueOf(optionspec9)).orElse(dedicatedserversettings.getProperties().levelName);
            final DedicatedServer dedicatedserver = new DedicatedServer(new File(s), dedicatedserversettings, DataConverterRegistry.a(), yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, usercache, WorldLoadListenerLogger::new, s1);

            dedicatedserver.i((String) optionset.valueOf(optionspec7));
            dedicatedserver.setPort((Integer) optionset.valueOf(optionspec10));
            dedicatedserver.e(optionset.has(optionspec2));
            dedicatedserver.f(optionset.has(optionspec3));
            dedicatedserver.setForceUpgrade(optionset.has(optionspec4));
            dedicatedserver.c(optionset.has(optionspec5));
            dedicatedserver.c((String) optionset.valueOf(optionspec11));
            boolean flag = !optionset.has(optionspec) && !optionset.valuesOf(nonoptionargumentspec).contains("nogui");

            if (flag && !GraphicsEnvironment.isHeadless()) {
                dedicatedserver.aY();
            }

            dedicatedserver.startServerThread();
            Thread thread = new Thread("Server Shutdown Thread") {
                public void run() {
                    dedicatedserver.safeShutdown(true);
                }
            };

            thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(MinecraftServer.LOGGER));
            Runtime.getRuntime().addShutdownHook(thread);
        } catch (Exception exception) {
            MinecraftServer.LOGGER.fatal("Failed to start the minecraft server", exception);
        }

    }

    protected void c(String s) {
        this.av = s;
    }

    protected void setForceUpgrade(boolean flag) {
        this.forceUpgrade = flag;
    }

    protected void c(boolean flag) {
        this.as = flag;
    }

    public void startServerThread() {
        this.serverThread.start();
    }

    public File d(String s) {
        return new File(this.s(), s);
    }

    public void info(String s) {
        MinecraftServer.LOGGER.info(s);
    }

    public void warning(String s) {
        MinecraftServer.LOGGER.warn(s);
    }

    public WorldServer getWorldServer(DimensionManager dimensionmanager) {
        return (WorldServer) this.worldServer.get(dimensionmanager);
    }

    public Iterable<WorldServer> getWorlds() {
        return this.worldServer.values();
    }

    public String getVersion() {
        return SharedConstants.a().getName();
    }

    public int getPlayerCount() {
        return this.playerList.getPlayerCount();
    }

    public int getMaxPlayers() {
        return this.playerList.getMaxPlayers();
    }

    public String[] getPlayers() {
        return this.playerList.e();
    }

    public boolean isDebugging() {
        return false;
    }

    public void g(String s) {
        MinecraftServer.LOGGER.error(s);
    }

    public void h(String s) {
        if (this.isDebugging()) {
            MinecraftServer.LOGGER.info(s);
        }

    }

    public String getServerModName() {
        return "vanilla";
    }

    public CrashReport b(CrashReport crashreport) {
        if (this.playerList != null) {
            crashreport.g().a("Player Count", () -> {
                return this.playerList.getPlayerCount() + " / " + this.playerList.getMaxPlayers() + "; " + this.playerList.getPlayers();
            });
        }

        crashreport.g().a("Data Packs", () -> {
            StringBuilder stringbuilder = new StringBuilder();
            Iterator iterator = this.resourcePackRepository.d().iterator();

            while (iterator.hasNext()) {
                ResourcePackLoader resourcepackloader = (ResourcePackLoader) iterator.next();

                if (stringbuilder.length() > 0) {
                    stringbuilder.append(", ");
                }

                stringbuilder.append(resourcepackloader.e());
                if (!resourcepackloader.c().a()) {
                    stringbuilder.append(" (incompatible)");
                }
            }

            return stringbuilder.toString();
        });
        if (this.av != null) {
            crashreport.g().a("Server Id", () -> {
                return this.av;
            });
        }

        return crashreport;
    }

    public boolean E() {
        return this.universe != null;
    }

    @Override
    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        MinecraftServer.LOGGER.info(ichatbasecomponent.getString());
    }

    public KeyPair getKeyPair() {
        return this.I;
    }

    public int getPort() {
        return this.serverPort;
    }

    public void setPort(int i) {
        this.serverPort = i;
    }

    public String getSinglePlayerName() {
        return this.J;
    }

    public void i(String s) {
        this.J = s;
    }

    public boolean isEmbeddedServer() {
        return this.J != null;
    }

    public String getWorld() {
        return this.K;
    }

    public void a(KeyPair keypair) {
        this.I = keypair;
    }

    public void a(EnumDifficulty enumdifficulty, boolean flag) {
        Iterator iterator = this.getWorlds().iterator();

        while (iterator.hasNext()) {
            WorldServer worldserver = (WorldServer) iterator.next();
            WorldData worlddata = worldserver.getWorldData();

            if (flag || !worlddata.isDifficultyLocked()) {
                if (worlddata.isHardcore()) {
                    worlddata.setDifficulty(EnumDifficulty.HARD);
                    worldserver.setSpawnFlags(true, true);
                } else if (this.isEmbeddedServer()) {
                    worlddata.setDifficulty(enumdifficulty);
                    worldserver.setSpawnFlags(worldserver.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                } else {
                    worlddata.setDifficulty(enumdifficulty);
                    worldserver.setSpawnFlags(this.getSpawnMonsters(), this.spawnAnimals);
                }
            }
        }

        this.getPlayerList().getPlayers().forEach(this::a);
    }

    public void d(boolean flag) {
        Iterator iterator = this.getWorlds().iterator();

        while (iterator.hasNext()) {
            WorldServer worldserver = (WorldServer) iterator.next();
            WorldData worlddata = worldserver.getWorldData();

            worlddata.e(flag);
        }

        this.getPlayerList().getPlayers().forEach(this::a);
    }

    private void a(EntityPlayer entityplayer) {
        WorldData worlddata = entityplayer.getWorldServer().getWorldData();

        entityplayer.playerConnection.sendPacket(new PacketPlayOutServerDifficulty(worlddata.getDifficulty(), worlddata.isDifficultyLocked()));
    }

    protected boolean getSpawnMonsters() {
        return true;
    }

    public boolean isDemoMode() {
        return this.demoMode;
    }

    public void e(boolean flag) {
        this.demoMode = flag;
    }

    public void f(boolean flag) {
        this.bonusChest = flag;
    }

    public Convertable getConvertable() {
        return this.convertable;
    }

    public String getResourcePack() {
        return this.O;
    }

    public String getResourcePackHash() {
        return this.P;
    }

    public void setResourcePack(String s, String s1) {
        this.O = s;
        this.P = s1;
    }

    @Override
    public void a(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("whitelist_enabled", false);
        mojangstatisticsgenerator.a("whitelist_count", 0);
        if (this.playerList != null) {
            mojangstatisticsgenerator.a("players_current", this.getPlayerCount());
            mojangstatisticsgenerator.a("players_max", this.getMaxPlayers());
            mojangstatisticsgenerator.a("players_seen", this.getWorldServer(DimensionManager.OVERWORLD).getDataManager().getSeenPlayers().length);
        }

        mojangstatisticsgenerator.a("uses_auth", this.onlineMode);
        mojangstatisticsgenerator.a("gui_state", this.ah() ? "enabled" : "disabled");
        mojangstatisticsgenerator.a("run_time", (SystemUtils.getMonotonicMillis() - mojangstatisticsgenerator.g()) / 60L * 1000L);
        mojangstatisticsgenerator.a("avg_tick_ms", (int) (MathHelper.a(this.f) * 1.0E-6D));
        int i = 0;
        Iterator iterator = this.getWorlds().iterator();

        while (iterator.hasNext()) {
            WorldServer worldserver = (WorldServer) iterator.next();

            if (worldserver != null) {
                WorldData worlddata = worldserver.getWorldData();

                mojangstatisticsgenerator.a("world[" + i + "][dimension]", worldserver.worldProvider.getDimensionManager());
                mojangstatisticsgenerator.a("world[" + i + "][mode]", worlddata.getGameType());
                mojangstatisticsgenerator.a("world[" + i + "][difficulty]", worldserver.getDifficulty());
                mojangstatisticsgenerator.a("world[" + i + "][hardcore]", worlddata.isHardcore());
                mojangstatisticsgenerator.a("world[" + i + "][generator_name]", worlddata.getType().name());
                mojangstatisticsgenerator.a("world[" + i + "][generator_version]", worlddata.getType().getVersion());
                mojangstatisticsgenerator.a("world[" + i + "][height]", this.G);
                mojangstatisticsgenerator.a("world[" + i + "][chunks_loaded]", worldserver.getChunkProvider().g());
                ++i;
            }
        }

        mojangstatisticsgenerator.a("worlds", i);
    }

    public abstract boolean R();

    public boolean getOnlineMode() {
        return this.onlineMode;
    }

    public void setOnlineMode(boolean flag) {
        this.onlineMode = flag;
    }

    public boolean T() {
        return this.A;
    }

    public void h(boolean flag) {
        this.A = flag;
    }

    public boolean getSpawnAnimals() {
        return this.spawnAnimals;
    }

    public void setSpawnAnimals(boolean flag) {
        this.spawnAnimals = flag;
    }

    public boolean getSpawnNPCs() {
        return this.spawnNPCs;
    }

    public abstract boolean W();

    public void setSpawnNPCs(boolean flag) {
        this.spawnNPCs = flag;
    }

    public boolean getPVP() {
        return this.pvpMode;
    }

    public void setPVP(boolean flag) {
        this.pvpMode = flag;
    }

    public boolean getAllowFlight() {
        return this.allowFlight;
    }

    public void setAllowFlight(boolean flag) {
        this.allowFlight = flag;
    }

    public abstract boolean getEnableCommandBlock();

    public String getMotd() {
        return this.motd;
    }

    public void setMotd(String s) {
        this.motd = s;
    }

    public int getMaxBuildHeight() {
        return this.G;
    }

    public void b(int i) {
        this.G = i;
    }

    public boolean isStopped() {
        return this.isStopped;
    }

    public PlayerList getPlayerList() {
        return this.playerList;
    }

    public void a(PlayerList playerlist) {
        this.playerList = playerlist;
    }

    public abstract boolean ae();

    public void setGamemode(EnumGamemode enumgamemode) {
        Iterator iterator = this.getWorlds().iterator();

        while (iterator.hasNext()) {
            WorldServer worldserver = (WorldServer) iterator.next();

            worldserver.getWorldData().setGameType(enumgamemode);
        }

    }

    @Nullable
    public ServerConnection getServerConnection() {
        return this.serverConnection;
    }

    public boolean ah() {
        return false;
    }

    public abstract boolean a(EnumGamemode enumgamemode, boolean flag, int i);

    public int ai() {
        return this.ticks;
    }

    public void aj() {
        this.T = true;
    }

    public int getSpawnProtection() {
        return 16;
    }

    public boolean a(World world, BlockPosition blockposition, EntityHuman entityhuman) {
        return false;
    }

    public void setForceGamemode(boolean flag) {
        this.U = flag;
    }

    public boolean getForceGamemode() {
        return this.U;
    }

    public int getIdleTimeout() {
        return this.H;
    }

    public void setIdleTimeout(int i) {
        this.H = i;
    }

    public MinecraftSessionService getMinecraftSessionService() {
        return this.minecraftSessionService;
    }

    public GameProfileRepository getGameProfileRepository() {
        return this.gameProfileRepository;
    }

    public UserCache getUserCache() {
        return this.userCache;
    }

    public ServerPing getServerPing() {
        return this.serverPing;
    }

    public void invalidatePingSample() {
        this.Z = 0L;
    }

    public int av() {
        return 29999984;
    }

    @Override
    public boolean isNotMainThread() {
        return super.isNotMainThread() && !this.isStopped();
    }

    @Override
    public Thread getThread() {
        return this.serverThread;
    }

    public int ay() {
        return 256;
    }

    public long az() {
        return this.nextTick;
    }

    public DataFixer aA() {
        return this.dataConverterManager;
    }

    public int a(@Nullable WorldServer worldserver) {
        return worldserver != null ? worldserver.getGameRules().c("spawnRadius") : 10;
    }

    public AdvancementDataWorld getAdvancementData() {
        return this.an;
    }

    public CustomFunctionData getFunctionData() {
        return this.ao;
    }

    public void reload() {
        if (!this.isMainThread()) {
            this.execute(this::reload);
        } else {
            this.getPlayerList().savePlayers();
            this.resourcePackRepository.a();
            this.b(this.getWorldServer(DimensionManager.OVERWORLD).getWorldData());
            this.getPlayerList().reload();
        }
    }

    private void b(WorldData worlddata) {
        List<ResourcePackLoader> list = Lists.newArrayList(this.resourcePackRepository.d());
        Iterator iterator = this.resourcePackRepository.b().iterator();

        while (iterator.hasNext()) {
            ResourcePackLoader resourcepackloader = (ResourcePackLoader) iterator.next();

            if (!worlddata.O().contains(resourcepackloader.e()) && !list.contains(resourcepackloader)) {
                MinecraftServer.LOGGER.info("Found new data pack {}, loading it automatically", resourcepackloader.e());
                resourcepackloader.h().a(list, resourcepackloader, (resourcepackloader1) -> {
                    return resourcepackloader1;
                }, false);
            }
        }

        this.resourcePackRepository.a((Collection) list);
        List<IResourcePack> list1 = Lists.newArrayList();

        this.resourcePackRepository.d().forEach((resourcepackloader1) -> {
            list1.add(resourcepackloader1.d());
        });
        CompletableFuture<Unit> completablefuture = this.ae.a(this.executorService, this, list1, CompletableFuture.completedFuture(Unit.INSTANCE));

        this.awaitTasks(completablefuture::isDone);
        worlddata.P().clear();
        worlddata.O().clear();
        this.resourcePackRepository.d().forEach((resourcepackloader1) -> {
            worlddata.P().add(resourcepackloader1.e());
        });
        this.resourcePackRepository.b().forEach((resourcepackloader1) -> {
            if (!this.resourcePackRepository.d().contains(resourcepackloader1)) {
                worlddata.O().add(resourcepackloader1.e());
            }

        });
    }

    public void a(CommandListenerWrapper commandlistenerwrapper) {
        if (this.aP()) {
            PlayerList playerlist = commandlistenerwrapper.getServer().getPlayerList();
            WhiteList whitelist = playerlist.getWhitelist();

            if (whitelist.isEnabled()) {
                List<EntityPlayer> list = Lists.newArrayList(playerlist.getPlayers());
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                    if (!whitelist.isWhitelisted(entityplayer.getProfile())) {
                        entityplayer.playerConnection.disconnect(new ChatMessage("multiplayer.disconnect.not_whitelisted", new Object[0]));
                    }
                }

            }
        }
    }

    public IReloadableResourceManager getResourceManager() {
        return this.ae;
    }

    public ResourcePackRepository<ResourcePackLoader> getResourcePackRepository() {
        return this.resourcePackRepository;
    }

    public CommandDispatcher getCommandDispatcher() {
        return this.commandDispatcher;
    }

    public CommandListenerWrapper getServerCommandListener() {
        return new CommandListenerWrapper(this, this.getWorldServer(DimensionManager.OVERWORLD) == null ? Vec3D.a : new Vec3D(this.getWorldServer(DimensionManager.OVERWORLD).getSpawn()), Vec2F.a, this.getWorldServer(DimensionManager.OVERWORLD), 4, "Server", new ChatComponentText("Server"), this, (Entity) null);
    }

    @Override
    public boolean shouldSendSuccess() {
        return true;
    }

    @Override
    public boolean shouldSendFailure() {
        return true;
    }

    public CraftingManager getCraftingManager() {
        return this.ai;
    }

    public TagRegistry getTagRegistry() {
        return this.aj;
    }

    public ScoreboardServer getScoreboard() {
        return this.ak;
    }

    public LootTableRegistry getLootTableRegistry() {
        return this.am;
    }

    public GameRules getGameRules() {
        return this.getWorldServer(DimensionManager.OVERWORLD).getGameRules();
    }

    public BossBattleCustomData getBossBattleCustomData() {
        return this.al;
    }

    public boolean aP() {
        return this.aq;
    }

    public void n(boolean flag) {
        this.aq = flag;
    }

    public int a(GameProfile gameprofile) {
        if (this.getPlayerList().isOp(gameprofile)) {
            OpListEntry oplistentry = (OpListEntry) this.getPlayerList().getOPs().get(gameprofile);

            return oplistentry != null ? oplistentry.a() : (this.b(gameprofile) ? 4 : (this.isEmbeddedServer() ? (this.getPlayerList().v() ? 4 : 0) : this.j()));
        } else {
            return 0;
        }
    }

    public GameProfiler getMethodProfiler() {
        return this.methodProfiler;
    }

    public Executor aT() {
        return this.executorService;
    }

    public abstract boolean b(GameProfile gameprofile);
}
