"use client";

import { useFormState } from "react-dom";

import BackButton from "../back-button";
import FormError from "../form-error";
import FormSubmitButton from "../form-submit-button";

import { deleteJobAction } from "@/action/job";
import { GetJobByIdResponse } from "@/type/entity/job";

type Props = {
    job: GetJobByIdResponse;
};

export default function JobDeleteForm({ job }: Props) {
    const [state, formAction] = useFormState(deleteJobAction, null);

    return (
        <form action={formAction} className="text-center space-y-6 pt-6">
            <FormError formState={state} />
            <input hidden name="id" type="hidden" value={job.id} />
            <h2 className="text-2xl font-bold">
                Are you sure you want to delete &apos;{job.title}&apos; job?
            </h2>
            <p className="text-lg">This action cannot be undone!</p>
            <div className="flex gap-6 justify-center">
                <FormSubmitButton color="danger">Delete</FormSubmitButton>
                <BackButton color="primary" startContent={null} variant="solid">
                    Go Back
                </BackButton>
            </div>
        </form>
    );
}
