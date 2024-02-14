package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author mohamed fawzy
 */
@Getter
@Setter
@Builder
public class GameResult {

    private String[][] matrix;
    private Double reward;

    @JsonProperty("applied_winning_combinations")
    private Map<String, List<String>> appliedWinningCombinations;

    @JsonProperty("applied_bonus_symbol")
    private String appliedBonusSymbol;

}
