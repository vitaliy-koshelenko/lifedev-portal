# Tailwing and Flowbite in Liferay

## Incorporating Tailwing Design System and Flowbite Components to Liferay

### Introduction 

Liferay has it's own design system based on Lexicon Design and Clay Components.
For regular use cases they can satisfy customer needs, and user interfaces can be built by using out-of-the-box components.
But for more sophisticated user interfaces an external design system might be needed.
TailwindCSS is one of the most popular CSS frameworks currently, and Flowbite is a components library based on TailwindCSS.
This articles shows how to incorporate TailwindCSS / Flowbite to Liferay 7.4.

### TailwindCSS & Flowbite Integration

#### Installation / Setup

Before integrating to Liferay you can install & configure TailwindCSS and Flowbite separately.

_Note: but do this inside Lifeary workspace for future integration._

Create a folder for TailwindCSS/Flowbite, e.g. `lifedev-flowbite-loader`.

**1. Install TailwindCSS**

TailwindCSS can be installed by leveraging Tailwind CLI, or can be included using external CDN file.
The Tailwind CLI way is a preferred one, and provides more enhanced configuration options.

To install TailwindCSS run these commands:

`npm install -D tailwindcss`

`npx tailwindcss init`

Files `package.json` and `tailwind.config.js` should be created.

_Note: see detailed installation instructions at [Get started with Tailwind CSS](https://tailwindcss.com/docs/installation)_

**2. Create Tailwind styles file**

Create `tailwind.css` file with the following directives

`@tailwind base;`

`@tailwind components;`

`@tailwind utilities;`

to include Tailwind's styles.

**3. Install Flowbite**

To install Flowbite run the following command:

`npm install -D flowbite`

**4. Install PostCSS and Autoprefixer**

`PostCSS` is required to transform CSS with JavaScript plugins, while `Autoprefixer` is a `PostCSS` for applying vendor prefixes for CSS properties.

Install them using `npm install -D postcss autoprefixer` command.

Also, create a `postcss.config.js` file for PostCSS configuration:

module.exports = {
    plugins: {
        tailwindcss: {},
        autoprefixer: {},
    }
}

**5. Adjust Tailwind configuration to use Flowbite plugin**

Add `require('flowbite/plugin')` to `plugins` array in `tailwind.config.js`.

**6. Verify Installation**

Add a build script in `package.json` to build CSS:

"scripts": {
    "build:css": "postcss tailwind.css -o output.css"
}

Install PostCSS CLI to be able to run PostCSS from command line:

`npm install -D postcss-cli`

Run build script: `npm run build:css`. As a result, output.css file should be created.

Create a sample HTML file to test installation: [index.html](modules/lifedev-flowbite-loader/index.html)

Copy some examples from Flowbite site and check appearance and behavior, sample:

![01-veirfy-installation.png](images/tailwind-flowbite/01-veirfy-installation.png)

_Note: Make sure to inslude dynamic components (e.g. Accordions) to verify JavaScript as well._ 

