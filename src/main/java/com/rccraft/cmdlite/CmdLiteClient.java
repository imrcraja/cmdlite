package com.rccraft.cmdlite;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class CmdLiteClient implements ClientModInitializer {

    public static final String MOD_ID = "cmdlite";
    public static final Logger LOGGER = LoggerFactory.getLogger("CmdLite");
    public static final String PREFIX = "lite:";

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerUserCommands(dispatcher);
            registerBuiltinCommands(dispatcher);
        });

        LOGGER.info("[CmdLite] Loaded. Drop .txt files into config/cmdlite/commands/");
    }

    private void registerUserCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        Map<String, List<String>> commands = CommandLoader.loadAll();

        for (Map.Entry<String, List<String>> entry : commands.entrySet()) {
            String cmdName = PREFIX + entry.getKey();
            List<String> linesToRun = entry.getValue();

            dispatcher.register(
                literal(cmdName).executes(ctx -> {
                    runLines(linesToRun);
                    ctx.getSource().sendFeedback(Component.literal("§b[CmdLite] §7Ran §f/" + entry.getKey()
                            + " §7(" + linesToRun.size() + " command" + (linesToRun.size() == 1 ? "" : "s") + ")"));
                    return 1;
                })
            );
        }
    }

    private void registerBuiltinCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // /lite:reload - rescans the commands folder without restarting the game
        dispatcher.register(
            literal(PREFIX + "reload").executes(ctx -> {
                dispatcher.getRoot().getChildren().removeIf(node -> node.getName().startsWith(PREFIX));
                registerUserCommands(dispatcher);
                registerBuiltinCommands(dispatcher);
                ctx.getSource().sendFeedback(Component.literal("§b[CmdLite] §aCommands reloaded."));
                return 1;
            })
        );

        // /lite:list - shows every loaded shortcut
        dispatcher.register(
            literal(PREFIX + "list").executes(ctx -> {
                Map<String, List<String>> commands = CommandLoader.loadAll();
                if (commands.isEmpty()) {
                    ctx.getSource().sendFeedback(Component.literal("§b[CmdLite] §7No commands found in config/cmdlite/commands/"));
                } else {
                    ctx.getSource().sendFeedback(Component.literal("§b[CmdLite] §7Loaded commands:"));
                    for (String name : commands.keySet()) {
                        ctx.getSource().sendFeedback(Component.literal("  §f/" + PREFIX + name));
                    }
                }
                return 1;
            })
        );
    }

    /**
     * Sends each line to the server exactly as if the player typed it themselves.
     * This is why CmdLite works on any server: the server only ever sees the
     * original full command, never the short alias.
     */
    private void runLines(List<String> lines) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        for (String line : lines) {
            player.connection.sendCommand(line);
        }
    }
}
