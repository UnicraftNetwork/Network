package cl.bgmp.lobby.commands;

import cl.bgmp.lobby.Config;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class LobbyCommand {
  @Command(
      aliases = {"reload"},
      desc = "Reloads Lobby's configuration.",
      max = 0)
  @CommandPermissions("lobby.reload")
  public static void reload(final CommandContext args, final CommandSender sender) {
    Config.reload();
    sender.sendMessage(ChatColor.GREEN + Translations.get("misc.configuration.reloaded", sender));
  }

  public static class LobbyParentCommand {
    @Command(
        aliases = {"lobby"},
        desc = "Lobby node command.")
    @NestedCommand(LobbyCommand.class)
    @CommandPermissions("lobby.node")
    public static void lobby(final CommandContext args, final CommandSender sender) {}
  }
}
