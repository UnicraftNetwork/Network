package cl.bgmp.commons.Modules;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Config;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.TimeUtils.Time;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TipsModule extends Module {
  private Time interval = Config.Tips.getInterval();
  private String prefix = Config.Tips.getPrefix();
  private ImmutableList<String> messages = Config.Tips.getMessages();

  public TipsModule() {
    super(ModuleId.TIPS, Config.Tips.isEnabled());
  }

  @Override
  public void load() {
    if (enabled) {
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
