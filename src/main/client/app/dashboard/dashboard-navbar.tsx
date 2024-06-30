"use client";

import { Button } from "@nextui-org/button";
import {
    Dropdown,
    DropdownItem,
    DropdownMenu,
    DropdownTrigger,
} from "@nextui-org/dropdown";
import {
    Navbar,
    NavbarBrand,
    NavbarContent,
    NavbarItem,
    NavbarMenu,
    NavbarMenuToggle,
} from "@nextui-org/navbar";
import { User } from "@nextui-org/user";
import { usePathname, useRouter } from "next/navigation";
import React, { Key } from "react";
import {
    FaAngleDown,
    FaExternalLinkAlt,
    FaEye,
    FaFileArchive,
    FaSearch,
    FaUser,
} from "react-icons/fa";

import { logoutAction } from "@/action/auth";
import FormSubmitButton from "@/component/form-submit-button";
import Logo from "@/component/logo";
import NextLink from "@/lib/next-ui/link";
import { ERole } from "@/type/constants";
import { NavbarRequiredCandidateProfileDetails } from "@/type/entity/candidate-profile";
import { NavbarRequiredRecruiterProfileDetails } from "@/type/entity/recruiter-profile";

export type Props = {
    profile:
        | NavbarRequiredCandidateProfileDetails
        | NavbarRequiredRecruiterProfileDetails;
};

export default function DashboardNavbar({ profile }: Readonly<Props>) {
    const [isMenuOpen, setIsMenuOpen] = React.useState(false);
    const path = usePathname();

    return (
        <Navbar
            classNames={{
                wrapper:
                    "lg:gap-12 xl:gap-20 2xl:gap-32 items-center w-full max-w-full xl:px-32",
            }}
            onMenuOpenChange={setIsMenuOpen}
        >
            <NavbarContent justify="start">
                <NavbarMenuToggle
                    aria-label={isMenuOpen ? "Close menu" : "Open menu"}
                    className="md:hidden"
                />
                <NavbarBrand>
                    <Logo href="/dashboard" />
                </NavbarBrand>
            </NavbarContent>

            <NavbarContent className="hidden md:flex gap-4" justify="center">
                {profile.role === ERole.CANDIDATE ? (
                    <CandidateNavbarItems path={path} />
                ) : (
                    <RecruiterNavbarItems path={path} />
                )}
            </NavbarContent>
            <NavbarContent justify="end">
                <NavbarItem>
                    <User
                        avatarProps={{
                            src: profile.profilePhotoUrl,
                            name: `${profile.firstName} ${profile.lastName}`,
                            fallback: <FaUser />,
                            showFallback: true,
                        }}
                        classNames={{
                            name: "hidden md:block",
                            description: "hidden md:block",
                        }}
                        description={profile.email}
                        name={`${profile.firstName} ${profile.lastName}`}
                    />
                </NavbarItem>
                <NavbarItem>
                    <form action={logoutAction}>
                        <FormSubmitButton color="primary" variant="flat">
                            Logout
                        </FormSubmitButton>
                    </form>
                </NavbarItem>
            </NavbarContent>
            <NavbarMenu>
                {profile.role === ERole.CANDIDATE ? (
                    <CandidateNavbarItems path={path} />
                ) : (
                    <RecruiterNavbarItems path={path} />
                )}
            </NavbarMenu>
        </Navbar>
    );
}

function CandidateNavbarItems({ path }: { path: string }) {
    const router = useRouter();

    function handleDrowdownAction(key: Key) {
        switch (key) {
            case "applied-jobs":
                return router.push("/dashboard/c/job/applied");
            case "bookmarked-jobs":
                return router.push("/dashboard/c/job/bookmarked");
            case "all-jobs":
                return router.push("/dashboard/c/job");
        }
    }

    return (
        <>
            <NavbarItem>
                <Button
                    as={NextLink}
                    color={
                        path === "/dashboard/c/job/search"
                            ? "primary"
                            : "secondary"
                    }
                    href="/dashboard/c/job/search"
                    startContent={<FaSearch />}
                >
                    Search for Jobs
                </Button>
            </NavbarItem>
            <NavbarItem>
                <Dropdown>
                    <DropdownTrigger>
                        <Button
                            color={
                                path.startsWith("/dashboard/c/job/") &&
                                path !== "/dashboard/c/job/search"
                                    ? "primary"
                                    : "secondary"
                            }
                            endContent={<FaAngleDown />}
                        >
                            More on Jobs
                        </Button>
                    </DropdownTrigger>
                    <DropdownMenu
                        aria-label="Static Actions"
                        onAction={handleDrowdownAction}
                    >
                        <DropdownItem
                            key="applied-jobs"
                            endContent={<FaExternalLinkAlt />}
                        >
                            Applied Jobs
                        </DropdownItem>
                        <DropdownItem
                            key="bookmarked-jobs"
                            endContent={<FaExternalLinkAlt />}
                        >
                            Bookmarked Jobs
                        </DropdownItem>
                        <DropdownItem
                            key="all-jobs"
                            endContent={<FaExternalLinkAlt />}
                        >
                            All Jobs
                        </DropdownItem>
                    </DropdownMenu>
                </Dropdown>
            </NavbarItem>
            <NavbarItem>
                <Button
                    as={NextLink}
                    color={
                        path.startsWith("/dashboard/c/profile")
                            ? "primary"
                            : "secondary"
                    }
                    href="/dashboard/c/profile"
                    startContent={<FaUser />}
                >
                    Your Profile
                </Button>
            </NavbarItem>
        </>
    );
}

function RecruiterNavbarItems({ path }: { path: string }) {
    return (
        <>
            <NavbarItem>
                <Button
                    as={NextLink}
                    color={
                        path === "/dashboard/r/job/create"
                            ? "primary"
                            : "secondary"
                    }
                    href="/dashboard/r/job/create"
                    startContent={<FaFileArchive />}
                >
                    Post New Job
                </Button>
            </NavbarItem>
            <NavbarItem>
                <Button
                    as={NextLink}
                    color={
                        path.startsWith("/dashboard/r/job") &&
                        path !== "/dashboard/r/job/create"
                            ? "primary"
                            : "secondary"
                    }
                    href="/dashboard/r/job"
                    startContent={<FaEye />}
                >
                    View Your Jobs
                </Button>
            </NavbarItem>
            <NavbarItem>
                <Button
                    as={NextLink}
                    color={
                        path.startsWith("/dashboard/r/profile")
                            ? "primary"
                            : "secondary"
                    }
                    href="/dashboard/r/profile"
                    startContent={<FaUser />}
                >
                    Your Profile
                </Button>
            </NavbarItem>
        </>
    );
}
