import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		
		System.out.println("Welcome to Blackjack!");
		
		//Create our playing deck
		deck playingDeck = new deck();
		playingDeck.createFullDeck();
		playingDeck.shuffle();
		
		//Create a deck for the player and dealer
		deck playerDeck = new deck();
		deck dealerDeck = new deck();
		
		double playerMoney = 1000.00;
		
		Scanner userInput = new Scanner(System.in);
		
		//Game Loop
		while (playerMoney > 0) {
			//Play On!
			//Take the players bet
			System.out.println("You have $" + playerMoney + ", how much would you like to bet?");
			double playerBet = userInput.nextDouble();
			if(playerBet > playerMoney) {
				System.out.println("You canot bet more than you have. Please leave.");
				break;
			}
			
			boolean endRound = false;
			
			playingDeck.shuffle();
			
			//Start dealing
			//Player gets two cards
			playerDeck.draw(playingDeck);
			playerDeck.draw(playingDeck);
			
			//Dealer gets two cards
			dealerDeck.draw(playingDeck);
			dealerDeck.draw(playingDeck);
			
			//if player auto gets blackjack
			if((playerDeck.cardsValue() == 21) && playerDeck.cardsValue() > dealerDeck.cardsValue()) {
				System.out.println("Your hand:");
				System.out.println(playerDeck.toString());
				System.out.println("Your hand is valued at: " + playerDeck.cardsValue());
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~BLACKJACK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("YOU WIN! Dealer has no blackjack.");
				playerMoney += playerBet + (playerBet/2);
				endRound = true;
				playerDeck.moveAlltoDeck(playingDeck);
				dealerDeck.moveAlltoDeck(playingDeck);
			//if player and dealer both gets blackjack, then play again.
			}else if(playerDeck.cardsValue() == 21 && playerDeck.cardsValue() == dealerDeck.cardsValue()) {
				System.out.println("Your hand:");
				System.out.println(playerDeck.toString());
				System.out.println("Your hand is valued at: " + playerDeck.cardsValue());
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Close~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("Dealer's hand:");
				System.out.println(dealerDeck.toString());
				System.out.println("Dealer's hand is valued at: " + dealerDeck.cardsValue());
				System.out.println("TIE GAME. You get your money back");
				endRound = true;
				playerDeck.moveAlltoDeck(playingDeck);
				dealerDeck.moveAlltoDeck(playingDeck);
			}else {
		
			while(true) {
				System.out.println("Your hand:");
				System.out.println(playerDeck.toString());
				System.out.println("Your hand is valued at: " + playerDeck.cardsValue());

					//Display Dealer Hand
					System.out.println("Dealer Hand: " + dealerDeck.getCard(0).toString() + " and [Hidden]");
					
					//What does the player want to do?
					System.out.println("Would you like to (1)Hit or (2)Stand?");
					
					int response = userInput.nextInt();
					
					//They Hit
					if(response == 1) {
						playerDeck.draw(playingDeck);
						System.out.println("You drew a: " + playerDeck.getCard(playerDeck.deckSize()-1).toString());
						
						//Bust if > 21
						if(playerDeck.cardsValue() > 21) {
							System.out.println("Bust. Currently valued at: " + playerDeck.cardsValue());
							playerMoney -= playerBet;
							endRound = true;
							break;
						}
					}
					
					//Player Stands
					if(response == 2) {
						break;
					}
				
				
			}
			
				//Reveal Dealer Cards
				System.out.println("Dealer Cards: " + dealerDeck.toString());
				
				//Display total value for Dealer
				System.out.println("Dealer's hand is valued at: " + dealerDeck.cardsValue());
				
				
				//See if dealer has more points than player
				if((dealerDeck.cardsValue() >= 17) && (dealerDeck.cardsValue() > playerDeck.cardsValue() && endRound == false)){
					System.out.println("Dealer beats you!");
					playerMoney -= playerBet;
					endRound = true;
				}
				
				//Dealer draws at 16, stand at 17
				while((dealerDeck.cardsValue() < 17) && endRound == false) {
					dealerDeck.draw(playingDeck);
					System.out.println("Dealer Draws: " + dealerDeck.getCard(dealerDeck.deckSize()-1).toString());
				}
				
				//Determine if dealer busted
				if((dealerDeck.cardsValue() > 21) && endRound == false) {
					System.out.println("Dealer busts! You win!");
					playerMoney += playerBet;
					endRound = true;
				}
				
				//Determine if push
				if((playerDeck.cardsValue() == dealerDeck.cardsValue()) && endRound == false) {
					System.out.println("Push");
					endRound = true;
				}
				
				//Player hand is greater than dealers
				if((playerDeck.cardsValue() > dealerDeck.cardsValue()) && endRound == false) {
					System.out.println("You win the hand!");
					playerMoney += playerBet;
					endRound = true;
				}
				else if (endRound == false) {
					System.out.println("You lose the hand.");
					playerMoney -= playerBet;
					endRound = true;
				}
				
				playerDeck.moveAlltoDeck(playingDeck);
				dealerDeck.moveAlltoDeck(playingDeck);
				
				System.out.println("End of hand.");
			}
		}
		
		
		System.out.println("Game over! You are out of money. :(");

	}

}

