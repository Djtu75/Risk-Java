Welcome to Risk Java!

While still not fully implemented, this holds the basic logic for the game of risk.

Provinces are the atomic object that makes the risk board. These provinces are grouped in continents, and those continents comprise the board, otherwise known as the "world." There are also player and card objects to store data on each individual player and the cards they hold. Additionally, the game object contains the logic for each turn, game setup, and various other methods. Finally, the PlayerLogic class is intended to be the parent class for everyone who wants to participate in the competition. It implements the methods laid out in the Logic interface in a basic way. All participants should be able to override these methods with their own implementations, that then get called during runtime by the game object.

Please leave feedback if you have any suggestions, bugfixes, or other ideas!
