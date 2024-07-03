import { CandidateProfile } from "./candidate-profile";
import { Job } from "./job";

export type CandidateJobApplication = {
    id?: number;
    candidateProfile: CandidateProfile;
    job: Job;
    coverLetter: string;
};
