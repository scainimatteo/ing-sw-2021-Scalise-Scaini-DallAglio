package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;

import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;

public class Warehouse {
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
	 * @param new_resource is the resource to be included in the Warehouse
	 * @return true if new_resource has been added
	 * @exception IllegalArgumentException is thrown if new_resource cannot be inserted
	 */
	private boolean isPossibleToInsert(Resource new_resource){
		if(!isPossibileToInsertTop(new_resource)){
			if(!isPossibileToInsertMiddle(new_resource)){
				if(!isPossibileToInsertBottom(new_resource)){
					throw new IllegalArgumentException();
				}
			}
		}
		return true;
	}

	/**
	 * @param new_resource is the resource to be included in the Warehouse
	 * @return values are specified in the method
	 */
	private boolean isPossibileToInsertTop(Resource new_resource){
		// if middle_resources have at least one space occupied and new_resource is the same resource, return false
		if(this.middle_resources[0] != null && new_resource.equals(this.middle_resources[0])){
			return false;
		}

		// if bottom_resources have at least one space occupied and new_resource is the same resource, return false
		if(this.bottom_resources[0] != null && new_resource.equals(this.bottom_resources[0])){
			return false;
		}

		// if top_resource is free, insert and return true
		if(this.top_resource == null){
			this.top_resource = new_resource;
			return true;
		// if top_resource is full, insert and return true
		}else{
			if(new_resource.equals(this.top_resource)){
				return rearrangeWarehouseTop(new_resource);
			}else{
				return false;
			}
		}
	}

