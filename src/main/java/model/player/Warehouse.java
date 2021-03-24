package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;

import java.io.*;

public class Warehouse {
	private Resource top_resource = null;
	private Resource[] middle_resources = {null, null};
	private Resource[] bottom_resources = {null, null, null};

	public Warehouse () {
		// Resource null_resource = null;
		// Resource top_resource = null;
		// Resource[] middle_resources = {null_resource, null_resource};
		// Resource[] bottom_resources = {null_resource, null_resource, null_resource};
	}

	public Resource getTopResource() {
		 return top_resource;
	}

	public Resource[] getMiddleResources() {
		return middle_resources;
	}

	public Resource[] getBottomResources() {
		return bottom_resources;
	}

	/**
	 * @param new_resource is the resource to be included in the Warehouse
	 * @return values are specified in the method
	 */
	private boolean isPossibleToInsert(Resource new_resource) {
		if (!isPossibileToInsertTop(new_resource)) {
			if (!isPossibileToInsertMiddle(new_resource)) {
				if (!isPossibileToInsertBottom(new_resource)) {
					// if is not possible to insert in any space, return false
					return false;
				}
			}
		}
		// if new_resource has been added, return true;
		return true;
	}

	/**
	 * @param new_resource is the resource to be included in the Warehouse
	 * @return values are specified in the method
	 */
	private boolean isPossibileToInsertTop(Resource new_resource) {
		// if middle_resources have at least one space occupied and new_resource is the same resource, return false
		if (this.middle_resources[0] != null && new_resource.equals(this.middle_resources[0])) {
			return false;
		}

		// if bottom_resources have at least one space occupied and new_resource is the same resource, return false
		if (this.bottom_resources[0] != null && new_resource.equals(this.bottom_resources[0])) {
			return false;
		}

		// if top_resource is free, insert and return true
		if (this.top_resource == null) {
			this.top_resource = new_resource;
			return true;
		// if top_resource is full, insert and return true
		}else {
			if (new_resource.equals(this.top_resource)) {
				return rearrangeWarehouseTop(new_resource);
			}else {
				return false;
			}
		}
	}

	private boolean rearrangeWarehouseTop(Resource new_resource) {
		if (middle_resources[0] == null) {
			middle_resources[0] = new_resource;
			middle_resources[1] = this.top_resource;
			top_resource = null;
			return true;
		}else if (bottom_resources[0] == null) {
			bottom_resources[0] = new_resource;
			bottom_resources[0] = this.top_resource;
			top_resource = null;
			return true;
		}else if (middle_resources[1] == null) {
			middle_resources[1] = top_resource;
			top_resource = middle_resources[0];
			middle_resources[0] = new_resource;
			return true;
		}else if (bottom_resources[1] == null) {
			bottom_resources[1] = top_resource;
			top_resource = bottom_resources[0];
			bottom_resources[0] = new_resource;
			return true;
		}else {
			return false;
		}
	}

	/**
	 * @param new_resource is the resource to be included in the Warehouse
	 * @return values are specified in the method
	 */
	private boolean isPossibileToInsertMiddle(Resource new_resource) {
		// if new_resource is the same resource stored in top_resource, return false
		if (new_resource.equals(this.top_resource)) {
			return false;
		}

		// if middle_resources has at least one space occupied and new resource isn't the same resource, return false
		if (this.middle_resources[0] != null && !(new_resource.equals(this.middle_resources[0]))) {
			return false;
		}

		// if bottom_resources have at least one space occupied and new_resource is the same resource, return false
		if (this.bottom_resources[0] != null && new_resource.equals(this.bottom_resources[0])) {
			return false;
		}

		if (this.middle_resources[1] == null) {
			// if middle_resources is empty, insert and return true
			if (this.middle_resources[0] == null) {
				this.middle_resources[0] = new_resource;
				return true;
			// if 1st space is occupied, insert in 2nd position and return true
			}else {
				this.middle_resources[1] = new_resource;
				return true;
			}
		// if middle_resources is full, return false
		}else {
			if (rearrangeWarehouseMiddle(new_resource)) {
				return true;
			}else {
				return false;
			}
		}
	}

