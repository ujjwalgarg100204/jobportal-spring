"use client";

import { Button } from "@nextui-org/button";
import React, { ReactNode } from "react";
import { FaAngleLeft } from "react-icons/fa";
import { useRouter } from "next/navigation";

export type Props = {
    startIcon?: ReactNode;
    endIcon?: ReactNode;
    className?: string;
    children: ReactNode;
};
export default function BackButton(props: Props) {
    const startIcon = props.startIcon ?? <FaAngleLeft />;
    const router = useRouter();

    return (
        <Button
            startContent={startIcon}
            {...props}
            variant="light"
            onClick={() => router.back()}
        >
            {props.children}
        </Button>
    );
}
