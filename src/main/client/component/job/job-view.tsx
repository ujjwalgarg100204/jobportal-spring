import { FaBuilding, FaMapPin } from "react-icons/fa";

import Description from "./job-description";
import JobActions from "./job-actions";

import { ERole } from "@/type/constants";
import { JobViewPage } from "@/type/entity/job";

type Props =
    | {
          job: JobViewPage;
          role: ERole.RECRUITER;
      }
    | {
          job: JobViewPage;
          role: ERole.CANDIDATE;
          isApplied: boolean;
          isBookmarked: boolean;
      };

export default function JobView(props: Props) {
    return (
        <main className="space-y-3 mt-8 w-full m-4 text-white">
            <section className="space-y-2 p-6 bg-secondary shadow-md rounded-xl">
                <h1 className="text-3xl md:text-4xl py-3">Job Detail</h1>
                <div className="space-y-1.5 pt-3">
                    <h2 className="font-bold text-2xl pb-3 underline text-white">
                        {props.job.title}
                    </h2>
                    <div className="space-x-2.5 flex items-center">
                        <FaMapPin className="size-5" />
                        <span className="w-full">
                            {props.job.address?.city},{props.job.address?.state}
                            ,{props.job.address?.country}
                        </span>
                    </div>
                    <div className="space-x-2.5 flex items-center">
                        <FaBuilding className="size-5" />
                        <span className="w-full">{props.job.company.name}</span>
                    </div>
                </div>
            </section>
            <div className="p-6 bg-white text-black shadow-md rounded-xl space-y-6">
                <section className="space-y-4">
                    <h2 className="text-2xl font-semibold">Job Specs</h2>
                    <div className="flex gap-4 justify-between rounded-md border-2 border-gray-600 p-4">
                        <span>
                            Date Posted:{" "}
                            {new Date(props.job.createdAt).toLocaleString(
                                "en-US",
                                {},
                            )}
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
                <section>
                    {props.role === ERole.CANDIDATE ? (
                        <JobActions
                            isApplied={props.isApplied}
                            isBookmarked={props.isBookmarked}
                            jobId={props.job.id}
                            role={props.role}
                        />
                    ) : (
                        <JobActions jobId={props.job.id} role={props.role} />
                    )}
                </section>
            </div>
        </main>
    );
}
