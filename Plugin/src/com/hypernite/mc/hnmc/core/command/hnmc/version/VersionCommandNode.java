package com.hypernite.mc.hnmc.core.command.hnmc.version;

import com.hypernite.mc.hnmc.core.config.implement.HNMCoreConfig;
import com.hypernite.mc.hnmc.core.exception.PluginNotFoundException;
import com.hypernite.mc.hnmc.core.exception.ResourceNotFoundException;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.ResourceManager;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class VersionCommandNode extends CommandNode {

    private static final List<String> plugins = Arrays.stream(Bukkit.getServer().getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toList());
    protected static final HNMCoreConfig config = HyperNiteMC.getHnmCoreConfig();

    public VersionCommandNode(CommandNode parent, @Nonnull String command, String permission, @Nonnull String description, String placeholder, String... alias) {
        super(parent, command, permission, description, placeholder, alias);
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        var plugin = args.get(0);
        ResourceManager manager;
        if (config.getVersionChecker().resourceId_to_checks.containsKey(plugin)) {
            manager = HyperNiteMC.getAPI().getResourceManager(ResourceManager.Type.SPIGOT);
        } else {
            manager = HyperNiteMC.getAPI().getResourceManager(ResourceManager.Type.HYPERNITE);
        }
        try {
            Plugin resource = Bukkit.getServer().getPluginManager().getPlugin(plugin);
            if (resource == null) throw new PluginNotFoundException(plugin);
            String currentVersion = resource.getDescription().getVersion();
            this.executeChecker(sender, manager, plugin, currentVersion);
        } catch (PluginNotFoundException e) {
            sender.sendMessage(config.getPrefix() + "§c找不到此插件。");
        } catch (ResourceNotFoundException e) {
            sender.sendMessage(config.getPrefix() + "§c找不到此插件的遠端資源。");
        }
        return true;
    }

    public abstract void executeChecker(CommandSender sender, ResourceManager manager, String plugin, String version) throws ResourceNotFoundException, PluginNotFoundException;

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        if (this.getPlaceholder() == null) return null;
        List<Integer> pluginTab = new LinkedList<>();
        String[] papis = this.getPlaceholder().split(" ");
        for (int i = 0; i < papis.length; i++) {
            if (papis[i].contains("plugin")) {
                pluginTab.add(i);
            }
        }
        return pluginTab.contains(args.size() - 1) ? plugins : null;
    }
}
