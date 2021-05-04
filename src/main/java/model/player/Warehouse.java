package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.player.Storage;

import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;

public class Warehouse implements Storage {
	private Resource top_resource = null;
	private Resource[] middle_resources = {null, null};
	private Resource[] bottom_resources = {null, null, null};

	public Warehouse(){
	}

	public Resource getTopResource(){
		 return top_resource;
	}

	public Resource[] getMiddleResources(){
		return middle_resources;
	}

	public Resource[] getBottomResources(){
		return bottom_resources;
	}
	
	/**
	* Swaps two rows if possible
	*
	* @param i,j indicate which rows to swap
	* @throws IllegalArgumentException if the parameters are invalid or the swap between the rows cannot be made
	*/
	public void swapRows (int i, int j) throws IllegalArgumentException {
		if (i>j){
			int temp = j;
			j = i;
			i = temp;
		}
		if (i<1 || j>3){
			throw new IllegalArgumentException();
		}
		else {
			if (i == 1){
				if (j == 2){
					if (middle_resources[1] == null){
						Resource temp = top_resource;
						top_resource = middle_resources[0];
						middle_resources[0] = temp;
					}
					else {throw new IllegalArgumentException();}
				}
				else if (j == 3){
					if (bottom_resources[1] == null && bottom_resources[2] == null){
						Resource temp = top_resource;
						top_resource = bottom_resources[0];
						bottom_resources[0] = temp;
					}
					else {throw new IllegalArgumentException();}
				}
			} 
			else if (i ==2){
				if (j == 3){
					if (bottom_resources[2] == null){
						Resource temp = middle_resources[0];
						middle_resources[0] = bottom_resources[0];
						bottom_resources[0] = temp;
						temp = middle_resources[1];
						middle_resources[1] = bottom_resources[1];
						bottom_resources[1] = temp;
					}
					else {throw new IllegalArgumentException();}
				}
			}
		}
	}
				
	/**
	 * @param new_resource is the resource to be included in the Warehouse
	 * @return true if new_resource can be added
	 */
	public boolean isPossibleToInsert(Resource new_resource){
		if (top_resource != null){
			if (!middle_resources[0].equals(new_resource) || middle_resources[1] != null){
				if (!bottom_resources[0].equals(new_resource) || (bottom_resources[1] != null && bottom_resources[2] != null)){
					return false;
				}
			}
		}
		return true;
	}

	private boolean isPresent (Resource resource){
		if (!top_resource.equals(resource)){
			if (!middle_resources[0].equals(resource)){
				if (!bottom_resources[0].equals(resource)){
					return false;
				}
			}
		}
		return true;
	}


	 /**	
	 * tries to insert the given resource into the warehouse
	 *
	 * @param new_resources is the resource to be inserted
	 * @throws IllegalArgumentException if the resource cannot be inserted
	 */
	public void insert(Resource new_resource) throws IllegalArgumentException{
		if (isPresent(new_resource)){	
			if(middle_resources[0].equals(new_resource) && middle_resources[1] == null){
				middle_resources[1] = new_resource;
				return;
			}
			else if (bottom_resources[0].equals(new_resource)){
				if(bottom_resources[1] == null){
					bottom_resources[1] = new_resource;
					return;
				}
				else if (bottom_resources[2] == null){
					bottom_resources[2] = new_resource;
					return;
				}
			}
		} else {
			if (top_resource == null) {
				top_resource = new_resource;
				return;
			}
			else if(middle_resources[0] == null){
				middle_resources[0] = new_resource;
				return;
			}
			else if (bottom_resources[0] == null){
				bottom_resources[0] = new_resource;
				return;
			}
		}
		throw new IllegalArgumentException();
	}


	/**
	 * @param new_resources is an array of resources obtained from the market that need to be inserted in the warehouse
	 * @return the number of resources that cannot be inserted
	 */
	public int tryToInsert(Resource[] new_resources){
		int resources_not_inserted = 0;

		for(Resource resource : new_resources){
			try {
				insert(resource);
			} catch(IllegalArgumentException e) {
				resources_not_inserted += 1;
			}
		}

		return resources_not_inserted;
	}

	@Override
	public void getResource(Resource res){
		return;
	}

	/**
	 * @param resource_type is the type of resource requested from the player
	 * @param quantity is the number of resource requested
	 * @return an array of the resources requested
	 */
	public Resource[] getFromWarehouse(Resource resource_type, int quantity) throws IllegalArgumentException, IndexOutOfBoundsException{
		Resource[] to_return = getFromTop(resource_type, quantity);
		return to_return;
	}

	/**
	 * @param resource_type is the type of resource requested from the player
	 * @param quantity is the number of resource requested
	 * @return the array of the resources requested if is of the same type as top_resource and the quantity requested is available . Returns an exception if the quantity requested is not available. Calls the next method if top_resource is empty or resource_type and top_resource are not the same.
	 * @exception IndexOutOfBoundsException is thrown if the quantity requested is greater than the resource available
	 */
	private Resource[] getFromTop(Resource resource_type, int quantity){
		if (this.top_resource != null && resource_type.equals(top_resource)){
			if (quantity == 1){
				Resource[] to_return = {top_resource};
				this.top_resource = null;
				return to_return;
			} else {
				throw new IndexOutOfBoundsException();
			}
		} else {
			return getFromMiddle(resource_type, quantity);
		}
	}

