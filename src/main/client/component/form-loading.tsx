import { Spinner } from "@nextui-org/spinner";
import { useFormStatus } from "react-dom";

export default function FormLoading() {
    const { pending } = useFormStatus();

    if (!pending) return null;

    return (
        <div className="grid place-content-center">
            <Spinner className="text-primary" />
        </div>
    );
}
