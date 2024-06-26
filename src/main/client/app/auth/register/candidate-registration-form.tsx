"use client";

import { Button } from "@nextui-org/button";
import { Input } from "@nextui-org/input";
import { useFormState, useFormStatus } from "react-dom";

import { registerNewCandidateAction } from "@/action/auth";
import FormError from "@/component/form-error";
import useNavigationWithToast from "@/hook/useNavigationWithToast";

export default function CandidateRegistrationForm() {
    const [state, formAction] = useFormState(registerNewCandidateAction, null);
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
                <Input
                    isRequired
                    required
                    className="col-span-2"
                    label="Short Description about yourself"
                    name="shortAbout"
                    type="text"
                />
            </fieldset>
            <fieldset className="grid grid-cols-2 gap-2">
                <legend className="mb-2">Contact Information</legend>
                <Input
                    isRequired
                    required
                    label="Phone"
                    name="contactInformation.phone"
                    type="tel"
                />
                <Input
                    label="Twitter"
                    name="contactInformation.twitterHandle"
                    type="url"
                />
                <Input
                    label="LinkedIn"
                    name="contactInformation.linkedinHandle"
                    type="url"
                />
                <Input
                    label="GitHub"
                    name="contactInformation.githubHandle"
                    type="url"
                />
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
