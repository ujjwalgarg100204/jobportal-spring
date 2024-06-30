import CompanyCreateUpdateForm from "../company-create-update-form";

export default function CreateCompanyPage() {
    return (
        <>
            <h2 className="text-2xl font-semibold md:text-3xl">
                Create Company
            </h2>
            <CompanyCreateUpdateForm defaultValue={null} />
        </>
    );
}
