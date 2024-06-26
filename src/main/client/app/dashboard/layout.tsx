import { ReactNode } from "react";
import { redirect } from "next/navigation";

import DashboardNavbar from "./dashboard-navbar";

import { getServerSession } from "@/service/auth";
import { getCandidateProfileById } from "@/service/candidate-profile";
import { getRecruiterProfileById } from "@/service/recruiter-profile";

type Props = {
    children: ReactNode;
};
export default async function DashboardLayout({ children }: Readonly<Props>) {
    const session = await getServerSession();

    if (!session) {
        redirect("/auth/login");
    }

    const profileResponse =
        session.user.role === "CANDIDATE"
            ? await getCandidateProfileById(session.user.id)
            : await getRecruiterProfileById(session.user.id);

    if (!profileResponse.success) {
        throw new Error("Unable to fetch user profile");
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
                    hasProfilePhoto: profileResponse.data.hasProfilePhoto,
                }}
            />
            <div className="px-4 md:px-8 lg:px-24 xl:px-32 mt-4">
                {children}
            </div>
        </>
    );
}
