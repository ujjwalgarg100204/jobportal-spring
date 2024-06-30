import { Avatar } from "@nextui-org/avatar";
import { Button } from "@nextui-org/button";
import { FaBuilding, FaPencilAlt } from "react-icons/fa";

import { getCompanyLogoUrl } from "@/service/company";
import { getCurrentRecruiterCompany } from "@/service/recruiter-profile";
import NextLink from "@/lib/next-ui/link";

export default async function CompanyViewPage() {
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
                {company.name}
            </h2>
            <section className="size-full space-y-8">
                <Avatar
                    showFallback
                    className="w-24 h-24 mx-auto md:w-32 md:h-32 lg:w-44 lg:h-44"
                    fallback={<FaBuilding className="w-10 h-10" />}
                    name={company.name}
                    src={logoUrl}
                />
                <div className="grid grid-cols-2 grid-rows-3 bg-gray-900 rounded-lg p-4 space-y-1.5">
                    <p className="row-span-3 justify-self-center self-center font-bold text-lg">
                        Address
                    </p>
                    {company.address && (
                        <>
                            <p>City: {company.address.city}</p>
                            <p>State: {company.address.state}</p>
                            <p>Country: {company.address.country}</p>
                        </>
                    )}
                </div>
                <div className="w-full grid place-content-center">
                    <Button
                        as={NextLink}
                        color="primary"
                        href="/dashboard/r/profile/company/update"
                        startContent={<FaPencilAlt />}
                    >
                        Update
                    </Button>
                </div>
            </section>
        </>
    );
}
