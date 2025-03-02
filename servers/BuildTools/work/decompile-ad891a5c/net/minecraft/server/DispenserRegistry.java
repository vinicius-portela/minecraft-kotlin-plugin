package net.minecraft.server;

import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DispenserRegistry {

    public static final PrintStream a = System.out;
    private static boolean b;
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        if (!DispenserRegistry.b) {
            DispenserRegistry.b = true;
            if (IRegistry.f.c()) {
                throw new IllegalStateException("Unable to load registries");
            } else {
                BlockFire.d();
                BlockComposter.d();
                if (EntityTypes.getName(EntityTypes.PLAYER) == null) {
                    throw new IllegalStateException("Failed loading EntityTypes");
                } else {
                    PotionBrewer.a();
                    PlayerSelector.a();
                    IDispenseBehavior.c();
                    ArgumentRegistry.a();
                    d();
                }
            }
        }
    }

    private static <T> void a(IRegistry<T> iregistry, Function<T, String> function, Set<String> set) {
        LocaleLanguage localelanguage = LocaleLanguage.a();

        iregistry.iterator().forEachRemaining((object) -> {
            String s = (String) function.apply(object);

            if (!localelanguage.b(s)) {
                set.add(s);
            }

        });
    }

    public static Set<String> b() {
        Set<String> set = new TreeSet();

        a(IRegistry.ENTITY_TYPE, EntityTypes::e, set);
        a(IRegistry.MOB_EFFECT, MobEffectList::c, set);
        a(IRegistry.ITEM, Item::getName, set);
        a(IRegistry.ENCHANTMENT, Enchantment::g, set);
        a(IRegistry.BIOME, BiomeBase::j, set);
        a(IRegistry.BLOCK, Block::l, set);
        a(IRegistry.CUSTOM_STAT, (minecraftkey) -> {
            return "stat." + minecraftkey.toString().replace(':', '.');
        }, set);
        return set;
    }

    public static void c() {
        if (!DispenserRegistry.b) {
            throw new IllegalArgumentException("Not bootstrapped");
        } else if (!SharedConstants.b) {
            b().forEach((s) -> {
                DispenserRegistry.LOGGER.error("Missing translations: " + s);
            });
        }
    }

    private static void d() {
        if (DispenserRegistry.LOGGER.isDebugEnabled()) {
            System.setErr(new DebugOutputStream("STDERR", System.err));
            System.setOut(new DebugOutputStream("STDOUT", DispenserRegistry.a));
        } else {
            System.setErr(new RedirectStream("STDERR", System.err));
            System.setOut(new RedirectStream("STDOUT", DispenserRegistry.a));
        }

    }

    public static void a(String s) {
        DispenserRegistry.a.println(s);
    }
}
