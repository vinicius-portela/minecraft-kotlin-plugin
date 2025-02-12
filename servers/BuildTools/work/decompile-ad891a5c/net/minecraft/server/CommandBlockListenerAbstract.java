package net.minecraft.server;

import com.mojang.brigadier.context.CommandContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;

public abstract class CommandBlockListenerAbstract implements ICommandListener {

    private static final SimpleDateFormat b = new SimpleDateFormat("HH:mm:ss");
    private long lastExecution = -1L;
    private boolean updateLastExecution = true;
    private int successCount;
    private boolean trackOutput = true;
    private IChatBaseComponent lastOutput;
    private String command = "";
    private IChatBaseComponent customName = new ChatComponentText("@");

    public CommandBlockListenerAbstract() {}

    public int i() {
        return this.successCount;
    }

    public void a(int i) {
        this.successCount = i;
    }

    public IChatBaseComponent j() {
        return (IChatBaseComponent) (this.lastOutput == null ? new ChatComponentText("") : this.lastOutput);
    }

    public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Command", this.command);
        nbttagcompound.setInt("SuccessCount", this.successCount);
        nbttagcompound.setString("CustomName", IChatBaseComponent.ChatSerializer.a(this.customName));
        nbttagcompound.setBoolean("TrackOutput", this.trackOutput);
        if (this.lastOutput != null && this.trackOutput) {
            nbttagcompound.setString("LastOutput", IChatBaseComponent.ChatSerializer.a(this.lastOutput));
        }

        nbttagcompound.setBoolean("UpdateLastExecution", this.updateLastExecution);
        if (this.updateLastExecution && this.lastExecution > 0L) {
            nbttagcompound.setLong("LastExecution", this.lastExecution);
        }

        return nbttagcompound;
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.command = nbttagcompound.getString("Command");
        this.successCount = nbttagcompound.getInt("SuccessCount");
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.customName = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("CustomName"));
        }

        if (nbttagcompound.hasKeyOfType("TrackOutput", 1)) {
            this.trackOutput = nbttagcompound.getBoolean("TrackOutput");
        }

        if (nbttagcompound.hasKeyOfType("LastOutput", 8) && this.trackOutput) {
            try {
                this.lastOutput = IChatBaseComponent.ChatSerializer.a(nbttagcompound.getString("LastOutput"));
            } catch (Throwable throwable) {
                this.lastOutput = new ChatComponentText(throwable.getMessage());
            }
        } else {
            this.lastOutput = null;
        }

        if (nbttagcompound.hasKey("UpdateLastExecution")) {
            this.updateLastExecution = nbttagcompound.getBoolean("UpdateLastExecution");
        }

        if (this.updateLastExecution && nbttagcompound.hasKey("LastExecution")) {
            this.lastExecution = nbttagcompound.getLong("LastExecution");
        } else {
            this.lastExecution = -1L;
        }

    }

    public void setCommand(String s) {
        this.command = s;
        this.successCount = 0;
    }

    public String getCommand() {
        return this.command;
    }

    public boolean a(World world) {
        if (!world.isClientSide && world.getTime() != this.lastExecution) {
            if ("Searge".equalsIgnoreCase(this.command)) {
                this.lastOutput = new ChatComponentText("#itzlipofutzli");
                this.successCount = 1;
                return true;
            } else {
                this.successCount = 0;
                MinecraftServer minecraftserver = this.d().getMinecraftServer();

                if (minecraftserver != null && minecraftserver.E() && minecraftserver.getEnableCommandBlock() && !UtilColor.b(this.command)) {
                    try {
                        this.lastOutput = null;
                        CommandListenerWrapper commandlistenerwrapper = this.getWrapper().a((commandcontext, flag, i) -> {
                            if (flag) {
                                ++this.successCount;
                            }

                        });

                        minecraftserver.getCommandDispatcher().a(commandlistenerwrapper, this.command);
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.a(throwable, "Executing command block");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Command to be executed");

                        crashreportsystemdetails.a("Command", this::getCommand);
                        crashreportsystemdetails.a("Name", () -> {
                            return this.getName().getString();
                        });
                        throw new ReportedException(crashreport);
                    }
                }

                if (this.updateLastExecution) {
                    this.lastExecution = world.getTime();
                } else {
                    this.lastExecution = -1L;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public IChatBaseComponent getName() {
        return this.customName;
    }

    public void setName(IChatBaseComponent ichatbasecomponent) {
        this.customName = ichatbasecomponent;
    }

    @Override
    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        if (this.trackOutput) {
            this.lastOutput = (new ChatComponentText("[" + CommandBlockListenerAbstract.b.format(new Date()) + "] ")).addSibling(ichatbasecomponent);
            this.e();
        }

    }

    public abstract WorldServer d();

    public abstract void e();

    public void c(@Nullable IChatBaseComponent ichatbasecomponent) {
        this.lastOutput = ichatbasecomponent;
    }

    public void a(boolean flag) {
        this.trackOutput = flag;
    }

    public boolean a(EntityHuman entityhuman) {
        if (!entityhuman.isCreativeAndOp()) {
            return false;
        } else {
            if (entityhuman.getWorld().isClientSide) {
                entityhuman.a(this);
            }

            return true;
        }
    }

    public abstract CommandListenerWrapper getWrapper();

    @Override
    public boolean shouldSendSuccess() {
        return this.d().getGameRules().getBoolean("sendCommandFeedback") && this.trackOutput;
    }

    @Override
    public boolean shouldSendFailure() {
        return this.trackOutput;
    }

    @Override
    public boolean shouldBroadcastCommands() {
        return this.d().getGameRules().getBoolean("commandBlockOutput");
    }
}
