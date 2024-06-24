import winston, { format } from "winston";

import { env } from "@/env";

const loggerFormat = format.printf(({ level, message, timestamp }) => {
    return `${timestamp} ${level}: ${message}`;
});

export const logger = winston.createLogger({
    level: "info",
    format: format.combine(format.timestamp(), loggerFormat),
    transports: [
        new winston.transports.File({
            filename: "logs/error.log",
            level: "error",
        }),
    ],
});

// If we're not in production then log to the `console` with the format:
// `${info.level}: ${info.message} JSON.stringify({ ...rest }) `
if (env.NODE_ENV !== "production") {
    logger.add(
        new winston.transports.Console({
            format: format.combine(format.timestamp(), loggerFormat),
        }),
    );
}
