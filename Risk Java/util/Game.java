package util;

import java.util.Random;
import participant.PlayerLogic;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.awt.*;
import java.awt.font.ShapeGraphicAttribute;

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

    public String startGame(int initialTroops, RenderEarth re){
        display = re;
        if(!started){
            started = true;
            if (this.validityCheck()){ //Check that world and players are useable
                randomOrder(players); //Sort player array in random order
                assignLand(); //Distribute land to players
                deck = generateDeck(); //Create deck of cards
                cardsLeft = deck.length; //array should change size as cards are drawn and turned in, this keeps track

                boolean phasefinished = false;
                int currentPlayerIndex = 0;
                boolean oldSchoolSetUp = false;
                if(oldSchoolSetUp){
                    //Initial land claiming for original setup |Unimplemented
                    for(int i = 0; i < players.length; i++){
                        Player activePlayer = players[i];
                        int troopsToPlace = initialTroops/players.length;
                        while(!phasefinished){
                            Set<DeployCommand> action = activePlayer.getLogic().draftPhase(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn()), troopsToPlace);
                            Object[] parameters = {activePlayer};
                            if(actionIsValid("placeTroops", action, parameters)){
                                for(DeployCommand dc : action){
                                    int numTroopsPlaced = dc.getNumTroops();
                                    Province destination = dc.getProvince();
                                    troopsToPlace -= numTroopsPlaced;
                                    destination.addSoldiers(numTroopsPlaced);
                                }
                                
                            }
                            else{
                                phasefinished = true;
                            }
                            if(troopsToPlace == 0){
                                phasefinished = true;
                            }
                        }
                    }
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
                            activePlayer.setNumsoldiers(activePlayer.getNumSoldiers() + 1);
                        }
                        while(troopsToPlace > 0){
                            Random rand = new Random();
                            Object[] temp = activePlayer.getTerritory().toArray();
                            ((Province) temp[rand.nextInt(temp.length)]).addSoldiers(1);
                            activePlayer.setNumsoldiers(activePlayer.getNumSoldiers() + 1);
                            troopsToPlace--;
                        }
                    }
                }
                //Scanner scnr = new Scanner(System.in);
                while(!gameOver){ //Main game loop |Needs mechanism to ignore/eliminate players who have lost all territory
                    //String s = scnr.next();
                    System.out.println(turnNum); 
                    //wait(3000);
                    turnNum++;
                    gameOver = checkGameOver();
                    //Normal turn
                    for(int i = 0; i < players.length; i++){
                        
                        Player activePlayer = players[i];
                        activePlayer.getLogic().beginTurn(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn())); //Tell player their turn is starting
                        System.out.println("Beginning " + activePlayer.getName());
                        int cardBonus = 0;
                        //Handle cards
                        int troopsToPlace = calcTroopAllot(activePlayer); //Calc troops player should get to place
                        if(activePlayer.getNumCards() >= 5){ //If 5 or more cards in hand, force turn in
                            Set<Card> cardsToTurnIn = activePlayer.getLogic().turnInCards(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn()), true, troopsToPlace);
                            Object[] parameters = {activePlayer};
                            if(actionIsValid("turnInCards", cardsToTurnIn, parameters)){
                                //Set<Card> cardsToTurnIn = new HashSet<Card>(Arrays.asList((Card)action[0], (Card)action[1], (Card)action[2]));
                                for(Card c: cardsToTurnIn){ //Returns cards to deck and applies +2 troops if owned by activeplayer
                                    Province cardProv = c.getProvince();
                                    for(Province p : activePlayer.getTerritory()){
                                        if(cardProv != null && p == cardProv){
                                            p.addSoldiers(2);
                                        }
                                    }
                                    returnCard(c);
                                }
                                totalTrades++;
                                cardBonus = calcCardTurnIn();
                                Set<Card> tempCards = activePlayer.getCards();
                                for(Card c: cardsToTurnIn){
                                    tempCards.remove(c);
                                }
                                //tempCards.removeAll(cardsToTurnIn);
                                activePlayer.setCards(tempCards);
                            }
                            else{ //If player attempts to turn in invalid set, clear player's hand
                                Set<Card> tempCards = activePlayer.getCards();
                                for(Card c: tempCards){
                                    returnCard(c);
                                }
                                tempCards.clear();
                                activePlayer.setCards(tempCards);
                            }
                        }
                        else{ //If fewer than 5 cards, ask player to turn in cards (if invalid input, do nothing as it is not required)
                            if(activePlayer.getCards().size() >= 3){
                                Set<Card> cardsToTurnIn = activePlayer.getLogic().turnInCards(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn()), false, troopsToPlace);
                                Object[] parameters = {activePlayer};
                                if(actionIsValid("turnInCards", cardsToTurnIn, parameters)){
                                    //Set<Card> cardsToTurnIn = new HashSet<Card>(Arrays.asList((Card)action[0], (Card)action[1], (Card)action[2]));
                                    for(Card c: cardsToTurnIn){ //Returns cards to deck and applies +2 troops if owned by activeplayer
                                        Province cardProv = c.getProvince();
                                        for(Province p : activePlayer.getTerritory()){
                                            if(cardProv != null && p == cardProv){
                                                p.addSoldiers(2);
                                            }
                                        }
                                        returnCard(c);
                                    }
                                    totalTrades++;
                                    cardBonus = calcCardTurnIn();
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
                        while(!phasefinished){
                            Set<DeployCommand> action = activePlayer.getLogic().draftPhase(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn()), troopsToPlace);
                            System.out.println("Placing " + activePlayer.getName());
                            Object[] parameters = {activePlayer, troopsToPlace};
                            if(actionIsValid("placeTroops", action, parameters)){
                                for(DeployCommand dc : action){
                                    int numTroopsPlaced = dc.getNumTroops();
                                    Province destination = dc.getProvince();
                                    troopsToPlace -= numTroopsPlaced;
                                    destination.addSoldiers(numTroopsPlaced);
                                    activePlayer.setNumsoldiers(activePlayer.getNumSoldiers() + numTroopsPlaced);
                                }
                            }
                            else{
                                phasefinished = true;
                            }
                            if(troopsToPlace == 0){
                                phasefinished = true;
                            }
                        }
                        phasefinished = false;
    
                        //handle attacking phase
                        boolean gainCard = false;
                        while(!phasefinished){
                            AttackCommand action = activePlayer.getLogic().attackPhase(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn())); //Prompt player to choose number of troops to attack with, and the two provinces involved
                            Object[] parameters = {activePlayer};
                            System.out.println("Attacking " + activePlayer.getName());
                            System.out.println("Valid? " + actionIsValid("attacking", action, parameters));
                            if(actionIsValid("attacking", action, parameters)){
                                int attackingTroops = action.getnumAttackingTroops();
                                Province attackingProvince = action.getAttackingProvince();
                                Province defendingProvince = action.getDefendingProvince();
                                int[] result = doBattle((defendingProvince.getNumSoldiers()), attackingTroops); //Do one set of dice rolls
                                attackingProvince.addSoldiers(result[1]*-1); //remove killed soldiers
                                attackingProvince.getOwner().setNumsoldiers(attackingProvince.getOwner().getNumSoldiers() + result[1]*-1);
                                defendingProvince.addSoldiers(result[0]*-1); //remove killed soldiers
                                defendingProvince.getOwner().setNumsoldiers(defendingProvince.getOwner().getNumSoldiers() + result[0]*-1);
                                System.out.println("Result " +result[0]);
                                activePlayer.getLogic().attackPhaseResults(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn()), result);
                                if(defendingProvince.getNumSoldiers() == 0){ //If no troops left in defending province, that province is conquered
                                    gainCard = true;
                                    activePlayer.addTerritory(defendingProvince);
                                    int followUpAction = activePlayer.getLogic().moveAfterConquer(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn()), attackingProvince, defendingProvince);
                                    Object[] parameters2 = {attackingTroops, attackingProvince};
                                    if(actionIsValid("moveAfterConquer", followUpAction, parameters2)){
                                        int troopsToMove = followUpAction;
                                        attackingProvince.addSoldiers(troopsToMove*-1);
                                        defendingProvince.addSoldiers(troopsToMove);
                                    }
                                    else{ //If invalid input, move minimum number of troops
                                        int troopsToMove = attackingTroops;
                                        attackingProvince.addSoldiers(troopsToMove*-1);
                                        defendingProvince.addSoldiers(troopsToMove);
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
                            MoveCommand action = activePlayer.getLogic().movePhase(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn()));
                            Object[] parameters = {activePlayer};
                            System.out.println("Moving " + activePlayer.getName());
                            if(actionIsValid("moving", action, parameters)){
                                int movingTroops = action.getnumMovingTroops();
                                Province sourceProvince = action.getSourceProvince();
                                Province destinationProvince = action.getTargetProvince();
                                destinationProvince.addSoldiers(movingTroops);
                                sourceProvince.addSoldiers(movingTroops*-1);
                            }
                            else{
                                phasefinished = true;
                            }
                        }

                        activePlayer.getLogic().endTurn(new Snapshot(world, players, activePlayer.getCards(), calcCardTurnIn())); //Signal to player that their turn is ending
                    }
                    //wait(1000);
                    if((turnNum % 200) == 0){
                        display.repaint();
                    }
                    else{
                        display.refreshPaint(display.getGraphics());
                    }
                    
                }
                Player winner = null;
                for(Player p : players){
                    if(p.getTerritory().size() > 0){
                        winner = p;
                    }
                }
                System.out.println("Winner: " + winner + " Turn#: " + turnNum);
                display.dispose();
                return "Winner: " + winner + " Turn#: " + turnNum;
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
                    System.out.println("gameover = true");
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
        int[] types = new int[4]; //indexes 0 = infantry, 1 = cavalry, 2 = artillery, 3 = wild
        Set<Card> returnSet = new HashSet<Card>();

        for(Card c : set){
            types[c.getValue()]++;
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
                        if(needed[i] == 0 && c.getValue() == i){
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
                    if(c.getValue() == target){
                        returnSet.add(c);
                        found++;
                    }
                }
                for(Card c : set){
                    if(found < 3 && c.getValue() == 3){
                        returnSet.add(c);
                        found++;
                    }
                }
            }
        }
        else{
            int found = 0;
            for(Card c : set){
                if(found < 3 && c.getValue() == setType){
                    returnSet.add(c);
                    found++;
                }
            }
        }
        System.out.println("Number of infantry "+ types[0]);
        System.out.println("Number of cavalry "+types[1]);
        System.out.println("Number of artillery "+types[2]);
        System.out.println("Number of wilds "+types[3]);

        return returnSet;
    }

    /**
     * @return Returns a random card from the deck. Decrements size of deck.
     */
    private Card drawCard(){
        Random rand = new Random();
        int indexToDraw = rand.nextInt(cardsLeft); //Pick index
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
        Set<Province> territory = player.getTerritory();
        int owned = player.getTerritory().size();
        int allotedTroops = 3;

        if(owned == 0){
            return 0;
        }

        if((owned/3) > allotedTroops){
            allotedTroops = owned/3;
        }
        
        for(Continent c : world.getContinents()){
            boolean controlled = true;
            Set<Province> provs = c.getProvinces();
            for(Province p: provs){
                if(!(territory.contains(p))){
                    controlled = false;
                }
            }
            if(controlled){
                allotedTroops += c.getBonus();
            }
        }

        return allotedTroops;
    }

    /**
     * @param cards cards being turned in
     * @return Returns bonus troops for set UNIMPLEMENTED
     */
    public int calcCardTurnIn(){
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

                result[0] += 1;
                
        System.out.println("(" + defenders[0] +", " + defenders[1] + ")"); //Prints roll results
        System.out.println("(" + offenders[0] +", " + offenders[1] + ", " + offenders[2] + ")"); //Prints roll results
        System.out.println("(" + result[0] +", " + result[1] + ")"); //Prints final losses
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

    /**
     * @param type
     * @param move
     * @return Determines if move is valid based on current game state. UNIMPLEMENTED
     */
    public Boolean actionIsValid(String type, Object action, Object[] params){

        if(type.equals("placeTroops")){
            try{
                System.out.println("attempting to place");
                Player activePlayer = (Player) params[0];
                int availableTroops = (int) params[1];
                Set<DeployCommand> commandSet = (Set<DeployCommand>) action;
                int totalPlacement = 0;
                for(DeployCommand dc : commandSet){
                    int numTroops = dc.getNumTroops();
                    Province destination = dc.getProvince();

                    totalPlacement += numTroops;

                    Boolean realProvince = false;
                    for(Province p: world.getProvinces()){
                        if (p == destination){
                            realProvince = true;
                        }
                    }

                    if(numTroops < 1 || !(destination.getOwner() == activePlayer) || !realProvince){
                        return false;
                    }
                }

                if(availableTroops >= totalPlacement){
                    return true;
                }

            }
            catch(Exception e){
                System.out.println("Got error: " + e);
                return false;
            }
        }
        else if(type.equals("attacking")){
            try{
                Player activePlayer = (Player) params[0];
                AttackCommand command = (AttackCommand) action;
                int attackingTroops = command.getnumAttackingTroops();
                Province attackingProvince = command.getAttackingProvince();
                Province defendingProvince = command.getDefendingProvince();
                System.out.println(attackingProvince.getOwner() == activePlayer);
                System.out.println(defendingProvince.getOwner() != activePlayer);
                System.out.println(attackingTroops < attackingProvince.getNumSoldiers());

                if(attackingProvince.getOwner() == activePlayer && defendingProvince.getOwner() != activePlayer && attackingTroops < attackingProvince.getNumSoldiers()){
                    return true;
                }

            }
            catch(Exception e){
                System.out.println("Got error: " + e);
                return false;
            }
        }
        else if(type.equals("moveAfterConquer")){
            try{
                int troopsToMove = (int) action;
                int minimumTroops = (int) params[0];
                Province source = (Province) params[1];

                if(troopsToMove >= minimumTroops && source.getNumSoldiers() > troopsToMove && troopsToMove > 0){
                    return true;
                }

            }
            catch(Exception e){
                System.out.println("Got error: " + e);
                return false;
            }
        }
        else if(type.equals("moving")){
            try{
                Player activePlayer = (Player) params[0];
                MoveCommand command = (MoveCommand) action;
                int movingTroops = command.getnumMovingTroops();
                Province source = command.getSourceProvince();
                Province destination = command.getTargetProvince();

                if(world.getProvinces().contains(destination) && world.getProvinces().contains(source) && source.getOwner() == activePlayer &&
                possibleDestinations(source).contains(destination) && source.getNumSoldiers() > movingTroops && movingTroops > 0){
                    return true;
                }

            }
            catch(Exception e){
                System.out.println("Got error: " + e);
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
