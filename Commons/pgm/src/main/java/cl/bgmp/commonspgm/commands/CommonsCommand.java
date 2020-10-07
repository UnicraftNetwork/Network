package cl.bgmp.commonspgm.commands;

import cl.bgmp.commonspgm.CommonsPGM;
import cl.bgmp.commonspgm.modules.manager.ModuleManagerImpl;
import cl.bgmp.commonspgm.translations.AllTranslations;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.NestedCommand;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommonsCommand {
  private CommonsPGM commons;
  private ModuleManagerImpl moduleManager;
  private AllTranslations translations;

  public CommonsCommand(
      CommonsPGM commons, ModuleManagerImpl moduleManager, AllTranslations translations) {
    this.commons = commons;
    this.moduleManager = moduleManager;
    this.translations = translations;
  }

  @Command(
      aliases = {"reload"},
      desc = "Reloads Commons's configuration.",
      max = 0)
  @CommandPermissions("commons.reload")
  public void reload(final CommandContext args, final CommandSender sender) {
    this.commons.reloadConfig();
    this.moduleManager.reloadModules();
    sender.sendMessage(
        ChatColor.GREEN + this.translations.get("misc.configuration.reloaded", sender));
  }

  public static class CommonsParentCommand {
    @Command(
        aliases = {"commons"},
        desc = "Commons node command")
    @NestedCommand(value = CommonsCommand.class, executeBody = false)
    @CommandPermissions("commons.node")
    public static void commons(final CommandContext args, final CommandSender sender) {}
  }

  @TabCompletion
  public static class CommonsCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(
        CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
      if (args.length == 1) return Collections.singletonList("reload");

      return Collections.emptyList();
    }
  }
}
