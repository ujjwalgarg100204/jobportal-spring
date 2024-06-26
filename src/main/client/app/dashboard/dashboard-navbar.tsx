"use client";

import { Button } from "@nextui-org/button";
import { Link } from "@nextui-org/link";
import {
    Navbar,
    NavbarBrand,
    NavbarContent,
    NavbarItem,
    NavbarMenu,
    NavbarMenuItem,
    NavbarMenuToggle,
} from "@nextui-org/navbar";
import { User } from "@nextui-org/user";
import { usePathname } from "next/navigation";
import React from "react";
import {
    FaEye,
    FaFileArchive,
    FaPencilAlt,
    FaSearch,
    FaUser,
} from "react-icons/fa";

import { logoutAction } from "@/action/auth";
import Logo from "@/component/logo";
import { NavbarRequiredCandidateProfileDetails } from "@/type/entity/candidate-profile";
import { NavbarRequiredRecruiterProfileDetails } from "@/type/entity/recruiter-profile";
import { env } from "@/utils/env";

const candidateNavbarItems = [
    { icon: <FaSearch />, text: "Search for Jobs", href: "/dashboard/j" },
    { icon: <FaEye />, text: "View saved jobs", href: "/dashboard/saved-jobs" },
    {
        icon: <FaPencilAlt />,
        text: "Edit Profile",
        href: "/dashboard/edit-profile",
    },
] as const;
const recruiterNavbarItems = [
    {
        icon: <FaFileArchive />,
        text: "Post new Job",
        href: "/dashboard/add-job",
    },
    { icon: <FaEye />, text: "View your jobs", href: "/dashboard/r" },
    {
        icon: <FaPencilAlt />,
        text: "Edit Account",
        href: "/dashboard/edit-account",
    },
] as const;

export type Props = {
    profile:
        | NavbarRequiredCandidateProfileDetails
        | NavbarRequiredRecruiterProfileDetails;
};

export default function DashboardNavbar({ profile }: Readonly<Props>) {
    const [isMenuOpen, setIsMenuOpen] = React.useState(false);
    const path = usePathname();
    const navbarItems =
        profile.role === "CANDIDATE"
            ? candidateNavbarItems
            : recruiterNavbarItems;

    return (
        <Navbar
            classNames={{
                wrapper: "lg:gap-12 xl:gap-20 2xl:gap-32 items-center",
            }}
            onMenuOpenChange={setIsMenuOpen}
        >
            <NavbarContent justify="start">
                <NavbarMenuToggle
                    aria-label={isMenuOpen ? "Close menu" : "Open menu"}
                    className="md:hidden"
                />
                <NavbarBrand>
                    <Logo />
                </NavbarBrand>
            </NavbarContent>

            <NavbarContent className="hidden md:flex gap-4" justify="center">
                {navbarItems.map(item => (
                    <NavbarItem key={item.text}>
                        <Button
                            as={Link}
                            color={path === item.href ? "primary" : "secondary"}
                            href={item.href}
                            startContent={item.icon}
                        >
                            {item.text}
                        </Button>
                    </NavbarItem>
                ))}
            </NavbarContent>
            <NavbarContent justify="end">
                <NavbarItem>
                    <User
                        avatarProps={{
                            src: getProfilePhotoUrl(profile),
                            name: getAvatarName(profile),
                            fallback: <FaUser />,
                            showFallback: true,
                        }}
                        classNames={{
                            name: "hidden md:block",
                            description: "hidden md:block",
                        }}
                        description={profile.email}
                        name={getAvatarName(profile)}
                    />
                </NavbarItem>
                <NavbarItem>
                    <form action={logoutAction}>
                        <Button color="primary" type="submit" variant="flat">
                            Logout
                        </Button>
                    </form>
                </NavbarItem>
            </NavbarContent>
            <NavbarMenu>
                {navbarItems.map((item, index) => (
                    <NavbarMenuItem key={`${item.text}-${index}`}>
                        <Button
                            as={Link}
                            className="w-full"
                            color="primary"
                            href={item.href}
                            size="lg"
                            startContent={item.icon}
                        >
                            {item.text}
                        </Button>
                    </NavbarMenuItem>
                ))}
            </NavbarMenu>
        </Navbar>
    );
}

function getProfilePhotoUrl(profile: Props["profile"]): string | undefined {
    if (profile.hasProfilePhoto) {
        return `${env.API_BASE_URL}/recruiter/profile/photo`;
    }
}

function getAvatarName(profile: Props["profile"]): string {
    let name = profile.email.substring(0, profile.email.indexOf("@"));

    if (profile.firstName) {
        name = profile.firstName;
    } else if (!!profile.firstName && !!profile.lastName) {
        name = `${profile.firstName} ${profile.lastName}`;
    }

    return name;
}
