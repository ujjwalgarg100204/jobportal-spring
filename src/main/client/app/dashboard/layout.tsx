import { ReactNode } from "react";
import { redirect } from "next/navigation";

import DashboardNavbar from "./dashboard-navbar";

import { getServerSession } from "@/service/auth";
import {
    getCandidateProfileById,
    getCandidateProfilePhotoUrl,
} from "@/service/candidate-profile";
import {
    getCurrentRecruiterProfile,
    getRecruiterProfilePhotoUrl,
} from "@/service/recruiter-profile";
import { ERole } from "@/type/constants";

type Props = {
    children: ReactNode;
};
export default async function DashboardLayout({ children }: Readonly<Props>) {
    const session = await getServerSession();

    if (!session) {
        redirect("/auth/login");
    }

    const profileResponse =
        session.user.role === ERole.CANDIDATE
            ? await getCandidateProfileById(session.user.id)
            : await getCurrentRecruiterProfile();

    if (!profileResponse.success) {
        throw new Error("Unable to fetch user profile");
    }

    let profilePhotoUrl: string | undefined = undefined;

    if (profileResponse.data.hasProfilePhoto) {
        const profilePhotoResponse =
            session.user.role === ERole.CANDIDATE
                ? await getCandidateProfilePhotoUrl()
                : await getRecruiterProfilePhotoUrl();

        if (profilePhotoResponse.success) {
            profilePhotoUrl = profilePhotoResponse.data;
        }
    }

    return (
        <>
            <DashboardNavbar
                profile={{
                    id: session.user.id,
                    email: session.user.email,
                    role: session.user.role,
                    firstName: profileResponse.data.firstName,
                    lastName: profileResponse.data.lastName,
                    profilePhotoUrl,
                }}
            />
            <div className="px-4 md:px-8 lg:px-24 xl:px-32 mt-4">
                {children}
            </div>
        </>
    );
}
