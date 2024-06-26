import { Address } from "./address";

export type Company = {
    id?: number;
    name: string;
    hasLogo: boolean;
    address?: Address;
};
