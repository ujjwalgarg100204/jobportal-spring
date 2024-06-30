"use client";

import { Button } from "@nextui-org/button";
import { Input } from "@nextui-org/input";
import { Select, SelectItem } from "@nextui-org/select";
import DOMPurify from "isomorphic-dompurify";
import { useState } from "react";
import { useFormState } from "react-dom";
import Editor, { ContentEditableEvent } from "react-simple-wysiwyg";

import FormError from "../form-error";

import { EmploymentType, RemoteType } from "@/type/constants";
import { UpdateJobRequest } from "@/type/entity/job";
import { createNewJobAction, updateJobAction } from "@/action/job";
import useNavigationWithToast from "@/hook/useNavigationWithToast";

type Props = {
    defaultValues: UpdateJobRequest | null;
};
export default function JobCreateUpdateForm({ defaultValues }: Props) {
    const [createState, createFormAction] = useFormState(
        createNewJobAction,
        null,
    );
    const [updateState, updateFormAction] = useFormState(updateJobAction, null);
    const [description, setDescription] = useState(
        defaultValues?.description ?? "",
    );
    const state = defaultValues ? updateState : createState;
    const formAction = defaultValues ? updateFormAction : createFormAction;

    useNavigationWithToast(state, "/dashboard/r/job");

    function descriptionChangeHandler(ev: ContentEditableEvent) {
        const sanitized = DOMPurify.sanitize(ev.target.value);

        setDescription(sanitized);
    }

    return (
        <form action={formAction} className="space-y-6">
            <FormError formState={state} />
            {defaultValues?.id && (
                <input name="id" type="hidden" value={defaultValues.id} />
            )}
            <fieldset className="gap-4 grid grid-cols-2">
                <legend className="text-xl font-semibold">Job Details</legend>
                <Input
                    isRequired
                    required
                    className="col-span-2"
                    defaultValue={defaultValues?.title}
                    label="Title of Job"
                    name="title"
                    type="text"
                    variant="faded"
                />
                <Input
                    isRequired
                    required
                    className={defaultValues ? "col-span-1" : "col-span-2"}
                    defaultValue={`${defaultValues?.noOfVacancy ?? ""}`}
                    label="No of Vacancy"
                    name="noOfVacancy"
                    type="number"
                    variant="faded"
                />
                {defaultValues && (
                    <Select
                        isRequired
                        required
                        defaultSelectedKeys={[
                            defaultValues.hiringComplete ? "yes" : "no",
                        ]}
                        label="Is Hiring Complete"
                        name="hiringComplete"
                    >
                        <SelectItem key="no" value="no">
                            No
                        </SelectItem>
                        <SelectItem key="yes" value="yes">
                            Yes
                        </SelectItem>
                    </Select>
                )}
                <div className="grid md:grid-cols-3 gap-4 col-span-2">
                    <Select
                        isRequired
                        required
                        defaultSelectedKeys={[
                            defaultValues?.employmentType ?? "",
                        ]}
                        label="Employment Type"
                        name="employmentType"
                    >
                        <SelectItem
                            key={EmploymentType.FULL_TIME}
                            value={EmploymentType.FULL_TIME}
                        >
                            Full-time
                        </SelectItem>
                        <SelectItem
                            key={EmploymentType.PART_TIME}
                            value={EmploymentType.PART_TIME}
                        >
                            Part-time
                        </SelectItem>
                        <SelectItem
                            key={EmploymentType.FREELANCE}
                            value={EmploymentType.FREELANCE}
                        >
                            Freelance
                        </SelectItem>
                        <SelectItem
                            key={EmploymentType.INTERNSHIP}
                            value={EmploymentType.INTERNSHIP}
                        >
                            Internship
                        </SelectItem>
                    </Select>
                    <Select
                        isRequired
                        required
                        defaultSelectedKeys={[defaultValues?.remoteType ?? ""]}
                        label="Remote"
                        name="remoteType"
                    >
                        <SelectItem
                            key={RemoteType.REMOTE_ONLY}
                            value={RemoteType.REMOTE_ONLY}
                        >
                            Remote-Only
                        </SelectItem>
                        <SelectItem
                            key={RemoteType.OFFICE_ONLY}
                            value={RemoteType.OFFICE_ONLY}
                        >
                            Office-Only
                        </SelectItem>
                        <SelectItem
                            key={RemoteType.PARTIAl_REMOTE}
                            value={RemoteType.PARTIAl_REMOTE}
                        >
                            Partial-Remote
                        </SelectItem>
                    </Select>
                    <Input
                        label="Salary"
                        name="salary"
                        type="text"
                        variant="faded"
                    />
                </div>

                <div className="col-span-2">
                    <h3 className="text-lg font-semibold">Description</h3>
                    <Editor
                        value={description}
                        onChange={descriptionChangeHandler}
                    />
                    <input
                        name="description"
                        type="hidden"
                        value={description}
                    />
                </div>
            </fieldset>

            <fieldset className="space-y-4">
                <legend className="text-xl font-semibold">Location</legend>
                {defaultValues?.address?.id && (
                    <input
                        name="address.id"
                        type="hidden"
                        value={defaultValues.address.id}
                    />
                )}
                <div className="grid md:grid-cols-3 gap-4">
                    <Input
                        defaultValue={defaultValues?.address?.city}
                        label="City"
                        name="address.city"
                        type="text"
                        variant="faded"
                    />
                    <Input
                        isRequired
                        required
                        defaultValue={defaultValues?.address?.state}
                        label="State"
                        name="address.state"
                        type="text"
                        variant="faded"
                    />
                    <Input
                        isRequired
                        required
                        defaultValue={defaultValues?.address?.country}
                        label="Country"
                        name="address.country"
                        type="text"
                        variant="faded"
                    />
                </div>
            </fieldset>
            <Button fullWidth color="primary" type="submit">
                {defaultValues ? "Update" : "Create"}
            </Button>
        </form>
    );
}
