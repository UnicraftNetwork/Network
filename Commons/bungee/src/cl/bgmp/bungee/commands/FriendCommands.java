package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.APIBungee;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.friends.FriendRequest;
import cl.bgmp.bungee.friends.FriendRequestManager;
import cl.bgmp.bungee.users.user.UnlinkedUser;
import cl.bgmp.bungee.users.user.User;
import cl.bgmp.butils.chat.Chat;
import cl.bgmp.butils.chat.ChatUtils;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import cl.bgmp.minecraft.util.commands.annotations.NestedCommand;
import java.util.Optional;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendCommands {
  private final CommonsBungee commonsBungee;
  private final APIBungee api;
  private final FriendRequestManager frm;

  public FriendCommands(CommonsBungee commonsBungee, APIBungee api, FriendRequestManager frm) {
    this.commonsBungee = commonsBungee;
    this.api = api;
    this.frm = frm;
  }

  public static class FriendParentCommand {
    private final CommonsBungee commonsBungee;
    private final APIBungee api;
    private final FriendRequestManager frm;

    public FriendParentCommand(
        CommonsBungee commonsBungee, APIBungee api, FriendRequestManager frm) {
      this.commonsBungee = commonsBungee;
      this.api = api;
      this.frm = frm;
    }

    @Command(
        aliases = {"friend"},
        usage = "<add | remove | list | accept (nick) | deny (nick)>",
        desc = "Friends node command.",
        max = 2)
    @NestedCommand(FriendCommands.class)
    @CommandScopes("player")
    public void friend(final CommandContext args, final CommandSender sender) {
      ProxiedPlayer player = (ProxiedPlayer) sender;
      final Optional<User> user = this.api.getUserStore().getUserOf(player);
      if (!user.isPresent()) return;
      if (user.get() instanceof UnlinkedUser) {
        player.sendMessage(ChatColor.RED + "You do not have an account on the forums!");
        player.sendMessage(
            ChatColor.RED + "Sign up on " + ChatColor.AQUA + "https://unicraft.cl/register");
      }

      sender.sendMessage(
          ChatUtils.horizontalLineHeading(
              ChatColor.AQUA + "Friends", ChatColor.GRAY, ChatUtils.MAX_CHAT_WIDTH));
      sender.sendMessage(
          ChatColor.AQUA + Chat.ARROWS_RIGHT + ChatColor.GREEN + " /friend add <nick>");
      sender.sendMessage(
          ChatColor.AQUA + Chat.ARROWS_RIGHT + ChatColor.GREEN + " /friend remove <nick>");
      sender.sendMessage(
          ChatColor.AQUA + Chat.ARROWS_RIGHT + ChatColor.GREEN + " /friend accept <nick>");
      sender.sendMessage(
          ChatColor.AQUA + Chat.ARROWS_RIGHT + ChatColor.GREEN + " /friend deny <nick>");
    }
  }

  @Command(
      aliases = {"add"},
      desc = "Send a friend request.",
      usage = "<nick>",
      min = 1)
  @CommandScopes("player")
  public void add(final CommandContext args, CommandSender sender) {
    final String receiverNick = args.getString(0);
    final ProxiedPlayer receiver = this.commonsBungee.getProxy().getPlayer(receiverNick);

    final Optional<FriendRequest> friendRequest =
        this.frm.createNewFriendRequest((ProxiedPlayer) sender, receiver);
    if (!friendRequest.isPresent()) return; // All messages are handled by the FriendRequestManager.

    this.frm.handleSubmitRequest(friendRequest.get());
  }

  @Command(
      aliases = {"remove"},
      desc = "Remove a friend.",
      usage = "<nick>",
      min = 1)
  @CommandScopes("player")
  public void remove(final CommandContext args, CommandSender sender) {
    final String removedNick = args.getString(0);
    final ProxiedPlayer removedPlayer = this.commonsBungee.getProxy().getPlayer(removedNick);

    this.frm.handleRemoveFriend((ProxiedPlayer) sender, removedPlayer);
  }

  @Command(
      aliases = {"accept"},
      desc = "Accept a friend request.",
      usage = "<nick>",
      min = 1)
  @CommandScopes("player")
  public void accept(final CommandContext args, CommandSender sender) {
    this.frm.handleAcceptFriend((ProxiedPlayer) sender, args.getString(0));
  }

  @Command(
      aliases = {"deny"},
      desc = "Deny a friend request.",
      usage = "<nick>",
      min = 1)
  @CommandScopes("player")
  public void deny(final CommandContext args, CommandSender sender) {
    this.frm.handleDenyRequest((ProxiedPlayer) sender, args.getString(0));
  }
}
