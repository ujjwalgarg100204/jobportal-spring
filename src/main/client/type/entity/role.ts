import { ERole } from "../constants";

import { User } from "./user";

export type Role = {
    id?: number;
    name: ERole;
    users: User[];
};
