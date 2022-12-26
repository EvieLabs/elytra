import { Health } from "./routes/Health";
import { Players } from "./routes/Players";
import { LangService } from "./services/LangService";

export interface ClientOptions {
  apiRoot: string;
  lang?: string;
  token?: string;
}

export interface ClientResponse<T> {
  data: T;
  headers: Headers;
}

export class Client {
  constructor(private readonly options: ClientOptions) {
    this.lang = new LangService({ lang: options.lang });
  }

  private _setup = false;

  public lang: LangService;

  public health = new Health(this);
  public players = new Players(this);

  public minecraftVersion = "Unknown";
  public bukkitVersion = "Unknown";

  private async _request<T>(
    method: string,
    path: string,
    body?: any
  ): Promise<ClientResponse<T>> {
    await this.setup();

    const res = await fetch(`${this.options.apiRoot}${path}`, {
      method,
      headers: {
        "Content-Type": "application/json",
        Authorization: this.options.token || "",
      },
      body: JSON.stringify(body),
    });

    if (res.status >= 400) {
      throw new Error(
        `${method} ${path} returned ${res.status} ${
          res.statusText
        } with body: ${await res.text()}`
      );
    }

    return {
      data: await res.json(),
      headers: res.headers,
    };
  }

  /**
   * Performs a GET request to the API
   * @param path Relative path to the API root
   * @returns Response data
   */
  public async get<T>(path: string): Promise<ClientResponse<T>> {
    return this._request("GET", path);
  }

  /**
   * Performs a POST request to the API
   * @param path Relative path to the API root
   * @param body Request body
   * @returns Response data
   */
  public async post<T>(path: string, body?: any): Promise<ClientResponse<T>> {
    return this._request("POST", path, body);
  }

  /**
   * Performs a DELETE request to the API
   * @param path Relative path to the API root
   * @returns Response data
   * @returns
   */
  public async delete<T>(path: string): Promise<ClientResponse<T>> {
    return this._request("DELETE", path);
  }

  /**
   * Sets up the client by pinging the server and receiving the initial metadata
   * @returns Promise that resolves when the client is ready to be used
   */
  public async setup() {
    if (this._setup) return;
    this._setup = true;

    const res = await this.health.ping();

    this.minecraftVersion = res.headers.get("X-Minecraft-Version") || "Unknown";
    this.bukkitVersion = res.headers.get("X-Bukkit-Version") || "Unknown";

    await this.lang.setup();
  }
}
