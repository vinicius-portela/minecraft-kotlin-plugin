package net.minecraft.server;

import javax.annotation.Nullable;

public class BlockDoor extends Block {

    public static final BlockStateDirection FACING = BlockFacingHorizontal.FACING;
    public static final BlockStateBoolean OPEN = BlockProperties.u;
    public static final BlockStateEnum<BlockPropertyDoorHinge> HINGE = BlockProperties.az;
    public static final BlockStateBoolean POWERED = BlockProperties.w;
    public static final BlockStateEnum<BlockPropertyDoubleBlockHalf> HALF = BlockProperties.U;
    protected static final VoxelShape f = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape g = Block.a(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape h = Block.a(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape i = Block.a(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);

    protected BlockDoor(Block.Info block_info) {
        super(block_info);
        this.o((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockDoor.FACING, EnumDirection.NORTH)).set(BlockDoor.OPEN, false)).set(BlockDoor.HINGE, BlockPropertyDoorHinge.LEFT)).set(BlockDoor.POWERED, false)).set(BlockDoor.HALF, BlockPropertyDoubleBlockHalf.LOWER));
    }

    @Override
    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        EnumDirection enumdirection = (EnumDirection) iblockdata.get(BlockDoor.FACING);
        boolean flag = !(Boolean) iblockdata.get(BlockDoor.OPEN);
        boolean flag1 = iblockdata.get(BlockDoor.HINGE) == BlockPropertyDoorHinge.RIGHT;

        switch (enumdirection) {
        case EAST:
        default:
            return flag ? BlockDoor.i : (flag1 ? BlockDoor.g : BlockDoor.f);
        case SOUTH:
            return flag ? BlockDoor.f : (flag1 ? BlockDoor.i : BlockDoor.h);
        case WEST:
            return flag ? BlockDoor.h : (flag1 ? BlockDoor.f : BlockDoor.g);
        case NORTH:
            return flag ? BlockDoor.g : (flag1 ? BlockDoor.h : BlockDoor.i);
        }
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        BlockPropertyDoubleBlockHalf blockpropertydoubleblockhalf = (BlockPropertyDoubleBlockHalf) iblockdata.get(BlockDoor.HALF);

        return enumdirection.k() == EnumDirection.EnumAxis.Y && blockpropertydoubleblockhalf == BlockPropertyDoubleBlockHalf.LOWER == (enumdirection == EnumDirection.UP) ? (iblockdata1.getBlock() == this && iblockdata1.get(BlockDoor.HALF) != blockpropertydoubleblockhalf ? (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockDoor.FACING, iblockdata1.get(BlockDoor.FACING))).set(BlockDoor.OPEN, iblockdata1.get(BlockDoor.OPEN))).set(BlockDoor.HINGE, iblockdata1.get(BlockDoor.HINGE))).set(BlockDoor.POWERED, iblockdata1.get(BlockDoor.POWERED)) : Blocks.AIR.getBlockData()) : (blockpropertydoubleblockhalf == BlockPropertyDoubleBlockHalf.LOWER && enumdirection == EnumDirection.DOWN && !iblockdata.canPlace(generatoraccess, blockposition) ? Blocks.AIR.getBlockData() : super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1));
    }

    @Override
    public void a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        super.a(world, entityhuman, blockposition, Blocks.AIR.getBlockData(), tileentity, itemstack);
    }

    @Override
    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman) {
        BlockPropertyDoubleBlockHalf blockpropertydoubleblockhalf = (BlockPropertyDoubleBlockHalf) iblockdata.get(BlockDoor.HALF);
        BlockPosition blockposition1 = blockpropertydoubleblockhalf == BlockPropertyDoubleBlockHalf.LOWER ? blockposition.up() : blockposition.down();
        IBlockData iblockdata1 = world.getType(blockposition1);

        if (iblockdata1.getBlock() == this && iblockdata1.get(BlockDoor.HALF) != blockpropertydoubleblockhalf) {
            world.setTypeAndData(blockposition1, Blocks.AIR.getBlockData(), 35);
            world.a(entityhuman, 2001, blockposition1, Block.getCombinedId(iblockdata1));
            ItemStack itemstack = entityhuman.getItemInMainHand();

            if (!world.isClientSide && !entityhuman.isCreative()) {
                Block.dropItems(iblockdata, world, blockposition, (TileEntity) null, entityhuman, itemstack);
                Block.dropItems(iblockdata1, world, blockposition1, (TileEntity) null, entityhuman, itemstack);
            }
        }

        super.a(world, blockposition, iblockdata, entityhuman);
    }

    @Override
    public boolean a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, PathMode pathmode) {
        switch (pathmode) {
        case LAND:
            return (Boolean) iblockdata.get(BlockDoor.OPEN);
        case WATER:
            return false;
        case AIR:
            return (Boolean) iblockdata.get(BlockDoor.OPEN);
        default:
            return false;
        }
    }

    private int d() {
        return this.material == Material.ORE ? 1011 : 1012;
    }

    private int e() {
        return this.material == Material.ORE ? 1005 : 1006;
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        BlockPosition blockposition = blockactioncontext.getClickPosition();

        if (blockposition.getY() < 255 && blockactioncontext.getWorld().getType(blockposition.up()).a(blockactioncontext)) {
            World world = blockactioncontext.getWorld();
            boolean flag = world.isBlockIndirectlyPowered(blockposition) || world.isBlockIndirectlyPowered(blockposition.up());

            return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.getBlockData().set(BlockDoor.FACING, blockactioncontext.f())).set(BlockDoor.HINGE, this.b(blockactioncontext))).set(BlockDoor.POWERED, flag)).set(BlockDoor.OPEN, flag)).set(BlockDoor.HALF, BlockPropertyDoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        world.setTypeAndData(blockposition.up(), (IBlockData) iblockdata.set(BlockDoor.HALF, BlockPropertyDoubleBlockHalf.UPPER), 3);
    }

    private BlockPropertyDoorHinge b(BlockActionContext blockactioncontext) {
        World world = blockactioncontext.getWorld();
        BlockPosition blockposition = blockactioncontext.getClickPosition();
        EnumDirection enumdirection = blockactioncontext.f();
        BlockPosition blockposition1 = blockposition.up();
        EnumDirection enumdirection1 = enumdirection.f();
        BlockPosition blockposition2 = blockposition.shift(enumdirection1);
        IBlockData iblockdata = world.getType(blockposition2);
        BlockPosition blockposition3 = blockposition1.shift(enumdirection1);
        IBlockData iblockdata1 = world.getType(blockposition3);
        EnumDirection enumdirection2 = enumdirection.e();
        BlockPosition blockposition4 = blockposition.shift(enumdirection2);
        IBlockData iblockdata2 = world.getType(blockposition4);
        BlockPosition blockposition5 = blockposition1.shift(enumdirection2);
        IBlockData iblockdata3 = world.getType(blockposition5);
        int i = (a(iblockdata.getCollisionShape(world, blockposition2)) ? -1 : 0) + (a(iblockdata1.getCollisionShape(world, blockposition3)) ? -1 : 0) + (a(iblockdata2.getCollisionShape(world, blockposition4)) ? 1 : 0) + (a(iblockdata3.getCollisionShape(world, blockposition5)) ? 1 : 0);
        boolean flag = iblockdata.getBlock() == this && iblockdata.get(BlockDoor.HALF) == BlockPropertyDoubleBlockHalf.LOWER;
        boolean flag1 = iblockdata2.getBlock() == this && iblockdata2.get(BlockDoor.HALF) == BlockPropertyDoubleBlockHalf.LOWER;

        if ((!flag || flag1) && i <= 0) {
            if ((!flag1 || flag) && i >= 0) {
                int j = enumdirection.getAdjacentX();
                int k = enumdirection.getAdjacentZ();
                Vec3D vec3d = blockactioncontext.j();
                double d0 = vec3d.x - (double) blockposition.getX();
                double d1 = vec3d.z - (double) blockposition.getZ();

                return (j >= 0 || d1 >= 0.5D) && (j <= 0 || d1 <= 0.5D) && (k >= 0 || d0 <= 0.5D) && (k <= 0 || d0 >= 0.5D) ? BlockPropertyDoorHinge.LEFT : BlockPropertyDoorHinge.RIGHT;
            } else {
                return BlockPropertyDoorHinge.LEFT;
            }
        } else {
            return BlockPropertyDoorHinge.RIGHT;
        }
    }

    @Override
    public boolean interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        if (this.material == Material.ORE) {
            return false;
        } else {
            iblockdata = (IBlockData) iblockdata.a((IBlockState) BlockDoor.OPEN);
            world.setTypeAndData(blockposition, iblockdata, 10);
            world.a(entityhuman, (Boolean) iblockdata.get(BlockDoor.OPEN) ? this.e() : this.d(), blockposition, 0);
            return true;
        }
    }

    public void setDoor(World world, BlockPosition blockposition, boolean flag) {
        IBlockData iblockdata = world.getType(blockposition);

        if (iblockdata.getBlock() == this && (Boolean) iblockdata.get(BlockDoor.OPEN) != flag) {
            world.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockDoor.OPEN, flag), 10);
            this.b(world, blockposition, flag);
        }
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        boolean flag1 = world.isBlockIndirectlyPowered(blockposition) || world.isBlockIndirectlyPowered(blockposition.shift(iblockdata.get(BlockDoor.HALF) == BlockPropertyDoubleBlockHalf.LOWER ? EnumDirection.UP : EnumDirection.DOWN));

        if (block != this && flag1 != (Boolean) iblockdata.get(BlockDoor.POWERED)) {
            if (flag1 != (Boolean) iblockdata.get(BlockDoor.OPEN)) {
                this.b(world, blockposition, flag1);
            }

            world.setTypeAndData(blockposition, (IBlockData) ((IBlockData) iblockdata.set(BlockDoor.POWERED, flag1)).set(BlockDoor.OPEN, flag1), 2);
        }

    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.down();
        IBlockData iblockdata1 = iworldreader.getType(blockposition1);

        return iblockdata.get(BlockDoor.HALF) == BlockPropertyDoubleBlockHalf.LOWER ? Block.d(iblockdata1, iworldreader, blockposition1, EnumDirection.UP) : iblockdata1.getBlock() == this;
    }

    private void b(World world, BlockPosition blockposition, boolean flag) {
        world.a((EntityHuman) null, flag ? this.e() : this.d(), blockposition, 0);
    }

    @Override
    public EnumPistonReaction getPushReaction(IBlockData iblockdata) {
        return EnumPistonReaction.DESTROY;
    }

    @Override
    public TextureType c() {
        return TextureType.CUTOUT;
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockDoor.FACING, enumblockrotation.a((EnumDirection) iblockdata.get(BlockDoor.FACING)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return enumblockmirror == EnumBlockMirror.NONE ? iblockdata : (IBlockData) iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockDoor.FACING))).a((IBlockState) BlockDoor.HINGE);
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockDoor.HALF, BlockDoor.FACING, BlockDoor.OPEN, BlockDoor.HINGE, BlockDoor.POWERED);
    }
}
