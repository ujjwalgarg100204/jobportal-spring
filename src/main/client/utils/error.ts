import { isValidationError } from "zod-validation-error";

type Jsonable =
    | string
    | number
    | boolean
    | null
    | undefined
    | readonly Jsonable[]
    | { readonly [key: string]: Jsonable }
    | { toJSON(): Jsonable };

export class BaseError extends Error {
    public readonly context?: Jsonable;

    constructor(
        message: string,
        options: { cause?: Error; context?: Jsonable } = {},
    ) {
        const { cause, context } = options;

        super(message, { cause });
        this.name = this.constructor.name;

        this.context = context;
    }
}

export const ensureError = (err: unknown): Error => {
    if (isValidationError(err)) return err;
    if (err instanceof Error) return err;

    let stringified = "[unable to stringify the thrown value]";

    try {
        stringified = JSON.stringify(err);
    } catch {}

    return new Error(
        `The value was thrown as is, not through an Error: ${stringified}`,
    );
};
