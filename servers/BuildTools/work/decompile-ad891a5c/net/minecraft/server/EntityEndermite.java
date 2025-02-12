package net.minecraft.server;

public class EntityEndermite extends EntityMonster {

    private static final PathfinderTargetCondition b = (new PathfinderTargetCondition()).a(5.0D).e();
    private int c;
    private boolean d;

    public EntityEndermite(EntityTypes<? extends EntityEndermite> entitytypes, World world) {
        super(entitytypes, world);
        this.f = 3;
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        this.goalSelector.a(3, new PathfinderGoalRandomStrollLand(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[0])).a());
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 0.1F;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(8.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(2.0D);
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_ENDERMITE_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_ENDERMITE_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_ENDERMITE_DEATH;
    }

    @Override
    protected void a(BlockPosition blockposition, IBlockData iblockdata) {
        this.a(SoundEffects.ENTITY_ENDERMITE_STEP, 0.15F, 1.0F);
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.c = nbttagcompound.getInt("Lifetime");
        this.d = nbttagcompound.getBoolean("PlayerSpawned");
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Lifetime", this.c);
        nbttagcompound.setBoolean("PlayerSpawned", this.d);
    }

    @Override
    public void tick() {
        this.aK = this.yaw;
        super.tick();
    }

    @Override
    public void l(float f) {
        this.yaw = f;
        super.l(f);
    }

    @Override
    public double aN() {
        return 0.1D;
    }

    public boolean isPlayerSpawned() {
        return this.d;
    }

    public void setPlayerSpawned(boolean flag) {
        this.d = flag;
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (this.world.isClientSide) {
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(Particles.PORTAL, this.locX + (this.random.nextDouble() - 0.5D) * (double) this.getWidth(), this.locY + this.random.nextDouble() * (double) this.getHeight(), this.locZ + (this.random.nextDouble() - 0.5D) * (double) this.getWidth(), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
            }
        } else {
            if (!this.isPersistent()) {
                ++this.c;
            }

            if (this.c >= 2400) {
                this.die();
            }
        }

    }

    @Override
    protected boolean I_() {
        return true;
    }

    @Override
    public boolean a(GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn) {
        if (super.a(generatoraccess, enummobspawn)) {
            EntityHuman entityhuman = this.world.a(EntityEndermite.b, (EntityLiving) this);

            return entityhuman == null;
        } else {
            return false;
        }
    }

    @Override
    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.ARTHROPOD;
    }
}
