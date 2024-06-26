"use client";

import { Button } from "@nextui-org/button";
import { Input } from "@nextui-org/input";
import { useFormState, useFormStatus } from "react-dom";

import { registerNewRecruiterAction } from "@/action/auth";
import FormError from "@/component/form-error";
import useNavigationWithToast from "@/hook/useNavigationWithToast";

export default function RecruiterRegistrationForm() {
    const [state, formAction] = useFormState(registerNewRecruiterAction, null);
    const { pending } = useFormStatus();

    useNavigationWithToast(state, "/auth/login");

    return (
        <form action={formAction} className="space-y-6">
            <FormError formState={state} />
            <fieldset className="grid gap-y-2">
                <legend className="mb-2">Credentials</legend>
                <Input
                    isRequired
                    required
                    label="Email"
                    name="email"
                    type="email"
                />
                <Input
                    isRequired
                    required
                    label="Password"
                    name="password"
                    type="password"
                />
            </fieldset>
            <fieldset className="grid gap-2 grid-cols-2">
                <legend className="mb-2">Your Name</legend>
                <Input
                    isRequired
                    required
                    label="First Name"
                    name="firstName"
                    type="text"
                />
                <Input
                    isRequired
                    required
                    label="Last Name"
                    name="lastName"
                    type="text"
                />
            </fieldset>
            <fieldset className="grid gap-2">
                <legend className="mb-2">Your Company Information</legend>
                <Input
                    isRequired
                    required
                    label="Company Name"
                    name="company.name"
                    type="tel"
                />
                <fieldset className="grid grid-cols-2 gap-2 lg:grid-cols-3">
                    <legend className="mb-2">Company&apos;s Address</legend>
                    <Input
                        className="col-span-2 lg:col-span-1"
                        label="City"
                        name="company.address.city"
                        type="text"
                    />
                    <Input
                        label="State"
                        name="company.address.state"
                        type="text"
                    />
                    <Input
                        label="Country"
                        name="company.address.country"
                        type="text"
                    />
                </fieldset>
            </fieldset>
            <Button
                fullWidth
                className="bg-primary font-semibold"
                isLoading={pending}
                type="submit"
            >
                Register
            </Button>
        </form>
    );
}
