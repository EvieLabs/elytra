import { z } from "zod";
import { ItemStackSchema } from "./ItemStack";
import { LocationSchema } from "./Location";

export const InventorySchema = z.object({
  contents: z.array(ItemStackSchema.nullable()),
  armorContents: z.array(ItemStackSchema.nullable()),
});

export const PlayerSchema = z.object({
  name: z.string(),
  uuid: z.string(),
  op: z.boolean(),
  banned: z.boolean(),
  whitelisted: z.boolean(),
  ip: z.string().nullable(),
  location: LocationSchema,
  inventory: InventorySchema,
});
