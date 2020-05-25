package cl.bgmp.commons.modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.timeutils.Time;
import com.google.common.collect.ImmutableList;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TipsModule extends Module {
  private Time interval = Config.Tips.getInterval();
  private String prefix = Config.Tips.getPrefix();
  private ImmutableList<String> messages = Config.Tips.getMessages();
  private Logger logger;

  public TipsModule(Logger logger) {
    super(ModuleId.TIPS, Config.Tips.isEnabled());
    this.logger = logger;
  }

  @Override
  public void load() {
    if (enabled) {

      if (messages.isEmpty()) {
        logger.warning("[Tip] Tips module is enabled, but no tips are declared in it.");
        logger.warning("[Tip] Check your config.yml file to add some tips, or disable the module.");
        return;
      }

      new BukkitRunnable() {
        int count = 0;

        @Override
        public void run() {
          if (count == messages.size()) count = 0;
          Bukkit.broadcastMessage(prefix + Chat.colourify(messages.get(count)));
          count++;
        }
      }.runTaskTimer(Commons.get(), interval.ticks(), interval.ticks());
    }
  }

  @Override
  public void unload() {}
}
