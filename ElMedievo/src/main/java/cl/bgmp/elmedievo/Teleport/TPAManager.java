package cl.bgmp.elmedievo.Teleport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;

public class TPAManager {
  private List<TPA> queue = new ArrayList<>();

  public TPAManager() {}

  public List<TPA> getQueue() {
    return queue;
  }

  public void registerTPA(TPA tpa) {
    queue.add(tpa);
  }

  public void unregisterTPA(TPA tpa) {
    queue.remove(tpa);
  }

  public List<TPA> getAllPlayerOutgoingTPA(Player player) {
    return queue.stream()
        .filter(tpa -> tpa.getSender().equals(player))
        .collect(Collectors.toList());
  }

  public List<TPA> getAllPlayerIncomingTPA(Player player) {
    return queue.stream()
        .filter(tpa -> tpa.getPlayerTo().equals(player))
        .collect(Collectors.toList());
  }

  public Optional<TPA> getMatchingTPA(TPA tpa) {
    return queue.stream().filter(tpaMatch -> tpaMatch.equals(tpa)).findFirst();
  }

  /**
   * Retrieves the oldest TPA added to queue which matches the provided player as target
   *
   * @param target The player the TPA was sent to
   * @return The oldest TPA, which may or may not be present
   */
  public Optional<TPA> getPlayerIncomingTPA(Player target) {
    return queue.stream().filter(tpa -> tpa.getPlayerTo().equals(target)).findFirst();
  }

  /**
   * Retrieves a specific TPA match
   *
   * @param target The player the TPA in queue was sent to
   * @param sender The player who requested the TPA to the target
   * @return The specific TPA match, which may or may not be present
   */
  public Optional<TPA> getPlayerSpecificIncomingTPA(Player target, Player sender) {
    return queue.stream()
        .filter(tpa -> tpa.getPlayerTo().equals(target) && tpa.getSender().equals(sender))
        .findFirst();
  }

  public Optional<TPA> getPlayerOutgoingTPA(Player player) {
    return queue.stream().filter(tpa -> tpa.getSender().equals(player)).findFirst();
  }
}
