"use server";

import { cookies } from "next/headers";

import { Session } from "@/type/auth";
import { BackendResponse } from "@/type/backend-communication";
import { LoginUser, NewCandidate, NewRecruiter } from "@/type/entity/user";
import { post } from "@/utils/backend";
import { logger } from "@/utils/logger";

export async function registerNewCandidate(
    data: NewCandidate,
): Promise<BackendResponse<null>> {
    const response = await post<null>("/auth/register/candidate", {
        body: JSON.stringify(data),
        authenticatedRequest: false,
        jsonRequest: true,
    });

    if (response.success) {
        logger.info(
            `user with email:${data.email} has been successfully registered`,
        );
    } else {
        logger.warn(
            `user with email:${data.email} was not registered, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function registerNewRecruiter(
    data: NewRecruiter,
): Promise<BackendResponse<null>> {
    const response = await post<null>("/auth/register/recruiter", {
        body: JSON.stringify(data),
        authenticatedRequest: false,
        jsonRequest: true,
    });

    if (response.success) {
        logger.info(
            `user with email:${data.email} has been successfully registered`,
        );
    } else {
        logger.warn(
            `user with email:${data.email} was not registered, response:${JSON.stringify(response)}`,
        );
    }

    return response;
}

export async function loginUser(
    data: LoginUser,
): Promise<BackendResponse<null>> {
    const res = await post<{ token: string }>("/auth/login", {
        body: JSON.stringify(data),
        authenticatedRequest: false,
        jsonRequest: true,
    });

    if (res.success) {
        cookies().set("jwt-token", res.data.token, {
            expires: new Date(Date.now() + 24 * 60 * 60 * 1000),
            httpOnly: true,
        });
        logger.info(
            `user with email:${data.email} has been successfully logged in`,
        );

        return { ...res, data: null };
    } else {
        logger.warn(
            `user withm email:${data.email} was not logged in, response:${JSON.stringify(res)}`,
        );
    }

    return res;
}

export async function getServerSession(): Promise<Session> {
    const jwtToken = cookies().get("jwt-token")?.value;

    if (!jwtToken) return null;

    const response = await post<Session>("/auth/me", {
        body: JSON.stringify({ token: jwtToken }),
        authenticatedRequest: false,
        jsonRequest: true,
    });

    if (response.success) {
        return response.data;
    }

    await logout();

    return null;
}

export async function logout(): Promise<void> {
    cookies().delete("jwt-token");
}
