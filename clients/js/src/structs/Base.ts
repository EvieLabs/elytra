import type { Client } from "../Client";

export class Base {
  public constructor(protected readonly client: Client) {}
}
