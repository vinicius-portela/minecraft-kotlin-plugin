package net.minecraft.server;

public class ContainerMerchant extends Container {

    private final IMerchant merchant;
    private final InventoryMerchant inventoryMerchant;

    public ContainerMerchant(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, new MerchantWrapper(playerinventory.player));
    }

    public ContainerMerchant(int i, PlayerInventory playerinventory, IMerchant imerchant) {
        super(Containers.MERCHANT, i);
        this.merchant = imerchant;
        this.inventoryMerchant = new InventoryMerchant(imerchant);
        this.a(new Slot(this.inventoryMerchant, 0, 136, 37));
        this.a(new Slot(this.inventoryMerchant, 1, 162, 37));
        this.a((Slot) (new SlotMerchantResult(playerinventory.player, imerchant, this.inventoryMerchant, 2, 220, 37)));

        int j;

        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.a(new Slot(playerinventory, k + j * 9 + 9, 108 + k * 18, 84 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(playerinventory, j, 108 + j * 18, 142));
        }

    }

    @Override
    public void a(IInventory iinventory) {
        this.inventoryMerchant.f();
        super.a(iinventory);
    }

    public void d(int i) {
        this.inventoryMerchant.c(i);
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return this.merchant.getTrader() == entityhuman;
    }

    @Override
    public boolean a(ItemStack itemstack, Slot slot) {
        return false;
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.a;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 2) {
                if (!this.a(itemstack1, 3, 39, true)) {
                    return ItemStack.a;
                }

                slot.a(itemstack1, itemstack);
            } else if (i != 0 && i != 1) {
                if (i >= 3 && i < 30) {
                    if (!this.a(itemstack1, 30, 39, false)) {
                        return ItemStack.a;
                    }
                } else if (i >= 30 && i < 39 && !this.a(itemstack1, 3, 30, false)) {
                    return ItemStack.a;
                }
            } else if (!this.a(itemstack1, 3, 39, false)) {
                return ItemStack.a;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.a);
            } else {
                slot.d();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.a;
            }

            slot.a(entityhuman, itemstack1);
        }

        return itemstack;
    }

    @Override
    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.merchant.setTradingPlayer((EntityHuman) null);
        if (!this.merchant.getWorld().isClientSide) {
            if (entityhuman.isAlive() && (!(entityhuman instanceof EntityPlayer) || !((EntityPlayer) entityhuman).o())) {
                entityhuman.inventory.a(entityhuman.world, this.inventoryMerchant.splitWithoutUpdate(0));
                entityhuman.inventory.a(entityhuman.world, this.inventoryMerchant.splitWithoutUpdate(1));
            } else {
                ItemStack itemstack = this.inventoryMerchant.splitWithoutUpdate(0);

                if (!itemstack.isEmpty()) {
                    entityhuman.drop(itemstack, false);
                }

                itemstack = this.inventoryMerchant.splitWithoutUpdate(1);
                if (!itemstack.isEmpty()) {
                    entityhuman.drop(itemstack, false);
                }
            }

        }
    }

    public void g(int i) {
        if (this.h().size() > i) {
            ItemStack itemstack = this.inventoryMerchant.getItem(0);

            if (!itemstack.isEmpty()) {
                if (!this.a(itemstack, 3, 39, true)) {
                    return;
                }

                this.inventoryMerchant.setItem(0, itemstack);
            }

            ItemStack itemstack1 = this.inventoryMerchant.getItem(1);

            if (!itemstack1.isEmpty()) {
                if (!this.a(itemstack1, 3, 39, true)) {
                    return;
                }

                this.inventoryMerchant.setItem(1, itemstack1);
            }

            if (this.inventoryMerchant.getItem(0).isEmpty() && this.inventoryMerchant.getItem(1).isEmpty()) {
                ItemStack itemstack2 = ((MerchantRecipe) this.h().get(i)).getBuyItem1();

                this.c(0, itemstack2);
                ItemStack itemstack3 = ((MerchantRecipe) this.h().get(i)).getBuyItem2();

                this.c(1, itemstack3);
            }

        }
    }

    private void c(int i, ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            for (int j = 3; j < 39; ++j) {
                ItemStack itemstack1 = ((Slot) this.slots.get(j)).getItem();

                if (!itemstack1.isEmpty() && this.b(itemstack, itemstack1)) {
                    ItemStack itemstack2 = this.inventoryMerchant.getItem(i);
                    int k = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    int l = Math.min(64 - k, itemstack1.getCount());
                    ItemStack itemstack3 = itemstack1.cloneItemStack();
                    int i1 = k + l;

                    itemstack1.subtract(l);
                    itemstack3.setCount(i1);
                    this.inventoryMerchant.setItem(i, itemstack3);
                    if (i1 >= 64) {
                        break;
                    }
                }
            }
        }

    }

    private boolean b(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.getItem() == itemstack1.getItem() && ItemStack.equals(itemstack, itemstack1);
    }

    public MerchantRecipeList h() {
        return this.merchant.getOffers();
    }
}
