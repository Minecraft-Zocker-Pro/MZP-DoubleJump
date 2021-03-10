package minecraft.doublejump.zocker.pro.listener;

import minecraft.core.zocker.pro.util.Cooldown;
import minecraft.doublejump.zocker.pro.Main;
import minecraft.doublejump.zocker.pro.event.PlayerDoubleJumpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PlayerDoubleJumpListener implements Listener {

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerDoubleJump(PlayerDoubleJumpEvent event) {
		Player player = event.getPlayer();

		List<Cooldown> cooldownList = Cooldown.getCooldown(player.getUniqueId());
		if (cooldownList == null) {
			Cooldown cooldown = new Cooldown(player.getUniqueId(), TimeUnit.SECONDS, 5);
			cooldown.setItemStack(Main.getItemStack());
			return;
		}

		Optional<Cooldown> cooldownOptional = Cooldown.getCooldown(player.getUniqueId())
			.stream()
			.filter(Objects::nonNull)
			.filter(cooldown1 -> cooldown1.getItemStack().isSimilar(Main.getItemStack()))
			.findFirst();

		if (cooldownOptional.isPresent()) {
			Cooldown cooldown = cooldownOptional.get();
			if (!cooldown.isElapsed()) {
				event.setCancelled(true);
				player.setAllowFlight(false);
				return;
			}

			cooldown.setEndTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
		} else {
			Cooldown cooldown = new Cooldown(player.getUniqueId(), TimeUnit.SECONDS, 5);
			cooldown.setItemStack(Main.getItemStack());
		}
	}
}