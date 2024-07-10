import { redirect, RedirectType } from "next/navigation";
import { ReactNode } from "react";

import { getServerSession } from "@/service/auth";
import { ERole } from "@/type/constants";

type Props = {
    children: ReactNode;
};

export default async function CandidateDashboardLayout({ children }: Props) {
    const session = await getServerSession();

    if (!session) {
        redirect("/auth/login");
    }

    if (session.user.role === ERole.CANDIDATE) {
        return children;
    }
    redirect("/dashboard/r", RedirectType.replace);
}
