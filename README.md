Welcome to Risk Java!

This project was designed for use in TU ACM's Risk Competition for fall 2024.

There will still be active development on this up until the submission due date for competitors. However, this will be on the graphical side and the backend, so user functionality should not be effected.

Important Objects:
Snapshot- provided everytime the player is required to sumbit a action, describes the current state of the game
Game - run the game and provides useful static methods for playerLogic files to call
World - contains all provinces in the game as well as what continents are in the game
Continents - describe what provinces comprise the continent as well as the bonus troops for owning it
Province - the atomic object of the game, describes who owns it, what it is adjacent to, and how many troops are on it
Card - an object representing the physical cards one would draw, these are drawn and turned in for troops

Participants should implement their player by inheriting from the PlayerLogic class. There are two example players already in the code, labeled ExamplePlayer1 and ExamplePlayer2.
One is incredibly offensive and one is incredibly defensive, designed to showcase multiple ways to code your player.

Provinces are the atomic object that makes the risk board. These provinces are grouped in continents, and those continents comprise the board, otherwise known as the "world." There are also player and card objects to store data on each individual player and the cards they hold. Additionally, the game object contains the logic for each turn, game setup, and various other methods. Finally, the PlayerLogic class is intended to be the parent class for everyone who wants to participate in the competition. It implements the methods laid out in the Logic interface in a basic way. All participants should be able to override these methods with their own implementations, that then get called during runtime by the game object.

Please leave feedback if you have any suggestions, bugfixes, or other ideas!