	private boolean rearrangeWarehouseTop(Resource new_resource){
		if(middle_resources[0] == null){
			middle_resources[0] = new_resource;
			middle_resources[1] = this.top_resource;
			top_resource = null;
			return true;
		}else if(bottom_resources[0] == null){
			bottom_resources[0] = new_resource;
			bottom_resources[1] = this.top_resource;
			top_resource = null;
			return true;
		}else if(middle_resources[1] == null){
			middle_resources[1] = top_resource;
			top_resource = middle_resources[0];
			middle_resources[0] = new_resource;
			return true;
		}else if(bottom_resources[1] == null){
			bottom_resources[1] = top_resource;
			top_resource = bottom_resources[0];
			bottom_resources[0] = new_resource;
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @param new_resource is the resource to be included in the Warehouse
	 * @return values are specified in the method
	 */
	private boolean isPossibileToInsertMiddle(Resource new_resource){
		// if new_resource is the same resource stored in top_resource, return false
		if(new_resource.equals(this.top_resource)){
			return false;
		}

		// if middle_resources has at least one space occupied and new resource isn't the same resource, return false
		if(this.middle_resources[0] != null && !(new_resource.equals(this.middle_resources[0]))){
			return false;
		}

		// if bottom_resources have at least one space occupied and new_resource is the same resource, return false
		if(this.bottom_resources[0] != null && new_resource.equals(this.bottom_resources[0])){
			return false;
		}

		if(this.middle_resources[1] == null){
			// if middle_resources is empty, insert and return true
			if(this.middle_resources[0] == null){
				this.middle_resources[0] = new_resource;
				return true;
			// if 1st space is occupied, insert in 2nd position and return true
			}else{
				this.middle_resources[1] = new_resource;
				return true;
			}
		// if middle_resources is full, return false
		}else{
			if(rearrangeWarehouseMiddle(new_resource)){
				return true;
			}else{
				return false;
			}
		}
	}

	private boolean rearrangeWarehouseMiddle(Resource new_resource){
		if(this.bottom_resources[0] == null){
			this.bottom_resources[0] = new_resource;
			this.bottom_resources[1] = middle_resources[0];
			this.bottom_resources[2] = middle_resources[1];
			this.middle_resources[0] = null;
			this.middle_resources[1] = null;
			return true;
		}else if(this.bottom_resources[1] == null){
			this.bottom_resources[1] = middle_resources[0];
			this.bottom_resources[2] = middle_resources[1];
			this.middle_resources[0] = this.bottom_resources[0];
			this.bottom_resources[0] = new_resource;
			this.middle_resources[1] = null;
			return true;
		}else if(this.bottom_resources[2] == null){
			this.bottom_resources[2] = middle_resources[1];
			this.middle_resources[0] = this.bottom_resources[0];
			this.middle_resources[1] = this.bottom_resources[1];
			this.bottom_resources[0] = new_resource;
			this.bottom_resources[1] = new_resource;
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @param new_resource is the resource to be included in the Warehouse
	 * @return values are specified in the method
	 */
	private boolean isPossibileToInsertBottom(Resource new_resource){
		// if new_resource is the same resource stored in top_resource, return false
		if(new_resource.equals(this.top_resource)){
			return false;
		}

		// if middle_resources have at least one space occupied and new_resource is the same resource, return false
		if(this.middle_resources[0] != null && new_resource.equals(this.middle_resources[0])){
			return false;
		}

		// if bottom_resources has at least one space occupied and new resource isn't the same resource, return false
		if(this.bottom_resources[0] != null && !(new_resource.equals(this.bottom_resources[0]))){
			return false;
		}

		if(this.bottom_resources[2] == null){
			if(this.bottom_resources[1] == null){
				// if bottom_resources is empty, insert and return true
				if(this.bottom_resources[0] == null){
					this.bottom_resources[0] = new_resource;
					return true;
				// if the 1st space is occupied, insert in the 2nd position and return true
				}else{
					this.bottom_resources[1] = new_resource;
					return true;
				}
			// if the 2nd space is occupied, insert in the 3rd position and return true
			}else{
				this.bottom_resources[2] = new_resource;
				return true;
			}
		// if bottom_resources is full, return false
		}else{
			return false;
		}
	}

	/**
	 * @param new_resources is an array of resources obtained from the market that need to be inserted in the warehouse
	 * @return the number of resources that cannot be inserted
	 */
	public int tryToInsert(Resource[] new_resources){
		int resources_not_inserted = 0;

		for(Resource resource : new_resources){
			try{
				isPossibleToInsert(resource);
			}catch(IllegalArgumentException e){
				resources_not_inserted += 1;
			}
		}

		return resources_not_inserted;
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
		if(this.top_resource != null && resource_type.equals(top_resource)){
			if(quantity == 1){
				Resource[] to_return = {top_resource};
				this.top_resource = null;
				return to_return;
			}else{
				throw new IndexOutOfBoundsException();
			}
		}else{
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
		if(this.middle_resources[0] != null && resource_type.equals(middle_resources[0])){
			if(this.middle_resources[1] != null){
				if(quantity == 2){
					Resource[] to_return = {middle_resources[0], middle_resources[1]};
					this.middle_resources[1] = null;
					this.middle_resources[0] = null;
					return to_return;
				}else if(quantity == 1){
					Resource[] to_return = {middle_resources[1]};
					this.middle_resources[1] = null;
					return to_return;
				}else{
					throw new IndexOutOfBoundsException();
				}
			}else{
				if(quantity == 1){
					Resource[] to_return = {middle_resources[0]};
					this.middle_resources[0] = null;
					return to_return;
				}else{
					throw new IndexOutOfBoundsException();
				}
			}
		}else{
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
		if(this.bottom_resources[0] != null && resource_type.equals(bottom_resources[0])){
			if(bottom_resources[1] != null){
				if(bottom_resources[2] != null){
					if(quantity == 3){
						Resource[] to_return = {bottom_resources[0], bottom_resources[1], bottom_resources[2]};
						this.bottom_resources[2] = null;
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					}else if(quantity == 2){
						Resource[] to_return = {bottom_resources[0], bottom_resources[1], bottom_resources[2]};
						this.bottom_resources[2] = null;
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					}else if(quantity == 1){
						Resource[] to_return = {bottom_resources[0], bottom_resources[1], bottom_resources[2]};
						this.bottom_resources[2] = null;
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					}else{
						throw new IndexOutOfBoundsException();
					}
				}else{
					if(quantity == 2){
						Resource[] to_return = {bottom_resources[0], bottom_resources[1]};
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					}else if(quantity == 1){
						Resource[] to_return = {bottom_resources[1]};
						this.bottom_resources[1] = null;
						return to_return;
					}else{
						throw new IndexOutOfBoundsException();
					}
				}
			}else{
				if(quantity == 1){
					Resource[] to_return = {bottom_resources[0]};
					this.bottom_resources[0] = null;
					return to_return;
				}else{
					throw new IndexOutOfBoundsException();
				}
			}
		}else{
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
			} else if(to_check[i].equals(this.bottom_resources[bot])){
				bot += 1;
			} else if (to_check[i] == null){
			} else {
				return false;
			}

			if(top >= 2 || mid >= 3 || bot >= 4){
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
		if(top_resource != null){
			to_print = to_print + top_resource.name();
		}else{
			to_print = to_print + "null";
		}

		to_print = to_print + "\n2: ";
		if(middle_resources[0] != null){
			to_print = to_print + middle_resources[0].name() + " | ";
		}else{
			to_print = to_print + "null | ";
		}
		if(middle_resources[1] != null){
			to_print = to_print + middle_resources[1].name();
		}else{
			to_print = to_print + "null";
		}

		to_print = to_print + "\n3: ";
		if(bottom_resources[0] != null){
			to_print = to_print + bottom_resources[0].name() + " | ";
		}else{
			to_print = to_print + "null | ";
		}
		if(bottom_resources[1] != null){
			to_print = to_print + bottom_resources[1].name() + " | ";
		}else{
			to_print = to_print + "null | ";
		}
		if(bottom_resources[2] != null){
			to_print = to_print + bottom_resources[2].name();
		}else{
			to_print = to_print + "null\n";
		}

		return to_print;
	}
}
