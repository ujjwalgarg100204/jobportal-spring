import { Button, ButtonGroup } from "@nextui-org/button";
import { ReactNode } from "react";
import {
    FaBookmark,
    FaCheck,
    FaPenAlt,
    FaRegChartBar,
    FaTrashAlt,
} from "react-icons/fa";
import { MdBookmarkAdded } from "react-icons/md";

import { ERole } from "@/type/constants";
import NextLink from "@/lib/next-ui/link";

export type Props =
    | {
          jobId: number | string;
          role: ERole.RECRUITER;
      }
    | {
          jobId: number | string;
          role: ERole.CANDIDATE;
          isApplied: boolean;
          isBookmarked: boolean;
      };

export default function JobActions(props: Props) {
    const actions: ReactNode[] = [];

    if (props.role === ERole.RECRUITER) {
        actions.push(
            <Button
                as={NextLink}
                href={`/dashboard/r/job/${props.jobId}/statistics`}
                startContent={<FaRegChartBar />}
            >
                Statistics
            </Button>,
        );
        actions.push(
            <Button
                as={NextLink}
                href={`/dashboard/r/job/${props.jobId}/update`}
                startContent={<FaPenAlt />}
            >
                Update
            </Button>,
        );
        actions.push(
            <Button
                as={NextLink}
                href={`/dashboard/r/job/${props.jobId}/delete`}
                startContent={<FaTrashAlt />}
            >
                Delete
            </Button>,
        );
    } else if (props.role === ERole.CANDIDATE) {
        if (props.isApplied) {
            actions.push(
                <Button disabled isDisabled startContent={<FaCheck />}>
                    Applied
                </Button>,
            );
        } else {
            actions.push(
                <Button
                    as={NextLink}
                    href={`/dashboard/c/job/${props.jobId}/apply`}
                    startContent={<FaCheck />}
                >
                    Apply
                </Button>,
            );
        }
        if (props.isBookmarked) {
            actions.push(
                <Button disabled isDisabled startContent={<MdBookmarkAdded />}>
                    Bookmarked
                </Button>,
            );
        } else {
            actions.push(
                <Button
                    as={NextLink}
                    href={`/dashboard/c/job/${props.jobId}/bookmark`}
                    startContent={<FaBookmark />}
                >
                    Bookmark
                </Button>,
            );
        }
    }

    return <ButtonGroup color="primary">{actions}</ButtonGroup>;
}
