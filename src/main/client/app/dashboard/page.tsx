import { redirect, RedirectType } from "next/navigation";

import { getServerSession } from "@/service/auth";
import { logger } from "@/utils/logger";

export default async function DashboardPage() {
    const session = (await getServerSession())!;

    switch (session.user.role) {
        case "CANDIDATE":
            return redirect("/dashboard/c", RedirectType.replace);
        case "RECRUITER":
            return redirect("/dashboard/j", RedirectType.replace);
        default:
            logger.error(
                `Invalid user logged in session:${JSON.stringify(session)}`,
            );
            throw new Error("Invalid user logged in");
    }
}
