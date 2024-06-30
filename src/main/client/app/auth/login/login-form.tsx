"use client";

import { Input } from "@nextui-org/input";
import { useFormState } from "react-dom";

import { loginAction } from "@/action/auth";
import FormError from "@/component/form-error";
import useNavigationWithToast from "@/hook/useNavigationWithToast";
import FormSubmitButton from "@/component/form-submit-button";

export default function LoginForm() {
    const [state, formAction] = useFormState(loginAction, null);

    useNavigationWithToast(state, "/dashboard");

    return (
        <form action={formAction} className="w-full space-y-10">
            <FormError formState={state} />
            <div className="space-y-2">
                <Input
                    required
                    label="Email"
                    name="email"
                    size="lg"
                    type="email"
                />
                <Input
                    required
                    label="Password"
                    name="password"
                    size="lg"
                    type="password"
                />
            </div>
            <FormSubmitButton fullWidth className="bg-primary font-semibold">
                Login
            </FormSubmitButton>
        </form>
    );
}
