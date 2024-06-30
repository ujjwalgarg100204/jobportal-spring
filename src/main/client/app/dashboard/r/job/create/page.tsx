import JobCreateUpdateForm from "@/component/job/job-create-update-form";

export default function CreateJobPage() {
    return (
        <main className="flex flex-col items-center mt-8 gap-6 mx-auto max-w-xl">
            <h1 className="text-4xl font-bold md:text-5xl">Create Job</h1>
            <JobCreateUpdateForm defaultValues={null} />
        </main>
    );
}
