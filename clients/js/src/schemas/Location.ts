import { z } from "zod";

export const LocationSchema = z.object({
  world: z.string().nullable(),
  x: z.number(),
  y: z.number(),
  z: z.number(),
  yaw: z.number(),
  pitch: z.number(),
});
