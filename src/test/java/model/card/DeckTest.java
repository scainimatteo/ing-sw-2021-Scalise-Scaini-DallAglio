package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*; 
import static org.junit.jupiter.api.Assertions.*;   

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;
import java.lang.IndexOutOfBoundsException;
import java.util.NoSuchElementException;

public class DeckTest { 
	int max = 20;
	Deck <Random> deck;
	Random random;

	@BeforeEach
	public void init(){
		deck = new Deck(max);
		random = new Random();
	}
	
	/**
	* test di inizializzazione corretta
	*/
	@Test
	public void testInit(){
		assertTrue (deck.isEmpty());
		assertNull (deck.peekTopCard());
		assertThrows (NoSuchElementException.class, () -> {deck.draw();});
	}

	/**
	* test in sequenza di aggiunte casuali
	*/ 

	@RepeatedTest(value = 5)
	public void testAdd() {
		deck.add(random);
		assertEquals (deck.peekTopCard(), random);
		while (deck.size() < max) {	
			random = new Random();
			deck.add(random);
			assertEquals (deck.peekTopCard(), random);
		}
		assertThrows (IndexOutOfBoundsException.class, () -> {deck.add(random);});
		assertEquals (deck.peekTopCard(), random);
	}	

	/**
	* test di rimozioni casuali
	*/
	@RepeatedTest(value = 5)
	public void testDraw(){
		Random[] random_deck = new Random[max];
		for (int i = 0; i<max; i++){
			random_deck[i] = random;
			deck.add(random);
			random = new Random();
			assertEquals (deck.size(), i+1);
		}
		while (!deck.isEmpty()){
			assertEquals(random_deck[deck.size()-1], deck.draw() );
		}
		assertThrows (NoSuchElementException.class, () -> {deck.draw();});
	}
	
	/**
	* tests shuffling
	*/
	@RepeatedTest(value = 10)
	public void testShuffle(){
		Random[] random_deck = new Random[max];
		Random[] copy = new Random[max];
		HashMap <Random, Integer> check = new HashMap<Random, Integer>();
		for (int i = 0; i<max; i++){
			random_deck[i] = random;
			deck.add(random);
			check.put(random, 1);
			random = new Random();
		}
		deck.shuffle();
		Iterator <Random> iterator = deck.iterator();
		for (int i = 0; i<max; i++){
			copy[i] = iterator.next();
			check.put(copy[i], check.get(copy[i]) + 1);
		}
		for (Integer x : check.values()){
			assertEquals (x, 2);
		}
		assertFalse (random_deck.equals(copy));
	}
}
