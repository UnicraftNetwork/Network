package cl.bgmp.commons.commands;

import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommonsCommand {
  @Command(
      aliases = {"reload"},
      desc = "Reloads Commons's configuration.",
      max = 0)
  @CommandPermissions("commons.reload")
  public static void reload(final CommandContext args, final CommandSender sender) {
    Config.reload();
    sender.sendMessage(ChatColor.GREEN + Translations.get("misc.configuration.reloaded", sender));
  }

  public static class CommonsParentCommand {
    @Command(
        aliases = {"commons"},
        desc = "Commons node command",
        max = 0)
    @NestedCommand(CommonsCommand.class)
    @CommandPermissions("commons.node")
    public static void commons(final CommandContext args, final CommandSender sender) {}
  }
}
