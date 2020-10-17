package cl.bgmp.api;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class APIBukkitConfig implements Config {
  private FileConfiguration config;
  private File datafolder;

  private String mysqlHost;
  private int mysqlPort;
  private String mysqlDatabase;
  private String mysqlUsername;
  private String mysqlPassword;
  private boolean isMySQLUsingSSL;

  private String minecraftUsersTable;
  private String usersTable;

  public APIBukkitConfig(FileConfiguration config, File datafolder) {
    this.config = config;
    this.datafolder = datafolder;

    this.mysqlHost = this.config.getString("mysql.host");
    this.mysqlPort = this.config.getInt("mysql.port");
    this.mysqlDatabase = this.config.getString("mysql.database");
    this.mysqlUsername = this.config.getString("mysql.username");
    this.mysqlPassword = this.config.getString("mysql.password");
    this.isMySQLUsingSSL = this.config.getBoolean("mysql.ssl");

    this.minecraftUsersTable = this.config.getString("mysql.tables.minecraft-users");
    this.usersTable = this.config.getString("mysql.tables.users");
  }

  @Override
  public String getMySQLHost() {
    return this.mysqlHost;
  }

  @Override
  public int getMySQLPort() {
    return this.mysqlPort;
  }

  @Override
  public String getMySQLDatabase() {
    return this.mysqlDatabase;
  }

  @Override
  public String getMySQLUsername() {
    return this.mysqlUsername;
  }

  @Override
  public String getMySQLPassword() {
    return this.mysqlPassword;
  }

  @Override
  public boolean isMySQLUsingSSL() {
    return this.isMySQLUsingSSL;
  }

  @Override
  public String getMinecraftUsersTable() {
    return this.minecraftUsersTable;
  }

  @Override
  public String getUsersTable() {
    return this.usersTable;
  }
}
