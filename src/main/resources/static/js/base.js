/* =========================================================================
   base.js - entrance animations, loaded on every page.

   Progressive enhancement, in this order:
     1. Content is visible by default. CSS only hides it once this script has
        added .js-reveal to <html>. No JS -> nothing is ever hidden.
     2. prefers-reduced-motion: reduce -> the class is never added at all.
     3. No IntersectionObserver support -> everything is revealed immediately.

   Button hover growth is NOT here. It is pure CSS in base.css, so it keeps
   working even if this file fails to load.
   ========================================================================= */

(function () {
    'use strict';

    var HERO_SELECTOR = '[data-hero]';
    var SECTION_SELECTOR = '[data-reveal]';
    var STEP_SELECTOR = '[data-reveal-step]';
    var VISIBLE_CLASS = 'is-visible';

    var root = document.documentElement;

    var prefersReducedMotion =
        window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    // Bail out before hiding anything at all. Someone who asked their OS for
    // less motion gets the finished page, not an animated one.
    if (prefersReducedMotion) {
        return;
    }

    // From this point on CSS is allowed to hide the animated elements, because
    // the code that brings them back is guaranteed to be running.
    root.classList.add('js-reveal');

    function reveal(element) {
        element.classList.add(VISIBLE_CLASS);
    }

    function revealAll(elements) {
        Array.prototype.forEach.call(elements, reveal);
    }

    document.addEventListener('DOMContentLoaded', function () {
        var heroes = document.querySelectorAll(HERO_SELECTOR);
        var targets = document.querySelectorAll(SECTION_SELECTOR + ',' + STEP_SELECTOR);

        // The hero is above the fold on every page: fade it in on load rather
        // than waiting for a scroll that may never happen. One frame of delay
        // lets the browser paint the hidden state first, so the transition runs.
        requestAnimationFrame(function () {
            requestAnimationFrame(function () {
                revealAll(heroes);
            });
        });

        if (!('IntersectionObserver' in window)) {
            revealAll(targets);
            return;
        }

        var observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (!entry.isIntersecting) {
                    return;
                }
                reveal(entry.target);
                // One-way animation: once something has appeared it stays put.
                // Re-animating on every scroll past is distracting, not elegant.
                observer.unobserve(entry.target);
            });
        }, {
            threshold: 0.12,
            rootMargin: '0px 0px -8% 0px'
        });

        Array.prototype.forEach.call(targets, function (target) {
            observer.observe(target);
        });
    });
})();
