package org.example.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.model.Config;
import org.example.model.GameResult;
import org.example.model.WinDetailsObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mohamed fawzy
 */
public class ResultParser {

    @SneakyThrows
    public String winDetailsToJson(String[][] matrix, WinDetailsObject winDetailsObject) {
        GameResult gameResult = GameResult.builder()
            .matrix(matrix)
            .appliedWinningCombinations(new HashMap<>())
            .appliedBonusSymbol(winDetailsObject.getBonusSymbol())
            .reward(winDetailsObject.getTotalWinningAmount())
            .build();

        for (Map.Entry<String, Set<Config.WinCompType>> winCompEntrySet : winDetailsObject.getWinCompMap().entrySet()) {
            gameResult.getAppliedWinningCombinations()
                .put(winCompEntrySet.getKey(), winCompEntrySet.getValue().stream().map(Config.WinCompType::getCompName).collect(Collectors.toList()));
        }
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(gameResult);
    }
}
