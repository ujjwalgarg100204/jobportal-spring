"use client";

import { Radio, RadioGroup } from "@nextui-org/radio";
import { useState } from "react";
import { twMerge } from "tailwind-merge";

import CandidateRegistrationForm from "./candidate-registration-form";
import RecruiterRegistrationForm from "./recruiter-registration-form";

import { ERole } from "@/type/constants";

export default function RegisterForm() {
    const [userRole, setUserRole] = useState<ERole>("CANDIDATE");

    return (
        <section className="w-full space-y-6">
            <RadioGroup
                defaultValue={userRole}
                label="I am a"
                orientation="horizontal"
                onChange={e => setUserRole(e.target.value as ERole)}
            >
                <Radio
                    classNames={{
                        base: twMerge(
                            "inline-flex m-0 bg-content1 hover:bg-content2 items-center justify-between",
                            "flex-row-reverse max-w-[300px] cursor-pointer rounded-lg gap-4 p-4 border-2 border-transparent",
                            "data-[selected=true]:border-primary",
                        ),
                    }}
                    defaultChecked={userRole === "CANDIDATE"}
                    value="CANDIDATE"
                >
                    Candidate
                </Radio>
                <Radio
                    classNames={{
                        base: twMerge(
                            "inline-flex m-0 bg-content1 hover:bg-content2 items-center justify-between",
                            "flex-row-reverse max-w-[300px] cursor-pointer rounded-lg gap-4 p-4 border-2 border-transparent",
                            "data-[selected=true]:border-primary",
                        ),
                    }}
                    defaultChecked={userRole === "RECRUITER"}
                    value="RECRUITER"
                >
                    Recruiter
                </Radio>
            </RadioGroup>
            {userRole === "CANDIDATE" ? (
                <CandidateRegistrationForm />
            ) : (
                <RecruiterRegistrationForm />
            )}
        </section>
    );
}
