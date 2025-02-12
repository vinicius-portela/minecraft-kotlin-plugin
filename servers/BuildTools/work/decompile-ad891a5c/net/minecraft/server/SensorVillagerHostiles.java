package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SensorVillagerHostiles extends Sensor<EntityLiving> {

    private static final ImmutableMap<EntityTypes<?>, Float> b = ImmutableMap.builder().put(EntityTypes.ZOMBIE, 8.0F).put(EntityTypes.EVOKER, 12.0F).put(EntityTypes.VINDICATOR, 10.0F).put(EntityTypes.VEX, 8.0F).put(EntityTypes.PILLAGER, 15.0F).put(EntityTypes.ILLUSIONER, 12.0F).put(EntityTypes.RAVAGER, 12.0F).put(EntityTypes.HUSK, 8.0F).build();

    public SensorVillagerHostiles() {}

    @Override
    public Set<MemoryModuleType<?>> a() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_HOSTILE);
    }

    @Override
    protected void a(WorldServer worldserver, EntityLiving entityliving) {
        entityliving.getBehaviorController().a(MemoryModuleType.NEAREST_HOSTILE, this.a(entityliving));
    }

    private Optional<EntityLiving> a(EntityLiving entityliving) {
        return this.b(entityliving).flatMap((list) -> {
            return list.stream().filter(this::c).filter((entityliving1) -> {
                return this.a(entityliving, entityliving1);
            }).min((entityliving1, entityliving2) -> {
                return this.a(entityliving, entityliving1, entityliving2);
            });
        });
    }

    private Optional<List<EntityLiving>> b(EntityLiving entityliving) {
        return entityliving.getBehaviorController().c(MemoryModuleType.VISIBLE_MOBS);
    }

    private int a(EntityLiving entityliving, EntityLiving entityliving1, EntityLiving entityliving2) {
        return (int) entityliving1.h((Entity) entityliving) - (int) entityliving2.h((Entity) entityliving);
    }

    private boolean a(EntityLiving entityliving, EntityLiving entityliving1) {
        float f = (Float) SensorVillagerHostiles.b.get(entityliving1.getEntityType());

        return entityliving1.h((Entity) entityliving) <= (double) (f * f);
    }

    private boolean c(EntityLiving entityliving) {
        return SensorVillagerHostiles.b.containsKey(entityliving.getEntityType());
    }
}
