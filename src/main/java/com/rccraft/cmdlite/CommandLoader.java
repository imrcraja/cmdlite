package com.rccraft.cmdlite;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scans .minecraft/config/cmdlite/commands/ for .txt files.
 * Each file becomes one command: filename (without .txt) = command name,
 * file content (one command per line) = what gets sent to the server.
 */
public class CommandLoader {

    public static Path getCommandsFolder() {
        Path folder = FabricLoader.getInstance()
                .getConfigDir()
                .resolve("cmdlite")
                .resolve("commands");
        try {
            Files.createDirectories(folder);
        } catch (IOException e) {
            CmdLiteClient.LOGGER.error("[CmdLite] Could not create commands folder", e);
        }
        return folder;
    }

    /**
     * Returns a map of commandName -> list of raw command lines to execute in order.
     */
    public static Map<String, List<String>> loadAll() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        Path folder = getCommandsFolder();

        try (Stream<Path> files = Files.list(folder)) {
            files.filter(p -> p.toString().toLowerCase().endsWith(".txt"))
                 .forEach(p -> {
                     String fileName = p.getFileName().toString();
                     String cmdName = fileName.substring(0, fileName.length() - 4).trim();
                     if (cmdName.isEmpty()) return;

                     try {
                         List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8)
                                 .stream()
                                 .map(String::trim)
                                 .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                                 .map(line -> line.startsWith("/") ? line.substring(1) : line)
                                 .collect(Collectors.toList());

                         if (!lines.isEmpty()) {
                             result.put(cmdName.toLowerCase(), lines);
                         }
                     } catch (IOException e) {
                         CmdLiteClient.LOGGER.error("[CmdLite] Failed to read {}", fileName, e);
                     }
                 });
        } catch (IOException e) {
            CmdLiteClient.LOGGER.error("[CmdLite] Failed to list commands folder", e);
        }

        return result;
    }
}