	private boolean rearrangeWarehouseMiddle (Resource new_resource) {
		if (this.bottom_resources[0] == null) {
			this.bottom_resources[0] = new_resource;
			this.bottom_resources[1] = middle_resources[0];
			this.bottom_resources[2] = middle_resources[1];
			this.middle_resources[0] = null;
			this.middle_resources[1] = null;
			return true;
		}else if (this.bottom_resources[1] == null) {
			this.bottom_resources[1] = middle_resources[0];
			this.bottom_resources[2] = middle_resources[1];
			this.middle_resources[0] = this.bottom_resources[0];
			this.bottom_resources[0] = new_resource;
			this.middle_resources[1] = null;
			return true;
		}else if (this.bottom_resources[2] == null) {
			this.bottom_resources[1] = middle_resources[0];
			this.bottom_resources[2] = middle_resources[1];
			this.middle_resources[0] = this.bottom_resources[0];
			this.middle_resources[1] = this.bottom_resources[1];
			this.bottom_resources[0] = new_resource;
			return true;
		}else {
			return false;
		}
	}

	/**
	 * @param new_resource is the resource to be included in the Warehouse
	 * @return values are specified in the method
	 */
	private boolean isPossibileToInsertBottom(Resource new_resource) {
		// if new_resource is the same resource stored in top_resource, return false
		if (new_resource.equals(this.top_resource)) {
			return false;
		}

		// if middle_resources have at least one space occupied and new_resource is the same resource, return false
		if (this.middle_resources[0] != null && new_resource.equals(this.middle_resources[0])) {
			return false;
		}

		// if bottom_resources has at least one space occupied and new resource isn't the same resource, return false
		if (this.bottom_resources[0] != null && !(new_resource.equals(this.bottom_resources[0]))) {
			return false;
		}

		if (this.bottom_resources[2] == null) {
			if (this.bottom_resources[1] == null) {
				// if bottom_resources is empty, insert and return true
				if (this.bottom_resources[0] == null) {
					this.bottom_resources[0] = new_resource;
					return true;
				// if the 1st space is occupied, insert in the 2nd position and return true
				}else {
					this.bottom_resources[1] = new_resource;
					return true;
				}
			// if the 2nd space is occupied, insert in the 3rd position and return true
			}else {
				this.bottom_resources[2] = new_resource;
				return true;
			}
		// if bottom_resources is full, return false
		}else {
			return false;
		}
	}

	/**
	 * @param new_resources is an array of resources obtained from the market that need to be inserted in the warehouse
	 * @return the number of resources that cannot be inserted
	 */
	public int tryToInsert(Resource[] new_resources) {
		int resources_not_inserted = 0;

		for (Resource resource : new_resources) {
			if (!isPossibleToInsert(resource)) {
				resources_not_inserted += 1;
			}
		}

		return resources_not_inserted;
	}

	/**
	 * TODO: complete this method
	 * gFW chiama getFromTop che checka il tipo e il numero richiesto
	 */
	public Resource[] getFromWarehouse(Resource resource_type, int quantity) {
		Resource[] to_return = getFromTop(resource_type, quantity);

		// int[] availability = checkResourceAvailability(resource_type);

		// if (quantity <= availability[0]) {
		// 	for (int i = 0; i < quantity; i++) {
		// 		to_return[i] = resource_type;
		// 	}

		// 	clearWarehouse(availability[1], quantity);
		// }

		return to_return;
	}

	private Resource[] getFromTop(Resource resource_type, int quantity) {
		if (this.top_resource != null && resource_type.equals(top_resource)) {
			if (quantity == 1) {
				Resource[] to_return = {top_resource};
				this.top_resource = null;
				return to_return;
			}else {
				//TODO: lancia eccezione
				return null;
			}
		}else {
			return getFromMiddle(resource_type, quantity);
		}
	}

	private Resource[] getFromMiddle(Resource resource_type, int quantity) {
		if (this.middle_resources[0] != null && resource_type.equals(middle_resources[0])) {
			if (this.middle_resources[1] != null) {
				if (quantity == 2) {
					Resource[] to_return = {middle_resource[0], middle_resource[1]};
					this.middle_resource[1] = null;
					this.middle_resource[0] = null;
					return to_return;
				}else if (quantity == 1) {
					Resource[] to_return = {middle_resource[1]};
					this.middle_resource[1] = null;
					return to_return;
				}else {
					//TODO: lancia eccezione
					return null;
				}
			}else {
				if (quantity == 1) {
					Resource[] to_return = {middle_resource[0]};
					this.middle_resource[0] = null;
					return to_return;
				}else {
					//TODO: lancia eccezione
					return null;
				}
			}
		}else {
			return getFromBottom(resource_type, quantity);
		}
	}

