package minecraft.doublejump.zocker.pro.listener;

import me.davidml16.aparkour.api.ParkourAPI;
import minecraft.core.zocker.pro.compatibility.CompatibleParticleHandler;
import minecraft.core.zocker.pro.compatibility.CompatibleSound;
import minecraft.doublejump.zocker.pro.Main;
import minecraft.doublejump.zocker.pro.event.PlayerDoubleJumpEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerToggleFlightListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();

		if (Bukkit.getServer().getPluginManager().isPluginEnabled("AParkour")) {
			if ((new ParkourAPI()).isInParkour(player)) {
				player.setAllowFlight(false);
				player.setFlying(false);
				event.setCancelled(true);
				return;
			}
		}
		
		PlayerDoubleJumpEvent playerDoubleJumpEvent = new PlayerDoubleJumpEvent(player);
		Bukkit.getPluginManager().callEvent(playerDoubleJumpEvent);

		if (playerDoubleJumpEvent.isCancelled()) {
			player.setAllowFlight(false);
			player.setFlying(false);
			event.setCancelled(true);
			player.setAllowFlight(true);
			return;
		}

		player.setVelocity(player.getLocation().getDirection()
			.multiply(Main.DOUBLE_JUMP_CONFIG.getDouble("doublejump.multiply"))
			.setY(Main.DOUBLE_JUMP_CONFIG.getDouble("doublejump.height")));

		if (Main.DOUBLE_JUMP_CONFIG.getBool("doublejump.sound.enabled")) {
			CompatibleSound.ENTITY_ENDER_DRAGON_FLAP.play(player, 3.0f, 2.0f);
		}

		if (Main.DOUBLE_JUMP_CONFIG.getBool("doublejump.effect.enabled")) {
			CompatibleParticleHandler.spawnParticles(CompatibleParticleHandler.ParticleType.FLAME, player.getLocation(), 5);
		}

		player.setAllowFlight(false);
		player.setFlying(false);
		event.setCancelled(true);
	}
}
