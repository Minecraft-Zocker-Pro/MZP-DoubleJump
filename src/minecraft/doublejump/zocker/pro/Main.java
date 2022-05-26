package minecraft.doublejump.zocker.pro;

import minecraft.core.zocker.pro.CorePlugin;
import minecraft.core.zocker.pro.compatibility.CompatibleMaterial;
import minecraft.core.zocker.pro.config.Config;
import minecraft.core.zocker.pro.inventory.util.ItemBuilder;
import minecraft.doublejump.zocker.pro.hook.AParkourHook;
import minecraft.doublejump.zocker.pro.listener.PlayerDoubleJumpListener;
import minecraft.doublejump.zocker.pro.listener.PlayerJoinListener;
import minecraft.doublejump.zocker.pro.listener.PlayerMoveListener;
import minecraft.doublejump.zocker.pro.listener.PlayerToggleFlightListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class Main extends CorePlugin {

	private static CorePlugin PLUGIN;
	private static ItemStack itemStack = null;
	public static Config DOUBLE_JUMP_CONFIG;

	@Override
	public void onEnable() {
		super.onEnable();

		super.setDisplayItem(CompatibleMaterial.LEATHER_BOOTS.getMaterial());
		super.setPluginName("MZP-DoubleJump");

		PLUGIN = this;

		itemStack = new ItemBuilder(CompatibleMaterial.LEATHER_BOOTS.getMaterial())
			.setDisplayName("§6§lDoubleJump")
			.toItemStack();

		this.buildConfig();
		this.registerListener();

		if (DOUBLE_JUMP_CONFIG.getBool("doublejump.cooldown.enabled")) {
			CooldownHandler.start();
		}
	}

	@Override
	public void registerListener() {
		PluginManager pluginManager = getServer().getPluginManager();

		pluginManager.registerEvents(new PlayerJoinListener(), this);

		pluginManager.registerEvents(new PlayerToggleFlightListener(), this);
		pluginManager.registerEvents(new PlayerMoveListener(), this);
		pluginManager.registerEvents(new PlayerDoubleJumpListener(), this);

		if (getServer().getPluginManager().isPluginEnabled("AParkour")) {
			pluginManager.registerEvents(new AParkourHook(), this);
		}
	}

	@Override
	public void buildConfig() {
		DOUBLE_JUMP_CONFIG = new Config("doublejump.yml", this.getPluginName());

		DOUBLE_JUMP_CONFIG.set("doublejump.cooldown.enabled", true, "0.0.1");
		DOUBLE_JUMP_CONFIG.set("doublejump.cooldown.duration", 5, "0.0.1");

		DOUBLE_JUMP_CONFIG.set("doublejump.height", 1, "0.0.1");
		DOUBLE_JUMP_CONFIG.set("doublejump.multiply", 1.6, "0.0.1");

		DOUBLE_JUMP_CONFIG.set("doublejump.sound.enabled", true, "0.0.1");
		DOUBLE_JUMP_CONFIG.set("doublejump.effect.enabled", true, "0.0.1");

		DOUBLE_JUMP_CONFIG.setVersion("0.0.1", true);
	}

	@Override
	public void reload() {
		DOUBLE_JUMP_CONFIG.reload();
		
		CooldownHandler.stop();
		CooldownHandler.start();
	}

	@Override
	public void onDisable() {
		CooldownHandler.stop();
	}

	public static CorePlugin getPlugin() {
		return PLUGIN;
	}
	
	public static ItemStack getItemStack() {
		return itemStack;
	}
}
