package cl.bgmp.lobbyx.lobbygames;

import cl.bgmp.lobbyx.LobbyX;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.plugin.Plugin;

public class LobbyGamesManager {
  private Plugin plugin = LobbyX.get();
  private Set<LobbyGame> games = new HashSet<>();

  public LobbyGamesManager() {}

  public void registerGames(LobbyGame... lobbyGames) {
    games.addAll(Arrays.asList(lobbyGames));
    Arrays.stream(lobbyGames)
        .forEach(game -> plugin.getServer().getPluginManager().registerEvents(game, this.plugin));
  }
}
