package net.minecraft.server;

public class EnchantmentLootBonus extends Enchantment {

    protected EnchantmentLootBonus(Enchantment.Rarity enchantment_rarity, EnchantmentSlotType enchantmentslottype, EnumItemSlot... aenumitemslot) {
        super(enchantment_rarity, enchantmentslottype, aenumitemslot);
    }

    @Override
    public int a(int i) {
        return 15 + (i - 1) * 9;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean a(Enchantment enchantment) {
        return super.a(enchantment) && enchantment != Enchantments.SILK_TOUCH;
    }
}
