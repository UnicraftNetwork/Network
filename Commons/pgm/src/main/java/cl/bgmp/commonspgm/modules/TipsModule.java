package cl.bgmp.commonspgm.modules;

import cl.bgmp.butils.chat.Chat;
import cl.bgmp.butils.time.SimpleDuration;
import com.google.common.collect.ImmutableList;
import java.time.Duration;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TipsModule extends Module {
  private Duration interval;
  private String prefix;
  private ImmutableList<String> messages;
  private BukkitRunnable tipsTask;

  public TipsModule() {
    super(ModuleId.TIPS);
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
  public void configure() {
    this.interval = SimpleDuration.fromString(this.config.getTipsInterval());
    this.prefix = this.config.getTipsPrefix();
    this.messages = ImmutableList.copyOf(this.config.getTips());
    this.tipsTask = getNewTipsTask();
  }

  @Override
  public boolean isEnabled() {
    return this.config.areTipsEnabled();
  }

  @Override
  public void load() {
    if (this.isEnabled()) {
      if (messages.isEmpty()) {
        this.commons
            .getLogger()
            .warning("[Tip] Tips module is enabled, but no tips are declared in it.");
        this.commons
            .getLogger()
            .warning("[Tip] Check your config.yml file to add some tips, or disable the module.");
      } else
        this.tipsTask.runTaskTimer(
            this.commons, interval.getSeconds() * 20, interval.getSeconds() * 20);
    }
  }

  @Override
  public void unload() {
    this.setInterval(SimpleDuration.fromString(this.config.getTipsInterval()));
    this.setMessages(ImmutableList.copyOf(this.config.getTips()));

    this.resetTipsTask();
    this.commons.unregisterEvent(this);
  }
}
