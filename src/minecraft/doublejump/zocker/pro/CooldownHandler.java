package minecraft.doublejump.zocker.pro;

import minecraft.core.zocker.pro.util.Cooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CooldownHandler {

	private static BukkitTask bukkitTask;
	private static boolean isRunning;

	public static void start() {
		if (isRunning) return;
		isRunning = true;

		bukkitTask = new BukkitRunnable() {
			@Override
			public void run() {
				for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					List<Cooldown> cooldownList = Cooldown.getCooldown(onlinePlayer.getUniqueId());
					if (cooldownList == null) continue;

					final Optional<Cooldown> cooldownOptional = cooldownList.stream()
						.filter(Objects::nonNull)
						.filter(cooldown -> {
							if (cooldown.getItemStack() != null) {
								return cooldown.getItemStack().isSimilar(Main.getItemStack());
							}

							return false;
						})
						.findFirst();

					if (cooldownOptional.isPresent()) {
						Cooldown cooldown = cooldownOptional.get();
						if (cooldown.isElapsed()) {
							Cooldown.getCooldown(onlinePlayer.getUniqueId()).remove(cooldown);

							new BukkitRunnable() {
								@Override
								public void run() {
									onlinePlayer.setFlying(false);
									onlinePlayer.setAllowFlight(true);
								}
							}.runTask(Main.getPlugin());
						}
					} else {
						new BukkitRunnable() {
							@Override
							public void run() {
								if (onlinePlayer.hasPermission("mzp.doublejump.use")) {
									onlinePlayer.setFlying(false);
									onlinePlayer.setAllowFlight(true);
								}
							}
						}.runTask(Main.getPlugin());
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 20, 20);
	}

	public static void stop() {
		if (!isRunning) return;

		isRunning = false;

		bukkitTask.cancel();
	}
}