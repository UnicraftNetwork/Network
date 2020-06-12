package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.FlashComponent;
import cl.bgmp.bungee.Util;
import com.sk89q.minecraft.util.commands.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LobbyCommand {

  @Command(
      aliases = {"hub", "lobby", "main"},
      desc = "Teleport to the lobby",
      max = 0)
  public static void hub(final CommandContext args, CommandSender sender) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(
          new FlashComponent(ChatConstant.NO_CONSOLE.getAsString()).color(ChatColor.RED).build());
      return;
    }

    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final ServerInfo suitableLobby = Util.resolveSuitableLobby();

    if (suitableLobby == null) {
      sender.sendMessage(
          new FlashComponent(ChatConstant.NO_LOBBIES_AVAILABLE.getAsString())
              .color(ChatColor.RED)
              .build());
    } else {
      sender.sendMessage(
          new FlashComponent("[")
              .color(ChatColor.WHITE)
              .append(Util.resolveServerName(suitableLobby))
              .append("] ")
              .color(ChatColor.WHITE)
              .append(ChatConstant.TELEPORTING.getAsString())
              .color(ChatColor.YELLOW)
              .build());
      player.connect(suitableLobby);
    }
  }
}
