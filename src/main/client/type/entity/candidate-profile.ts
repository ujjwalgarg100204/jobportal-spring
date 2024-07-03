import { ERole, EmploymentType, WorkAuthorization } from "../constants";

import { Address } from "./address";
import { CandidateBookmarkedJob } from "./candidate-bookmarked";
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
    portfolioWebsite?: string;
    preferredEmploymentType?: EmploymentType;
    workAuthorization?: WorkAuthorization;
    user: User;
    address?: Address;
    contactInformation: ContactInformation;
    educations?: Education[];
    interests?: Interest[];
    skills?: Skill[];
    bookmarkedJobs?: CandidateBookmarkedJob[];
};

export type NavbarRequiredCandidateProfileDetails = Pick<
    CandidateProfile,
    "firstName" | "lastName"
> & {
    role: ERole.CANDIDATE;
    email: User["email"];
    id: Required<CandidateProfile>["id"];
    profilePhotoUrl?: string;
};

export type GetCandidateProfileByIdResponse = {
    id: Required<CandidateProfile>["id"];
} & Omit<CandidateProfile, "user">;

export type UpdateCandidateProfileRequest = Pick<
    CandidateProfile,
    | "firstName"
    | "lastName"
    | "shortAbout"
    | "about"
    | "preferredEmploymentType"
    | "workAuthorization"
    | "address"
    | "contactInformation"
    | "educations"
    | "interests"
> & {
    skills?: Omit<Skill, "candidateProfile">[];
};
