# Tailwind Design System Integration

## Create Liferay Theme

Navigate to `themes` directory.
Run `yo liferay-theme` command. Answer to wizard questions, and wait until theme is generated.

## Install Dependencies

_Note: run commands specified below from the theme's directory_

### gulp-postcss

The [gulp-postcss](https://www.npmjs.com/package/gulp-postcss) is a Gulp plugin that allows you to use [PostCSS](https://github.com/postcss/postcss) - a tool for transforming styles with JavaScript plugins.
It can be installed using the following command:

    npm install --save-dev gulp-postcss@^8

After gulp-postcss installation it can be used inside Gulp tasks to apply PostCSS transformations based on Tailwind configuration file.

### tailwindcss

[Tailwind CSS](https://tailwindcss.com/) can be installed as [tailwindcss](https://www.npmjs.com/package/tailwindcss) npm dependency:

    npm install tailwindcss@npm:@tailwindcss/postcss7-compat

### postcss and autoprefixer

Install [postcss](https://www.npmjs.com/package/postcss) and [autoprefixer](https://www.npmjs.com/package/autoprefixer) packages:
    
    npm install postcss@^7
    npm install autoprefixer@^9

for CSS styles transformation with Javascript and for adding vendor prefixes ( -webkit-, -moz- etc. ) to CSS properties, accordingly.

Todo: check/remove

    npm install tailwindcss@npm:@tailwindcss/postcss7-compat postcss@^7 autoprefixer@^9
    npm install tailwindcss@latest postcss@latest autoprefixer@latest

### flowbite

[Flowbite](https://flowbite.com/) can be installed as [flowbite](https://www.npmjs.com/package/flowbite) npm dependency:

    npm install flowbite


npm audit fix --force

## Tailwind and Flowbite Configuration

### Define Tailwind Configuration Files

Run the following command from the theme's directory:

    npx tailwindcss init -p

to create a fresh new Tailwind configuration file. `tailwind.config.js` and `postcss.config.js` files should be created.

### Adjust postcss.config.js file

Add a reference to `tailwind.config.js` file. The resulting `postcss.config.js` is the following:

    `module.exports = {
        plugins: {
            tailwindcss: {
                config: './tailwind.config.js'
            },
            autoprefixer: {},
        }
    }`

### Adjust tailwind.config.js file

Add `flowbite/plugin` to the list of plugins. The resulting `tailwind.config.js` is the following:

    `module.exports = {
        content: [],
        theme: {
            extend: {},
        },
        plugins: [
            require('flowbite/plugin')
        ],
    }`

## Import Tailwind CSS Files

Add the following import statements:

    @import 'tailwindcss/base';
    @import 'tailwindcss/components';
    @import 'tailwindcss/utilities';

to theme's `_custom.scss` file.

## Gulp Build Process Customization

Customize `gulpfile.js`:

    const gulp = require('gulp');
    const liferayThemeTasks = require('liferay-theme-tasks');
    const postcss = require('gulp-postcss');
    const tailwindcss = require('tailwindcss');
    
    liferayThemeTasks.registerTasks({
    
        gulp,
    
        hookFn: function (gulp) {
    
            gulp.hook('before:build:src', function (done) {
                // Copy flowbite.min.js from node_modules
                gulp.src('node_modules/flowbite/dist/flowbite.min.js')
                    .pipe(gulp.dest('./src/js/'))
                    .on('end', done);
            });
    
            gulp.hook('before:build:css', function (done) {
                // Apply Tailwind Config
                gulp.src('src/css/**/*.css')
                    .pipe(sass().on('error', sass.logError))
                    .pipe(postcss([
                        tailwindcss('./tailwind.config.js'),
                        require('autoprefixer'),
                    ]))
                    .pipe(gulp.dest('build/css'))
                    .on('end', done);
            });
        }
    });

## Add Tailwind Javascript file

Add Javascript import statement

    <script src="${theme_display.getPathThemeRoot()}/js/flowbite.min.js"></script>

inside the `<head>` tag in `portal_normal.ftl` file.


## Configure Package Manager

Liferay uses `yarn` package manager ny default. Change it to `npm` by specifying 

`liferay.workspace.node.package.manager=npm`

in `gradle.properties` of the Liferay Gradle Workspace.

## Deploy Theme

Run `npm install` and then `gulp deploy` from the theme's directory.