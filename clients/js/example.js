import inquirer from "inquirer";
import { Client } from "./dist/index.js";

const client = new Client({
  apiRoot: "http://localhost:8080",
});

async function getAllPlayers() {
  const players = await client.players.get();

  console.log(`There are ${players.length} players online`);

  const player = await inquirer.prompt([
    {
      type: "list",
      name: "player",
      message: "Select a player",
      choices: ["Go back", ...players.map((player) => player.name)],
    },
  ]);

  if (player.player === "Go back") return menu();

  const selectedPlayer = players.find((player) => player.name === player.name);

  return playerMenu(selectedPlayer);
}

async function getPlayer() {
  const nameOrUUID = await inquirer.prompt([
    {
      type: "input",
      name: "nameOrUUID",
      message: "Enter player name or UUID",
    },
  ]);

  const player = await client.players.get(nameOrUUID.nameOrUUID);

  console.table({
    "Player Name": player.name,
    "Player UUID": player.uuid,
    "Player IP": player.ip,
  });

  playerMenu(player);
}

/**
 * @param {import("./dist/index").Player} player
 */
async function playerMenu(player) {
  const playerOption = await inquirer.prompt([
    {
      type: "list",
      name: "option",
      message: "What do you want to do?",
      choices: [
        "View Inventory",
        "Dupe item",
        "Remove item",
        "Teleport",
        "Go back",
      ],
    },
  ]);

  switch (playerOption.option) {
    case "View Inventory":
      console.table(
        player.inventory.items.map((item, index) => ({
          index,
          name: item?.name || "Empty",
          type: item?.type || "Empty",
          amount: item?.amount || 0,
        }))
      );
      break;
    case "Dupe item":
      const selectedItem = await inquirer.prompt([
        {
          type: "list",
          name: "item",
          message: "What item do you want to dupe?",
          choices: player.inventory.items
            .filter((item) => item !== null)
            .map((item) => item.type),
        },
      ]);

      const itemToDupe = player.inventory.items.find(
        (item) => item.type === selectedItem.item
      );

      await player.inventory.addItem(itemToDupe);

      console.log("Done!");
      break;
    case "Remove item":
      const itemToRemove = await inquirer.prompt([
        {
          type: "list",
          name: "item",
          message: "What item do you want to remove?",
          choices: player.inventory.items
            .filter((item) => item !== null)
            .map((item) => item.type),
        },
      ]);

      await player.inventory.removeItem(
        player.inventory.items.findIndex(
          (item) => item.type === itemToRemove.item
        )
      );

      console.log("Done!");
      break;

    case "Teleport":
      const location = await inquirer.prompt([
        {
          type: "input",
          name: "x",
          message: "X",
        },
        {
          type: "input",
          name: "y",
          message: "Y",
        },
        {
          type: "input",
          name: "z",
          message: "Z",
        },
      ]);

      await player.location.set({
        x: Number(location.x),
        y: Number(location.y),
        z: Number(location.z),
      });

      console.log("Done!");
      break;
    case "Go back":
      menu();
      return;
  }

  playerMenu(player);
}

async function menu() {
  const option = await inquirer.prompt([
    {
      type: "list",
      name: "option",
      message: "What do you want to do?",
      choices: ["Get all players", "Get player by name or UUID", "Exit"],
    },
  ]);

  switch (option.option) {
    case "Get all players":
      await getAllPlayers();
      break;
    case "Get player by name or UUID":
      await getPlayer();
      break;
    case "Exit":
      process.exit(0);
      break;
  }
}

async function setup() {
  await client.setup();

  console.table({
    "Minecraft Version": client.minecraftVersion,
    "Bukkit Version": client.bukkitVersion,
  });

  menu();
}

setup();
