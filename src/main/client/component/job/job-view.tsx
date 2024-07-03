import { Tooltip } from "@nextui-org/tooltip";
import { FaBuilding, FaMapPin } from "react-icons/fa";
import { Avatar } from "@nextui-org/avatar";

import Description from "./job-description";
import JobActions from "./job-actions";

import { ERole } from "@/type/constants";
import { JobViewPage } from "@/type/entity/job";

type Props =
    | { job: JobViewPage; role: ERole.RECRUITER }
    | {
          job: JobViewPage;
          role: ERole.CANDIDATE;
          isApplied: boolean;
          isBookmarked: boolean;
      };

export default function JobView(props: Props) {
    return (
        <main className="space-y-3 mt-8 w-full text-white">
            <section className="space-y-2 p-6 bg-secondary flex justify-between shadow-md items-center rounded-xl">
                <div>
                    <h1 className="text-3xl md:text-4xl py-3">
                        Job Detail -{" "}
                        <span className="underline">{props.job.title}</span>
                    </h1>
                    <div className="grid grid-cols-[5%_90%] gap-x-2 gap-y-4 pt-3">
                        <div className="size-6">
                            <FaMapPin className="size-full" />
                        </div>
                        <span>
                            {props.job.address?.city},{props.job.address?.state}
                            ,{props.job.address?.country}
                        </span>
                        <div className="size-6">
                            <FaBuilding className="size-full" />
                        </div>
                        <span>{props.job.company.name}</span>
                    </div>
                </div>
                <div className="sm:flex gap-4 items-center hidden">
                    <Tooltip showArrow content="No. of Candidates Applied">
                        <p className="text-5xl font-bold underline">
                            {props.job.noOfAppliedCandidates}
                        </p>
                    </Tooltip>
                    <span className="text-xl">for</span>
                    <Tooltip showArrow content="No. of Vacancy">
                        <p className="text-5xl font-bold underline">
                            {props.job.noOfVacancy}
                        </p>
                    </Tooltip>
                </div>
            </section>
            <div className="p-6 bg-white text-black shadow-md rounded-xl space-y-8">
                <section className="space-y-4">
                    <h2 className="text-2xl font-semibold">Job Specs</h2>
                    <div className="flex gap-2 md:gap-4 flex-col md:flex-row justify-between rounded-md border-2 border-gray-600 p-4">
                        <span>
                            Date Posted:{" "}
                            {new Date(props.job.createdAt).toDateString()}
                        </span>
                        <span>Salary: {props.job.salary ?? "Not posted"}</span>
                        <span>Type: {props.job.employmentType}</span>
                        <span>Remote: {props.job.remoteType}</span>
                    </div>
                </section>
                <section className="space-x-4 space-y-3">
                    <h2 className="text-2xl font-semibold">Job Description</h2>
                    <Description description={props.job.description} />
                </section>

                <section className="space-x-4 space-y-3">
                    <h2 className="text-2xl font-semibold">
                        About the Company
                    </h2>
                    <section className="flex items-start justify-center gap-4 flex-col-reverse sm:flex-row sm:items-center sm:justify-start sm:gap-20 lg:gap-24">
                        <div className="text-sm">
                            <h3 className="text-xl">
                                {props.job.company.name}
                            </h3>
                            <p className="font-semibold mt-4">Address</p>
                            {props.job.company.address && (
                                <>
                                    <p>
                                        City: {props.job.company.address.city}
                                    </p>
                                    <p>
                                        State: {props.job.company.address.state}
                                    </p>
                                    <p>
                                        Country:{" "}
                                        {props.job.company.address.country}
                                    </p>
                                </>
                            )}
                        </div>
                        <Avatar
                            showFallback
                            className="w-24 h-24 md:w-32 md:h-32"
                            fallback={<FaBuilding className="w-10 h-10" />}
                            name={props.job.company.name}
                            src={undefined}
                        />
                    </section>
                </section>

                <section className="grid place-content-center">
                    {props.role === ERole.CANDIDATE ? (
                        <JobActions
                            isApplied={props.isApplied}
                            isBookmarked={props.isBookmarked}
                            jobId={props.job.id}
                            role={ERole.CANDIDATE}
                        />
                    ) : (
                        <JobActions
                            jobId={props.job.id}
                            role={ERole.RECRUITER}
                        />
                    )}
                </section>
            </div>
        </main>
    );
}
