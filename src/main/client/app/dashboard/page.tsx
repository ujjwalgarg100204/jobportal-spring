import { redirect, RedirectType } from "next/navigation";

import { getServerSession } from "@/service/auth";
import { logger } from "@/utils/logger";
import { ERole } from "@/type/constants";

export default async function DashboardPage() {
    const session = (await getServerSession())!;

    switch (session.user.role) {
        case ERole.CANDIDATE:
            return redirect("/dashboard/c", RedirectType.replace);
        case ERole.RECRUITER:
            return redirect("/dashboard/r", RedirectType.replace);
        default:
            logger.error(
                `Invalid user logged in session:${JSON.stringify(session)}`,
            );
            throw new Error("Invalid user logged in");
    }
}
