package cl.bgmp.commons.modules;

import cl.bgmp.butils.chat.Chat;
import cl.bgmp.butils.time.SimpleDuration;
import cl.bgmp.commons.Commons;
import com.google.common.collect.ImmutableList;
import java.time.Duration;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TipsModule extends Module {
  private Duration interval =
      SimpleDuration.fromString(Commons.get().getConfiguration().getTipsInterval());
  private String prefix = Commons.get().getConfiguration().getTipsPrefix();
  private ImmutableList<String> messages =
      ImmutableList.copyOf(Commons.get().getConfiguration().getTips());
  private BukkitRunnable tipsTask = getNewTipsTask();
  private Logger logger;

  public TipsModule(Logger logger) {
    super(ModuleId.TIPS, Commons.get().getConfiguration().areTipsEnabled());
    this.logger = logger;
  }

  public void setInterval(Duration interval) {
    this.interval = interval;
  }

  public void setMessages(ImmutableList<String> messages) {
    this.messages = messages;
  }

  public void resetTipsTask() {
    this.tipsTask.cancel();
    this.tipsTask = getNewTipsTask();
  }

  private BukkitRunnable getNewTipsTask() {
    return new BukkitRunnable() {
      int count = 0;

      @Override
      public void run() {
        if (count == messages.size()) count = 0;
        Bukkit.broadcastMessage(Chat.color(prefix) + Chat.color(messages.get(count)));
        count++;
      }
    };
  }

  @Override
  public void load() {
    if (enabled) {
      if (messages.isEmpty()) {
        logger.warning("[Tip] Tips module is enabled, but no tips are declared in it.");
        logger.warning("[Tip] Check your config.yml file to add some tips, or disable the module.");
      } else
        this.tipsTask.runTaskTimer(
            Commons.get(), interval.getSeconds() * 20, interval.getSeconds() * 20);
    }
  }

  @Override
  public void unload() {
    setEnabled(Commons.get().getConfiguration().areTipsEnabled());
    setInterval(SimpleDuration.fromString(Commons.get().getConfiguration().getTipsInterval()));
    setMessages(ImmutableList.copyOf(Commons.get().getConfiguration().getTips()));

    resetTipsTask();
    Commons.get().unregisterEvents(this);
  }
}
