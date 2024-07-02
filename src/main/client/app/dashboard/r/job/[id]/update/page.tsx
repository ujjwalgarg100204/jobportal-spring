import { notFound } from "next/navigation";

import JobCreateUpdateForm from "@/component/job/job-create-update-form";
import { getJobById } from "@/service/job";

type Props = {
    params: { id: string };
};

export default async function UpdateJobPage({ params }: Props) {
    const jobResponse = await getJobById(params.id);

    if (!jobResponse.success) {
        notFound();
    }

    const job = jobResponse.data;

    return (
        <main className="flex flex-col items-center mt-8 gap-6 mx-auto max-w-xl">
            <h1 className="text-4xl font-bold md:text-5xl">Update Job</h1>
            <JobCreateUpdateForm defaultValues={job} />
        </main>
    );
}
