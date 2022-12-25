import type { PlayerPayload } from "../schemas/infer";
import { PlayerSchema } from "../schemas/Player";
import { Player } from "../structs/Player";
import { RouteAdapter } from "./RouteAdapter";

export class Players extends RouteAdapter {
  /**
   * Get an online player's data
   * @param target Username or UUID of the player to get
   * @returns Player data
   */
  public async get(target: string): Promise<Player>;
  /**
   * Get all online players' data
   * @returns Player data
   */
  public async get(): Promise<Player[]>;
  public async get(target?: string): Promise<Player | Player[]> {
    if (target) return this.getPlayer(target);
    return await this.getPlayers();
  }

  private async getPlayer(target: string) {
    return this.constructPlayer(
      await (
        await this.client.get<Player>(`/players/${target}`)
      ).data
    );
  }

  private async getPlayers() {
    return (await this.client.get<PlayerPayload[]>("/players")).data.map((p) =>
      this.constructPlayer(p)
    );
  }

  public constructPlayer(raw: unknown) {
    return new Player(this.client, PlayerSchema.parse(raw));
  }
}
