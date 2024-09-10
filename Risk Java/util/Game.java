package util;

import java.util.Random;
import participant.PlayerLogic;

import java.util.Set;

import javax.imageio.IIOException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class Game {
    
    private boolean started = false;
    private boolean gameOver = false;
    private int turnNum = 0;
    private Player[] players;
    private World world;
    private Card[] deck;
    private int totalTrades;
    private int cardsLeft;
    private RenderEarth display = null;
    private GraphicProvince[] gp = null;
    private double[][] winPct = {
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

    public Game(Player[] players, World world, GraphicProvince[] gps){
        this.players = players;
        this.world = world;
        this.gp = gps;
    }

    public GraphicProvince[] getgps(){
        return gp;
    }

    public String startGame(int initialTroops, RenderEarth re)throws IllegalArgumentException{
        display = re;
        if(!started){
            started = true;
            if (this.validityCheck()){ //Check that world and players are useable
                randomOrder(players); //Sort player array in random order
                assignLand(); //Distribute land to players
                deck = generateDeck(); //Create deck of cards
                cardsLeft = deck.length; //array should change size as cards are drawn and turned in, this keeps track

                boolean phasefinished = false;
                boolean oldSchoolSetUp = false;
                if(oldSchoolSetUp){
                    //Initial land claiming for original setup |Unimplemented
                    // for(int i = 0; i < players.length; i++){
                    //     Player activePlayer = players[i];
                    //     int troopsToPlace = initialTroops/players.length;
                    //     while(!phasefinished){
                    //         Object[] action = activePlayer.getLogic().draftPhase(troopsToPlace);
                    //         Object[] parameters = {activePlayer};
                    //         if(actionIsValid("placeTroops", action, parameters)){
                    //             int numTroopsPlaced = (int) action[0];
                    //             Province destination = (Province) action[1];
                    //             troopsToPlace -= numTroopsPlaced;
                    //             destination.addSoldiers((int) action[0]);
                    //         }
                    //         else{
                    //             phasefinished = true;
                    //         }
                    //         if(troopsToPlace == 0){
                    //             phasefinished = true;
                    //         }
                    //     }
                    // }
                }
                else{ //If using standard quick start, distribute even amounts of land between players and add troops randomly to that land
                    for(int i = 0; i < players.length; i++){
                        Player activePlayer = players[i];
                        activePlayer.getLogic().initialize(this, activePlayer);
                        System.out.println("Initializing " + activePlayer.getName());
                        int troopsToPlace = initialTroops/players.length;
                        for(Province prov: activePlayer.getTerritory()){
                            troopsToPlace--;
                            prov.setNumsoldiers(1);
                        }
                        while(troopsToPlace > 0){
                            Random rand = new Random();
                            Object[] temp = activePlayer.getTerritory().toArray();
                            ((Province) temp[rand.nextInt(temp.length)]).addSoldiers(1);
                            troopsToPlace--;
                        }
                    }
                }
                while(!gameOver){ //Main game loop |Needs mechanism to ignore/eliminate players who have lost all territory
                      
                    //wait(3000);
                    gameOver = checkGameOver();
                    
                    //Normal turn
                    for(int i = 0; i < players.length; i++){
                        Player activePlayer = players[i];
                        // if(activePlayer.getTerritory().size() <= 0){

                        activePlayer.getLogic().beginTurn(); //Tell player their turn is starting
                        System.out.println("********************************************************************************************************************************************");
                        System.out.println("Beginning " + activePlayer.getName());
                        System.out.println("Has "+activePlayer.getNumCards()+" Cards");

                        //Object[] action;
                        int cardBonus = 0;


                        //Handle cards
                        int troopsToPlace = calcTroopAllot(activePlayer); //Calc troops player should get to place

                        if(activePlayer.getNumCards() >= 5){ //If 5 or more cards in hand, force turn in
                            Set<Card> cardsToTurnIn = makeSet(activePlayer.getCards());
                            if(cardsToTurnIn.contains(null)){
                                System.out.println("ILLEGAL CARD DETECTED");
                                throw new IllegalArgumentException("ILLEGAL CARD DETECTED");
                            }
                            //Object[] parameters = {activePlayer};
                            if(isValidTurnIn(cardsToTurnIn , activePlayer)/*actionIsValid("turnInCards", action, parameters)*/){
                                //Set<Card> cardsToTurnIn = new HashSet<Card>(Arrays.asList((Card)action[0], (Card)action[1], (Card)action[2]));
                                for(Card c: cardsToTurnIn){ //Returns cards to deck
                                    returnCard(c);
                                }
                                cardBonus = calcCardTurnIn(cardsToTurnIn);
                                Set<Card> tempCards = activePlayer.getCards();
                                for(Card c: cardsToTurnIn){
                                    tempCards.remove(c);
                                }
                                //tempCards.removeAll(cardsToTurnIn);
                                activePlayer.setCards(tempCards);
                            }
                            
                            else{ //If player attempts to turn in invalid set, clear player's hand
                                Set<Card> tempCards = activePlayer.getCards();
                                tempCards.clear();
                                activePlayer.setCards(tempCards);
                            }
                        }
                        else{ //If fewer than 5 cards, ask player to turn in cards (if invalid input, do nothing as it is not required)
                            // action = activePlayer.getLogic().turnInCards(false, troopsToPlace);
                            // Object[] parameters = {activePlayer};
                            if(activePlayer.getCards().size() >= 3){
                                Set<Card> cardsToTurnIn = activePlayer.getLogic().turnInCards(troopsToPlace);
                                if(isValidTurnIn(cardsToTurnIn, activePlayer)/*actionIsValid("turnInCards", action, parameters)*/){
                                    //Set<Card> cardsToTurnIn = new HashSet<Card>(Arrays.asList((Card)action[0], (Card)action[1], (Card)action[2]));
                                    for(Card c: cardsToTurnIn){ //Returns cards to deck
                                        returnCard(c);
                                    }
                                    cardBonus = calcCardTurnIn(cardsToTurnIn);
                                    Set<Card> tempCards = activePlayer.getCards();
                                    for(Card c: cardsToTurnIn){
                                        tempCards.remove(c);
                                    }
                                    //tempCards.removeAll(cardsToTurnIn);
                                    activePlayer.setCards(tempCards);
                                }
                            }
                        }

                        //Handle placement phase
                        troopsToPlace += cardBonus;
                        phasefinished = false;
                        System.out.println(activePlayer.getName() + " Has " + troopsToPlace +" soldiers to place");
                        while(!phasefinished){
                            // action = activePlayer.getLogic().draftPhase(troopsToPlace);
                            // System.out.println("Placing " + activePlayer.getName());
                            // Object[] parameters = {activePlayer, troopsToPlace};
                            ArrayList<placeTroop> act = activePlayer.getLogic().draftPhase(troopsToPlace);
                            for(placeTroop placement : act){
                                System.out.println("***\nTesting placeTroops: "+ isValidPlaceTroop(placement, activePlayer, troopsToPlace)+"\n***\n");
                                if(isValidPlaceTroop(placement, activePlayer, troopsToPlace)/*actionIsValid("placeTroops", action, parameters)*/){
                                    System.out.println("Valid Troop Placement");
                                    troopsToPlace -= placement.getTroopPlacement();
                                    placement.getProvince().addSoldiers(placement.getTroopPlacement());
                                }
                                else{
                                    phasefinished = true;
                                    break;
                                }
                            }
                            if(troopsToPlace == 0){
                                phasefinished = true;
                            }else{
                                phasefinished = true;
                                System.out.println("Troops not placed: "+ troopsToPlace);
                            }
                        }
                        phasefinished = false;
    
                        //handle attacking phase
                        boolean gainCard = false;
                        while(!phasefinished){
                            // action = activePlayer.getLogic().attackPhase(); //Prompt player to choose number of troops to attack with, and the two provinces involved
                            // Object[] parameters = {activePlayer};
                            // System.out.println("Attacking " + activePlayer.getName());
                            // System.out.println("Valid attack? " + actionIsValid("attacking", action, parameters));
                            attack act = activePlayer.getLogic().attackPhase();
                            if(isValidAttack(act, activePlayer)/*actionIsValid("attacking ", action, parameters)*/){
                                // int attackingTroops = (int) action[0];
                                // Province attackingProvince = (Province) action[1];
                                // Province defendingProvince = (Province) action[2];

                                //System.out.println("***\nTesting attack: "+ isValidAttack(act, activePlayer)+"\n***\n");
                                    int[] result = doBattle((act.getDefendingProvince().getNumSoldiers()), act.getAttackingTroops()); //Do one set of dice rolls
                                    act.getAttackingProvince().addSoldiers(result[1]*-1); //remove killed soldiers
                                    act.getDefendingProvince().addSoldiers(result[0]*-1); //remove killed soldiers
                                    //System.out.println("Result: Defenders killed:" +result[0]+" Attackers killed: "+result[1]);
                                    activePlayer.getLogic().attackPhaseResults(result);
                                if(act.getDefendingProvince().getNumSoldiers() <= 0){ //If no troops left in defending province, that province is conquered
                                    act.getDefendingProvince().setNumsoldiers(0);
                                    System.out.println("Territory: "+act.getDefendingProvince().getName() + " Conquered by "+ activePlayer.getName());
                                    gainCard = true;
                                    activePlayer.addTerritory(act.getDefendingProvince());
                                    int moveAfterConquer = activePlayer.getLogic().moveAfterConquer(act.getAttackingProvince(), act.getDefendingProvince());
                                    //Object[] parameters2 = {attackingTroops, attackingProvince};
                                    if(isValidMoveAfterConquer(moveAfterConquer, act.getAttackingProvince(), act.getDefendingProvince())/*actionIsValid("moveAfterConquer", action, parameters2)*/){
                                        //int troopsToMove = (int) action[0];
                                        act.getAttackingProvince().addSoldiers(moveAfterConquer*-1);
                                        act.getDefendingProvince().addSoldiers(moveAfterConquer);
                                    }
                                    else{ //If invalid input, move minimum number of troops
                                        int troopsToMove = act.getAttackingTroops();
                                        act.getAttackingProvince().addSoldiers(troopsToMove*-1);
                                        act.getDefendingProvince().addSoldiers(troopsToMove);
                                    }
                                }
                            }
                            else{
                                phasefinished = true;
                            }
                        }
                        if(gainCard){
                            activePlayer.addCard(drawCard());
                        }
                        phasefinished = false;
    
                        //handle move phase
                        while(!phasefinished){
                            //action = activePlayer.getLogic().movePhase();
                            moveTroop act= activePlayer.getLogic().movePhase();
                            //Object[] parameters = {activePlayer};
                            System.out.println("Moving " + activePlayer.getName());
                            System.out.println("***\nTesting moveTroops: "+ isValidMoveTroop(act/*test2 */, activePlayer)+"\n***\n");
                            if(isValidMoveTroop(act , activePlayer)/*actionIsValid("moving", action, parameters)*/){
                                // int movingTroops = (int) action[0];
                                // Province sourceProvince = (Province) action[1];
                                // Province destinationProvince = (Province) action[2];
                                //moveTroop test2 = new moveTroop(movingTroops, sourceProvince, destinationProvince);
                                System.out.println("Moving " + act.getMovingTroops()+ " from " + act.getSourceProvince().getName()+ " to "+ act.getTargetProvince().getName());
                                // destinationProvince.addSoldiers(movingTroops);
                                // sourceProvince.addSoldiers(movingTroops*-1);
                                act.getTargetProvince().addSoldiers(act.getMovingTroops());
                                act.getSourceProvince().addSoldiers((act.getMovingTroops() * -1));
                                phasefinished = true;
                            }
                            else{
                                phasefinished = true;
                            }
                        }

                        activePlayer.getLogic().endTurn(); //Signal to player that their turn is ending
                    turnNum++;
                    display.repaint();
                    
                    }   
                }
                return "Winner";
            }
            else{
                return "error";
            }
        }
        else{
            return "Game already initialized.";
        }
        
    }

    public static void wait(int ms)
{
    try
    {
        Thread.sleep(ms);

    }
    catch(InterruptedException ex)
    {
        Thread.currentThread().interrupt();
    }
    

}

    /**
     * @return Checks if game has properties valid for setup
     */
    public Boolean validityCheck(){ 
        if(players == null || world == null || players.length > world.getProvinces().size()){
            return false;
        }
        for(Player p: players){
            if(p == null){
                return false;
            }
        }
        for(Province p: world.getProvinces()){
            if(p.getAdjacent().isEmpty() || p.getNumSoldiers() < 0){
                return false;
            }
        }

        return true;
    }

    /**
     * @return Checks if game has ended by evaluating if one player owns all territory
     */
    public boolean checkGameOver(){
        for(Player p: players){
            if(p != null){
                if(world.getProvinces().size()== p.getTerritory().size()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return Returns a deck of cards that includes a card for each province, and wild cards proportionate to the size of the deck (1 per 21 cards)
     */
    private Card[] generateDeck(){
        Set<Province> provinces = world.getProvinces();
        int deckSize = provinces.size() + (int)(provinces.size()/21); //In standard risk, 2 wild cards per deck (which has 42 territory cards)
        if(deckSize < (players.length)*5){ //Make sure there are enough cards for each player to hold the maximum amount of 5
            deckSize = (players.length)*5;
        }
        Card[] returnArray = new Card[deckSize];
        int gennum = 0;
        for(Province p: world.getProvinces()){
            returnArray[gennum] = new Card(p); //Generate a card for each province
            gennum++;
        }
        while(gennum < deckSize){
            returnArray[gennum] = new Card(); //Fill the rest of the deck with wild cards
            gennum++;
        }
        return(returnArray);
    }
    //******************************************************************************************************************************** */
    public double winPctOfFullAttack(Province attacker, Province defender){
        int attackers = attacker.getNumSoldiers();
        int defenders = defender.getNumSoldiers();
        if(attackers > 10 || defenders > 10){
            return -1.0;
        }

        return (winPct[attackers -1][defenders -1] * 100);

    }
    public double winPctOfPartialAttack(int attackers, int defenders){
        if(attackers > 10 || defenders > 10){
            return -1.0;
        }
        return (winPct[attackers -1][defenders -1] * 100);
    }
    public Set<Card> makeSet(Set<Card> set){
        Set<Card> BAD = new HashSet<>();
        Set<Card> men = new HashSet<Card>();
        Set<Card> horses = new HashSet<Card>();
        Set<Card> cannon = new HashSet<Card>();
        Set<Card> oneOfEach = new HashSet<Card>();
        Set<Card> wild = new HashSet<Card>();

        for(Card c : set){
            if(c.getValue() == 0){
                men.add(c);
            }
            else if(c.getValue() == 1){
                horses.add(c);
            }
            else if(c.getValue() == 2){
                cannon.add(c);
            }else{
                wild.add(c);
            }
            if(men.size()>=3 ){//three men
                Set<Card> ret = new HashSet<Card>();
                Iterator<Card> iter = men.iterator();
                for(int i = 0; i < 3; i++){
                    ret.add(iter.next());
                }
                System.out.println("Returning infantry set");
                return ret;
            }else if(horses.size() >=3 ){//three horses
                Set<Card> ret = new HashSet<Card>();
                Iterator<Card> iter = horses.iterator();
                for(int i = 0; i < 3; i++){
                    ret.add(iter.next());
                }
                System.out.println("Returning cavalry set");
                return ret;
            }else if(cannon.size() >=3 ){//three cannons
                Set<Card> ret = new HashSet<Card>();
                Iterator<Card> iter = cannon.iterator();
                for(int i = 0; i < 3; i++){
                    ret.add(iter.next());
                }
                System.out.println("Returning artillery set");
                return ret;
            }else if((men.size()+wild.size()) >= 3){//men + wild
                Set<Card> ret = new HashSet<Card>();
                Iterator<Card> iter = men.iterator();
                for(int i = 0; i < men.size(); i++){
                    ret.add(iter.next());
                }
                Iterator<Card> iterWild = wild.iterator();
                while(ret.size() < 3){
                    ret.add(iterWild.next());
                }
                System.out.println("Returning mixed set");
                return ret;
            }else if((horses.size()+wild.size()) >= 3){//horses + wild
                Set<Card> ret = new HashSet<Card>();
                Iterator<Card> iter = horses.iterator();
                for(int i = 0; i < horses.size(); i++){
                    ret.add(iter.next());
                }
                Iterator<Card> iterWild = wild.iterator();
                while(ret.size() < 3){
                    ret.add(iterWild.next());
                }
                System.out.println("Returning mixed set");
                return ret;
            }else if((cannon.size()+wild.size()) >= 3){//cannon + wild
                Set<Card> ret = new HashSet<Card>();
                Iterator<Card> iter = cannon.iterator();
                for(int i = 0; i < cannon.size(); i++){
                    ret.add(iter.next());
                }
                Iterator<Card> iterWild = wild.iterator();
                while(ret.size() < 3){
                    ret.add(iterWild.next());
                }
                System.out.println("Returning mixed set");
                return ret;
            }else if(wild.size() >= 3){//three wild
                Set<Card> ret = new HashSet<Card>();
                Iterator<Card> iter = wild.iterator();
                for(int i = 0; i < 3; i++){
                    ret.add(iter.next());
                }
                System.out.println("Returning wild set");
                return ret;
            }
            //Check for one of each
            if(!men.isEmpty()){//One of Each or wild
                oneOfEach.add(men.iterator().next());
            }
            if(!horses.isEmpty()){
                oneOfEach.add(horses.iterator().next());
            }
            if(!cannon.isEmpty()){
                oneOfEach.add(cannon.iterator().next());
            }
            if(oneOfEach.size() == 3){
                return oneOfEach;
            }else if(oneOfEach.size() < 3 && (!wild.isEmpty())){
                oneOfEach.add(wild.iterator().next());
                return oneOfEach;
            }else{
                oneOfEach.clear();
            }
        }

        System.out.println("makeSet Failed");
        for(Card c : set){
            System.out.print("\nCARD:    troop value: "+c.getValue());
            if(c.getValue() < 3){
                System.out.print(" province name: "+c.getProvince().getName()+"\n");
            }else{
                System.out.print(" wild card\n");
            }
        }
        System.out.println("Number of infantry "+men.size());
        System.out.println("Number of cavalry "+horses.size());
        System.out.println("Number of artillery "+cannon.size());
        System.out.println("Number of wilds "+wild.size());
        Card c = null;
        BAD.add(c);
        BAD.add(c);
        BAD.add(c);
        return BAD;
    }

    /**
     * @return Returns a random card from the deck. Decrements size of deck.
     */
    private Card drawCard(){
        Random rand = new Random();
        int indexToDraw = 0; //Pick index
        System.out.println("Cards Left: "+ cardsLeft);
        if(cardsLeft <= 0){

        }else{
            indexToDraw = rand.nextInt(cardsLeft); //Pick index
        }
        Card returnCard = deck[indexToDraw]; //Save card
        deck[indexToDraw] = deck[cardsLeft-1]; //Swap drawn card with last elem of list
        cardsLeft--; //shrink deck to exclude drawn card
        return returnCard;
    }

    /**
     * @param card Card to return to deck.
     */
    private void returnCard(Card card){
        if(cardsLeft != deck.length){ //If (for some reason) deck is already full, deny card return
            deck[cardsLeft] = card; //Add card to end of deck
            cardsLeft++; //increase size of deck to include returned card
            System.out.println("returned Card "+card.getTroop());
        }
    }

    /**
     * //Evenly distributes the land, COMRADE
     */
    private void assignLand(){ 
        Object[] provinces = world.getProvinces().toArray();
        randomOrder(provinces);
        int playerIndex = 0;
        for(Object province: provinces){
            players[playerIndex].addTerritory(((Province) province));
            playerIndex = (playerIndex+1)%players.length;
        }
    }

    /**
     * @param array Array is sorted in random order
     */
    public static void randomOrder(Object[] array){
        Random rand = new Random();
        for (int i = array.length-1; i > 0; i--) {
			int randomIndex = rand.nextInt(i+1);
			Object temp = array[randomIndex];
			array[randomIndex] = array[i];
			array[i] = temp;
		}

    }

    /**
     * @param player player to calculate for
     * @return Returns troops for player based on owned territory and continents UNIMPLEMENTED
     */
    public int calcTroopAllot(Player player){
        int ret = 3;
        int owned = player.getTerritory().size();
        if(owned <= 11){
            ret = 3;
        }else if(owned <= 14){
            ret = 4;
        }else{
            ret =  5;
        }
        Set<Province> ownedProvinces = player.getTerritory();
        Set<Continent> conts = world.getContinents();
        for(Continent cont : conts){
            Set<Province> provinces = cont.getProvinces();
            Boolean ownedContinent = true;
            for(Province province : provinces){
                Boolean ownedProvinceOfContinent = false;
                for(Province ownedProvince : ownedProvinces){
                    if(ownedProvince.getName().equals(province.getName())){
                        ownedProvinceOfContinent = true;
                    }
                }
                if(!ownedProvinceOfContinent){
                    ownedContinent = false;
                }
            }
            if(ownedContinent){
                System.out.println("Owns Continent: "+ cont.getName()+ " Worth +"+cont.getBonus());
                ret += cont.getBonus();
            }
        }
        return ret;
    }   

    /**
     * @param cards cards being turned in
     * @return Returns bonus troops for set UNIMPLEMENTED
     */
    public int calcCardTurnIn(Set<Card> cards){
        if(totalTrades > 6){
            return(15 + totalTrades*5);
        }
        if(totalTrades == 6){
            return 15;
        }
        if(totalTrades == 5){
            return 12;
        }
        if(totalTrades == 4){
            return 10;
        }
        if(totalTrades == 3){
            return 8;
        }
        if(totalTrades == 2){
            return 6;
        }
        if(totalTrades == 1){
            return 4;
        }
        return 0;
    }

    /**
     * @param defendingTroops
     * @param offendingTroops
     * @return Returns array of losses from battle. [0] is defender's lost troops, [1] is attacker's lost troops. Does not consider more attacking troops than 3 or defending troops than 2, as this is the maximum battle size in risk.
     */
    public static int[] doBattle(int defendingTroops, int offendingTroops){ //returns array where [0] is defender's lost troops and [1] is offender's lost troops
        Random rand = new Random();
        int[] result = new int[2];
        int[] defenders = new int[2];
        int[] offenders = new int[3];
        if(defendingTroops > 2){
            defendingTroops = 2;
        }
        if(offendingTroops > 3){
            offendingTroops = 3;
        }
        for(int i = 0; i < defendingTroops; i++){ //Rolls dice
            defenders[i] = rand.nextInt(6)+1;
        }
        sort(defenders);
        for(int i = 0; i < offendingTroops; i++){ //Rolls dice
            offenders[i] = rand.nextInt(6)+1;
        }
        sort(offenders);

        for(int i = 0; (i < defendingTroops && i < offendingTroops); i++){ //Compares dice rolls
            if(defenders[i] >= offenders[i]){
                result[1] += 1;
            }
            else{
                result[0] += 1;
            }
        }
        //System.out.println("defenders roles(" + defenders[0] +", " + defenders[1] + ")"); //Prints roll results
        //System.out.println("attackers roles(" + offenders[0] +", " + offenders[1] + ", " + offenders[2] + ")"); //Prints roll results
        //System.out.println("losses(" + result[0] +", " + result[1] + ")"); //Prints final losses
        return result;
    }

    /**
     * @param pl
     * @return Provides method for players to request their current hand data, as the data is protected
     */
    public Set<Card> getCardData(PlayerLogic pl){
        if(pl != null){
            for(Player p : players){
                if(p.getLogic() == pl){
                    return(p.getCards());
                }
            }
        }
        return(null);
    }

    /*New Version of actionIsValid */
    public Boolean isValidAttack(attack act, Player activePlayer)throws IllegalArgumentException{
        //Do you own attacking province?
        //Do you not own defending province?
        //Do you have enough troops to attack?
        if(act == null){
            return false;
        }
        if(act.getAttackingProvince() == null){
            return false;
        }
        if(act.getDefendingProvince() == null){
            return false;
        }
        if(act.getAttackingProvince().getOwner() != activePlayer){
            return false;
        }
        if(act.getDefendingProvince().getOwner() == activePlayer){
            return false;
        }
        if(act.getAttackingTroops() >= act.getAttackingProvince().getNumSoldiers() ){
            return false;
        }
        return true;
    }

    public Boolean isValidMoveTroop(moveTroop act, Player activePlayer)throws IllegalArgumentException{
        //Do you own both provinces?
        //Are they real provinces?
        //Is there enough soliders on the source province?
        if((act.getSourceProvince() == null || act.getTargetProvince() == null)){
            System.out.println("null Province Value");
            return false;
        }
        if((act.getSourceProvince().getOwner() != activePlayer ) || (act.getTargetProvince().getOwner() != activePlayer)){
            System.out.println("invalid province: isnt owned");
            return false;
        }
        if(world.getProvinces().contains(act.getSourceProvince()) || world.getProvinces().contains(act.getTargetProvince())){
            System.out.println("invalid province: not on map");
            return false;
        }
        if((act.getSourceProvince().getNumSoldiers() - act.getMovingTroops()) < 1){
            System.out.println("invalid amount of troops: "+act.getMovingTroops() +" when only "+act.getSourceProvince().getNumSoldiers() +" are available");
            return false;
        }
        return true;
    }

    public Boolean isValidPlaceTroop(placeTroop act, Player activePlayer, int troopsToPlace)throws IllegalArgumentException{
        // realProvince?
        // realOwner?
        // validNumberOfTroops?
        Set<Province> ps = world.getProvinces();
        Boolean real = false;
        for(Province p : ps){
            if(p.getName().equals(act.getProvince().getName())){
                real = true;
            }
        }
        if(!real){
            System.out.println("Invalid province ");
            return false;
        }
        // if(!ps.contains(act.getProvince())){
        //     System.out.println("Invalid targetProvince "+act.getProvince().toString());
        //     return false;
        // }
        if(act.getProvince().getOwner() != activePlayer){
            System.out.println("Invalid targetProvince Owner");
            return false;
        }
        if((act.getTroopPlacement() > troopsToPlace)  || (act.getTroopPlacement() <= 0)){
            System.out.println("Invalid amount of troops placed");
            return false;
        }
        return true;
    }
    public Boolean isValidMoveAfterConquer(int troopsMoving, Province attackingProvince, Province defendingProvince){
        if((attackingProvince.getNumSoldiers() - troopsMoving) < 1){
            return false;
        }
        if(troopsMoving < 0){
            return false;
        }
        return true;
    }
    public Boolean isValidTurnIn(Set<Card> set, Player activePlayer){
        for(Card c : set){//A turned in Card is not contained in players hand
            if(!activePlayer.getCards().contains(c)){
                return false;
            }
        }
        Set<Card> temp = makeSet(set);
        if(temp.contains(null)){//the makeSet function puts nulls if a set isnt found
            return false;
        }else{
            return true;
        }
    }

    /**
     * @param type
     * @param move
     * @return Determines if move is valid based on current game state. UNIMPLEMENTED
     */
    public Boolean actionIsValid(String type, Object[] action, Object[] params){

        if(type.equals("placeTroops")){
            try{
                System.out.println("attempting to place");
                Player activePlayer = (Player) params[0];
                int availableTroops = (int) params[1];
                int numTroops = (int) action[0];
                Province destination = (Province) action[1];

                Boolean realProvince = false;
                for(Province p: world.getProvinces()){
                    if (p == destination){
                        realProvince = true;
                    }
                }

                System.out.println("real Province? " + realProvince);
                System.out.println("is owned? "+ (destination.getOwner() == activePlayer));
                System.out.println("valid Troop Count? "+ (availableTroops >= numTroops));
                System.out.println("integer valued Troop Count? "+(numTroops > 0)+ "\n");

                if(realProvince && destination.getOwner() == activePlayer && availableTroops >= numTroops && numTroops > 0){
                    return true;
                }

            }
            catch(Exception e){
                System.out.println("Got error in placeTroops: " + e + "\n");
                return false;
            }
        }
        else if(type.equals("attacking")){
            if(action != null){//if action[0] == null player is not attacking
            try{
                Player activePlayer = (Player) params[0];
                int attackingTroops = (int) action[0];
                Province attackingProvince = (Province) action[1];
                Province defendingProvince = (Province) action[2];
                System.out.println("is Owned?" + (attackingProvince.getOwner() == activePlayer));
                System.out.println("not friendly fire?" + (defendingProvince.getOwner() != activePlayer));
                System.out.println("enough Troops to attack?"+ (attackingTroops < attackingProvince.getNumSoldiers())+ "\n");

                if(attackingProvince.getOwner() == activePlayer && defendingProvince.getOwner() != activePlayer && attackingTroops < attackingProvince.getNumSoldiers()){
                    return true;
                }

            }
            catch(Exception e){
                System.out.println("Got error in attacking: " + e + "\n");
                return false;
            }
        }else{
            System.out.println("Player is not attacking\n");
        }
        }
        else if(type.equals("moveAfterConquer")){
            try{
                int troopsToMove = (int) action[0];
                int minimumTroops = (int) params[0];
                Province source = (Province) params[1];

                if(troopsToMove >= minimumTroops && source.getNumSoldiers() > troopsToMove && troopsToMove > 0){
                    return true;
                }

            }
            catch(Exception e){
                System.out.println("Got error in moveAfterConquer: " + e+ "\n");
                return false;
            }
        }
        else if(type.equals("moving")){
            try{
                Player activePlayer = (Player) params[0];
                int movingTroops = (int) action[0];
                Province source = (Province) action[1];
                Province destination = (Province) action[2];

                if(world.getProvinces().contains(destination) && world.getProvinces().contains(source) && source.getOwner() == activePlayer &&
                possibleDestinations(source).contains(destination) && source.getNumSoldiers() > movingTroops && movingTroops > 0){
                    return true;
                }

            }
            catch(Exception e){
                System.out.println("Got error in moving action: " + e + "\n");
                return false;
            }
        }

        return false;
    }

    /**
     * @param source
     * @return Returns an array of all possible places troops could be moved to from the source province.
     * Because troops can only be sent through a chain of continuous land owned by the same player, method assumes that target player is the owner of source.
     */
    public static Set<Province> possibleDestinations(Province source){
        Player sourceOwner = source.getOwner();
        Set<Province> unExplored = new HashSet<Province>();
        unExplored.add(source);
        Set<Province> explored = new HashSet<Province>();
        Set<Province> temp = new HashSet<Province>();
        Set<Province> valid = new HashSet<Province>();
        while(unExplored.size() > 0){
            for(Province p: unExplored){
                if(p.getOwner() == sourceOwner){
                    valid.add(p);
                    for(Province adj: p.getAdjacent()){
                        if(!explored.contains(adj)){
                            temp.add(adj);
                        }
                    }
                }
                explored.add(p);
            }
            unExplored.clear();
            unExplored.addAll(temp);
            temp.clear();
        }
        return valid;
    }

    public static void sort(int arr[]) 
    { 
        int n = arr.length; 
        for (int i = 1; i < n; ++i) { 
            int key = arr[i]; 
            int j = i - 1; 
  
            // Move elements of arr[0..i-1], that are 
            // greater than key, to one position ahead 
            // of their current position 
            while (j >= 0 && arr[j] < key) { 
                arr[j + 1] = arr[j]; 
                j = j - 1; 
            } 
            arr[j + 1] = key; 
        } 
    }
//********************************************************************************************************************************************* */
    public static void main(String[] args) {
        int loopnum = 0;
        int wins = 0;
        int defend = 0;
        int attack = 0;
        int[] result;
        while(loopnum < 1000){
            defend = 2;
            attack = 3;
            while((defend != 0) && (attack != 0)){
                result = doBattle(defend, attack);
                defend -= result[0];
                attack -= result[1];
            }
            if(attack == 0){
                wins++;
            }
            loopnum++;
            
        }
        System.out.println(wins);

        Player player1 = new Player("uno", null);
        Player player2 = new Player("dos", null);
        Player player3 = new Player("tres", null);
        Player player4 = new Player("cuatro", null);

        Game game = new Game(new Player[]{player1, player2, player3, player4}, null, null);
        game.startGame(120, null);

        loopnum = 0;
        Player[] testarray = new Player[]{player1, player2, player3, player4};
        String[] names = new String[]{"uno","dos","tres","cuatro"};
        int[][] results = new int[4][4];
        while(loopnum < 1000000){
            randomOrder(testarray);
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    if(testarray[j].getName() == names[i]){
                        results[i][j]++;
                    }
                }
            }
            testarray = new Player[]{player1, player2, player3, player4};
            loopnum++;   
        }
        for(int[] eh: results){
            System.out.println();
            for(int he: eh){
                System.out.print(he +" ");
            }
        }
        System.out.println();
        /* Proof of even distribution
        loopnum = 0;
        int[] timesRecievedprov1 = new int[4];
        while(loopnum < 10000){
            Player[] testarr = new Player[]{player1, player2, player3, player4};
            World world = new World("worldy world");
            
            for(int i = 0; i < 100; i++){
                world.addProvince(new Province("Province " + i, null, i));
            }
            Game newgame = new Game(testarr, world);
            newgame.assignLand();
            for(int i = 0; i < 4; i++){
                for(Province prov: newgame.players[i].getTerritory()){
                    if(prov.getName().equals("Province 99")){
                        timesRecievedprov1[i] += 1;
                    }
                }
            }
            loopnum++;
        }
        for(int i = 0; i < 4; i++){
            System.out.println(timesRecievedprov1[i]);
        }*/


        // try {
        //     Thread.sleep(1000);
        //   } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        //   }
    }

}
