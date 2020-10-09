package cl.bgmp.bungee;

import cl.bgmp.bungee.channels.ChannelsManager;
import cl.bgmp.bungee.channels.ECChannel;
import cl.bgmp.bungee.channels.EveryoneChannel;
import cl.bgmp.bungee.channels.RefChannel;
import cl.bgmp.bungee.channels.StaffChannel;
import cl.bgmp.bungee.commands.ChannelCommands;
import cl.bgmp.bungee.commands.HelpOPCommand;
import cl.bgmp.bungee.commands.LobbyCommand;
import cl.bgmp.bungee.commands.ServersCommand;
import cl.bgmp.bungee.commands.privatemessage.PrivateMessageCommands;
import cl.bgmp.bungee.injection.CommonsBungeeModule;
import cl.bgmp.bungee.listeners.PlayerEvents;
import cl.bgmp.bungee.privatemessages.PrivateMessagesManager;
import cl.bgmp.bungee.translations.AllTranslations;
import cl.bgmp.bungee.util.BungeeCommandsManager;
import cl.bgmp.bungee.util.CommandExecutor;
import cl.bgmp.bungee.util.CommandRegistration;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.ScopeMismatchException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
import cl.bgmp.minecraft.util.commands.injection.SimpleInjector;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.Arrays;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class CommonsBungee extends Plugin implements CommandExecutor<CommandSender> {
  private BungeeCommandsManager commandsManager;
  private CommandRegistration commandRegistration;

  private AllTranslations translations;
  private NetworkInfoProvider networkInfoProvider;
  private ChannelsManager channelsManager;
  private MultiResolver multiResolver;
  private PrivateMessagesManager privateMessagesManager;

  @Inject private StaffChannel staffChannel;
  @Inject private EveryoneChannel everyoneChannel;
  @Inject private ECChannel ecChannel;
  @Inject private RefChannel refChannel;

  @Override
  public void onEnable() {
    this.commandsManager = new BungeeCommandsManager();
    this.commandRegistration =
        new CommandRegistration(this, this.getProxy().getPluginManager(), commandsManager, this);

    this.translations = new AllTranslations();
    this.networkInfoProvider = new NetworkInfoProvider(this);
    this.channelsManager = new ChannelsManager(this);
    this.multiResolver = new MultiResolver(this);
    this.privateMessagesManager = new PrivateMessagesManager(this, this.multiResolver);

    this.inject();

    this.channelsManager.registerChannel(this.staffChannel);
    this.channelsManager.registerChannel(this.everyoneChannel);
    this.channelsManager.registerChannel(this.ecChannel);
    this.channelsManager.registerChannel(this.refChannel);

    this.registerCommands();
    this.registerEvents();
  }

  private void inject() {
    final CommonsBungeeModule module =
        new CommonsBungeeModule(
            this,
            this.translations,
            this.networkInfoProvider,
            this.channelsManager,
            this.multiResolver);
    final Injector injector = module.createInjector();

    injector.injectMembers(this);
    injector.injectMembers(this.translations);
    injector.injectMembers(this.networkInfoProvider);
    injector.injectMembers(this.channelsManager);
  }

  public void registerCommands() {
    this.registerCommand(HelpOPCommand.class, this, this.multiResolver);
    this.registerCommand(LobbyCommand.class, this.multiResolver);
    this.registerCommand(PrivateMessageCommands.class, this, this.privateMessagesManager);
    this.registerCommand(ServersCommand.class, this, this.networkInfoProvider, this.multiResolver);
    this.registerCommand(ChannelCommands.class, this.channelsManager);
  }

  private void registerCommand(Class<?> clazz, Object... toInject) {
    if (toInject.length > 0) {
      this.commandsManager.setInjector(new SimpleInjector(toInject));
    }

    this.commandRegistration.register(clazz);
  }

  @Inject private PlayerEvents playerEvents;

  public void registerEvents() {
    final PluginManager pm = this.getProxy().getPluginManager();
    pm.registerListener(this, this.playerEvents);
  }

  public void registerEvent(Listener listener) {
    this.getProxy().getPluginManager().registerListener(this, listener);
  }

  @Override
  public void onCommand(CommandSender sender, String commandName, String[] args) {
    try {
      this.commandsManager.execute(commandName, args, sender, sender);
    } catch (ScopeMismatchException exception) {
      String[] scopes = exception.getScopes();
      if (!Arrays.asList(scopes).contains("player")) {
        sender.sendMessage(
            new ComponentWrapper("You must execute this command via server console!")
                .color(ChatColor.RED)
                .build());
      } else {
        sender.sendMessage(
            new ComponentWrapper("You must be a player to execute this command!")
                .color(ChatColor.RED)
                .build());
      }
    } catch (CommandPermissionsException e) {
      sender.sendMessage(
          new ComponentWrapper(ChatConstant.NO_PERMISSION.getAsString())
              .color(ChatColor.RED)
              .build());
    } catch (MissingNestedCommandException e) {
      sender.sendMessage(new ComponentWrapper(e.getUsage()).color(ChatColor.RED).build());
    } catch (CommandUsageException e) {
      sender.sendMessage(new ComponentWrapper(e.getMessage()).color(ChatColor.RED).build());
      sender.sendMessage(new ComponentWrapper(e.getUsage()).color(ChatColor.RED).build());
    } catch (WrappedCommandException e) {
      if (e.getCause() instanceof NumberFormatException) {
        sender.sendMessage(
            new ComponentWrapper(ChatConstant.NUMBER_STRING_EXCEPTION.getAsString())
                .color(ChatColor.RED)
                .build());
      } else {
        sender.sendMessage(
            new ComponentWrapper(ChatConstant.ERROR.getAsString()).color(ChatColor.RED).build());
        e.printStackTrace();
      }
    } catch (CommandException e) {
      sender.sendMessage(new ComponentWrapper(e.getMessage()).color(ChatColor.RED).build());
    }
  }
}
