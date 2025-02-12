package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class DataConverterFlatten extends DataFix {

    private static final Map<String, String> a = (Map) DataFixUtils.make(Maps.newHashMap(), (hashmap) -> {
        hashmap.put("minecraft:stone.0", "minecraft:stone");
        hashmap.put("minecraft:stone.1", "minecraft:granite");
        hashmap.put("minecraft:stone.2", "minecraft:polished_granite");
        hashmap.put("minecraft:stone.3", "minecraft:diorite");
        hashmap.put("minecraft:stone.4", "minecraft:polished_diorite");
        hashmap.put("minecraft:stone.5", "minecraft:andesite");
        hashmap.put("minecraft:stone.6", "minecraft:polished_andesite");
        hashmap.put("minecraft:dirt.0", "minecraft:dirt");
        hashmap.put("minecraft:dirt.1", "minecraft:coarse_dirt");
        hashmap.put("minecraft:dirt.2", "minecraft:podzol");
        hashmap.put("minecraft:leaves.0", "minecraft:oak_leaves");
        hashmap.put("minecraft:leaves.1", "minecraft:spruce_leaves");
        hashmap.put("minecraft:leaves.2", "minecraft:birch_leaves");
        hashmap.put("minecraft:leaves.3", "minecraft:jungle_leaves");
        hashmap.put("minecraft:leaves2.0", "minecraft:acacia_leaves");
        hashmap.put("minecraft:leaves2.1", "minecraft:dark_oak_leaves");
        hashmap.put("minecraft:log.0", "minecraft:oak_log");
        hashmap.put("minecraft:log.1", "minecraft:spruce_log");
        hashmap.put("minecraft:log.2", "minecraft:birch_log");
        hashmap.put("minecraft:log.3", "minecraft:jungle_log");
        hashmap.put("minecraft:log2.0", "minecraft:acacia_log");
        hashmap.put("minecraft:log2.1", "minecraft:dark_oak_log");
        hashmap.put("minecraft:sapling.0", "minecraft:oak_sapling");
        hashmap.put("minecraft:sapling.1", "minecraft:spruce_sapling");
        hashmap.put("minecraft:sapling.2", "minecraft:birch_sapling");
        hashmap.put("minecraft:sapling.3", "minecraft:jungle_sapling");
        hashmap.put("minecraft:sapling.4", "minecraft:acacia_sapling");
        hashmap.put("minecraft:sapling.5", "minecraft:dark_oak_sapling");
        hashmap.put("minecraft:planks.0", "minecraft:oak_planks");
        hashmap.put("minecraft:planks.1", "minecraft:spruce_planks");
        hashmap.put("minecraft:planks.2", "minecraft:birch_planks");
        hashmap.put("minecraft:planks.3", "minecraft:jungle_planks");
        hashmap.put("minecraft:planks.4", "minecraft:acacia_planks");
        hashmap.put("minecraft:planks.5", "minecraft:dark_oak_planks");
        hashmap.put("minecraft:sand.0", "minecraft:sand");
        hashmap.put("minecraft:sand.1", "minecraft:red_sand");
        hashmap.put("minecraft:quartz_block.0", "minecraft:quartz_block");
        hashmap.put("minecraft:quartz_block.1", "minecraft:chiseled_quartz_block");
        hashmap.put("minecraft:quartz_block.2", "minecraft:quartz_pillar");
        hashmap.put("minecraft:anvil.0", "minecraft:anvil");
        hashmap.put("minecraft:anvil.1", "minecraft:chipped_anvil");
        hashmap.put("minecraft:anvil.2", "minecraft:damaged_anvil");
        hashmap.put("minecraft:wool.0", "minecraft:white_wool");
        hashmap.put("minecraft:wool.1", "minecraft:orange_wool");
        hashmap.put("minecraft:wool.2", "minecraft:magenta_wool");
        hashmap.put("minecraft:wool.3", "minecraft:light_blue_wool");
        hashmap.put("minecraft:wool.4", "minecraft:yellow_wool");
        hashmap.put("minecraft:wool.5", "minecraft:lime_wool");
        hashmap.put("minecraft:wool.6", "minecraft:pink_wool");
        hashmap.put("minecraft:wool.7", "minecraft:gray_wool");
        hashmap.put("minecraft:wool.8", "minecraft:light_gray_wool");
        hashmap.put("minecraft:wool.9", "minecraft:cyan_wool");
        hashmap.put("minecraft:wool.10", "minecraft:purple_wool");
        hashmap.put("minecraft:wool.11", "minecraft:blue_wool");
        hashmap.put("minecraft:wool.12", "minecraft:brown_wool");
        hashmap.put("minecraft:wool.13", "minecraft:green_wool");
        hashmap.put("minecraft:wool.14", "minecraft:red_wool");
        hashmap.put("minecraft:wool.15", "minecraft:black_wool");
        hashmap.put("minecraft:carpet.0", "minecraft:white_carpet");
        hashmap.put("minecraft:carpet.1", "minecraft:orange_carpet");
        hashmap.put("minecraft:carpet.2", "minecraft:magenta_carpet");
        hashmap.put("minecraft:carpet.3", "minecraft:light_blue_carpet");
        hashmap.put("minecraft:carpet.4", "minecraft:yellow_carpet");
        hashmap.put("minecraft:carpet.5", "minecraft:lime_carpet");
        hashmap.put("minecraft:carpet.6", "minecraft:pink_carpet");
        hashmap.put("minecraft:carpet.7", "minecraft:gray_carpet");
        hashmap.put("minecraft:carpet.8", "minecraft:light_gray_carpet");
        hashmap.put("minecraft:carpet.9", "minecraft:cyan_carpet");
        hashmap.put("minecraft:carpet.10", "minecraft:purple_carpet");
        hashmap.put("minecraft:carpet.11", "minecraft:blue_carpet");
        hashmap.put("minecraft:carpet.12", "minecraft:brown_carpet");
        hashmap.put("minecraft:carpet.13", "minecraft:green_carpet");
        hashmap.put("minecraft:carpet.14", "minecraft:red_carpet");
        hashmap.put("minecraft:carpet.15", "minecraft:black_carpet");
        hashmap.put("minecraft:hardened_clay.0", "minecraft:terracotta");
        hashmap.put("minecraft:stained_hardened_clay.0", "minecraft:white_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.1", "minecraft:orange_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.2", "minecraft:magenta_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.3", "minecraft:light_blue_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.4", "minecraft:yellow_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.5", "minecraft:lime_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.6", "minecraft:pink_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.7", "minecraft:gray_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.8", "minecraft:light_gray_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.9", "minecraft:cyan_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.10", "minecraft:purple_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.11", "minecraft:blue_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.12", "minecraft:brown_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.13", "minecraft:green_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.14", "minecraft:red_terracotta");
        hashmap.put("minecraft:stained_hardened_clay.15", "minecraft:black_terracotta");
        hashmap.put("minecraft:silver_glazed_terracotta.0", "minecraft:light_gray_glazed_terracotta");
        hashmap.put("minecraft:stained_glass.0", "minecraft:white_stained_glass");
        hashmap.put("minecraft:stained_glass.1", "minecraft:orange_stained_glass");
        hashmap.put("minecraft:stained_glass.2", "minecraft:magenta_stained_glass");
        hashmap.put("minecraft:stained_glass.3", "minecraft:light_blue_stained_glass");
        hashmap.put("minecraft:stained_glass.4", "minecraft:yellow_stained_glass");
        hashmap.put("minecraft:stained_glass.5", "minecraft:lime_stained_glass");
        hashmap.put("minecraft:stained_glass.6", "minecraft:pink_stained_glass");
        hashmap.put("minecraft:stained_glass.7", "minecraft:gray_stained_glass");
        hashmap.put("minecraft:stained_glass.8", "minecraft:light_gray_stained_glass");
        hashmap.put("minecraft:stained_glass.9", "minecraft:cyan_stained_glass");
        hashmap.put("minecraft:stained_glass.10", "minecraft:purple_stained_glass");
        hashmap.put("minecraft:stained_glass.11", "minecraft:blue_stained_glass");
        hashmap.put("minecraft:stained_glass.12", "minecraft:brown_stained_glass");
        hashmap.put("minecraft:stained_glass.13", "minecraft:green_stained_glass");
        hashmap.put("minecraft:stained_glass.14", "minecraft:red_stained_glass");
        hashmap.put("minecraft:stained_glass.15", "minecraft:black_stained_glass");
        hashmap.put("minecraft:stained_glass_pane.0", "minecraft:white_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.1", "minecraft:orange_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.2", "minecraft:magenta_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.3", "minecraft:light_blue_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.4", "minecraft:yellow_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.5", "minecraft:lime_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.6", "minecraft:pink_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.7", "minecraft:gray_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.8", "minecraft:light_gray_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.9", "minecraft:cyan_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.10", "minecraft:purple_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.11", "minecraft:blue_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.12", "minecraft:brown_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.13", "minecraft:green_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.14", "minecraft:red_stained_glass_pane");
        hashmap.put("minecraft:stained_glass_pane.15", "minecraft:black_stained_glass_pane");
        hashmap.put("minecraft:prismarine.0", "minecraft:prismarine");
        hashmap.put("minecraft:prismarine.1", "minecraft:prismarine_bricks");
        hashmap.put("minecraft:prismarine.2", "minecraft:dark_prismarine");
        hashmap.put("minecraft:concrete.0", "minecraft:white_concrete");
        hashmap.put("minecraft:concrete.1", "minecraft:orange_concrete");
        hashmap.put("minecraft:concrete.2", "minecraft:magenta_concrete");
        hashmap.put("minecraft:concrete.3", "minecraft:light_blue_concrete");
        hashmap.put("minecraft:concrete.4", "minecraft:yellow_concrete");
        hashmap.put("minecraft:concrete.5", "minecraft:lime_concrete");
        hashmap.put("minecraft:concrete.6", "minecraft:pink_concrete");
        hashmap.put("minecraft:concrete.7", "minecraft:gray_concrete");
        hashmap.put("minecraft:concrete.8", "minecraft:light_gray_concrete");
        hashmap.put("minecraft:concrete.9", "minecraft:cyan_concrete");
        hashmap.put("minecraft:concrete.10", "minecraft:purple_concrete");
        hashmap.put("minecraft:concrete.11", "minecraft:blue_concrete");
        hashmap.put("minecraft:concrete.12", "minecraft:brown_concrete");
        hashmap.put("minecraft:concrete.13", "minecraft:green_concrete");
        hashmap.put("minecraft:concrete.14", "minecraft:red_concrete");
        hashmap.put("minecraft:concrete.15", "minecraft:black_concrete");
        hashmap.put("minecraft:concrete_powder.0", "minecraft:white_concrete_powder");
        hashmap.put("minecraft:concrete_powder.1", "minecraft:orange_concrete_powder");
        hashmap.put("minecraft:concrete_powder.2", "minecraft:magenta_concrete_powder");
        hashmap.put("minecraft:concrete_powder.3", "minecraft:light_blue_concrete_powder");
        hashmap.put("minecraft:concrete_powder.4", "minecraft:yellow_concrete_powder");
        hashmap.put("minecraft:concrete_powder.5", "minecraft:lime_concrete_powder");
        hashmap.put("minecraft:concrete_powder.6", "minecraft:pink_concrete_powder");
        hashmap.put("minecraft:concrete_powder.7", "minecraft:gray_concrete_powder");
        hashmap.put("minecraft:concrete_powder.8", "minecraft:light_gray_concrete_powder");
        hashmap.put("minecraft:concrete_powder.9", "minecraft:cyan_concrete_powder");
        hashmap.put("minecraft:concrete_powder.10", "minecraft:purple_concrete_powder");
        hashmap.put("minecraft:concrete_powder.11", "minecraft:blue_concrete_powder");
        hashmap.put("minecraft:concrete_powder.12", "minecraft:brown_concrete_powder");
        hashmap.put("minecraft:concrete_powder.13", "minecraft:green_concrete_powder");
        hashmap.put("minecraft:concrete_powder.14", "minecraft:red_concrete_powder");
        hashmap.put("minecraft:concrete_powder.15", "minecraft:black_concrete_powder");
        hashmap.put("minecraft:cobblestone_wall.0", "minecraft:cobblestone_wall");
        hashmap.put("minecraft:cobblestone_wall.1", "minecraft:mossy_cobblestone_wall");
        hashmap.put("minecraft:sandstone.0", "minecraft:sandstone");
        hashmap.put("minecraft:sandstone.1", "minecraft:chiseled_sandstone");
        hashmap.put("minecraft:sandstone.2", "minecraft:cut_sandstone");
        hashmap.put("minecraft:red_sandstone.0", "minecraft:red_sandstone");
        hashmap.put("minecraft:red_sandstone.1", "minecraft:chiseled_red_sandstone");
        hashmap.put("minecraft:red_sandstone.2", "minecraft:cut_red_sandstone");
        hashmap.put("minecraft:stonebrick.0", "minecraft:stone_bricks");
        hashmap.put("minecraft:stonebrick.1", "minecraft:mossy_stone_bricks");
        hashmap.put("minecraft:stonebrick.2", "minecraft:cracked_stone_bricks");
        hashmap.put("minecraft:stonebrick.3", "minecraft:chiseled_stone_bricks");
        hashmap.put("minecraft:monster_egg.0", "minecraft:infested_stone");
        hashmap.put("minecraft:monster_egg.1", "minecraft:infested_cobblestone");
        hashmap.put("minecraft:monster_egg.2", "minecraft:infested_stone_bricks");
        hashmap.put("minecraft:monster_egg.3", "minecraft:infested_mossy_stone_bricks");
        hashmap.put("minecraft:monster_egg.4", "minecraft:infested_cracked_stone_bricks");
        hashmap.put("minecraft:monster_egg.5", "minecraft:infested_chiseled_stone_bricks");
        hashmap.put("minecraft:yellow_flower.0", "minecraft:dandelion");
        hashmap.put("minecraft:red_flower.0", "minecraft:poppy");
        hashmap.put("minecraft:red_flower.1", "minecraft:blue_orchid");
        hashmap.put("minecraft:red_flower.2", "minecraft:allium");
        hashmap.put("minecraft:red_flower.3", "minecraft:azure_bluet");
        hashmap.put("minecraft:red_flower.4", "minecraft:red_tulip");
        hashmap.put("minecraft:red_flower.5", "minecraft:orange_tulip");
        hashmap.put("minecraft:red_flower.6", "minecraft:white_tulip");
        hashmap.put("minecraft:red_flower.7", "minecraft:pink_tulip");
        hashmap.put("minecraft:red_flower.8", "minecraft:oxeye_daisy");
        hashmap.put("minecraft:double_plant.0", "minecraft:sunflower");
        hashmap.put("minecraft:double_plant.1", "minecraft:lilac");
        hashmap.put("minecraft:double_plant.2", "minecraft:tall_grass");
        hashmap.put("minecraft:double_plant.3", "minecraft:large_fern");
        hashmap.put("minecraft:double_plant.4", "minecraft:rose_bush");
        hashmap.put("minecraft:double_plant.5", "minecraft:peony");
        hashmap.put("minecraft:deadbush.0", "minecraft:dead_bush");
        hashmap.put("minecraft:tallgrass.0", "minecraft:dead_bush");
        hashmap.put("minecraft:tallgrass.1", "minecraft:grass");
        hashmap.put("minecraft:tallgrass.2", "minecraft:fern");
        hashmap.put("minecraft:sponge.0", "minecraft:sponge");
        hashmap.put("minecraft:sponge.1", "minecraft:wet_sponge");
        hashmap.put("minecraft:purpur_slab.0", "minecraft:purpur_slab");
        hashmap.put("minecraft:stone_slab.0", "minecraft:stone_slab");
        hashmap.put("minecraft:stone_slab.1", "minecraft:sandstone_slab");
        hashmap.put("minecraft:stone_slab.2", "minecraft:petrified_oak_slab");
        hashmap.put("minecraft:stone_slab.3", "minecraft:cobblestone_slab");
        hashmap.put("minecraft:stone_slab.4", "minecraft:brick_slab");
        hashmap.put("minecraft:stone_slab.5", "minecraft:stone_brick_slab");
        hashmap.put("minecraft:stone_slab.6", "minecraft:nether_brick_slab");
        hashmap.put("minecraft:stone_slab.7", "minecraft:quartz_slab");
        hashmap.put("minecraft:stone_slab2.0", "minecraft:red_sandstone_slab");
        hashmap.put("minecraft:wooden_slab.0", "minecraft:oak_slab");
        hashmap.put("minecraft:wooden_slab.1", "minecraft:spruce_slab");
        hashmap.put("minecraft:wooden_slab.2", "minecraft:birch_slab");
        hashmap.put("minecraft:wooden_slab.3", "minecraft:jungle_slab");
        hashmap.put("minecraft:wooden_slab.4", "minecraft:acacia_slab");
        hashmap.put("minecraft:wooden_slab.5", "minecraft:dark_oak_slab");
        hashmap.put("minecraft:coal.0", "minecraft:coal");
        hashmap.put("minecraft:coal.1", "minecraft:charcoal");
        hashmap.put("minecraft:fish.0", "minecraft:cod");
        hashmap.put("minecraft:fish.1", "minecraft:salmon");
        hashmap.put("minecraft:fish.2", "minecraft:clownfish");
        hashmap.put("minecraft:fish.3", "minecraft:pufferfish");
        hashmap.put("minecraft:cooked_fish.0", "minecraft:cooked_cod");
        hashmap.put("minecraft:cooked_fish.1", "minecraft:cooked_salmon");
        hashmap.put("minecraft:skull.0", "minecraft:skeleton_skull");
        hashmap.put("minecraft:skull.1", "minecraft:wither_skeleton_skull");
        hashmap.put("minecraft:skull.2", "minecraft:zombie_head");
        hashmap.put("minecraft:skull.3", "minecraft:player_head");
        hashmap.put("minecraft:skull.4", "minecraft:creeper_head");
        hashmap.put("minecraft:skull.5", "minecraft:dragon_head");
        hashmap.put("minecraft:golden_apple.0", "minecraft:golden_apple");
        hashmap.put("minecraft:golden_apple.1", "minecraft:enchanted_golden_apple");
        hashmap.put("minecraft:fireworks.0", "minecraft:firework_rocket");
        hashmap.put("minecraft:firework_charge.0", "minecraft:firework_star");
        hashmap.put("minecraft:dye.0", "minecraft:ink_sac");
        hashmap.put("minecraft:dye.1", "minecraft:rose_red");
        hashmap.put("minecraft:dye.2", "minecraft:cactus_green");
        hashmap.put("minecraft:dye.3", "minecraft:cocoa_beans");
        hashmap.put("minecraft:dye.4", "minecraft:lapis_lazuli");
        hashmap.put("minecraft:dye.5", "minecraft:purple_dye");
        hashmap.put("minecraft:dye.6", "minecraft:cyan_dye");
        hashmap.put("minecraft:dye.7", "minecraft:light_gray_dye");
        hashmap.put("minecraft:dye.8", "minecraft:gray_dye");
        hashmap.put("minecraft:dye.9", "minecraft:pink_dye");
        hashmap.put("minecraft:dye.10", "minecraft:lime_dye");
        hashmap.put("minecraft:dye.11", "minecraft:dandelion_yellow");
        hashmap.put("minecraft:dye.12", "minecraft:light_blue_dye");
        hashmap.put("minecraft:dye.13", "minecraft:magenta_dye");
        hashmap.put("minecraft:dye.14", "minecraft:orange_dye");
        hashmap.put("minecraft:dye.15", "minecraft:bone_meal");
        hashmap.put("minecraft:silver_shulker_box.0", "minecraft:light_gray_shulker_box");
        hashmap.put("minecraft:fence.0", "minecraft:oak_fence");
        hashmap.put("minecraft:fence_gate.0", "minecraft:oak_fence_gate");
        hashmap.put("minecraft:wooden_door.0", "minecraft:oak_door");
        hashmap.put("minecraft:boat.0", "minecraft:oak_boat");
        hashmap.put("minecraft:lit_pumpkin.0", "minecraft:jack_o_lantern");
        hashmap.put("minecraft:pumpkin.0", "minecraft:carved_pumpkin");
        hashmap.put("minecraft:trapdoor.0", "minecraft:oak_trapdoor");
        hashmap.put("minecraft:nether_brick.0", "minecraft:nether_bricks");
        hashmap.put("minecraft:red_nether_brick.0", "minecraft:red_nether_bricks");
        hashmap.put("minecraft:netherbrick.0", "minecraft:nether_brick");
        hashmap.put("minecraft:wooden_button.0", "minecraft:oak_button");
        hashmap.put("minecraft:wooden_pressure_plate.0", "minecraft:oak_pressure_plate");
        hashmap.put("minecraft:noteblock.0", "minecraft:note_block");
        hashmap.put("minecraft:bed.0", "minecraft:white_bed");
        hashmap.put("minecraft:bed.1", "minecraft:orange_bed");
        hashmap.put("minecraft:bed.2", "minecraft:magenta_bed");
        hashmap.put("minecraft:bed.3", "minecraft:light_blue_bed");
        hashmap.put("minecraft:bed.4", "minecraft:yellow_bed");
        hashmap.put("minecraft:bed.5", "minecraft:lime_bed");
        hashmap.put("minecraft:bed.6", "minecraft:pink_bed");
        hashmap.put("minecraft:bed.7", "minecraft:gray_bed");
        hashmap.put("minecraft:bed.8", "minecraft:light_gray_bed");
        hashmap.put("minecraft:bed.9", "minecraft:cyan_bed");
        hashmap.put("minecraft:bed.10", "minecraft:purple_bed");
        hashmap.put("minecraft:bed.11", "minecraft:blue_bed");
        hashmap.put("minecraft:bed.12", "minecraft:brown_bed");
        hashmap.put("minecraft:bed.13", "minecraft:green_bed");
        hashmap.put("minecraft:bed.14", "minecraft:red_bed");
        hashmap.put("minecraft:bed.15", "minecraft:black_bed");
        hashmap.put("minecraft:banner.15", "minecraft:white_banner");
        hashmap.put("minecraft:banner.14", "minecraft:orange_banner");
        hashmap.put("minecraft:banner.13", "minecraft:magenta_banner");
        hashmap.put("minecraft:banner.12", "minecraft:light_blue_banner");
        hashmap.put("minecraft:banner.11", "minecraft:yellow_banner");
        hashmap.put("minecraft:banner.10", "minecraft:lime_banner");
        hashmap.put("minecraft:banner.9", "minecraft:pink_banner");
        hashmap.put("minecraft:banner.8", "minecraft:gray_banner");
        hashmap.put("minecraft:banner.7", "minecraft:light_gray_banner");
        hashmap.put("minecraft:banner.6", "minecraft:cyan_banner");
        hashmap.put("minecraft:banner.5", "minecraft:purple_banner");
        hashmap.put("minecraft:banner.4", "minecraft:blue_banner");
        hashmap.put("minecraft:banner.3", "minecraft:brown_banner");
        hashmap.put("minecraft:banner.2", "minecraft:green_banner");
        hashmap.put("minecraft:banner.1", "minecraft:red_banner");
        hashmap.put("minecraft:banner.0", "minecraft:black_banner");
        hashmap.put("minecraft:grass.0", "minecraft:grass_block");
        hashmap.put("minecraft:brick_block.0", "minecraft:bricks");
        hashmap.put("minecraft:end_bricks.0", "minecraft:end_stone_bricks");
        hashmap.put("minecraft:golden_rail.0", "minecraft:powered_rail");
        hashmap.put("minecraft:magma.0", "minecraft:magma_block");
        hashmap.put("minecraft:quartz_ore.0", "minecraft:nether_quartz_ore");
        hashmap.put("minecraft:reeds.0", "minecraft:sugar_cane");
        hashmap.put("minecraft:slime.0", "minecraft:slime_block");
        hashmap.put("minecraft:stone_stairs.0", "minecraft:cobblestone_stairs");
        hashmap.put("minecraft:waterlily.0", "minecraft:lily_pad");
        hashmap.put("minecraft:web.0", "minecraft:cobweb");
        hashmap.put("minecraft:snow.0", "minecraft:snow_block");
        hashmap.put("minecraft:snow_layer.0", "minecraft:snow");
        hashmap.put("minecraft:record_11.0", "minecraft:music_disc_11");
        hashmap.put("minecraft:record_13.0", "minecraft:music_disc_13");
        hashmap.put("minecraft:record_blocks.0", "minecraft:music_disc_blocks");
        hashmap.put("minecraft:record_cat.0", "minecraft:music_disc_cat");
        hashmap.put("minecraft:record_chirp.0", "minecraft:music_disc_chirp");
        hashmap.put("minecraft:record_far.0", "minecraft:music_disc_far");
        hashmap.put("minecraft:record_mall.0", "minecraft:music_disc_mall");
        hashmap.put("minecraft:record_mellohi.0", "minecraft:music_disc_mellohi");
        hashmap.put("minecraft:record_stal.0", "minecraft:music_disc_stal");
        hashmap.put("minecraft:record_strad.0", "minecraft:music_disc_strad");
        hashmap.put("minecraft:record_wait.0", "minecraft:music_disc_wait");
        hashmap.put("minecraft:record_ward.0", "minecraft:music_disc_ward");
    });
    private static final Set<String> b = (Set) DataConverterFlatten.a.keySet().stream().map((s) -> {
        return s.substring(0, s.indexOf(46));
    }).collect(Collectors.toSet());
    private static final Set<String> c = Sets.newHashSet(new String[] { "minecraft:bow", "minecraft:carrot_on_a_stick", "minecraft:chainmail_boots", "minecraft:chainmail_chestplate", "minecraft:chainmail_helmet", "minecraft:chainmail_leggings", "minecraft:diamond_axe", "minecraft:diamond_boots", "minecraft:diamond_chestplate", "minecraft:diamond_helmet", "minecraft:diamond_hoe", "minecraft:diamond_leggings", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel", "minecraft:diamond_sword", "minecraft:elytra", "minecraft:fishing_rod", "minecraft:flint_and_steel", "minecraft:golden_axe", "minecraft:golden_boots", "minecraft:golden_chestplate", "minecraft:golden_helmet", "minecraft:golden_hoe", "minecraft:golden_leggings", "minecraft:golden_pickaxe", "minecraft:golden_shovel", "minecraft:golden_sword", "minecraft:iron_axe", "minecraft:iron_boots", "minecraft:iron_chestplate", "minecraft:iron_helmet", "minecraft:iron_hoe", "minecraft:iron_leggings", "minecraft:iron_pickaxe", "minecraft:iron_shovel", "minecraft:iron_sword", "minecraft:leather_boots", "minecraft:leather_chestplate", "minecraft:leather_helmet", "minecraft:leather_leggings", "minecraft:shears", "minecraft:shield", "minecraft:stone_axe", "minecraft:stone_hoe", "minecraft:stone_pickaxe", "minecraft:stone_shovel", "minecraft:stone_sword", "minecraft:wooden_axe", "minecraft:wooden_hoe", "minecraft:wooden_pickaxe", "minecraft:wooden_shovel", "minecraft:wooden_sword"});

    public DataConverterFlatten(Schema schema, boolean flag) {
        super(schema, flag);
    }

    public TypeRewriteRule makeRule() {
        Type<?> type = this.getInputSchema().getType(DataConverterTypes.ITEM_STACK);
        OpticFinder<Pair<String, String>> opticfinder = DSL.fieldFinder("id", DSL.named(DataConverterTypes.r.typeName(), DSL.namespacedString()));
        OpticFinder<?> opticfinder1 = type.findField("tag");

        return this.fixTypeEverywhereTyped("ItemInstanceTheFlatteningFix", type, (typed) -> {
            Optional<Pair<String, String>> optional = typed.getOptional(opticfinder);

            if (!optional.isPresent()) {
                return typed;
            } else {
                Typed<?> typed1 = typed;
                Dynamic<?> dynamic = (Dynamic) typed.get(DSL.remainderFinder());
                int i = dynamic.get("Damage").asInt(0);
                String s = a((String) ((Pair) optional.get()).getSecond(), i);

                if (s != null) {
                    typed1 = typed.set(opticfinder, Pair.of(DataConverterTypes.r.typeName(), s));
                }

                if (DataConverterFlatten.c.contains(((Pair) optional.get()).getSecond())) {
                    Typed<?> typed2 = typed.getOrCreateTyped(opticfinder1);
                    Dynamic<?> dynamic1 = (Dynamic) typed2.get(DSL.remainderFinder());

                    dynamic1 = dynamic1.set("Damage", dynamic1.createInt(i));
                    typed1 = typed1.set(opticfinder1, typed2.set(DSL.remainderFinder(), dynamic1));
                }

                typed1 = typed1.set(DSL.remainderFinder(), dynamic.remove("Damage"));
                return typed1;
            }
        });
    }

    @Nullable
    public static String a(@Nullable String s, int i) {
        if (DataConverterFlatten.b.contains(s)) {
            String s1 = (String) DataConverterFlatten.a.get(s + '.' + i);

            return s1 == null ? (String) DataConverterFlatten.a.get(s + ".0") : s1;
        } else {
            return null;
        }
    }
}
