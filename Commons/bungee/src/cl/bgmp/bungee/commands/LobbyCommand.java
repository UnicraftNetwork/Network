package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.Util;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LobbyCommand {

  @Command(
      aliases = {"hub", "lobby", "main"},
      desc = "Teleport to the lobby",
      max = 0)
  @CommandScopes("player")
  public static void hub(final CommandContext args, CommandSender sender) {
    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final ServerInfo suitableLobby = Util.resolveSuitableLobby();

    if (suitableLobby == null) {
      sender.sendMessage(
          new ComponentWrapper(ChatConstant.NO_LOBBIES_AVAILABLE.getAsString())
              .color(ChatColor.RED)
              .build());
    } else {
      sender.sendMessage(
          new ComponentWrapper("[")
              .color(ChatColor.WHITE)
              .append(Util.resolveServerName(suitableLobby))
              .append("] ")
              .color(ChatColor.WHITE)
              .append(ChatConstant.TELEPORTING.getAsString())
              .color(ChatColor.DARK_PURPLE)
              .build());
      player.connect(suitableLobby);
    }
  }
}
