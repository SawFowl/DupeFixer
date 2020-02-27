package mr_krab.dupefixer.utils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;

import mr_krab.dupefixer.DupeFixer;

public class ConfigUtil {
	
	private final static DupeFixer plugin = DupeFixer.getInstance();

	public ConfigUtil() {
		this.saveConfig();
	}

	public void saveConfig() {
		Optional<Asset> assetOpt = Sponge.getAssetManager().getAsset(plugin, "config.yml");
		assetOpt.ifPresent(asset -> {
		    try {
		        asset.copyToDirectory(plugin.getConfigDir());
		        plugin.getLogger().info("Config saved");
		    } catch (IOException e) {
		    	plugin. getLogger().error("Failed to save config! ", e);
		    }
		});
	}

  	// Check the configuration version.
	public void checkConfigVersion() {
		File oldConfig = new File(plugin.getConfigDir() + File.separator + "config.yml");
		File renamedConfig = new File(plugin.getConfigDir() + File.separator + "ConfigOld.yml");
		if (!plugin.getRootNode().getNode("Config-Version").isVirtual()) {
			// This part can be supplemented.
			if (plugin.getRootNode().getNode("Config-Version").getInt() != 4) {
				plugin.getLogger().warn("Attention!!! The version of your configuration file does not match the current one!");
				if(oldConfig.exists()) {
					oldConfig.renameTo(renamedConfig);
				}
				plugin.getLogger().warn("Your config has been replaced with the default config. Old config see in the file ConfigOld.yml.");
				saveConfig();
			}
		} else {
			// Action in the absence of the configuration version.
			plugin.getLogger().warn("Attention!!! The version of your configuration file does not match the current one!");
			if(oldConfig.exists()) {
				oldConfig.renameTo(renamedConfig);
			}
			plugin.getLogger().warn("Your config has been replaced with the default config. Old config see in the file ConfigOld.yml.");
			saveConfig();
		}
	}
}
