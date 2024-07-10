"use server";

import { z } from "zod";
import { fromZodError } from "zod-validation-error";
import { revalidateTag } from "next/cache";

import { ActionResponse } from "@/type/backend-communication";
import { EmploymentType, ExperienceLevel } from "@/type/constants";
import { UpdateCandidateProfileRequest } from "@/type/entity/candidate-profile";
import { patch } from "@/utils/backend";
import { handleActionLevelError } from "@/utils/error";
import { UpdateRecruiterProfileRequest } from "@/type/entity/recruiter-profile";

export async function updateRecruiterProfileAction(
    _: ActionResponse<null>,
    formData: FormData,
): Promise<ActionResponse<null>> {
    const parsed = (
        z.object({
            firstName: z.string().min(1),
            lastName: z.string().min(1),
            about: z.string().min(1),
            address: z
                .object({
                    id: z.coerce.number().optional(),
                    city: z.string().min(1).optional(),
                    state: z.string().min(1),
                    country: z.string().min(1),
                })
                .optional(),
            contactInformation: z
                .object({
                    id: z.coerce.number().optional(),
                    phone: z.string().min(10).optional(),
                    twitterHandle: z.string().url().optional(),
                    linkedinHandle: z.string().url().optional(),
                    githubHandle: z.string().url().optional(),
                })
                .optional(),
            educations: z
                .object({
                    id: z.coerce.number().optional(),
                    title: z.string().min(1),
                    description: z.string().min(1),
                })
                .array()
                .optional(),
            interests: z
                .object({
                    id: z.coerce.number().optional(),
                    title: z.string().min(1),
                })
                .array()
                .optional(),
            company: z.object({
                id: z.number(),
                name: z.string().min(1),
                address: z
                    .object({
                        id: z.coerce.number().optional(),
                        city: z.string().min(1).optional(),
                        state: z.string().min(1),
                        country: z.string().min(1),
                    })
                    .optional(),
            }),
        }) satisfies z.ZodType<UpdateRecruiterProfileRequest>
    ).safeParse({
        firstName: formData.get("firstName"),
        lastName: formData.get("lastName"),
        about: formData.get("about"),
        address: {
            id: formData.get("address.id"),
            city: formData.get("address.city"),
            state: formData.get("address.state"),
            country: formData.get("address.country"),
        },
        contactInformation: {
            id: Number(formData.get("contactInformation.id")),
            phone: formData.get("contactInformation.phone"),
            twitterHandle: formData.get("contactInformation.twitterHandle"),
            linkedinHandle: formData.get("contactInformation.linkedinHandle"),
            githubHandle: formData.get("contactInformation.githubHandle"),
        },
        educations: __getEducationsFromFormData(formData),
        interests: __getInterestsFromFormData(formData),
        company: {
            id: formData.get("company.id"),
            name: formData.get("company.name"),
            address: {
                id: formData.get("company.address.id"),
                city: formData.get("company.address.city"),
                state: formData.get("company.address.state"),
                country: formData.get("company.address.country"),
            },
        },
    });

    try {
        if (!parsed.success) {
            throw fromZodError(parsed.error);
        }
        const profilePhoto = formData.get("profilePhoto");

        // create new form data object
        const newFormData = new FormData();

        newFormData.set(
            "profile",
            new Blob([JSON.stringify(parsed.data)], {
                type: "application/json",
            }),
        );
        if (profilePhoto) {
            newFormData.set("profilePhoto", profilePhoto);
        }

        const response = await patch<null>("/recruiter/profile", {
            body: newFormData,
            authenticatedRequest: true,
            jsonRequest: false,
        });

        if (response.success) {
            revalidateTag("recruiter-profile");

            return response;
        }

        throw new Error(response.message);
    } catch (e) {
        return handleActionLevelError(e);
    }
}

