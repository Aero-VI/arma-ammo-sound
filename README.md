# Arma 2 Ammo Sound - RuneLite Plugin

Plays the iconic Arma 2 "cannot fire" dry click sound every time you run out of ranged ammo in OSRS.

## Features

- **Detects when you run out of ranged ammo** (arrows, bolts, darts, knives, chinchompas, etc.)
- **Full Spam Mode** (enabled by default): Keeps playing "CANNOT FIRE" every game tick while you're still trying to attack with no ammo
- **Configurable volume** (1-100)
- Supports all ranged weapons: bows, crossbows, blowpipe, ballista, thrown weapons, etc.

## Installation (Side-loading)

Since this is a private plugin (not on the RuneLite Plugin Hub), you need to side-load it:

### Option 1: Developer Mode (Recommended)

1. Clone this repo
2. Build with Gradle: `./gradlew build`
3. The compiled .jar will be in `build/libs/`
4. Run RuneLite with the `--developer-mode` flag
5. Copy the .jar to `~/.runelite/plugins/` (create the folder if it doesn't exist)
6. Restart RuneLite

### Option 2: Build and run from source

1. Clone this repo
2. Open in IntelliJ IDEA
3. Set up RuneLite as a dependency (see RuneLite wiki)
4. Run directly from IDE

## Configuration

In RuneLite settings, find "Arma 2 Ammo Sound":

| Setting | Default | Description |
|---------|---------|-------------|
| Full Spam Mode | ON | Plays sound every tick while attacking with no ammo |
| Volume | 75 | Sound volume (1-100) |

## Supported Weapons

All standard ranged attack animations are detected:
- Shortbow, Longbow, Dark Bow, Crystal Bow, Bow of Faerdhinen
- Crossbow, Zaryte Crossbow, Karil's Crossbow
- Blowpipe
- Darts, Knives, Throwing Axes
- Chinchompas
- Ballista
- Webweaver Bow

## Ban Risk

**Effectively zero.** This plugin:
- Does NOT automate any inputs
- Does NOT interact with the game server
- Does NOT provide any gameplay advantage
- Only plays a local sound effect on your computer
- Is functionally identical to RuneLite's built-in idle notification sounds

## Sound

The included sound is a standard dry fire / trigger click sound effect (CC0 public domain).

## Credits

Built by Azula for Sam (Pixel). Because running out of ammo at Jad should sound like running out of ammo in Chernarus.
