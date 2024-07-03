import { CandidateProfile } from "./candidate-profile";
import { Job } from "./job";

export type CandidateBookmarkedJob = {
    id?: number;
    candidateProfile: CandidateProfile;
    job: Job;
};
