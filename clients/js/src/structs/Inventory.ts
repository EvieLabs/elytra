import type { Client } from "../Client";
import type { InventoryPayload } from "../schemas/infer";
import { Base } from "./Base";
import { ItemStack } from "./ItemStack";

export class Inventory extends Base {
  public constructor(
    client: Client,
    private target: string,
    private data: InventoryPayload
  ) {
    super(client);
  }

  public get items(): (ItemStack | null)[] {
    return this.data.contents.map((item) =>
      item ? new ItemStack(this.client, item) : null
    );
  }

  public get armor(): (ItemStack | null)[] {
    return this.data.armorContents.map((item) =>
      item ? new ItemStack(this.client, item) : null
    );
  }

  public async addItem(item: string | ItemStack) {
    return this.client.players.constructPlayer(
      (
        await this.client.post(`/players/${this.target}/inventory`, {
          b64: typeof item === "string" ? item : item.base64,
        })
      ).data
    );
  }

  public async removeItem(slot: number) {
    return this.client.players.constructPlayer(
      (await this.client.delete(`/players/${this.target}/inventory/${slot}`))
        .data
    );
  }
}
