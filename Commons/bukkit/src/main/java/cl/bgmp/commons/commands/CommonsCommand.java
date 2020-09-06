package cl.bgmp.commons.commands;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.annotations.Command;
import com.sk89q.minecraft.util.commands.annotations.CommandPermissions;
import com.sk89q.minecraft.util.commands.annotations.NestedCommand;
import com.sk89q.minecraft.util.commands.annotations.TabCompletion;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommonsCommand {
  @Command(
      aliases = {"reload"},
      desc = "Reloads Commons's configuration.",
      max = 0)
  @CommandPermissions("commons.reload")
  public static void reload(final CommandContext args, final CommandSender sender) {
    Config.reload();
    Commons.get().reloadModules();
    sender.sendMessage(ChatColor.GREEN + Translations.get("misc.configuration.reloaded", sender));
  }

  public static class CommonsParentCommand {
    @Command(
        aliases = {"commons"},
        desc = "Commons node command")
    @NestedCommand(value = CommonsCommand.class, executeBody = false)
    @CommandPermissions("commons.node")
    public static void commons(final CommandContext args, final CommandSender sender) {}
  }

  @TabCompletion
  public static class FUtilsTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(
        CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
      switch (args.length) {
        case 1:
          return Arrays.asList("reload");
        default:
          return Collections.emptyList();
      }
    }
  }
}
