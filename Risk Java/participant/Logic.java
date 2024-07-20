package participant;

import util.*;

public interface Logic {

    public void initialize(Game mygame);
    public void beginTurn();
    public void endTurn();

    public Object[] draftPhase(int numTroopstoPlace);
    public Object[] attackPhase();
    public Object[] movePhase();

    public int moveAfterConquer(Province attackingProvince, Province defendingProvince);
    public Object[] turnInCards(boolean required, int currentTroops);

    
}
