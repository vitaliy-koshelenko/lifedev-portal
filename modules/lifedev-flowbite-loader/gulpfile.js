const gulp = require('gulp');
const postcss = require('gulp-postcss');
var cleanCSS = require('gulp-clean-css');
const uglify = require('gulp-uglify');
const concat = require('gulp-concat');
const tailwindcss = require('tailwindcss');
const autoprefixer = require('autoprefixer');
const rename = require('gulp-rename');
const fs = require("fs");

const config = {
    css: {
        tailwind: 'tailwind.css',
        flowbite: 'node_modules/flowbite/dist/flowbite.css',
        target: 'build.min.css',
        tempTarget: 'flowbite-temp.min.css'  // Temporary file for Flowbite CSS
    },
    js: {
        src: 'node_modules/flowbite/dist/flowbite.js',
        target: 'build.min.js'
    },
    destination: 'src/main/resources/META-INF/resources'
}

// ---------------------- Flowbite CSS ----------------------
function buildFlowbiteCss() {
    return gulp.src(config.css.flowbite)    // Source File: Flowbite CSS
        .pipe(postcss([
            autoprefixer
        ]))                               // Process with PostCSS
        .pipe(cleanCSS())                 // Minify CSS
        .pipe(rename(config.css.tempTarget)) // Rename to temporary target file
        .pipe(gulp.dest(config.destination)); // Write to destination
}

// ---------------------- Tailwind CSS ----------------------
function buildTailwindCss() {
    return gulp.src(config.css.tailwind)   // Source File: Tailwind CSS
        .pipe(postcss([
            tailwindcss,
            autoprefixer
        ]))                               // Process with PostCSS
        .pipe(cleanCSS())                 // Minify CSS
        .pipe(gulp.dest(config.destination)); // Write to destination directory
}

// ---------------------- Concatenate Flowbite and Tailwind CSS ----------------------
function concatCss() {
    return gulp.src([`${config.destination}/${config.css.tempTarget}`, `${config.destination}/${config.css.tailwind}`]) // Source Files: Temp Flowbite CSS and Tailwind CSS
        .pipe(concat(config.css.target))  // Concatenate to target file
        .pipe(gulp.dest(config.destination)) // Write to destination
        .on('end', function() {
            // Optional: Delete the temporary file after concatenation
            fs.unlinkSync(`${config.destination}/${config.css.tempTarget}`);
            fs.unlinkSync(`${config.destination}/${config.css.tailwind}`);
        });
}

// ---------------------- Flowbite JS ----------------------
function buildJs() {
    return gulp.src(config.js.src)            // Source File: Flowbite JS
        .pipe(concat(config.js.target))       // Target File Name
        .pipe(uglify())                       // Minify JS
        .pipe(gulp.dest(config.destination)); // Write to destination
}

// Default Build Task
gulp.task('build', gulp.parallel(
        gulp.series(buildFlowbiteCss, buildTailwindCss, concatCss),
        buildJs
    ));
