package participant.Entries;

import participant.*;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.LogRecord;
import java.util.HashSet;
import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.Map;
import java.util.HashMap;
import java.util.List;



import util.*;

public class GoalPlayer extends PlayerLogic {
    GameLogger GL;
    private Set<Goal> goals = null;
    private boolean achievedCardReward = false;
    private double[][] winPctGrid = {
        {0.417 ,0.106 ,0.027 ,0.007 ,0.002 ,0.000 ,0.000 ,0.000 ,0.000 ,0.000},
        {0.754 ,0.363 ,0.206 ,0.091, 0.049, 0.021, 0.011, 0.005, 0.003, 0.001},
        {0.916 ,0.656 ,0.470 ,0.315 ,0.206 ,0.134 ,0.084 ,0.054 ,0.033, 0.021},
        {0.972 ,0.785 ,0.642 ,0.477 ,0.359 ,0.253 ,0.181 ,0.123 ,0.086, 0.057},
        {0.990 ,0.890 ,0.769 ,0.638 ,0.506 ,0.397 ,0.297 ,0.224 ,0.162 ,0.118},
        {0.997 ,0.934 ,0.857 ,0.745 ,0.638 ,0.521 ,0.423 ,0.329 ,0.258 ,0.193},
        {0.999 ,0.967 ,0.910 ,0.834 ,0.736 ,0.640 ,0.536 ,0.446 ,0.357 ,0.287},
        {1.000 ,0.980 ,0.947 ,0.888 ,0.818 ,0.730 ,0.643 ,0.547 ,0.464 ,0.380},
        {1.000 ,0.990 ,0.967 ,0.930 ,0.873 ,0.808 ,0.726 ,0.646 ,0.558 ,0.480},
        {1.000 ,0.994 ,0.981 ,0.954 ,0.916 ,0.861 ,0.800 ,0.724 ,0.650 ,0.568}
        };
    public GoalPlayer(){
        super();
    }

    public class Goal {
        public double getDeployScore(Snapshot s, DeployCommand dc) {
            return 0.0;
        }
        public double getAttackScore(Snapshot s, AttackCommand ac) {
            return 0.0;
        }
        public double getMoveAfterConquerScore(Snapshot s, Province attackingProvince, Province defendingProvince, int numTroops) {
            return 0.0;
        }
        public double getMoveScore(Snapshot s, MoveCommand mc) {
            return 0.0;
        }
        public double getTurnInScore(Snapshot s, boolean turnIn) {
            return 0.0;
        }
    }

    public class CardRewardGoal extends Goal {
        public double getAttackScore(Snapshot s, AttackCommand ac) {
            if(!achievedCardReward){
                return 10.0 * winPct(ac.getnumAttackingTroops(), ac.getDefendingProvince().getNumSoldiers());
            }
            return 0.0;
        }
        public double getTurnInScore(Snapshot s, boolean turnIn) {
            if(turnIn){
                return -30.0;
            }
            return 0.0;
        }
    }

