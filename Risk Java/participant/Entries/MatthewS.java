package participant.Entries;

import participant.*;
import util.*;
import java.util.*;

public class MatthewS extends PlayerLogic {
    private Random rand = new Random();

    private Map<Province, Integer> borderThreats = new HashMap<>(); 
    private List<Province> easyTargets = new ArrayList<>(); 
    
    
    public void beginTurn(Snapshot s) {
        List<Province> borderProvinces = getBorderProvinces(s);
        
        borderThreats.clear();
        easyTargets.clear();
        
        for (Province borderProvince : borderProvinces) {
            for (Province neighbor : borderProvince.getAdjacent()) {
                if (neighbor.getOwner() != s.getMyPlayer()) {
                    int strengthDifference = borderProvince.getNumSoldiers() - neighbor.getNumSoldiers();
                    borderThreats.put(borderProvince, strengthDifference);
                    
                    if (strengthDifference > 1) {
                        easyTargets.add(neighbor);
                    }
                }
            }
        }
    }

    
    public Set<DeployCommand> draftPhase(Snapshot s, int numTroopstoPlace) {
        Set<DeployCommand> commands = new HashSet<>();
        List<Province> myProvinces = new ArrayList<>(s.getMyPlayer().getTerritory());
        
        List<Province> borderProvinces = getBorderProvinces(s);
        
        while (numTroopstoPlace > 0) {
            Province target = borderProvinces.isEmpty() ? 
                myProvinces.get(rand.nextInt(myProvinces.size())) : 
                borderProvinces.get(rand.nextInt(borderProvinces.size()));
            
            int troopsToPlace = Math.min(numTroopstoPlace, numTroopstoPlace / 2 + 1);
            commands.add(new DeployCommand(troopsToPlace, target));
            numTroopstoPlace -= troopsToPlace;
        }
        
        return commands;
    }

    
    public AttackCommand attackPhase(Snapshot s) {
        List<Province> myProvinces = new ArrayList<>(s.getMyPlayer().getTerritory());
        
        for (Province myProvince : myProvinces) {
            for (Province neighbor : myProvince.getAdjacent()) {
                if (neighbor.getOwner() != s.getMyPlayer() && 
                    myProvince.getNumSoldiers() > neighbor.getNumSoldiers() + 1) {
                    int attackingTroops = Math.min(3, myProvince.getNumSoldiers() - 1);
                    return new AttackCommand(attackingTroops, myProvince, neighbor);
                }
            }
        }
        
        return null;
    }

    private double aggressionLevel = 0.5; 
    private int successfulAttacks = 0;    
    private int failedAttacks = 0;        
   
    public void attackPhaseResults(Snapshot s, int[] battleResults) {
    int totalBattles = battleResults.length;
    int wins = 0;
    int losses = 0;
    
    for (int result : battleResults) {
        if (result == 1) {
            wins++;
        } else {
            losses++;
        }
    }

    successfulAttacks += wins;
    failedAttacks += losses;
    
    double winRate = totalBattles > 0 ? (double) wins / totalBattles : 0.5;

    if (winRate > 0.75) {

        aggressionLevel = Math.min(aggressionLevel + 0.1, 1.0); 
    } else if (winRate < 0.25) {
       
        aggressionLevel = Math.max(aggressionLevel - 0.1, 0.0);
    }
    
    if (successfulAttacks + failedAttacks >= 20) {
        successfulAttacks = 0;
        failedAttacks = 0;
    }

    adjustAttackStrategyBasedOnAggression(aggressionLevel);
    }
    private int maxAttacksPerTurn = 3;
    private void adjustAttackStrategyBasedOnAggression(double aggressionLevel) {
    
        if (aggressionLevel > 0.7) {

            maxAttacksPerTurn = 5;

        } else if (aggressionLevel < 0.3) {
            
            maxAttacksPerTurn = 2;

        } else {
           
            maxAttacksPerTurn = 3;
        }
    }

    public int moveAfterConquer(Snapshot s, Province attackingProvince, Province defendingProvince) {
        return Math.max(attackingProvince.getNumSoldiers() - 1, 3);
    }
   
    public MoveCommand movePhase(Snapshot s) {
        List<Province> myProvinces = new ArrayList<>(s.getMyPlayer().getTerritory());
        Province sourceProvince = null;
        Province targetProvince = null;
        int maxDifference = 0;

        for (Province province : myProvinces) {
            if (province.getNumSoldiers() > 1) {
                for (Province neighbor : province.getAdjacent()) {
                    if (neighbor.getOwner() == s.getMyPlayer()) {
                        int difference = province.getNumSoldiers() - neighbor.getNumSoldiers();
                        if (difference > maxDifference) {
                            sourceProvince = province;
                            targetProvince = neighbor;
                            maxDifference = difference;
                        }
                    }
                }
            }
        }

        if (sourceProvince != null && targetProvince != null) {
            int troopsToMove = sourceProvince.getNumSoldiers() / 2;
            return new MoveCommand(troopsToMove, sourceProvince, targetProvince);
        }

        return null;
    }

    
    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops) {
        Set<Card> hand = s.getUserHand();
        if (hand.size() < 3 && !required) {
            return new HashSet<>();
        }
        
        Set<Card> toTurnIn = new HashSet<>();
        Iterator<Card> it = hand.iterator();
        while (it.hasNext() && toTurnIn.size() < 3) {
            toTurnIn.add(it.next());
        }
        
        return toTurnIn;
    }

    private List<Province> getBorderProvinces(Snapshot s) {
        List<Province> borderProvinces = new ArrayList<>();
        for (Province province : s.getMyPlayer().getTerritory()) {
            for (Province neighbor : province.getAdjacent()) {
                if (neighbor.getOwner() != s.getMyPlayer()) {
                    borderProvinces.add(province);
                    break;
                }
            }
        }
        return borderProvinces;
    }

    
    public String getCustomName() {
        return "Matthew S";
    }

}