import { FaBug } from "react-icons/fa";

import { ActionResponse } from "@/type/backend-communication";

type Props = {
    formState: ActionResponse<any>;
};
export default function FormError({ formState }: Readonly<Props>) {
    if (!formState || formState.success) return null;

    return (
        <div className="flex flex-col items-center gap-3 rounded-md border-2 border-red-600 p-4">
            <FaBug className="size-8 text-red-600" />
            <div className="space-y-2">
                {formState.validationErrors && (
                    <details>
                        <summary className="font-semibold">
                            Validation Errors
                        </summary>
                        <ol className="list-inside list-image-[❌]">
                            {formState.validationErrors.map((e, index) => (
                                <li key={index}>{e}</li>
                            ))}
                        </ol>
                    </details>
                )}
                {formState.message && (
                    <details>
                        <summary className="font-semibold">
                            Server Error
                        </summary>
                        <p>{formState.message}</p>
                    </details>
                )}
            </div>
        </div>
    );
}
