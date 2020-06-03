package cl.bgmp.elmedievo.listeners;

import cl.bgmp.utilsbukkit.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class PlayerEvents implements Listener {

  private static String luckRune = "Pandasaurus_R";
  private static String speedRune = "Sugar_Cane_";

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    if (!player.hasPermission("elmedievo.chat.color")) return;

    event.setMessage(Chat.colourify(event.getMessage()));
  }

  //prevent players place custom runes
  @SuppressWarnings({"ConstantConditions", "deprecation"})
  private boolean ItemIsRune(final @NotNull ItemStack itemStack) {
    if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore())
      return false;
    if (itemStack.getItemMeta() instanceof SkullMeta) {
      final SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
      return skullMeta.getOwner().equals(luckRune) || skullMeta.getOwner().equals(speedRune);
    } else
      return false;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockPlace(BlockPlaceEvent event) {
    Player p = event.getPlayer();
    final PlayerInventory inventory = event.getPlayer().getInventory();
    if (!ItemIsRune(inventory.getItemInMainHand())
            && !ItemIsRune(inventory.getItemInOffHand()))
      return;
    event.setCancelled(true);
    p.sendMessage(ChatColor.RED + "¡No puedes poner runas en el suelo!");
  }
}
