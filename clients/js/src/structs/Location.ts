import type { Client } from "../Client";
import type { LocationPayload } from "../schemas/infer";
import { Base } from "./Base";

export class Location extends Base {
  public constructor(
    client: Client,
    private target: string,
    private data: LocationPayload
  ) {
    super(client);
  }

  public get x(): number {
    return this.data.x;
  }

  public get y(): number {
    return this.data.y;
  }

  public get z(): number {
    return this.data.z;
  }

  public get yaw(): number {
    return this.data.yaw;
  }

  public get pitch(): number {
    return this.data.pitch;
  }

  public get world(): string | null {
    return this.data.world;
  }

  /**
   * Set the player's location
   * @param location Set the player's location
   * @returns Player data
   */
  public async set(location: LocationPayload) {
    return this.client.players.constructPlayer(
      (await this.client.post(`/players/${this.target}/location`, location))
        .data
    );
  }
}
