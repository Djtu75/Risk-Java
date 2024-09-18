package participant;
import util.*;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class PlayerLogic implements Logic {

    Game mygame = null;
    Player myplayer = null;

    public void initialize(Game mygame, Player myplayer){
        this.mygame = mygame;
        this.myplayer = myplayer;
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
        Province destination1 = null;
        int placeXTroops1 = 1;
        Province destination2 = null;
        int placeXTroops2 = 1;
        DeployCommand command1 = new DeployCommand(placeXTroops1, destination1);
        DeployCommand command2 = new DeployCommand(placeXTroops2, destination2);
        Set<DeployCommand> returnSet = new HashSet<DeployCommand>(Arrays.asList(command1, command2));
        return(returnSet);
    }

    /**
     * @return //Asks player to perform attack. Should return [int numTroopstoUse, Province attackingProvince, Province defendingProvince]
     */
    public AttackCommand attackPhase(Snapshot s){
        int useXtroops = 3;
        Province attackingProvince = null;
        Province defendingProvince = null;
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
        return (command);
    }

    public void attackPhaseResults(Snapshot s, int[] battleResults){
        //Check if attack went well
        //Change logic before attackPhase gets called again
        return;
    }


    /**
     * @return //Asks player how many troops to move into conquered territory. Should return [int numTroops]
     */
    public int moveAfterConquer(Snapshot s, Province attackingProvince, Province defendingProvince){
        int transferXTroops = 1;
        return(transferXTroops);

    }

    /**
     * @return //Asks player to turn in set of cards. If required, must be [Card X, Card Y, Card Z]. If not required, any invalid input will count as passing on your turn.
     */
    public Set<Card> turnInCards(Snapshot s, boolean required, int currentTroops){
        if(required){
            Set<Card> mycards = mygame.getCardData(this);
            //Some Logic to pick which 3 to turn in
            Iterator<Card> iter = mycards.iterator();
            Card card1 = iter.next();
            Card card2 = iter.next();
            Card card3 = iter.next();
            Set<Card> returnSet = new HashSet<Card>(Arrays.asList(card1, card2, card3));
            return (returnSet);
        }
        else{
            return (new HashSet<Card>(Arrays.asList()));
        }

    }

}