    public class ControlContinentGoal extends Goal {
        private Continent c;
        private Set<Province> provinces;
        private Set<Province> provincesPlus;
        private Set<Province> adjacentProvinces;
        private Set<Province> minHold;
        private int minHoldSize;
        private int troopsPerTurn;
        private double value;
        public ControlContinentGoal(Continent c){
            this.c = c;
            provinces = c.getProvinces();
            adjacentProvinces = c.calculateAdjacent();
            //Calculate minimum number of provinces to hold to control continent
            minHold = new HashSet<Province>();
            for (Province p : provinces){
                // Include provinces adjacent to adjacentProvinces
                a: for (Province adj : p.getAdjacent()){
                    if (adjacentProvinces.contains(adj)){
                        minHold.add(p);
                        break a;
                    }
                }
            }
            Map<Province, Integer> soloConnections = new HashMap<Province, Integer>(); 
            HashSet<Province> soloConnected = new HashSet<Province>();
            for (Province p : minHold){
                int connections = 0;
                Province solo = null;
                for (Province adj : p.getAdjacent()){
                    if (adjacentProvinces.contains(adj)){
                        connections += 1;
                        solo = adj;
                    }
                }
                if (connections == 1){
                    soloConnections.put(solo, soloConnections.getOrDefault(solo, 0) + 1);
                    soloConnected.add(p);
                }
            }
            for (Province p : soloConnections.keySet()){
                if (soloConnections.get(p) > 1){
                    minHold.add(p);
                    // remove provinces solo connected to this province
                    for (Province adj : p.getAdjacent()){
                        if (soloConnected.contains(adj)){
                            minHold.remove(adj);
                        }
                    }
                }
            }
            minHoldSize = minHold.size();
            troopsPerTurn = c.getBonus();
            this.value = (double) troopsPerTurn + (double) troopsPerTurn / (double) minHoldSize;
            provincesPlus = new HashSet<Province>(provinces);
            provincesPlus.addAll(minHold);
        }
        public int totalEnemyTroopCount(Snapshot s){
            // Count the total number of enemy troops in the continent
            int totalTroops = 0;
            for(Province p : provinces){
                if(p.getOwner() != s.getMyPlayer()){
                    totalTroops += p.getNumSoldiers();
                }
            }
            return totalTroops;
        }         
        public int totalInvasionCount(Snapshot s){
            // Count the total number of own attackers needed to remove defenders in the continent
            int totalDefenders = 0;
            for(Province p : provincesPlus){
                if(p.getOwner() != s.getMyPlayer()){
                    totalDefenders += p.getNumSoldiers() + 1;
                }
            }
            return totalDefenders;
        }
        public Player controlledBy(Snapshot s){
            // Return the player who controls the continent, or null if no one does
            Player controllingPlayer = null;
            for(Province p : provinces){
                if(controllingPlayer == null){
                    controllingPlayer = p.getOwner();
                }
                else if(controllingPlayer != p.getOwner()){
                    return null;
                }
            }
            return controllingPlayer;
        }
        public double getDeployScore(Snapshot s, DeployCommand dc) {
            double score = 0.0;
            int oldTroops = dc.getProvince().getNumSoldiers();
            int newTroops = oldTroops + dc.getNumTroops();
            if(minHold.contains(dc.getProvince()) && controlledBy(s) == s.getMyPlayer()){
                int nearbyOpponentAttackers = nearbyOpponentAttackerCount(s, dc.getProvince());
                int paranoidNearbyOpponentAttackers = nearbyOpponentAttackers + (int) (0.00 * totalEnemyTroopCount(s));
                score += value * (1.0 - winPct(paranoidNearbyOpponentAttackers, newTroops));
                score -= value * (1.0 - winPct(paranoidNearbyOpponentAttackers, oldTroops));
            } else {
                boolean adjacentEnemy = false;
                for(Province adjacent : dc.getProvince().getAdjacent()){
                    if((adjacent.getOwner() != s.getMyPlayer()) && provincesPlus.contains(adjacent)){
                        adjacentEnemy = true;
                        break;
                    }
                }
                if(adjacentEnemy){
                    int invasionCount = totalInvasionCount(s);
                    score += value * winPct(newTroops - 1, invasionCount);
                    score -= value * winPct(oldTroops - 1, invasionCount);
                }
            }
            return score;
        }
        public double getAttackScore(Snapshot s, AttackCommand ac) {
            double score = 0.0;
            if(provincesPlus.contains(ac.getDefendingProvince())){
                int invasionCount = totalInvasionCount(s);
                score += value * winPct(ac.getnumAttackingTroops(), invasionCount);
            } 
            if(minHold.contains(ac.getAttackingProvince()) && controlledBy(s) == s.getMyPlayer()){
                int nearbyOpponentAttackers = nearbyOpponentAttackerCount(s, ac.getDefendingProvince());
                int paranoidNearbyOpponentAttackers = nearbyOpponentAttackers + (int) (0.00 * totalEnemyTroopCount(s));
                score -= value * (1.0 - winPct(paranoidNearbyOpponentAttackers, ac.getnumAttackingTroops()));
            }
            return score;
        }
        public double getMoveAfterConquerScore(Snapshot s, Province attackingProvince, Province defendingProvince, int numTroops) {
            double score = 0.0;
            if(minHold.contains(defendingProvince) && controlledBy(s) == s.getMyPlayer()){
                score += value * winPct(numTroops, nearbyOpponentAttackerCount(s, defendingProvince));
            }
            if (minHold.contains(attackingProvince) && controlledBy(s) == s.getMyPlayer()){
                score -= value * winPct(numTroops, nearbyOpponentAttackerCount(s, attackingProvince));
            }
            // Check if either province is adjacent to an enemy province in the continent
            boolean attackAdjacentEnemy = false;
            for(Province adjacent : attackingProvince.getAdjacent()){
                if(adjacent.getOwner() != s.getMyPlayer() && adjacent != defendingProvince && provincesPlus.contains(adjacent)){
                    attackAdjacentEnemy = true;
                    break;
                }
            }
            boolean defendAdjacentEnemy = false;
            for(Province adjacent : defendingProvince.getAdjacent()){
                if(adjacent.getOwner() != s.getMyPlayer() && adjacent != attackingProvince && provincesPlus.contains(adjacent)){
                    defendAdjacentEnemy = true;
                    break;
                }
            }
            if(attackAdjacentEnemy && !defendAdjacentEnemy){
                score += value * winPct(numTroops - 1, totalInvasionCount(s));
            }
            if(!defendAdjacentEnemy && attackAdjacentEnemy){
                score -= value * winPct(numTroops - 1, totalInvasionCount(s));
            }
            return score;
        }
        public double getMoveScore(Snapshot s, MoveCommand mc) {
            int numTroops = mc.getnumMovingTroops();
            Province source = mc.getSourceProvince();
            Province target = mc.getTargetProvince();
            int oldSourceTroops = source.getNumSoldiers();
            int newSourceTroops = oldSourceTroops - numTroops;
            int oldTargetTroops = target.getNumSoldiers();
            int newTargetTroops = oldTargetTroops + numTroops;

            double score = 0.0;
            if(minHold.contains(target) && controlledBy(s) == s.getMyPlayer()){
                score += value * winPct(newTargetTroops, nearbyOpponentAttackerCount(s, target));
                score -= value * winPct(oldTargetTroops, nearbyOpponentAttackerCount(s, target));
            }
            if (minHold.contains(source) && controlledBy(s) == s.getMyPlayer()){
                score += value * winPct(newSourceTroops, nearbyOpponentAttackerCount(s, source));
                score -= value * winPct(oldSourceTroops, nearbyOpponentAttackerCount(s, source));
            }
            // Check if either province is adjacent to an enemy province in the continent
            boolean attackAdjacentEnemy = false;
            for(Province adjacent : source.getAdjacent()){
                if(adjacent.getOwner() != s.getMyPlayer() && adjacent != target && provincesPlus.contains(adjacent)){
                    attackAdjacentEnemy = true;
                    break;
                }
            }
            boolean defendAdjacentEnemy = false;
            for(Province adjacent : target.getAdjacent()){
                if(adjacent.getOwner() != s.getMyPlayer() && adjacent != source && provincesPlus.contains(target)){
                    defendAdjacentEnemy = true;
                    break;
                }
            }
            if(attackAdjacentEnemy && !defendAdjacentEnemy){
                score += value * winPct(newTargetTroops - 1, totalInvasionCount(s));
                score -= value * winPct(oldTargetTroops - 1, totalInvasionCount(s));
            }
            if(!defendAdjacentEnemy && attackAdjacentEnemy){
                score -= value * winPct(newSourceTroops - 1, totalInvasionCount(s));
                score += value * winPct(oldSourceTroops - 1, totalInvasionCount(s));
            }
            return score;
        }
    }
        
