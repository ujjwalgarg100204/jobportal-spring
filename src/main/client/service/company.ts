"use server";

import { revalidateTag } from "next/cache";

import { BackendResponse } from "@/type/backend-communication";
import { GetAllCompanyResponse } from "@/type/entity/company";
import { get, patch, put } from "@/utils/backend";
import { logger } from "@/utils/logger";

export async function getCompanyLogoUrl(id: number) {
    const response = await get<string>(`/company/${id}/logo`, {
        authenticatedRequest: true,
        jsonRequest: true,
        next: { revalidate: 5000, tags: ["company"] },
    });

    if (response.success) {
        logger.info(
            `Company logo fetched successfully, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Company logo fetch failed with error: response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function getAllCompany(): Promise<
    BackendResponse<GetAllCompanyResponse>
> {
    const response = await get<GetAllCompanyResponse>("/company", {
        authenticatedRequest: true,
        jsonRequest: true,
        next: { tags: ["company"] },
    });

    if (response.success) {
        logger.info(
            `Company fetched successfully, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Company fetch failed with error: response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function createNewCompany(
    formData: FormData,
): Promise<BackendResponse<null>> {
    const response = await put<null>("/company", {
        body: formData,
        authenticatedRequest: false,
        jsonRequest: false,
    });

    if (response.success) {
        logger.info(
            `Company created successfully, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Company creation failed, response:${JSON.stringify(response)}`,
        );
    }

    revalidateTag("company");

    return response;
}

export async function updateCompany(
    formData: FormData,
): Promise<BackendResponse<null>> {
    const response = await patch<null>("/company", {
        body: formData,
        authenticatedRequest: true,
        jsonRequest: false,
    });

    if (response.success) {
        logger.info(
            `Company updated successfully, response:${JSON.stringify(response)}`,
        );
    } else {
        logger.error(
            `Company update failed, response:${JSON.stringify(response)}`,
        );
    }

    revalidateTag("company");

    return response;
}
