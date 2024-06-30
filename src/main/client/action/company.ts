"use server";

import { z } from "zod";
import { fromZodError } from "zod-validation-error";

import { ActionResponse } from "@/type/backend-communication";
import { UpdateCompanyRequest } from "@/type/entity/company";
import { handleActionLevelError } from "@/utils/error";
import { patch } from "@/utils/backend";

export async function updateCompanyAction(
    _: ActionResponse<null>,
    formData: FormData,
): Promise<ActionResponse<null>> {
    try {
        const parsed = (
            z.object({
                id: z.coerce.number(),
                name: z.string(),
                address: z
                    .object({
                        id: z.coerce.number().optional(),
                        city: z.string().optional(),
                        state: z.string(),
                        country: z.string(),
                    })
                    .optional(),
            }) satisfies z.ZodType<UpdateCompanyRequest>
        ).safeParse({
            id: formData.get("id"),
            name: formData.get("name"),
            address: {
                id: formData.get("address.id"),
                city: formData.get("address.city"),
                state: formData.get("address.state"),
                country: formData.get("address.country"),
            },
        });

        if (!parsed.success) {
            throw fromZodError(parsed.error);
        }
        const logo = formData.get("logo");

        // create new form data
        const newFormData = new FormData();

        newFormData.set(
            "company",
            new Blob([JSON.stringify(parsed.data)], {
                type: "application/json",
            }),
        );
        if (logo) {
            newFormData.set("logo", logo);
        }

        const response = await patch<null>("/company", {
            body: newFormData,
            authenticatedRequest: true,
            jsonRequest: false,
        });

        if (response.success) {
            return response;
        }
        throw new Error(response.message);
    } catch (e) {
        return handleActionLevelError(e);
    }
}
