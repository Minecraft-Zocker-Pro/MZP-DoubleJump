package minecraft.doublejump.zocker.pro.hook;

import me.davidml16.aparkour.api.ParkourAPI;
import minecraft.doublejump.zocker.pro.event.PlayerDoubleJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AParkourHook implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onAParkourDoubleJump(PlayerDoubleJumpEvent event) {
		if ((new ParkourAPI()).isInParkour(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
