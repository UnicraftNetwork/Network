package cl.bgmp.elmedievo.listeners;

import cl.bgmp.elmedievo.Config;
import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.sound.SoundManager;
import cl.bgmp.utilsbukkit.timeutils.Time;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EventManager implements Listener {
  private boolean initialPhase = Config.Event.inInitialPhase();
  private boolean registering = true;
  private Time gracePeriod = Config.Event.getGracePeriod();
  private List<String> participants = Config.Event.getParticipants();
  private List<String> onlineParticipants = new ArrayList<>();
  private boolean withinGracePeriod = !initialPhase && registering;

  public EventManager() {
    ElMedievo.get().registerEvents(this);
    if (!initialPhase) handleNoShowsIn(gracePeriod);
    scheduleWithinGracePeriodEventAlerts();
  }

  @EventHandler
  public void notifyGracePeriod(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (participants.contains(player.getName()) && withinGracePeriod) {
      player.sendMessage(
          ChatColor.GREEN
              + "=> Te has unido correctamente dentro del periodo de gracia ("
              + gracePeriod.toEffectiveLocalizedString(player)
              + ") del evento.");
      player.sendMessage(ChatColor.GREEN + "=> No te desconectes!");
    }
  }

  @EventHandler
  public void handleRegistry(PlayerJoinEvent event) {
    if (initialPhase) {
      Player player = event.getPlayer();
      Config.Event.addParticipant(player);
      participants = Config.Event.getParticipants();
    }
  }

  @EventHandler
  public void handleParticipantJoin(PlayerJoinEvent event) {
    if (withinGracePeriod && participants.contains(event.getPlayer().getName())) {
      onlineParticipants.add(event.getPlayer().getName());
    }
  }

  @EventHandler
  public void handleParticipantQuit(PlayerQuitEvent event) {
    if (withinGracePeriod && participants.contains(event.getPlayer().getName())) {
      onlineParticipants.remove(event.getPlayer().getName());
    }
  }

  @EventHandler
  public void handleForfeit(PlayerQuitEvent event) {
    if (!withinGracePeriod) participants.remove(event.getPlayer().getName());
  }

  @EventHandler
  public void handleParticipantDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    if (!withinGracePeriod) {
      event.setCancelled(true);
      player.setHealth(20);
      participants.remove(player.getName());

      SoundManager.playSoundAtPlayer(player, Sound.ENTITY_BLAZE_DEATH);
      player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "=> HAS MUERTO! :(");
      applySpectatorTo(player);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void handleSpectators(PlayerJoinEvent event) {
    if (initialPhase) return;

    Player player = event.getPlayer();
    if (participants.contains(player.getName())) return;

    applySpectatorTo(player);
  }

  public void scheduleWithinGracePeriodEventAlerts() {
    new BukkitRunnable() {
      @Override
      public void run() {
        if (!withinGracePeriod) {
          this.cancel();
          return;
        }

        Bukkit.broadcastMessage(ChatColor.AQUA + "=> El evento de destrucción comenzará pronto!");
        SoundManager.playSoundAtPlayers(Sound.BLOCK_NOTE_BLOCK_PLING, Bukkit.getOnlinePlayers());
      }
    }.runTaskTimer(ElMedievo.get(), 0L, Time.fromString("2m").ticks());
  }

  public void handleNoShowsIn(Time time) {
    new BukkitRunnable() {
      @Override
      public void run() {
        handleNoShows();
        registering = false;
        withinGracePeriod = false;
        Bukkit.broadcastMessage(
            ChatColor.RED.toString() + ChatColor.BOLD + "=> EL EVENTO HA COMENZADO!");
        SoundManager.playSoundAtPlayers(Sound.ENTITY_WITHER_DEATH, Bukkit.getOnlinePlayers());
      }
    }.runTaskLater(ElMedievo.get(), time.ticks());
  }

  private void handleNoShows() {
    participants.clear();
    participants.addAll(onlineParticipants);
  }

  public void applySpectatorTo(Player player) {
    player.setGameMode(GameMode.SPECTATOR);
    player.addAttachment(ElMedievo.get(), "minecraft.command.teleport", true);
    player.sendMessage(ChatColor.GREEN + "=> Estás observando el evento de destrucción de Towny!");
    player.sendMessage(
        ChatColor.GREEN
            + "=> Puedes usar "
            + ChatColor.AQUA
            + "/tp "
            + ChatColor.GREEN
            + "para teletransportarte a quien desees.");
  }
}
