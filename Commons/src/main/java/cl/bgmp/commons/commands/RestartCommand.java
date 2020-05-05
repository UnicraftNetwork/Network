package cl.bgmp.commons.commands;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.modules.Module;
import cl.bgmp.commons.modules.ModuleId;
import cl.bgmp.commons.modules.RestartModule;
import cl.bgmp.utilsbukkit.timeutils.Time;
import cl.bgmp.utilsbukkit.timeutils.TimeUnit;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RestartCommand {
  private static void setNewRestart(
      CommandSender setter, RestartModule restartModule, Time interval) {
    restartModule.setInterval(interval);
    restartModule.runNewRestartTask();
    setter.sendMessage(
        ChatColor.AQUA
            + Translations.get(
                "module.restart.server.will.restart.in",
                setter,
                ChatColor.DARK_RED + String.valueOf(interval.getValue()) + ChatColor.AQUA,
                interval.getUnit().toString().toLowerCase()));
  }

  @Command(
      aliases = {"queuerestart", "qrestart"},
      desc = "Command for scheduling server restarts.",
      usage = "<time>",
      min = 1)
  @CommandPermissions("commons.restart")
  public static void restart(final CommandContext args, final CommandSender sender) {
    final Module module = Commons.get().getModule(ModuleId.RESTART);
    if (module == null) {
      sender.sendMessage(ChatColor.RED + Translations.get("module.restart.disabled", sender));
      return;
    }

    final RestartModule restartModule = (RestartModule) module;
    final String timeString = args.getString(0);
    Time time;

    // If a clean number is provided as argument, the command will try to default it to seconds
    try {
      final long seconds = Long.parseLong(timeString);
      time = new Time(seconds, TimeUnit.SECONDS);
      setNewRestart(sender, restartModule, time);
      return;
    } catch (NumberFormatException ignore) {
    }

    try {
      time = Time.fromString(timeString);
      setNewRestart(sender, restartModule, time);
    } catch (Exception ignore) {
      sender.sendMessage(
          ChatColor.RED + Translations.get("module.restart.invalid.time", sender, timeString));
    }
  }
}
