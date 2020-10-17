package cl.bgmp.commons.commands;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.modules.ChatFormatModule;
import cl.bgmp.commons.modules.Module;
import cl.bgmp.commons.modules.ModuleId;
import cl.bgmp.commons.modules.manager.ModuleManagerImpl;
import cl.bgmp.commons.translations.AllTranslations;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.NestedCommand;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommonsCommand {
  private Commons commons;
  private ModuleManagerImpl moduleManager;
  private AllTranslations translations;

  public CommonsCommand(
      Commons commons, ModuleManagerImpl moduleManager, AllTranslations translations) {
    this.commons = commons;
    this.moduleManager = moduleManager;
    this.translations = translations;
  }

  public static class CommonsParentCommand {
    @Command(
        aliases = {"commons"},
        desc = "Commons node command")
    @NestedCommand(value = CommonsCommand.class, executeBody = false)
    @CommandPermissions("commons.node")
    public static void commons(final CommandContext args, final CommandSender sender) {}
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

  @Command(
      aliases = {"chat", "chatformat"},
      desc = "Chat formatting command.",
      max = 1)
  @CommandPermissions("commons.chat.reload")
  public void chat(final CommandContext args, final CommandSender sender) {
    if (!args.getString(0).equalsIgnoreCase("reload")) return;

    final Optional<Module> module = this.moduleManager.getModule(ModuleId.CHAT_FORMAT);
    if (!module.isPresent()) {
      sender.sendMessage(
          ChatColor.RED + this.translations.get("module.chatformat.no.formatting", sender));
      return;
    }

    final ChatFormatModule chatFormatModule = (ChatFormatModule) module.get();
    chatFormatModule.refreshVault();
    chatFormatModule.reloadConfigValues();

    sender.sendMessage(
        ChatColor.GREEN + this.translations.get("module.chatformat.reload.success", sender));
  }

  @TabCompletion
  public static class CommonsCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(
        CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
      if (args.length == 1) {
        return Arrays.asList("reload", "chat");
      } else if (args.length == 2) {
        if (args[0].equalsIgnoreCase("chat")) {
          return Collections.singletonList("reload");
        }
      }

      return Collections.emptyList();
    }
  }
}
