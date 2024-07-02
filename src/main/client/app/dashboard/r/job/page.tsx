import JobCard from "@/component/job/job-card";
import {
    getAllJobsOfRecruiterWithApplicantCount,
    getCurrentRecruiterProfile,
} from "@/service/recruiter-profile";
import { ERole } from "@/type/constants";
import { BaseError } from "@/utils/error";

export default async function AllJobsOfRecruiterPage() {
    const jobResponse = await getAllJobsOfRecruiterWithApplicantCount();
    const profileResponse = await getCurrentRecruiterProfile();

    if (!jobResponse.success) {
        throw new BaseError(jobResponse.message, { context: jobResponse });
    } else if (!profileResponse.success) {
        throw new BaseError(profileResponse.message, {
            context: profileResponse,
        });
    }

    const company = profileResponse.data.company;
    const jobs = jobResponse.data;

    return (
        <section className="space-y-10 mt-8">
            <h1 className="text-4xl font-bold md:text-5xl text-center">
                Your Jobs
            </h1>
            <ul className="flex flex-wrap gap-8 items-center justify-center">
                {jobs.length === 0 && (
                    <li>Jobs created by you will show up here</li>
                )}
                {jobs.map(job => (
                    <li key={job.id}>
                        <JobCard
                            actionProps={{
                                role: ERole.RECRUITER,
                                jobId: job.id,
                            }}
                            job={{ ...job, company }}
                        />
                    </li>
                ))}
            </ul>
        </section>
    );
}
