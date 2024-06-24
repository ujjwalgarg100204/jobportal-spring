import { nextui } from "@nextui-org/theme";

/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./components/**/*.{js,ts,jsx,tsx,mdx}",
        "./app/**/*.{js,ts,jsx,tsx,mdx}",
        "./node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            colors: {
                primary: "#fb4f14",
                secondary: "#002244",
            },
            fontFamily: {
                sans: ["var(--font-sans)"],
                mono: ["var(--font-geist-mono)"],
            },
            backgroundImage: {
                "coffee-mug": "url('/assets/images/coffee-mug.jpg')",
            },
        },
    },
    darkMode: "class",
    plugins: [nextui()],
};
