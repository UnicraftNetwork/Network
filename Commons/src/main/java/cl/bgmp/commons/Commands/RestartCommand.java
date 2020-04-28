package cl.bgmp.commons.Commands;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Modules.Module;
import cl.bgmp.commons.Modules.ModuleId;
import cl.bgmp.commons.Modules.RestartModule;
import cl.bgmp.utilsbukkit.TimeUtils.Time;
import cl.bgmp.utilsbukkit.TimeUtils.TimeUnit;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

// TODO: Translations
public class RestartCommand {
  private static void setNewRestart(
      CommandSender setter, RestartModule restartModule, Time interval) {
    restartModule.setInterval(interval);
    restartModule.runNewRestartTask();

    setter.sendMessage(
        ChatColor.AQUA
            + "Server will restart in "
            + ChatColor.DARK_RED
            + interval.getValue()
            + ChatColor.AQUA
            + " "
            + interval.getUnit().toString().toLowerCase());
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
      sender.sendMessage(ChatColor.RED + "Server restart module is currently disabled.");
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
      sender.sendMessage(ChatColor.RED + "'" + timeString + "' is not a valid time amount.");
    }
  }
}