export async function updateCandidateProfileAction(
    _: ActionResponse<null>,
    formData: FormData,
): Promise<ActionResponse<null>> {
    const parsed = (
        z.object({
            firstName: z.string().min(1),
            lastName: z.string().min(1),
            shortAbout: z.string().min(1),
            about: z.string().min(1).optional(),
            preferredEmploymentType: z.nativeEnum(EmploymentType).optional(),
            address: z.object({
                id: z.coerce.number().optional(),
                city: z.string().min(1).optional(),
                state: z.string().min(1),
                country: z.string().min(1),
            }),
            contactInformation: z.object({
                id: z.coerce.number().optional(),
                phone: z.string().min(10).optional(),
                twitterHandle: z.string().url().optional(),
                linkedinHandle: z.string().url().optional(),
                githubHandle: z.string().url().optional(),
            }),
            educations: z
                .object({
                    id: z.coerce.number().optional(),
                    title: z.string().min(1),
                    description: z.string().min(1),
                })
                .array(),
            interests: z
                .object({
                    id: z.coerce.number().optional(),
                    title: z.string().min(1),
                })
                .array(),
            skills: z
                .object({
                    id: z.coerce.number().optional(),
                    name: z.string().min(1),
                    yearsOfExperience: z.string().min(1),
                    experienceLevel: z.nativeEnum(ExperienceLevel),
                })
                .array(),
        }) satisfies z.ZodType<UpdateCandidateProfileRequest>
    ).safeParse({
        firstName: formData.get("firstName"),
        lastName: formData.get("lastName"),
        shortAbout: formData.get("shortAbout"),
        preferredEmploymentType: formData.get("preferredEmploymentType"),
        address: {
            id: formData.get("address.id"),
            city: formData.get("address.city"),
            state: formData.get("address.state"),
            country: formData.get("address.country"),
        },
        contactInformation: {
            id: Number(formData.get("contactInformation.id")),
            phone: formData.get("contactInformation.phone"),
            twitterHandle: formData.get("contactInformation.twitterHandle"),
            linkedinHandle: formData.get("contactInformation.linkedinHandle"),
            githubHandle: formData.get("contactInformation.githubHandle"),
        },
        educations: __getEducationsFromFormData(formData),
        interests: __getInterestsFromFormData(formData),
        skills: __getSkillsFromFormData(formData),
    });

    try {
        if (!parsed.success) {
            throw fromZodError(parsed.error);
        }

        const resume = formData.get("resume");
        const profilePhoto = formData.get("profilePhoto");

        // create new form data object
        const newFormData = new FormData();

        newFormData.set(
            "profile",
            new Blob([JSON.stringify(parsed.data)], {
                type: "application/json",
            }),
        );
        if (resume) {
            newFormData.set("resume", resume);
        }
        if (profilePhoto) {
            newFormData.set("profilePhoto", profilePhoto);
        }

        const response = await patch<null>("/candidate/profile", {
            body: newFormData,
            authenticatedRequest: true,
            jsonRequest: false,
        });

        if (response.success) {
            revalidateTag("candidate-profile");

            return response;
        }

        throw new Error(response.message);
    } catch (e) {
        return handleActionLevelError(e);
    }
}

function __getEducationsFromFormData(formData: FormData) {
    const ids = formData.getAll("education.id");
    const titles = formData.getAll("education.title");
    const descriptions = formData.getAll("education.description");

    const educations = [];

    for (
        let i = 0;
        i < Math.max(ids.length, titles.length, descriptions.length);
        i++
    ) {
        educations.push({
            id: ids.at(i),
            title: titles.at(i),
            description: descriptions.at(i),
        });
    }

    return educations;
}

function __getInterestsFromFormData(formData: FormData) {
    const ids = formData.getAll("interest.id");
    const titles = formData.getAll("interest.title");

    const skills = [];

    for (let i = 0; i < Math.max(ids.length, titles.length); i++) {
        skills.push({
            id: ids.at(i),
            title: titles.at(i),
        });
    }

    return skills;
}

function __getSkillsFromFormData(formData: FormData) {
    const ids = formData.getAll("skill.id");
    const names = formData.getAll("skill.name");
    const yearsOfExperiences = formData.getAll("skill.yearsOfExperience");
    const experienceLevels = formData.getAll("skill.experienceLevel");
    const skills = [];

    for (
        let i = 0;
        i < Math.max(ids.length, names.length, experienceLevels.length);
        i++
    ) {
        skills.push({
            id: ids.at(i),
            name: names.at(i),
            yearsOfExperience: yearsOfExperiences.at(i),
            experienceLevel: experienceLevels.at(i),
        });
    }

    return skills;
}
