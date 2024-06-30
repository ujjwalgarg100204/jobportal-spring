import { Button } from "@nextui-org/button";
import { Input } from "@nextui-org/input";
import {
    Navbar,
    NavbarBrand,
    NavbarContent,
    NavbarItem,
} from "@nextui-org/navbar";
import { FaSearch } from "react-icons/fa";
import { MdLocationPin } from "react-icons/md";

import Logo from "@/component/logo";
import NextLink from "@/lib/next-ui/link";
import { getServerSession } from "@/service/auth";

export default async function IndexPage() {
    const session = await getServerSession();

    return (
        <main className="min-h-screen w-screen">
            <Navbar>
                <NavbarBrand>
                    <Logo />
                </NavbarBrand>
                <NavbarContent justify="end">
                    {session ? (
                        <NavbarItem>
                            <Button
                                as={NextLink}
                                className="px-3 min-w-16 h-8 text-tiny rounded-small md:px-4 md:min-w-20 md:h-10 md:text-small md:rounded-medium"
                                color="primary"
                                href="/dashboard"
                            >
                                Dashboard
                            </Button>
                        </NavbarItem>
                    ) : (
                        <>
                            <NavbarItem>
                                <Button
                                    as={NextLink}
                                    className="px-3 min-w-16 h-8 text-tiny rounded-small md:px-4 md:min-w-20 md:h-10 md:text-small md:rounded-medium"
                                    color="primary"
                                    href="/auth/login"
                                >
                                    Login
                                </Button>
                            </NavbarItem>
                            <NavbarItem>
                                <Button
                                    as={NextLink}
                                    className="px-3 min-w-16 h-8 text-tiny rounded-small md:px-4 md:min-w-20 md:h-10 md:text-small md:rounded-medium"
                                    color="primary"
                                    href="/auth/register"
                                    variant="flat"
                                >
                                    Register
                                </Button>
                            </NavbarItem>
                        </>
                    )}
                </NavbarContent>
            </Navbar>

            <section className="text-center p-4 mt-40 space-y-6 max-w-3xl mx-auto md:p-0 md:space-y-12 lg:space-y-16">
                <h1 className="font-bold text-3xl md:text-4xl lg:text-5xl">
                    Find Your Dream Job <br className="my-1" />
                    in your Dream Field
                </h1>
                <form className="grid grid-cols-2 gap-x-2 gap-y-6 md:gap-x-4">
                    <Input
                        label="Job Title"
                        labelPlacement="inside"
                        placeholder="Ex: Full-Stack Web Developer"
                        startContent={
                            <FaSearch className="text-base text-default-400 pointer-events-none flex-shrink-0" />
                        }
                        type="text"
                    />
                    <Input
                        label="Job Title"
                        labelPlacement="inside"
                        placeholder="Ex: Full-Stack Web Developer"
                        startContent={
                            <MdLocationPin className="text-base text-default-400 pointer-events-none flex-shrink-0" />
                        }
                        type="text"
                    />
                    <Button
                        className="col-span-2"
                        color="primary"
                        type="submit"
                    >
                        Search
                    </Button>
                </form>
            </section>
        </main>
    );
}
