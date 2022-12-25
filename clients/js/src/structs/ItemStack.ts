import type { Client } from "../Client";
import type { ItemStackPayload } from "../schemas/infer";
import { Base } from "./Base";

export class ItemStack extends Base {
  public constructor(client: Client, private data: ItemStackPayload) {
    super(client);
  }

  public get name() {
    return (
      this.data.name ??
      this.client.lang.translateItemStackType(this.type, this.id)
    );
  }

  public get id() {
    return this.data.id;
  }

  public get type() {
    return this.data.type;
  }

  public get amount() {
    return this.data.amount;
  }

  public get durability() {
    return this.data.durability;
  }

  public get enchantments() {
    return this.data.enchantments;
  }

  /**
   * Serialized data of the item encoded in base64
   */
  public get base64() {
    return this.data.b64;
  }
}