    public class ControlTerritoryGoal extends Goal {
        private double value = 1.0 / 3.0;
        // public double getDeployScore(Snapshot s, DeployCommand dc) {
        //     double weakNeighborsScore = 0.0;
        //     double score = 0.0;
        //     for(Province p : dc.getProvince().getAdjacent()){
        //         if(p.getOwner() != s.getMyPlayer()){
        //             weakNeighborsScore += 0.1 + 0.9 * (1.0 / p.getNumSoldiers());
        //         }
        //     }
        //     if(weakNeighborsScore < 0.00001){
        //         weakNeighborsScore = -1.0;
        //     }
            

        //     // defensive bonus for deploying on a spot with few soldiers
        //     int oldTroops = dc.getProvince().getNumSoldiers();
        //     int newTroops = oldTroops + dc.getNumTroops();
        //     double defenseScore = 0.0;
        //     defenseScore += (double) (newTroops - 1.0) / (double) (newTroops);
        //     defenseScore -= (double) (oldTroops - 1.0) / (double) (oldTroops); 

        //     score +=  value * 0.1* weakNeighborsScore * dc.getNumTroops();
        //     score += value * defenseScore;
        //     return score;
        // }
        public double getAttackScore(Snapshot s, AttackCommand ac) {
            double score = 0.0;
            boolean adjacentEnemy = false;
            for(Province p : ac.getAttackingProvince().getAdjacent()){
                if(p.getOwner() != s.getMyPlayer()){
                    adjacentEnemy = true;
                    break;
                }
            }
            double mult = 0.6;
            if(adjacentEnemy){
                mult = 0.8;
            }
            // Reward for chance of winning
            score += value * mult * (winPct(ac.getnumAttackingTroops(), ac.getDefendingProvince().getNumSoldiers()));
            
            // Penalize for chance of loosing troops
            score -= value * (1.0 - winPct(ac.getnumAttackingTroops(), ac.getDefendingProvince().getNumSoldiers()) - 0.1);
            return score;
        }
        public double getMoveAfterConquerScore(Snapshot s, Province attackingProvince, Province defendingProvince, int numTroops) {
            double weakNeighborsScore = 0.0;
            for(Province p : defendingProvince.getAdjacent()){
                if(p.getOwner() != s.getMyPlayer()){
                    weakNeighborsScore += 0.1 + 0.9 * (1.0 / p.getNumSoldiers());
                }
            }
            if (weakNeighborsScore < 0.00001){
                weakNeighborsScore = -1.0;
            }
            double homeWeakNeighborsScore = 0.0;
            for(Province p : attackingProvince.getAdjacent()){
                if(p.getOwner() != s.getMyPlayer()){
                    homeWeakNeighborsScore += 0.1 + 0.9 * (1.0 / p.getNumSoldiers());
                }
            }
            if (homeWeakNeighborsScore < 0.00001){
                homeWeakNeighborsScore = -1.0;
            }
            return value * (weakNeighborsScore - homeWeakNeighborsScore) * numTroops;
        }
        public double getMoveScore(Snapshot s, MoveCommand mc) {
            double weakNeighborsScore = 0.0;
            for(Province p : mc.getTargetProvince().getAdjacent()){
                if(p.getOwner() != s.getMyPlayer()){
                    weakNeighborsScore += 0.1 + 0.9 * (1.0 / p.getNumSoldiers());
                }
            }
            if (weakNeighborsScore < 0.00001){
                weakNeighborsScore = -1.0;
            }
            double homeWeakNeighborsScore = 0.0;
            for(Province p : mc.getSourceProvince().getAdjacent()){
                if(p.getOwner() != s.getMyPlayer()){
                    homeWeakNeighborsScore += 0.1 + 0.9 * (1.0 / p.getNumSoldiers());
                }
            }
            if (homeWeakNeighborsScore < 0.00001){
                homeWeakNeighborsScore = -1.0;
            }
            return value * (weakNeighborsScore - homeWeakNeighborsScore) * mc.getnumMovingTroops();
        }
   }

