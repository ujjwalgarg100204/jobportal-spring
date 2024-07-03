import JobView from "@/component/job/job-view";
import { ERole } from "@/type/constants";
import { getJobById } from "@/service/job";
import { BaseError } from "@/utils/error";
import {
    checkIfJobIsApplied,
    checkIfJobIsBookmarked,
} from "@/service/candidate-profile";

type Props = {
    params: { id: string };
};
export default async function JobViewPage({ params }: Props) {
    const jobResponse = await getJobById(params.id);
    const isAppliedResponse = await checkIfJobIsApplied(params.id);
    const isBookmarkedResponse = await checkIfJobIsBookmarked(params.id);

    if (
        !jobResponse.success ||
        !isAppliedResponse.success ||
        !isBookmarkedResponse.success
    ) {
        throw new BaseError(
            "Failed to fetch data for job view page for candidate",
            {
                context: {
                    jobResponse,
                    isAppliedResponse,
                    isBookmarkedResponse,
                },
            },
        );
    }

    return (
        <JobView
            isApplied={isAppliedResponse.data}
            isBookmarked={isBookmarkedResponse.data}
            job={jobResponse.data}
            role={ERole.CANDIDATE}
        />
    );
}
