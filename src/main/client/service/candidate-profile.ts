"use server";

import { GetCandidateProfileByIdResponse } from "@/type/entity/candidate-profile";
import { get, patch } from "@/utils/backend";
import { logger } from "@/utils/logger";

export async function getCandidateProfileById(id: number) {
    const response = await get<GetCandidateProfileByIdResponse>(
        "/candidate/profile",
        {
            authenticatedRequest: true,
            jsonRequest: true,
            next: { tags: ["candidate-profile"] },
        },
    );

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
    const response = await patch<null>("/candidate/profile", {
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
    const response = await get<string>("/candidate/profile/photo", {
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
