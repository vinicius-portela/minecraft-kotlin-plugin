package net.minecraft.server;

import java.util.Random;

public class BlockBambooSapling extends Block implements IBlockFragilePlantElement {

    protected static final VoxelShape a = Block.a(4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D);

    public BlockBambooSapling(Block.Info block_info) {
        super(block_info);
    }

    @Override
    public Block.EnumRandomOffset S_() {
        return Block.EnumRandomOffset.XZ;
    }

    @Override
    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        Vec3D vec3d = iblockdata.l(iblockaccess, blockposition);

        return BlockBambooSapling.a.a(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public void tick(IBlockData iblockdata, World world, BlockPosition blockposition, Random random) {
        if (random.nextInt(3) == 0 && world.isEmpty(blockposition.up()) && world.getLightLevel(blockposition.up(), 0) >= 9) {
            this.a(world, blockposition);
        }

    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        return iworldreader.getType(blockposition.down()).a(TagsBlock.BAMBOO_PLANTABLE_ON);
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (!iblockdata.canPlace(generatoraccess, blockposition)) {
            return Blocks.AIR.getBlockData();
        } else {
            if (enumdirection == EnumDirection.UP && iblockdata1.getBlock() == Blocks.BAMBOO) {
                generatoraccess.setTypeAndData(blockposition, Blocks.BAMBOO.getBlockData(), 2);
            }

            return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
        }
    }

    @Override
    public boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        return iblockaccess.getType(blockposition.up()).isAir();
    }

    @Override
    public boolean a(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        return true;
    }

    @Override
    public void b(World world, Random random, BlockPosition blockposition, IBlockData iblockdata) {
        this.a(world, blockposition);
    }

    @Override
    public float getDamage(IBlockData iblockdata, EntityHuman entityhuman, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return entityhuman.getItemInMainHand().getItem() instanceof ItemSword ? 1.0F : super.getDamage(iblockdata, entityhuman, iblockaccess, blockposition);
    }

    @Override
    public TextureType c() {
        return TextureType.CUTOUT;
    }

    protected void a(World world, BlockPosition blockposition) {
        world.setTypeAndData(blockposition.up(), (IBlockData) Blocks.BAMBOO.getBlockData().set(BlockBamboo.e, BlockPropertyBambooSize.SMALL), 3);
    }
}
