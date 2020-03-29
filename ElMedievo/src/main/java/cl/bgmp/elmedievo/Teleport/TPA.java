package cl.bgmp.elmedievo.Teleport;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.Translations.ChatConstant;
import cl.bgmp.elmedievo.Translations.Translator;
import cl.bgmp.utilsbukkit.Chat;
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
            ChatConstant.TPA_RECEIVED
                .getTranslatedTo(playerTo.getLocale())
                .replace("{0}", sender.getDisplayName())
                .replace("-|", Chat.NEW_LINE)));
  }

  public void accept() {
    sender.teleport(playerTo.getLocation());
    unregister();

    playerTo.sendMessage(
        ChatColor.GREEN
            + Translator.translate(playerTo, ChatConstant.TPA_ACCEPT)
                .replace("{0}", sender.getDisplayName() + ChatColor.GREEN));
    sender.sendMessage(
        ChatColor.GREEN
            + ChatConstant.TPA_ACCEPTED
                .getTranslatedTo(sender.getLocale())
                .replace("{0}", playerTo.getDisplayName() + ChatColor.GREEN));
  }

  public void deny() {
    unregister();
    sender.sendMessage(
        ChatColor.RED
            + ChatConstant.TPA_DENIED
                .getTranslatedTo(sender.getLocale())
                .replace("{0}", playerTo.getDisplayName() + ChatColor.RED));
  }

  public void cancel() {
    unregister();
    playerTo.sendMessage(
        ChatColor.RED
            + ChatConstant.TPA_CANCELLED
                .getTranslatedTo(playerTo.getLocale())
                .replace("{0}", sender.getDisplayName() + ChatColor.RED));
  }

  public void expire() {
    unregister();
    sender.sendMessage(
        ChatColor.RED
            + ChatConstant.TPA_EXPIRED
                .getTranslatedTo(sender.getLocale())
                .replace("{0}", playerTo.getDisplayName() + ChatColor.RED));
  }

  public void register() {
    ElMedievo.get().getTpaManager().registerTPA(this);
  }

  public void unregister() {
    tpaTask.cancel();
    ElMedievo.get().getTpaManager().unregisterTPA(this);
  }
}
