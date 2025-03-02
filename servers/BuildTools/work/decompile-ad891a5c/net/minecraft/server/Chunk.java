package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk implements IChunkAccess {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final ChunkSection a = null;
    private final ChunkSection[] sections;
    private final BiomeBase[] d;
    private final Map<BlockPosition, NBTTagCompound> e;
    public boolean loaded;
    public final World world;
    public final Map<HeightMap.Type, HeightMap> heightMap;
    private final ChunkConverter i;
    public final Map<BlockPosition, TileEntity> tileEntities;
    public final EntitySlice<Entity>[] entitySlices;
    private final Map<String, StructureStart> l;
    private final Map<String, LongSet> m;
    private final ShortList[] n;
    private TickList<Block> o;
    private TickList<FluidType> p;
    private boolean q;
    private long lastSaved;
    private boolean s;
    private long t;
    @Nullable
    private Supplier<PlayerChunk.State> u;
    @Nullable
    private Consumer<Chunk> v;
    private final ChunkCoordIntPair loc;
    private volatile boolean x;

    public Chunk(World world, ChunkCoordIntPair chunkcoordintpair, BiomeBase[] abiomebase, ChunkConverter chunkconverter, TickList<Block> ticklist, TickList<FluidType> ticklist1, long i, @Nullable ChunkSection[] achunksection, @Nullable Consumer<Chunk> consumer) {
        this.sections = new ChunkSection[16];
        this.e = Maps.newHashMap();
        this.heightMap = Maps.newEnumMap(HeightMap.Type.class);
        this.tileEntities = Maps.newHashMap();
        this.l = Maps.newHashMap();
        this.m = Maps.newHashMap();
        this.n = new ShortList[16];
        this.entitySlices = (EntitySlice[]) (new EntitySlice[16]);
        this.world = world;
        this.loc = chunkcoordintpair;
        this.i = chunkconverter;
        HeightMap.Type[] aheightmap_type = HeightMap.Type.values();
        int j = aheightmap_type.length;

        for (int k = 0; k < j; ++k) {
            HeightMap.Type heightmap_type = aheightmap_type[k];

            if (ChunkStatus.FULL.h().contains(heightmap_type)) {
                this.heightMap.put(heightmap_type, new HeightMap(this, heightmap_type));
            }
        }

        for (int l = 0; l < this.entitySlices.length; ++l) {
            this.entitySlices[l] = new EntitySlice<>(Entity.class);
        }

        this.d = abiomebase;
        this.o = ticklist;
        this.p = ticklist1;
        this.t = i;
        this.v = consumer;
        if (achunksection != null) {
            if (this.sections.length == achunksection.length) {
                System.arraycopy(achunksection, 0, this.sections, 0, this.sections.length);
            } else {
                Chunk.LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", achunksection.length, this.sections.length);
            }
        }

    }

    public Chunk(World world, ProtoChunk protochunk) {
        this(world, protochunk.getPos(), protochunk.getBiomeIndex(), protochunk.p(), protochunk.n(), protochunk.o(), protochunk.q(), protochunk.getSections(), (Consumer) null);
        Iterator iterator = protochunk.y().iterator();

        while (iterator.hasNext()) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) iterator.next();

            EntityTypes.a(nbttagcompound, world, (entity) -> {
                this.a(entity);
                return entity;
            });
        }

        iterator = protochunk.x().values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            this.a(tileentity);
        }

        this.e.putAll(protochunk.z());

        for (int i = 0; i < protochunk.l().length; ++i) {
            this.n[i] = protochunk.l()[i];
        }

        this.a(protochunk.h());
        this.b(protochunk.v());
        iterator = protochunk.f().iterator();

        while (iterator.hasNext()) {
            Entry<HeightMap.Type, HeightMap> entry = (Entry) iterator.next();

            if (ChunkStatus.FULL.h().contains(entry.getKey())) {
                this.b((HeightMap.Type) entry.getKey()).a(((HeightMap) entry.getValue()).a());
            }
        }

        this.b(protochunk.r());
        this.s = true;
    }

    @Override
    public HeightMap b(HeightMap.Type heightmap_type) {
        return (HeightMap) this.heightMap.computeIfAbsent(heightmap_type, (heightmap_type1) -> {
            return new HeightMap(this, heightmap_type1);
        });
    }

    @Override
    public Set<BlockPosition> c() {
        Set<BlockPosition> set = Sets.newHashSet(this.e.keySet());

        set.addAll(this.tileEntities.keySet());
        return set;
    }

    @Override
    public ChunkSection[] getSections() {
        return this.sections;
    }

    @Override
    public IBlockData getType(BlockPosition blockposition) {
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();

        if (this.world.P() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            IBlockData iblockdata = null;

            if (j == 60) {
                iblockdata = Blocks.BARRIER.getBlockData();
            }

            if (j == 70) {
                iblockdata = ChunkProviderDebug.a(i, k);
            }

            return iblockdata == null ? Blocks.AIR.getBlockData() : iblockdata;
        } else {
            try {
                if (j >= 0 && j >> 4 < this.sections.length) {
                    ChunkSection chunksection = this.sections[j >> 4];

                    if (!ChunkSection.a(chunksection)) {
                        return chunksection.getType(i & 15, j & 15, k & 15);
                    }
                }

                return Blocks.AIR.getBlockData();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Getting block state");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being got");

                crashreportsystemdetails.a("Location", () -> {
                    return CrashReportSystemDetails.a(i, j, k);
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        return this.a(blockposition.getX(), blockposition.getY(), blockposition.getZ());
    }

    public Fluid a(int i, int j, int k) {
        try {
            if (j >= 0 && j >> 4 < this.sections.length) {
                ChunkSection chunksection = this.sections[j >> 4];

                if (!ChunkSection.a(chunksection)) {
                    return chunksection.b(i & 15, j & 15, k & 15);
                }
            }

            return FluidTypes.EMPTY.i();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Getting fluid state");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being got");

            crashreportsystemdetails.a("Location", () -> {
                return CrashReportSystemDetails.a(i, j, k);
            });
            throw new ReportedException(crashreport);
        }
    }

    @Nullable
    @Override
    public IBlockData setType(BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        int i = blockposition.getX() & 15;
        int j = blockposition.getY();
        int k = blockposition.getZ() & 15;
        ChunkSection chunksection = this.sections[j >> 4];

        if (chunksection == Chunk.a) {
            if (iblockdata.isAir()) {
                return null;
            }

            chunksection = new ChunkSection(j >> 4 << 4);
            this.sections[j >> 4] = chunksection;
        }

        boolean flag1 = chunksection.c();
        IBlockData iblockdata1 = chunksection.setType(i, j & 15, k, iblockdata);

        if (iblockdata1 == iblockdata) {
            return null;
        } else {
            Block block = iblockdata.getBlock();
            Block block1 = iblockdata1.getBlock();

            ((HeightMap) this.heightMap.get(HeightMap.Type.MOTION_BLOCKING)).a(i, j, k, iblockdata);
            ((HeightMap) this.heightMap.get(HeightMap.Type.MOTION_BLOCKING_NO_LEAVES)).a(i, j, k, iblockdata);
            ((HeightMap) this.heightMap.get(HeightMap.Type.OCEAN_FLOOR)).a(i, j, k, iblockdata);
            ((HeightMap) this.heightMap.get(HeightMap.Type.WORLD_SURFACE)).a(i, j, k, iblockdata);
            boolean flag2 = chunksection.c();

            if (flag1 != flag2) {
                this.world.getChunkProvider().getLightEngine().a(blockposition, flag2);
            }

            if (!this.world.isClientSide) {
                iblockdata1.remove(this.world, blockposition, iblockdata, flag);
            } else if (block1 != block && block1 instanceof ITileEntity) {
                this.world.removeTileEntity(blockposition);
            }

            if (chunksection.getType(i, j & 15, k).getBlock() != block) {
                return null;
            } else {
                TileEntity tileentity;

                if (block1 instanceof ITileEntity) {
                    tileentity = this.a(blockposition, Chunk.EnumTileEntityState.CHECK);
                    if (tileentity != null) {
                        tileentity.invalidateBlockCache();
                    }
                }

                if (!this.world.isClientSide) {
                    iblockdata.onPlace(this.world, blockposition, iblockdata1, flag);
                }

                if (block instanceof ITileEntity) {
                    tileentity = this.a(blockposition, Chunk.EnumTileEntityState.CHECK);
                    if (tileentity == null) {
                        tileentity = ((ITileEntity) block).createTile(this.world);
                        this.world.setTileEntity(blockposition, tileentity);
                    } else {
                        tileentity.invalidateBlockCache();
                    }
                }

                this.s = true;
                return iblockdata1;
            }
        }
    }

    @Nullable
    @Override
    public LightEngine e() {
        return this.world.getChunkProvider().getLightEngine();
    }

    public int a(BlockPosition blockposition, int i) {
        return this.a(blockposition, i, this.world.getWorldProvider().g());
    }

    @Override
    public void a(Entity entity) {
        this.q = true;
        int i = MathHelper.floor(entity.locX / 16.0D);
        int j = MathHelper.floor(entity.locZ / 16.0D);

        if (i != this.loc.x || j != this.loc.z) {
            Chunk.LOGGER.warn("Wrong location! ({}, {}) should be ({}, {}), {}", i, j, this.loc.x, this.loc.z, entity);
            entity.dead = true;
        }

        int k = MathHelper.floor(entity.locY / 16.0D);

        if (k < 0) {
            k = 0;
        }

        if (k >= this.entitySlices.length) {
            k = this.entitySlices.length - 1;
        }

        entity.inChunk = true;
        entity.chunkX = this.loc.x;
        entity.chunkY = k;
        entity.chunkZ = this.loc.z;
        this.entitySlices[k].add(entity);
    }

    @Override
    public void a(HeightMap.Type heightmap_type, long[] along) {
        ((HeightMap) this.heightMap.get(heightmap_type)).a(along);
    }

    public void b(Entity entity) {
        this.a(entity, entity.chunkY);
    }

    public void a(Entity entity, int i) {
        if (i < 0) {
            i = 0;
        }

        if (i >= this.entitySlices.length) {
            i = this.entitySlices.length - 1;
        }

        this.entitySlices[i].remove(entity);
    }

    @Override
    public int a(HeightMap.Type heightmap_type, int i, int j) {
        return ((HeightMap) this.heightMap.get(heightmap_type)).a(i & 15, j & 15) - 1;
    }

    @Nullable
    private TileEntity j(BlockPosition blockposition) {
        IBlockData iblockdata = this.getType(blockposition);
        Block block = iblockdata.getBlock();

        return !block.isTileEntity() ? null : ((ITileEntity) block).createTile(this.world);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPosition blockposition) {
        return this.a(blockposition, Chunk.EnumTileEntityState.CHECK);
    }

    @Nullable
    public TileEntity a(BlockPosition blockposition, Chunk.EnumTileEntityState chunk_enumtileentitystate) {
        TileEntity tileentity = (TileEntity) this.tileEntities.get(blockposition);

        if (tileentity == null) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) this.e.remove(blockposition);

            if (nbttagcompound != null) {
                TileEntity tileentity1 = this.a(blockposition, nbttagcompound);

                if (tileentity1 != null) {
                    return tileentity1;
                }
            }
        }

        if (tileentity == null) {
            if (chunk_enumtileentitystate == Chunk.EnumTileEntityState.IMMEDIATE) {
                tileentity = this.j(blockposition);
                this.world.setTileEntity(blockposition, tileentity);
            }
        } else if (tileentity.isRemoved()) {
            this.tileEntities.remove(blockposition);
            return null;
        }

        return tileentity;
    }

    public void a(TileEntity tileentity) {
        this.setTileEntity(tileentity.getPosition(), tileentity);
        if (this.loaded || this.world.e()) {
            this.world.setTileEntity(tileentity.getPosition(), tileentity);
        }

    }

    @Override
    public void setTileEntity(BlockPosition blockposition, TileEntity tileentity) {
        if (this.getType(blockposition).getBlock() instanceof ITileEntity) {
            tileentity.setWorld(this.world);
            tileentity.setPosition(blockposition);
            tileentity.n();
            TileEntity tileentity1 = (TileEntity) this.tileEntities.put(blockposition.immutableCopy(), tileentity);

            if (tileentity1 != null && tileentity1 != tileentity) {
                tileentity1.m();
            }

        }
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        this.e.put(new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z")), nbttagcompound);
    }

    @Override
    public void removeTileEntity(BlockPosition blockposition) {
        if (this.loaded || this.world.e()) {
            TileEntity tileentity = (TileEntity) this.tileEntities.remove(blockposition);

            if (tileentity != null) {
                tileentity.m();
            }
        }

    }

    public void addEntities() {
        if (this.v != null) {
            this.v.accept(this);
            this.v = null;
        }

    }

    public void markDirty() {
        this.s = true;
    }

    public void a(@Nullable Entity entity, AxisAlignedBB axisalignedbb, List<Entity> list, @Nullable Predicate<? super Entity> predicate) {
        int i = MathHelper.floor((axisalignedbb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxY + 2.0D) / 16.0D);

        i = MathHelper.clamp(i, 0, this.entitySlices.length - 1);
        j = MathHelper.clamp(j, 0, this.entitySlices.length - 1);

        for (int k = i; k <= j; ++k) {
            if (!this.entitySlices[k].isEmpty()) {
                Iterator iterator = this.entitySlices[k].iterator();

                while (iterator.hasNext()) {
                    Entity entity1 = (Entity) iterator.next();

                    if (entity1.getBoundingBox().c(axisalignedbb) && entity1 != entity) {
                        if (predicate == null || predicate.test(entity1)) {
                            list.add(entity1);
                        }

                        if (entity1 instanceof EntityEnderDragon) {
                            EntityComplexPart[] aentitycomplexpart = ((EntityEnderDragon) entity1).dT();
                            int l = aentitycomplexpart.length;

                            for (int i1 = 0; i1 < l; ++i1) {
                                EntityComplexPart entitycomplexpart = aentitycomplexpart[i1];

                                if (entitycomplexpart != entity && entitycomplexpart.getBoundingBox().c(axisalignedbb) && (predicate == null || predicate.test(entitycomplexpart))) {
                                    list.add(entitycomplexpart);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public void a(@Nullable EntityTypes<?> entitytypes, AxisAlignedBB axisalignedbb, List<Entity> list, Predicate<? super Entity> predicate) {
        int i = MathHelper.floor((axisalignedbb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxY + 2.0D) / 16.0D);

        i = MathHelper.clamp(i, 0, this.entitySlices.length - 1);
        j = MathHelper.clamp(j, 0, this.entitySlices.length - 1);

        for (int k = i; k <= j; ++k) {
            Iterator iterator = this.entitySlices[k].a(Entity.class).iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if ((entitytypes == null || entity.getEntityType() == entitytypes) && entity.getBoundingBox().c(axisalignedbb) && predicate.test(entity)) {
                    list.add(entity);
                }
            }
        }

    }

    public <T extends Entity> void a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, List<T> list, @Nullable Predicate<? super T> predicate) {
        int i = MathHelper.floor((axisalignedbb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxY + 2.0D) / 16.0D);

        i = MathHelper.clamp(i, 0, this.entitySlices.length - 1);
        j = MathHelper.clamp(j, 0, this.entitySlices.length - 1);

        for (int k = i; k <= j; ++k) {
            Iterator iterator = this.entitySlices[k].a(oclass).iterator();

            while (iterator.hasNext()) {
                T t0 = (Entity) iterator.next();

                if (t0.getBoundingBox().c(axisalignedbb) && (predicate == null || predicate.test(t0))) {
                    list.add(t0);
                }
            }
        }

    }

    public boolean isEmpty() {
        return false;
    }

    @Override
    public ChunkCoordIntPair getPos() {
        return this.loc;
    }

    @Override
    public BiomeBase[] getBiomeIndex() {
        return this.d;
    }

    public void c(boolean flag) {
        this.loaded = flag;
    }

    public World getWorld() {
        return this.world;
    }

    @Override
    public Collection<Entry<HeightMap.Type, HeightMap>> f() {
        return Collections.unmodifiableSet(this.heightMap.entrySet());
    }

    public Map<BlockPosition, TileEntity> getTileEntities() {
        return this.tileEntities;
    }

    public EntitySlice<Entity>[] getEntitySlices() {
        return this.entitySlices;
    }

    @Override
    public NBTTagCompound i(BlockPosition blockposition) {
        return (NBTTagCompound) this.e.get(blockposition);
    }

    @Override
    public Stream<BlockPosition> m() {
        return StreamSupport.stream(BlockPosition.b(this.loc.d(), 0, this.loc.e(), this.loc.f(), 255, this.loc.g()).spliterator(), false).filter((blockposition) -> {
            return this.getType(blockposition).h() != 0;
        });
    }

    @Override
    public TickList<Block> n() {
        return this.o;
    }

    @Override
    public TickList<FluidType> o() {
        return this.p;
    }

    @Override
    public void setNeedsSaving(boolean flag) {
        this.s = flag;
    }

    @Override
    public boolean isNeedsSaving() {
        return this.s || this.q && this.world.getTime() != this.lastSaved;
    }

    public void d(boolean flag) {
        this.q = flag;
    }

    @Override
    public void setLastSaved(long i) {
        this.lastSaved = i;
    }

    @Nullable
    @Override
    public StructureStart a(String s) {
        return (StructureStart) this.l.get(s);
    }

    @Override
    public void a(String s, StructureStart structurestart) {
        this.l.put(s, structurestart);
    }

    @Override
    public Map<String, StructureStart> h() {
        return this.l;
    }

    @Override
    public void a(Map<String, StructureStart> map) {
        this.l.clear();
        this.l.putAll(map);
    }

    @Override
    public LongSet b(String s) {
        return (LongSet) this.m.computeIfAbsent(s, (s1) -> {
            return new LongOpenHashSet();
        });
    }

    @Override
    public void a(String s, long i) {
        ((LongSet) this.m.computeIfAbsent(s, (s1) -> {
            return new LongOpenHashSet();
        })).add(i);
    }

    @Override
    public Map<String, LongSet> v() {
        return this.m;
    }

    @Override
    public void b(Map<String, LongSet> map) {
        this.m.clear();
        this.m.putAll(map);
    }

    @Override
    public long q() {
        return this.t;
    }

    @Override
    public void b(long i) {
        this.t = i;
    }

    public void A() {
        ChunkCoordIntPair chunkcoordintpair = this.getPos();

        for (int i = 0; i < this.n.length; ++i) {
            if (this.n[i] != null) {
                ShortListIterator shortlistiterator = this.n[i].iterator();

                while (shortlistiterator.hasNext()) {
                    Short oshort = (Short) shortlistiterator.next();
                    BlockPosition blockposition = ProtoChunk.a(oshort, i, chunkcoordintpair);
                    IBlockData iblockdata = this.getType(blockposition);
                    IBlockData iblockdata1 = Block.b(iblockdata, (GeneratorAccess) this.world, blockposition);

                    this.world.setTypeAndData(blockposition, iblockdata1, 20);
                }

                this.n[i].clear();
            }
        }

        if (this.o instanceof ProtoChunkTickList) {
            ((ProtoChunkTickList) this.o).a(this.world.getBlockTickList(), (blockposition1) -> {
                return this.getType(blockposition1).getBlock();
            });
        } else if (this.o instanceof TickListChunk) {
            this.world.getBlockTickList().a(((TickListChunk) this.o).a());
            this.o = TickListEmpty.a();
        }

        if (this.p instanceof ProtoChunkTickList) {
            ((ProtoChunkTickList) this.p).a(this.world.getFluidTickList(), (blockposition1) -> {
                return this.getFluid(blockposition1).getType();
            });
        } else if (this.p instanceof TickListChunk) {
            this.world.getFluidTickList().a(((TickListChunk) this.p).a());
            this.p = TickListEmpty.a();
        }

        Iterator iterator = Sets.newHashSet(this.e.keySet()).iterator();

        while (iterator.hasNext()) {
            BlockPosition blockposition1 = (BlockPosition) iterator.next();

            this.getTileEntity(blockposition1);
        }

        this.e.clear();
        this.i.a(this);
    }

    @Nullable
    private TileEntity a(BlockPosition blockposition, NBTTagCompound nbttagcompound) {
        TileEntity tileentity;

        if ("DUMMY".equals(nbttagcompound.getString("id"))) {
            Block block = this.getType(blockposition).getBlock();

            if (block instanceof ITileEntity) {
                tileentity = ((ITileEntity) block).createTile(this.world);
            } else {
                tileentity = null;
                Chunk.LOGGER.warn("Tried to load a DUMMY block entity @ {} but found not block entity block {} at location", blockposition, this.getType(blockposition));
            }
        } else {
            tileentity = TileEntity.create(nbttagcompound);
        }

        if (tileentity != null) {
            tileentity.setPosition(blockposition);
            this.a(tileentity);
        } else {
            Chunk.LOGGER.warn("Tried to load a block entity for block {} but failed at location {}", this.getType(blockposition), blockposition);
        }

        return tileentity;
    }

    @Override
    public ChunkConverter p() {
        return this.i;
    }

    @Override
    public ShortList[] l() {
        return this.n;
    }

    public void a(WorldServer worldserver) {
        this.o = new TickListChunk<>(IRegistry.BLOCK::getKey, worldserver.getBlockTickList().a(true, this.loc));
        this.p = new TickListChunk<>(IRegistry.FLUID::getKey, worldserver.getFluidTickList().a(true, this.loc));
        this.setNeedsSaving(true);
    }

    @Override
    public ChunkStatus getChunkStatus() {
        return ChunkStatus.FULL;
    }

    public PlayerChunk.State getState() {
        return this.u == null ? PlayerChunk.State.BORDER : (PlayerChunk.State) this.u.get();
    }

    public void a(Supplier<PlayerChunk.State> supplier) {
        this.u = supplier;
    }

    @Override
    public void a(LightEngine lightengine) {}

    @Override
    public boolean r() {
        return this.x;
    }

    @Override
    public void b(boolean flag) {
        this.x = flag;
        this.setNeedsSaving(true);
    }

    public static enum EnumTileEntityState {

        IMMEDIATE, QUEUED, CHECK;

        private EnumTileEntityState() {}
    }
}
