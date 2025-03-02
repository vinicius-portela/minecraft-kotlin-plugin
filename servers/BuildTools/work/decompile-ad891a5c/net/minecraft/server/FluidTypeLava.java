package net.minecraft.server;

import java.util.Random;

public abstract class FluidTypeLava extends FluidTypeFlowing {

    public FluidTypeLava() {}

    @Override
    public FluidType e() {
        return FluidTypes.FLOWING_LAVA;
    }

    @Override
    public FluidType f() {
        return FluidTypes.LAVA;
    }

    @Override
    public Item b() {
        return Items.LAVA_BUCKET;
    }

    @Override
    public void b(World world, BlockPosition blockposition, Fluid fluid, Random random) {
        if (world.getGameRules().getBoolean("doFireTick")) {
            int i = random.nextInt(3);

            if (i > 0) {
                BlockPosition blockposition1 = blockposition;

                for (int j = 0; j < i; ++j) {
                    blockposition1 = blockposition1.b(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
                    if (!world.o(blockposition1)) {
                        return;
                    }

                    IBlockData iblockdata = world.getType(blockposition1);

                    if (iblockdata.isAir()) {
                        if (this.a((IWorldReader) world, blockposition1)) {
                            world.setTypeUpdate(blockposition1, Blocks.FIRE.getBlockData());
                            return;
                        }
                    } else if (iblockdata.getMaterial().isSolid()) {
                        return;
                    }
                }
            } else {
                for (int k = 0; k < 3; ++k) {
                    BlockPosition blockposition2 = blockposition.b(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);

                    if (!world.o(blockposition2)) {
                        return;
                    }

                    if (world.isEmpty(blockposition2.up()) && this.b(world, blockposition2)) {
                        world.setTypeUpdate(blockposition2.up(), Blocks.FIRE.getBlockData());
                    }
                }
            }

        }
    }

    private boolean a(IWorldReader iworldreader, BlockPosition blockposition) {
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];

            if (this.b(iworldreader, blockposition.shift(enumdirection))) {
                return true;
            }
        }

        return false;
    }

    private boolean b(IWorldReader iworldreader, BlockPosition blockposition) {
        return blockposition.getY() >= 0 && blockposition.getY() < 256 && !iworldreader.isLoaded(blockposition) ? false : iworldreader.getType(blockposition).getMaterial().isBurnable();
    }

    @Override
    protected void a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata) {
        this.a(generatoraccess, blockposition);
    }

    @Override
    public int b(IWorldReader iworldreader) {
        return iworldreader.getWorldProvider().isNether() ? 4 : 2;
    }

    @Override
    public IBlockData a(Fluid fluid) {
        return (IBlockData) Blocks.LAVA.getBlockData().set(BlockFluids.LEVEL, d(fluid));
    }

    @Override
    public boolean a(FluidType fluidtype) {
        return fluidtype == FluidTypes.LAVA || fluidtype == FluidTypes.FLOWING_LAVA;
    }

    @Override
    public int c(IWorldReader iworldreader) {
        return iworldreader.getWorldProvider().isNether() ? 1 : 2;
    }

    @Override
    public boolean a(Fluid fluid, IBlockAccess iblockaccess, BlockPosition blockposition, FluidType fluidtype, EnumDirection enumdirection) {
        return fluid.getHeight(iblockaccess, blockposition) >= 0.44444445F && fluidtype.a(TagsFluid.WATER);
    }

    @Override
    public int a(IWorldReader iworldreader) {
        return iworldreader.getWorldProvider().h() ? 10 : 30;
    }

    @Override
    public int a(World world, BlockPosition blockposition, Fluid fluid, Fluid fluid1) {
        int i = this.a((IWorldReader) world);

        if (!fluid.isEmpty() && !fluid1.isEmpty() && !(Boolean) fluid.get(FluidTypeLava.FALLING) && !(Boolean) fluid1.get(FluidTypeLava.FALLING) && fluid1.getHeight(world, blockposition) > fluid.getHeight(world, blockposition) && world.getRandom().nextInt(4) != 0) {
            i *= 4;
        }

        return i;
    }

    private void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        generatoraccess.triggerEffect(1501, blockposition, 0);
    }

    @Override
    protected boolean g() {
        return false;
    }

    @Override
    protected void a(GeneratorAccess generatoraccess, BlockPosition blockposition, IBlockData iblockdata, EnumDirection enumdirection, Fluid fluid) {
        if (enumdirection == EnumDirection.DOWN) {
            Fluid fluid1 = generatoraccess.getFluid(blockposition);

            if (this.a(TagsFluid.LAVA) && fluid1.a(TagsFluid.WATER)) {
                if (iblockdata.getBlock() instanceof BlockFluids) {
                    generatoraccess.setTypeAndData(blockposition, Blocks.STONE.getBlockData(), 3);
                }

                this.a(generatoraccess, blockposition);
                return;
            }
        }

        super.a(generatoraccess, blockposition, iblockdata, enumdirection, fluid);
    }

    @Override
    protected boolean k() {
        return true;
    }

    @Override
    protected float d() {
        return 100.0F;
    }

    public static class a extends FluidTypeLava {

        public a() {}

        @Override
        protected void a(BlockStateList.a<FluidType, Fluid> blockstatelist_a) {
            super.a(blockstatelist_a);
            blockstatelist_a.a(FluidTypeLava.a.LEVEL);
        }

        @Override
        public int c(Fluid fluid) {
            return (Integer) fluid.get(FluidTypeLava.a.LEVEL);
        }

        @Override
        public boolean b(Fluid fluid) {
            return false;
        }
    }

    public static class b extends FluidTypeLava {

        public b() {}

        @Override
        public int c(Fluid fluid) {
            return 8;
        }

        @Override
        public boolean b(Fluid fluid) {
            return true;
        }
    }
}
