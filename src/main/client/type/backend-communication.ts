type BackendSuccessResponse<Data> = {
    success: true;
    data: Data;
    message: string;
};
type BackendErrorResponse = {
    success: false;
    validationErrors?: string[];
    message: string;
};

export type BackendResponse<Data> =
    | BackendSuccessResponse<Data>
    | BackendErrorResponse;

export type RequestConfig = Omit<RequestInit, "method"> & {
    authenticatedRequest: boolean;
    jsonRequest: boolean;
};

export type HttpVerb = "GET" | "POST" | "PUT" | "DELETE" | "PATCH";

export type ActionResponse<Data> = BackendResponse<Data> | null;

export type Id = string | number;
