import JobView from "@/component/job/job-view";
import { ERole } from "@/type/constants";
import { getJobById } from "@/service/job";
import { BaseError } from "@/utils/error";

type Props = {
    params: { id: string };
};
export default async function JobViewPage({ params }: Props) {
    const jobResponse = await getJobById(params.id);

    if (!jobResponse.success) {
        throw new BaseError(
            "Failed to fetch data for job view page for candidate",
            { context: { jobResponse } },
        );
    }

    return <JobView job={jobResponse.data} role={ERole.RECRUITER} />;
}
