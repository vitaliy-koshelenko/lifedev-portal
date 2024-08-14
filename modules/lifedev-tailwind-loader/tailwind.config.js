/** @type {import('tailwindcss').Config} */
module.exports = {
  prefix: 'ld-',
  content: [],
  safelist: [
    {
      pattern: /p[trblxy]?-\d+/,
    },
    {
      pattern: /m[trblxy]?-\d+/,
    }
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('flowbite/plugin')
  ],
}

