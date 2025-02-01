I. GENERAL DESCRIPTION
A text-based variation of the game Poker in Scala, being able to save and load the game freely according to the player’s will.

This is a digital version of the Casino card game. In this game, players compete to acquire points by strategically playing cards from their hands to take cards off the table. The game continues until no player has cards in his/her hand. 

In my variation, the points are added up to the value of the cards the player captured. The game will start a new round if there exists a player that has over 16 points. The player next to the dealer will be the new dealer for the next round. They will play until the game ends, and after that the bonus points are added to the players’ score according to the scoring rules. The last player to capture something will obtain the remaining cards on the table to his/her pile.
Each round begins with the dealer shuffling the deck and dealing four cards to each player and four cards face-up on the table. Players take turns playing cards from their hands, either to capture cards from the table or to place cards on the table (to build or just when not wanting to do anything). The goal is to capture cards in combinations that match the value of the card played from their hand. 

Special cards like Aces, Diamonds-10, and Spades-2 have enhanced values and can be strategically used to capture specific combinations of cards.

The program features a text-based user interface where players can see the game state, make their moves, and interact with the game. It also includes an algorithm to prevent illegal moves.

Additionally, the program supports saving and loading game states from text files, allowing players to continue their games later.


II. USER INTERFACE: 
Launch the program by running the game object (textBased.scala to be specific). 
The game will first ask if the user wants to load an existing game or play a new game. Press “load” if  he wants to load, otherwise any input except from “load” can be pressed to start a new game. 
After that, if a new game starts, it will ask for an integer input of the number of players. If it is smaller than 2 or not an integer, the game will ask the user again until the input is valid. Then the players can type their names, but their names cannot be identical. If identical, the game will ask them to put another name. After that, the game starts.

There are 5 commands that are considered valid: 
- “play” : This is valid for all players except the current dealer. It is used to play cards (put down or capture something).  (E.g. “play ace“) If the played card is not on the player’s hand, it will count as invalid and the player must enter another command. It will automatically avoid choosing a card of Spades (as the scoring counts the number of Spades) or choosing a valuable card with the same name if there are more than one card that shares the same name on the player’s hand and there exists no possible combinations.
For example: 10 of Spades and 10 of Diamonds. It will choose 10 of Spades as 10 of Diamonds has value 16, which is beneficial for the player. For 2 of Spades (value 15)  and 2 of Hearts (2), it will put down 2 of Hearts. For 10 of Clubs and 10 of Spades, it will put down 10 of Clubs as the game will count the Spades cards in players’ pile for bonus points. 
- “hand”:  For players to see their hand of cards in the game, including the dealer.
- “pile”:  For players to see their pile of cards in the game, including the dealer.
- “save”: Save the game as a text file.
- “deal”:  Only for the current dealer so that the game skips to the next player.

III. ALGORITHMS: 
a. Dealer: 
The game rotates the dealer position after each round, starting from the first player. It does this by assigning a boolean variable in class Player and changing it once a new round starts. 

b. Turns: 
The game assigns turns to players by using the method that changes the index of the current player in the players buffer. If the command is “play” and it is valid, the turn will change to the next player in line. If they only call “hand” or “pile” or “save” then it will not change yet. 

c. Playing algorithm: 
When command “play” is called, the algorithm makes iterations (summing up values) by looping among the cards in the player's hand to determine which cards will be called according to his/her command. 
Then it will iterate through all combinations that can be made from the cards on the table, choosing the combinations of the cards that add up to the played card’s value and then selecting the combination that has the largest size. The cards in this combination will be added into the player’s pile and their values are added to the score of the player. And also, this player is also considered as the last person to capture something (for the end game’s scoring). If there is no combination can be found then it just merely puts the card down on the table and does not add anything into the pile. 
When a player exceeds 16 then a new round will start, it will make the scores return to 0 and add the last round score to each player’s total score. The game will clear the table, clear the players’ hands, shuffle the remaining decks and deal to the players again. 
There is a method that has a boolean value to check if the game ends or not (every player has run out of cards) and the end game’s scoring will proceed if it is called.

d. Validating algorithm: The algorithm checks whether the combination of cards selected from the table forms a valid move based on the following criteria:
The sum of the values of the selected cards equals the value of the card played from the player's hand.
The player must have at least one card in their hand that matches the value of the card played.
The card they call to capture must be present on the table.
If the command is not valid, the game will give a reason for it - the player has to make another move. If the number of players or the name of the loaded file is not valid (the file does not exist or the game in the file has already been over), then it will return a feedback that shows the error. Validity can be stored as a false boolean variable and if it is valid, I will change it to “true”.
It also does the same for checking if the current position of the player (dealer or player) is able to call the command. 

e. Saving/loading: 
The sweeps are counted throughout the game and only added to the total score once the game has ended, so it is also in the saving file, along with the table, the deck, last capturer and the turns and who is currently owning the turn. It also adds each player’s score, total score, hand and pile. The gameLoad object will locate the lines of information, convert the text to number or cards according to the parameters’ original types, change the parameters value into the game and return it to the interface. The “turns” part works as a sign to know if the game has ended or not.

f. Scoring: 
It is based on various factors, such as the value of captured cards, sweeps, and special combinations. Players accumulate points based on the cards they capture and the additional scoring conditions. The game will then decide the player that has the highest scores throughout the rounds as the winner.
Interface: It makes sure that the table is shown in every turn and the player’s hand is shown in their turn. It will also show the pile for them when they complete the command. If something is invalid, it will loop the command request until it is valid. It also offers to save the game when it is over, along with saving it when a player calls “save”.

IV. PROGRAM STRUCTURE
a.  Main sub-parts: 
- Game logic (the classes): Handles the core logic of the card game, including managing players, decks, hands, turns, and scoring.
- User interface (textBased.scala): Manages interactions between the user and the game, displaying game state, receiving user input, and providing feedback.
- Game Saver/Loader: Responsible for saving and loading game states to and from files.
  
b. Logic classes: 
- Game: Manages the overall game state, including players, decks, turns, and scoring.
- Player: Represents a player in the game, handling their hand, pile, score, and actions.
- Deck: Represents the deck of cards used in the game, handling shuffling, dealing, and remaining cards.
- Table: Represents the table where cards are played, managing the cards on the table.
- Cards: Represents individual cards with suits, values, and game context.
  
c. User interface classes: 
- textBased: Manages the game progress, determines if the move is valid or not.
- GameSaver, gameLoad: Handles the conversion of game state to and from strings for saving/loading for textBased.

