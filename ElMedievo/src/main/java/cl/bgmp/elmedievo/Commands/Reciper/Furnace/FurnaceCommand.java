package cl.bgmp.elmedievo.Commands.Reciper.Furnace;

import cl.bgmp.elmedievo.ElMedievo;
import cl.bgmp.elmedievo.Translations.ChatConstant;
import cl.bgmp.elmedievo.Translations.Translator;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class FurnaceCommand {
  @Command(
      aliases = {"reload"},
      desc = "Reloads all custom furnace recipes.",
      max = 0)
  @CommandPermissions("elmedievo.reciper.furnace.reload")
  public static void reload(final CommandContext args, final CommandSender sender)
      throws CommandException {
    ElMedievo.get().getFurnaceRecipesManager().reload();
    sender.sendMessage(
        ChatColor.GREEN + Translator.translate(sender, ChatConstant.RELOADED_FURNACE_RECIPES));
  }

  public static class FurnaceParentCommand {
    @Command(
        aliases = {"furnace"},
        desc = "Furnace reciper sub-command.",
        min = 1,
        max = 1)
    @CommandPermissions("elmedievo.reciper.furnace")
    @NestedCommand({FurnaceCommand.class})
    public static void furnace(final CommandContext args, final CommandSender sender)
        throws CommandException {}
  }
}
