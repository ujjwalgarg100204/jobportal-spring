import CompanyCreateUpdateForm from "../company-create-update-form";

import { getCompanyLogoUrl } from "@/service/company";
import { getCurrentRecruiterCompany } from "@/service/recruiter-profile";

export default async function CompanyUpdatePage() {
    const companyRes = await getCurrentRecruiterCompany();

    if (!companyRes.success) {
        throw new Error(`Failed to fetch company: ${companyRes.message}`);
    }
    const company = companyRes.data;
    let logoUrl: string | undefined = undefined;

    if (company.hasLogo) {
        const response = await getCompanyLogoUrl(company.id);

        if (response.success) {
            logoUrl = response.data;
        }
    }

    return (
        <>
            <h2 className="text-2xl font-semibold md:text-3xl">
                Update Company: {company.name}
            </h2>
            <CompanyCreateUpdateForm
                defaultValue={{
                    ...company,
                    logoUrl,
                }}
            />
        </>
    );
}
