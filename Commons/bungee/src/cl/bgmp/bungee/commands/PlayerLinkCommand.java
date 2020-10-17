package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.APIBungee;
import cl.bgmp.bungee.users.user.LinkedUser;
import cl.bgmp.bungee.users.user.UnlinkedUser;
import cl.bgmp.bungee.users.user.User;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import java.util.Optional;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerLinkCommand {
  private final APIBungee api;

  public PlayerLinkCommand(APIBungee api) {
    this.api = api;
  }

  @Command(
      aliases = {"link"},
      desc = "Link your account with the forums.",
      usage = "<token>",
      min = 1,
      max = 1)
  @CommandScopes("player")
  public void link(final CommandContext args, CommandSender sender) {
    ProxiedPlayer player = (ProxiedPlayer) sender;
    Optional<User> user = this.api.getUserStore().getUserOf(player);
    if (!user.isPresent()) return;
    if (user.get() instanceof LinkedUser) {
      sender.sendMessage(ChatColor.RED + "You have already linked your account with the forums.");
      return;
    }

    UnlinkedUser unlinkedUser = (UnlinkedUser) user.get();
    String token = args.getString(0);

    if (!unlinkedUser.link(token)) {
      sender.sendMessage(ChatColor.RED + "Token inválido.");
      return;
    }

    sender.sendMessage(ChatColor.GREEN + "¡Has vinculado tu cuenta a la web satisfactoriamente!");
    sender.sendMessage(
        ChatColor.GREEN
            + "¡Ve a revisarla en "
            + ChatColor.AQUA
            + "https://unicraft.cl/"
            + ChatColor.GREEN
            + "!");
  }
}
