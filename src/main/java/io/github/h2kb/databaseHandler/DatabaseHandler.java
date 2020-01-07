package io.github.h2kb.databaseHandler;

import io.github.h2kb.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseHandler {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public DatabaseHandler(File dbProperties) throws IOException {
        Properties properties = getConnectionData(dbProperties);

        dbUrl = properties.getProperty("db.url");
        dbUser = properties.getProperty("db.user");
        dbPassword = properties.getProperty("db.password");
    }

    private Properties getConnectionData(File dbProperties) throws IOException {
        Properties properties = new Properties();

        FileInputStream in = new FileInputStream(dbProperties);
        properties.load(in);

        return properties;
    }

    public Connection getDbConnection() throws SQLException {

        return DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
    }

    public void savePlayer(Player player, String teamName) throws SQLException {
        String insert = "INSERT INTO player(name, surname, age, role) VALUES(?, ?, ?, ?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
        preparedStatement.setString(1, player.getName());
        preparedStatement.setString(2, player.getSurname());
        preparedStatement.setInt(3, player.getAge());
        preparedStatement.setInt(4, getRoleId(player.getRole()));

        preparedStatement.executeUpdate();

        int playerId = getPlayerId(player.getName(), player.getSurname());
        int teamId = getTeamId(teamName);
        addPlayer2Team(playerId, teamId);
    }

    public void addPlayer(Player player, String teamName) throws SQLException {
        addRole(player.getRole());
        addTeam(teamName);
        savePlayer(player, teamName);
    }

    public int getPlayerId(String playerName, String playerSurname) throws SQLException {
        int playerId = -1;
        String select = "SELECT id FROM player WHERE name=? AND surname=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
        preparedStatement.setString(1, playerName);
        preparedStatement.setString(2, playerSurname);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            playerId = resultSet.getInt(1);
        }

        return playerId;
    }

    public void addTeam(String teamName) throws SQLException {
        if (getTeamId(teamName) == -1) {
            String insert = "INSERT INTO team(name) VALUES(?)";

            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, teamName);

            preparedStatement.executeUpdate();
        }
    }

    public int getTeamId(String teamName) throws SQLException {
        int teamId = -1;
        String select = "SELECT id FROM team WHERE name=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
        preparedStatement.setString(1, teamName);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            teamId = resultSet.getInt(1);
        }

        return teamId;
    }

    public void addRole(String role) throws SQLException {
        if (getRoleId(role) == -1) {
            String insert = "INSERT INTO role(role) VALUES(?)";

            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, role);

            preparedStatement.executeUpdate();
        }
    }

    public int getRoleId(String role) throws SQLException {
        int roleId = -1;
        String select = "SELECT id FROM role WHERE role=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
        preparedStatement.setString(1, role);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            roleId = resultSet.getInt(1);
        }

        return roleId;
    }

    public void addPlayer2Team(int playerId, int teamId) throws SQLException {
        String insert = "INSERT INTO player2team(player, team) VALUES(?, ?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
        preparedStatement.setInt(1, playerId);
        preparedStatement.setInt(2, teamId);

        preparedStatement.executeUpdate();
    }

    public void addResult(int teamId, int place) throws SQLException {
        String insert = "INSERT INTO result(team, place) VALUES(?, ?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
        preparedStatement.setInt(1, teamId);
        preparedStatement.setInt(2, place);

        preparedStatement.executeUpdate();
    }

    public String getResult() throws SQLException {
        StringBuilder builder = new StringBuilder();
        String select = "SELECT result.place, team.name FROM result INNER JOIN team ON result.team = team.id ORDER BY result.place";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String place = resultSet.getString(1);
            String teamName = resultSet.getString(2);
            String teamList = getTeamList(teamName);

            builder.append("Place ").append(place).append(": ").append(teamName).append("\n");
            builder.append(teamList);
        }

        return builder.toString();
    }

    public String getTeamList(String teamName) throws SQLException {
        StringBuilder builder = new StringBuilder().append("Team list:\n");
        String select = "SELECT name, surname FROM player WHERE id IN(SELECT player FROM player2team INNER JOIN team ON player2team.team = team.id AND name=?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
        preparedStatement.setString(1, teamName);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String playerName = resultSet.getString(1);
            String playerSurname = resultSet.getString(2);
            builder.append("\t").append(playerName).append(" ").append(playerSurname).append("\n");
        }

        return builder.toString();
    }
}
