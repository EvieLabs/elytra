import type { Client } from "../Client";

export class RouteAdapter {
  public constructor(protected readonly client: Client) {}
}
