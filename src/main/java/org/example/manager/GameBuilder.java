package org.example.manager;

import org.example.model.Config;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;

/**
 * @author mohamed fawzy
 */
public class GameBuilder {


    public String pickSymbolForColAndRow(Integer column, Integer row, Config config, Boolean isBonusSymbol){
        Config.ProbSymbol probSymbol = null;
        if(Boolean.FALSE.equals(isBonusSymbol)) {
            probSymbol = config.getProbabilities().getStandardSymbolList()
                .stream()
                .filter(probSymbolObj -> probSymbolObj.getColumn().equals(column) && probSymbolObj.getRow().equals(row))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find Probabilities for col " + column + " and row " + row));
        } else {
            probSymbol = config.getProbabilities().getBonusSymbol();
        }
        Double probSymbolSum = probSymbol.getSymbols().values().stream().map(val -> (double)val).reduce(0.0, Double::sum);
        int randomIndex = new Random().nextInt(probSymbolSum.intValue());
        int sum = 0;
        Map.Entry<String, Integer> choosedEntry = null;
        for(Map.Entry<String, Integer> entry: probSymbol.getSymbols().entrySet()){
            if(sum > randomIndex){
                break;
            }
            sum = sum + entry.getValue();
            choosedEntry = entry;
        }
        return choosedEntry != null ? choosedEntry.getKey() : probSymbol.getSymbols().entrySet().stream().findFirst().orElse(null).getKey();
    }

    public String[][] generateBettingMatrix(Config config){
        String [][] matrix;
        if(config.getColumns() != null && config.getRows() != null){
            matrix = new String[config.getRows()][config.getColumns()];
            //Create Matrix Based on col & Row
        } else {
            //Create Matrix Based on Provided "standard_symbols"
            Integer col = config.getProbabilities()
                .getStandardSymbolList()
                .stream()
                .max(Comparator.comparingInt(Config.ProbSymbol::getColumn))
                .orElseThrow(() -> new IllegalStateException("Cannot Get Column Size"))
                .getColumn() + 1;
            Integer row = config.getProbabilities()
                .getStandardSymbolList()
                .stream()
                .max(Comparator.comparingInt(Config.ProbSymbol::getRow))
                .orElseThrow(() -> new IllegalStateException("Cannot Get Row Size"))
                .getRow() + 1;
            matrix = new String[row][col];
        }
        for(int row = 0 ; row < matrix.length; row++){
            for(int col = 0 ; col<matrix[row].length; col++){
                matrix[row][col] = pickSymbolForColAndRow(col, row, config, Boolean.FALSE);
            }
        }
        addBonusSymbol(matrix,config);
        return matrix;
    }

    public void addBonusSymbol(String[][] matrix, Config config){
        Integer randomRow = new Random().nextInt(matrix.length-1);
        Integer randomCol = new Random().nextInt(matrix[randomRow].length-1);
        String bonusSymbol = pickSymbolForColAndRow(randomCol, randomRow, config, Boolean.TRUE);
        matrix[randomRow][randomCol] = bonusSymbol;
    }
}
