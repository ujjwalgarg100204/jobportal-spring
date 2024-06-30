"use client";

import { Button } from "@nextui-org/button";
import { useEffect } from "react";

type Props = {
    error: Error;
    reset: () => void;
};
export default function Error({ error, reset }: Props) {
    useEffect(() => {
        // Log the error to an error reporting service
        /* eslint-disable no-console */
        console.error(error);
    }, [error]);

    return (
        <div className="h-screen w-full flex flex-col justify-center items-center gap-8">
            <h2 className="text-4xl font-bold">Something went wrong!</h2>
            <Button color="danger" size="lg" onClick={reset}>
                Try again
            </Button>
        </div>
    );
}
