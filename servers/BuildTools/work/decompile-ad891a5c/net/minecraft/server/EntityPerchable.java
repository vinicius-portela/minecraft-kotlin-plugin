package net.minecraft.server;

public abstract class EntityPerchable extends EntityTameableAnimal {

    private int bD;

    protected EntityPerchable(EntityTypes<? extends EntityPerchable> entitytypes, World world) {
        super(entitytypes, world);
    }

    public boolean d(EntityPlayer entityplayer) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setString("id", this.getSaveID());
        this.save(nbttagcompound);
        if (entityplayer.g(nbttagcompound)) {
            this.die();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        ++this.bD;
        super.tick();
    }

    public boolean eh() {
        return this.bD > 100;
    }
}
