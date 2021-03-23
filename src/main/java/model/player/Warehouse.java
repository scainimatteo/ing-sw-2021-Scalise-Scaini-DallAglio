package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;

public class Warehouse {
	private Resource top_resource;
	private Resource[] middle_resources;
	private Resource[] bottom_resources;

	public Warehouse () {
		Resource top_resource = null;
		Resource[] middle_resources = {null, null};
		Resource[] bottom_resources = {null, null, null};
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
				if (rearrangeWarehouseTop(new_resource)) {
					return true;
				}else {
					return false;
				}
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

	private boolean rearrangeWarehouse(Resource new_resource) {
		return false;
	}

	public void tryToInsert(Resource[] new_resources) {
		for (Resource resource : new_resources) {
			if (!isPossibleToInsert(resource)) {
				System.out.println ("Impossibile inserire risorsa");
			}
		}
	}

	public Resource[] getFromWarehouse(Resource resource_type, int quantity) {
		return null;
	}

	public String toString () {
		String to_print;
		to_print = "1: " + top_resource.name() +"\n";
		to_print = to_print + "2: " + middle_resources[0].name() + " | " + middle_resources[1].name() +"\n";
		to_print = to_print + "3: " + bottom_resources[0].name() + " | " + bottom_resources[0].name() + " | " + bottom_resources[0].name();
		return to_print;
	}
}
