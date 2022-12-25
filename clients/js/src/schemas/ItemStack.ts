import { z } from "zod";

export const ItemStackSchema = z.object({
  name: z.string().nullable(),
  id: z.string(),
  type: z.union([z.literal("item"), z.literal("block")]),
  amount: z.number(),
  durability: z.number(),
  enchantments: z.record(z.number()),
  b64: z.string(),
});
