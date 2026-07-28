package com.armaammosound;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("armaammosound")
public interface ArmaAmmoSoundConfig extends Config
{
	@ConfigItem(
		keyName = "fullSpam",
		name = "Full Spam Mode",
		description = "Plays 'CANNOT FIRE' every tick while you have no ammo and are trying to attack"
	)
	default boolean fullSpam()
	{
		return true;
	}

	@Range(min = 1, max = 100)
	@ConfigItem(
		keyName = "volume",
		name = "Volume",
		description = "Volume of the cannot fire sound (1-100)"
	)
	default int volume()
	{
		return 75;
	}
}
