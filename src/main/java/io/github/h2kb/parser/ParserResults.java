package io.github.h2kb.parser;

import io.github.h2kb.databaseHandler.DatabaseHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ParserResults extends Parser{

    private DatabaseHandler dbHandler;

    public ParserResults(File dbProperties) throws IOException {
        dbHandler = new DatabaseHandler(dbProperties);
    }

    public void parseResults(File file) throws SQLException, IOException, SAXException, ParserConfigurationException {
        Document dResults = createDOM(file);
        NodeList results = dResults.getElementsByTagName("result");

        for (int i = 0; i < results.getLength(); i++) {
            Element result = (Element) results.item(i);

            if (result.getNodeType() == Node.ELEMENT_NODE) {
                String teamName = getElementData(result, "team");
                int teamId = dbHandler.getTeamId(teamName);
                int place = Integer.parseInt(getElementData(result, "place"));

                dbHandler.addResult(teamId, place);
            }
        }
    }
}
