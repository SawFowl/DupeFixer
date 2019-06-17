package mr_krab.dupefixer.listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.text.serializer.TextSerializers;

import mr_krab.dupefixer.DupeFixer;
import ninja.leaping.configurate.ConfigurationNode;

public class InteractItemListenerFluidFixRP {

	private DupeFixer plugin;

	public InteractItemListenerFluidFixRP(DupeFixer plugin) {
		this.plugin = plugin;
	}

	/**
	 * Руки оторвать бы разработчику RedProtect за то, как он предоставляет API своего плагина.
	 * The RedProtect developer needs to tear off his hands for how he provides his plugin API.
	 */
	@Listener
	public void fixBucketDupe(InteractItemEvent.Secondary event) {
		if(event.getSource() instanceof Player) {
			Player player = (Player) event.getSource();
			if(plugin.getProtectPluginsAPI().isPresentRPAPI()) {
				String playerName = player.getName();
				if(!(plugin.getProtectPluginsAPI().getRedProtectAPI().getRegion(player.getLocation()).isMember(playerName) && 
						plugin.getProtectPluginsAPI().getRedProtectAPI().getRegion(player.getLocation()).isLeader(playerName) &&
						plugin.getProtectPluginsAPI().getRedProtectAPI().getRegion(player.getLocation()).isAdmin(playerName))) {
					List<String> itemList = plugin.getRootNode().getNode("FixFluidDupe", "ItemList", "List").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
					if(itemList.contains(event.getItemStack().createStack().getType().getId())) {
						event.setCancelled(true);
						String position = player.getPosition().toString();
						plugin.getLogger().info(TextSerializers.formattingCode('§').serialize(TextSerializers.FORMATTING_CODE.deserialize(plugin.getRootNode().getNode("FixFluidDupe", "Messages", "Console").getString()
								.replace("%player%", playerName).replace("%coordinates%", position))));
						if(plugin.getRootNode().getNode("FixFluidDupe", "Messages", "SendToPlayer").getBoolean()) {
							player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(plugin.getRootNode().getNode("FixFluidDupe", "Messages", "Player").getString()));
						}
					}
				}
			}
		}
	}
}
