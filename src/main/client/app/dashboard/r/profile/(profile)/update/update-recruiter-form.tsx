"use client";

import { Button } from "@nextui-org/button";
import { Input } from "@nextui-org/input";
import React, { ChangeEvent, useState } from "react";
import { useFormState } from "react-dom";
import { Avatar } from "@nextui-org/avatar";
import { Select, SelectItem } from "@nextui-org/select";
import { FaBuilding, FaUser } from "react-icons/fa";
import { usePathname } from "next/navigation";

import { updateRecruiterProfileAction } from "@/action/user-profile";
import FormError from "@/component/form-error";
import useNavigationWithToast from "@/hook/useNavigationWithToast";
import { UpdateRecruiterProfileRequest } from "@/type/entity/recruiter-profile";
import { GetAllCompanyResponse } from "@/type/entity/company";
import FormSubmitButton from "@/component/form-submit-button";
import NextLink from "@/lib/next-ui/link";

type Props = {
    defaultValue: UpdateRecruiterProfileRequest & { profilePhotoUrl?: string };
    companies: GetAllCompanyResponse;
};

export default function UpdateRecruiterForm({
    defaultValue,
    companies,
}: Props) {
    const [state, formAction] = useFormState(
        updateRecruiterProfileAction,
        null,
    );
    const [pluralAttributesCounter, setPluralAttributesCounter] = useState<{
        education: number;
        interest: number;
    }>(() => {
        const val = { education: 0, interest: 0 };

        if (defaultValue) {
            if (defaultValue.educations?.length) {
                val.education = defaultValue.educations.length;
            }
            if (defaultValue.interests?.length) {
                val.interest = defaultValue.interests.length;
            }
        }

        return val;
    });
    const [logo, setLogo] = useState<File | null>(null);
    const path = usePathname();

    useNavigationWithToast(state, "/dashboard/r/profile");

    function incrementCounter(key: keyof typeof pluralAttributesCounter) {
        setPluralAttributesCounter(prev => ({
            ...prev,
            [key]: prev[key] + 1,
        }));
    }

    function logoFileChangeHandler(ev: ChangeEvent<HTMLInputElement>) {
        if (ev.target.files) {
            setLogo(ev.target.files[0]);
        }
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
                    defaultValue={defaultValue?.about}
                    label="About"
                    name="about"
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
                    defaultValue={defaultValue?.contactInformation?.phone}
                    label="Phone"
                    name="contactInformation.phone"
                    type="tel"
                    variant="faded"
                />
                <Input
                    defaultValue={
                        defaultValue?.contactInformation?.twitterHandle
                    }
                    label="Twitter"
                    name="contactInformation.twitterHandle"
                    type="url"
                    variant="faded"
                />
                <Input
                    defaultValue={
                        defaultValue?.contactInformation?.linkedinHandle
                    }
                    label="LinkedIn"
                    name="contactInformation.linkedinHandle"
                    type="url"
                    variant="faded"
                />
                <Input
                    defaultValue={
                        defaultValue?.contactInformation?.githubHandle
                    }
                    label="GitHub"
                    name="contactInformation.githubHandle"
                    type="url"
                    variant="faded"
                />
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
            <fieldset className="w-full space-y-3">
                <legend className="my-2 text-lg">Company</legend>
                <Select
                    fullWidth
                    classNames={{
                        label: "group-data-[filled=true]:-translate-y-5",
                        trigger: "min-h-16",
                        listboxWrapper: "max-h-[400px]",
                    }}
                    defaultSelectedKeys={[defaultValue.company.id!]}
                    items={companies}
                    label="Your Company"
                    listboxProps={{
                        itemClasses: {
                            base: [
                                "rounded-md",
                                "text-default-500",
                                "transition-opacity",
                                "data-[hover=true]:text-foreground",
                                "data-[hover=true]:bg-default-100",
                                "dark:data-[hover=true]:bg-default-50",
                                "data-[selectable=true]:focus:bg-default-50",
                                "data-[pressed=true]:opacity-70",
                                "data-[focus-visible=true]:ring-default-500",
                            ],
                        },
                    }}
                    name="company.id"
                    popoverProps={{
                        classNames: {
                            base: "before:bg-default-200",
                            content:
                                "p-0 border-small border-divider bg-background",
                        },
                    }}
                    renderValue={items => {
                        return items.map(item => (
                            <div
                                key={item.key}
                                className="flex items-center gap-2"
                            >
                                <Avatar
                                    showFallback
                                    alt={item.data?.name}
                                    className="flex-shrink-0"
                                    fallback={<FaBuilding />}
                                    size="sm"
                                    src={
                                        item.data?.hasLogo
                                            ? item.data.logoUrl!
                                            : undefined
                                    }
                                />
                                <div className="flex flex-col">
                                    <span>{item.data?.name}</span>
                                    <span className="text-default-500 text-tiny">
                                        ({item.data?.address?.state},{" "}
                                        {item.data?.address?.country})
                                    </span>
                                </div>
                            </div>
                        ));
                    }}
                    variant="bordered"
                >
                    {company => (
                        <SelectItem
                            key={company.id}
                            textValue={company.name}
                            value={company.id}
                        >
                            <div className="flex gap-2 items-center">
                                <Avatar
                                    showFallback
                                    alt={company.name}
                                    className="flex-shrink-0"
                                    fallback={<FaBuilding />}
                                    size="sm"
                                    src={
                                        company.hasLogo
                                            ? company.logoUrl
                                            : undefined
                                    }
                                />
                                <div className="flex flex-col">
                                    <span className="text-small">
                                        {company.name}
                                    </span>
                                    <span className="text-tiny text-default-400">
                                        {company.address?.state},{" "}
                                        {company.address?.country}
                                    </span>
                                </div>
                            </div>
                        </SelectItem>
                    )}
                </Select>
                <div className="flex gap-4 w-full items-center">
                    <hr className="h-1 w-full" />
                    <span className="text-tiny">Or</span>
                    <hr className="h-1 w-full" />
                </div>
                <p>
                    Create new company{" "}
                    <NextLink
                        showAnchorIcon
                        href={`/dashboard/r/profile/company/create?redirect=${path}`}
                        underline="always"
                    >
                        Here
                    </NextLink>
                </p>
            </fieldset>
            <fieldset className="grid gap-4">
                <legend className="my-2 text-lg">Files</legend>
                <div className="grid gap-4 mx-auto">
                    <Avatar
                        showFallback
                        className="w-20 h-20 mx-auto"
                        fallback={<FaUser className="w-10 h-10" />}
                        name={defaultValue.firstName}
                        src={
                            logo
                                ? URL.createObjectURL(logo)
                                : defaultValue.profilePhotoUrl
                        }
                    />
                    <Input
                        fullWidth
                        accept="image/png, image/jpeg, image/jpg"
                        label="Profile Photo"
                        name="profilePhoto"
                        placeholder="file"
                        type="file"
                        variant="faded"
                        onChange={logoFileChangeHandler}
                    />
                </div>
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
