package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface IEntityAccess {

    List<Entity> getEntities(@Nullable Entity entity, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super Entity> predicate);

    <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super T> predicate);

    List<? extends EntityHuman> getPlayers();

    default List<Entity> getEntities(@Nullable Entity entity, AxisAlignedBB axisalignedbb) {
        return this.getEntities(entity, axisalignedbb, IEntitySelector.f);
    }

    default boolean a(@Nullable Entity entity, VoxelShape voxelshape) {
        return voxelshape.isEmpty() ? true : this.getEntities(entity, voxelshape.getBoundingBox()).stream().filter((entity1) -> {
            return !entity1.dead && entity1.i && (entity == null || !entity1.x(entity));
        }).noneMatch((entity1) -> {
            return VoxelShapes.c(voxelshape, VoxelShapes.a(entity1.getBoundingBox()), OperatorBoolean.AND);
        });
    }

    default <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb) {
        return this.a(oclass, axisalignedbb, IEntitySelector.f);
    }

    default Stream<VoxelShape> a(@Nullable Entity entity, VoxelShape voxelshape, Set<Entity> set) {
        if (voxelshape.isEmpty()) {
            return Stream.empty();
        } else {
            AxisAlignedBB axisalignedbb = voxelshape.getBoundingBox();

            return this.getEntities(entity, axisalignedbb.g(0.25D)).stream().filter((entity1) -> {
                return !set.contains(entity1) && (entity == null || !entity.x(entity1));
            }).flatMap((entity1) -> {
                return Stream.of(entity1.ap(), entity == null ? null : entity.j(entity1)).filter(Objects::nonNull).filter((axisalignedbb1) -> {
                    return axisalignedbb1.c(axisalignedbb);
                }).map(VoxelShapes::a);
            });
        }
    }

    @Nullable
    default EntityHuman a(double d0, double d1, double d2, double d3, @Nullable Predicate<Entity> predicate) {
        double d4 = -1.0D;
        EntityHuman entityhuman = null;
        Iterator iterator = this.getPlayers().iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman1 = (EntityHuman) iterator.next();

            if (predicate == null || predicate.test(entityhuman1)) {
                double d5 = entityhuman1.e(d0, d1, d2);

                if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    @Nullable
    default EntityHuman findNearbyPlayer(Entity entity, double d0) {
        return this.a(entity.locX, entity.locY, entity.locZ, d0, false);
    }

    @Nullable
    default EntityHuman a(double d0, double d1, double d2, double d3, boolean flag) {
        Predicate<Entity> predicate = flag ? IEntitySelector.e : IEntitySelector.f;

        return this.a(d0, d1, d2, d3, predicate);
    }

    @Nullable
    default EntityHuman a(double d0, double d1, double d2) {
        double d3 = -1.0D;
        EntityHuman entityhuman = null;
        Iterator iterator = this.getPlayers().iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman1 = (EntityHuman) iterator.next();

            if (IEntitySelector.f.test(entityhuman1)) {
                double d4 = entityhuman1.e(d0, entityhuman1.locY, d1);

                if ((d2 < 0.0D || d4 < d2 * d2) && (d3 == -1.0D || d4 < d3)) {
                    d3 = d4;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    default boolean isPlayerNearby(double d0, double d1, double d2, double d3) {
        Iterator iterator = this.getPlayers().iterator();

        double d4;

        do {
            EntityHuman entityhuman;

            do {
                do {
                    if (!iterator.hasNext()) {
                        return false;
                    }

                    entityhuman = (EntityHuman) iterator.next();
                } while (!IEntitySelector.f.test(entityhuman));
            } while (!IEntitySelector.b.test(entityhuman));

            d4 = entityhuman.e(d0, d1, d2);
        } while (d3 >= 0.0D && d4 >= d3 * d3);

        return true;
    }

    @Nullable
    default EntityHuman a(PathfinderTargetCondition pathfindertargetcondition, EntityLiving entityliving) {
        return (EntityHuman) this.a(this.getPlayers(), pathfindertargetcondition, entityliving, entityliving.locX, entityliving.locY, entityliving.locZ);
    }

    @Nullable
    default EntityHuman a(PathfinderTargetCondition pathfindertargetcondition, EntityLiving entityliving, double d0, double d1, double d2) {
        return (EntityHuman) this.a(this.getPlayers(), pathfindertargetcondition, entityliving, d0, d1, d2);
    }

    @Nullable
    default EntityHuman a(PathfinderTargetCondition pathfindertargetcondition, double d0, double d1, double d2) {
        return (EntityHuman) this.a(this.getPlayers(), pathfindertargetcondition, (EntityLiving) null, d0, d1, d2);
    }

    @Nullable
    default <T extends EntityLiving> T a(Class<? extends T> oclass, PathfinderTargetCondition pathfindertargetcondition, @Nullable EntityLiving entityliving, double d0, double d1, double d2, AxisAlignedBB axisalignedbb) {
        return this.a(this.a(oclass, axisalignedbb, (Predicate) null), pathfindertargetcondition, entityliving, d0, d1, d2);
    }

    @Nullable
    default <T extends EntityLiving> T a(List<? extends T> list, PathfinderTargetCondition pathfindertargetcondition, @Nullable EntityLiving entityliving, double d0, double d1, double d2) {
        double d3 = -1.0D;
        T t0 = null;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            T t1 = (EntityLiving) iterator.next();

            if (pathfindertargetcondition.a(entityliving, t1)) {
                double d4 = t1.e(d0, d1, d2);

                if (d3 == -1.0D || d4 < d3) {
                    d3 = d4;
                    t0 = t1;
                }
            }
        }

        return t0;
    }

    default List<EntityHuman> a(PathfinderTargetCondition pathfindertargetcondition, EntityLiving entityliving, AxisAlignedBB axisalignedbb) {
        List<EntityHuman> list = Lists.newArrayList();
        Iterator iterator = this.getPlayers().iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            if (axisalignedbb.e(entityhuman.locX, entityhuman.locY, entityhuman.locZ) && pathfindertargetcondition.a(entityliving, entityhuman)) {
                list.add(entityhuman);
            }
        }

        return list;
    }

    default <T extends EntityLiving> List<T> a(Class<? extends T> oclass, PathfinderTargetCondition pathfindertargetcondition, EntityLiving entityliving, AxisAlignedBB axisalignedbb) {
        List<T> list = this.a(oclass, axisalignedbb, (Predicate) null);
        List<T> list1 = Lists.newArrayList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            T t0 = (EntityLiving) iterator.next();

            if (pathfindertargetcondition.a(entityliving, t0)) {
                list1.add(t0);
            }
        }

        return list1;
    }

    @Nullable
    default EntityHuman b(UUID uuid) {
        for (int i = 0; i < this.getPlayers().size(); ++i) {
            EntityHuman entityhuman = (EntityHuman) this.getPlayers().get(i);

            if (uuid.equals(entityhuman.getUniqueID())) {
                return entityhuman;
            }
        }

        return null;
    }
}
