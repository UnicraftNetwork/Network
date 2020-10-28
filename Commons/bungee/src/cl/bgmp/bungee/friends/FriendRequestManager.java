package cl.bgmp.bungee.friends;

import cl.bgmp.bungee.APIBungee;
import cl.bgmp.bungee.CommonsBungee;
import cl.bgmp.bungee.ComponentWrapper;
import cl.bgmp.bungee.users.user.LinkedUser;
import cl.bgmp.bungee.users.user.UnlinkedUser;
import cl.bgmp.bungee.users.user.User;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendRequestManager {
  private CommonsBungee commonsBungee;
  private APIBungee api;
  private Set<FriendRequest> requests = new HashSet<>();

  public FriendRequestManager(CommonsBungee commonsBungee, APIBungee api) {
    this.commonsBungee = commonsBungee;
    this.api = api;
    this.commonsBungee
        .getProxy()
        .getScheduler()
        .schedule(
            this.commonsBungee,
            () -> this.requests.removeIf(FriendRequest::isExpired),
            1,
            5,
            TimeUnit.SECONDS);
  }

  /**
   * Handles the creation of a new {@link FriendRequest}.
   *
   * @param sender The friend request sender.
   * @param receiver The user who receives the request.
   * @return The new, to-be-submitted, friend request, which may or may not be present depending on
   *     if it could be created
   */
  public Optional<FriendRequest> createNewFriendRequest(
      ProxiedPlayer sender, ProxiedPlayer receiver) {
    if (receiver == null) {
      sender.sendMessage(new ComponentWrapper("Player not found.").color(ChatColor.RED).build());
      return Optional.empty();
    }

    final Optional<User> receiverUser = this.api.getUserStore().getUserOf(receiver);
    if (!receiverUser.isPresent()) {
      sender.sendMessage(new ComponentWrapper("Player not found.").color(ChatColor.RED).build());
      return Optional.empty();
    }

    final Optional<User> senderUser = this.api.getUserStore().getUserOf(sender);
    if (!senderUser.isPresent()) {
      sender.sendMessage(new ComponentWrapper("Internal error.").color(ChatColor.RED).build());
      return Optional.empty();
    }

    if (senderUser.get() instanceof UnlinkedUser) {
      sender.sendMessage(
          new ComponentWrapper("You do not have an account on the forums.")
              .color(ChatColor.RED)
              .build());
      return Optional.empty();
    }

    if (receiverUser.get() instanceof UnlinkedUser) {
      sender.sendMessage(
          new ComponentWrapper(receiver.getName())
              .color(ChatColor.AQUA)
              .append(" doesn't have an account on the forums.")
              .color(ChatColor.RED)
              .build());
      return Optional.empty();
    }

    if (sender.getName().equals(receiver.getName())) {
      sender.sendMessage(
          new ComponentWrapper("You may not friend yourself!").color(ChatColor.RED).build());
      return Optional.empty();
    }

    if (this.getRequest(senderUser.get().getNick(), receiver.getName()).isPresent()) {
      sender.sendMessage(
          new ComponentWrapper("You are already requesting to be friends with this player")
              .color(ChatColor.RED)
              .build());
      return Optional.empty();
    }

    final LinkedUser linkedSender = (LinkedUser) senderUser.get();
    final LinkedUser linkedReceiver = (LinkedUser) receiverUser.get();

    if (linkedSender.getFriends().contains(linkedReceiver.getUUID())) {
      sender.sendMessage(new ComponentWrapper("This player is already your friend!").build());
      return Optional.empty();
    }

    sender.sendMessage(
        ChatColor.GREEN
            + "Friend request sent to "
            + ChatColor.AQUA
            + receiver.getName()
            + ChatColor.GREEN
            + "!");
    sender.sendMessage(ChatColor.GREEN + "This request will expire in 2 minutes.");

    receiver.sendMessage(
        ChatColor.GREEN
            + "The user "
            + ChatColor.AQUA
            + sender.getName()
            + ChatColor.GREEN
            + " is requesting to be your friend.");
    receiver.sendMessage(
        ChatColor.GREEN
            + "Use "
            + ChatColor.AQUA
            + "/friend accept "
            + sender.getName()
            + ChatColor.GREEN
            + " to accept.");
    receiver.sendMessage(ChatColor.GREEN + "This request will expire in 2 minutes.");

    return Optional.of(
        new FriendRequest(this.commonsBungee, this.api, linkedSender, linkedReceiver));
  }

  /**
   * Handles the removal of a friend
   *
   * @param sender The player removing a friend
   * @param removed The player being removed as a friend
   */
  public void handleRemoveFriend(ProxiedPlayer sender, ProxiedPlayer removed) {
    final Optional<User> removerUser = this.api.getUserStore().getUserOf(sender);
    final Optional<User> removedUser = this.api.getUserStore().getUserOf(removed);

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
              + removed.getName()
              + ChatColor.RED
              + " does not have an account on the forums!");
      return;
    }

    LinkedUser remover = (LinkedUser) removerUser.get();
    LinkedUser linkedRemoved = (LinkedUser) removedUser.get();

    if (!remover.getFriends().contains(linkedRemoved.getUUID())) {
      sender.sendMessage(
          ChatColor.AQUA + removed.getName() + ChatColor.RED + " is not your friend.");
      return;
    }

    remover.removeFriend(linkedRemoved);
    sender.sendMessage(
        ChatColor.RED
            + "You have removed "
            + ChatColor.AQUA
            + removed.getName()
            + ChatColor.RED
            + " as your friend!");

    linkedRemoved.removeFriend(remover);
    removed.sendMessage(
        ChatColor.AQUA
            + remover.getNick()
            + ChatColor.RED
            + " has removed you from their friend list!");
  }

  /**
   * Handles the acceptance of a friend request between two players
   *
   * @param acceptingPlayer The player accepting the request-
   * @param requestingParty The player who has sent the friend request to the acceptingParty
   */
  public void handleAcceptFriend(ProxiedPlayer acceptingPlayer, String requestingParty) {
    Optional<User> acceptingUser = this.api.getUserStore().getUserOf(acceptingPlayer);
    if (!acceptingUser.isPresent()) {
      acceptingPlayer.sendMessage(ChatColor.RED + "Player not found.");
      return;
    }

    Optional<FriendRequest> friendRequest =
        this.getRequest(requestingParty, acceptingUser.get().getNick());
    if (!friendRequest.isPresent()) {
      acceptingPlayer.sendMessage(
          ChatColor.RED
              + "Could not find pending friend request from "
              + ChatColor.AQUA
              + requestingParty
              + ChatColor.RED
              + ".");
      return;
    }

    // Effectively accept the friend request in question
    friendRequest.get().accept();

    acceptingPlayer.sendMessage(
        ChatColor.GREEN + "You are now friends with " + ChatColor.AQUA + requestingParty);

    ProxiedPlayer requestingPlayer = this.commonsBungee.getProxy().getPlayer(requestingParty);
    if (requestingParty == null) return;
    requestingPlayer.sendMessage(
        ChatColor.GREEN + "You are now friends with " + ChatColor.AQUA + acceptingPlayer.getName());
  }

  public void handleSubmitRequest(FriendRequest request) {
    if (request.getSender() != null && request.getReceiver() != null) {
      this.requests.add(request);
      request.submit();
    } else {
      this.commonsBungee.getLogger().severe("Invalid friend request submission attempted.");
    }
  }

  /**
   * Handles a friend request which is being denied
   *
   * @param denyingPlayer The player denying ti
   * @param requestingParty The player who original requested to be friends with the other
   */
  public void handleDenyRequest(ProxiedPlayer denyingPlayer, String requestingParty) {
    ProxiedPlayer requestReceiverPlayer = (ProxiedPlayer) denyingPlayer;
    Optional<User> requestReceiver = this.api.getUserStore().getUserOf(requestReceiverPlayer);
    if (!requestReceiver.isPresent()) {
      denyingPlayer.sendMessage(ChatColor.RED + "Player not found.");
      return;
    }

    Optional<FriendRequest> friendRequest =
        this.getRequest(requestingParty, requestReceiver.get().getNick());
    if (!friendRequest.isPresent()) {
      denyingPlayer.sendMessage(
          ChatColor.RED
              + "Could not find pending friend request from "
              + ChatColor.AQUA
              + ChatColor.RED
              + ".");
      return;
    }

    friendRequest.get().deny();
    denyingPlayer.sendMessage(
        ChatColor.RED
            + "You have denied an incoming friend request from "
            + ChatColor.AQUA
            + requestingParty);

    ProxiedPlayer requestSenderPlayer = this.commonsBungee.getProxy().getPlayer(requestingParty);
    if (requestingParty == null) return;

    requestSenderPlayer.sendMessage(
        ChatColor.AQUA
            + requestReceiverPlayer.getName()
            + ChatColor.RED
            + " has denied your friend request.");
  }

  public Set<FriendRequest> getRequestsFor(String username) {
    return this.requests.stream()
        .filter(r -> r.getReceiver().getNick().equalsIgnoreCase(username))
        .collect(Collectors.toSet());
  }

  public Set<FriendRequest> getRequestsFrom(String username) {
    return this.requests.stream()
        .filter(r -> r.getSender().getNick().equalsIgnoreCase(username))
        .collect(Collectors.toSet());
  }

  public Optional<FriendRequest> getRequest(String sender, String receiver) {
    return this.requests.stream()
        .filter(
            r ->
                r.getSender().getNick().equalsIgnoreCase(sender)
                    && r.getReceiver().getNick().equalsIgnoreCase(receiver))
        .findFirst();
  }
}
