package mr_krab.dupefixer.listeners;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.world.World;

import mr_krab.dupefixer.DupeFixer;

public class PortalListener {

	private DupeFixer plugin;

	public PortalListener(DupeFixer plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void onPortal(MoveEntityEvent.Teleport.Portal event) {
		Entity entity = event.getTargetEntity();
		if(plugin.getRootNode().getNode("PortalLock", "AllWorlds").getBoolean()) {
			if(plugin.getRootNode().getNode("PortalLock", "BlockTeleportation", "Items").getBoolean()) {
				if(entity instanceof Item) {
					event.setCancelled(true);
				}
			}
			if(plugin.getRootNode().getNode("PortalLock", "BlockTeleportation", "Mobs").getBoolean()) {
				if(entity instanceof Living && !(entity instanceof Player)) {
					event.setCancelled(true);
				}
			}
			if(plugin.getRootNode().getNode("PortalLock", "BlockTeleportation", "Players").getBoolean()) {
				if(entity instanceof Player) {
					event.setCancelled(true);
				}
			}
		} else {
			World world = event.getTargetEntity().getWorld();
			if(!plugin.getRootNode().getNode("PortalLock", world.getName(), "BlockTeleportation", "Items").isVirtual()) {
				if(plugin.getRootNode().getNode("PortalLock", world.getName(), "BlockTeleportation", "Items").getBoolean()) {
					if(entity instanceof Item) {
						event.setCancelled(true);
					}
				}
			}
			if(!plugin.getRootNode().getNode("PortalLock", world.getName(), "BlockTeleportation", "Mobs").isVirtual()) {
				if(plugin.getRootNode().getNode("PortalLock", world.getName(), "BlockTeleportation", "Mobs").getBoolean()) {
					if(entity instanceof Living && !(entity instanceof Player)) {
						event.setCancelled(true);
					}
				}
			}
			if(!plugin.getRootNode().getNode("PortalLock", world.getName(), "BlockTeleportation", "Players").isVirtual()) {
				if(plugin.getRootNode().getNode("PortalLock", world.getName(), "BlockTeleportation", "Players").getBoolean()) {
					if(entity instanceof Player) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
