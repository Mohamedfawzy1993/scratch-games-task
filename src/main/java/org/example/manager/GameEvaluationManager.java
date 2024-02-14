package org.example.manager;

import org.example.model.Config;
import org.example.model.WinDetailsObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mohamed fawzy
 */
public class GameEvaluationManager {

    public WinDetailsObject evaluateScore(String[][] matrix, Config config, Double betting) {
        Optional<WinDetailsObject> scoreResult;
        scoreResult = collectSameSymbolWin(matrix, config);
        scoreResult = collectLinerSymbolWin(matrix, config, scoreResult.get());
        Double totalWinning = 0.0;
        for (Map.Entry<String, Set<Config.WinCompType>> scoreEntrySet : scoreResult.get().getWinCompMap().entrySet()) {
            Double symbolRewardMultiplayer = config.getSymbols().get(scoreEntrySet.getKey()).getRewardMultiplier();
            Double sameSymbolReward = betting * symbolRewardMultiplayer;
            for (Config.WinCompType winCompType : scoreEntrySet.getValue()) {
                sameSymbolReward *= winCompType.getRewardMultiplier();
            }
            totalWinning += sameSymbolReward;
        }
        scoreResult.get().setTotalWinningAmount(addBonusReward(totalWinning, scoreResult.get()));
        return scoreResult.get();
    }

    public Optional<WinDetailsObject> collectSameSymbolWin(String[][] matrix, Config config) {

        Map<Integer, Config.WinCompType> winCompTypeMap = config.getWinComp().entrySet().stream()
            .map(entry -> {
                entry.getValue().setCompName(entry.getKey());
                return entry.getValue();
            })
            .filter(comp -> comp.getWhen().equals("same_symbols"))
            .collect(Collectors.toMap(Config.WinCompType::getCount, val -> val));

        Map<String, Integer> bonusSymbolKeySet = config.getProbabilities().getBonusSymbol().getSymbols();

        if (winCompTypeMap.isEmpty()) {
            return Optional.empty();
        }

        WinDetailsObject winDetailsObject = new WinDetailsObject();
        Map<String, Integer> symbolOccurrence = new HashMap<>();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                String currentSymbol = matrix[row][col];
                symbolOccurrence.merge(currentSymbol, 1, Integer::sum);
                if (bonusSymbolKeySet.containsKey(currentSymbol)) {
                    winDetailsObject.setBonusSymbol(currentSymbol);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : symbolOccurrence.entrySet()) {
            if (winCompTypeMap.containsKey(entry.getValue())) {
                Config.WinCompType winCompType = winCompTypeMap.get(entry.getValue());
                if (winDetailsObject.getWinCompMap().containsKey(entry.getKey())) {
                    winDetailsObject.getWinCompMap().get(entry.getKey()).add(winCompType);
                } else {
                    Set<Config.WinCompType> winCompTypeSet = new HashSet<>();
                    winCompTypeSet.add(winCompType);
                    winDetailsObject.getWinCompMap().put(entry.getKey(), winCompTypeSet);
                }
            }
        }
        return Optional.of(winDetailsObject);
    }

    public Optional<WinDetailsObject> collectLinerSymbolWin(String[][] matrix, Config config, WinDetailsObject winDetailsObject) {

        List<Config.WinCompType> winCompTypeList = config.getWinComp().entrySet().stream()
            .map(entry -> {
                entry.getValue().setCompName(entry.getKey());
                return entry.getValue();
            })
            .filter(comp -> comp.getWhen().equals("linear_symbols"))
            .collect(Collectors.toList());

        if (winCompTypeList.isEmpty()) {
            return Optional.empty();
        }

        if (winDetailsObject == null) {
            winDetailsObject = new WinDetailsObject();
        }

        for (Config.WinCompType winCompType : winCompTypeList) {
            for (int compRow = 0; compRow < winCompType.getCoveredAreas().length; compRow++) {
                Map<String, Integer> characterCountMap = new HashMap<>();
                for (int compCol = 0; compCol < winCompType.getCoveredAreas()[compRow].length; compCol++) {
                    String[] matrixDimension = winCompType.getCoveredAreas()[compRow][compCol].split(":");
                    String character = matrix[Integer.parseInt(matrixDimension[0])][Integer.parseInt(matrixDimension[1])];
                    characterCountMap.merge(character, 1, Integer::sum);
                }
                if (characterCountMap.size() != 1) {
                    continue;
                }
                Map.Entry<String, Integer> resultEntry = characterCountMap.entrySet().stream().findFirst().orElse(null);
                if (winDetailsObject.getWinCompMap().containsKey(resultEntry.getKey())) {
                    winDetailsObject.getWinCompMap().get(resultEntry.getKey()).add(winCompType);
                } else {
                    Set<Config.WinCompType> winCompTypeSet = new HashSet<>();
                    winCompTypeSet.add(winCompType);
                    winDetailsObject.getWinCompMap().put(resultEntry.getKey(), winCompTypeSet);
                }
            }
        }
        return Optional.of(winDetailsObject);
    }

    public Double addBonusReward(Double totalWinningWithoutReward, WinDetailsObject winDetailsObject) {
        if (totalWinningWithoutReward.equals(0.0) || winDetailsObject.getBonusSymbol().equals("MISS")) {
            return totalWinningWithoutReward;
        } else if (winDetailsObject.getBonusSymbol().contains("x")) {
            Double rewardValue = Double.valueOf(winDetailsObject.getBonusSymbol().replace("x", ""));
            return totalWinningWithoutReward * rewardValue;
        } else {
            Double rewardValue = Double.valueOf(winDetailsObject.getBonusSymbol().replace("+", ""));
            return totalWinningWithoutReward + rewardValue;
        }
    }
}
