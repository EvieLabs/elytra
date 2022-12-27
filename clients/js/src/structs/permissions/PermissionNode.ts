import type { Client } from "../../Client";
import { Base } from "../Base";

export class PermissionNode extends Base {
  public constructor(client: Client, private perm: string) {
    super(client);
  }

  public get identifier() {
    return this.perm;
  }
}
