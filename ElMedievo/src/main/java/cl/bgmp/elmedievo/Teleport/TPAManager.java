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

  public Optional<TPA> getPlayerIncomingTPA(Player player) {
    return queue.stream().filter(tpa -> tpa.getPlayerTo().equals(player)).findFirst();
  }

  public Optional<TPA> getPlayerOutgoingTPA(Player player) {
    return queue.stream().filter(tpa -> tpa.getSender().equals(player)).findFirst();
  }
}
