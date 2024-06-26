import NextLink from "next/link";
import { Link } from "@nextui-org/link";
import { twMerge } from "tailwind-merge";

export type Props = {
    className?: string;
};

export default function Logo({ className }: Props) {
    return (
        <Link
            as={NextLink}
            className={twMerge(
                "gap-1 font-semibold text-white md:gap-2 text-base md:text-lg lg:text-xl xl:text-2xl",
                className,
            )}
            color="primary"
            href="/"
        >
            <span className="font-title">hotdevjobs</span>
            <span
                className="rounded-lg bg-primary p-1 text-white md:p-1.5"
                // style={{ borderRadius: ".5rem" }}
            >
                .com
            </span>
        </Link>
    );
}
