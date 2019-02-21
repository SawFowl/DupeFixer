package mr_krab.dupefixer.listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.serializer.TextSerializers;

import mr_krab.dupefixer.DupeFixer;
import ninja.leaping.configurate.ConfigurationNode;

public class InventoryListener {

	private DupeFixer plugin;

	public InventoryListener(DupeFixer plugin) {
		this.plugin = plugin;
	}
	
	@Listener
	public void dropListener(DropItemEvent.Pre event, @First Player player) {
		event.getDroppedItems().forEach(itemStackSnapshot -> {
			String dropedItemID = itemStackSnapshot.getType().getId();
			// Debug
			if(plugin.getRootNode().getNode("Debug").getBoolean()) {
				if(!(player.getOpenInventory().get().toString().contains("ContainerChest") || player.getOpenInventory().get().toString().contains("ContainerPlayer"))) {
					String inventoryName = player.getOpenInventory().get().toString();
					inventoryName = inventoryName.split("@")[0];
					String debug = TextSerializers.formattingCode('§').serialize(TextSerializers.FORMATTING_CODE.deserialize(
							plugin.getRootNode().getNode("FixContainer", "Messages", "Debug").getString()
								.replace("%player%", player.getName()).replace("%item%", dropedItemID).replace("%сontainer%", inventoryName)));
					plugin.getLogger().info(debug);
				}
			}
			// Fix
			Task.builder().execute(() -> {
				if(verifyItemID(dropedItemID) && verifyInventory(player.getOpenInventory().get().toString())) {
					player.closeInventory();
					plugin.getLogger().info(TextSerializers.formattingCode('§').serialize(TextSerializers.FORMATTING_CODE.deserialize(plugin.getRootNode().getNode("FixContainer", "Messages", "Console").getString()
							.replace("%player%", player.getName()).replace("%item_id%", dropedItemID))));
					if(plugin.getRootNode().getNode("FixContainer", "Messages", "SendToPlayer").getBoolean()) {
						player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(plugin.getRootNode().getNode("FixContainer", "Messages", "Player").getString()));
					}
				}
			}).delayTicks(1).submit(plugin);
		});
	}

	private boolean verifyItemID(String itemID) {
		boolean result = false;
		List<String> itemList = plugin.getRootNode().getNode("FixContainer", "ItemList", "List").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		if(plugin.getRootNode().getNode("FixContainer", "ItemList", "BlackList").getBoolean()) {
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
		List<String> inventoryList = plugin.getRootNode().getNode("FixContainer", "ContainerList", "List").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		if(plugin.getRootNode().getNode("FixContainer", "ContainerList", "BlackList").getBoolean()) {
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
