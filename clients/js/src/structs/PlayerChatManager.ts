import { z } from "zod";
import type { Client } from "../Client";
import { Base } from "./Base";
import type { Player } from "./Player";

export class PlayerChatManager extends Base {
  public constructor(client: Client, private player: Player) {
    super(client);
  }

  /**
   * Send a message to the player
   * @param message Message to send
   * @returns New player instance
   */
  public async send(message: string) {
    return this.client.players.constructPlayer(
      (
        await this.client.post(`/players/${this.player.uuid}/chat`, {
          message,
        })
      ).data
    );
  }

  /**
   * Creates a message collector on the player this manager is for.
   * @returns Whether the collector was created or it already existed
   */
  public async createCollector() {
    const res = z
      .object({
        status: z.union([
          z.literal("collector-exists"),
          z.literal("collector-created"),
        ]),
      })
      .parse(
        await (
          await this.client.post(`/players/${this.player.uuid}/chat/collector`)
        ).data
      );

    switch (res.status) {
      case "collector-exists":
        return false;
      case "collector-created":
        return true;
    }
  }

  /**
   * Returns the collected message
   * @returns Collected message
   */
  public async checkCollector() {
    return z
      .union([
        z.object({
          status: z.union([z.literal("no-collector"), z.literal("waiting")]),
        }),
        z.object({
          status: z.literal("collected"),
          result: z.string(),
        }),
      ])
      .parse(
        (await this.client.get(`/players/${this.player.uuid}/chat/collector`))
          .data
      );
  }
}
