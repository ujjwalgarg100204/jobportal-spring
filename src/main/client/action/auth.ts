"use server";

import { z } from "zod";
import { fromZodError } from "zod-validation-error";
import { redirect } from "next/navigation";

import {
    loginUser,
    logout,
    registerNewCandidate,
    registerNewRecruiter,
} from "@/service/auth";
import { ActionResponse } from "@/type/backend-communication";
import { LoginUser, NewCandidate, NewRecruiter } from "@/type/entity/user";
import { handleActionLevelError } from "@/utils/error";

export async function registerNewCandidateAction(
    _: ActionResponse<null>,
    formData: FormData,
): Promise<ActionResponse<null>> {
    try {
        const parsed = (
            z.object({
                email: z.string().min(1).email(),
                password: z.string().min(1),
                firstName: z.string().min(1),
                lastName: z.string().min(1),
                shortAbout: z.string().min(1),
                contactInformation: z.object({
                    phone: z.string().min(1).optional(),
                    twitterHandle: z.string().url().optional(),
                    linkedinHandle: z.string().url().optional(),
                    githubHandle: z.string().url().optional(),
                }),
            }) satisfies z.ZodType<NewCandidate>
        ).safeParse({
            email: formData.get("email"),
            password: formData.get("password"),
            firstName: formData.get("firstName"),
            lastName: formData.get("lastName"),
            shortAbout: formData.get("shortAbout"),
            contactInformation: {
                phone: formData.get("contactInformation.phone"),
                twitterHandle: formData.get("contactInformation.twitterHandle"),
                linkedinHandle: formData.get(
                    "contactInformation.linkedinHandle",
                ),
                githubHandle: formData.get("contactInformation.githubHandle"),
            },
        });

        if (!parsed.success) {
            throw fromZodError(parsed.error);
        }

        const response = await registerNewCandidate(parsed.data);

        if (response.success) {
            return { success: true, message: response.message, data: null };
        }

        throw new Error(response.message);
    } catch (e) {
        return handleActionLevelError(e);
    }
}

export async function registerNewRecruiterAction(
    _: ActionResponse<null>,
    formData: FormData,
): Promise<ActionResponse<null>> {
    try {
        const parsed = (
            z.object({
                email: z.string().min(1).email(),
                password: z.string().min(1),
                firstName: z.string().min(1),
                lastName: z.string().min(1),
                company: z.object({
                    name: z.string().min(1),
                    address: z
                        .object({
                            city: z.string().min(1).optional(),
                            state: z.string().min(1),
                            country: z.string().min(1),
                        })
                        .optional(),
                }),
            }) satisfies z.ZodType<NewRecruiter>
        ).safeParse({
            email: formData.get("email"),
            password: formData.get("password"),
            firstName: formData.get("firstName"),
            lastName: formData.get("lastName"),
            company: {
                name: formData.get("company.name"),
                address: {
                    city: formData.get("company.address.city"),
                    state: formData.get("company.address.state"),
                    country: formData.get("company.address.country"),
                },
            },
        });

        if (!parsed.success) {
            throw fromZodError(parsed.error);
        }

        const response = await registerNewRecruiter(parsed.data);

        if (response.success) {
            return { success: true, message: response.message, data: null };
        }

        throw new Error(response.message);
    } catch (e) {
        return handleActionLevelError(e);
    }
}

export async function loginAction(
    _: ActionResponse<null>,
    formData: FormData,
): Promise<ActionResponse<null>> {
    try {
        const parsed = (
            z.object({
                email: z.string().min(1).email(),
                password: z.string().min(1),
                roleId: z.coerce.number(),
            }) satisfies z.ZodType<LoginUser>
        ).safeParse({
            email: formData.get("email"),
            password: formData.get("password"),
            roleId: formData.get("roleId"),
        });

        if (!parsed.success) {
            throw fromZodError(parsed.error);
        }
        const response = await loginUser(parsed.data);

        if (response.success) {
            return {
                success: true,
                message: response.message,
                data: null,
            };
        }
        throw new Error(response.message);
    } catch (e) {
        return handleActionLevelError(e);
    }
}

export async function logoutAction(_: FormData): Promise<ActionResponse<null>> {
    await logout();
    redirect("/");
}
