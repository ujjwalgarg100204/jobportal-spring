import { ReactNode } from "react";

type Props = { children: ReactNode };

export default function ProfileLayoutPage({ children }: Props) {
    return (
        <main className="flex flex-col items-center mt-8 gap-6 mx-auto max-w-xl">
            <h1 className="text-4xl font-bold md:text-5xl">Your Profile</h1>
            {children}
        </main>
    );
}
