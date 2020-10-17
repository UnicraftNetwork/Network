package cl.bgmp.bungee.commands;

import cl.bgmp.bungee.APIBungee;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.friends.FriendRequest;
import cl.bgmp.bungee.friends.FriendRequestManager;
import cl.bgmp.bungee.users.user.LinkedUser;
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
    if (receiver == null) {
      sender.sendMessage(ChatColor.RED + "Player not found.");
      return;
    }

    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final Optional<User> senderUser = this.api.getUserStore().getUserOf(player);
    final Optional<User> receiverUser = this.api.getUserStore().getUserOf(receiver);

    if (!senderUser.isPresent()) {
      sender.sendMessage(ChatColor.RED + "Internal error.");
      return;
    }

    if (!receiverUser.isPresent()) {
      sender.sendMessage(ChatColor.RED + "Player not found.");
      return;
    }

    if ((receiverUser.get() instanceof UnlinkedUser)) {
      sender.sendMessage(
          ChatColor.AQUA
              + receiverNick
              + ChatColor.RED
              + " does not have an account on the forums!");
      return;
    }

    if ((senderUser.get() instanceof UnlinkedUser)) {
      sender.sendMessage(ChatColor.RED + "You do not have an account on the forums!");
      return;
    }

    LinkedUser senderLinkedUser = (LinkedUser) senderUser.get();
    LinkedUser receiverLinkedUser = (LinkedUser) receiverUser.get();

    /*
        if (senderLinkedUser.getNick().equals(receiverNick)) {
      sender.sendMessage(ChatColor.RED + "You may not friend yourself!");
      return;
    }
     */

    final Optional<FriendRequest> request =
        this.frm.getRequest(senderLinkedUser.getNick(), receiverNick);
    if (request.isPresent()) {
      sender.sendMessage(
          ChatColor.RED + "You are already requesting to be friends with this player.");
      return;
    }

    if (senderLinkedUser.getFriends().contains(receiverLinkedUser.getUUID())) {
      sender.sendMessage(
          ChatColor.AQUA
              + receiverLinkedUser.getNick()
              + ChatColor.RED
              + " already is your friend.");
      return;
    }

    this.frm.submitRequest(
        new FriendRequest(this.commonsBungee, this.api, senderLinkedUser, receiverLinkedUser));
    sender.sendMessage(
        ChatColor.GREEN
            + "Friend request sent to "
            + ChatColor.AQUA
            + receiverNick
            + ChatColor.GREEN
            + "!");
    sender.sendMessage(ChatColor.GREEN + "This request will expire in 2 minutes.");

    receiver.sendMessage(
        ChatColor.GREEN
            + "The user "
            + ChatColor.AQUA
            + player.getName()
            + ChatColor.GREEN
            + " is requesting to be your friend.");
    receiver.sendMessage(
        ChatColor.GREEN
            + "Use "
            + ChatColor.AQUA
            + "/friend accept "
            + player.getName()
            + ChatColor.GREEN
            + " to accept.");
    receiver.sendMessage(ChatColor.GREEN + "This request will expire in 2 minutes.");
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
    if (removedPlayer == null) {
      sender.sendMessage(ChatColor.RED + "Player not found.");
      return;
    }

    final ProxiedPlayer player = (ProxiedPlayer) sender;
    final Optional<User> removerUser = this.api.getUserStore().getUserOf(player);
    final Optional<User> removedUser = this.api.getUserStore().getUserOf(removedPlayer);

    if (!removerUser.isPresent()) {
      sender.sendMessage(ChatColor.RED + "Internal error.");
      return;
    }

    if (!removedUser.isPresent()) {
      sender.sendMessage(ChatColor.RED + "Player not found.");
      return;
    }

    if ((removerUser.get() instanceof UnlinkedUser)) {
      sender.sendMessage(ChatColor.RED + "You do not have an account on the forums!");
      return;
    }

    if ((removedUser.get() instanceof UnlinkedUser)) {
      sender.sendMessage(
          ChatColor.AQUA
              + removedNick
              + ChatColor.RED
              + " does not have an account on the forums!");
      return;
    }

    LinkedUser remover = (LinkedUser) removerUser.get();
    LinkedUser removed = (LinkedUser) removedUser.get();

    if (!remover.getFriends().contains(removed.getUUID())) {
      sender.sendMessage(
          ChatColor.AQUA + removed.getNick() + ChatColor.RED + " is not your friend.");
      return;
    }

    remover.removeFriend(removed);
    sender.sendMessage(
        ChatColor.RED
            + "You have removed "
            + ChatColor.AQUA
            + removedNick
            + ChatColor.RED
            + " as your friend!");

    removed.removeFriend(remover);
    removedPlayer.sendMessage(
        ChatColor.AQUA
            + remover.getNick()
            + ChatColor.RED
            + " has removed you from their friend list!");
  }

  @Command(
      aliases = {"accept"},
      desc = "Accept a friend request.",
      usage = "<nick>",
      min = 1)
  @CommandScopes("player")
  public void accept(final CommandContext args, CommandSender sender) {
    ProxiedPlayer requestReceiverPlayer = (ProxiedPlayer) sender;
    Optional<User> requestReceiver = this.api.getUserStore().getUserOf(requestReceiverPlayer);
    if (!requestReceiver.isPresent()) {
      sender.sendMessage(ChatColor.RED + "Player not found.");
      return;
    }

    String requestSender = args.getString(0);
    Optional<FriendRequest> friendRequest =
        frm.getRequest(requestSender, requestReceiver.get().getNick());
    if (!friendRequest.isPresent()) {
      sender.sendMessage(
          ChatColor.RED
              + "Could not find pending friend request from "
              + ChatColor.AQUA
              + requestSender
              + ChatColor.RED
              + ".");
      return;
    }

    friendRequest.get().accept();
    sender.sendMessage(
        ChatColor.GREEN + "You are now friends with " + ChatColor.AQUA + requestSender);

    ProxiedPlayer requestSenderPlayer = this.commonsBungee.getProxy().getPlayer(requestSender);
    if (requestSender == null) return;

    requestSenderPlayer.sendMessage(
        ChatColor.GREEN
            + "You are now friends with "
            + ChatColor.AQUA
            + requestReceiverPlayer.getName());
  }

  @Command(
      aliases = {"deny"},
      desc = "Deny a friend request.",
      usage = "<nick>",
      min = 1)
  @CommandScopes("player")
  public void deny(final CommandContext args, CommandSender sender) {
    ProxiedPlayer requestReceiverPlayer = (ProxiedPlayer) sender;
    Optional<User> requestReceiver = this.api.getUserStore().getUserOf(requestReceiverPlayer);
    if (!requestReceiver.isPresent()) {
      sender.sendMessage(ChatColor.RED + "Player not found.");
      return;
    }

    String requestSender = args.getString(0);
    Optional<FriendRequest> friendRequest =
        frm.getRequest(requestSender, requestReceiver.get().getNick());
    if (!friendRequest.isPresent()) {
      sender.sendMessage(
          ChatColor.RED
              + "Could not find pending friend request from "
              + ChatColor.AQUA
              + requestSender
              + ChatColor.RED
              + ".");
      return;
    }

    friendRequest.get().deny();
    sender.sendMessage(
        ChatColor.RED
            + "You have denied an incoming friend request from "
            + ChatColor.AQUA
            + requestSender);

    ProxiedPlayer requestSenderPlayer = this.commonsBungee.getProxy().getPlayer(requestSender);
    if (requestSender == null) return;

    requestSenderPlayer.sendMessage(
        ChatColor.AQUA
            + requestReceiverPlayer.getName()
            + ChatColor.RED
            + " has denied your friend request.");
  }
}
