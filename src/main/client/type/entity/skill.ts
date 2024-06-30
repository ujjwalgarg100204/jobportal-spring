import { ExperienceLevel } from "../constants";

import { CandidateProfile } from "./candidate-profile";

export type Skill = {
    id?: number;
    name: string;
    yearsOfExperience: string;
    experienceLevel: ExperienceLevel;
    candidateProfile: CandidateProfile;
};
