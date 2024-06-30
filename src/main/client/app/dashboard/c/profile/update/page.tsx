import CandidateUpdateForm from "./candidate-update-form";

import { getServerSession } from "@/service/auth";
import { getCandidateProfileById } from "@/service/candidate-profile";

export default async function EditCandidateProfile() {
    const session = (await getServerSession())!;
    const profile = await getCandidateProfileById(session.user.id);

    if (!profile.success) {
        throw new Error("Candidate Profile not found");
    }

    return (
        <main className="flex flex-col items-center mt-8 gap-6 mx-auto max-w-xl">
            <h1 className="text-4xl font-bold md:text-5xl">Candidate</h1>
            <h2 className="text-xl font-semibold md:text-3xl">
                Update Your Profile
            </h2>
            <CandidateUpdateForm defaultValue={profile.data} />
        </main>
    );
}
