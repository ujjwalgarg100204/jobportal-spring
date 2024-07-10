import { Input } from "@nextui-org/input";
import { Checkbox, CheckboxGroup } from "@nextui-org/checkbox";
import { FaSearch } from "react-icons/fa";
import { useFormState } from "react-dom";

import FormSubmitButton from "../form-submit-button";
import FormLoading from "../form-loading";
import FormError from "../form-error";

import JobCard from "./job-card";

import { Session } from "@/type/auth";

type Props = {
    session: Session;
};

export default function JobSearch({ session }: Props) {
    const [state, formAction] = useFormState(() => {}, null);

    return (
        <form
            action={formAction}
            className="flex flex-col md:flex-row gap-12 justify-start"
        >
            <aside className="space-y-6">
                <p className="underline text-lg">FILTER RESULTS</p>
                <div className="grid grid-cols-2 gap-x-3 gap-y-6 sm:grid-cols-3 md:grid-cols-1">
                    <CheckboxGroup
                        label="Employment Type"
                        name="employment-type"
                    >
                        <Checkbox value="Part-Time">Part-Time</Checkbox>
                        <Checkbox value="Full-Time">Full-Time</Checkbox>
                        <Checkbox value="Freelance">Freelance</Checkbox>
                    </CheckboxGroup>
                    <CheckboxGroup label="Remote" name="remote">
                        <Checkbox value="Remote-Only">Remote-Only</Checkbox>
                        <Checkbox value="Office-Only">Office-Only</Checkbox>
                        <Checkbox value="Partial-Remote">
                            Partial-Remote
                        </Checkbox>
                    </CheckboxGroup>
                    <CheckboxGroup
                        className="col-span-2 justify-self-center sm:col-span-1"
                        label="Date Posted"
                        name="in-last-x-days"
                    >
                        <Checkbox value="0">Today</Checkbox>
                        <Checkbox value="7">Last 7 Days</Checkbox>
                        <Checkbox value="30">Last 30 Days</Checkbox>
                    </CheckboxGroup>
                </div>
            </aside>
            <main className="flex-1 space-y-8">
                <h1 className="text-3xl font-bold col-span-2">
                    Job Seeker Dashboard
                </h1>
                <div className="flex flex-col gap-4 md:flex-row">
                    <Input
                        className="col-span-2 md:col-span-1"
                        label="Search for a Job"
                        name="query"
                        type="text"
                        variant="faded"
                    />
                    <Input
                        className="col-span-2 md:col-span-1"
                        label="Location"
                        name="location"
                        type="text"
                        variant="faded"
                    />
                </div>
                <div className="grid placeholder-center">
                    <FormSubmitButton
                        className="w-full md:mx-auto md:max-w-fit"
                        color="primary"
                        size="lg"
                        startContent={<FaSearch />}
                    >
                        Search
                    </FormSubmitButton>
                </div>

                <section className="col-span-2 pt-4 space-y-8">
                    <h2 className="text-2xl text-center font-bold">
                        Search Results
                    </h2>
                    <div className="max-w-xl mx-auto">
                        <FormError formState={state} />
                    </div>
                    <FormLoading />
                    {state?.success && (
                        <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8 2xl:grid-cols-4">
                            {state.data.map(job => (
                                <li key={job.id}>
                                    <JobCard actionProps={} />
                                </li>
                            ))}
                        </ul>
                    )}
                </section>
            </main>
        </form>
    );
}
