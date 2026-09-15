# CmdLite

**Turn huge Minecraft commands into short, memorable shortcuts — works on any server.**

CmdLite lets you drop a `.txt` file containing a long command (or several commands) into a folder, and instantly get a short client command like `/lite:teleport` that runs it. No server-side mod needed — CmdLite just sends the original full command to the server exactly like you typed it yourself.

---

## ✨ Features

- 📁 **Drop-in commands** — one `.txt` file = one command
- 🔡 **Any length** — no character limit, multi-line files run line by line
- 🌍 **Works everywhere** — vanilla servers, modded servers, singleplayer
- 🔄 **Live reload** — `/lite:reload` rescans the folder, no restart needed
- 📋 **Command list** — `/lite:list` shows everything currently loaded

---

## 🚀 How it works

1. Go to `.minecraft/config/cmdlite/commands/`
2. Create a file, e.g. `teleport.txt`
3. Put your command inside (with or without the leading `/`):
   ```
   execute as @a at @s run summon minecraft:zombie ~ ~1 ~ {NoAI:1b,CustomName:'"BigZombie"',Tags:["boss"]}
   ```
4. In-game, run:
   ```
   /lite:teleport
   ```
5. Done — CmdLite sends the full command to the server for you.

**Multiple commands in one file?** Put each on its own line — they run in order.

---

## 📦 Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.20.1
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Drop `cmdlite-1.0.0.jar` into your `mods` folder
4. Launch the game

---

## 🔗 Links

- 🛒 Mod store: [rccraft.xo.je](https://rccraft.xo.je)
- 📺 YouTube: [@RCRAJAGAMER2.0](https://youtube.com/@RCRAJAGAMER2.0)
- 🐛 Report a bug / suggest a feature: use the **Issues** tab above

---

## License

MIT — free to use, modify, and share.

