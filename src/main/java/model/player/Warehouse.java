package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;

import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;
import java.util.ArrayList;

//TODO: remove various exceptions raised by methods and clean documentation
public class Warehouse {
	private ArrayList<Resource> top_resource;
	private ArrayList<Resource> middle_resources;
	private ArrayList<Resource>bottom_resources;

	public Warehouse(){
		top_resource = new ArrayList<Resource>();
		middle_resources = new ArrayList<Resource>();
		bottom_resources = new ArrayList<Resource>();
	}

	public ArrayList<Resource>  getTopResource(){
		 return top_resource;
	}

	public ArrayList<Resource> getMiddleResources(){
		return middle_resources;
	}

	public ArrayList<Resource> getBottomResources(){
		return bottom_resources;
	}

	/**
	* Swaps two rows if possible
	*
	* @param i,j indicate which rows to swap
	* @throws IllegalArgumentException if the parameters are invalid or the swap between the rows cannot be made
	*/
	public void swapRows (int i, int j) throws IllegalArgumentException {
		ArrayList<Resource> swap;
		if (i>j){
			int temp = j;
			j = i;
			i = temp;
		}
		if (i<1 || j>3){
			throw new IllegalArgumentException();
		} else {
			if (i == 1) {
				if (j == 2) {
					if (middle_resources.size() < 2){
						swap = middle_resources;
						middle_resources = top_resource;
						top_resource = swap;
					}
					else {throw new IllegalArgumentException();}
				} else if (j == 3) {
					if (bottom_resources.size() < 2){
						swap = bottom_resources;
						bottom_resources = top_resource;
						top_resource = swap;
					}
					else {throw new IllegalArgumentException();}
				}
			} else if ( i == 2) {
				if (j == 3) {
					if (bottom_resources.size() < 3){
						swap = bottom_resources;
						bottom_resources = middle_resources;
						middle_resources = swap;
					}
					else {throw new IllegalArgumentException();}
				}
			}
		}
	}

	/**
	 * Checks whether the given resource is already present in the Strongbox
	 *
	 * @param resource is the resource to be checked
	 */
	private boolean isPresent (Resource resource){
		return top_resource.contains(resource) || middle_resources.contains(resource) || bottom_resources.contains(resource);
	}

	/**
	 * Checks whether the given ArrayList of resources containes only one resource type
	 * 
	 * @param res is the ArrayList of resources to be checked
	 */
	private boolean isAllTheSame (ArrayList<Resource> res){
		if (!res.isEmpty()){
			Resource check = res.get(0);
			for (Resource x : res){
				if (!x.equals(check)){
					return false;
				}
			}
		}
		return true;
	}

	public void clearWarehouse(){
		this.top_resource.clear();
		this.middle_resources.clear();
		this.bottom_resources.clear();
	}

	/**
	 * STORE RELATED METHODS
	 */ 

	/**
	 * Checks whether the given ArrayList of resources can be stored on the top shelf
	 * 
	 * @param res is the ArrayList of resources to be checked
	 */
	public boolean canBeStoredTop (ArrayList<Resource> res){
		if (res.isEmpty()){
			return true;
		} else {
			return res.size() <= 1 - top_resource.size() && isAllTheSame(res) && !isPresent(res.get(0));
		}
	}
	
	/**
	 * Checks whether the given ArrayList of resources can be stored on the middle shelf
	 * 
	 * @param res is the ArrayList of resources to be checked
	 */
	public boolean canBeStoredMiddle (ArrayList<Resource> res){
		if (res.isEmpty()){
			return true;
		} else if (middle_resources.isEmpty()){
			return res.size() <= 2 - middle_resources.size() && isAllTheSame(res) && !isPresent(res.get(0));
		} else {
			return res.size() <= 2 - middle_resources.size() && isAllTheSame(res) && res.get(0).equals(middle_resources.get(0));
		}
	}

	/**
	 * Checks whether the given ArrayList of resources can be stored on the bottom shelf
	 * 
	 * @param res is the ArrayList of resources to be checked
	 */
	public boolean canBeStoredBottom (ArrayList<Resource> res){
		if (res.isEmpty()){
			return true;
		} else if (bottom_resources.isEmpty()){
			return res.size() <= 3 - bottom_resources.size() && isAllTheSame(res) && !isPresent(res.get(0));
		} else {
			return res.size() <= 3 - bottom_resources.size() && isAllTheSame(res) && res.get(0).equals(bottom_resources.get(0));
		}
	}

	/**
	 * Stores resources in the middle shelf
	 *
	 * @param res is the array list of resources
	 * @exception IndexOutOfBoundsException is thrown if the quantity requested is greater than the space available
	 * @exception IllegalArgumentException is thrown if the list contains different resources or resources which cannot be put in the shelf
	 **/
	public void storeTop(ArrayList<Resource> res){
	/*	if (res.isEmpty()){
			return;
		} else if (res.size() > 1 - top_resource.size()){
			throw new IndexOutOfBoundsException();
		} else if (!isAllTheSame(res)) {
			throw new IllegalArgumentException();
		} else {
			if (top_resource.isEmpty() && !isPresent(res.get(0))){
				top_resource.addAll(res);
			} else {throw new IllegalArgumentException();}
		}
		*/
		top_resource.addAll(res);
	}
	
