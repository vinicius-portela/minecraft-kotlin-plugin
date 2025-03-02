package net.minecraft.server;

import java.util.EnumSet;

public class PathfinderGoalOwnerHurtByTarget extends PathfinderGoalTarget {

    private final EntityTameableAnimal a;
    private EntityLiving b;
    private int c;

    public PathfinderGoalOwnerHurtByTarget(EntityTameableAnimal entitytameableanimal) {
        super(entitytameableanimal, false);
        this.a = entitytameableanimal;
        this.a(EnumSet.of(PathfinderGoal.Type.TARGET));
    }

    @Override
    public boolean a() {
        if (!this.a.isTamed()) {
            return false;
        } else {
            EntityLiving entityliving = this.a.getOwner();

            if (entityliving == null) {
                return false;
            } else {
                this.b = entityliving.getLastDamager();
                int i = entityliving.cs();

                return i != this.c && this.a(this.b, PathfinderTargetCondition.a) && this.a.a(this.b, entityliving);
            }
        }
    }

    @Override
    public void c() {
        this.e.setGoalTarget(this.b);
        EntityLiving entityliving = this.a.getOwner();

        if (entityliving != null) {
            this.c = entityliving.cs();
        }

        super.c();
    }
}
