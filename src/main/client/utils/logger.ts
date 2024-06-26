import winston, { format } from "winston";

const loggerFormat = format.printf(({ level, message, timestamp }) => {
    return `${timestamp} ${level}: ${message}`;
});

export const logger = winston.createLogger({
    level: "info",
    format: format.combine(
        format.errors({ stack: true }), // capture stack traces
        format.timestamp({ format: "YYYY-MM-DD HH:mm:ss" }),
        loggerFormat,
    ),
    transports: [
        new winston.transports.Console(),
        new winston.transports.File({
            filename: "logs/all.log",
            level: "error",
        }),
    ],
    exceptionHandlers: [
        new winston.transports.File({ filename: "logs/exceptions.log" }),
    ],
    exitOnError: false,
});
