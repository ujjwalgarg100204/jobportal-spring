"use server";

import type {
    BackendResponse,
    HttpVerb,
    RequestConfig,
} from "@/type/backend-communication";

import { cookies } from "next/headers";

import { env } from "./env";
import { ensureError } from "./error";
import { logger } from "./logger";

const BASE_URL = env.API_BASE_URL;

export async function get<Data>(
    path: string,
    requestConfig: RequestConfig,
): Promise<BackendResponse<Data>> {
    const response = await fetchResponseFromBackend<Data>(
        "GET",
        path,
        requestConfig,
    );

    return response;
}

export async function post<Data>(
    path: string,
    requestConfig: RequestConfig,
): Promise<BackendResponse<Data>> {
    const response = await fetchResponseFromBackend<Data>(
        "POST",
        path,
        requestConfig,
    );

    return response;
}

export async function put<Data>(
    path: string,
    requestConfig: RequestConfig,
): Promise<BackendResponse<Data>> {
    const response = await fetchResponseFromBackend<Data>(
        "PUT",
        path,
        requestConfig,
    );

    return response;
}

export async function deletee<Data>(
    path: string,
    requestConfig: RequestConfig,
): Promise<BackendResponse<Data>> {
    const response = await fetchResponseFromBackend<Data>(
        "DELETE",
        path,
        requestConfig,
    );

    return response;
}

export async function patch<Data>(
    path: string,
    requestConfig: RequestConfig,
): Promise<BackendResponse<Data>> {
    const response = await fetchResponseFromBackend<Data>(
        "PATCH",
        path,
        requestConfig,
    );

    return response;
}

async function fetchResponseFromBackend<Data>(
    method: HttpVerb,
    path: string,
    requestConfig: RequestConfig,
): Promise<BackendResponse<Data>> {
    const { headers, ...restConfig } = requestConfig;
    const requestHeaders: Record<string, string> = {};

    if (restConfig.authenticatedRequest) {
        const jwtToken = cookies().get("jwt-token")?.value;

        if (jwtToken) {
            requestHeaders["Authorization"] = `Bearer ${jwtToken}`;
        }
    }

    if (restConfig.jsonRequest) {
        requestHeaders["Content-Type"] = "application/json";
        requestHeaders["Accept"] = "application/json";
    }
    let response: Response | null = null;

    try {
        response = await fetch(BASE_URL + path, {
            method,
            headers: {
                ...headers,
                ...requestHeaders,
            },
            ...restConfig,
        });
    } catch (e) {
        const err = ensureError(e);

        logger.error(
            `couldn't connect to backend on path:${BASE_URL + path} with *${method}* request, err:${JSON.stringify(err)}`,
        );

        return {
            success: false,
            message: `Failed to connect to backend with message: ${err.message}`,
        };
    }

    try {
        const data = (await response.json()) as BackendResponse<Data>;

        if (data.success) {
            logger.debug(
                `successfully made *${method}* request to backend on path:${BASE_URL + path} and got back data:${JSON.stringify(data)}`,
            );

            return {
                success: true,
                data: data.data,
                message: data.message,
            };
        }
        logger.warn(
            `backend failed on *${method}* request on path:${BASE_URL + path} with message: ${data.message}`,
        );

        return {
            success: false,
            validationErrors: data.validationErrors,
            message: data.message,
        };
    } catch (e) {
        const err = ensureError(e);

        if (err instanceof SyntaxError) {
            logger.error(
                `Failed to convert data from backend to JSON, response:${JSON.stringify(response)}`,
            );

            return {
                success: false,
                message: "Failed to parse JSON from backend",
            };
        }

        logger.error(
            `Failed to parse response with unknown error:${JSON.stringify(err)}`,
        );

        return {
            success: false,
            message: err.message,
        };
    }
}
