package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;
 

public interface Storage{
//		public void putResource (Resource res) throws IllegalArgumentException;
		public void getResource (Resource res) throws NoSuchElementException;
}
