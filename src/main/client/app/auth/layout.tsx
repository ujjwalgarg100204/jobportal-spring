import { ReactNode } from "react";

import Logo from "@/component/logo";

type Props = { children: ReactNode };

export default function AuthLayout({ children }: Readonly<Props>) {
    return (
        <main className="flex min-h-screen flex-col p-4 md:flex-row md:p-0">
            <header className="mt-4 rounded-lg bg-black px-2 py-4 md:hidden">
                <Logo />
            </header>
            <aside className="relative hidden h-auto w-1/2 border-r-4 border-r-gray-400 bg-coffee-mug bg-cover bg-center md:block md:px-8 md:py-12">
                <Logo className="w-min rounded-lg bg-[rgba(0,0,0,0.4)] p-4 xl:text-2xl" />
            </aside>
            {children}
        </main>
    );
}
