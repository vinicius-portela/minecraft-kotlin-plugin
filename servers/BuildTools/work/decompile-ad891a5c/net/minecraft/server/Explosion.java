package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

public class Explosion {

    private final boolean a;
    private final Explosion.Effect b;
    private final Random c = new Random();
    private final World world;
    private final double posX;
    private final double posY;
    private final double posZ;
    public final Entity source;
    private final float size;
    private DamageSource j;
    private final List<BlockPosition> blocks = Lists.newArrayList();
    private final Map<EntityHuman, Vec3D> l = Maps.newHashMap();

    public Explosion(World world, @Nullable Entity entity, double d0, double d1, double d2, float f, boolean flag, Explosion.Effect explosion_effect) {
        this.world = world;
        this.source = entity;
        this.size = f;
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        this.a = flag;
        this.b = explosion_effect;
        this.j = DamageSource.explosion(this);
    }

    public static float a(Vec3D vec3d, Entity entity) {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox();
        double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
            int i = 0;
            int j = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                        double d5 = MathHelper.d((double) f, axisalignedbb.minX, axisalignedbb.maxX);
                        double d6 = MathHelper.d((double) f1, axisalignedbb.minY, axisalignedbb.maxY);
                        double d7 = MathHelper.d((double) f2, axisalignedbb.minZ, axisalignedbb.maxZ);
                        Vec3D vec3d1 = new Vec3D(d5 + d3, d6, d7 + d4);

                        if (entity.world.rayTrace(new RayTrace(vec3d1, vec3d, RayTrace.BlockCollisionOption.OUTLINE, RayTrace.FluidCollisionOption.NONE, entity)).getType() == MovingObjectPosition.EnumMovingObjectType.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }

    public void a() {
        Set<BlockPosition> set = Sets.newHashSet();
        boolean flag = true;

        int i;
        int j;

        for (int k = 0; k < 16; ++k) {
            for (i = 0; i < 16; ++i) {
                for (j = 0; j < 16; ++j) {
                    if (k == 0 || k == 15 || i == 0 || i == 15 || j == 0 || j == 15) {
                        double d0 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                        double d1 = (double) ((float) i / 15.0F * 2.0F - 1.0F);
                        double d2 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = this.size * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double d4 = this.posX;
                        double d5 = this.posY;
                        double d6 = this.posZ;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPosition blockposition = new BlockPosition(d4, d5, d6);
                            IBlockData iblockdata = this.world.getType(blockposition);
                            Fluid fluid = this.world.getFluid(blockposition);

                            if (!iblockdata.isAir() || !fluid.isEmpty()) {
                                float f2 = Math.max(iblockdata.getBlock().getDurability(), fluid.k());

                                if (this.source != null) {
                                    f2 = this.source.a(this, this.world, blockposition, iblockdata, fluid, f2);
                                }

                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && (this.source == null || this.source.a(this, this.world, blockposition, iblockdata, f))) {
                                set.add(blockposition);
                            }

                            d4 += d0 * 0.30000001192092896D;
                            d5 += d1 * 0.30000001192092896D;
                            d6 += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.blocks.addAll(set);
        float f3 = this.size * 2.0F;

        i = MathHelper.floor(this.posX - (double) f3 - 1.0D);
        j = MathHelper.floor(this.posX + (double) f3 + 1.0D);
        int l = MathHelper.floor(this.posY - (double) f3 - 1.0D);
        int i1 = MathHelper.floor(this.posY + (double) f3 + 1.0D);
        int j1 = MathHelper.floor(this.posZ - (double) f3 - 1.0D);
        int k1 = MathHelper.floor(this.posZ + (double) f3 + 1.0D);
        List<Entity> list = this.world.getEntities(this.source, new AxisAlignedBB((double) i, (double) l, (double) j1, (double) j, (double) i1, (double) k1));
        Vec3D vec3d = new Vec3D(this.posX, this.posY, this.posZ);

        for (int l1 = 0; l1 < list.size(); ++l1) {
            Entity entity = (Entity) list.get(l1);

            if (!entity.bR()) {
                double d7 = (double) (MathHelper.sqrt(entity.c(new Vec3D(this.posX, this.posY, this.posZ))) / f3);

                if (d7 <= 1.0D) {
                    double d8 = entity.locX - this.posX;
                    double d9 = entity.locY + (double) entity.getHeadHeight() - this.posY;
                    double d10 = entity.locZ - this.posZ;
                    double d11 = (double) MathHelper.sqrt(d8 * d8 + d9 * d9 + d10 * d10);

                    if (d11 != 0.0D) {
                        d8 /= d11;
                        d9 /= d11;
                        d10 /= d11;
                        double d12 = (double) a(vec3d, entity);
                        double d13 = (1.0D - d7) * d12;

                        entity.damageEntity(this.b(), (float) ((int) ((d13 * d13 + d13) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        double d14 = d13;

                        if (entity instanceof EntityLiving) {
                            d14 = EnchantmentProtection.a((EntityLiving) entity, d13);
                        }

                        entity.setMot(entity.getMot().add(d8 * d14, d9 * d14, d10 * d14));
                        if (entity instanceof EntityHuman) {
                            EntityHuman entityhuman = (EntityHuman) entity;

                            if (!entityhuman.isSpectator() && (!entityhuman.isCreative() || !entityhuman.abilities.isFlying)) {
                                this.l.put(entityhuman, new Vec3D(d8 * d13, d9 * d13, d10 * d13));
                            }
                        }
                    }
                }
            }
        }

    }

    public void a(boolean flag) {
        this.world.a((EntityHuman) null, this.posX, this.posY, this.posZ, SoundEffects.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
        boolean flag1 = this.b != Explosion.Effect.NONE;

        if (this.size >= 2.0F && flag1) {
            this.world.addParticle(Particles.EXPLOSION_EMITTER, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
        } else {
            this.world.addParticle(Particles.EXPLOSION, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
        }

        Iterator iterator;
        BlockPosition blockposition;

        if (flag1) {
            iterator = this.blocks.iterator();

            while (iterator.hasNext()) {
                blockposition = (BlockPosition) iterator.next();
                IBlockData iblockdata = this.world.getType(blockposition);
                Block block = iblockdata.getBlock();

                if (flag) {
                    double d0 = (double) ((float) blockposition.getX() + this.world.random.nextFloat());
                    double d1 = (double) ((float) blockposition.getY() + this.world.random.nextFloat());
                    double d2 = (double) ((float) blockposition.getZ() + this.world.random.nextFloat());
                    double d3 = d0 - this.posX;
                    double d4 = d1 - this.posY;
                    double d5 = d2 - this.posZ;
                    double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / (double) this.size + 0.1D);

                    d7 *= (double) (this.world.random.nextFloat() * this.world.random.nextFloat() + 0.3F);
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    this.world.addParticle(Particles.POOF, (d0 + this.posX) / 2.0D, (d1 + this.posY) / 2.0D, (d2 + this.posZ) / 2.0D, d3, d4, d5);
                    this.world.addParticle(Particles.SMOKE, d0, d1, d2, d3, d4, d5);
                }

                if (!iblockdata.isAir()) {
                    if (block.a(this) && this.world instanceof WorldServer) {
                        TileEntity tileentity = block.isTileEntity() ? this.world.getTileEntity(blockposition) : null;
                        LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder((WorldServer) this.world)).a(this.world.random).set(LootContextParameters.POSITION, blockposition).set(LootContextParameters.TOOL, ItemStack.a).setOptional(LootContextParameters.BLOCK_ENTITY, tileentity);

                        if (this.b == Explosion.Effect.DESTROY) {
                            loottableinfo_builder.set(LootContextParameters.EXPLOSION_RADIUS, this.size);
                        }

                        Block.b(iblockdata, loottableinfo_builder);
                    }

                    this.world.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
                    block.wasExploded(this.world, blockposition, this);
                }
            }
        }

        if (this.a) {
            iterator = this.blocks.iterator();

            while (iterator.hasNext()) {
                blockposition = (BlockPosition) iterator.next();
                if (this.world.getType(blockposition).isAir() && this.world.getType(blockposition.down()).g(this.world, blockposition.down()) && this.c.nextInt(3) == 0) {
                    this.world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
                }
            }
        }

    }

    public DamageSource b() {
        return this.j;
    }

    public void a(DamageSource damagesource) {
        this.j = damagesource;
    }

    public Map<EntityHuman, Vec3D> c() {
        return this.l;
    }

    @Nullable
    public EntityLiving getSource() {
        return this.source == null ? null : (this.source instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.source).getSource() : (this.source instanceof EntityLiving ? (EntityLiving) this.source : null));
    }

    public void clearBlocks() {
        this.blocks.clear();
    }

    public List<BlockPosition> getBlocks() {
        return this.blocks;
    }

    public static enum Effect {

        NONE, BREAK, DESTROY;

        private Effect() {}
    }
}
