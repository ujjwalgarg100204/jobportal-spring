"use client";

import { useRouter } from "next/navigation";
import { useEffect } from "react";
import { toast } from "react-hot-toast";

import { ActionResponse } from "@/type/backend-communication";

export default function useNavigationWithToast(
    actionState: ActionResponse<any>,
    to: string,
) {
    const router = useRouter();

    useEffect(() => {
        let timer: NodeJS.Timeout | null = null;

        if (!actionState) {
            return;
        }
        if (actionState.success) {
            toast.success(actionState.message);
            timer = setTimeout(() => router.push(to), 1500);
        } else {
            toast.error(actionState.message ?? "Oops, something bad happened!");
        }

        return () => {
            if (timer) clearTimeout(timer);
        };
    }, [actionState]);
}
