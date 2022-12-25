import type { z } from "zod";
import type { ItemStackSchema } from "./ItemStack";
import type { LocationSchema } from "./Location";
import type { InventorySchema, PlayerSchema } from "./Player";

export type PlayerPayload = z.infer<typeof PlayerSchema>;
export type ItemStackPayload = z.infer<typeof ItemStackSchema>;
export type InventoryPayload = z.infer<typeof InventorySchema>;
export type LocationPayload = z.infer<typeof LocationSchema>;