    public class EliminatePlayerGoal extends Goal {
        private Player target;
        private double value;
        private int invasionCount;
        private int attackerCount;
        private boolean onePlayerLeft;
        public EliminatePlayerGoal(Player target, Snapshot s){
            this.target = target;
            this.value = 3.0 * target.getNumCards();
            // Add value if target is last player
            int players = 0;
            for (Player p : s.getPlayers()){
                if(p.getTerritory().size() == 0){
                    players += 1;
                }
            }
            if(players == 2){
                this.onePlayerLeft = true;
                this.value = 500.0;
            }
            this.invasionCount = 0;
            
            for(Province p : target.getTerritory()){
                this.invasionCount += p.getNumSoldiers() + 1;
            } 
            this.attackerCount = 0;
            for(Province p : s.getMyPlayer().getTerritory()){
                this.attackerCount += p.getNumSoldiers() - 1;
            }
        }

        public double getDeployScore(Snapshot s, DeployCommand dc) {
            double score = 0.0;
            // if adjacent to target, score is proportional to win probability
            int adjacent = 0;
            for(Province p : dc.getProvince().getAdjacent()){
                if(p.getOwner() == target){
                    adjacent += p.getNumSoldiers();
                }
            }
            if (adjacent > 0){
                score += value * winPct(attackerCount, invasionCount) * winPct(dc.getNumTroops(), adjacent);
                if(onePlayerLeft){
                    score += 1000000.0;
                }
            }
            return score;
        }
        
