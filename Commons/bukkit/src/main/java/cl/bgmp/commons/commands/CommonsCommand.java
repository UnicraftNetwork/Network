package cl.bgmp.commons.commands;

import cl.bgmp.commons.Commons;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.NestedCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommonsCommand {
  @Command(
      aliases = {"reload"},
      desc = "Reloads Commons's configuration.",
      max = 0)
  @CommandPermissions("commons.reload")
  public static void reload(final CommandContext args, final CommandSender sender) {
    Commons.get().reloadConfig();
    Commons.get().getModuleManager().reloadModules();
    sender.sendMessage(
        ChatColor.GREEN
            + Commons.get().getTranslations().get("misc.configuration.reloaded", sender));
  }

  public static class CommonsParentCommand {
    @Command(
        aliases = {"commons"},
        desc = "Commons node command")
    @NestedCommand(value = CommonsCommand.class, executeBody = false)
    @CommandPermissions("commons.node")
    public static void commons(final CommandContext args, final CommandSender sender) {}
  }
}
