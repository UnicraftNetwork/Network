package cl.bgmp.bungee;

import cl.bgmp.bungee.injection.APIBungeeModule;
import cl.bgmp.bungee.translations.AllTranslations;
import cl.bgmp.bungee.users.BungeeUserStore;
import cl.bgmp.bungee.util.BungeeCommandsManager;
import cl.bgmp.bungee.util.CommandExecutor;
import cl.bgmp.bungee.util.CommandRegistration;
import cl.bgmp.butils.mysql.MySQLConnector;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.ScopeMismatchException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
import cl.bgmp.minecraft.util.commands.injection.SimpleInjector;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.apache.commons.io.FileUtils;

public class APIBungee extends Plugin implements CommandExecutor<CommandSender> {
  private static APIBungee apiBungee;

  private BungeeCommandsManager commandsManager;
  private CommandRegistration commandRegistration;
  private APIBungeeConfig config;
  private AllTranslations translations;
  private MySQLConnector connector;

  @Inject private BungeeUserStore bungeeUserStore;

  public static APIBungee get() {
    return apiBungee;
  }

  public APIBungeeConfig getConfiguration() {
    return config;
  }

  public AllTranslations getTranslations() {
    return translations;
  }

  public MySQLConnector getDatabase() {
    return connector;
  }

  public BungeeUserStore getUserStore() {
    return bungeeUserStore;
  }

  @Override
  public void onEnable() {
    apiBungee = this;

    this.loadConfig();

    this.commandsManager = new BungeeCommandsManager();
    this.commandRegistration =
        new CommandRegistration(this, this.getProxy().getPluginManager(), commandsManager, this);
    this.translations = new AllTranslations();
    this.connector =
        new MySQLConnector(
            APIBungee.class,
            this.config.getMySQLHost(),
            this.config.getMySQLDatabase(),
            this.config.getMySQLUsername(),
            this.config.getMySQLPassword(),
            this.config.getMySQLPort(),
            this.config.isMySQLUsingSSL());
    this.connector.connect();

    this.inject();

    this.registerCommands();
  }

  private void loadConfig() {
    Configuration configuration = null;

    try {
      final File datafolder = this.getDataFolder();
      if (!datafolder.exists()) datafolder.mkdirs();

      final File configFile = new File(datafolder, "/config.yml");

      if (!configFile.exists()) {
        if (configFile.createNewFile()) {
          this.getLogger().info("No configuration file was found.");
          this.getLogger().info("Creating one...");

          final InputStream configAsStream = this.getResourceAsStream("config.yml");
          FileUtils.copyInputStreamToFile(configAsStream, configFile);
        } else {
          this.getLogger().severe("An error has occurred while loading config.yml.");
          this.getLogger().severe("Shutting down...");
          this.getProxy().stop();
        }
      }

      configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.config = new APIBungeeConfig(configuration, this.getDataFolder());
  }

  private void inject() {
    final APIBungeeModule module = new APIBungeeModule(this);
    final Injector injector = module.createInjector();

    injector.injectMembers(this);
    injector.injectMembers(this.translations);
    injector.injectMembers(this.config);
  }

  public void registerCommands() {}

  private void registerCommand(Class<?> clazz, Object... toInject) {
    if (toInject.length > 0) {
      this.commandsManager.setInjector(new SimpleInjector(toInject));
    }

    this.commandRegistration.register(clazz);
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
          new ComponentWrapper("You do not have permission.").color(ChatColor.RED).build());
    } catch (MissingNestedCommandException e) {
      sender.sendMessage(new ComponentWrapper(e.getUsage()).color(ChatColor.RED).build());
    } catch (CommandUsageException e) {
      sender.sendMessage(new ComponentWrapper(e.getMessage()).color(ChatColor.RED).build());
      sender.sendMessage(new ComponentWrapper(e.getUsage()).color(ChatColor.RED).build());
    } catch (WrappedCommandException e) {
      if (e.getCause() instanceof NumberFormatException) {
        sender.sendMessage(
            new ComponentWrapper("Expected a string, number received instead.")
                .color(ChatColor.RED)
                .build());
      } else {
        sender.sendMessage(
            new ComponentWrapper("An unknown error has occurred.").color(ChatColor.RED).build());
        e.printStackTrace();
      }
    } catch (CommandException e) {
      sender.sendMessage(new ComponentWrapper(e.getMessage()).color(ChatColor.RED).build());
    }
  }
}
