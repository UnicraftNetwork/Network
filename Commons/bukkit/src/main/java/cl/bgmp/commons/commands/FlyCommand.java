package cl.bgmp.commons.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand {

  @Command(
      aliases = {"fly"},
      desc = "Allows a player to fly.",
      max = 0)
  @CommandPermissions("commons.fly")
  @CommandScopes("player")
  public static void fly(final CommandContext args, final CommandSender sender) {
    Player player = (Player) sender;
    player.setAllowFlight(!player.getAllowFlight());
    player.sendMessage(
        player.getAllowFlight()
            ? ChatColor.YELLOW + "Ahora puedes volar!"
            : ChatColor.YELLOW + "Ya no puedes volar!");
  }
}
