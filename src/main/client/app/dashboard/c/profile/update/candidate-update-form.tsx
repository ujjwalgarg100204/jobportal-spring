"use client";

import React from "react";
import { Button } from "@nextui-org/button";
import { Input } from "@nextui-org/input";
import { Select, SelectItem } from "@nextui-org/select";
import { useState } from "react";
import { useFormState } from "react-dom";

import { updateCandidateProfileAction } from "@/action/user-profile";
import FormError from "@/component/form-error";
import {
    EmploymentType,
    ExperienceLevel,
    WorkAuthorization,
} from "@/type/constants";
import { UpdateCandidateProfileRequest } from "@/type/entity/candidate-profile";
import useNavigationWithToast from "@/hook/useNavigationWithToast";
import FormSubmitButton from "@/component/form-submit-button";

type Props = { defaultValue: UpdateCandidateProfileRequest };

export default function CandidateUpdateForm({ defaultValue }: Props) {
    const [state, formAction] = useFormState(
        updateCandidateProfileAction,
        null,
    );
    const [pluralAttributesCounter, setPluralAttributesCounter] = useState<{
        education: number;
        interest: number;
        skill: number;
    }>(() => {
        const val = { education: 1, interest: 1, skill: 1 };

        if (defaultValue) {
            if (defaultValue.educations?.length) {
                val.education = defaultValue.educations.length;
            }
            if (defaultValue.interests?.length) {
                val.interest = defaultValue.interests.length;
            }
            if (defaultValue.skills?.length) {
                val.skill = defaultValue.skills.length;
            }
        }

        return val;
    });

    useNavigationWithToast(state, "/dashboard/c");

    function incrementCounter(key: keyof typeof pluralAttributesCounter) {
        setPluralAttributesCounter(prev => ({
            ...prev,
            [key]: prev[key] + 1,
        }));
    }

    return (
        <form action={formAction} className="w-full space-y-4">
            <FormError formState={state} />
            <fieldset className="grid md:grid-cols-2 gap-4">
                <legend className="my-2 text-lg">Personal Details</legend>
                <Input
                    isRequired
                    required
                    defaultValue={defaultValue?.firstName}
                    label="First Name"
                    name="firstName"
                    type="text"
                    variant="faded"
                />
                <Input
                    isRequired
                    required
                    defaultValue={defaultValue?.lastName}
                    label="Last Name"
                    name="lastName"
                    type="text"
                    variant="faded"
                />
                <Input
                    isRequired
                    required
                    className="md:col-span-2"
                    defaultValue={defaultValue?.shortAbout}
                    label="Short About"
                    name="shortAbout"
                    type="text"
                    variant="faded"
                />
            </fieldset>
            <div className="grid md:grid-cols-3 gap-4">
                {defaultValue.address?.id && (
                    <input
                        name="address.id"
                        type="hidden"
                        value={defaultValue.address.id}
                    />
                )}
                <Input
                    defaultValue={defaultValue?.address?.city}
                    label="City"
                    name="address.city"
                    type="text"
                    variant="faded"
                />
                <Input
                    defaultValue={defaultValue?.address?.state}
                    label="State"
                    name="address.state"
                    type="text"
                    variant="faded"
                />
                <Input
                    defaultValue={defaultValue?.address?.country}
                    label="Country"
                    name="address.country"
                    type="text"
                    variant="faded"
                />
            </div>
            <fieldset className="grid grid-cols-2 gap-2">
                <legend className="mb-2">Contact Information</legend>
                {defaultValue.contactInformation?.id && (
                    <input
                        name="contactInformation.id"
                        type="hidden"
                        value={defaultValue.contactInformation.id}
                    />
                )}
                <Input
                    isRequired
                    required
                    defaultValue={defaultValue?.contactInformation.phone}
                    label="Phone"
                    name="contactInformation.phone"
                    type="tel"
                    variant="faded"
                />
                <Input
                    defaultValue={
                        defaultValue?.contactInformation.twitterHandle
                    }
                    label="Twitter"
                    name="contactInformation.twitterHandle"
                    type="url"
                    variant="faded"
                />
                <Input
                    defaultValue={
                        defaultValue?.contactInformation.linkedinHandle
                    }
                    label="LinkedIn"
                    name="contactInformation.linkedinHandle"
                    type="url"
                    variant="faded"
                />
                <Input
                    defaultValue={defaultValue?.contactInformation.githubHandle}
                    label="GitHub"
                    name="contactInformation.githubHandle"
                    type="url"
                    variant="faded"
                />
            </fieldset>
            <fieldset className="grid md:grid-cols-2 gap-4">
                <legend className="my-2 text-lg">Work Classification</legend>
                <Select
                    defaultSelectedKeys={[
                        defaultValue?.workAuthorization ?? "",
                    ]}
                    label="Work Authorization"
                    name="workAuthorization"
                >
                    <SelectItem
                        key="US Citizen"
                        value={WorkAuthorization.US_CITIZEN}
                    >
                        US Citizen
                    </SelectItem>
                    <SelectItem
                        key="Canadian Citizen"
                        value={WorkAuthorization.CANADIAN_CITIZEN}
                    >
                        Canadian Citizen
                    </SelectItem>
                    <SelectItem
                        key="Green Card"
                        value={WorkAuthorization.GREEN_CARD}
                    >
                        Green Card
                    </SelectItem>
                    <SelectItem key="H1 Visa" value={WorkAuthorization.H1_VISA}>
                        H1 Visa
                    </SelectItem>
                    <SelectItem
                        key="TN Permit"
                        value={WorkAuthorization.TN_PERMIT}
                    >
                        TN Permit
                    </SelectItem>
                </Select>
                <Select
                    isRequired
                    required
                    defaultSelectedKeys={[
                        defaultValue?.preferredEmploymentType ?? "",
                    ]}
                    label="Preferred Employment Type"
                    name="preferredEmploymentType"
                >
                    <SelectItem
                        key={EmploymentType.FULL_TIME}
                        value={EmploymentType.FULL_TIME}
                    >
                        Full-Time
                    </SelectItem>
                    <SelectItem
                        key={EmploymentType.FREELANCE}
                        value={EmploymentType.PART_TIME}
                    >
                        Part-Time
                    </SelectItem>
                    <SelectItem
                        key={EmploymentType.FREELANCE}
                        value={EmploymentType.FREELANCE}
                    >
                        Freelance
                    </SelectItem>
                </Select>
            </fieldset>
            <fieldset className="flex flex-col gap-4">
                <legend className="my-2 text-lg">Education</legend>
                {Array(pluralAttributesCounter.education)
                    .fill("")
                    .map((_, index) => (
                        <div key={index} className="grid md:grid-cols-2 gap-4">
                            {defaultValue.educations?.at(index)?.id && (
                                <input
                                    name="education.id"
                                    type="hidden"
                                    value={
                                        defaultValue.educations.at(index)?.id
                                    }
                                />
                            )}
                            <Input
                                defaultValue={
                                    defaultValue.educations?.at(index)?.title
                                }
                                label="Title"
                                name="education.title"
                                type="text"
                                variant="faded"
                            />
                            <Input
                                defaultValue={
                                    defaultValue.educations?.at(index)
                                        ?.description
                                }
                                label="Description"
                                name="education.description"
                                type="text"
                                variant="faded"
                            />
                        </div>
                    ))}
                <div>
                    <Button onClick={incrementCounter.bind(null, "education")}>
                        Add Education
                    </Button>
                </div>
            </fieldset>
            <fieldset className="flex flex-col gap-4">
                <legend className="my-2 text-lg">Interest</legend>
                <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
                    {Array(pluralAttributesCounter.interest)
                        .fill("")
                        .map((_, index) => (
                            <React.Fragment key={index}>
                                {defaultValue.interests?.at(index)?.id && (
                                    <input
                                        name="interest.id"
                                        type="hidden"
                                        value={
                                            defaultValue.interests.at(index)?.id
                                        }
                                    />
                                )}
                                <Input
                                    defaultValue={
                                        defaultValue.interests?.at(index)?.title
                                    }
                                    label="Title"
                                    name="interest.title"
                                    type="text"
                                    variant="faded"
                                />
                            </React.Fragment>
                        ))}
                </div>
                <div>
                    <Button onClick={incrementCounter.bind(null, "interest")}>
                        Add Interest
                    </Button>
                </div>
            </fieldset>
            <fieldset className="flex flex-col gap-4">
                <legend className="my-2 text-lg">Skills</legend>
                {Array(pluralAttributesCounter.skill)
                    .fill("")
                    .map((_, index) => (
                        <div
                            key={index}
                            className="grid grid-cols-2 md:grid-cols-3 gap-4"
                        >
                            {defaultValue.skills?.at(index)?.id && (
                                <input
                                    name="skill.id"
                                    type="hidden"
                                    value={defaultValue.skills.at(index)?.id}
                                />
                            )}
                            <Input
                                defaultValue={
                                    defaultValue.skills?.at(index)?.name
                                }
                                label="Skill Name"
                                name="skill.name"
                                type="text"
                                variant="faded"
                            />
                            <Input
                                defaultValue={
                                    defaultValue.skills?.at(index)
                                        ?.yearsOfExperience
                                }
                                label="Years of Experience"
                                name="skill.yearsOfExperience"
                                type="text"
                                variant="faded"
                            />
                            <Select
                                className="col-span-2 md:col-span-1"
                                defaultSelectedKeys={[
                                    defaultValue.skills?.at(index)
                                        ?.experienceLevel ?? "",
                                ]}
                                label="Experience Level"
                                name="skill.experienceLevel"
                            >
                                <SelectItem
                                    key={ExperienceLevel.BEGINNER}
                                    value={ExperienceLevel.BEGINNER}
                                >
                                    Beginner
                                </SelectItem>
                                <SelectItem
                                    key={ExperienceLevel.INTERMEDIATE}
                                    value={ExperienceLevel.INTERMEDIATE}
                                >
                                    Intermediate
                                </SelectItem>
                                <SelectItem
                                    key={ExperienceLevel.ADVANCE}
                                    value={ExperienceLevel.ADVANCE}
                                >
                                    Advance
                                </SelectItem>
                            </Select>
                        </div>
                    ))}
                <div>
                    <Button onClick={incrementCounter.bind(null, "skill")}>
                        Add Skill
                    </Button>
                </div>
            </fieldset>
            <fieldset className="grid md:grid-cols-2 gap-4">
                <legend className="my-2 text-lg">Files</legend>
                <Input
                    fullWidth
                    accept="image/png, image/jpeg"
                    label="Profile Photo"
                    name="profilePhoto"
                    placeholder="file"
                    type="file"
                    variant="faded"
                />
                <Input
                    fullWidth
                    accept="application/pdf"
                    label="Resume"
                    name="resume"
                    placeholder="file"
                    type="file"
                    variant="faded"
                />
            </fieldset>
            <FormSubmitButton
                fullWidth
                color="primary"
                size="lg"
                variant="solid"
            >
                Update
            </FormSubmitButton>
        </form>
    );
}
