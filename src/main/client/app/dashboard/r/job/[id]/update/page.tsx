import JobCreateUpdateForm from "@/component/job/job-create-update-form";
import { EmploymentType, RemoteType } from "@/type/constants";

type Props = {
    params: { id: string };
};

// TODO: connect to backend this page
export default async function UpdateJobPage({ params }: Props) {
    return (
        <main className="flex flex-col items-center mt-8 gap-6 mx-auto max-w-xl">
            <h1 className="text-4xl font-bold md:text-5xl">Update Job</h1>
            <JobCreateUpdateForm
                defaultValues={{
                    hiringComplete: false,
                    noOfVacancy: 3,
                    employmentType: EmploymentType.FREELANCE,
                    remoteType: RemoteType.PARTIAl_REMOTE,
                    description: "sfasd",
                    id: Number(params.id),
                    title: "Good Job",
                }}
            />
        </main>
    );
}
