'use strict';

/**
 * @ngdoc directive
 * @name influuntApp.directive:wizardSteps
 * @description
 * # wizardSteps
 */
angular.module('influuntApp')
  .directive('wizardSteps', function () {
    return {
      restrict: 'A',
      link: function postLink(scope, el) {
        scope.$watch(function() {
          return $(el).html();
        }, function(val) {
          if (val && val.match(/current/)) {
            var current = $(el).find('li.current');
            if (current.length > 0) {
              current.addClass('visited');
              current.nextAll(':not(.visited)').addClass('disabled');
              current.prevAll().andSelf().removeClass('disabled');
            }
          }
        });
      }
    };
  });