	private Resource[] getFromBottom(Resource resource_type, int quantity) {
		if (this.bottom_resources[0] != null && resource_type.equals(bottom_resources[0])) {
			if (bottom_resources[1] != null) {
				if (bottom_resources[2] != null) {
					if (quantity == 3) {
						Resource[] to_return = {bottom_resources[0], bottom_resources[1], bottom_resources[2]};
						this.bottom_resources[2] = null;
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					}else if (quantity == 2) {
						Resource[] to_return = {bottom_resources[0], bottom_resources[1], bottom_resources[2]};
						this.bottom_resources[2] = null;
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					}else if (quantity == 1) {
						Resource[] to_return = {bottom_resources[0], bottom_resources[1], bottom_resources[2]};
						this.bottom_resources[2] = null;
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					}else {
						//TODO: lancia eccezione
						return null;
					}
				}else {
					if (quantity == 2) {
						Resource[] to_return = {bottom_resources[0], bottom_resources[1]};
						this.bottom_resources[1] = null;
						this.bottom_resources[0] = null;
						return to_return;
					}else if (quantity == 1) {
						Resource[] to_return = {bottom_resources[1]};
						this.bottom_resources[1] = null;
						return to_return;
					}else {
						//TODO: lancia eccezione
						return null;
					}
				}
			}else {
				if (quantity == 1) {
					Resource[] to_return = {bottom_resources[0]};
					this.bottom_resources[0] = null;
					return to_return;
				}else {
					//TODO: lancia eccezione
					return null;
				}
			}
		}else {
			//TODO: lancia eccezione
			return null;
		}
	}

	private int[] checkResourceAvailability(Resource resource_type) {
		int[] availability = new int[2];

		if (this.top_resource != null && resource_type.equals(this.top_resource)) {
			availability[0] = 1;
			availability[1] = 1;
		}else if (this.middle_resources[0] != null && resource_type.equals(this.middle_resources[0])) {
			availability[1] = 2;
			if (this.middle_resources[1] != null) {
				availability[0] = 1;
			}else {
				availability[0] = 2;
			}
		}else if (this.bottom_resources[0] != null && resource_type.equals(this.bottom_resources[0])) {
			availability[1] = 3;
			if (this.bottom_resources[1] != null) {
				if (this.bottom_resources[2] != null) {
					availability[0] = 3;
				}else {
					availability[0] = 2;
				}
			}else {
				availability[0] = 1;
			}
		}else {
			availability[0] = 0;
			availability[1] = 0;
		}

		return availability;
	}

	private void clearWarehouse(int warehouse_level, int quantity) {
		switch (warehouse_level) {
			case 1: this.top_resource = null;
					break;
			case 2: for (int i = 1; i >= (2 - quantity); i --) {
						this.middle_resources[i] = null;
					}
					break;
			case 3: for (int i = 2; i >= (3 - quantity); i --) {
						this.bottom_resources[i] = null;
					}
					break;
			default:
					break;
		}
	}

	public String toString () {
		String to_print;

		to_print = "1: ";
		if (top_resource != null) {
			to_print = to_print + top_resource.name();
		}else {
			to_print = to_print + "null";
		}

		to_print = to_print + "\n2: ";
		if (middle_resources[0] != null) {
			to_print = to_print + middle_resources[0].name() + " | ";
		}else {
			to_print = to_print + "null | ";
		}
		if (middle_resources[1] != null) {
			to_print = to_print + middle_resources[1].name();
		}else {
			to_print = to_print + "null";
		}

		to_print = to_print + "\n3: ";
		if (bottom_resources[0] != null) {
			to_print = to_print + bottom_resources[0].name() + " | ";
		}else {
			to_print = to_print + "null | ";
		}
		if (bottom_resources[1] != null) {
			to_print = to_print + bottom_resources[1].name() + " | ";
		}else {
			to_print = to_print + "null | ";
		}
		if (bottom_resources[2] != null) {
			to_print = to_print + bottom_resources[2].name();
		}else {
			to_print = to_print + "null\n";
		}

		return to_print;
	}
}
