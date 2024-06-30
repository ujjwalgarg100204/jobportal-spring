"use client";

import { Button, ButtonProps } from "@nextui-org/button";
import { useFormStatus } from "react-dom";

export default function FormSubmitButton({
    children,
    ...otherProps
}: Omit<ButtonProps, "isLoading" | "type">) {
    const { pending } = useFormStatus();

    return (
        <Button {...otherProps} isLoading={pending} type="submit">
            {children}
        </Button>
    );
}
