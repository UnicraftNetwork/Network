package cl.bgmp.bungee;

import java.io.File;
import net.md_5.bungee.config.Configuration;

public class APIBungeeConfig implements Config {
  private Configuration config;
  private File datafolder;

  private String mysqlHost;
  private int mysqlPort;
  private String mysqlDatabase;
  private String mysqlUsername;
  private String mysqlPassword;
  private boolean isMySQLUsingSSL;

  private String minecraftUsersTable;
  private String usersTable;

  public APIBungeeConfig(Configuration config, File datafolder) {
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
