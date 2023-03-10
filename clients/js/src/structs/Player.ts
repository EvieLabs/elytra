import type { Client } from "../Client";
import type { PlayerPayload } from "../schemas/infer";
import { Base } from "./Base";
import { Inventory } from "./Inventory";
import { Location } from "./Location";
import { PermissionNode } from "./permissions";
import { PlayerChatManager } from "./PlayerChatManager";

export class Player extends Base {
  public constructor(client: Client, private data: PlayerPayload) {
    super(client);
    this.location = new Location(client, this.data.uuid, this.data.location);
    this.inventory = new Inventory(client, this.data.uuid, this.data.inventory);
    this.chat = new PlayerChatManager(client, this);
  }

  public get name(): string {
    return this.data.name;
  }

  public get uuid(): string {
    return this.data.uuid;
  }

  public get op(): boolean {
    return this.data.op;
  }

  public get banned(): boolean {
    return this.data.banned;
  }

  public get whitelisted(): boolean {
    return this.data.whitelisted;
  }

  public get ip(): string | null {
    return this.data.ip;
  }

  public location: Location;

  public inventory: Inventory;

  public chat: PlayerChatManager;

  public get permissions() {
    return this.data.permissions.map((p) => new PermissionNode(this.client, p));
  }
}
