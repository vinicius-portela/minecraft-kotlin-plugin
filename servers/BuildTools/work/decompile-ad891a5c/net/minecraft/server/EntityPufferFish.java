package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class EntityPufferFish extends EntityFish {

    private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityPufferFish.class, DataWatcherRegistry.b);
    private int c;
    private int d;
    private static final Predicate<EntityLiving> bz = (entityliving) -> {
        return entityliving == null ? false : (entityliving instanceof EntityHuman && (entityliving.t() || ((EntityHuman) entityliving).isCreative()) ? false : entityliving.getMonsterType() != EnumMonsterType.e);
    };

    public EntityPufferFish(EntityTypes<? extends EntityPufferFish> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityPufferFish.b, 0);
    }

    public int getPuffState() {
        return (Integer) this.datawatcher.get(EntityPufferFish.b);
    }

    public void setPuffState(int i) {
        this.datawatcher.set(EntityPufferFish.b, i);
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityPufferFish.b.equals(datawatcherobject)) {
            this.updateSize();
        }

        super.a(datawatcherobject);
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("PuffState", this.getPuffState());
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setPuffState(nbttagcompound.getInt("PuffState"));
    }

    @Override
    protected ItemStack l() {
        return new ItemStack(Items.PUFFERFISH_BUCKET);
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(1, new EntityPufferFish.a(this));
    }

    @Override
    public void tick() {
        if (!this.world.isClientSide && this.isAlive() && this.de()) {
            if (this.c > 0) {
                if (this.getPuffState() == 0) {
                    this.a(SoundEffects.ENTITY_PUFFER_FISH_BLOW_UP, this.getSoundVolume(), this.cU());
                    this.setPuffState(1);
                } else if (this.c > 40 && this.getPuffState() == 1) {
                    this.a(SoundEffects.ENTITY_PUFFER_FISH_BLOW_UP, this.getSoundVolume(), this.cU());
                    this.setPuffState(2);
                }

                ++this.c;
            } else if (this.getPuffState() != 0) {
                if (this.d > 60 && this.getPuffState() == 2) {
                    this.a(SoundEffects.ENTITY_PUFFER_FISH_BLOW_OUT, this.getSoundVolume(), this.cU());
                    this.setPuffState(1);
                } else if (this.d > 100 && this.getPuffState() == 1) {
                    this.a(SoundEffects.ENTITY_PUFFER_FISH_BLOW_OUT, this.getSoundVolume(), this.cU());
                    this.setPuffState(0);
                }

                ++this.d;
            }
        }

        super.tick();
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (this.isAlive() && this.getPuffState() > 0) {
            List<EntityInsentient> list = this.world.a(EntityInsentient.class, this.getBoundingBox().g(0.3D), EntityPufferFish.bz);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityInsentient entityinsentient = (EntityInsentient) iterator.next();

                if (entityinsentient.isAlive()) {
                    this.a(entityinsentient);
                }
            }
        }

    }

    private void a(EntityInsentient entityinsentient) {
        int i = this.getPuffState();

        if (entityinsentient.damageEntity(DamageSource.mobAttack(this), (float) (1 + i))) {
            entityinsentient.addEffect(new MobEffect(MobEffects.POISON, 60 * i, 0));
            this.a(SoundEffects.ENTITY_PUFFER_FISH_STING, 1.0F, 1.0F);
        }

    }

    @Override
    public void pickup(EntityHuman entityhuman) {
        int i = this.getPuffState();

        if (entityhuman instanceof EntityPlayer && i > 0 && entityhuman.damageEntity(DamageSource.mobAttack(this), (float) (1 + i))) {
            ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutGameStateChange(9, 0.0F));
            entityhuman.addEffect(new MobEffect(MobEffects.POISON, 60 * i, 0));
        }

    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_PUFFER_FISH_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_PUFFER_FISH_DEATH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_PUFFER_FISH_HURT;
    }

    @Override
    protected SoundEffect getSoundFlop() {
        return SoundEffects.ENTITY_PUFFER_FISH_FLOP;
    }

    @Override
    public EntitySize a(EntityPose entitypose) {
        return super.a(entitypose).a(q(this.getPuffState()));
    }

    private static float q(int i) {
        switch (i) {
        case 0:
            return 0.5F;
        case 1:
            return 0.7F;
        default:
            return 1.0F;
        }
    }

    static class a extends PathfinderGoal {

        private final EntityPufferFish a;

        public a(EntityPufferFish entitypufferfish) {
            this.a = entitypufferfish;
        }

        @Override
        public boolean a() {
            List<EntityLiving> list = this.a.world.a(EntityLiving.class, this.a.getBoundingBox().g(2.0D), EntityPufferFish.bz);

            return !list.isEmpty();
        }

        @Override
        public void c() {
            this.a.c = 1;
            this.a.d = 0;
        }

        @Override
        public void d() {
            this.a.c = 0;
        }

        @Override
        public boolean b() {
            List<EntityLiving> list = this.a.world.a(EntityLiving.class, this.a.getBoundingBox().g(2.0D), EntityPufferFish.bz);

            return !list.isEmpty();
        }
    }
}
