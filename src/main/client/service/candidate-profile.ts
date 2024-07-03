"use server";

import { Id } from "@/type/backend-communication";
import { GetCandidateProfileByIdResponse } from "@/type/entity/candidate-profile";
import { get, patch } from "@/utils/backend";
import { logger } from "@/utils/logger";

const BASE_URL = "/candidate/profile";

export async function getCandidateProfileById(id: number) {
    const response = await get<GetCandidateProfileByIdResponse>(BASE_URL, {
        authenticatedRequest: true,
        jsonRequest: true,
        next: { tags: ["candidate-profile"] },
    });

    if (response.success) {
        logger.info(
            `Successfully fetched candidate profile with id ${id}, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to fetch candidate profile with id ${id}, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function updateCandidateProfile(formData: FormData) {
    const response = await patch<null>(BASE_URL, {
        body: formData,
        authenticatedRequest: true,
        jsonRequest: false,
    });

    if (response.success) {
        logger.info(
            `Successfully updated candidate profile, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to update candidate profile, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function getCandidateProfilePhotoUrl() {
    const response = await get<string>(`${BASE_URL}/photo`, {
        next: { tags: ["candidate-profile"] },
        authenticatedRequest: true,
        jsonRequest: true,
    });

    if (response.success) {
        logger.info(
            `Successfully fetched candidate profile photo url, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to fetch candidate profile photo url, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function checkIfJobIsApplied(id: string | number) {
    const response = await get<boolean>(
        `${BASE_URL}/jobApplications/${id}/@exists`,
        {
            authenticatedRequest: true,
            jsonRequest: true,
            next: { tags: ["job"], revalidate: 24 * 60 * 60 },
        },
    );

    if (response.success) {
        logger.info(
            `Successfully checked if job is applied, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to check if job is applied, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function checkIfJobIsBookmarked(id: Id) {
    const response = await get<boolean>(
        `${BASE_URL}/bookmarkedJobs/${id}/@exists`,
        {
            authenticatedRequest: true,
            jsonRequest: true,
            next: { tags: ["job"], revalidate: 24 * 60 * 60 },
        },
    );

    if (response.success) {
        logger.info(
            `Successfully checked if job is bookmarked, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to check if job is bookmarked, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}
