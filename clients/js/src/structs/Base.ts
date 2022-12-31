import type { Client } from "../Client";

export class Base {
  public constructor(public readonly client: Client) {
    Object.defineProperty(this, "client", { value: client });
  }
}
