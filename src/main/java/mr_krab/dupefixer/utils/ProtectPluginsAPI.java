package mr_krab.dupefixer.utils;

import br.net.fabiozumbi12.RedProtect.Sponge.RedProtect;
import br.net.fabiozumbi12.RedProtect.Sponge.API.RedProtectAPI;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;
import mr_krab.dupefixer.DupeFixer;

public class ProtectPluginsAPI {
	
	private static final DupeFixer plugin = DupeFixer.getInstance();

	private GriefPreventionApi griefPreventionApi;
	private RedProtect redProtect;
	private RedProtectAPI redProtectAPI;
	
	private int checkRedProtectAPICount = 0;

	public void setGriefPreventionAPI(GriefPreventionApi griefPreventionApi) {
		this.griefPreventionApi = griefPreventionApi;
	}

	public GriefPreventionApi getGriefPreventionApi() {
		return griefPreventionApi;
	}

	public boolean isPresentGP() {
		if(griefPreventionApi != null) {
			return true;
		}
		return false;
	}

	public void setRedProtect(RedProtect redProtect) {
		this.redProtect = redProtect;
		try {
			setRedProtectAPI(this.redProtect.getAPI());
		} catch (Exception e) {
		}
	}

	public void setRedProtectAPI(RedProtectAPI redProtectAPI) {
		this.redProtectAPI = redProtectAPI;
	}

	public RedProtectAPI getRedProtectAPI() {
		return redProtectAPI;
	}

	public boolean isPresentRP() {
		if(redProtect != null) {
			return true;
		} 
		return false;
	}

	/**
	 * Руки оторвать бы разработчику RedProtect за то, как он предоставляет API своего плагина.
	 * The RedProtect developer needs to tear off his hands for how he provides his plugin API. Google Translate.
	 */
	public boolean isPresentRPAPI() {
		if(redProtectAPI != null) {
			return true;
		} 
		if(redProtect != null) {
			try {
				setRedProtectAPI(redProtect.getAPI());
				if(redProtectAPI != null) {
					return true;
				}
			} catch (Exception e) {
				if(checkRedProtectAPICount < 10) {
					checkRedProtectAPICount++;
					plugin.getLogger().warn("The RedProtect plugin does not correctly provide the API. Try to find out from his developer why he registers the API when the server is already fully loaded.");
				}
			}
		}
		return false;
	}
}
