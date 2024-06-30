import JobView from "@/component/job/job-view";
import { ERole } from "@/type/constants";

// TODO: get job info and user info etc
type Props = {
    params: { id: string };
};
export default async function JobViewPage({ params }: Props) {
    return <JobView job={{}} role={ERole.RECRUITER} />;
}
