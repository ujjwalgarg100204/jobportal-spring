import { Card, CardBody, CardFooter, CardHeader } from "@nextui-org/card";
import { Divider } from "@nextui-org/divider";
import { FaBuilding } from "react-icons/fa";

import JobActions, { Props as JobActionsProps } from "./job-actions";

import { GetRecruiterJobsResponse } from "@/type/entity/job";
import { Company } from "@/type/entity/company";
import { ERole } from "@/type/constants";

type Props = {
    job: GetRecruiterJobsResponse & { company: Company };
    actionProps: JobActionsProps;
};

export default function JobCard({ job, actionProps }: Props) {
    return (
        <Card className="max-w-[340px]">
            <CardHeader className="flex gap-3">
                <div className="flex flex-col gap-1">
                    <p className="text-lg font-semibold">{job.title}</p>
                    <p className="text-small text-default-500 flex items-center gap-2">
                        <FaBuilding className="size-4" /> {job.company.name}
                    </p>
                </div>
            </CardHeader>
            <Divider />
            <CardBody>
                <p>
                    Posted at: {new Date(job.createdAt).toLocaleString("en-US")}
                </p>
                {actionProps.role === ERole.RECRUITER && (
                    <p>
                        <span className="underline font-bold space-x-1">
                            {job.noOfApplicants}
                        </span>{" "}
                        Applicants applied
                    </p>
                )}
                {actionProps.role === ERole.CANDIDATE &&
                    (job.noOfApplicants === 0 ? (
                        <p>
                            Be the{" "}
                            <span className="underline font-bold space-x-1">
                                First
                            </span>{" "}
                            one to apply
                        </p>
                    ) : (
                        <p>
                            <span className="underline font-bold space-x-1">
                                {job.noOfApplicants}
                            </span>{" "}
                            Applicants applied
                        </p>
                    ))}
            </CardBody>
            <Divider />
            <CardFooter>
                <JobActions {...actionProps} />
            </CardFooter>
        </Card>
    );
}
