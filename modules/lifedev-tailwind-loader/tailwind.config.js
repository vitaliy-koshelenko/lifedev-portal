/** @type {import('tailwindcss').Config} */
module.exports = {
  prefix: 'ld-',
  content:
      [
        '*.{html,js}',
        "../../**/*.html",
        "../../**/*.ftl",
        "../../**/*.jsp",
        "../../**/*.js"
      ],
  safelist: [
    {
      pattern: /p[trblxy]?-\d+/,
    },
    {
      pattern: /m[trblxy]?-\d+/,
    },
    {
      pattern: /text-[a-z]+-\d{1,3}/, // Text color classes like text-red-500
    },
    {
      pattern: /bg-[a-z]+-\d{1,3}/,  // Background color classes like bg-red-500
    }
  ],
  theme: {
    extend: {
      colors: {
        red: {
          950: "var(--red-950)",
          900: "var(--red-900)",
          800: "var(--red-800)",
          700: "var(--red-700)",
          600: "var(--red-600)",
          500: "var(--red-500)",
          400: "var(--red-400)",
          300: "var(--red-300)",
          200: "var(--red-200)",
          100: "var(--red-100)",
          50: "var(--red-50)",
        },
        green: {
          950: "var(--green-950)",
          900: "var(--green-900)",
          800: "var(--green-800)",
          700: "var(--green-700)",
          600: "var(--green-600)",
          400: "var(--green-400)",
          300: "var(--green-300)",
          200: "var(--green-200)",
          100: "var(--green-100)",
          50: "var(--green-50)",
        },
        navy: {
          950: "var(--navy-950)",
          900: "var(--navy-900)",
          800: "var(--navy-800)",
          700: "var(--navy-700)",
          600: "var(--navy-600)",
          500: "var(--navy-500)",
          400: "var(--navy-400)",
          300: "var(--navy-300)",
          200: "var(--navy-200)",
          100: "var(--navy-100)",
          50: "var(--navy-50)",
        }
      }
    },
  },
  plugins: [
    require('flowbite/plugin')
  ],
}