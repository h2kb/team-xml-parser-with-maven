package io.github.h2kb.parser;

import io.github.h2kb.databaseHandler.DatabaseHandler;
import io.github.h2kb.player.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ParserTeams extends Parser{

    private DatabaseHandler dbHandler;

    public ParserTeams(File dbProperties) throws IOException {
        dbHandler = new DatabaseHandler(dbProperties);
    }

    public void parseTeams(File file) throws SQLException, IOException, SAXException, ParserConfigurationException {
        Document dTeams = createDOM(file);
        NodeList teams = dTeams.getElementsByTagName("team");

        for (int i = 0; i < teams.getLength(); i++) {
            Node team = teams.item(i);

            if (team.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) team;

                String teamName = element.getElementsByTagName("name").item(0).getTextContent();
                NodeList playersList = element.getElementsByTagName("player");

                for (int j = 0; j < playersList.getLength(); j++) {
                    Node playerNode = playersList.item(j);

                    if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element playerDetail = (Element) playerNode;

                        String name = playerDetail.getElementsByTagName("name").item(0).getTextContent();
                        String surname = playerDetail.getElementsByTagName("surname").item(0).getTextContent();
                        int age = Integer.parseInt(playerDetail.getElementsByTagName("age").item(0).getTextContent());
                        String role = playerDetail.getElementsByTagName("role").item(0).getTextContent();

                        if (dbHandler.getRoleId(role) == -1) {
                            dbHandler.addRole(role);
                        }

                        if (dbHandler.getTeamId(teamName) == -1) {
                            dbHandler.addTeam(teamName);
                        }

                        Player player = new Player(name, surname, age, teamName, role);
                        dbHandler.addPlayer(player);

                        int playerId = dbHandler.getPlayerId(player.getName(), player.getSurname());
                        int teamId = dbHandler.getTeamId(teamName);

                        dbHandler.addPlayer2Team(playerId, teamId);
                    }
                }
            }
        }
    }
}
