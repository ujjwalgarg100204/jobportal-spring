"use client";

import { Button, ButtonProps } from "@nextui-org/button";
import React from "react";
import { FaAngleLeft } from "react-icons/fa";
import { useRouter } from "next/navigation";

export default function BackButton(props: Omit<ButtonProps, "onClick">) {
    const router = useRouter();

    return (
        <Button
            startContent={<FaAngleLeft />}
            variant="light"
            {...props}
            onClick={() => router.back()}
        >
            {props.children}
        </Button>
    );
}
