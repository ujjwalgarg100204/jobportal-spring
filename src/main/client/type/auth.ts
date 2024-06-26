import { Role } from "./entity/role";
import { User } from "./entity/user";

export type Session = {
    token: string;
    user: Required<Pick<User, "id" | "email">> & {
        role: Role["name"];
    };
} | null;
