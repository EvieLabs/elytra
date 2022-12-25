export class LangService {
  private lang?: Record<string, string>;
  public constructor(private options?: { lang?: string }) {}

  public translate(key: string) {
    if (!this.lang) {
      throw new Error("LangService not initialized");
    }

    return this.lang[key] ?? key;
  }

  public translateItemStackType(type: string, id: string) {
    return this.translate(`${type}.minecraft.${id.toLowerCase()}`);
  }

  public async setup() {
    const targetLang = this.options?.lang ?? "en_us";
    const res = await fetch(
      `https://raw.githubusercontent.com/TristanSMP/mc-lang/main/${targetLang}.json`
    );

    if (res.status >= 400) {
      throw new Error(`Failed to fetch language file for ${targetLang}`);
    }

    this.lang = await res.json();
  }
}
