package com.armaammosound;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Arma 2 Ammo Sound",
	description = "Plays Arma 2 'cannot fire' when out of ammo + CoD 'Mission Failed' on death",
	tags = {"ammo", "ranged", "sound", "arma", "death", "cod"}
)
public class ArmaAmmoSoundPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ArmaAmmoSoundConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private ConfigManager configManager;

	private byte[] cannotFireData;
	private AudioFormat cannotFireFormat;
	private byte[] missionFailedData;
	private AudioFormat missionFailedFormat;

	private boolean wasRanging = false;
	private int lastAmmoCount = -1;
	private int ticksSinceRangedAttack = 0;
	private boolean wasDead = false;

	private EventBus.Subscriber animSub;
	private EventBus.Subscriber tickSub;
	private EventBus.Subscriber configSub;

	private static final int DEATH_ANIM = 836;

	@Provides
	ArmaAmmoSoundConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ArmaAmmoSoundConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		cannotFireData = null;
		cannotFireFormat = null;
		missionFailedData = null;
		missionFailedFormat = null;
		loadSound("cannot_fire.wav", true);
		loadSound("mission_failed.wav", false);

		// Use Consumer-based registration to avoid LambdaConversionException with @Subscribe
		animSub = eventBus.register(AnimationChanged.class, this::handleAnimationChanged, 0f);
		tickSub = eventBus.register(GameTick.class, this::handleGameTick, 0f);
		configSub = eventBus.register(ConfigChanged.class, this::handleConfigChanged, 0f);

		log.info("Arma 2 Ammo Sound plugin started - CANNOT FIRE + MISSION FAILED loaded");
	}

	@Override
	protected void shutDown() throws Exception
	{
		if (animSub != null)
		{
			eventBus.unregister(animSub);
			animSub = null;
		}
		if (tickSub != null)
		{
			eventBus.unregister(tickSub);
			tickSub = null;
		}
		if (configSub != null)
		{
			eventBus.unregister(configSub);
			configSub = null;
		}

		cannotFireData = null;
		cannotFireFormat = null;
		missionFailedData = null;
		missionFailedFormat = null;
		log.info("Arma 2 Ammo Sound plugin stopped");
	}

	private void handleConfigChanged(ConfigChanged event)
	{
		if (!"armaammosound".equals(event.getGroup()))
		{
			return;
		}

		if ("testAmmoSound".equals(event.getKey()) && "true".equals(event.getNewValue()))
		{
			playSound(cannotFireData, cannotFireFormat, config.volume());
			// Auto-reset the toggle back to off
			configManager.setConfiguration("armaammosound", "testAmmoSound", false);
		}
		else if ("testDeathSound".equals(event.getKey()) && "true".equals(event.getNewValue()))
		{
			playSound(missionFailedData, missionFailedFormat, config.deathVolume());
			// Auto-reset the toggle back to off
			configManager.setConfiguration("armaammosound", "testDeathSound", false);
		}
	}

	private void loadSound(String filename, boolean isCannotFire)
	{
		try
		{
			InputStream audioSrc = getClass().getResourceAsStream(filename);
			if (audioSrc == null)
			{
				log.error("Cannot find {} resource!", filename);
				return;
			}
			InputStream bufferedIn = new BufferedInputStream(audioSrc);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
			AudioFormat format = audioStream.getFormat();
			byte[] data = audioStream.readAllBytes();
			audioStream.close();

			if (isCannotFire)
			{
				cannotFireFormat = format;
				cannotFireData = data;
			}
			else
			{
				missionFailedFormat = format;
				missionFailedData = data;
			}

			log.info("Loaded {} successfully ({} bytes)", filename, data.length);
		}
		catch (Exception e)
		{
			log.error("Failed to load {}", filename, e);
		}
	}

	private void handleAnimationChanged(AnimationChanged event)
	{
		if (event.getActor() != client.getLocalPlayer())
		{
			return;
		}

		int animId = event.getActor().getAnimation();

		if (isRangedAnimation(animId))
		{
			wasRanging = true;
			ticksSinceRangedAttack = 0;
		}

		if (animId == DEATH_ANIM && config.deathSoundEnabled())
		{
			if (!wasDead)
			{
				wasDead = true;
				playSound(missionFailedData, missionFailedFormat, config.deathVolume());
			}
		}
	}

	private void handleGameTick(GameTick event)
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return;
		}

		ticksSinceRangedAttack++;

		int currentHealth = client.getBoostedSkillLevel(Skill.HITPOINTS);
		if (currentHealth > 0)
		{
			wasDead = false;
		}

		ItemContainer equipment = client.getItemContainer(InventoryID.WORN);
		if (equipment == null)
		{
			return;
		}

		int currentAmmo = getAmmoCount(equipment);

		if (wasRanging && lastAmmoCount > 0 && currentAmmo == 0)
		{
			playSound(cannotFireData, cannotFireFormat, config.volume());
		}

		if (config.fullSpam() && wasRanging && currentAmmo == 0)
		{
			Actor target = player.getInteracting();
			if (target instanceof NPC && ticksSinceRangedAttack < 10)
			{
				playSound(cannotFireData, cannotFireFormat, config.volume());
			}
		}

		if (currentAmmo > 0)
		{
			wasRanging = isCurrentlyRanging(equipment);
		}

		if (ticksSinceRangedAttack > 20)
		{
			wasRanging = false;
		}

		lastAmmoCount = currentAmmo;
	}

	private int getAmmoCount(ItemContainer equipment)
	{
		Item ammo = equipment.getItem(EquipmentInventorySlot.AMMO.getSlotIdx());
		if (ammo != null && ammo.getQuantity() > 0)
		{
			return ammo.getQuantity();
		}

		Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if (weapon != null && weapon.getQuantity() > 1)
		{
			return weapon.getQuantity();
		}

		return 0;
	}

	private boolean isCurrentlyRanging(ItemContainer equipment)
	{
		Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if (weapon == null)
		{
			return false;
		}

		Item ammo = equipment.getItem(EquipmentInventorySlot.AMMO.getSlotIdx());
		return ammo != null || weapon.getQuantity() > 1;
	}

	private boolean isRangedAnimation(int animId)
	{
		switch (animId)
		{
			case 426:   // Bows
			case 1074:  // Magic shortbow spec
			case 2075:  // Karil's crossbow
			case 4230:  // Crossbow
			case 5061:  // Blowpipe
			case 7554:  // Dart
			case 7555:  // Ballista
			case 7617:  // Knife / throwing axe
			case 7618:  // Chinchompa
			case 9168:  // Zaryte crossbow
			case 9964:  // Webweaver bow
				return true;
			default:
				return false;
		}
	}

	private void playSound(byte[] data, AudioFormat format, int volumePct)
	{
		if (data == null || format == null)
		{
			return;
		}

		new Thread(() ->
		{
			try
			{
				Clip clip = AudioSystem.getClip();
				clip.open(format, data, 0, data.length);

				if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
				{
					FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					float dB = (float) (Math.log(Math.max(volumePct, 1) / 100.0) / Math.log(10.0) * 20.0);
					volumeControl.setValue(Math.max(dB, volumeControl.getMinimum()));
				}

				clip.start();

				clip.addLineListener(e ->
				{
					if (e.getType() == LineEvent.Type.STOP)
					{
						clip.close();
					}
				});
			}
			catch (Exception e)
			{
				log.error("Failed to play sound", e);
			}
		}).start();
	}
}
