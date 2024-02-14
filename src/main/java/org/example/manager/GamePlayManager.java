package org.example.manager;

import org.example.model.Config;
import org.example.model.WinDetailsObject;
import org.example.parser.ConfigParser;
import org.example.parser.ResultParser;

/**
 * @author mohamed fawzy
 */
public class GamePlayManager {

    private final ConfigParser configParser;

    private final GameBuilder gameBuilder;

    private final GameEvaluationManager gameEvaluationManager;

    private final ResultParser resultParser;

    public GamePlayManager(ConfigParser configParser, GameBuilder gameBuilder, GameEvaluationManager gameEvaluationManager, ResultParser resultParser) {
        this.configParser = configParser;
        this.gameBuilder = gameBuilder;
        this.gameEvaluationManager = gameEvaluationManager;
        this.resultParser = resultParser;
    }

    public String generateGameAndEvaluateScore(String configFilePath, String betting){
        Config config = configParser.parseGameConfigFile(configFilePath);
        String[][] bettingMatrix = gameBuilder.generateBettingMatrix(config);
        WinDetailsObject winDetailsObject = gameEvaluationManager.evaluateScore(bettingMatrix, config, Double.parseDouble(betting));
        return resultParser.winDetailsToJson(bettingMatrix, winDetailsObject);
    }


}
