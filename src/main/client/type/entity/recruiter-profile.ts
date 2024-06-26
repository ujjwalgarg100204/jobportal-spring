import { Address } from "./address";
import { Company } from "./company";
import { ContactInformation } from "./contact-information";
import { Education } from "./education";
import { Interest } from "./interest";
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
};

export type NavbarRequiredRecruiterProfileDetails = Pick<
    RecruiterProfile,
    "firstName" | "lastName" | "hasProfilePhoto"
> & {
    role: "RECRUITER";
    email: User["email"];
    id: Required<RecruiterProfile>["id"];
};

export type GetRecruiterProfileByIdResponse = {
    id: Required<RecruiterProfile>["id"];
} & Omit<RecruiterProfile, "user">;
