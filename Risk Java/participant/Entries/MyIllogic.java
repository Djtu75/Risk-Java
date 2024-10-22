package participant.Entries;

import participant.*;
import util.*;

import java.util.*;

public class MyIllogic extends PlayerLogic {
    GameLogger GL;

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
    //place based on current percent differences, as a percent of total
    //TODO: now every so often we get stuck
    public Set<DeployCommand> draftPhase(Snapshot s, int numTroopstoPlace){
        //GL = GameLogger.getGameLogger();
        //GL.LogMessage("num troops to place: " + numTroopstoPlace);
        Set<DeployCommand> returnArray = new HashSet<DeployCommand>();
        Set<Province> myterritory = s.getMyPlayer().getTerritory();
        if(myterritory.size() <= 0){
            return returnArray;
        }

        Set<Province> neighbors = new HashSet<Province>();
        Set<Province> borders = new HashSet<Province>();

        int troopsPlaced = 0;
        Province maxWeak = null;
        int maxDiff = 0;
        while(troopsPlaced < numTroopstoPlace) {
            for (Province owned : myterritory) {
                Set<Province> adjacent = owned.getAdjacent();
                for (Province target : adjacent) {
                    if (target.getOwner() != s.getMyPlayer()) {
                        neighbors.add(target);
                        borders.add(owned);
                        int diff = target.getNumSoldiers() - owned.getNumSoldiers();
                        if (diff > maxDiff) {
                            maxDiff = diff;
                            maxWeak = owned;
                            returnArray.add(new DeployCommand(1, owned));
                            troopsPlaced++;
                            //GL.LogMessage(1 + " troops placed in " + owned);
                        }
                    }
                }
            }
            //if there aren't any/enough with fewer, add to equal
            for (Province owned : myterritory) {
                Set<Province> adjacent = owned.getAdjacent();
                for (Province target : adjacent) {
                    if (target.getOwner() != s.getMyPlayer()) {
                        neighbors.add(target);
                        borders.add(owned);
                        int diff = target.getNumSoldiers() - owned.getNumSoldiers();
                        if (diff >= maxDiff) {
                            maxDiff = diff;
                            maxWeak = owned;
                            returnArray.add(new DeployCommand(1, owned));
                            troopsPlaced++;
                            //GL.LogMessage(1 + " troops placed in " + owned);
                        }
                    }
                }
            }
            //if there aren't any/enough greater or equal, add to any border
            for (Province owned : myterritory) {
                Set<Province> adjacent = owned.getAdjacent();
                for (Province target : adjacent) {
                    if (target.getOwner() != s.getMyPlayer()) {
                        returnArray.add(new DeployCommand(1, owned));
                        troopsPlaced++;
                        //GL.LogMessage(1 + " troops placed in " + owned);
                    }
                }
            }
        }
        if (troopsPlaced < numTroopstoPlace) {
            returnArray.add(new DeployCommand(numTroopstoPlace - troopsPlaced, maxWeak));
            //GL.LogMessage("Gave up and placed" + (numTroopstoPlace - troopsPlaced) + "troops placed in " + maxWeak);
        }
        while(troopsPlaced > numTroopstoPlace) {
            returnArray.remove(returnArray.iterator().next());
            //GL.LogMessage("Removed one command");
            troopsPlaced--;
        }

        //GL.LogMessage(("Has "+ borders.size() +" border territories and "+ neighbors.size() +" neighboring territories").toString());

        return(returnArray);
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    //find largest difference between mine and neighbor
    //TODO: why do we have infinite loops
    public AttackCommand attackPhase(Snapshot s){
        if(s.getMyPlayer().getTerritory().size() <= 0){
            return new AttackCommand(0, null,null);
        }

        Province attackingProvince = null;
        Province defendingProvince = null;
        GL = GameLogger.getGameLogger();
        Set<Province> myterritory = s.getMyPlayer().getTerritory();

        int biggestOutnumber = 1;

        //find all neighbors
        for(Province owned : myterritory){
            Set<Province> adjacent =  owned.getAdjacent();
            for(Province target : adjacent){
                if(target.getOwner() != s.getMyPlayer()){
                    /*int diff = target.getNumSoldiers() - owned.getNumSoldiers();
                    if (diff > biggestOutnumber){
                        attackingProvince = owned;
                        defendingProvince = target;
                        biggestOutnumber = diff;
                    }*/
                    if(owned.getNumSoldiers() > 1 && target.getNumSoldiers() * .75 <= owned.getNumSoldiers()){
                        attackingProvince = owned;
                        defendingProvince = target;
                    }
                }
            }
        }
        if(attackingProvince != null) {
            int useXtroops = attackingProvince.getNumSoldiers() - 2;
            AttackCommand command = new AttackCommand(useXtroops, attackingProvince, defendingProvince);
            return (command);
        } else {
            return null;
        }
    }

    /**
     * @return //Asks player to move troops. Should return [int numTroopstoMove, Province source, Province destination]
     */
    //decreasing logically by closest enemy - by squares? closest has 4, next has 2, next has 1?
    //I wish. You can only return one moveCommand
    public MoveCommand movePhase(Snapshot s){
        Province source = null;
        Province destination = null;
        int moveXTroops = 0;
        Set<Province> myterritory = s.getMyPlayer().getTerritory();

        Set<Province> neighbors = new HashSet<Province>();
        Set<Province> borders = new HashSet<Province>();
        int maxWeakness = 0;
        //find the biggest different in troops between two neighboring territories
        for(Province owned : myterritory){
            Set<Province> adjacent =  owned.getAdjacent();
            for(Province target : adjacent){
                if(target.getOwner() != s.getMyPlayer()){
                    neighbors.add(target);
                    borders.add(owned);
                    int diff = target.getNumSoldiers() - owned.getNumSoldiers();
                    //GL.LogMessage(target.getName() + " :" + target.getNumSoldiers() + " is theirs vs " + owned.getName() + " :" + owned.getNumSoldiers() + " is mine");
                    if (diff >= maxWeakness){
                        maxWeakness = diff;
                        destination = owned;
                    }
                }
            }
        }
        //find max distant
        int maxInsideTroops = 1;
        if(destination != null) {
            //GL.LogMessage("destination: " + destination.getName());
            if(Game.possibleDestinations(destination) != null) {
                Province possSource = null;/*closest(Game.possibleDestinations(destination), s.getMyPlayer());*/
                for(Province p : Game.possibleDestinations(destination)){
                    if(p.getNumSoldiers() > maxInsideTroops && !borders.contains(p)){
                        possSource = p;
                    }
                }
                if (possSource != null && possSource.getNumSoldiers() > 1) {
                    source = possSource;
                }
            }
        }

        if (source != null && destination != null) {
            //moveXTroops = source.getNumSoldiers() - destination.getNumSoldiers() - 1;
            moveXTroops = source.getNumSoldiers() - 1;
        }
        MoveCommand command = new MoveCommand(moveXTroops, destination, source);
        return (command);
    }

    //??? how
    public void attackPhaseResults(Snapshot s, int[] battleResults){
        //Check if attack went well
        //Change logic before attackPhase gets called again
        return;
    }


    /**
     * @return //Asks player how many troops to move into conquered territory. Should return [int numTroops]
     */
    //follow MoveCommand logic
    public int moveAfterConquer(Snapshot s, Province attackingProvince, Province defendingProvince){
        int transferXTroops = 1;
        if(attackingProvince.getNumSoldiers() > 2) {
            transferXTroops = attackingProvince.getNumSoldiers() - 2;
        }
        return(transferXTroops);
    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If not required, any invalid input will count as passing on your turn.
     */
    //only turn in if have to
    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops){
        if(required) {
            if (s.getUserHand().size() >= 3) {
                Set<Card> mycards = s.getUserHand();
                Set<Province> myterritory = s.getMyPlayer().getTerritory();

                Card card1 = null;
                Card card2 = null;
                Card card3 = null;

                for (Card c : mycards) {
                    if (c != null && myterritory.contains(c.getProvince())) {
                        card1 = c;
                    }
                }

                if (card1 == null) {
                    card1 = mycards.iterator().next();
                    mycards.remove(card1);
                }
                for (Card card : mycards) {
                    if (card != null && card.getType() == card1.getType() || card.getType() == 3) {
                        card2 = card;

                    }
                }
                if (card2 == null) {
                    card2 = mycards.iterator().next();

                }
                mycards.remove(card2);
                if (card1.getType() == card2.getType()) {
                    for (Card card : mycards) {
                        if (card.getType() == card2.getType() || card.getType() == 3) {
                            card3 = card;
                        }
                    }
                } else {
                    for (Card card : mycards) {
                        if ((card.getType() != card1.getType() && card.getType() != card2.getType()) || card.getType() == 3) {
                            card3 = card;
                        }
                    }
                }
                mycards.remove(card3);

                mycards.add(card1);
                mycards.add(card2);
                mycards.add(card3);
                Set<Card> returnSet = new HashSet<Card>(Arrays.asList(card1, card2, card3));
                return (returnSet);
            }
        }
        return (null);
    }
    //TODO: this is basically recursion
    public Province closest(Set<Province> adjterritory, Player myPlayer){
        //loop 1
        for(Province owned : adjterritory){
            GL.LogMessage("Loop 1: checking province" + owned.getName());
            Set<Province> adjacent = owned.getAdjacent();
            //loop 2
            for(Province target : adjacent){
                //GL.LogMessage("Loop 2: checking province " + target.getName());
                if(target.getOwner() != myPlayer){
                    break;
                } else {
                    //loop 3
                    for(Province inside : adjacent){
                        GL.LogMessage("Loop 3: checking province " + inside.getName());
                        Set<Province> insideAdj = inside.getAdjacent();
                        //loop 4
                        for(Province insideTarget : insideAdj){
                            //GL.LogMessage("Loop 4: checking province " + insideTarget.getName());
                            if(insideTarget.getOwner() != myPlayer){
                                break;
                            } else {
                                //loop 5
                                for(Province insider : insideAdj){
                                    GL.LogMessage("Loop 5: checking province " + insider.getName());
                                    Set<Province> insiderAdj = insider.getAdjacent();
                                    //loop 6
                                    for(Province insiderTarget : insiderAdj){
                                        //GL.LogMessage("Loop 6: checking province " + insiderTarget.getName());
                                        if(insiderTarget.getOwner() != myPlayer){
                                            break;
                                        } else {
                                            //loop 7
                                            for(Province insidest : insiderAdj){
                                                GL.LogMessage("Loop 7: checking province " + insidest.getName());
                                                Set<Province> insidestAdj = insidest.getAdjacent();
                                                //loop 8
                                                for(Province insidestTarget : insidestAdj){
                                                    //GL.LogMessage("Loop 8: checking province " + insidestTarget.getName());
                                                    if(insidestTarget.getOwner() != myPlayer){
                                                        break;
                                                    } else{
                                                        return insidestTarget;
                                                    }
                                                }
                                            }
                                            return insiderTarget;
                                        }
                                    }
                                }
                                return insideTarget;
                            }
                        }
                    }
                    return target;
                }
            }
        }
        //TODO: this is a bad idea
        return null;
    }
    public Set<Province>[] moving (Set<Province> myterritory, Player myPlayer){

        Set<Province>[] territories = new Set[myterritory.size()];
        //ArrayList<Set<Province>> territories = new ArrayList<>();

        //find all neighbors
        //TODO: fix stupid recursion. Actually, even the first loop errors out of bounds
        int distance = 0;
        for(Province owned : myterritory){
            Set<Province> adjacent =  owned.getAdjacent();
            for(Province target : adjacent){
                if(target.getOwner() != myPlayer){
                    territories[distance].add(owned);
                } else {
                    /*distance++;
                    Set<Province> TList = new HashSet<>();
                    for (Set<Province> ring : territories) {
                        TList.addAll(ring);
                    }
                    moving(TList, myPlayer);*/
                }
            }
        }

        return territories;

    }
}