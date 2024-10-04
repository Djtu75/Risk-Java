package participant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import util.*;

public class ExamplePlayer2 extends PlayerLogic {

    public ExamplePlayer2(){
        super();
    }

    /**
     * @return Method to tell player their turn is about to start. Intended to let player initialize variables and logic.
     */
    public void beginTurn(Snapshot s){
        //Do setup logic
    }

    /**
     * @return //Method to tell player their turn is about to end. Intended to let player clean up variables and logic.
     */
    public void endTurn(Snapshot s){
        //Do ending logic
    }

    /**
     * @return //Asks player to place troops. Should return [int numTroopstoPlace, Province destination]
     */
    public Set<DeployCommand> draftPhase(Snapshot s, int numTroopstoPlace){
        Set<DeployCommand> returnSet = new HashSet<DeployCommand>();
        while(numTroopstoPlace > 0){
            for(Province p: s.getMyPlayer().getTerritory()){
                if(numTroopstoPlace > 0 && isAdjacentToEnemy(s, p)){
                    Province destination = p;
                    int placeXTroops = 1;
                    DeployCommand command = new DeployCommand(placeXTroops, destination);
                    numTroopstoPlace--;
                    returnSet.add(command);
                }
            
            }
        }
        
        return(returnSet);
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public AttackCommand attackPhase(Snapshot s){
        Province attackingProvince = null;
        Province defendingProvince = null;
        for(Province p: s.getMyPlayer().getTerritory()){
            if(p.getNumSoldiers() > 3){
                for(Province adj: p.getAdjacent()){
                    if(adj.getOwner() != p.getOwner()){
                        attackingProvince = p;
                        defendingProvince = adj;
                    }
                }
            }
        }
        int useXtroops = 3;
        AttackCommand command = new AttackCommand(useXtroops, attackingProvince, defendingProvince);
        return (command);

    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
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
        
        return (command);
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
        int transferXTroops = attackingProvince.getNumSoldiers()-1;
        return(transferXTroops);

    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If not required, any invalid input will count as passing on your turn.
     */
    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops){
        if(required){
            Set<Card> mycards = s.getUserHand();
            //Some Logic to pick which 3 to turn in
            Set<Card> returnSet = makeSet(mycards);
            return (returnSet);
        }
        else{
            return (new HashSet<Card>(Arrays.asList()));
        }

    }

    public Set<Card> makeSet(Set<Card> set){
        int[] types = new int[4]; //indexes 0 = infantry, 1 = cavalry, 2 = artillery, 3 = wild
        Set<Card> returnSet = new HashSet<Card>();

        for(Card c : set){
            types[c.getType()]++;
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
            }
        }
        return bordersEnemy;
    }
    
}
