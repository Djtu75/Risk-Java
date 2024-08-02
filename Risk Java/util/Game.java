package util;

import java.util.Random;
import participant.PlayerLogic;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

public class Game {
    
    private boolean started = false;
    private boolean gameOver = false;
    private int turnNum = 0;
    private Player[] players;
    private World world;
    private Card[] deck;
    private int cardsLeft;

    public Game(Player[] players, World world){
        this.players = players;
        this.world = world;
    }

    public String startGame(int initialTroops){
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
                            Object[] action = activePlayer.getLogic().draftPhase(troopsToPlace);
                            Object[] parameters = {activePlayer};
                            if(actionIsValid("placetroops", action, parameters)){
                                int numTroopsPlaced = (int) action[0];
                                Province destination = (Province) action[1];
                                troopsToPlace -= numTroopsPlaced;
                                destination.addSoldiers((int) action[0]);
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
                    gameOver = checkGameOver();
                    //Normal turn
                    for(int i = 0; i < players.length; i++){
                        Player activePlayer = players[i];
                        activePlayer.getLogic().beginTurn(); //Tell player their turn is starting
                        Object[] action;
                        int cardBonus = 0;
                        //Handle cards
                        int troopsToPlace = calcTroopAllot(activePlayer); //Calc troops player should get to place
                        if(activePlayer.getNumCards() >= 5){ //If 5 or more cards in hand, force turn in
                            action = activePlayer.getLogic().turnInCards(true, troopsToPlace);
                            Object[] parameters = {activePlayer};
                            if(actionIsValid("turnInCards", action, parameters)){
                                Set<Card> cardsToTurnIn = new HashSet<Card>(Arrays.asList((Card)action[0], (Card)action[1], (Card)action[2]));
                                for(Card c: cardsToTurnIn){ //Returns cards to deck
                                    returnCard(c);
                                }
                                cardBonus = calcCardTurnIn(cardsToTurnIn);
                                Set<Card> tempCards = activePlayer.getCards();
                                tempCards.removeAll(cardsToTurnIn);
                                activePlayer.setCards(tempCards);
                            }
                            else{ //If player attempts to turn in invalid set, clear player's hand
                                Set<Card> tempCards = activePlayer.getCards();
                                tempCards.clear();
                                activePlayer.setCards(tempCards);
                            }
                        }
                        else{ //If fewer than 5 cards, ask player to turn in cards (if invalid input, do nothing as it is not required)
                            action = activePlayer.getLogic().turnInCards(false, troopsToPlace);
                            Object[] parameters = {activePlayer};
                            if(actionIsValid("turnInCards", action, parameters)){
                                Set<Card> cardsToTurnIn = new HashSet<Card>(Arrays.asList((Card)action[0], (Card)action[1], (Card)action[2]));
                                for(Card c: cardsToTurnIn){ //Returns cards to deck
                                    returnCard(c);
                                }
                                cardBonus = calcCardTurnIn(cardsToTurnIn);
                                Set<Card> tempCards = activePlayer.getCards();
                                tempCards.removeAll(cardsToTurnIn);
                                activePlayer.setCards(tempCards);
                            }
                        }
                        //Handle placement phase
                        troopsToPlace += cardBonus;
                        while(!phasefinished){
                            action = activePlayer.getLogic().draftPhase(troopsToPlace);
                            Object[] parameters = {activePlayer, troopsToPlace};
                            if(actionIsValid("placetroops", action, parameters)){
                                int numTroopsPlaced = (int) action[0];
                                Province destination = (Province) action[1];
                                troopsToPlace -= numTroopsPlaced;
                                destination.addSoldiers((int) action[0]);
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
                            action = activePlayer.getLogic().attackPhase(); //Prompt player to choose number of troops to attack with, and the two provinces involved
                            Object[] parameters = {activePlayer};
                            if(actionIsValid("attacking", action, parameters)){
                                int attackingTroops = (int) action[0];
                                Province attackingProvince = (Province) action[1];
                                Province defendingProvince = (Province) action[2];
                                int[] result = doBattle((defendingProvince.getNumSoldiers()), attackingTroops); //Do one set of dice rolls
                                attackingProvince.addSoldiers(result[1]*-1); //remove killed soldiers
                                defendingProvince.addSoldiers(result[0]*-1); //remove killed soldiers
                                if(defendingProvince.getNumSoldiers() == 0){ //If no troops left in defending province, that province is conquered
                                    gainCard = true;
                                    activePlayer.addTerritory(defendingProvince);
                                    action = new Object[]{activePlayer.getLogic().moveAfterConquer(attackingProvince, defendingProvince)};
                                    Object[] parameters2 = {attackingTroops, attackingProvince};
                                    if(actionIsValid("moveAfterConquer", action, parameters2)){
                                        int troopsToMove = (int) action[0];
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
                            action = activePlayer.getLogic().movePhase();
                            Object[] parameters = {activePlayer};
                            if(actionIsValid("moving", action, parameters)){
                                int movingTroops = (int) action[0];
                                Province sourceProvince = (Province) action[1];
                                Province destinationProvince = (Province) action[2];
                                destinationProvince.addSoldiers(movingTroops);
                                sourceProvince.addSoldiers(movingTroops*-1);
                            }
                            else{
                                phasefinished = true;
                            }
                        }

                        activePlayer.getLogic().endTurn(); //Signal to player that their turn is ending
                    }
                    turnNum++;
                    
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
    public static int calcTroopAllot(Player player){

        return 0;
    }

    /**
     * @param cards cards being turned in
     * @return Returns bonus troops for set UNIMPLEMENTED
     */
    public static int calcCardTurnIn(Set<Card> cards){

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
    public Boolean actionIsValid(String type, Object[] action, Object[] params){

        if(type.equals("placeTroops")){
            try{
                Player activePlayer = (Player) params[0];
                int availableTroops = (int) params[1];
                int numTroops = (int) action[0];
                Province destination = (Province) action[1];

                if(world.getProvinces().contains(destination) && destination.getOwner() == activePlayer && availableTroops >= numTroops && numTroops > 0){
                    return true;
                }

            }
            catch(Exception e){
                System.out.println("Got error: " + e);
                return false;
            }
        }
        else if(type.equals("attacking")){

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
                System.out.println("Got error: " + e);
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

        Game game = new Game(new Player[]{player1, player2, player3, player4}, null);
        game.startGame(120);

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
