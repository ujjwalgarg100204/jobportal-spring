import { twMerge } from "tailwind-merge";

import NextLink from "@/lib/next-ui/link";

export type Props = {
    className?: string;
    href?: string;
};

export default function Logo({ className, href }: Props) {
    return (
        <NextLink
            className={twMerge(
                "gap-1 font-semibold text-white md:gap-2 text-base md:text-lg lg:text-xl xl:text-2xl",
                className,
            )}
            color="primary"
            href={href ?? "/"}
        >
            <span className="font-title">hotdevjobs</span>
            <span
                className="rounded-lg bg-primary p-1 text-white md:p-1.5"
                // style={{ borderRadius: ".5rem" }}
            >
                .com
            </span>
        </NextLink>
    );
}
