"use server";

import { z } from "zod";
import { fromZodError } from "zod-validation-error";
import { redirect, RedirectType } from "next/navigation";

import { createNewJob, deleteJobById, updateJob } from "@/service/job";
import { ActionResponse } from "@/type/backend-communication";
import { EmploymentType, RemoteType } from "@/type/constants";
import { CreateNewJobRequest, UpdateJobRequest } from "@/type/entity/job";
import { handleActionLevelError } from "@/utils/error";

export async function createNewJobAction(
    _: ActionResponse<null>,
    formData: FormData,
): Promise<ActionResponse<null>> {
    try {
        const parsed = (
            z.object({
                title: z.string(),
                description: z.string(),
                salary: z.string(),
                employmentType: z.nativeEnum(EmploymentType),
                remoteType: z.nativeEnum(RemoteType),
                noOfVacancy: z.coerce.number(),
                address: z
                    .object({
                        city: z.string(),
                        state: z.string().min(1),
                        country: z.string().min(1),
                    })
                    .optional(),
            }) satisfies z.ZodType<CreateNewJobRequest>
        ).safeParse({
            title: formData.get("title"),
            description: formData.get("description"),
            salary: formData.get("salary"),
            employmentType: formData.get("employmentType"),
            remoteType: formData.get("remoteType"),
            noOfVacancy: formData.get("noOfVacancy"),
            address: {
                city: formData.get("address.city"),
                state: formData.get("address.state"),
                country: formData.get("address.country"),
            },
        });

        if (!parsed.success) {
            throw fromZodError(parsed.error);
        }
        const response = await createNewJob(parsed.data);

        if (response.success) {
            return response;
        }
        throw new Error(response.message);
    } catch (e) {
        return handleActionLevelError(e);
    }
}

export async function updateJobAction(
    _: ActionResponse<null>,
    formData: FormData,
): Promise<ActionResponse<null>> {
    try {
        const parsed = (
            z.object({
                id: z.coerce.number(),
                title: z.string(),
                description: z.string(),
                salary: z.string(),
                employmentType: z.nativeEnum(EmploymentType),
                remoteType: z.nativeEnum(RemoteType),
                noOfVacancy: z.coerce.number(),
                address: z.object({
                    id: z.coerce.number().optional(),
                    city: z.string().optional(),
                    state: z.string().min(1),
                    country: z.string().min(1),
                }),
                hiringComplete: z.boolean(),
            }) satisfies z.ZodType<UpdateJobRequest>
        ).safeParse({
            id: formData.get("id"),
            title: formData.get("title"),
            description: formData.get("description"),
            salary: formData.get("salary"),
            employmentType: formData.get("employmentType"),
            remoteType: formData.get("remoteType"),
            noOfVacancy: formData.get("noOfVacancy"),
            address: {
                id: formData.get("address.id"),
                city: formData.get("address.city"),
                state: formData.get("address.state"),
                country: formData.get("address.country"),
            },
            hiringComplete: formData.get("hiringComplete") === "yes",
        });

        if (!parsed.success) {
            throw fromZodError(parsed.error);
        }

        const response = await updateJob(parsed.data);

        if (response.success) {
            return response;
        }
        throw new Error(response.message);
    } catch (e) {
        return handleActionLevelError(e);
    }
}

export async function deleteJobAction(
    _: ActionResponse<null>,
    formData: FormData,
) {
    try {
        const response = await deleteJobById(String(formData.get("id")));

        if (response.success) {
            redirect("/dashboard/r/job", RedirectType.replace);
        }
        throw new Error(response.message);
    } catch (e) {
        return handleActionLevelError(e);
    }
}
