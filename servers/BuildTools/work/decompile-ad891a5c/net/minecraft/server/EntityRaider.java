package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public abstract class EntityRaider extends EntityMonsterPatrolling {

    protected static final DataWatcherObject<Boolean> c = DataWatcher.a(EntityRaider.class, DataWatcherRegistry.i);
    private static final Predicate<EntityItem> b = (entityitem) -> {
        return !entityitem.q() && entityitem.isAlive() && ItemStack.matches(entityitem.getItemStack(), Raid.a);
    };
    @Nullable
    protected Raid d;
    private int bz;
    private boolean bA;
    private int bB;

    protected EntityRaider(EntityTypes<? extends EntityRaider> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(1, new EntityRaider.b<>(this));
        this.goalSelector.a(3, new PathfinderGoalRaid<>(this));
        this.goalSelector.a(4, new EntityRaider.d(this, 1.0499999523162842D, 1));
        this.goalSelector.a(5, new EntityRaider.c(this));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityRaider.c, false);
    }

    public abstract void a(int i, boolean flag);

    public boolean ej() {
        return this.bA;
    }

    public void t(boolean flag) {
        this.bA = flag;
    }

    @Override
    public void movementTick() {
        if (this.world instanceof WorldServer && this.isAlive()) {
            Raid raid = this.ek();

            if (this.ej()) {
                if (raid == null) {
                    if (this.world.getTime() % 20L == 0L) {
                        Raid raid1 = ((WorldServer) this.world).c_(new BlockPosition(this));

                        if (raid1 != null && PersistentRaid.a(this, raid1)) {
                            raid1.a(raid1.l(), this, (BlockPosition) null, true);
                        }
                    }
                } else {
                    EntityLiving entityliving = this.getGoalTarget();

                    if (entityliving != null && (entityliving.getEntityType() == EntityTypes.PLAYER || entityliving.getEntityType() == EntityTypes.IRON_GOLEM)) {
                        this.ticksFarFromPlayer = 0;
                    }
                }
            }
        }

        super.movementTick();
    }

    @Override
    protected void ec() {
        this.ticksFarFromPlayer += 2;
    }

    @Override
    public void die(DamageSource damagesource) {
        if (this.world instanceof WorldServer) {
            Entity entity = damagesource.getEntity();

            if (this.ek() != null) {
                if (this.isPatrolLeader()) {
                    this.ek().c(this.em());
                }

                if (entity != null && entity.getEntityType() == EntityTypes.PLAYER) {
                    this.ek().a(entity);
                }

                this.ek().a(this, false);
            }

            if (this.isPatrolLeader() && this.ek() == null && ((WorldServer) this.world).c_(new BlockPosition(this)) == null) {
                ItemStack itemstack = this.getEquipment(EnumItemSlot.HEAD);
                EntityHuman entityhuman = null;

                if (entity instanceof EntityHuman) {
                    entityhuman = (EntityHuman) entity;
                } else if (entity instanceof EntityWolf) {
                    EntityWolf entitywolf = (EntityWolf) entity;
                    EntityLiving entityliving = entitywolf.getOwner();

                    if (entitywolf.isTamed() && entityliving instanceof EntityHuman) {
                        entityhuman = (EntityHuman) entityliving;
                    }
                }

                if (!itemstack.isEmpty() && ItemStack.matches(itemstack, Raid.a) && entityhuman != null) {
                    MobEffect mobeffect = entityhuman.getEffect(MobEffects.BAD_OMEN);
                    byte b0 = 1;
                    int i;

                    if (mobeffect != null) {
                        i = b0 + mobeffect.getAmplifier();
                        entityhuman.c(MobEffects.BAD_OMEN);
                    } else {
                        i = b0 - 1;
                    }

                    i = MathHelper.clamp(i, 0, 5);
                    MobEffect mobeffect1 = new MobEffect(MobEffects.BAD_OMEN, 120000, i, false, false, true);

                    entityhuman.addEffect(mobeffect1);
                }
            }
        }

        super.die(damagesource);
    }

    @Override
    public boolean ed() {
        return !this.el();
    }

    public void a(@Nullable Raid raid) {
        this.d = raid;
    }

    @Nullable
    public Raid ek() {
        return this.d;
    }

    public boolean el() {
        return this.ek() != null && this.ek().v();
    }

    public void a(int i) {
        this.bz = i;
    }

    public int em() {
        return this.bz;
    }

    public void u(boolean flag) {
        this.datawatcher.set(EntityRaider.c, flag);
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Wave", this.bz);
        nbttagcompound.setBoolean("CanJoinRaid", this.bA);
        if (this.d != null) {
            nbttagcompound.setInt("RaidId", this.d.u());
        }

    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.bz = nbttagcompound.getInt("Wave");
        this.bA = nbttagcompound.getBoolean("CanJoinRaid");
        if (nbttagcompound.hasKeyOfType("RaidId", 3)) {
            if (this.world instanceof WorldServer) {
                this.d = ((WorldServer) this.world).C().a(nbttagcompound.getInt("RaidId"));
            }

            if (this.d != null) {
                this.d.a(this.bz, this, false);
                if (this.isPatrolLeader()) {
                    this.d.a(this.bz, this);
                }
            }
        }

    }

    @Override
    protected void a(EntityItem entityitem) {
        ItemStack itemstack = entityitem.getItemStack();
        boolean flag = this.el() && this.ek().b(this.em()) != null;

        if (this.el() && !flag && ItemStack.matches(itemstack, Raid.a)) {
            EnumItemSlot enumitemslot = EnumItemSlot.HEAD;
            ItemStack itemstack1 = this.getEquipment(enumitemslot);
            double d0 = (double) this.d(enumitemslot);

            if (!itemstack1.isEmpty() && (double) (this.random.nextFloat() - 0.1F) < d0) {
                this.a(itemstack1);
            }

            this.setSlot(enumitemslot, itemstack);
            this.receive(entityitem, itemstack.getCount());
            entityitem.die();
            this.ek().a(this.em(), this);
            this.setPatrolLeader(true);
        } else {
            super.a(entityitem);
        }

    }

    @Override
    public boolean isTypeNotPersistent(double d0) {
        return this.ek() == null ? super.isTypeNotPersistent(d0) : false;
    }

    @Override
    public boolean I() {
        return this.ek() != null;
    }

    public int eo() {
        return this.bB;
    }

    public void b(int i) {
        this.bB = i;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.el()) {
            this.ek().q();
        }

        return super.damageEntity(damagesource, f);
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(GeneratorAccess generatoraccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.t(this.getEntityType() != EntityTypes.WITCH || enummobspawn != EnumMobSpawn.NATURAL);
        return super.prepare(generatoraccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    public abstract SoundEffect dW();

    static class d extends PathfinderGoal {

        private final EntityRaider a;
        private final double b;
        private BlockPosition c;
        private final List<BlockPosition> d = Lists.newArrayList();
        private final int e;
        private boolean f;

        public d(EntityRaider entityraider, double d0, int i) {
            this.a = entityraider;
            this.b = d0;
            this.e = i;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            this.j();
            return this.g() && this.h() && this.a.getGoalTarget() == null;
        }

        private boolean g() {
            return this.a.el() && !this.a.ek().a();
        }

        private boolean h() {
            WorldServer worldserver = (WorldServer) this.a.world;
            BlockPosition blockposition = new BlockPosition(this.a);
            Optional<BlockPosition> optional = worldserver.B().a((villageplacetype) -> {
                return villageplacetype == VillagePlaceType.q;
            }, this::a, VillagePlace.Occupancy.ANY, blockposition, 48, this.a.random);

            if (!optional.isPresent()) {
                return false;
            } else {
                this.c = ((BlockPosition) optional.get()).immutableCopy();
                return true;
            }
        }

        @Override
        public boolean b() {
            return this.a.getNavigation().n() ? false : this.a.getGoalTarget() == null && !this.c.a((IPosition) this.a.ch(), (double) (this.a.getWidth() + (float) this.e)) && !this.f;
        }

        @Override
        public void d() {
            if (this.c.a((IPosition) this.a.ch(), (double) this.e)) {
                this.d.add(this.c);
            }

        }

        @Override
        public void c() {
            super.c();
            this.a.m(0);
            this.a.getNavigation().a((double) this.c.getX(), (double) this.c.getY(), (double) this.c.getZ(), this.b);
            this.f = false;
        }

        @Override
        public void e() {
            if (this.a.getNavigation().n()) {
                int i = this.c.getX();
                int j = this.c.getY();
                int k = this.c.getZ();
                Vec3D vec3d = RandomPositionGenerator.a((EntityCreature) this.a, 16, 7, new Vec3D((double) i, (double) j, (double) k), 0.3141592741012573D);

                if (vec3d == null) {
                    vec3d = RandomPositionGenerator.a(this.a, 8, 7, new Vec3D((double) i, (double) j, (double) k));
                }

                if (vec3d == null) {
                    this.f = true;
                    return;
                }

                this.a.getNavigation().a(vec3d.x, vec3d.y, vec3d.z, this.b);
            }

        }

        private boolean a(BlockPosition blockposition) {
            Iterator iterator = this.d.iterator();

            BlockPosition blockposition1;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                blockposition1 = (BlockPosition) iterator.next();
            } while (!Objects.equals(blockposition, blockposition1));

            return false;
        }

        private void j() {
            if (this.d.size() > 2) {
                this.d.remove(0);
            }

        }
    }

    public class a extends PathfinderGoal {

        private final EntityRaider c;
        private final float d;
        public final PathfinderTargetCondition a = (new PathfinderTargetCondition()).a(8.0D).d().a().b().c().e();

        public a(EntityIllagerAbstract entityillagerabstract, float f) {
            this.c = entityillagerabstract;
            this.d = f * f;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean a() {
            EntityLiving entityliving = this.c.getLastDamager();

            return this.c.ek() == null && this.c.isPatrolling() && this.c.getGoalTarget() != null && !this.c.dR() && (entityliving == null || entityliving.getEntityType() != EntityTypes.PLAYER);
        }

        @Override
        public void c() {
            super.c();
            this.c.getNavigation().o();
            List<EntityRaider> list = this.c.world.a(EntityRaider.class, this.a, this.c, this.c.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityRaider entityraider = (EntityRaider) iterator.next();

                entityraider.setGoalTarget(this.c.getGoalTarget());
            }

        }

        @Override
        public void d() {
            super.d();
            EntityLiving entityliving = this.c.getGoalTarget();

            if (entityliving != null) {
                List<EntityRaider> list = this.c.world.a(EntityRaider.class, this.a, this.c, this.c.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityRaider entityraider = (EntityRaider) iterator.next();

                    entityraider.setGoalTarget(entityliving);
                    entityraider.q(true);
                }

                this.c.q(true);
            }

        }

        @Override
        public void e() {
            EntityLiving entityliving = this.c.getGoalTarget();

            if (entityliving != null) {
                if (this.c.h((Entity) entityliving) > (double) this.d) {
                    this.c.getControllerLook().a(entityliving, 30.0F, 30.0F);
                    if (this.c.random.nextInt(50) == 0) {
                        this.c.B();
                    }
                } else {
                    this.c.q(true);
                }

                super.e();
            }
        }
    }

    public class c extends PathfinderGoal {

        private final EntityRaider b;

        c(EntityRaider entityraider) {
            this.b = entityraider;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            Raid raid = this.b.ek();

            return this.b.isAlive() && this.b.getGoalTarget() == null && raid != null && raid.f();
        }

        @Override
        public void c() {
            this.b.u(true);
            super.c();
        }

        @Override
        public void d() {
            this.b.u(false);
            super.d();
        }

        @Override
        public void e() {
            if (!this.b.isSilent() && this.b.random.nextInt(100) == 0) {
                EntityRaider.this.a(EntityRaider.this.dW(), EntityRaider.this.getSoundVolume(), EntityRaider.this.cU());
            }

            if (!this.b.isPassenger() && this.b.random.nextInt(50) == 0) {
                this.b.getControllerJump().jump();
            }

            super.e();
        }
    }

    public class b<T extends EntityRaider> extends PathfinderGoal {

        private final T b;

        public b(EntityRaider entityraider) {
            this.b = entityraider;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            Raid raid = this.b.ek();

            if (this.b.el() && !this.b.ek().a() && this.b.dY() && !ItemStack.matches(this.b.getEquipment(EnumItemSlot.HEAD), Raid.a)) {
                EntityRaider entityraider = raid.b(this.b.em());

                if (entityraider == null || !entityraider.isAlive()) {
                    List<EntityItem> list = this.b.world.a(EntityItem.class, this.b.getBoundingBox().grow(16.0D, 8.0D, 16.0D), EntityRaider.b);

                    if (!list.isEmpty()) {
                        return this.b.getNavigation().a((Entity) list.get(0), 1.149999976158142D);
                    }
                }

                return false;
            } else {
                return false;
            }
        }

        @Override
        public void e() {
            if (this.b.getNavigation().h().a((IPosition) this.b.ch(), 1.414D)) {
                List<EntityItem> list = this.b.world.a(EntityItem.class, this.b.getBoundingBox().grow(4.0D, 4.0D, 4.0D), EntityRaider.b);

                if (!list.isEmpty()) {
                    this.b.a((EntityItem) list.get(0));
                }
            }

        }
    }
}
