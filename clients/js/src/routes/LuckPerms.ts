import type { Player } from "../structs";
import { RouteAdapter } from "./RouteAdapter";

export class LuckPerms extends RouteAdapter {
  /**
   * Adds a permission node to the player
   * @param target Username or UUID of the player to target
   * @param perm Permission node to add
   * @throws If something goes wrong
   */
  public async addPermission(target: string | Player, perm: string) {
    const t = typeof target === "string" ? target : target.uuid;
    await this.client.post(`/luckperms/users/${t}/permissions`, {
      permission: perm,
    });
  }

  /**
   * Removes a permission node from the player
   * @param target Username or UUID of the player to target
   * @param perm Permission node to remove
   * @throws If something goes wrong
   */
  public async removePermission(target: string | Player, perm: string) {
    const t = typeof target === "string" ? target : target.uuid;
    await this.client.delete(`/luckperms/users/${t}/permissions/${perm}`);
  }
}
