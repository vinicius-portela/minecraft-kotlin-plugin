package net.minecraft.server;

import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockCommand extends BlockTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final BlockStateDirection a = BlockDirectional.FACING;
    public static final BlockStateBoolean b = BlockProperties.c;

    public BlockCommand(Block.Info block_info) {
        super(block_info);
        this.o((IBlockData) ((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockCommand.a, EnumDirection.NORTH)).set(BlockCommand.b, false));
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        TileEntityCommand tileentitycommand = new TileEntityCommand();

        tileentitycommand.b(this == Blocks.CHAIN_COMMAND_BLOCK);
        return tileentitycommand;
    }

    @Override
    public void doPhysics(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1, boolean flag) {
        if (!world.isClientSide) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityCommand) {
                TileEntityCommand tileentitycommand = (TileEntityCommand) tileentity;
                boolean flag1 = world.isBlockIndirectlyPowered(blockposition);
                boolean flag2 = tileentitycommand.d();

                tileentitycommand.a(flag1);
                if (!flag2 && !tileentitycommand.f() && tileentitycommand.t() != TileEntityCommand.Type.SEQUENCE) {
                    if (flag1) {
                        tileentitycommand.r();
                        world.getBlockTickList().a(blockposition, this, this.a((IWorldReader) world));
                    }

                }
            }
        }
    }

    @Override
    public void tick(IBlockData iblockdata, World world, BlockPosition blockposition, Random random) {
        if (!world.isClientSide) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityCommand) {
                TileEntityCommand tileentitycommand = (TileEntityCommand) tileentity;
                CommandBlockListenerAbstract commandblocklistenerabstract = tileentitycommand.getCommandBlock();
                boolean flag = !UtilColor.b(commandblocklistenerabstract.getCommand());
                TileEntityCommand.Type tileentitycommand_type = tileentitycommand.t();
                boolean flag1 = tileentitycommand.g();

                if (tileentitycommand_type == TileEntityCommand.Type.AUTO) {
                    tileentitycommand.r();
                    if (flag1) {
                        this.a(iblockdata, world, blockposition, commandblocklistenerabstract, flag);
                    } else if (tileentitycommand.u()) {
                        commandblocklistenerabstract.a(0);
                    }

                    if (tileentitycommand.d() || tileentitycommand.f()) {
                        world.getBlockTickList().a(blockposition, this, this.a((IWorldReader) world));
                    }
                } else if (tileentitycommand_type == TileEntityCommand.Type.REDSTONE) {
                    if (flag1) {
                        this.a(iblockdata, world, blockposition, commandblocklistenerabstract, flag);
                    } else if (tileentitycommand.u()) {
                        commandblocklistenerabstract.a(0);
                    }
                }

                world.updateAdjacentComparators(blockposition, this);
            }

        }
    }

    private void a(IBlockData iblockdata, World world, BlockPosition blockposition, CommandBlockListenerAbstract commandblocklistenerabstract, boolean flag) {
        if (flag) {
            commandblocklistenerabstract.a(world);
        } else {
            commandblocklistenerabstract.a(0);
        }

        a(world, blockposition, (EnumDirection) iblockdata.get(BlockCommand.a));
    }

    @Override
    public int a(IWorldReader iworldreader) {
        return 1;
    }

    @Override
    public boolean interact(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman, EnumHand enumhand, MovingObjectPositionBlock movingobjectpositionblock) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityCommand && entityhuman.isCreativeAndOp()) {
            entityhuman.a((TileEntityCommand) tileentity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    @Override
    public int a(IBlockData iblockdata, World world, BlockPosition blockposition) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        return tileentity instanceof TileEntityCommand ? ((TileEntityCommand) tileentity).getCommandBlock().i() : 0;
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityCommand) {
            TileEntityCommand tileentitycommand = (TileEntityCommand) tileentity;
            CommandBlockListenerAbstract commandblocklistenerabstract = tileentitycommand.getCommandBlock();

            if (itemstack.hasName()) {
                commandblocklistenerabstract.setName(itemstack.getName());
            }

            if (!world.isClientSide) {
                if (itemstack.b("BlockEntityTag") == null) {
                    commandblocklistenerabstract.a(world.getGameRules().getBoolean("sendCommandFeedback"));
                    tileentitycommand.b(this == Blocks.CHAIN_COMMAND_BLOCK);
                }

                if (tileentitycommand.t() == TileEntityCommand.Type.SEQUENCE) {
                    boolean flag = world.isBlockIndirectlyPowered(blockposition);

                    tileentitycommand.a(flag);
                }
            }

        }
    }

    @Override
    public EnumRenderType c(IBlockData iblockdata) {
        return EnumRenderType.MODEL;
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return (IBlockData) iblockdata.set(BlockCommand.a, enumblockrotation.a((EnumDirection) iblockdata.get(BlockCommand.a)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a((EnumDirection) iblockdata.get(BlockCommand.a)));
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockCommand.a, BlockCommand.b);
    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        return (IBlockData) this.getBlockData().set(BlockCommand.a, blockactioncontext.d().opposite());
    }

    private static void a(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition(blockposition);
        GameRules gamerules = world.getGameRules();

        IBlockData iblockdata;
        int i;

        for (i = gamerules.c("maxCommandChainLength"); i-- > 0; enumdirection = (EnumDirection) iblockdata.get(BlockCommand.a)) {
            blockposition_mutableblockposition.c(enumdirection);
            iblockdata = world.getType(blockposition_mutableblockposition);
            Block block = iblockdata.getBlock();

            if (block != Blocks.CHAIN_COMMAND_BLOCK) {
                break;
            }

            TileEntity tileentity = world.getTileEntity(blockposition_mutableblockposition);

            if (!(tileentity instanceof TileEntityCommand)) {
                break;
            }

            TileEntityCommand tileentitycommand = (TileEntityCommand) tileentity;

            if (tileentitycommand.t() != TileEntityCommand.Type.SEQUENCE) {
                break;
            }

            if (tileentitycommand.d() || tileentitycommand.f()) {
                CommandBlockListenerAbstract commandblocklistenerabstract = tileentitycommand.getCommandBlock();

                if (tileentitycommand.r()) {
                    if (!commandblocklistenerabstract.a(world)) {
                        break;
                    }

                    world.updateAdjacentComparators(blockposition_mutableblockposition, block);
                } else if (tileentitycommand.u()) {
                    commandblocklistenerabstract.a(0);
                }
            }
        }

        if (i <= 0) {
            int j = Math.max(gamerules.c("maxCommandChainLength"), 0);

            BlockCommand.LOGGER.warn("Command Block chain tried to execute more than {} steps!", j);
        }

    }
}
