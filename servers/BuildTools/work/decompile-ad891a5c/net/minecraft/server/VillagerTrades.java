package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class VillagerTrades {

    public static final Map<VillagerProfession, Int2ObjectMap<VillagerTrades.IMerchantRecipeOption[]>> a = (Map) SystemUtils.a((Object) Maps.newHashMap(), (hashmap) -> {
        hashmap.put(VillagerProfession.FARMER, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.WHEAT, 20, 8, 2), new VillagerTrades.b(Items.POTATO, 26, 8, 2), new VillagerTrades.b(Items.CARROT, 22, 8, 2), new VillagerTrades.b(Items.BEETROOT, 15, 8, 2), new VillagerTrades.h(Items.BREAD, 1, 6, 8, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Blocks.PUMPKIN, 6, 6, 10), new VillagerTrades.h(Items.PUMPKIN_PIE, 1, 4, 5), new VillagerTrades.h(Items.APPLE, 1, 4, 8, 5)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Items.COOKIE, 3, 18, 10), new VillagerTrades.b(Blocks.MELON, 4, 6, 20)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Blocks.CAKE, 1, 1, 6, 15), new VillagerTrades.i(MobEffects.FASTER_MOVEMENT, 160, 15), new VillagerTrades.i(MobEffects.JUMP, 160, 15), new VillagerTrades.i(MobEffects.WEAKNESS, 140, 15), new VillagerTrades.i(MobEffects.BLINDNESS, 120, 15), new VillagerTrades.i(MobEffects.POISON, 280, 15), new VillagerTrades.i(MobEffects.SATURATION, 7, 15)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Items.GOLDEN_CARROT, 3, 3, 30), new VillagerTrades.h(Items.GLISTERING_MELON_SLICE, 4, 3, 30)})));
        hashmap.put(VillagerProfession.FISHERMAN, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.STRING, 20, 8, 2), new VillagerTrades.b(Items.COAL, 10, 8, 2), new VillagerTrades.g(Items.COD, 6, Items.COOKED_COD, 6, 8, 1), new VillagerTrades.h(Items.COD_BUCKET, 3, 1, 8, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.COD, 15, 8, 10), new VillagerTrades.g(Items.SALMON, 6, Items.COOKED_SALMON, 6, 8, 5), new VillagerTrades.h(Items.pS, 2, 1, 5)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.SALMON, 13, 8, 20), new VillagerTrades.e(Items.FISHING_ROD, 3, 2, 10, 0.2F)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.TROPICAL_FISH, 6, 6, 30)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.PUFFERFISH, 4, 6, 30), new VillagerTrades.c(1, 6, 30, ImmutableMap.builder().put(VillagerType.c, Items.OAK_BOAT).put(VillagerType.g, Items.SPRUCE_BOAT).put(VillagerType.e, Items.SPRUCE_BOAT).put(VillagerType.a, Items.JUNGLE_BOAT).put(VillagerType.b, Items.JUNGLE_BOAT).put(VillagerType.d, Items.ACACIA_BOAT).put(VillagerType.f, Items.DARK_OAK_BOAT).build())})));
        hashmap.put(VillagerProfession.SHEPHERD, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Blocks.WHITE_WOOL, 18, 8, 2), new VillagerTrades.b(Blocks.BROWN_WOOL, 18, 8, 2), new VillagerTrades.b(Blocks.BLACK_WOOL, 18, 8, 2), new VillagerTrades.b(Blocks.GRAY_WOOL, 18, 8, 2), new VillagerTrades.h(Items.SHEARS, 2, 1, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.WHITE_DYE, 12, 8, 10), new VillagerTrades.b(Items.GRAY_DYE, 12, 8, 10), new VillagerTrades.b(Items.BLACK_DYE, 12, 8, 10), new VillagerTrades.b(Items.LIGHT_BLUE_DYE, 12, 8, 10), new VillagerTrades.b(Items.LIME_DYE, 12, 8, 10), new VillagerTrades.h(Blocks.WHITE_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.ORANGE_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.MAGENTA_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.LIGHT_BLUE_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.YELLOW_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.LIME_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.PINK_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.GRAY_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.LIGHT_GRAY_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.CYAN_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.PURPLE_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.BLUE_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.BROWN_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.GREEN_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.RED_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.BLACK_WOOL, 1, 1, 8, 5), new VillagerTrades.h(Blocks.WHITE_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.ORANGE_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.MAGENTA_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.LIGHT_BLUE_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.YELLOW_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.LIME_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.PINK_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.GRAY_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.LIGHT_GRAY_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.CYAN_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.PURPLE_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.BLUE_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.BROWN_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.GREEN_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.RED_CARPET, 1, 4, 8, 5), new VillagerTrades.h(Blocks.BLACK_CARPET, 1, 4, 8, 5)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.YELLOW_DYE, 12, 8, 20), new VillagerTrades.b(Items.LIGHT_GRAY_DYE, 12, 8, 20), new VillagerTrades.b(Items.ORANGE_DYE, 12, 8, 20), new VillagerTrades.b(Items.RED_DYE, 12, 8, 20), new VillagerTrades.b(Items.PINK_DYE, 12, 8, 20), new VillagerTrades.h(Blocks.WHITE_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.YELLOW_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.RED_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.BLACK_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.BLUE_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.BROWN_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.CYAN_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.GRAY_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.GREEN_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.LIGHT_BLUE_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.LIGHT_GRAY_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.LIME_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.MAGENTA_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.ORANGE_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.PINK_BED, 3, 1, 6, 10), new VillagerTrades.h(Blocks.PURPLE_BED, 3, 1, 6, 10)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.BROWN_DYE, 12, 8, 30), new VillagerTrades.b(Items.PURPLE_DYE, 12, 8, 30), new VillagerTrades.b(Items.BLUE_DYE, 12, 8, 30), new VillagerTrades.b(Items.GREEN_DYE, 12, 8, 30), new VillagerTrades.b(Items.MAGENTA_DYE, 12, 8, 30), new VillagerTrades.b(Items.CYAN_DYE, 12, 8, 30), new VillagerTrades.h(Items.WHITE_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.BLUE_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.LIGHT_BLUE_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.RED_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.PINK_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.GREEN_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.LIME_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.GRAY_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.BLACK_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.PURPLE_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.MAGENTA_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.CYAN_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.BROWN_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.YELLOW_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.ORANGE_BANNER, 3, 1, 6, 15), new VillagerTrades.h(Items.LIGHT_GRAY_BANNER, 3, 1, 6, 15)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Items.PAINTING, 2, 3, 30)})));
        hashmap.put(VillagerProfession.FLETCHER, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.STICK, 32, 8, 2), new VillagerTrades.h(Items.ARROW, 1, 16, 1), new VillagerTrades.g(Blocks.GRAVEL, 10, Items.FLINT, 10, 6, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.FLINT, 26, 6, 10), new VillagerTrades.h(Items.BOW, 2, 1, 5)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.STRING, 14, 8, 20), new VillagerTrades.h(Items.CROSSBOW, 3, 1, 10)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.FEATHER, 24, 8, 30), new VillagerTrades.e(Items.BOW, 2, 2, 15)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.dE, 8, 6, 30), new VillagerTrades.e(Items.CROSSBOW, 3, 2, 15), new VillagerTrades.j(Items.ARROW, 5, Items.TIPPED_ARROW, 5, 2, 6, 30)})));
        hashmap.put(VillagerProfession.LIBRARIAN, a(ImmutableMap.builder().put(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.PAPER, 24, 8, 2), new VillagerTrades.d(1), new VillagerTrades.h(Blocks.BOOKSHELF, 6, 3, 6, 1)}).put(2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.BOOK, 4, 6, 10), new VillagerTrades.d(5), new VillagerTrades.h(Items.pQ, 1, 1, 5)}).put(3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.INK_SAC, 5, 6, 20), new VillagerTrades.d(10), new VillagerTrades.h(Items.am, 1, 4, 10)}).put(4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.WRITABLE_BOOK, 2, 6, 30), new VillagerTrades.d(15), new VillagerTrades.h(Items.CLOCK, 5, 1, 15), new VillagerTrades.h(Items.COMPASS, 4, 1, 15)}).put(5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Items.NAME_TAG, 20, 1, 30)}).build()));
        hashmap.put(VillagerProfession.CARTOGRAPHER, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.PAPER, 24, 8, 2), new VillagerTrades.h(Items.MAP, 7, 1, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.df, 10, 8, 10), new VillagerTrades.k(13, "Monument", MapIcon.Type.MONUMENT, 6, 5)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.COMPASS, 1, 6, 20), new VillagerTrades.k(14, "Mansion", MapIcon.Type.MANSION, 6, 10)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Items.ITEM_FRAME, 7, 1, 15), new VillagerTrades.h(Items.WHITE_BANNER, 3, 1, 15), new VillagerTrades.h(Items.BLUE_BANNER, 3, 1, 15), new VillagerTrades.h(Items.LIGHT_BLUE_BANNER, 3, 1, 15), new VillagerTrades.h(Items.RED_BANNER, 3, 1, 15), new VillagerTrades.h(Items.PINK_BANNER, 3, 1, 15), new VillagerTrades.h(Items.GREEN_BANNER, 3, 1, 15), new VillagerTrades.h(Items.LIME_BANNER, 3, 1, 15), new VillagerTrades.h(Items.GRAY_BANNER, 3, 1, 15), new VillagerTrades.h(Items.BLACK_BANNER, 3, 1, 15), new VillagerTrades.h(Items.PURPLE_BANNER, 3, 1, 15), new VillagerTrades.h(Items.MAGENTA_BANNER, 3, 1, 15), new VillagerTrades.h(Items.CYAN_BANNER, 3, 1, 15), new VillagerTrades.h(Items.BROWN_BANNER, 3, 1, 15), new VillagerTrades.h(Items.YELLOW_BANNER, 3, 1, 15), new VillagerTrades.h(Items.ORANGE_BANNER, 3, 1, 15), new VillagerTrades.h(Items.LIGHT_GRAY_BANNER, 3, 1, 15)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Items.GLOBE_BANNER_PATTERN, 8, 1, 30)})));
        hashmap.put(VillagerProfession.CLERIC, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.ROTTEN_FLESH, 32, 8, 2), new VillagerTrades.h(Items.REDSTONE, 1, 2, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.GOLD_INGOT, 3, 6, 10), new VillagerTrades.h(Items.LAPIS_LAZULI, 1, 1, 5)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.RABBIT_FOOT, 2, 6, 20), new VillagerTrades.h(Blocks.GLOWSTONE, 4, 1, 6, 10)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.SCUTE, 4, 6, 30), new VillagerTrades.b(Items.GLASS_BOTTLE, 9, 6, 30), new VillagerTrades.h(Items.ENDER_PEARL, 5, 1, 15)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.NETHER_WART, 22, 6, 30), new VillagerTrades.h(Items.EXPERIENCE_BOTTLE, 3, 1, 30)})));
        hashmap.put(VillagerProfession.ARMORER, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.COAL, 15, 8, 2), new VillagerTrades.h(new ItemStack(Items.IRON_LEGGINGS), 7, 1, 6, 1, 0.2F), new VillagerTrades.h(new ItemStack(Items.IRON_BOOTS), 4, 1, 6, 1, 0.2F), new VillagerTrades.h(new ItemStack(Items.IRON_HELMET), 5, 1, 6, 1, 0.2F), new VillagerTrades.h(new ItemStack(Items.IRON_CHESTPLATE), 9, 1, 6, 1, 0.2F)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.IRON_INGOT, 4, 6, 10), new VillagerTrades.h(new ItemStack(Items.pP), 36, 1, 6, 5, 0.2F), new VillagerTrades.h(new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 6, 5, 0.2F), new VillagerTrades.h(new ItemStack(Items.CHAINMAIL_LEGGINGS), 3, 1, 6, 5, 0.2F)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.LAVA_BUCKET, 1, 6, 20), new VillagerTrades.b(Items.DIAMOND, 1, 6, 20), new VillagerTrades.h(new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 6, 10, 0.2F), new VillagerTrades.h(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 6, 10, 0.2F), new VillagerTrades.h(new ItemStack(Items.SHIELD), 5, 1, 6, 10, 0.2F)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.e(Items.DIAMOND_LEGGINGS, 14, 2, 15, 0.2F), new VillagerTrades.e(Items.DIAMOND_BOOTS, 8, 2, 15, 0.2F)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.e(Items.DIAMOND_HELMET, 8, 2, 30, 0.2F), new VillagerTrades.e(Items.DIAMOND_CHESTPLATE, 16, 2, 30, 0.2F)})));
        hashmap.put(VillagerProfession.WEAPONSMITH, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.COAL, 15, 8, 2), new VillagerTrades.h(new ItemStack(Items.IRON_AXE), 3, 1, 6, 1, 0.2F), new VillagerTrades.e(Items.IRON_SWORD, 2, 2, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.IRON_INGOT, 4, 6, 10), new VillagerTrades.h(new ItemStack(Items.pP), 36, 1, 6, 5, 0.2F)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.FLINT, 24, 6, 20)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.DIAMOND, 1, 6, 30), new VillagerTrades.e(Items.DIAMOND_AXE, 12, 2, 15, 0.2F)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.e(Items.DIAMOND_SWORD, 8, 2, 30, 0.2F)})));
        hashmap.put(VillagerProfession.TOOLSMITH, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.COAL, 15, 8, 2), new VillagerTrades.h(new ItemStack(Items.STONE_AXE), 1, 1, 6, 1, 0.2F), new VillagerTrades.h(new ItemStack(Items.STONE_SHOVEL), 1, 1, 6, 1, 0.2F), new VillagerTrades.h(new ItemStack(Items.STONE_PICKAXE), 1, 1, 6, 1, 0.2F), new VillagerTrades.h(new ItemStack(Items.STONE_HOE), 1, 1, 6, 1, 0.2F)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.IRON_INGOT, 4, 6, 10), new VillagerTrades.h(new ItemStack(Items.pP), 36, 1, 6, 5, 0.2F)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.FLINT, 30, 6, 20), new VillagerTrades.e(Items.IRON_AXE, 1, 2, 10, 0.2F), new VillagerTrades.e(Items.IRON_SHOVEL, 2, 2, 10, 0.2F), new VillagerTrades.e(Items.IRON_PICKAXE, 3, 2, 10, 0.2F), new VillagerTrades.e(Items.DIAMOND_HOE, 9, 2, 10, 0.2F)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.DIAMOND, 1, 6, 30), new VillagerTrades.e(Items.DIAMOND_AXE, 12, 2, 15, 0.2F), new VillagerTrades.e(Items.DIAMOND_SHOVEL, 5, 2, 15, 0.2F)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.e(Items.DIAMOND_PICKAXE, 13, 2, 30, 0.2F)})));
        hashmap.put(VillagerProfession.BUTCHER, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.CHICKEN, 14, 8, 2), new VillagerTrades.b(Items.PORKCHOP, 7, 8, 2), new VillagerTrades.b(Items.RABBIT, 4, 8, 2), new VillagerTrades.h(Items.RABBIT_STEW, 1, 1, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.COAL, 15, 8, 2), new VillagerTrades.h(Items.COOKED_PORKCHOP, 1, 5, 8, 5), new VillagerTrades.h(Items.COOKED_CHICKEN, 1, 8, 8, 5)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.MUTTON, 7, 8, 20), new VillagerTrades.b(Items.BEEF, 10, 8, 20)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.kP, 10, 6, 30)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.SWEET_BERRIES, 10, 6, 30)})));
        hashmap.put(VillagerProfession.LEATHERWORKER, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.LEATHER, 6, 8, 2), new VillagerTrades.a(Items.LEATHER_LEGGINGS, 3), new VillagerTrades.a(Items.LEATHER_CHESTPLATE, 7)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.FLINT, 26, 6, 10), new VillagerTrades.a(Items.LEATHER_HELMET, 5, 6, 5), new VillagerTrades.a(Items.LEATHER_BOOTS, 4, 6, 5)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.RABBIT_HIDE, 9, 6, 20), new VillagerTrades.a(Items.LEATHER_CHESTPLATE, 7)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.SCUTE, 4, 6, 30), new VillagerTrades.a(Items.LEATHER_HORSE_ARMOR, 6, 6, 15)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(new ItemStack(Items.SADDLE), 6, 1, 6, 30, 0.2F), new VillagerTrades.a(Items.LEATHER_HELMET, 5, 6, 30)})));
        hashmap.put(VillagerProfession.MASON, a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.CLAY_BALL, 10, 8, 2), new VillagerTrades.h(Items.BRICK, 1, 10, 8, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Blocks.STONE, 20, 8, 10), new VillagerTrades.h(Blocks.CHISELED_STONE_BRICKS, 1, 4, 8, 5)}, 3, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Blocks.GRANITE, 16, 8, 20), new VillagerTrades.b(Blocks.ANDESITE, 16, 8, 20), new VillagerTrades.b(Blocks.DIORITE, 16, 8, 20), new VillagerTrades.h(Blocks.POLISHED_ANDESITE, 1, 4, 8, 10), new VillagerTrades.h(Blocks.POLISHED_DIORITE, 1, 4, 8, 10), new VillagerTrades.h(Blocks.POLISHED_GRANITE, 1, 4, 8, 10)}, 4, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.b(Items.QUARTZ, 12, 6, 30), new VillagerTrades.h(Blocks.ORANGE_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.WHITE_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.BLUE_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.LIGHT_BLUE_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.GRAY_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.LIGHT_GRAY_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.BLACK_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.RED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.PINK_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.MAGENTA_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.LIME_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.GREEN_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.CYAN_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.PURPLE_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.YELLOW_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.BROWN_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.ORANGE_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.WHITE_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.BLUE_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.GRAY_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.BLACK_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.RED_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.PINK_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.MAGENTA_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.LIME_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.GREEN_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.CYAN_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.PURPLE_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.YELLOW_GLAZED_TERRACOTTA, 1, 1, 6, 15), new VillagerTrades.h(Blocks.BROWN_GLAZED_TERRACOTTA, 1, 1, 6, 15)}, 5, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Blocks.QUARTZ_PILLAR, 1, 1, 6, 30), new VillagerTrades.h(Blocks.QUARTZ_BLOCK, 1, 1, 6, 30)})));
    });
    public static final Int2ObjectMap<VillagerTrades.IMerchantRecipeOption[]> b = a(ImmutableMap.of(1, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Items.aC, 2, 1, 5, 1), new VillagerTrades.h(Items.SLIME_BALL, 4, 1, 5, 1), new VillagerTrades.h(Items.cJ, 2, 1, 5, 1), new VillagerTrades.h(Items.NAUTILUS_SHELL, 5, 1, 5, 1), new VillagerTrades.h(Items.az, 1, 1, 12, 1), new VillagerTrades.h(Items.kN, 1, 1, 8, 1), new VillagerTrades.h(Items.cF, 1, 1, 4, 1), new VillagerTrades.h(Items.kO, 3, 1, 12, 1), new VillagerTrades.h(Items.cw, 3, 1, 8, 1), new VillagerTrades.h(Items.aU, 1, 1, 12, 1), new VillagerTrades.h(Items.aV, 1, 1, 12, 1), new VillagerTrades.h(Items.aW, 1, 1, 8, 1), new VillagerTrades.h(Items.aX, 1, 1, 12, 1), new VillagerTrades.h(Items.aY, 1, 1, 12, 1), new VillagerTrades.h(Items.aZ, 1, 1, 12, 1), new VillagerTrades.h(Items.ba, 1, 1, 12, 1), new VillagerTrades.h(Items.bb, 1, 1, 12, 1), new VillagerTrades.h(Items.bc, 1, 1, 12, 1), new VillagerTrades.h(Items.bd, 1, 1, 12, 1), new VillagerTrades.h(Items.be, 1, 1, 12, 1), new VillagerTrades.h(Items.bf, 1, 1, 7, 1), new VillagerTrades.h(Items.WHEAT_SEEDS, 1, 1, 12, 1), new VillagerTrades.h(Items.BEETROOT_SEEDS, 1, 1, 12, 1), new VillagerTrades.h(Items.PUMPKIN_SEEDS, 1, 1, 12, 1), new VillagerTrades.h(Items.MELON_SEEDS, 1, 1, 12, 1), new VillagerTrades.h(Items.x, 5, 1, 8, 1), new VillagerTrades.h(Items.v, 5, 1, 8, 1), new VillagerTrades.h(Items.y, 5, 1, 8, 1), new VillagerTrades.h(Items.w, 5, 1, 8, 1), new VillagerTrades.h(Items.t, 5, 1, 8, 1), new VillagerTrades.h(Items.u, 5, 1, 8, 1), new VillagerTrades.h(Items.RED_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.WHITE_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.BLUE_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.PINK_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.BLACK_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.GREEN_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.LIGHT_GRAY_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.MAGENTA_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.YELLOW_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.GRAY_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.PURPLE_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.LIGHT_BLUE_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.LIME_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.ORANGE_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.BROWN_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.CYAN_DYE, 1, 3, 12, 1), new VillagerTrades.h(Items.hK, 3, 1, 8, 1), new VillagerTrades.h(Items.hL, 3, 1, 8, 1), new VillagerTrades.h(Items.hM, 3, 1, 8, 1), new VillagerTrades.h(Items.hN, 3, 1, 8, 1), new VillagerTrades.h(Items.hJ, 3, 1, 8, 1), new VillagerTrades.h(Items.dh, 1, 1, 12, 1), new VillagerTrades.h(Items.bh, 1, 1, 12, 1), new VillagerTrades.h(Items.bi, 1, 1, 12, 1), new VillagerTrades.h(Items.dr, 1, 2, 5, 1), new VillagerTrades.h(Items.A, 1, 8, 8, 1), new VillagerTrades.h(Items.B, 1, 4, 6, 1)}, 2, new VillagerTrades.IMerchantRecipeOption[] { new VillagerTrades.h(Items.TROPICAL_FISH_BUCKET, 5, 1, 4, 1), new VillagerTrades.h(Items.PUFFERFISH_BUCKET, 5, 1, 4, 1), new VillagerTrades.h(Items.fg, 3, 1, 6, 1), new VillagerTrades.h(Items.ii, 6, 1, 6, 1), new VillagerTrades.h(Items.GUNPOWDER, 1, 1, 8, 1), new VillagerTrades.h(Items.l, 3, 3, 6, 1)}));

    private static Int2ObjectMap<VillagerTrades.IMerchantRecipeOption[]> a(ImmutableMap<Integer, VillagerTrades.IMerchantRecipeOption[]> immutablemap) {
        return new Int2ObjectOpenHashMap(immutablemap);
    }

    static class g implements VillagerTrades.IMerchantRecipeOption {

        private final ItemStack a;
        private final int b;
        private final int c;
        private final ItemStack d;
        private final int e;
        private final int f;
        private final int g;
        private final float h;

        public g(IMaterial imaterial, int i, Item item, int j, int k, int l) {
            this(imaterial, i, 1, item, j, k, l);
        }

        public g(IMaterial imaterial, int i, int j, Item item, int k, int l, int i1) {
            this.a = new ItemStack(imaterial);
            this.b = i;
            this.c = j;
            this.d = new ItemStack(item);
            this.e = k;
            this.f = l;
            this.g = i1;
            this.h = 0.05F;
        }

        @Nullable
        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            return new MerchantRecipe(new ItemStack(Items.EMERALD, this.c), new ItemStack(this.a.getItem(), this.b), new ItemStack(this.d.getItem(), this.e), this.f, this.g, this.h);
        }
    }

    static class k implements VillagerTrades.IMerchantRecipeOption {

        private final int a;
        private final String b;
        private final MapIcon.Type c;
        private final int d;
        private final int e;

        public k(int i, String s, MapIcon.Type mapicon_type, int j, int k) {
            this.a = i;
            this.b = s;
            this.c = mapicon_type;
            this.d = j;
            this.e = k;
        }

        @Nullable
        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            World world = entity.world;
            BlockPosition blockposition = world.a(this.b, new BlockPosition(entity), 100, true);

            if (blockposition != null) {
                ItemStack itemstack = ItemWorldMap.createFilledMapView(world, blockposition.getX(), blockposition.getZ(), (byte) 2, true, true);

                ItemWorldMap.applySepiaFilter(world, itemstack);
                WorldMap.decorateMap(itemstack, blockposition, "+", this.c);
                itemstack.a((IChatBaseComponent) (new ChatMessage("filled_map." + this.b.toLowerCase(Locale.ROOT), new Object[0])));
                return new MerchantRecipe(new ItemStack(Items.EMERALD, this.a), new ItemStack(Items.COMPASS), itemstack, this.d, this.e, 0.2F);
            } else {
                return null;
            }
        }
    }

    static class d implements VillagerTrades.IMerchantRecipeOption {

        private final int a;

        public d(int i) {
            this.a = i;
        }

        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            Enchantment enchantment = (Enchantment) IRegistry.ENCHANTMENT.a(random);
            int i = MathHelper.nextInt(random, enchantment.getStartLevel(), enchantment.getMaxLevel());
            ItemStack itemstack = ItemEnchantedBook.a(new WeightedRandomEnchant(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;

            if (enchantment.isTreasure()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }

            return new MerchantRecipe(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemstack, 6, this.a, 0.2F);
        }
    }

    static class a implements VillagerTrades.IMerchantRecipeOption {

        private final Item a;
        private final int b;
        private final int c;
        private final int d;

        public a(Item item, int i) {
            this(item, i, 6, 1);
        }

        public a(Item item, int i, int j, int k) {
            this.a = item;
            this.b = i;
            this.c = j;
            this.d = k;
        }

        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            ItemStack itemstack = new ItemStack(Items.EMERALD, this.b);
            ItemStack itemstack1 = new ItemStack(this.a);

            if (this.a instanceof ItemArmorColorable) {
                List<ItemDye> list = Lists.newArrayList();

                list.add(a(random));
                if (random.nextFloat() > 0.7F) {
                    list.add(a(random));
                }

                if (random.nextFloat() > 0.8F) {
                    list.add(a(random));
                }

                itemstack1 = IDyeable.a(itemstack1, list);
            }

            return new MerchantRecipe(itemstack, itemstack1, this.c, this.d, 0.2F);
        }

        private static ItemDye a(Random random) {
            return ItemDye.a(EnumColor.fromColorIndex(random.nextInt(16)));
        }
    }

    static class j implements VillagerTrades.IMerchantRecipeOption {

        private final ItemStack a;
        private final int b;
        private final int c;
        private final int d;
        private final int e;
        private final Item f;
        private final int g;
        private final float h;

        public j(Item item, int i, Item item1, int j, int k, int l, int i1) {
            this.a = new ItemStack(item1);
            this.c = k;
            this.d = l;
            this.e = i1;
            this.f = item;
            this.g = i;
            this.b = j;
            this.h = 0.05F;
        }

        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            ItemStack itemstack = new ItemStack(Items.EMERALD, this.c);
            List<PotionRegistry> list = (List) IRegistry.POTION.d().filter((potionregistry) -> {
                return !potionregistry.a().isEmpty() && PotionBrewer.a(potionregistry);
            }).collect(Collectors.toList());
            PotionRegistry potionregistry = (PotionRegistry) list.get(random.nextInt(list.size()));
            ItemStack itemstack1 = PotionUtil.a(new ItemStack(this.a.getItem(), this.b), potionregistry);

            return new MerchantRecipe(itemstack, new ItemStack(this.f, this.g), itemstack1, this.d, this.e, this.h);
        }
    }

    static class e implements VillagerTrades.IMerchantRecipeOption {

        private final ItemStack a;
        private final int b;
        private final int c;
        private final int d;
        private final float e;

        public e(Item item, int i, int j, int k) {
            this(item, i, j, k, 0.05F);
        }

        public e(Item item, int i, int j, int k, float f) {
            this.a = new ItemStack(item);
            this.b = i;
            this.c = j;
            this.d = k;
            this.e = f;
        }

        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            int i = 5 + random.nextInt(15);
            ItemStack itemstack = EnchantmentManager.a(random, new ItemStack(this.a.getItem()), i, false);
            int j = Math.min(this.b + i, 64);
            ItemStack itemstack1 = new ItemStack(Items.EMERALD, j);

            return new MerchantRecipe(itemstack1, itemstack, this.c, this.d, this.e);
        }
    }

    static class i implements VillagerTrades.IMerchantRecipeOption {

        final MobEffectList a;
        final int b;
        final int c;
        private final float d;

        public i(MobEffectList mobeffectlist, int i, int j) {
            this.a = mobeffectlist;
            this.b = i;
            this.c = j;
            this.d = 0.05F;
        }

        @Nullable
        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            ItemStack itemstack = new ItemStack(Items.SUSPICIOUS_STEW, 1);

            ItemSuspiciousStew.a(itemstack, this.a, this.b);
            return new MerchantRecipe(new ItemStack(Items.EMERALD, 1), itemstack, 6, this.c, this.d);
        }
    }

    static class h implements VillagerTrades.IMerchantRecipeOption {

        private final ItemStack a;
        private final int b;
        private final int c;
        private final int d;
        private final int e;
        private final float f;

        public h(Block block, int i, int j, int k, int l) {
            this(new ItemStack(block), i, j, k, l);
        }

        public h(Item item, int i, int j, int k) {
            this(new ItemStack(item), i, j, 6, k);
        }

        public h(Item item, int i, int j, int k, int l) {
            this(new ItemStack(item), i, j, k, l);
        }

        public h(ItemStack itemstack, int i, int j, int k, int l) {
            this(itemstack, i, j, k, l, 0.05F);
        }

        public h(ItemStack itemstack, int i, int j, int k, int l, float f) {
            this.a = itemstack;
            this.b = i;
            this.c = j;
            this.d = k;
            this.e = l;
            this.f = f;
        }

        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            return new MerchantRecipe(new ItemStack(Items.EMERALD, this.b), new ItemStack(this.a.getItem(), this.c), this.d, this.e, this.f);
        }
    }

    static class c implements VillagerTrades.IMerchantRecipeOption {

        private final Map<VillagerType, Item> a;
        private final int b;
        private final int c;
        private final int d;

        public c(int i, int j, int k, Map<VillagerType, Item> map) {
            IRegistry.VILLAGER_TYPE.d().filter((villagertype) -> {
                return !map.containsKey(villagertype);
            }).findAny().ifPresent((villagertype) -> {
                throw new IllegalStateException("Missing trade for villager type: " + IRegistry.VILLAGER_TYPE.getKey(villagertype));
            });
            this.a = map;
            this.b = i;
            this.c = j;
            this.d = k;
        }

        @Nullable
        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            if (entity instanceof VillagerDataHolder) {
                ItemStack itemstack = new ItemStack((IMaterial) this.a.get(((VillagerDataHolder) entity).getVillagerData().getType()), this.b);

                return new MerchantRecipe(itemstack, new ItemStack(Items.EMERALD), this.c, this.d, 0.05F);
            } else {
                return null;
            }
        }
    }

    static class b implements VillagerTrades.IMerchantRecipeOption {

        private final Item a;
        private final int b;
        private final int c;
        private final int d;
        private final float e;

        public b(IMaterial imaterial, int i, int j, int k) {
            this.a = imaterial.getItem();
            this.b = i;
            this.c = j;
            this.d = k;
            this.e = 0.05F;
        }

        @Override
        public MerchantRecipe a(Entity entity, Random random) {
            ItemStack itemstack = new ItemStack(this.a, this.b);

            return new MerchantRecipe(itemstack, new ItemStack(Items.EMERALD), this.c, this.d, this.e);
        }
    }

    public interface IMerchantRecipeOption {

        @Nullable
        MerchantRecipe a(Entity entity, Random random);
    }
}
