"use client";

import { Avatar } from "@nextui-org/avatar";
import { Input } from "@nextui-org/input";
import { ChangeEvent, useState } from "react";
import { useFormState } from "react-dom";
import { FaBuilding, FaPencilAlt } from "react-icons/fa";

import { updateCompanyAction } from "@/action/company";
import FormError from "@/component/form-error";
import useNavigationWithToast from "@/hook/useNavigationWithToast";
import { UpdateCompanyRequest } from "@/type/entity/company";
import FormSubmitButton from "@/component/form-submit-button";

type Props = {
    defaultValue: (UpdateCompanyRequest & { logoUrl?: string }) | null;
};
export default function CompanyCreateUpdateForm({ defaultValue }: Props) {
    const [updateState, updateFormAction] = useFormState(
        updateCompanyAction,
        null,
    );
    // TODO: add create company action
    const [createState, createFormAction] = useFormState(
        createCompanyAction,
        null,
    );
    const [logo, setLogo] = useState<File | null>(null);

    useNavigationWithToast(updateState, "/dashboard/r/profile/company");

    function logoFileChangeHandler(ev: ChangeEvent<HTMLInputElement>) {
        if (ev.target.files) {
            setLogo(ev.target.files[0]);
        }
    }

    const formAction = defaultValue ? updateFormAction : createFormAction;
    const state = defaultValue ? updateState : createState;

    return (
        <form action={formAction} className="size-full space-y-4">
            {defaultValue && (
                <input name="id" type="hidden" value={defaultValue.id} />
            )}
            <FormError formState={state} />
            <div className="grid gap-4">
                <Avatar
                    showFallback
                    className="w-20 h-20 mx-auto"
                    fallback={<FaBuilding className="w-10 h-10" />}
                    name={defaultValue?.name}
                    src={
                        logo ? URL.createObjectURL(logo) : defaultValue?.logoUrl
                    }
                />
                <Input
                    fullWidth
                    accept="image/png, image/jpeg, image/jpg"
                    label="Company Logo"
                    name="logo"
                    placeholder="file"
                    type="file"
                    onChange={logoFileChangeHandler}
                />
            </div>
            <Input
                className="col-span-2 lg:col-span-1"
                defaultValue={defaultValue?.name}
                label="Company Name"
                name="name"
                size="lg"
                type="text"
            />
            <fieldset className="grid grid-cols-3 gap-2">
                <legend className="mb-2">Company&apos;s Address</legend>
                {defaultValue?.address?.id && (
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
                />
                <Input
                    defaultValue={defaultValue?.address?.state}
                    label="State"
                    name="address.state"
                    type="text"
                />
                <Input
                    defaultValue={defaultValue?.address?.country}
                    label="Country"
                    name="address.country"
                    type="text"
                />
            </fieldset>
            <FormSubmitButton
                fullWidth
                color="primary"
                startContent={<FaPencilAlt />}
            >
                Update
            </FormSubmitButton>
        </form>
    );
}
