"use server";

import { revalidateTag } from "next/cache";

import {
    CreateNewJobRequest,
    GetJobByIdResponse,
    UpdateJobRequest,
} from "@/type/entity/job";
import { deletee, get, patch, post } from "@/utils/backend";
import { logger } from "@/utils/logger";

const BASE_URL = "/job";

export async function createNewJob(data: CreateNewJobRequest) {
    const response = await post<null>(BASE_URL, {
        authenticatedRequest: true,
        jsonRequest: true,
        body: JSON.stringify(data),
    });

    if (response.success) {
        logger.info(
            `Successfully created new job, response:${JSON.stringify(response)}`,
        );
        revalidateTag("job");
    } else {
        logger.error(
            `Failed to create new job, response:${JSON.stringify(response)}`,
        );
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
        logger.info(
            `Successfully updated job, response:${JSON.stringify(response)}`,
        );
        revalidateTag("job");
    } else {
        logger.error(
            `Failed to update job, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function getJobById(id: number | string) {
    const response = await get<GetJobByIdResponse>(`${BASE_URL}/${id}`, {
        authenticatedRequest: true,
        jsonRequest: true,
        next: { tags: ["job"] },
    });

    if (response.success) {
        logger.info(
            `Successfully fetched job by id, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to fetch job by id, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function deleteJobById(id: number | string) {
    const response = await deletee<null>(`${BASE_URL}/${id}`, {
        authenticatedRequest: true,
        jsonRequest: true,
    });

    if (response.success) {
        revalidateTag("job");
        logger.info(
            `Successfully deleted job by id, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to delete by id, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function searchJobs() {
    const response = await get<GetJobByIdResponse[]>(`${BASE_URL}/@search`, {
        authenticatedRequest: true,
        jsonRequest: true,
        next: { tags: ["job"] },
    });

    if (response.success) {
        logger.info(
            `Successfully fetched jobs, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Failed to fetch jobs, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}
