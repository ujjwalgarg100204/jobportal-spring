import { Link } from "@nextui-org/link";
import NextLink from "next/link";
import { RedirectType, redirect } from "next/navigation";

import LoginForm from "./login-form";

import { getServerSession } from "@/service/auth";
export default async function LoginPage() {
    const session = await getServerSession();

    if (session) {
        redirect("/dashboard", RedirectType.replace);
    }

    return (
        <>
            <title>Login | Jobportal</title>
            <section className="flex h-full flex-col items-center justify-center md:my-auto md:flex-1 md:px-8 xl:px-16">
                <h1 className="my-10 text-2xl font-semibold md:text-3xl 2xl:text-4xl">
                    Login
                </h1>
                <LoginForm />
                <p className="text-center mt-4">
                    Are you a new user?{" "}
                    <Link
                        as={NextLink}
                        href="/auth/register"
                        underline="always"
                    >
                        Register Now
                    </Link>
                </p>
            </section>
        </>
    );
}
