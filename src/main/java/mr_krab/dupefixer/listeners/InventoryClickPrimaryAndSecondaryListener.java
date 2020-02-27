package mr_krab.dupefixer.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;

public class InventoryClickPrimaryAndSecondaryListener {
/*
	private DupeFixer plugin;

	public InventoryClickPrimaryAndSecondaryListener(DupeFixer plugin) {
		this.plugin = plugin;
	}
*/
	@Listener
	public void primaryClickListener(ClickInventoryEvent.Primary event) {
		if(event.getTransactions().isEmpty()) {
			return;
		}
		if(event.getTargetInventory().getArchetype().getId().equals(InventoryArchetypes.ANVIL.getId()) ) {
			int id = event.getTransactions().get(0).getSlot().getInventoryProperty(SlotIndex.class).get().getValue();
			if(id == 0) {
				Slot slot1 = event.getTargetInventory().query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(1))).first();
				Slot slot2 = event.getTargetInventory().query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(2))).first();
				if(slot1.totalItems() !=0 && event.getTargetInventory().query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(2))).first().totalItems() != 0) {
					slot2.poll();
				}
			}
		}
	}

	@Listener
	public void secondaryClickListener(ClickInventoryEvent.Secondary event) {
		if(event.getTransactions().isEmpty()) {
			return;
		}
		if(event.getTargetInventory().getArchetype().getId().equals(InventoryArchetypes.ANVIL.getId()) ) {
			int id = event.getTransactions().get(0).getSlot().getInventoryProperty(SlotIndex.class).get().getValue();
			if(id == 0) {
				Slot slot1 = event.getTargetInventory().query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(1))).first();
				Slot slot2 = event.getTargetInventory().query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(2))).first();
				if(slot1.totalItems() !=0 && slot2.totalItems() != 0) {
					slot2.poll();
				}
			}
		}
	}
}
