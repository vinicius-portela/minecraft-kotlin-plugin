package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import java.util.Set;

public class BehaviorCooldown extends Behavior<EntityLiving> {

    public BehaviorCooldown() {}

    @Override
    protected Set<Pair<MemoryModuleType<?>, MemoryStatus>> a() {
        return ImmutableSet.of();
    }

    @Override
    protected void a(WorldServer worldserver, EntityLiving entityliving, long i) {
        boolean flag = BehaviorPanic.b(entityliving) || BehaviorPanic.a(entityliving) || a(entityliving);

        if (!flag) {
            entityliving.getBehaviorController().b(MemoryModuleType.HURT_BY);
            entityliving.getBehaviorController().b(MemoryModuleType.HURT_BY_ENTITY);
            entityliving.getBehaviorController().a(worldserver.getDayTime(), worldserver.getTime());
        }

    }

    private static boolean a(EntityLiving entityliving) {
        return entityliving.getBehaviorController().c(MemoryModuleType.HURT_BY_ENTITY).filter((entityliving1) -> {
            return entityliving1.h((Entity) entityliving) <= 36.0D;
        }).isPresent();
    }
}
