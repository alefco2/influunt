'use strict';

/**
 * @ngdoc directive
 * @name influuntApp.directive:influuntKnob
 * @description
 * # influuntKnob
 */
angular.module('influuntApp')
  .directive('influuntKnob', ['$timeout',
    function ($timeout) {
      var changeTimeout = null;

      var getSafeIntegerValue = function(value, min) {
        value = parseInt(value);
        return _.isSafeInteger(value) ? parseInt(value) : parseInt(min) || 0;
      };

      /**
       * On change event for knob jquery plugin.
       * It will debounce the ng-model value update.
       *
       * @param      {<type>}  value   The value
       * @param      {<type>}  scope   The scope
       */
      var onChange = function(dial, value, scope) {
        $timeout.cancel(changeTimeout);
        changeTimeout = $timeout(function() {
          value = getSafeIntegerValue(value, scope.min);
          value = (value >= scope.min) ? value : scope.min;
          value = (value <= scope.max) ? value : scope.max;

          value = parseInt(value);
          scope.ngModel = value;
          dial.val(value).trigger('change');

          scope.ngModel = parseInt(scope.ngModel);
        }, 200);
      };

      return {
        templateUrl: 'views/directives/influunt-knob.html',
        restrict: 'E',
        scope: {
          title: '@',
          label: '@',
          min: '=',
          max: '=',
          ngModel: '='
        },
        link: function postLink(scope, element) {
          scope.min = scope.min || 0;

          var dial = $(element).find('.dial');
          dial.knob({
            max: scope.max,
            change: function(v) {
              onChange(dial, v, scope);
            }
          });

          dial.on('change', function() {
            var value = getSafeIntegerValue($(this).val(), scope.min);
            // var value = parseInt($(this).val());
            var $element = $(element);
            var hidden = $element.find('input.previous-value');
            if (hidden.length === 0) {
              hidden = $('<input type="hidden" class="previous-value" />');
              hidden.val(value);
              $element.append(hidden);
              return onChange(dial, value, scope);
            } else {
              if (parseInt(hidden.val()) !== parseInt(value)) {
                hidden.val(value);
                return onChange(dial, value, scope);
              }
            }

            scope.ngModel = parseInt(scope.ngModel);
          });

          scope.$watch('ngModel', function(value) {
            value = getSafeIntegerValue(value, scope.min);
            dial.val(angular.isDefined(value) ? value : scope.min).trigger('change');
            scope.ngModel = parseInt(scope.ngModel);
          });
        }
      };
    }]);
