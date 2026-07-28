# Arma 2 Ammo Sound - RuneLite Plugin

A private RuneLite plugin that brings military shooter vibes to Old School RuneScape.

## Features

### 🔫 "Cannot Fire" Dry Click (Arma 2)
Plays the iconic Arma 2 dry click sound when you run out of ranged ammo.

- **Full Spam Mode** (on by default): Keeps clicking every game tick while you have no ammo and are still targeting an NPC. Maximum annoyance.
- Detects all ranged weapons: bows, crossbows, blowpipe, darts, knives, chins, ballista, etc.
- Adjustable volume (1-100)

### 💀 "Mission Failed, We'll Get 'Em Next Time" (CoD)
Plays the iconic Modern Warfare line whenever you die in-game.

- Toggleable on/off (on by default)
- Separate volume control
- Triggers once per death (won't spam)

## Installation

1. Download the latest `.jar` from the `release/` folder
2. Place it in your RuneLite plugins folder:
   - **Windows:** `%USERPROFILE%/.runelite/plugins/`
   - **macOS:** `~/.runelite/plugins/`
   - **Linux:** `~/.runelite/plugins/`
3. If the `plugins` folder doesn't exist, create it
4. Restart RuneLite (or use the "Load external plugin" option if available)
5. Enable "Arma 2 Ammo Sound" in the RuneLite plugin list
6. Configure settings in the plugin sidebar panel

## Settings

| Setting | Default | Description |
|---------|---------|-------------|
| Full Spam Mode | On | Repeat dry click every tick with no ammo |
| Ammo Volume | 75 | Volume for the cannot fire sound |
| Enable Death Sound | On | Play mission failed on death |
| Death Sound Volume | 100 | Volume for the mission failed sound |

## Supported Ranged Weapons

Shortbow, Longbow, Dark Bow, Crystal Bow, Bowfa, Magic Shortbow, Karil's Crossbow, Crossbows, Blowpipe, Darts, Ballista, Knives, Throwing Axes, Chinchompas, Zaryte Crossbow, Webweaver Bow

## ⚠️ Disclaimer

This is a **private plugin** for personal use. It does NOT automate any gameplay inputs. It only plays sound effects based on game events. No ban risk.

## Version History

- **v1.1.0** - Added "Mission Failed, We'll Get 'Em Next Time" death sound with toggle + volume control
- **v1.0.0** - Initial release with Arma 2 dry click on ammo depletion
