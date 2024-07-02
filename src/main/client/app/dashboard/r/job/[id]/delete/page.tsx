import { notFound } from "next/navigation";

import { getJobById } from "@/service/job";
import JobDeleteForm from "@/component/job/job-delete-form";

type Props = {
    params: { id: string };
};

export default async function DeleteJobPage({ params }: Props) {
    const jobResponse = await getJobById(params.id);

    if (!jobResponse.success) {
        notFound();
    }
    const job = jobResponse.data;

    return (
        <main className="flex flex-col items-center mt-8 gap-6 mx-auto max-w-xl">
            <h1 className="text-4xl font-bold md:text-5xl">Delete Job</h1>
            <JobDeleteForm job={job} />
        </main>
    );
}
