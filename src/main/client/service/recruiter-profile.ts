"use server";

import { BackendResponse } from "@/type/backend-communication";
import { GetCompanyOfCurrentRecruiterResponse } from "@/type/entity/company";
import { GetRecruiterJobsResponse } from "@/type/entity/job";
import { GetRecruiterProfileByIdResponse } from "@/type/entity/recruiter-profile";
import { get, patch } from "@/utils/backend";
import { logger } from "@/utils/logger";

const BASE_URL = "/recruiter/profile";

export async function getAllJobsOfRecruiterWithApplicantCount() {
    const response = await get<GetRecruiterJobsResponse[]>(`${BASE_URL}/job`, {
        authenticatedRequest: true,
        jsonRequest: true,
        next: {
            tags: ["job"],
            revalidate: 60 * 5,
        },
    });

    if (response.success) {
        logger.info(
            `Successfully fetched all jobs of recruiter with applicant count, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to fetch all jobs of recruiter with applicant count, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function getCurrentRecruiterProfile() {
    const response = await get<GetRecruiterProfileByIdResponse>(
        "/recruiter/profile",
        {
            authenticatedRequest: true,
            jsonRequest: true,
            next: {
                tags: ["recruiter-profile"],
                revalidate: 1000 * 60 * 60 * 24,
            },
        },
    );

    if (response.success) {
        logger.info(
            `Successfully fetched recruiter profile with, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to fetch recruiter profile with , response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function updateRecruiterProfile(formData: FormData) {
    const response = await patch<null>("/recruiter/profile", {
        body: formData,
        authenticatedRequest: true,
        jsonRequest: false,
    });

    if (response.success) {
        logger.info(
            `Successfully updated recruiter profile, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to update candidate profile, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function getRecruiterProfilePhotoUrl() {
    const response = await get<string>("/recruiter/profile/photo", {
        authenticatedRequest: true,
        jsonRequest: true,
        next: { tags: ["recruiter-profile"] },
    });

    if (response.success) {
        logger.info(
            `Successfully fetched recruiter profile photo url, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to fetch recruiter profile photo url, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function getCurrentRecruiterCompany(): Promise<
    BackendResponse<GetCompanyOfCurrentRecruiterResponse>
> {
    const response = await get<GetCompanyOfCurrentRecruiterResponse>(
        "/recruiter/profile/company",
        {
            authenticatedRequest: true,
            jsonRequest: true,
            next: { tags: ["company"] },
        },
    );

    if (response.success) {
        logger.info(
            `Successfully fetched recruiter company, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to fetch recruiter company, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}
