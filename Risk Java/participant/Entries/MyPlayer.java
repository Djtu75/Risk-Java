package participant.Entries;

import participant.*;

import java.awt.Color;
import java.util.*;
import util.*;


public class MyPlayer extends PlayerLogic {


    GameLogger GL;


    public MyPlayer(){
        super();
    }


    public void beginTurn(Snapshot s){
    }


    public void endTurn(Snapshot s){
    }


    public Set<DeployCommand> draftPhase(Snapshot s, int numTroopstoPlace){
        GL = GameLogger.getGameLogger();
        Set<DeployCommand> returnSet = new HashSet<>();
        Set<Province> myTerritory = s.getMyPlayer().getTerritory();


        if(myTerritory.isEmpty()){
            return returnSet;
        }


        Map<Province, Integer> borderProvinces = new HashMap<>();
        for(Province p : myTerritory){
            int adjacentEnemyTroops = 0;
            boolean isBorder = false;
            for(Province adj : p.getAdjacent()){
                if(adj.getOwner() != s.getMyPlayer()){
                    isBorder = true;
                    adjacentEnemyTroops += adj.getNumSoldiers();
                }
            }
            if(isBorder){
                borderProvinces.put(p, adjacentEnemyTroops);
            }
        }


        if(borderProvinces.isEmpty()){
            return returnSet;
        }


        List<Map.Entry<Province, Integer>> sortedBorderProvinces = new ArrayList<>(borderProvinces.entrySet());
        sortedBorderProvinces.sort((a, b) -> b.getValue() - a.getValue());


        while(numTroopstoPlace > 0){
            for(Map.Entry<Province, Integer> entry : sortedBorderProvinces){
                if(numTroopstoPlace <= 0){
                    break;
                }
                Province p = entry.getKey();
                returnSet.add(new DeployCommand(1, p));
                numTroopstoPlace--;
            }
        }


        return returnSet;
    }


    public AttackCommand attackPhase(Snapshot s){
        Province attackingProvince = null;
        Province defendingProvince = null;
        int maxEnemyTroops = -1;


        for(Province p : s.getMyPlayer().getTerritory()){
            if(p.getNumSoldiers() > 1){
                for(Province adj : p.getAdjacent()){
                    if(adj.getOwner() != s.getMyPlayer()){
                        if(adj.getNumSoldiers() > maxEnemyTroops){
                            attackingProvince = p;
                            defendingProvince = adj;
                            maxEnemyTroops = adj.getNumSoldiers();
                        }
                    }
                }
            }
        }
        if(attackingProvince != null){
            int attackingTroops = Math.min(attackingProvince.getNumSoldiers() - 1, 3);
            return new AttackCommand(attackingTroops, attackingProvince, defendingProvince);
        }
        return new AttackCommand(0, null, null);
    }


    public void attackPhaseResults(Snapshot s, int[] battleResults){
    }


    public int moveAfterConquer(Snapshot s, Province attackingProvince, Province defendingProvince){
        int troopsToMove = attackingProvince.getNumSoldiers() - 1;
        return troopsToMove;
    }


    public MoveCommand movePhase(Snapshot s){
        int moveXTroops = 1;
        Province source = null;
        Province destination = null;
        MoveCommand command = new MoveCommand(moveXTroops, destination, source);
        for(Province p: s.getMyPlayer().getTerritory()){
            if(p.getNumSoldiers() > 1){
                boolean bordersEnemy = isAdjacentToEnemy(s, p);
                if(!bordersEnemy){
                    for(Province option: Game.possibleDestinations(p)){
                        if(isAdjacentToEnemy(s, option)){
                            moveXTroops = p.getNumSoldiers()-1;
                            source = p;
                            destination = option;
                            command = new MoveCommand(moveXTroops, destination, source);
                        }
                    }
                }
            }
        }
        return command;
    }


    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops){
        if(required){
            Set<Card> mycards = s.getUserHand();
            Set<Card> returnSet = makeSet(mycards);
            return returnSet;
        }
        else{
            return new HashSet<Card>(Arrays.asList());
        }
    }


    private Set<Card> makeSet(Set<Card> set){
        int[] types = new int[4];
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
                int[] needed = {0, 0, 0};
                for(Card c : set){
                    for(int i = 0; i < needed.length; i++){
                        if(needed[i] == 0 && c.getType() == i){
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


    private boolean isAdjacentToEnemy(Snapshot s, Province p){
        boolean bordersEnemy = false;
        for(Province adj: p.getAdjacent()){
            if(adj.getOwner() != s.getMyPlayer()){
                bordersEnemy = true;
                break;
            }
        }
        return bordersEnemy;
    }


    public String getCustomName(){
        return "JPlayer";
    }


    public Color[] getCustomColor(){
        return null;
    }


    public String getCustomProfile(){
        return null;
    }
}