	/**
	 * @param resource_type is the type of resource requested from the player
	 * @param quantity is the number of resource requested
	 * @return the array of the resources requested if is of the same type as middle_resource and the quantity requested is available . Returns an exception if the quantity requested is not available. Calls the next method if middle_resources is empty or resource_type and middle_resources are not the same.
	 * @exception IndexOutOfBoundsException is thrown if the quantity requested is greater than the resource available
	 */
	private Resource[] getFromMiddle(Resource resource_type, int quantity){
		if (this.middle_resources[0] != null && resource_type.equals(middle_resources[0])){
			if (this.middle_resources[1] != null){
				if (quantity == 2){
					Resource[] to_return = {middle_resources[0], middle_resources[1]};
					this.middle_resources[1] = null;
					this.middle_resources[0] = null;
					return to_return;
				} else if (quantity == 1){
					Resource[] to_return = {middle_resources[1]};
					this.middle_resources[1] = null;
					return to_return;
				} else {
					throw new IndexOutOfBoundsException();
				}
			} else {
				if (quantity == 1){
					Resource[] to_return = {middle_resources[0]};
					this.middle_resources[0] = null;
					return to_return;
				} else {
					throw new IndexOutOfBoundsException();
				}
			}
		} else {
			return getFromBottom(resource_type, quantity);
		}
	}

	/**
	 * @param resource_type is the type of resource requested from the player
	 * @param quantity is the number of resource requested
	 * @return the array of the resources requested if is of the same type as bottom_resource and the quantity requested is available
	 * @exception IndexOutOfBoundsException is thrown if the quantity requested is greater than the resource available
	 * @exception IllegalArgumentException is thrown if the resource_type is not contained
	 *
	 */
	private Resource[] getFromBottom(Resource resource_type, int quantity){
		if (this.bottom_resources[0] != null && resource_type.equals(bottom_resources[0])){
			if (bottom_resources[1] != null){
				if (bottom_resources[2] != null){
					if (quantity == 3){
						Resource[] to_return = {bottom_resources[0], bottom_resources[1], bottom_resources[2]};
						this.bottom_resources[2] = null;
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					} else if (quantity == 2){
						Resource[] to_return = {bottom_resources[0], bottom_resources[1], bottom_resources[2]};
						this.bottom_resources[2] = null;
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					} else if (quantity == 1){
						Resource[] to_return = {bottom_resources[0], bottom_resources[1], bottom_resources[2]};
						this.bottom_resources[2] = null;
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					} else {
						throw new IndexOutOfBoundsException();
					}
				} else {
					if (quantity == 2){
						Resource[] to_return = {bottom_resources[0], bottom_resources[1]};
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					} else if (quantity == 1){
						Resource[] to_return = {bottom_resources[1]};
						this.bottom_resources[1] = null;
						return to_return;
					} else {
						throw new IndexOutOfBoundsException();
					}
				}
			} else {
				if (quantity == 1){
					Resource[] to_return = {bottom_resources[0]};
					this.bottom_resources[0] = null;
					return to_return;
				} else {
					throw new IndexOutOfBoundsException();
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public boolean areContainedInWarehouse(Resource[] to_check){
		int top = 0;
		int mid = 0;
		int bot = 0;

		for (int i = 0; i < to_check.length; i ++){
			if (to_check[i].equals(this.top_resource)){
				top += 1;
			} else if (to_check[i].equals(this.middle_resources[mid])){
				mid += 1;
			} else if (to_check[i].equals(this.bottom_resources[bot])){
				bot += 1;
			} else if (to_check[i] == null){
			} else {
				return false;
			}

			if (top >= 2 || mid >= 3 || bot >= 4){
				return false;
			}

			to_check[i] = null;
		}

		return true;
	}

	public void clearWarehouse(){
		this.top_resource = null;
		this.middle_resources[0] = null;
		this.middle_resources[1] = null;
		this.bottom_resources[0] = null;
		this.bottom_resources[1] = null;
		this.bottom_resources[2] = null;
	}

	public String toString(){
		String to_print;

		to_print = "1: ";
		if (top_resource != null){
			to_print = to_print + top_resource.name();
		} else {
			to_print = to_print + "null";
		}

		to_print = to_print + "\n2: ";
		if (middle_resources[0] != null){
			to_print = to_print + middle_resources[0].name() + " | ";
		} else {
			to_print = to_print + "null | ";
		}
		if (middle_resources[1] != null){
			to_print = to_print + middle_resources[1].name();
		} else {
			to_print = to_print + "null";
		}

		to_print = to_print + "\n3: ";
		if (bottom_resources[0] != null){
			to_print = to_print + bottom_resources[0].name() + " | ";
		} else {
			to_print = to_print + "null | ";
		}
		if (bottom_resources[1] != null){
			to_print = to_print + bottom_resources[1].name() + " | ";
		} else {
			to_print = to_print + "null | ";
		}
		if (bottom_resources[2] != null){
			to_print = to_print + bottom_resources[2].name();
		} else {
			to_print = to_print + "null\n";
		}

		return to_print;
	}
}
