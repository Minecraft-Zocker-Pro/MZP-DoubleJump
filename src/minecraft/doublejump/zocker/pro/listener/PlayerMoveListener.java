package minecraft.doublejump.zocker.pro.listener;

import me.davidml16.aparkour.api.ParkourAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled()) return;
		if (event.getPlayer().getAllowFlight()) return;

		if (Bukkit.getServer().getPluginManager().isPluginEnabled("AParkour")) {
			if ((new ParkourAPI()).isInParkour(event.getPlayer())) {
				return;
			}

			event.getPlayer().setAllowFlight(true);
		} else {
			event.getPlayer().setAllowFlight(true);
		}
	}
}
