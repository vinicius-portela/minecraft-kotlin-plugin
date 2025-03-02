package net.minecraft.server;

public class BlockPressurePlateWeighted extends BlockPressurePlateAbstract {

    public static final BlockStateInteger POWER = BlockProperties.as;
    private final int weight;

    protected BlockPressurePlateWeighted(int i, Block.Info block_info) {
        super(block_info);
        this.o((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockPressurePlateWeighted.POWER, 0));
        this.weight = i;
    }

    @Override
    protected int b(World world, BlockPosition blockposition) {
        int i = Math.min(world.a(Entity.class, BlockPressurePlateWeighted.c.a(blockposition)).size(), this.weight);

        if (i > 0) {
            float f = (float) Math.min(this.weight, i) / (float) this.weight;

            return MathHelper.f(f * 15.0F);
        } else {
            return 0;
        }
    }

    @Override
    protected void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        generatoraccess.a((EntityHuman) null, blockposition, SoundEffects.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.90000004F);
    }

    @Override
    protected void b(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        generatoraccess.a((EntityHuman) null, blockposition, SoundEffects.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.75F);
    }

    @Override
    protected int getPower(IBlockData iblockdata) {
        return (Integer) iblockdata.get(BlockPressurePlateWeighted.POWER);
    }

    @Override
    protected IBlockData a(IBlockData iblockdata, int i) {
        return (IBlockData) iblockdata.set(BlockPressurePlateWeighted.POWER, i);
    }

    @Override
    public int a(IWorldReader iworldreader) {
        return 10;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockPressurePlateWeighted.POWER);
    }
}
