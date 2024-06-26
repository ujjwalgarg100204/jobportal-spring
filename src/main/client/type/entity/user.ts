import { CandidateProfile } from "./candidate-profile";
import { Company } from "./company";
import { RecruiterProfile } from "./recruiter-profile";
import { Role } from "./role";

export type User = {
    id?: number;
    email: string;
    password: string;
    registrationDate: string;
    role: Role;
};

export type NewCandidate = Pick<User, "email" | "password"> &
    Pick<
        CandidateProfile,
        "firstName" | "lastName" | "shortAbout" | "contactInformation"
    >;

export type NewRecruiter = Pick<User, "email" | "password"> &
    Pick<RecruiterProfile, "firstName" | "lastName"> & {
        company: Pick<Company, "name" | "address">;
    };

export type LoginUser = Pick<User, "email" | "password"> & {
    roleId: number;
};
