import { EmploymentType } from "../constants";

import { Address } from "./address";
import { ContactInformation } from "./contact-information";
import { Education } from "./education";
import { Interest } from "./interest";
import { Skill } from "./skill";
import { User } from "./user";

export type CandidateProfile = {
    id?: number;
    firstName: string;
    lastName: string;
    shortAbout: string;
    about?: string;
    hasProfilePhoto: boolean;
    hasResume: boolean;
    preferredEmploymentType?: EmploymentType;
    user: User;
    address?: Address;
    contactInformation: ContactInformation;
    educations?: Education[];
    interests?: Interest[];
    skills?: Skill[];
};

export type NavbarRequiredCandidateProfileDetails = Pick<
    CandidateProfile,
    "firstName" | "lastName" | "hasProfilePhoto"
> & {
    role: "CANDIDATE";
    email: User["email"];
    id: Required<CandidateProfile>["id"];
};

export type GetCandidateProfileByIdResponse = {
    id: Required<CandidateProfile>["id"];
} & Omit<CandidateProfile, "user">;
