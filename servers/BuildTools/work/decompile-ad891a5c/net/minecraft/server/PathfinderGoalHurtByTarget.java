package net.minecraft.server;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class PathfinderGoalHurtByTarget extends PathfinderGoalTarget {

    private static final PathfinderTargetCondition a = (new PathfinderTargetCondition()).c().e();
    private boolean b;
    private int c;
    private final Class<?>[] d;
    private Class<?>[] i;

    public PathfinderGoalHurtByTarget(EntityCreature entitycreature, Class<?>... aclass) {
        super(entitycreature, true);
        this.d = aclass;
        this.a(EnumSet.of(PathfinderGoal.Type.TARGET));
    }

    @Override
    public boolean a() {
        int i = this.e.cs();
        EntityLiving entityliving = this.e.getLastDamager();

        if (i != this.c && entityliving != null) {
            Class[] aclass = this.d;
            int j = aclass.length;

            for (int k = 0; k < j; ++k) {
                Class<?> oclass = aclass[k];

                if (oclass.isAssignableFrom(entityliving.getClass())) {
                    return false;
                }
            }

            return this.a(entityliving, PathfinderGoalHurtByTarget.a);
        } else {
            return false;
        }
    }

    public PathfinderGoalHurtByTarget a(Class<?>... aclass) {
        this.b = true;
        this.i = aclass;
        return this;
    }

    @Override
    public void c() {
        this.e.setGoalTarget(this.e.getLastDamager());
        this.g = this.e.getGoalTarget();
        this.c = this.e.cs();
        this.h = 300;
        if (this.b) {
            this.g();
        }

        super.c();
    }

    protected void g() {
        double d0 = this.k();
        List<EntityInsentient> list = this.e.world.a(this.e.getClass(), (new AxisAlignedBB(this.e.locX, this.e.locY, this.e.locZ, this.e.locX + 1.0D, this.e.locY + 1.0D, this.e.locZ + 1.0D)).grow(d0, 10.0D, d0));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityInsentient entityinsentient = (EntityInsentient) iterator.next();

            if (this.e != entityinsentient && entityinsentient.getGoalTarget() == null && (!(this.e instanceof EntityTameableAnimal) || ((EntityTameableAnimal) this.e).getOwner() == ((EntityTameableAnimal) entityinsentient).getOwner()) && !entityinsentient.r(this.e.getLastDamager())) {
                if (this.i != null) {
                    boolean flag = false;
                    Class[] aclass = this.i;
                    int i = aclass.length;

                    for (int j = 0; j < i; ++j) {
                        Class<?> oclass = aclass[j];

                        if (entityinsentient.getClass() == oclass) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        continue;
                    }
                }

                this.a(entityinsentient, this.e.getLastDamager());
            }
        }

    }

    protected void a(EntityInsentient entityinsentient, EntityLiving entityliving) {
        entityinsentient.setGoalTarget(entityliving);
    }
}
