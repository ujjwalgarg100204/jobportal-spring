"use client";

import { Button } from "@nextui-org/button";
import { Input } from "@nextui-org/input";
import { useFormState, useFormStatus } from "react-dom";

import { loginAction } from "@/action/auth";
import FormError from "@/component/form-error";
import useNavigationWithToast from "@/hook/useNavigationWithToast";

export default function LoginForm() {
    const [state, formAction] = useFormState(loginAction, null);
    const { pending } = useFormStatus();

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
            <Button
                fullWidth
                className="bg-primary font-semibold"
                isLoading={pending}
                type="submit"
            >
                Login
            </Button>
        </form>
    );
}
