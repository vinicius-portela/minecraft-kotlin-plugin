package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class TileEntityBeacon extends TileEntity implements ITileInventory, ITickable {

    public static final MobEffectList[][] a = new MobEffectList[][] { { MobEffects.FASTER_MOVEMENT, MobEffects.FASTER_DIG}, { MobEffects.RESISTANCE, MobEffects.JUMP}, { MobEffects.INCREASE_DAMAGE}, { MobEffects.REGENERATION}};
    private static final Set<MobEffectList> b = (Set) Arrays.stream(TileEntityBeacon.a).flatMap(Arrays::stream).collect(Collectors.toSet());
    private List<TileEntityBeacon.BeaconColorTracker> c = Lists.newArrayList();
    private List<TileEntityBeacon.BeaconColorTracker> g = Lists.newArrayList();
    public int levels = 0;
    private int i = -1;
    @Nullable
    public MobEffectList primaryEffect;
    @Nullable
    public MobEffectList secondaryEffect;
    @Nullable
    public IChatBaseComponent customName;
    public ChestLock chestLock;
    private final IContainerProperties containerProperties;

    public TileEntityBeacon() {
        super(TileEntityTypes.BEACON);
        this.chestLock = ChestLock.a;
        this.containerProperties = new IContainerProperties() {
            @Override
            public int getProperty(int i) {
                switch (i) {
                case 0:
                    return TileEntityBeacon.this.levels;
                case 1:
                    return MobEffectList.getId(TileEntityBeacon.this.primaryEffect);
                case 2:
                    return MobEffectList.getId(TileEntityBeacon.this.secondaryEffect);
                default:
                    return 0;
                }
            }

            @Override
            public void setProperty(int i, int j) {
                switch (i) {
                case 0:
                    TileEntityBeacon.this.levels = j;
                    break;
                case 1:
                    if (!TileEntityBeacon.this.world.isClientSide && !TileEntityBeacon.this.c.isEmpty()) {
                        TileEntityBeacon.this.a(SoundEffects.BLOCK_BEACON_POWER_SELECT);
                    }

                    TileEntityBeacon.this.primaryEffect = TileEntityBeacon.b(j);
                    break;
                case 2:
                    TileEntityBeacon.this.secondaryEffect = TileEntityBeacon.b(j);
                }

            }

            @Override
            public int a() {
                return 3;
            }
        };
    }

    @Override
    public void tick() {
        int i = this.position.getX();
        int j = this.position.getY();
        int k = this.position.getZ();
        BlockPosition blockposition;

        if (this.i < j) {
            blockposition = this.position;
            this.g = Lists.newArrayList();
            this.i = blockposition.getY() - 1;
        } else {
            blockposition = new BlockPosition(i, this.i + 1, k);
        }

        TileEntityBeacon.BeaconColorTracker tileentitybeacon_beaconcolortracker = this.g.isEmpty() ? null : (TileEntityBeacon.BeaconColorTracker) this.g.get(this.g.size() - 1);
        int l = this.world.a(HeightMap.Type.WORLD_SURFACE, i, k);

        int i1;

        for (i1 = 0; i1 < 10 && blockposition.getY() <= l; ++i1) {
            IBlockData iblockdata = this.world.getType(blockposition);
            Block block = iblockdata.getBlock();

            if (block instanceof IBeaconBeam) {
                float[] afloat = ((IBeaconBeam) block).a().d();

                if (blockposition.getY() <= j + 1) {
                    tileentitybeacon_beaconcolortracker = new TileEntityBeacon.BeaconColorTracker(afloat);
                    this.g.add(tileentitybeacon_beaconcolortracker);
                } else if (Arrays.equals(afloat, tileentitybeacon_beaconcolortracker.a)) {
                    tileentitybeacon_beaconcolortracker.a();
                } else {
                    tileentitybeacon_beaconcolortracker = new TileEntityBeacon.BeaconColorTracker(new float[] { (tileentitybeacon_beaconcolortracker.a[0] + afloat[0]) / 2.0F, (tileentitybeacon_beaconcolortracker.a[1] + afloat[1]) / 2.0F, (tileentitybeacon_beaconcolortracker.a[2] + afloat[2]) / 2.0F});
                    this.g.add(tileentitybeacon_beaconcolortracker);
                }
            } else {
                if (iblockdata.b((IBlockAccess) this.world, blockposition) >= 15 && block != Blocks.BEDROCK) {
                    this.g.clear();
                    this.i = l;
                    break;
                }

                tileentitybeacon_beaconcolortracker.a();
            }

            blockposition = blockposition.up();
            ++this.i;
        }

        if (this.i >= l) {
            this.i = -1;
            i1 = this.levels;
            boolean flag = this.levels > 0 && !this.c.isEmpty();

            this.c = this.g;
            if (!this.world.isClientSide) {
                if (i1 < this.levels) {
                    Iterator iterator = this.world.a(EntityPlayer.class, (new AxisAlignedBB((double) i, (double) j, (double) k, (double) i, (double) (j - 4), (double) k)).grow(10.0D, 5.0D, 10.0D)).iterator();

                    while (iterator.hasNext()) {
                        EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                        CriterionTriggers.l.a(entityplayer, this);
                    }
                }

                boolean flag1 = this.levels > 0 && !this.c.isEmpty();

                if (flag != flag1) {
                    this.a(flag1 ? SoundEffects.BLOCK_BEACON_ACTIVATE : SoundEffects.BLOCK_BEACON_DEACTIVATE);
                }
            }
        }

        if (this.world.getTime() % 80L == 0L) {
            if (!this.c.isEmpty()) {
                this.a(i, j, k);
            }

            if (this.levels > 0 && !this.c.isEmpty()) {
                this.applyEffects();
                this.a(SoundEffects.BLOCK_BEACON_AMBIENT);
            }
        }

    }

    private void a(int i, int j, int k) {
        this.levels = 0;

        for (int l = 1; l <= 4; this.levels = l++) {
            int i1 = j - l;

            if (i1 < 0) {
                break;
            }

            boolean flag = true;

            for (int j1 = i - l; j1 <= i + l && flag; ++j1) {
                for (int k1 = k - l; k1 <= k + l; ++k1) {
                    Block block = this.world.getType(new BlockPosition(j1, i1, k1)).getBlock();

                    if (block != Blocks.EMERALD_BLOCK && block != Blocks.GOLD_BLOCK && block != Blocks.DIAMOND_BLOCK && block != Blocks.IRON_BLOCK) {
                        flag = false;
                        break;
                    }
                }
            }

            if (!flag) {
                break;
            }
        }

    }

    private void applyEffects() {
        if (!this.world.isClientSide && this.primaryEffect != null) {
            double d0 = (double) (this.levels * 10 + 10);
            byte b0 = 0;

            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
                b0 = 1;
            }

            int i = (9 + this.levels * 2) * 20;
            AxisAlignedBB axisalignedbb = (new AxisAlignedBB(this.position)).g(d0).b(0.0D, (double) this.world.getHeight(), 0.0D);
            List<EntityHuman> list = this.world.a(EntityHuman.class, axisalignedbb);
            Iterator iterator = list.iterator();

            EntityHuman entityhuman;

            while (iterator.hasNext()) {
                entityhuman = (EntityHuman) iterator.next();
                entityhuman.addEffect(new MobEffect(this.primaryEffect, i, b0, true, true));
            }

            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect != null) {
                iterator = list.iterator();

                while (iterator.hasNext()) {
                    entityhuman = (EntityHuman) iterator.next();
                    entityhuman.addEffect(new MobEffect(this.secondaryEffect, i, 0, true, true));
                }
            }

        }
    }

    public void a(SoundEffect soundeffect) {
        this.world.a((EntityHuman) null, this.position, soundeffect, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public int f() {
        return this.levels;
    }

    @Nullable
    @Override
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return new PacketPlayOutTileEntityData(this.position, 3, this.b());
    }

    @Override
    public NBTTagCompound b() {
        return this.save(new NBTTagCompound());
    }

    @Nullable
    private static MobEffectList b(int i) {
        MobEffectList mobeffectlist = MobEffectList.fromId(i);

        return TileEntityBeacon.b.contains(mobeffectlist) ? mobeffectlist : null;
    }

    @Override
    public void load(NBTTagCompound nbttagcompound) {
        super.load(nbttagcompound);
        this.primaryEffect = b(nbttagcompound.getInt("Primary"));
        this.secondaryEffect = b(nbttagcompound.getInt("Secondary"));
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.customName = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("CustomName"));
        }

        this.chestLock = ChestLock.b(nbttagcompound);
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        nbttagcompound.setInt("Primary", MobEffectList.getId(this.primaryEffect));
        nbttagcompound.setInt("Secondary", MobEffectList.getId(this.secondaryEffect));
        if (this.customName != null) {
            nbttagcompound.setString("CustomName", IChatBaseComponent.ChatSerializer.a(this.customName));
        }

        this.chestLock.a(nbttagcompound);
        return nbttagcompound;
    }

    public void setCustomName(@Nullable IChatBaseComponent ichatbasecomponent) {
        this.customName = ichatbasecomponent;
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerinventory, EntityHuman entityhuman) {
        return TileEntityContainer.a(entityhuman, this.chestLock, this.getScoreboardDisplayName()) ? new ContainerBeacon(i, playerinventory, this.containerProperties, ContainerAccess.at(this.world, this.getPosition())) : null;
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return (IChatBaseComponent) (this.customName != null ? this.customName : new ChatMessage("container.beacon", new Object[0]));
    }

    public static class BeaconColorTracker {

        private final float[] a;
        private int b;

        public BeaconColorTracker(float[] afloat) {
            this.a = afloat;
            this.b = 1;
        }

        protected void a() {
            ++this.b;
        }
    }
}
