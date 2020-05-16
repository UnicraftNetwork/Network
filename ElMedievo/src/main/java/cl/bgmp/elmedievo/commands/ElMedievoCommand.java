package cl.bgmp.elmedievo.commands;

import cl.bgmp.elmedievo.Config;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ElMedievoCommand {
  @Command(
      aliases = {"reload"},
      desc = "Reloads ElMedievo's configuration.",
      max = 0)
  @CommandPermissions("elmedievo.reload")
  public static void reload(final CommandContext args, final CommandSender sender) {
    Config.reload();
    sender.sendMessage(ChatColor.GREEN + Translations.get("misc.configuration.reloaded", sender));
  }

  public static class ElMedievoParentCommand {
    @Command(
        aliases = {"medievo"},
        desc = "ElMedievo node command.")
    @NestedCommand(ElMedievoCommand.class)
    @CommandPermissions("elmedievo.node")
    public static void medievo(final CommandContext args, final CommandSender sender) {}
  }
}
