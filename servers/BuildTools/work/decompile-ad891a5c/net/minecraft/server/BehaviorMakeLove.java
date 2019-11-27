package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import java.util.Set;

public class BehaviorMakeLove extends Behavior<EntityVillager> {

    private long a;

    public BehaviorMakeLove() {
        super(350, 350);
    }

    @Override
    protected Set<Pair<MemoryModuleType<?>, MemoryStatus>> a() {
        return ImmutableSet.of(Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.VISIBLE_MOBS, MemoryStatus.VALUE_PRESENT));
    }

    protected boolean a(WorldServer worldserver, EntityVillager entityvillager) {
        return this.b(entityvillager);
    }

    protected boolean g(WorldServer worldserver, EntityVillager entityvillager, long i) {
        return i <= this.a && this.b(entityvillager);
    }

    protected void a(WorldServer worldserver, EntityVillager entityvillager, long i) {
        EntityVillager entityvillager1 = this.a(entityvillager);

        BehaviorUtil.a((EntityLiving) entityvillager, (EntityLiving) entityvillager1);
        worldserver.broadcastEntityEffect(entityvillager1, (byte) 18);
        worldserver.broadcastEntityEffect(entityvillager, (byte) 18);
        int j = 275 + entityvillager.getRandom().nextInt(50);

        this.a = i + (long) j;
    }

    protected void d(WorldServer worldserver, EntityVillager entityvillager, long i) {
        EntityVillager entityvillager1 = this.a(entityvillager);

        if (entityvillager.h((Entity) entityvillager1) <= 5.0D) {
            BehaviorUtil.a((EntityLiving) entityvillager, (EntityLiving) entityvillager1);
            if (i >= this.a) {
                Optional<BlockPosition> optional = this.b(worldserver, entityvillager);

                if (!optional.isPresent()) {
                    worldserver.broadcastEntityEffect(entityvillager1, (byte) 13);
                    worldserver.broadcastEntityEffect(entityvillager, (byte) 13);
                    return;
                }

                entityvillager.en();
                entityvillager1.en();
                Optional<EntityVillager> optional1 = this.a(entityvillager, entityvillager1);

                if (optional1.isPresent()) {
                    entityvillager.s(12);
                    entityvillager1.s(12);
                    this.a(worldserver, (EntityVillager) optional1.get(), (BlockPosition) optional.get());
                } else {
                    worldserver.B().b((BlockPosition) optional.get());
                }
            }

            if (entityvillager.getRandom().nextInt(35) == 0) {
                worldserver.broadcastEntityEffect(entityvillager1, (byte) 12);
                worldserver.broadcastEntityEffect(entityvillager, (byte) 12);
            }

        }
    }

    protected void f(WorldServer worldserver, EntityVillager entityvillager, long i) {
        entityvillager.getBehaviorController().b(MemoryModuleType.BREED_TARGET);
    }

    private EntityVillager a(EntityVillager entityvillager) {
        return (EntityVillager) entityvillager.getBehaviorController().c(MemoryModuleType.BREED_TARGET).get();
    }

    private boolean b(EntityVillager entityvillager) {
        BehaviorController<EntityVillager> behaviorcontroller = entityvillager.getBehaviorController();

        if (!behaviorcontroller.c(MemoryModuleType.BREED_TARGET).isPresent()) {
            return false;
        } else {
            EntityVillager entityvillager1 = this.a(entityvillager);

            return BehaviorUtil.a(behaviorcontroller, MemoryModuleType.BREED_TARGET, EntityTypes.VILLAGER) && entityvillager.canBreed() && entityvillager1.canBreed();
        }
    }

    private Optional<BlockPosition> b(WorldServer worldserver, EntityVillager entityvillager) {
        return worldserver.B().a(VillagePlaceType.q.c(), (blockposition) -> {
            return true;
        }, new BlockPosition(entityvillager), 48);
    }

    private Optional<EntityVillager> a(EntityVillager entityvillager, EntityVillager entityvillager1) {
        EntityVillager entityvillager2 = entityvillager.createChild(entityvillager1);

        if (entityvillager2 == null) {
            return Optional.empty();
        } else {
            entityvillager.setAgeRaw(6000);
            entityvillager1.setAgeRaw(6000);
            entityvillager2.setAgeRaw(-24000);
            entityvillager2.setPositionRotation(entityvillager.locX, entityvillager.locY, entityvillager.locZ, 0.0F, 0.0F);
            entityvillager.world.addEntity(entityvillager2);
            entityvillager.world.broadcastEntityEffect(entityvillager2, (byte) 12);
            return Optional.of(entityvillager2);
        }
    }

    private void a(WorldServer worldserver, EntityVillager entityvillager, BlockPosition blockposition) {
        GlobalPos globalpos = GlobalPos.a(worldserver.getWorldProvider().getDimensionManager(), blockposition);

        entityvillager.getBehaviorController().a(MemoryModuleType.HOME, (Object) globalpos);
    }
}
