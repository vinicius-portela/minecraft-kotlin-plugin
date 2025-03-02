package net.minecraft.server;

public class DebugReportFluidTags extends DebugReportTags<FluidType> {

    public DebugReportFluidTags(DebugReportGenerator debugreportgenerator) {
        super(debugreportgenerator, IRegistry.FLUID);
    }

    @Override
    protected void b() {
        this.a(TagsFluid.WATER).a((Object[])(FluidTypes.WATER, FluidTypes.FLOWING_WATER));
        this.a(TagsFluid.LAVA).a((Object[])(FluidTypes.LAVA, FluidTypes.FLOWING_LAVA));
    }

    @Override
    protected java.nio.file.Path a(MinecraftKey minecraftkey) {
        return this.b.b().resolve("data/" + minecraftkey.b() + "/tags/fluids/" + minecraftkey.getKey() + ".json");
    }

    @Override
    public String a() {
        return "Fluid Tags";
    }

    @Override
    protected void a(Tags<FluidType> tags) {
        TagsFluid.a(tags);
    }
}
