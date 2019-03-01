package mr_krab.dupefixer.listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.text.serializer.TextSerializers;

import mr_krab.dupefixer.DupeFixer;
import ninja.leaping.configurate.ConfigurationNode;

public class ShiftClickListener {

	private DupeFixer plugin;

	public ShiftClickListener(DupeFixer plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void shiftClickListener(ClickInventoryEvent.Shift event) {
		if(event.getSource() instanceof Player) {
			Player player = (Player) event.getSource();
			if(event.getTransactions().isEmpty() || event.getTransactions().get(0).getSlot().totalItems() == 0) {
				return;
			}
			String itemId = "air_or_empty";
			if(event.getTransactions().get(0).getSlot().peek().isPresent()) {
				itemId = event.getTransactions().get(0).getSlot().peek().get().getType().getId();
			}
			// Debug
			if(plugin.getRootNode().getNode("Debug").getBoolean()) {
				if(!(player.getOpenInventory().get().toString().contains("ContainerChest") || player.getOpenInventory().get().toString().contains("ContainerPlayer"))) {
					String inventoryName = player.getOpenInventory().get().toString();
					inventoryName = inventoryName.split("@")[0];
					String debug = TextSerializers.formattingCode('§').serialize(TextSerializers.FORMATTING_CODE.deserialize(
							plugin.getRootNode().getNode("BlockShiftClick", "Messages", "Debug").getString()
								.replace("%player%", player.getName()).replace("%item%", itemId).replace("%сontainer%", inventoryName)));
					plugin.getLogger().info(debug);
				}
			}
			// Block
			if(event.getTransactions().get(0).getSlot().peek().isPresent()) {
				if(verifyInventory(player.getOpenInventory().get().toString())) {
					if(verifyItemID(event.getTransactions().get(0).getSlot().peek().get().getType().getId())) {
						event.setCancelled(true);
						plugin.getLogger().info(TextSerializers.formattingCode('§').serialize(
							TextSerializers.FORMATTING_CODE.deserialize(plugin.getRootNode().getNode("FixContainer", "Messages", "Console").getString()
								.replace("%player%", player.getName()).replace("%item_id%", itemId))));
						player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(
							plugin.getRootNode().getNode("BlockShiftClick", "Messages", "Player").getString()));
					}
				}
			}
		}
	}

	private boolean verifyItemID(String itemID) {
		boolean result = false;
		List<String> itemList = plugin.getRootNode().getNode("BlockShiftClick", "ItemList", "List").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		if(plugin.getRootNode().getNode("BlockShiftClick", "ItemList", "BlackList").getBoolean()) {
			if(itemList.contains(itemID)) {
				result = true;
			}
		} else {
			if(!itemList.contains(itemID)) {
				result = true;
			}
		}
		return result;
	}

	private boolean verifyInventory(String inventoryName) {
		inventoryName = inventoryName.split("@")[0];
		boolean result = false;
		List<String> inventoryList = plugin.getRootNode().getNode("BlockShiftClick", "ContainerList", "List").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		if(plugin.getRootNode().getNode("BlockShiftClick", "ContainerList", "BlackList").getBoolean()) {
			if(inventoryList.contains(inventoryName) || inventoryList.contains(inventoryName.substring(inventoryName.lastIndexOf('.')+1))) {
				result = true;
			}
		} else {
			if(!(inventoryList.contains(inventoryName) || inventoryList.contains(inventoryName.substring(inventoryName.lastIndexOf('.')+1)))) {
				result = true;
			}
		}
		return result;
	}
}