        public double getAttackScore(Snapshot s, AttackCommand ac) {
            double score = 0.0;
            if(ac.getDefendingProvince().getOwner() == target){
                score += value * winPct(attackerCount, invasionCount) * winPct(ac.getnumAttackingTroops(), ac.getDefendingProvince().getNumSoldiers()) ;
                if(onePlayerLeft){
                    score += 1000000.0;
                }
            }
            return score;
        }

        public double getMoveAfterConquerScore(Snapshot s, Province attackingProvince, Province defendingProvince, int numTroops) {
            double score = 0.0;
            // if moving adjacent to target, score is proportional to win probability
            int adjacent = 0;
            int provinceCount = 0; 
            for(Province p : defendingProvince.getAdjacent()){
                if(p.getOwner() == target){
                    adjacent += p.getNumSoldiers();
                    provinceCount += 1;
                }
            }
            score += value * winPct(attackerCount, invasionCount) * winPct(numTroops, adjacent) * provinceCount;
            if(onePlayerLeft && provinceCount > 0){
                score += 1000000.0;
            }
                
            return score;
        }

        public double getMoveScore(Snapshot s, MoveCommand mc) {
            double score = 0.0;
            // if moving adjacent to target, score is proportional to win probability
            int adjacent = 0;
            int provinceCount = 0;
            for(Province p : mc.getTargetProvince().getAdjacent()){
                if(p.getOwner() == target){
                    adjacent += p.getNumSoldiers();
                    provinceCount += 1;
                }
            }
            score += value * winPct(attackerCount, invasionCount) * winPct(mc.getnumMovingTroops(), adjacent) * provinceCount;
            if(onePlayerLeft && provinceCount > 0){
                score += 1000000.0;
            }
            return score;
        }
    }

    /**
     * @return Method to tell player their turn is about to start. Intended to let player initialize variables and logic.
     */
    public void beginTurn(Snapshot s){
        achievedCardReward = false;
        goals = new HashSet<Goal>();
        goals.add(new CardRewardGoal());
        for(Continent c : s.getWorld().getContinents()){
            goals.add(new ControlContinentGoal(c));
        }
        goals.add(new ControlTerritoryGoal());
    }

    /**
     * @return //Method to tell player their turn is about to end. Intended to let player clean up variables and logic.
     */
    public void endTurn(Snapshot s){
        goals = null;
    }

    /**
     * @return //Asks player to place troops. Should return [int numTroopstoPlace, Province destination]
     */

