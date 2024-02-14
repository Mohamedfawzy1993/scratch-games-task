package org.example;

import lombok.SneakyThrows;
import org.example.manager.GameBuilder;
import org.example.manager.GameEvaluationManager;
import org.example.manager.GamePlayManager;
import org.example.parser.ConfigParser;
import org.example.parser.ResultParser;

public class Main {

    static GameBuilder gameBuilder = new GameBuilder();
    static GameEvaluationManager gameEvaluationManager = new GameEvaluationManager();

    static ResultParser resultParser = new ResultParser();
    static GamePlayManager gameManager = new GamePlayManager(new ConfigParser(), gameBuilder, gameEvaluationManager, resultParser);

    @SneakyThrows
    public static void main(String[] args) {
        String filePath = getNamedArg("--config", args);
        String bet = getNamedArg("--betting-amount", args);
        System.out.println(gameManager.generateGameAndEvaluateScore(filePath, bet));
    }

    public static String getNamedArg(String name, String[] args){
        for(int index = 0; index < args.length; index++){
            if(args[index].equals(name) && index + 1 != args.length && !args[index+1].startsWith("--")){
                return args[index+1];
            }
        }
        throw new IllegalArgumentException(name+" Value not Exist");
    }
}
