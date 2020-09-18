package cl.bgmp.lobbyx.lobbygames;

import cl.bgmp.butils.entity.FireworkBuilder;
import cl.bgmp.butils.items.ItemBuilder;
import cl.bgmp.lobbyx.LobbyX;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import java.util.Map;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class TargetShootingGame extends LobbyGame {
  private LobbyX lobbyX;
  private Map<Integer, ItemStack> prizes =
      ImmutableMap.<Integer, ItemStack>builder()
          .put(1, new ItemBuilder(Material.GUNPOWDER).setName("&4Basura").build())
          .put(
              2,
              new ItemBuilder(Material.APPLE)
                  .setName("&4Manzana Confitada")
                  .setLore("&4¡&1Cuidado con &flos dientes&4!")
                  .enchant(Enchantment.DURABILITY, 10, true)
                  .build())
          .put(
              3,
              new ItemBuilder(Material.REDSTONE)
                  .setName("&4Mil pesos")
                  .setLore("&1¡&fPara comprar alguna cosita&1!")
                  .enchant(Enchantment.MENDING, 1, true)
                  .build())
          .put(
              4,
              new ItemBuilder(Material.BREAD)
                  .setName("&4Cho&1ri&fpan")
                  .setLore(
                      "&4La mejor longa del país",
                      "&1traida directamente de donde",
                      "&fmás te gusta. ¡Disfrutalo!")
                  .enchant(Enchantment.MENDING, 1, true)
                  .build())
          .put(
              5,
              new ItemBuilder(Material.SUNFLOWER)
                  .setName("&1¡&4Vale &fOtro!&1!")
                  .setLore("&4El mismísimo &1qlo &fcon suerte")
                  .enchant(Enchantment.MENDING, 1, true)
                  .build())
          .build();

  @Inject
  public TargetShootingGame(LobbyX lobbyX) {
    this.lobbyX = lobbyX;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerShootTarget(ProjectileHitEvent event) {
    final ProjectileSource shooter = event.getEntity().getShooter();
    if (!(shooter instanceof Player)) return;

    final Block hitBlock = event.getHitBlock();
    if (hitBlock == null) return;
    if (hitBlock.getType() != Material.TARGET) return;

    new BukkitRunnable() {
      @Override
      public void run() {
        if (!(hitBlock.getBlockData() instanceof AnaloguePowerable)) return;

        final Player player = (Player) shooter;
        final AnaloguePowerable powerable = (AnaloguePowerable) hitBlock.getBlockData();
        TargetShootingGame.this.givePrize(hitBlock, player, powerable.getPower());
      }
    }.runTaskLater(this.lobbyX, 1L);
  }

  private void givePrize(Block target, Player player, int score) {
    if (score < 3) return;
    FireworkBuilder builder = new FireworkBuilder();
    builder
        .setColor(Color.RED, Color.BLUE, Color.WHITE)
        .setFade(Color.RED, Color.BLUE, Color.WHITE)
        .setPower(1)
        .flicker()
        .trail()
        .spawn(target.getLocation().add(new Vector(0, 1.5, 0)));

    player.getInventory().addItem(prizes.get(score / 3));
  }
}
