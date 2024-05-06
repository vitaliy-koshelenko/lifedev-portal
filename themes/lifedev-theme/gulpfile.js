'use strict';

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