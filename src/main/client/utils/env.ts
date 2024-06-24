import { env as nodeEnv } from "process";

export const env = {
    API_BASE_URL: nodeEnv.API_BASE_URL as string,
    NODE_ENV: nodeEnv.NODE_ENV,
};
