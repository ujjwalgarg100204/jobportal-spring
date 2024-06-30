import RegisterForm from "./register-form";

import NextLink from "@/lib/next-ui/link";

export default function RegisterPage() {
    return (
        <>
            <title>Register | Jobportal</title>
            <section className="flex h-full flex-col items-center justify-center md:my-auto md:flex-1 md:px-8 xl:px-16">
                <h1 className="my-10 text-2xl font-semibold md:text-3xl 2xl:text-4xl">
                    Register
                </h1>
                <RegisterForm />
                <p className="text-center mt-4">
                    Already a user?{" "}
                    <NextLink href="/auth/register" underline="always">
                        Login
                    </NextLink>
                </p>
            </section>
        </>
    );
}
