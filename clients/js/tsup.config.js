import { defineConfig } from "tsup";

export default defineConfig({
  entry: ["src/index.ts"],
  platform: "node",
  format: ["esm"],
  target: "es2022",
  skipNodeModulesBundle: true,
  clean: false,
  shims: true,
  minify: false,
  splitting: false,
  keepNames: true,
  dts: true,
  sourcemap: true,
});
