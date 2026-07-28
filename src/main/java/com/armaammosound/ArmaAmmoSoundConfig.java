package com.armaammosound;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("armaammosound")
public interface ArmaAmmoSoundConfig extends Config
{
	@ConfigSection(
		name = "Ammo Sound",
		description = "Settings for the 'cannot fire' dry click",
		position = 0
	)
	String ammoSection = "ammoSection";

	@ConfigSection(
		name = "Death Sound",
		description = "Settings for the 'mission failed' death sound",
		position = 1
	)
	String deathSection = "deathSection";

	@ConfigSection(
		name = "Test Sounds",
		description = "Preview the sounds",
		position = 2
	)
	String testSection = "testSection";

	@ConfigItem(
		keyName = "fullSpam",
		name = "Full Spam Mode",
		description = "Plays 'CANNOT FIRE' every tick while you have no ammo and are trying to attack",
		section = ammoSection
	)
	default boolean fullSpam()
	{
		return true;
	}

	@Range(min = 1, max = 100)
	@ConfigItem(
		keyName = "volume",
		name = "Volume",
		description = "Volume of the cannot fire sound (1-100)",
		section = ammoSection
	)
	default int volume()
	{
		return 75;
	}

	@ConfigItem(
		keyName = "deathSoundEnabled",
		name = "Enable Death Sound",
		description = "Plays 'Mission Failed, We'll Get Em Next Time' when you die",
		section = deathSection
	)
	default boolean deathSoundEnabled()
	{
		return true;
	}

	@Range(min = 1, max = 100)
	@ConfigItem(
		keyName = "deathVolume",
		name = "Death Sound Volume",
		description = "Volume of the mission failed sound (1-100)",
		section = deathSection
	)
	default int deathVolume()
	{
		return 100;
	}

	@ConfigItem(
		keyName = "testAmmoSound",
		name = "▶ Test Ammo Sound",
		description = "Toggle ON to preview the 'cannot fire' dry click, it will auto-reset",
		section = testSection
	)
	default boolean testAmmoSound()
	{
		return false;
	}

	@ConfigItem(
		keyName = "testDeathSound",
		name = "▶ Test Death Sound",
		description = "Toggle ON to preview 'Mission Failed', it will auto-reset",
		section = testSection
	)
	default boolean testDeathSound()
	{
		return false;
	}
}
