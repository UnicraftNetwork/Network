package cl.bgmp.bungee.friends;

import cl.bgmp.bungee.CommonsBungee;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FriendRequestManager {
  private CommonsBungee commonsBungee;
  private Set<FriendRequest> requests = new HashSet<>();

  public FriendRequestManager(CommonsBungee commonsBungee) {
    this.commonsBungee = commonsBungee;
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

  public void submitRequest(FriendRequest request) {
    if (request.getSender() != null && request.getReceiver() != null) {
      this.requests.add(request);
    } else {
      this.commonsBungee.getLogger().severe("Invalid friend request submission attempted.");
    }
  }

  public Set<FriendRequest> getRequestsFor(String username) {
    return this.requests.stream()
        .filter(r -> r.getReceiver().getNick().equals(username))
        .collect(Collectors.toSet());
  }

  public Set<FriendRequest> getRequestsFrom(String username) {
    return this.requests.stream()
        .filter(r -> r.getSender().getNick().equals(username))
        .collect(Collectors.toSet());
  }

  public Optional<FriendRequest> getRequest(String sender, String receiver) {
    return this.requests.stream()
        .filter(
            r ->
                r.getSender().getNick().equals(sender)
                    && r.getReceiver().getNick().equals(receiver))
        .findFirst();
  }
}
