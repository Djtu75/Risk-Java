package util;

import java.util.Random;
import java.awt.Color;

import participant.DummyPlayer;
import participant.ExamplePlayer1;
import participant.Jakefrozen;
import participant.PlayerLogic;

public class RunTestsOnPlayer {

    public static void main(String[] args){
    //INSERT PLAYER TO BE TESTED HERE VVV
    Jakefrozen sl1 = new Jakefrozen();
    //INSERT PLAYER TO BE TESTED HERE ^^^


    /*
     * Welcome!
     * This is a simple test runner to help you make sure your player can function properly.
     * 
     * After you've inserted your player above, you can look at the tests currently available below.
     * Each has a short description, but just running them should be pretty self explanatory.
     * 
     * You can enable or disable each test by just changing them to false. In each scenario, it should end with your player winning.
     * 
     */

    DummyPlayer sl2 = new DummyPlayer();

    Boolean runAttackTest = true; //Tests player's ability to win if given overwhelming advantage
    Boolean runMoveTest = true; //Tests player's ability to move troops out of dead ends
    Boolean runCardTest = true; //Tests player's ability to turn in cards (and then attack with the gained troops)
    Boolean runConquerTest = true; //Tests if the player can use all mechanisms togther against a dummy opponent to win
    PlayerLogic[] playerLogics = {sl1, sl2};

    //This test gives the player 50 troops to kill an adjacent opponent with 1 troop
    if(runAttackTest){
        Player[] players = setUpPlayers(playerLogics);

        World world = MapReader.readFile("Risk Java\\util\\AttackTest.txt");
        
        Game game = new TestGame(players, world, world.getGraphicProvinces(), false, false, false, true, false, null);
        String[] backgroundImages = {"Risk Java\\util\\top-view-of-sea-in-the-ocean-background-footage-for-traveling-4k-free-video.jpg"};

        RenderEarth test = new RenderEarth(world.getGraphicProvinces(), backgroundImages, players, game);

        String result = game.startGame(1, test);
        System.out.println("Result: " + result);
    }

    //This test places the player in center of a cross, and the player has to avoid getting their troops stuck in the ends of the cross
    if(runMoveTest){

        Player[] players = setUpPlayers(playerLogics);

        World world = MapReader.readFile("Risk Java\\util\\MoveTest.txt");
        
        Game game = new TestGame(players, world, world.getGraphicProvinces(), false, false, false, true, true, "prov3");
        String[] backgroundImages = {"Risk Java\\util\\top-view-of-sea-in-the-ocean-background-footage-for-traveling-4k-free-video.jpg"};

        RenderEarth test = new RenderEarth(world.getGraphicProvinces(), backgroundImages, players, game);

        String result = game.startGame(1, test);
        System.out.println("Result: " + result);
    }

    //This test disables draft, so the only way to get troops is turning in the 5 cards provided at the start of the game.
    if(runCardTest){

        Player[] players = setUpPlayers(playerLogics);

        World world = MapReader.readFile("Risk Java\\util\\CardTest.txt");
        
        Game game = new TestGame(players, world, world.getGraphicProvinces(), false, true, true, false, false, null);
        String[] backgroundImages = {"Risk Java\\util\\top-view-of-sea-in-the-ocean-background-footage-for-traveling-4k-free-video.jpg"};

        RenderEarth test = new RenderEarth(world.getGraphicProvinces(), backgroundImages, players, game);

        String result = game.startGame(1, test);
        System.out.println("Result: " + result);
    }

    //This test puts the player in the regular world map, but with a do-nothing opponent
    if(runConquerTest){

        Player[] players = setUpPlayers(playerLogics);

        World world = MapReader.readFile("Risk Java\\util\\BasicWorld.txt");
        
        Game game = new Game(players, world, world.getGraphicProvinces());
        String[] backgroundImages = {"Risk Java\\util\\top-view-of-sea-in-the-ocean-background-footage-for-traveling-4k-free-video.jpg", "Risk Java\\util\\worldimg.png"};

        RenderEarth test = new RenderEarth(world.getGraphicProvinces(), backgroundImages, players, game);

        String result = game.startGame(1, test);
        System.out.println("Result: " + result);
    }
    
    
    }

    private static Player[] setUpPlayers(PlayerLogic[] playerLogics){
        
        Color[] defaultColors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.RED, Color.YELLOW, Color.PINK};
        Player[] players = new Player[playerLogics.length];
        for(int i = 0; i < playerLogics.length; i++){

            Player player = new Player(Integer.toString(i+1), playerLogics[i]);
            if(playerLogics[i].getCustomName() != null){
                player = new Player(playerLogics[i].getCustomName(), playerLogics[i]);
            }

            if(playerLogics[i].getCustomColor() != null){
                player.setColors(playerLogics[i].getCustomColor()[0], playerLogics[i].getCustomColor()[1]);
            }
            else{
                Random rand = new Random();
                Color c1 = null;
                Color c2 = null;
                int a = 0;
                while(c1 == null){
                    a = rand.nextInt(defaultColors.length);
                    c1 = defaultColors[a];
                }
                defaultColors[a] = null;
                while(c2 == null){
                    a = rand.nextInt(defaultColors.length);
                    c2 = defaultColors[a];
                }
                defaultColors[a] = null;
                player.setColors(c1, c2);
            }

            if(playerLogics[i].getCustomProfile() != null){
                player.setProfilePictureLink(playerLogics[i].getCustomProfile());
            }
            else{
                player.setProfilePictureLink("https://waapple.org/wp-content/uploads/2021/06/Variety_Cosmic-Crisp-transparent-658x677.png");
            }

            players[i] = player;
        }
        
        //In the tragic case that two players choose the same colors, one will have to be set to a different color.
        for(int i = 0; i < players.length; i++){
            for(int j = i+1; j < players.length; j++){
                if(((Math.abs(players[i].getColors()[0].getRed() - players[j].getColors()[0].getRed())) < 10) && ((Math.abs(players[i].getColors()[0].getBlue() - players[j].getColors()[0].getBlue())) < 10) && ((Math.abs(players[i].getColors()[0].getGreen() - players[j].getColors()[0].getGreen())) < 10)){
                    if(((Math.abs(players[i].getColors()[1].getRed() - players[j].getColors()[1].getRed())) < 10) && ((Math.abs(players[i].getColors()[1].getBlue() - players[j].getColors()[1].getBlue())) < 10) && ((Math.abs(players[i].getColors()[1].getGreen() - players[j].getColors()[1].getGreen())) < 10)){
                        Random rand = new Random();
                        Color c1 = null;
                        Color c2 = null;
                        int a = 0;
                        while(c1 != null){
                            a = rand.nextInt(defaultColors.length);
                            c1 = defaultColors[a];
                        }
                        defaultColors[a] = null;
                        while(c2 != null){
                            a = rand.nextInt(defaultColors.length);
                            c2 = defaultColors[a];
                        }
                        defaultColors[a] = null;
                        players[j].setColors(c1, c2);
                    }
                }
            }
        }
        return players;
    }
}
