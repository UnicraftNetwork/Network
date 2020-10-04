package cl.bgmp.commons.commands;

import cl.bgmp.butils.time.SimpleDuration;
import cl.bgmp.commons.modules.Module;
import cl.bgmp.commons.modules.ModuleId;
import cl.bgmp.commons.modules.RestartModule;
import cl.bgmp.commons.modules.manager.ModuleManagerImpl;
import cl.bgmp.commons.translations.AllTranslations;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import java.time.Duration;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RestartCommand {
  private ModuleManagerImpl moduleManager;
  private AllTranslations translations;

  public RestartCommand(ModuleManagerImpl moduleManager, AllTranslations translations) {
    this.moduleManager = moduleManager;
    this.translations = translations;
  }

  private void setNewRestart(CommandSender setter, RestartModule restartModule, Duration interval) {
    restartModule.setInterval(interval);
    restartModule.runNewRestartTask();
    setter.sendMessage(
        ChatColor.AQUA
            + this.translations.get(
                "module.restart.server.will.restart.in",
                setter,
                ChatColor.DARK_RED + String.valueOf(interval.getSeconds()) + ChatColor.AQUA,
                this.translations.get("time.unit.seconds", setter)));
  }

  @Command(
      aliases = {"queuerestart", "qrestart"},
      desc = "Command for scheduling server restarts.",
      usage = "<time>",
      min = 1)
  @CommandPermissions("commons.restart")
  public void restart(final CommandContext args, final CommandSender sender) {
    final Optional<Module> module = this.moduleManager.getModule(ModuleId.RESTART);
    if (!module.isPresent() || !module.get().isEnabled()) {
      sender.sendMessage(ChatColor.RED + this.translations.get("module.restart.disabled", sender));
      return;
    }

    final RestartModule restartModule = (RestartModule) module.get();
    final String timeString = args.getString(0);
    Duration time;

    // If a clean number is provided as argument, the command will try to default it to seconds
    try {
      final long seconds = Long.parseLong(timeString);
      time = Duration.ofSeconds(seconds);
      setNewRestart(sender, restartModule, time);
      return;
    } catch (NumberFormatException ignore) {
    }

    try {
      time = SimpleDuration.fromString(timeString);
      setNewRestart(sender, restartModule, time);
    } catch (Exception ignore) {
      sender.sendMessage(
          ChatColor.RED + this.translations.get("module.restart.invalid.time", sender, timeString));
    }
  }
}
