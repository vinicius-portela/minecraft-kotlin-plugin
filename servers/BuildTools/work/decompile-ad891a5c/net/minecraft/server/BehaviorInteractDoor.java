package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BehaviorInteractDoor extends Behavior<EntityLiving> {

    public BehaviorInteractDoor() {}

    @Override
    protected Set<Pair<MemoryModuleType<?>, MemoryStatus>> a() {
        return ImmutableSet.of(Pair.of(MemoryModuleType.PATH, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.INTERACTABLE_DOORS, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected void a(WorldServer worldserver, EntityLiving entityliving, long i) {
        BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();
        PathEntity pathentity = (PathEntity) behaviorcontroller.c(MemoryModuleType.PATH).get();
        List<GlobalPos> list = (List) behaviorcontroller.c(MemoryModuleType.INTERACTABLE_DOORS).get();
        List<BlockPosition> list1 = (List) pathentity.d().stream().map((pathpoint) -> {
            return new BlockPosition(pathpoint.a, pathpoint.b, pathpoint.c);
        }).collect(Collectors.toList());
        Set<BlockPosition> set = this.a(worldserver, list, list1);
        int j = pathentity.f() - 1;

        this.a(worldserver, list1, set, j);
    }

    private Set<BlockPosition> a(WorldServer worldserver, List<GlobalPos> list, List<BlockPosition> list1) {
        Stream stream = list.stream().filter((globalpos) -> {
            return globalpos.a() == worldserver.getWorldProvider().getDimensionManager();
        }).map(GlobalPos::b);

        list1.getClass();
        return (Set) stream.filter(list1::contains).collect(Collectors.toSet());
    }

    private void a(WorldServer worldserver, List<BlockPosition> list, Set<BlockPosition> set, int i) {
        set.forEach((blockposition) -> {
            int j = list.indexOf(blockposition);
            IBlockData iblockdata = worldserver.getType(blockposition);
            Block block = iblockdata.getBlock();

            if (TagsBlock.WOODEN_DOORS.isTagged(block) && block instanceof BlockDoor) {
                ((BlockDoor) block).setDoor(worldserver, blockposition, j >= i);
            }

        });
    }
}
