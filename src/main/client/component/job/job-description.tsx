"use client";

import { useEffect, useRef } from "react";

type Props = {
    description: string;
};
export default function Description({ description }: Props) {
    const ref = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (ref.current) {
            ref.current.innerHTML = description;
        }
    }, [ref]);

    return <div ref={ref} className="ml-0" />;
}
