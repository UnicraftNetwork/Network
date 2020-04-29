package cl.bgmp.elmedievo.Teleport;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Translations.Translations;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TPA {
  private Player sender;
  private Player playerTo;
  private long expiry = 2400L;
  private TPATask tpaTask;

  public TPA(Player sender, Player playerTo) {
    this.sender = sender;
    this.playerTo = playerTo;
    this.tpaTask = new TPATask(this);
  }

  public Player getSender() {
    return sender;
  }

  public Player getPlayerTo() {
    return playerTo;
  }

  public long getExpiry() {
    return expiry;
  }

  public void send() {
    register();
    playerTo.sendMessage(
        Chat.colourify(
            Translations.get(
                "tpa.received", playerTo, sender.getDisplayName(), Chat.NEW_LINE, Chat.NEW_LINE)));
  }

  public void accept() {
    sender.teleport(playerTo.getLocation());
    unregister();

    playerTo.sendMessage(
        ChatColor.GREEN
            + Translations.get("tpa.accept", playerTo, sender.getDisplayName() + ChatColor.GREEN));
    sender.sendMessage(
        ChatColor.GREEN
            + Translations.get(
                "tpa.accepted", sender, playerTo.getDisplayName() + ChatColor.GREEN));
  }

  public void deny() {
    unregister();
    sender.sendMessage(
        ChatColor.RED
            + Translations.get("tpa.denied", sender, playerTo.getDisplayName() + ChatColor.RED));
  }

  public void cancel() {
    unregister();
    playerTo.sendMessage(
        ChatColor.RED
            + Translations.get("tpa.cancelled", playerTo, sender.getDisplayName() + ChatColor.RED));
  }

  public void expire() {
    unregister();
    sender.sendMessage(
        ChatColor.RED
            + Translations.get("tpa.expired", sender, playerTo.getDisplayName() + ChatColor.RED));
  }

  public void register() {
    ElMedievo.get().getTpaManager().registerTPA(this);
  }

  public void unregister() {
    tpaTask.cancel();
    ElMedievo.get().getTpaManager().unregisterTPA(this);
  }
}
