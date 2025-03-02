package net.minecraft.server;

public abstract class EntityCreature extends EntityInsentient {

    protected EntityCreature(EntityTypes<? extends EntityCreature> entitytypes, World world) {
        super(entitytypes, world);
    }

    public float f(BlockPosition blockposition) {
        return this.a(blockposition, (IWorldReader) this.world);
    }

    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return 0.0F;
    }

    @Override
    public boolean a(GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn) {
        return super.a(generatoraccess, enummobspawn) && this.a(new BlockPosition(this.locX, this.getBoundingBox().minY, this.locZ), (IWorldReader) generatoraccess) >= 0.0F;
    }

    public boolean dT() {
        return !this.getNavigation().n();
    }

    @Override
    protected void dM() {
        super.dM();
        Entity entity = this.getLeashHolder();

        if (entity != null && entity.world == this.world) {
            this.a(new BlockPosition((int) entity.locX, (int) entity.locY, (int) entity.locZ), 5);
            float f = this.g(entity);

            if (this instanceof EntityTameableAnimal && ((EntityTameableAnimal) this).isSitting()) {
                if (f > 10.0F) {
                    this.unleash(true, true);
                }

                return;
            }

            this.u(f);
            if (f > 10.0F) {
                this.unleash(true, true);
                this.goalSelector.a(PathfinderGoal.Type.MOVE);
            } else if (f > 6.0F) {
                double d0 = (entity.locX - this.locX) / (double) f;
                double d1 = (entity.locY - this.locY) / (double) f;
                double d2 = (entity.locZ - this.locZ) / (double) f;

                this.setMot(this.getMot().add(Math.copySign(d0 * d0 * 0.4D, d0), Math.copySign(d1 * d1 * 0.4D, d1), Math.copySign(d2 * d2 * 0.4D, d2)));
            } else {
                this.goalSelector.b(PathfinderGoal.Type.MOVE);
                float f1 = 2.0F;
                Vec3D vec3d = (new Vec3D(entity.locX - this.locX, entity.locY - this.locY, entity.locZ - this.locZ)).d().a((double) Math.max(f - 2.0F, 0.0F));

                this.getNavigation().a(this.locX + vec3d.x, this.locY + vec3d.y, this.locZ + vec3d.z, this.dU());
            }
        }

    }

    protected double dU() {
        return 1.0D;
    }

    protected void u(float f) {}
}
