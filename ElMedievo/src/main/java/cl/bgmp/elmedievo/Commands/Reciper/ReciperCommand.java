package cl.bgmp.elmedievo.Commands.Reciper;

import cl.bgmp.elmedievo.Commands.Reciper.Furnace.FurnaceCommand;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class ReciperCommand {
  @Command(
      aliases = {"reciper"},
      desc = "Reciper parent command.",
      min = 2,
      max = 2)
  @CommandPermissions("elmedievo.reciper")
  @NestedCommand({FurnaceCommand.FurnaceParentCommand.class})
  public static void reciper(final CommandContext args, final CommandSender sender)
      throws CommandException {}
}
