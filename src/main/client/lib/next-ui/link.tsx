"use client";

import NextJSLink from "next/link";
import { Link, LinkProps } from "@nextui-org/link";

export default function NextLink({
    children,
    ...otherProps
}: Omit<LinkProps, "as">) {
    return (
        <Link {...otherProps} as={NextJSLink}>
            {children}
        </Link>
    );
}
