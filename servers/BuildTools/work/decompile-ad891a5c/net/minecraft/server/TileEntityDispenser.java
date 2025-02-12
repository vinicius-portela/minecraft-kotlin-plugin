package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;

public class TileEntityDispenser extends TileEntityLootable {

    private static final Random a = new Random();
    private NonNullList<ItemStack> items;

    protected TileEntityDispenser(TileEntityTypes<?> tileentitytypes) {
        super(tileentitytypes);
        this.items = NonNullList.a(9, ItemStack.a);
    }

    public TileEntityDispenser() {
        this(TileEntityTypes.DISPENSER);
    }

    @Override
    public int getSize() {
        return 9;
    }

    @Override
    public boolean isNotEmpty() {
        Iterator iterator = this.items.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    public int r() {
        this.d((EntityHuman) null);
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.items.size(); ++k) {
            if (!((ItemStack) this.items.get(k)).isEmpty() && TileEntityDispenser.a.nextInt(j++) == 0) {
                i = k;
            }
        }

        return i;
    }

    public int addItem(ItemStack itemstack) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (((ItemStack) this.items.get(i)).isEmpty()) {
                this.setItem(i, itemstack);
                return i;
            }
        }

        return -1;
    }

    @Override
    protected IChatBaseComponent getContainerName() {
        return new ChatMessage("container.dispenser", new Object[0]);
    }

    @Override
    public void load(NBTTagCompound nbttagcompound) {
        super.load(nbttagcompound);
        this.items = NonNullList.a(this.getSize(), ItemStack.a);
        if (!this.d(nbttagcompound)) {
            ContainerUtil.b(nbttagcompound, this.items);
        }

    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        super.save(nbttagcompound);
        if (!this.e(nbttagcompound)) {
            ContainerUtil.a(nbttagcompound, this.items);
        }

        return nbttagcompound;
    }

    @Override
    protected NonNullList<ItemStack> f() {
        return this.items;
    }

    @Override
    protected void a(NonNullList<ItemStack> nonnulllist) {
        this.items = nonnulllist;
    }

    @Override
    protected Container createContainer(int i, PlayerInventory playerinventory) {
        return new ContainerDispenser(i, playerinventory, this);
    }
}
