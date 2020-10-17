package cl.bgmp.api;

public interface Config {

  String getMySQLHost();

  int getMySQLPort();

  String getMySQLDatabase();

  String getMySQLUsername();

  String getMySQLPassword();

  boolean isMySQLUsingSSL();

  String getMinecraftUsersTable();

  String getUsersTable();
}
