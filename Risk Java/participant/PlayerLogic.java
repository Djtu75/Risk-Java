package participant;
import util.*;
import java.util.Set;
import java.util.Iterator;

public class PlayerLogic implements Logic {

    Game mygame = null;

    public void initialize(Game mygame){
        this.mygame = mygame;
    }

    public void beginTurn(){
        //Do setup logic
    }

    public void endTurn(){
        //Do ending logic
    }

    public Object[] draftPhase(int numTroopstoPlace){
        Province destination = new Province(null, null, 0);
        int placeXTroops = 1;
        Object[] returnArray = {placeXTroops, destination};
        return(returnArray);
    }
    public Object[] attackPhase(){
        int useXtroops = 3;
        Province attackingProvince = new Province(null, null, 0);
        Province defendingProvince = new Province(null, null, 0);
        Object[] returnArray = {useXtroops, attackingProvince, defendingProvince};
        return (returnArray);

    }
    public Object[] movePhase(){
        int moveXTroops = 1;
        Province source = new Province(null, null, 0);
        Province destination = new Province(null, null, 0);
        Object[] returnArray = {moveXTroops, source, destination};
        return (returnArray);
    }

    public int moveAfterConquer(Province attackingProvince, Province defendingProvince){
        int transferXTroops = 1;
        return(transferXTroops);

    }
    public Object[] turnInCards(boolean required, int currentTroops){
        if(required){
            Set<Card> mycards = mygame.getCardData(this);
            //Some Logic to pick which 3 to turn in
            Iterator<Card> iter = mycards.iterator();
            Card card1 = iter.next();
            Card card2 = iter.next();
            Card card3 = iter.next();
            return(new Object[]{card1, card2, card3});
        }
        else{
            return (new Object[]{"optional"});
        }

    }

}
