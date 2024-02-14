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
public class Config {

    private Integer columns;
    private Integer rows;
    private Map<String, Symbol> symbols;
    private Probabilities probabilities;
    @JsonProperty("win_combinations")
    private Map<String, WinCompType> winComp;


    @Getter
    @Setter
    public static class Symbol {
        @JsonProperty("reward_multiplier")
        private Double rewardMultiplier;
        private String type;
        private String impact;
        private Double extra;
    }

    @Getter
    @Setter
    public static class Probabilities{
        @JsonProperty("standard_symbols")
        private List<ProbSymbol> standardSymbolList;

        @JsonProperty("bonus_symbols")
        private ProbSymbol bonusSymbol;
    }
    @Getter
    @Setter
    public static class ProbSymbol{
        private Integer column;
        private Integer row;
        private Map<String, Integer> symbols;

    }
    @Getter
    @Setter
    public static class WinCompType{
        @JsonProperty("reward_multiplier")
        private Double rewardMultiplier;

        private String when;
        private Integer count;

        private String group;

        @JsonProperty("covered_areas")
        private String[][] coveredAreas;

        private String compName;

    }


}
