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
import java.util.ArrayList;
import java.util.List;

public class ParserTeams extends Parser {

    private DatabaseHandler dbHandler;

    public ParserTeams(File dbProperties) throws IOException {
        dbHandler = new DatabaseHandler(dbProperties);
    }

    private List<Player> transformNodeListToPlayersList(NodeList nodeList) {
        List<Player> playersList = new ArrayList<Player>(nodeList.getLength());
        for (int j = 0; j < nodeList.getLength(); j++) {
            Element playerNode = (Element) nodeList.item(j);
            if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
                playersList.add(createPlayerFromElementNode(playerNode));
            }
        }

        return playersList;
    }

    private Player createPlayerFromElementNode(Element playerNode) {
        String name = getElementData(playerNode, "name");
        String surname = getElementData(playerNode, "surname");
        int age = Integer.parseInt(getElementData(playerNode, "age"));
        String role = getElementData(playerNode, "role");

        return new Player(name, surname, age, role);
    }

    public void parseTeams(File file) throws SQLException, IOException, SAXException, ParserConfigurationException {
        Document dTeams = createDOM(file);
        NodeList teams = dTeams.getElementsByTagName("team");

        for (int i = 0; i < teams.getLength(); i++) {
            Node team = teams.item(i);

            if (team.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) team;
                String teamName = getElementData(element, "name");
                NodeList nodeList = element.getElementsByTagName("player");
                List<Player> players = transformNodeListToPlayersList(nodeList);

                for (Player player : players) {
                    dbHandler.addPlayer(player, teamName);
                }
            }
        }
    }
}