    public Set<DeployCommand> draftPhase(Snapshot s, int numTroopstoPlace){
        GL = GameLogger.getGameLogger();
        HashMap<Province, Double> scores = new HashMap<Province, Double>();
        HashMap<Province, Integer> troops = new HashMap<Province, Integer>();
        Set<DeployCommand> returnArray = new HashSet<DeployCommand>();
        Set<Province> myterritory = s.getMyPlayer().getTerritory();
        if(myterritory.size() <= 0){
            return returnArray;
        }
        // initialize scores and troops to 0 and 0.0
        for(Province owned : myterritory){
            scores.put(owned, 0.0);
            troops.put(owned, 0);
        }
        // evalulate score for each province
        for(int troop = 0; troop < numTroopstoPlace; troop++){
            double bestScoreDiff = Double.NEGATIVE_INFINITY;
            double bestNextScore = 0.0;
            Province bestProvince = null;
            for(Province owned : myterritory){
                int currentTroops = troops.get(owned);
                double currentScore = 0.0;
                double nextScore = 0.0;
                for(Goal g : goals){
                    nextScore += g.getDeployScore(s, new DeployCommand(currentTroops + 1, owned));
                }
                double scoreDiff = nextScore - currentScore;
                if(scoreDiff > bestScoreDiff) {
                    bestScoreDiff = scoreDiff;
                    bestProvince = owned;
                    bestNextScore = nextScore;
                }
            }
            troops.put(bestProvince, troops.get(bestProvince) + 1);
            scores.put(bestProvince, bestNextScore);
        }
        for(Province p : myterritory){
            if(troops.get(p) > 0) {
                returnArray.add(new DeployCommand(troops.get(p), p));
            }
        }
        return(returnArray);
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public AttackCommand attackPhase(Snapshot s){
        if(s.getMyPlayer().getTerritory().size() <= 0){
            return new AttackCommand(0, null,null);
        }
        AttackCommand ret;
        Province bestAttackingProvince = null;
        Province bestDefendingProvince = null;
        int bestUseXtroops = 0;
        double bestScore = 0.0;
        int checked = 0;
        for(Province p: s.getMyPlayer().getTerritory()){
            if(p.getNumSoldiers() > 1){
                for(Province adj: p.getAdjacent()){
                    if(!adj.getOwner().equals(s.getMyPlayer())){
                        checked++;
                        // use max troops
                        int maxTroops = p.getNumSoldiers() - 1;
                        ret = new AttackCommand(maxTroops, p, adj);
                        double score = 0.0;
                        for(Goal g : goals){
                            score += g.getAttackScore(s, ret);
                        }
                        if(score > bestScore){
                            bestScore = score;
                            bestUseXtroops = maxTroops;
                            bestAttackingProvince = p;
                            bestDefendingProvince = adj;
                        }
                        
                    }
                }
            }
            if(checked > 200){
                break;
            }
        }
        return new AttackCommand(bestUseXtroops, bestAttackingProvince, bestDefendingProvince);
    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
    public MoveCommand movePhase(Snapshot s){
        if(s.getMyPlayer().getTerritory().size() <= 0){
            return new MoveCommand(0, null,null);
        }
        MoveCommand ret;
        Province bestSource = null;
        Province bestDestination = null;
        int bestMoveXTroops = 0;
        double bestScore = 0.0;
        for(Province p: s.getMyPlayer().getTerritory()){
            if(p.getNumSoldiers() > 1){
                for(Province d: Game.possibleDestinations(p)) {
                    int maxTroops = p.getNumSoldiers() - 1;
                    ret = new MoveCommand(maxTroops, d, p);
                    double score = 0.0;
                    for(Goal g : goals){
                        score += g.getMoveScore(s, ret);
                    }
                    if(score > bestScore){
                        bestScore = score;
                        bestMoveXTroops = maxTroops;
                        bestSource = p;
                        bestDestination = d;
                    }
                }
            }
        }
        return new MoveCommand(bestMoveXTroops, bestDestination, bestSource);
    }

    public void attackPhaseResults(int[] battleResults){
        //Check if attack went well
        //Change logic before attackPhase gets called again
        return;
    }

    /**
     * @return //Asks player how many troops to move into conquered territory. Should return [int numTroops]
     */
    public int moveAfterConquer(Snapshot s, Province attackingProvince, Province defendingProvince){
        achievedCardReward = true;
        //find all neighbors
        if(s.getMyPlayer().getTerritory().size() <= 0){
            return 0;
        }
        int maxTroops = attackingProvince.getNumSoldiers()-1;
        int bestTroops = 0;
        double bestScore = Double.NEGATIVE_INFINITY;
        for(int i = 1; i <= maxTroops; i++){
            double score = 0.0;
            for(Goal g : goals){
                score += g.getMoveAfterConquerScore(s, attackingProvince, defendingProvince, i);
            }
            if(score > bestScore){
                bestScore = score;
                bestTroops = i;
            }
        }
        return bestTroops;
    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If not required, any invalid input will count as passing on your turn.
     */
    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops){
        // if we have less than 3 cards, we can't turn in
        if (s.getUserHand().size() < 3){
            return new HashSet<Card>();
        }
        // if we have 5 cards, we must turn in
        if (required){
            return makeSet(s.getUserHand());
        }
        Set<Card> mycards = s.getUserHand();
        Set<Card> set = makeSet(mycards);
        // if we have a complete set, we have to decide if we want to turn in
        if (set.size() == 3){
            double turnInScore = 0.0;
            for(Goal g : goals){
                turnInScore += g.getTurnInScore(s, true);
            }
            double passScore = 0.0;
            for(Goal g : goals){
                passScore += g.getTurnInScore(s, false);
            }
            if (turnInScore > passScore){
                return set;
            }
            else{
                return new HashSet<Card>();
            }
        }
        // if we have an incomplete set, we can't turn in
        return new HashSet<Card>();
    }

    // helper functions

    public int nearbyOpponentAttackerCount(Snapshot s, Province p){
        // For each opponent, count how many attackers could reach this province
        HashMap<Player, Integer> nearbyAttackers = new HashMap<Player, Integer>();
        for(Player player : s.getPlayers()){
            if(player != s.getMyPlayer()){
                nearbyAttackers.put(player, 0);
            }
        }
        HashSet<Province> visited = new HashSet<Province>();
        for(Province adjacent : p.getAdjacent()){
            if(adjacent.getOwner() != s.getMyPlayer()){
                nearbyAttackers.put(adjacent.getOwner(), nearbyAttackers.get(adjacent.getOwner()) + adjacent.getNumSoldiers() - 1);
            }

            visited.add(adjacent);
            // Check provinces 2 provinces away
            for(Province adjacent2 : adjacent.getAdjacent()){
                if(!visited.contains(adjacent2)){
                    if(adjacent2.getOwner() != s.getMyPlayer()){
                        nearbyAttackers.put(adjacent2.getOwner(), nearbyAttackers.get(adjacent2.getOwner()) + adjacent2.getNumSoldiers() - 2);
                    }
                }
            }
        }
        // Return the maximum number of attackers from any one player
        int maxAttackers = 0;
        for(Player player : nearbyAttackers.keySet()){
            if(nearbyAttackers.get(player) > maxAttackers){
                maxAttackers = nearbyAttackers.get(player);
            }
        }
        return maxAttackers;
    }

    public double winPct(int attackers, int defenders){
        if(attackers < 1){
            return 0.0;
        }
        if(defenders < 1){
            return 1.0;
        }
        if(attackers > 10 || defenders > 10){
            double adjustedAttackers = attackers - 4;
            double adjustedDefenders = defenders - 4;
            double diff = adjustedAttackers - adjustedDefenders;
            double attAdv = 1.05;
            double numAdv = 1.45;
            double weight = Math.pow(attAdv, adjustedAttackers) * Math.pow(numAdv, diff);
            return weight / (weight + 1.0);            
        }
        return (winPctGrid[attackers -1][defenders -1]);
    }

    public double scoreProb(double winScore, double loseScore, double winProb){
        return winScore * winProb + loseScore * (1.0 - winProb);
    }

    public Set<Card> makeSet(Set<Card> set){
        int[] types = new int[4]; //indexes 0 = infantry, 1 = cavalry, 2 = artillery, 3 = wild
        Set<Card> returnSet = new HashSet<Card>();

        for(Card c : set){
            if(c != null){
                types[c.getType()] ++;
            }
        }
        int setType = 3;
        for(int i = 0; i < types.length; i++){
            if(types[i] >= 3){
                setType = i;
            }
        }
        if(setType == 3){
            if(types[0] > 0 && types[1] > 0 && types[2] > 0){
                int[] needed = {0, 0, 0}; //0 = not found, 1 = found
                for(Card c : set){
                    for(int i = 0; i < needed.length; i++){
                        if(needed[i] == 0 && c != null && c.getType() == i){
                            returnSet.add(c);
                            needed[i] = 1;
                        }
                    }
                }
            }
            else{
                int found = 0;
                int target = 2;
                if(types[0] >= types[1] && types[0] >= types[2]){
                    target = 0;
                }
                else if(types[1] >= types[0] && types[1] >= types[2]){
                    target = 1;
                }
                for(Card c : set){
                    if(c.getType() == target){
                        returnSet.add(c);
                        found++;
                    }
                }
                for(Card c : set){
                    if(found < 3 && c.getType() == 3){
                        returnSet.add(c);
                        found++;
                    }
                }
            }
        }
        else{
            int found = 0;
            for(Card c : set){
                if(found < 3 && c.getType() == setType){
                    returnSet.add(c);
                    found++;
                }
            }
        }

        return returnSet;
    }
    
    public String getCustomName(){
        return "GoalPlayer";
    }

    public Color[] getCustomColor(){
        return new Color[] {Color.BLACK, Color.WHITE};
    }

    public String getCustomProfile(){
        return null;
    }

}
