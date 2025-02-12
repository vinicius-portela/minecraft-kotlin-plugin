package net.minecraft.server;

import javax.annotation.Nullable;

public class TileEntityCommand extends TileEntity {

    private boolean a;
    private boolean b;
    private boolean c;
    private boolean g;
    private final CommandBlockListenerAbstract h = new CommandBlockListenerAbstract() {
        @Override
        public void setCommand(String s) {
            super.setCommand(s);
            TileEntityCommand.this.update();
        }

        @Override
        public WorldServer d() {
            return (WorldServer) TileEntityCommand.this.world;
        }

        @Override
        public void e() {
            IBlockData iblockdata = TileEntityCommand.this.world.getType(TileEntityCommand.this.position);

            this.d().notify(TileEntityCommand.this.position, iblockdata, iblockdata, 3);
        }

        @Override
        public CommandListenerWrapper getWrapper() {
            return new CommandListenerWrapper(this, new Vec3D((double) TileEntityCommand.this.position.getX() + 0.5D, (double) TileEntityCommand.this.position.getY() + 0.5D, (double) TileEntityCommand.this.position.getZ() + 0.5D), Vec2F.a, this.d(), 2, this.getName().getString(), this.getName(), this.d().getMinecraftServer(), (Entity) null);
        }
    };

    public TileEntityCommand() {
        super(TileEntityTypes.COMMAND_BLOCK);
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        this.h.a(nbttagcompound);
        nbttagcompound.setBoolean("powered", this.d());
        nbttagcompound.setBoolean("conditionMet", this.g());
        nbttagcompound.setBoolean("auto", this.f());
        return nbttagcompound;
    }

    @Override
    public void load(NBTTagCompound nbttagcompound) {
        super.load(nbttagcompound);
        this.h.b(nbttagcompound);
        this.a = nbttagcompound.getBoolean("powered");
        this.c = nbttagcompound.getBoolean("conditionMet");
        this.b(nbttagcompound.getBoolean("auto"));
    }

    @Nullable
    @Override
    public PacketPlayOutTileEntityData getUpdatePacket() {
        if (this.s()) {
            this.c(false);
            NBTTagCompound nbttagcompound = this.save(new NBTTagCompound());

            return new PacketPlayOutTileEntityData(this.position, 2, nbttagcompound);
        } else {
            return null;
        }
    }

    @Override
    public boolean isFilteredNBT() {
        return true;
    }

    public CommandBlockListenerAbstract getCommandBlock() {
        return this.h;
    }

    public void a(boolean flag) {
        this.a = flag;
    }

    public boolean d() {
        return this.a;
    }

    public boolean f() {
        return this.b;
    }

    public void b(boolean flag) {
        boolean flag1 = this.b;

        this.b = flag;
        if (!flag1 && flag && !this.a && this.world != null && this.t() != TileEntityCommand.Type.SEQUENCE) {
            Block block = this.getBlock().getBlock();

            if (block instanceof BlockCommand) {
                this.r();
                this.world.getBlockTickList().a(this.position, block, block.a((IWorldReader) this.world));
            }
        }

    }

    public boolean g() {
        return this.c;
    }

    public boolean r() {
        this.c = true;
        if (this.u()) {
            BlockPosition blockposition = this.position.shift(((EnumDirection) this.world.getType(this.position).get(BlockCommand.a)).opposite());

            if (this.world.getType(blockposition).getBlock() instanceof BlockCommand) {
                TileEntity tileentity = this.world.getTileEntity(blockposition);

                this.c = tileentity instanceof TileEntityCommand && ((TileEntityCommand) tileentity).getCommandBlock().i() > 0;
            } else {
                this.c = false;
            }
        }

        return this.c;
    }

    public boolean s() {
        return this.g;
    }

    public void c(boolean flag) {
        this.g = flag;
    }

    public TileEntityCommand.Type t() {
        Block block = this.getBlock().getBlock();

        return block == Blocks.COMMAND_BLOCK ? TileEntityCommand.Type.REDSTONE : (block == Blocks.REPEATING_COMMAND_BLOCK ? TileEntityCommand.Type.AUTO : (block == Blocks.CHAIN_COMMAND_BLOCK ? TileEntityCommand.Type.SEQUENCE : TileEntityCommand.Type.REDSTONE));
    }

    public boolean u() {
        IBlockData iblockdata = this.world.getType(this.getPosition());

        return iblockdata.getBlock() instanceof BlockCommand ? (Boolean) iblockdata.get(BlockCommand.b) : false;
    }

    @Override
    public void n() {
        this.invalidateBlockCache();
        super.n();
    }

    public static enum Type {

        SEQUENCE, AUTO, REDSTONE;

        private Type() {}
    }
}
