import { RedirectType, redirect } from "next/navigation";
import { ReactNode } from "react";

import { getServerSession } from "@/service/auth";
import { ERole } from "@/type/constants";

type Props = {
    children: ReactNode;
};

export default async function RecruiterDashboardLayout({ children }: Props) {
    const session = (await getServerSession())!;

    if (session.user.role === ERole.RECRUITER) {
        return children;
    }
    redirect("/dashboard/c", RedirectType.replace);
}
