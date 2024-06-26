"use server";

import { GetCandidateProfileByIdResponse } from "@/type/entity/candidate-profile";
import { get } from "@/utils/backend";
import { logger } from "@/utils/logger";

export async function getCandidateProfileById(id: number) {
    const response = await get<GetCandidateProfileByIdResponse>(
        "/candidate/profile",
        { authenticatedRequest: true, jsonRequest: true },
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
