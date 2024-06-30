import UpdateRecruiterForm from "./update-recruiter-form";

import { getAllCompany } from "@/service/company";
import { getCurrentRecruiterProfile } from "@/service/recruiter-profile";

export default async function UpdateRecruiterProfilePage() {
    const profileRes = await getCurrentRecruiterProfile();

    if (!profileRes.success) {
        throw new Error(profileRes.message);
    }
    const companyRes = await getAllCompany();

    if (!companyRes.success) {
        throw new Error(companyRes.message);
    }

    const profile = profileRes.data;
    const companies = companyRes.data;

    return (
        <>
            <h2 className="text-xl font-semibold md:text-3xl">
                Update Your Profile
            </h2>
            <UpdateRecruiterForm companies={companies} defaultValue={profile} />
        </>
    );
}
