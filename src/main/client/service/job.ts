"use server";

import { UpdateJobRequest } from "@/type/entity/job";
import { CreateNewJobRequest } from "@/type/entity/job";
import { patch, post } from "@/utils/backend";
import { logger } from "@/utils/logger";

const BASE_URL = "/job";

export async function createNewJob(data: CreateNewJobRequest) {
    const response = await post<null>(BASE_URL, {
        authenticatedRequest: true,
        jsonRequest: true,
        body: JSON.stringify(data),
    });

    if (response.success) {
        logger.info(`Successfully created new job, response:${response}`);
    } else {
        logger.error(`Failed to create new job, response:${response}`);
    }

    return response;
}

export async function updateJob(data: UpdateJobRequest) {
    const response = await patch<null>(BASE_URL, {
        authenticatedRequest: true,
        jsonRequest: true,
        body: JSON.stringify(data),
    });

    if (response.success) {
        logger.info(`Successfully updated job, response:${response}`);
    } else {
        logger.error(`Failed to update job, response:${response}`);
    }

    return response;
}