	/**
	 * Stores resources in the middle shelf
	 *
	 * @param res is the array list of resources
	 * @exception IndexOutOfBoundsException is thrown if the quantity requested is greater than the space available
	 * @exception IllegalArgumentException is thrown if the list contains different resources or resources which cannot be put in the shelf
	 **/
	public void storeMiddle (ArrayList<Resource> res){
		/*
		if (res.isEmpty()){
			return;
		} else if (res.size() > 2 - middle_resources.size()){
			throw new IndexOutOfBoundsException();
		} else if (!isAllTheSame(res)) {
			throw new IllegalArgumentException();
		} else {
			if (!isPresent(res.get(0))){
				if (middle_resources.isEmpty()){
					middle_resources.addAll(res);
				} else {throw new IllegalArgumentException();}
			} else { 
				if (res.get(0).equals(middle_resources.get(0))){
					middle_resources.addAll(res);
				} else {throw new IllegalArgumentException();}
			}		
		}
		*/
		middle_resources.addAll(res);
	}

	/**
	 * Stores resources in the bottom shelf
	 *
	 * @param res is the array list of resources
	 * @exception IndexOutOfBoundsException is thrown if the quantity requested is greater than the space available
	 * @exception IllegalArgumentException is thrown if the list contains different resources or resources which cannot be put in the shelf
	 **/
	public void storeBottom (ArrayList<Resource> res) {
		/*
		if (res.isEmpty()){
			return;
		} else if (res.size() > 3 - bottom_resources.size()){
			throw new IndexOutOfBoundsException();
		} else if (!isAllTheSame(res)) {
			throw new IllegalArgumentException();
		} else {
			if (!isPresent(res.get(0))){
				if (bottom_resources.isEmpty()){
					bottom_resources.addAll(res);
				} else {throw new IllegalArgumentException();}
			} else { 
				if (res.get(0).equals(bottom_resources.get(0))){
					bottom_resources.addAll(res);
				} else {throw new IllegalArgumentException();}
			}		
		}
		*/
		bottom_resources.addAll(res);
	}
	
	
	/**
	 * PAYMENT RELATED METHODS
	 */

	/**
	 * Checks whether the given ArrayList of resources is contained in the top shelf
	 * 
	 * @param res is the ArrayList of resources to be checked
	 */
	public boolean isContainedTop (ArrayList<Resource> res){
		if (res.isEmpty()){
			return true;
		} else {
			return res.size() <= top_resource.size() && res.get(0).equals(top_resource.get(0));
		}
	}

	/**
	 * Checks whether the given ArrayList of resources is contained in the middle shelf
	 * 
	 * @param res is the ArrayList of resources to be checked
	 */
	public boolean isContainedMiddle (ArrayList<Resource> res){
		if (res.isEmpty()){
			return true;
		} else {
			return res.size() <= middle_resources.size() && isAllTheSame(res) && res.get(0).equals(middle_resources.get(0));
		}
	}

	/**
	 * Checks whether the given ArrayList of resources is contained in the bottom shelf
	 * 
	 * @param res is the ArrayList of resources to be checked
	 */
	public boolean isContainedBottom (ArrayList<Resource> res){
		if (res.isEmpty()){
			return true;
		} else {
			return res.size() <= bottom_resources.size() && isAllTheSame(res) && res.get(0).equals(bottom_resources.get(0));
		}
	}

	/**
	 * Removes resource from the top shelf
	 *
	 * @param res is the array list of resources
	 * @exception IndexOutOfBoundsException is thrown if the quantity requested is greater than the resource available
	 * @exception IllegalArgumentException is thrown if the list contains a type of resources not contained in the row
	 **/
	public void getFromTop(ArrayList<Resource> res){
		/*
		if (res.size() > top_resource.size()){
			throw new IndexOutOfBoundsException();
		} else {
			if (!res.isEmpty()) {
				if (isAllTheSame(res) || res.get(0).equals(top_resource.get(0))){
					for (Resource x :res) {
						top_resource.remove(x);
					}
				} else {throw new IllegalArgumentException();}
			}
		}
		*/
		for (Resource x :res) {
			top_resource.remove(x);
		}
	}

	/**
	 * Removes resource from the middle shelf
	 *
	 * @param res is the array list of resources
	 * @exception IndexOutOfBoundsException is thrown if the quantity requested is greater than the resource available
	 * @exception IllegalArgumentException is thrown if the list contains a type of resources not contained in the row
	 **/
	public void getFromMiddle(ArrayList<Resource> res){
		/*if (res.size() > middle_resources.size()){
			throw new IndexOutOfBoundsException();
		} else {
			if (!res.isEmpty()) {
				if (isAllTheSame(res) || res.get(0).equals(middle_resources.get(0))){
					for (Resource x :res) {
						middle_resources.remove(x);
					}
				} else {throw new IllegalArgumentException();}
			}
		}*/
		for (Resource x :res) {
			middle_resources.remove(x);
		}
	}

	/**
	 * Removes resource from the bottom shelf
	 *
	 * @param res is the array list of resources
	 * @exception IndexOutOfBoundsException is thrown if the quantity requested is greater than the resource available
	 * @exception IllegalArgumentException is thrown if the list contains a type of resources not contained in the row
	 **/
	public void getFromBottom(ArrayList<Resource> res){
		/*if (res.size() > bottom_resources.size()){
			throw new IndexOutOfBoundsException();
		} else {
			if (!res.isEmpty()) {
				if (isAllTheSame(res) || res.get(0).equals(bottom_resources.get(0))){
					for (Resource x :res) {
						bottom_resources.remove(x);
					}
				} else {throw new IllegalArgumentException();}
			}
		}*/
		for (Resource x :res) {
			bottom_resources.remove(x);
		}
	}
}
