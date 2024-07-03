import { EmploymentType, RemoteType } from "../constants";

import { Address } from "./address";
import { CandidateBookmarkedJob } from "./candidate-bookmarked";
import { CandidateJobApplication } from "./candidate-job-application";
import { Company } from "./company";
import {
    GetRecruiterProfileByIdResponse,
    RecruiterProfile,
} from "./recruiter-profile";

export type Job = {
    id?: number;
    title: string;
    description: string;
    salary?: string;
    employmentType: EmploymentType;
    remoteType: RemoteType;
    noOfVacancy: number;
    createdAt: string;
    hiringComplete: boolean;
    address?: Address;
    company: Company;
    recruiter: RecruiterProfile;
    candidateJobApplications: CandidateJobApplication[];
    candidateBookmarkedJobs: CandidateBookmarkedJob[];
};

export type GetJobByIdResponse = Omit<Job, "id" | "recruiter" | "company"> & {
    id: Required<Job>["id"];
    recruiter: GetRecruiterProfileByIdResponse;
    company: Company;
};

export type GetRecruiterJobsResponse = Omit<Job, "id" | "company"> & {
    id: Required<Job>["id"];
    noOfApplicants: number;
};

export type JobViewPage = Pick<
    Job,
    | "title"
    | "address"
    | "company"
    | "createdAt"
    | "salary"
    | "employmentType"
    | "remoteType"
    | "description"
    | "noOfVacancy"
> & { id: Required<Job>["id"]; noOfAppliedCandidates: number };

export type CreateNewJobRequest = Pick<
    Job,
    | "title"
    | "description"
    | "salary"
    | "employmentType"
    | "remoteType"
    | "noOfVacancy"
> & {
    address?: Omit<Address, "id">;
};

export type UpdateJobRequest = Pick<
    Job,
    | "title"
    | "description"
    | "salary"
    | "employmentType"
    | "remoteType"
    | "noOfVacancy"
    | "address"
    | "hiringComplete"
> & { id: Required<Job>["id"] };
