import { RouteAdapter } from "./RouteAdapter";

export class Health extends RouteAdapter {
  public async ping() {
    return this.client.get("/health");
  }
}
