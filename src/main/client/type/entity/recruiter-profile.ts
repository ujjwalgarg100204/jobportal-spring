import { ERole } from "../constants";

import { Address } from "./address";
import { Company } from "./company";
import { ContactInformation } from "./contact-information";
import { Education } from "./education";
import { Interest } from "./interest";
import { Job } from "./job";
import { User } from "./user";

export type RecruiterProfile = {
    id?: number;
    firstName: string;
    lastName: string;
    about?: string;
    hasProfilePhoto: boolean;
    user: User;
    address?: Address;
    contactInformation?: ContactInformation;
    educations?: Education[];
    interests?: Interest[];
    company: Company;
    jobs?: Job[];
};

export type NavbarRequiredRecruiterProfileDetails = Pick<
    RecruiterProfile,
    "firstName" | "lastName"
> & {
    role: ERole.RECRUITER;
    email: User["email"];
    id: Required<RecruiterProfile>["id"];
    profilePhotoUrl?: string;
};

export type GetRecruiterProfileByIdResponse = {
    id: Required<RecruiterProfile>["id"];
    profilePhotoUrl?: string;
} & Omit<RecruiterProfile, "user">;

export type UpdateRecruiterProfileRequest = Pick<
    RecruiterProfile,
    | "firstName"
    | "lastName"
    | "about"
    | "address"
    | "contactInformation"
    | "educations"
    | "interests"
> & { company: Omit<Company, "hasLogo">; id: Required<RecruiterProfile>["id"] };
