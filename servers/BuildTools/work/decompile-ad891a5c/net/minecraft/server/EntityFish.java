package net.minecraft.server;

import java.util.function.Predicate;

public abstract class EntityFish extends EntityWaterAnimal {

    private static final DataWatcherObject<Boolean> FROM_BUCKET = DataWatcher.a(EntityFish.class, DataWatcherRegistry.i);

    public EntityFish(EntityTypes<? extends EntityFish> entitytypes, World world) {
        super(entitytypes, world);
        this.moveController = new EntityFish.a(this);
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return entitysize.height * 0.65F;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(3.0D);
    }

    @Override
    public boolean I() {
        return this.isFromBucket();
    }

    @Override
    public boolean a(GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn) {
        BlockPosition blockposition = new BlockPosition(this);

        return generatoraccess.getType(blockposition).getBlock() == Blocks.WATER && generatoraccess.getType(blockposition.up()).getBlock() == Blocks.WATER ? super.a(generatoraccess, enummobspawn) : false;
    }

    @Override
    public boolean isTypeNotPersistent(double d0) {
        return !this.isFromBucket() && !this.hasCustomName();
    }

    @Override
    public int dC() {
        return 8;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityFish.FROM_BUCKET, false);
    }

    public boolean isFromBucket() {
        return (Boolean) this.datawatcher.get(EntityFish.FROM_BUCKET);
    }

    public void setFromBucket(boolean flag) {
        this.datawatcher.set(EntityFish.FROM_BUCKET, flag);
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("FromBucket", this.isFromBucket());
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setFromBucket(nbttagcompound.getBoolean("FromBucket"));
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new PathfinderGoalPanic(this, 1.25D));
        PathfinderGoalSelector pathfindergoalselector = this.goalSelector;
        Predicate predicate = IEntitySelector.f;

        predicate.getClass();
        pathfindergoalselector.a(2, new PathfinderGoalAvoidTarget<>(this, EntityHuman.class, 8.0F, 1.6D, 1.4D, predicate::test));
        this.goalSelector.a(4, new EntityFish.b(this));
    }

    @Override
    protected NavigationAbstract b(World world) {
        return new NavigationGuardian(this, world);
    }

    @Override
    public void e(Vec3D vec3d) {
        if (this.de() && this.isInWater()) {
            this.a(0.01F, vec3d);
            this.move(EnumMoveType.SELF, this.getMot());
            this.setMot(this.getMot().a(0.9D));
            if (this.getGoalTarget() == null) {
                this.setMot(this.getMot().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.e(vec3d);
        }

    }

    @Override
    public void movementTick() {
        if (!this.isInWater() && this.onGround && this.y) {
            this.setMot(this.getMot().add((double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), 0.4000000059604645D, (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
            this.onGround = false;
            this.impulse = true;
            this.a(this.getSoundFlop(), this.getSoundVolume(), this.cU());
        }

        super.movementTick();
    }

    @Override
    protected boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.WATER_BUCKET && this.isAlive()) {
            this.a(SoundEffects.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
            itemstack.subtract(1);
            ItemStack itemstack1 = this.l();

            this.i(itemstack1);
            if (!this.world.isClientSide) {
                CriterionTriggers.j.a((EntityPlayer) entityhuman, itemstack1);
            }

            if (itemstack.isEmpty()) {
                entityhuman.a(enumhand, itemstack1);
            } else if (!entityhuman.inventory.pickup(itemstack1)) {
                entityhuman.drop(itemstack1, false);
            }

            this.die();
            return true;
        } else {
            return super.a(entityhuman, enumhand);
        }
    }

    protected void i(ItemStack itemstack) {
        if (this.hasCustomName()) {
            itemstack.a(this.getCustomName());
        }

    }

    protected abstract ItemStack l();

    protected boolean dV() {
        return true;
    }

    protected abstract SoundEffect getSoundFlop();

    @Override
    protected SoundEffect getSoundSwim() {
        return SoundEffects.ENTITY_FISH_SWIM;
    }

    static class a extends ControllerMove {

        private final EntityFish i;

        a(EntityFish entityfish) {
            super(entityfish);
            this.i = entityfish;
        }

        @Override
        public void a() {
            if (this.i.a(TagsFluid.WATER)) {
                this.i.setMot(this.i.getMot().add(0.0D, 0.005D, 0.0D));
            }

            if (this.h == ControllerMove.Operation.MOVE_TO && !this.i.getNavigation().n()) {
                double d0 = this.b - this.i.locX;
                double d1 = this.c - this.i.locY;
                double d2 = this.d - this.i.locZ;
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                d1 /= d3;
                float f = (float) (MathHelper.d(d2, d0) * 57.2957763671875D) - 90.0F;

                this.i.yaw = this.a(this.i.yaw, f, 90.0F);
                this.i.aK = this.i.yaw;
                float f1 = (float) (this.e * this.i.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());

                this.i.o(MathHelper.g(0.125F, this.i.da(), f1));
                this.i.setMot(this.i.getMot().add(0.0D, (double) this.i.da() * d1 * 0.1D, 0.0D));
            } else {
                this.i.o(0.0F);
            }
        }
    }

    static class b extends PathfinderGoalRandomSwim {

        private final EntityFish h;

        public b(EntityFish entityfish) {
            super(entityfish, 1.0D, 40);
            this.h = entityfish;
        }

        @Override
        public boolean a() {
            return this.h.dV() && super.a();
        }
    }
}
