package io.github.h2kb.parser;

import io.github.h2kb.databaseHandler.DatabaseHandler;
import io.github.h2kb.player.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    private static Document createDOM(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        return dBuilder.parse(file);
    }

    private static void parseResults(Document dResults, HashMap<String, Integer> resultsOfGame) {
        NodeList results = dResults.getElementsByTagName("result");

        for (int i = 0; i < results.getLength(); i++) {
            Node result = results.item(i);

            if (result.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) result;

                String team = element.getElementsByTagName("team").item(0).getTextContent();
                int place = Integer.parseInt(element.getElementsByTagName("place").item(0).getTextContent());
                resultsOfGame.put(team, place);
            }
        }
    }

    private static void parseTeams(Document dTeams, ArrayList<Player> players) {
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

                        Player player = new Player(name, surname, age, teamName, role);
                        players.add(player);
                    }
                }
            }
        }
    }

    private static void generateResultXML(HashMap<String, Integer> resultOfGame, ArrayList<Player> players) {
        StringBuilder builder = new StringBuilder();

        for (HashMap.Entry<String, Integer> item : resultOfGame.entrySet()) {
            builder.append("<").append(item.getValue()).append(">:<").append(item.getKey()).append(">\n<");

            for (Player player : players) {
                if (player.getTeam().equals(item.getKey())) {
                    builder.append(player.getName()).append(" ").append(player.getSurname()).append(" ");
                }
            }
            builder.append(">\n");
        }

        System.out.println(builder.toString());
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, SQLException {
        HashMap<String, Integer> resultsOfGame = new HashMap<>();
        ArrayList<Player> players = new ArrayList<>();

        File results = new File("./src/xml/results.xml");
        File teams = new File("./src/xml/teams.xml");

        Document dResults = createDOM(results);
        Document dTeams = createDOM(teams);

//        parseResults(dResults, resultsOfGame);
//        parseTeams(dTeams, players);
//        generateResultXML(resultsOfGame, players);
        Connection dbConnection = DatabaseHandler.getDbConnection();
        String query = "SELECT * FROM books";
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
        }
    }
}
