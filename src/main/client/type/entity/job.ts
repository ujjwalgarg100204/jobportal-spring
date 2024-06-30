import { EmploymentType, RemoteType } from "../constants";

import { Address } from "./address";
import { Company } from "./company";
import { RecruiterProfile } from "./recruiter-profile";

export type Job = {
    id?: number;
    title: string;
    description: string;
    salary?: string;
    employmentType: EmploymentType;
    remoteType: RemoteType;
    noOfVacancy: number;
    recruiter: RecruiterProfile;
    address?: Address;
    company: Company;
    hiringComplete: boolean;
    createdAt: string;
};

export type GetRecruiterJobsResponse = Omit<Job, "id" | "company"> & {
    id: Required<Job>["id"];
    noOfApplicants: number;
};

export type JobViewPage = Omit<Job, "id"> & { id: Required<Job>["id"] };

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
