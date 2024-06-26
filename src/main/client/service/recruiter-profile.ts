"use server";

import { GetRecruiterProfileByIdResponse } from "@/type/entity/recruiter-profile";
import { logger } from "@/utils/logger";
import { get } from "@/utils/backend";

export async function getRecruiterProfileById(id: number) {
    const response = await get<GetRecruiterProfileByIdResponse>(
        "/recruiter/profile",
        { authenticatedRequest: true, jsonRequest: true },
    );

    if (response.success) {
        logger.info(
            `Successfully fetched recruiter profile with id ${id}, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to fetch recruiter profile with id ${id}, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}
