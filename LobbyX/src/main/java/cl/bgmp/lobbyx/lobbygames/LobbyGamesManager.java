package cl.bgmp.lobbyx.lobbygames;

import cl.bgmp.lobbyx.LobbyX;
import com.google.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LobbyGamesManager {
  private LobbyX lobbyX;
  private Set<LobbyGame> games = new HashSet<>();

  @Inject
  public LobbyGamesManager(LobbyX lobbyX) {
    this.lobbyX = lobbyX;
  }

  public void registerGames(LobbyGame... lobbyGames) {
    games.addAll(Arrays.asList(lobbyGames));
    Arrays.stream(lobbyGames)
        .forEach(game -> lobbyX.getServer().getPluginManager().registerEvents(game, this.lobbyX));
  }
}
