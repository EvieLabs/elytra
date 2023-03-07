import { z } from "zod";

const ItemStackNOTShulkerBoxSchema = z.object({
  name: z.string().nullable(),
  id: z.string(),
  type: z.union([z.literal("item"), z.literal("block")]),
  amount: z.number(),
  durability: z.number(),
  enchantments: z.record(z.number()),
  b64: z.string(),
  lore: z.array(z.string()),
});

export const ItemStackSchema = ItemStackNOTShulkerBoxSchema.extend({
  itemsInside: z.array(
    z.union([
      ItemStackNOTShulkerBoxSchema.extend({
        itemsInside: z.array(z.null()),
      }),
      z.null(),
    ])
  ),
});
