package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.ChatConstant;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.MultiResolver;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import java.util.Optional;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LobbyCommand {

  private final MultiResolver multiResolver;

  public LobbyCommand(MultiResolver multiResolver) {
    this.multiResolver = multiResolver;
  }

  @Command(
      aliases = {"lobby", "hug"},
      desc = "Teleport to the lobby",
      max = 0)
  @CommandScopes("player")
  public void lobby(final CommandContext args, CommandSender sender) {
    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final Optional<ServerInfo> suitableLobby = this.multiResolver.resolveSuitableLobby();

    if (!suitableLobby.isPresent()) {
      sender.sendMessage(
          new ComponentWrapper(ChatConstant.NO_LOBBIES_AVAILABLE.getAsString())
              .color(ChatColor.RED)
              .build());
    } else {
      sender.sendMessage(
          new ComponentWrapper("[")
              .color(ChatColor.WHITE)
              .append(this.multiResolver.getClickableNameOf(suitableLobby.get()))
              .append("] ")
              .color(ChatColor.WHITE)
              .append(ChatConstant.TELEPORTING.getAsString())
              .color(ChatColor.DARK_PURPLE)
              .build());
      player.connect(suitableLobby.get());
    }
  }
}