class deck {
	
	private ArrayList<card> cards;
	
	public deck () {
		this.cards = new ArrayList<card>();
	}
	
	//returns the size of deck
	public int deckSize() {
		return this.cards.size();
	}
	
	//Generate Cards
	public void createFullDeck() {
		for(Suit cardSuit: Suit.values()) {
			for(Value cardValue: Value.values()) {
				//Add a new card to the mix
				this.cards.add(new card(cardSuit,cardValue));
			}
		}
	}
	
	//Making a temp deck to shuffle and then setting main deck equal to temp deck
	public void shuffle() {
		ArrayList<card> tempDeck = new ArrayList<card>();
		//Using Random
		Random random = new Random();
		int randomCardIndex = 0;
		int originalSize = this.cards.size();
		
		for(int i = 0 ; i < originalSize; i++) {
			//Generate Random Index 		rand.nextInt((max-min)+1)+min;
			randomCardIndex = random.nextInt((this.cards.size()-1-0)+1)+0;
			tempDeck.add(this.cards.get(randomCardIndex));
			
			//Remove from original deck
			this.cards.remove(randomCardIndex);
		}
		
		this.cards = tempDeck;
	}
	
	public String toString() {
		String cardListOutput = "";
		//making a string with all of the card values in it and its going to put each index in there using the i integer
		for(card aCard: this.cards) {
			cardListOutput += "\n" + aCard.toString();
		}
		return cardListOutput;
	}
	
	//remove an index from our arraylist of cards
	public void removeCard(int i) {
		this.cards.remove(i);
	}
	
	public card getCard(int i) {
		return this.cards.get(i);
	}
	
	//add a card to the deck
	public void addCard(card addCard) {
		this.cards.add(addCard);
	}
	
	
	//Draws from the deck
	public void draw(deck comingFrom) {
		this.cards.add(comingFrom.getCard(0));
		comingFrom.removeCard(0);
	}
	
	public void moveAlltoDeck(deck moveTo) {
		int thisDeckSize = this.cards.size();
		
		//put cards into moveTo deck
		for(int i = 0; i < thisDeckSize; i++) {
			moveTo.addCard(this.getCard(i));
		}
		
		for(int i = 0; i < thisDeckSize; i++) {
			this.removeCard(0);
		}
	}
	
	//Return total value of cards in deck
	public int cardsValue() {
		int totalValue = 0;
		int aces = 0;
		
		for(card aCard: this.cards) {
			switch(aCard.getValue()) {
			case TWO: totalValue += 2; break;
			case THREE: totalValue += 3; break;
			case FOUR: totalValue += 4; break;
			case FIVE: totalValue += 5; break;
			case SIX: totalValue += 6; break;
			case SEVEN: totalValue += 7; break;
			case EIGHT: totalValue += 8; break;
			case NINE: totalValue += 9; break;
			case TEN: totalValue += 10; break;
			case JACK: totalValue += 10; break;
			case QUEEN: totalValue += 10; break;
			case KING: totalValue += 10; break;
			case ACE: aces += 1; break;	
			}
		}
		
		for(int i = 0; i < aces; i++) {
			if(totalValue > 10) {
				totalValue += 1;
			}else {
				totalValue += 11;
			}
		}
		
		return totalValue;
	}
}

class card {
	
	private Suit suit; 
	private Value value;
	
	//refers to this instance of the card
	public card (Suit suit, Value value) {
		this.value = value;
		this.suit = suit;
	}
	
	//Print out the value and the suit 
	public String toString() {
		return this.suit.toString() + "-" + this.value.toString();
	}
	
	//gets the value to calculate the overall hand
	public Value getValue() {
		return this.value;
	}
	
}
