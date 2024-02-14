package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mohamed fawzy
 */



@ToString
public class WinDetailsObject {

   @Getter
   private Map<String, Set<Config.WinCompType>> winCompMap;
   @Setter
   @Getter
   private Double totalWinningAmount;

   @Getter
   @Setter
   private String bonusSymbol;

   public WinDetailsObject() {
      this.winCompMap = new HashMap<>();
      this.totalWinningAmount = 0.0;
   }

}
