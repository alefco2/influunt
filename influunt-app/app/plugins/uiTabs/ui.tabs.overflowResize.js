/*!
 * Copyright (c) 2015 Adam Jimenez
 *
 * Dual licensed under the MIT (MIT_LICENSE.txt)
 * and GPL (GPL_LICENSE.txt) licenses
 *
 * https://github.com/adamjimenez/ui.tabs.overflowResize
 */

(function($) {

//  overridden ui.tabs functions
var uiTabsFuncs = {
	option: $.ui.tabs.prototype.option,
	_ui: $.ui.tabs.prototype._ui,
};

uiTabsFuncs = $.extend(
	uiTabsFuncs,
	{
		add: $.ui.tabs.prototype.add,
		remove: $.ui.tabs.prototype.remove
	}
);

$.extend($.ui.tabs.prototype, {
	overflowResize: function(options) {
		var self = this, initialized = false, buttonWidth, containerWidth, resizeTimer = null, hover = false;

		// initialize overflow
		function init() {
			destroy();
			$(window).on('resize', resize);
			$(self._getList()).on('mouseover', mouseenter);
			$(self._getList()).on('mouseout', mouseout);
			initialized = true;
			resize();
		}

		function destroy() {
			$(window).off('resize', resize);
			$(self._getList()).off('mouseenter', mouseenter);
			$(self._getList()).off('mouseout', mouseout);
			initialized = false;
		}

		function doResize(animate) {
			//console.log('resize overflow');

			// get button width
			var totalButtonWidth = 0;
			self.tabs.each(function(i) {
				var tab = self.tabs.eq(i);

				if (tab.hasClass('button')) {
					totalButtonWidth += tab.outerWidth(true);
				}
			});

			// calc new widths
			var containerWidth = parseFloat(window.getComputedStyle(self._getList()[0]).width);
			var item = self._getList().children(':not(.button)');

			if(!item.length)
				return;

			var totalMargin = (item[0].getBoundingClientRect().width - parseFloat(window.getComputedStyle(item[0]).width)) * self.tabs.length;
			var availableWidth = containerWidth - totalButtonWidth - totalMargin;
			var tabMaxWidth = availableWidth / item.length;
			var css = {'max-width' : tabMaxWidth};

			if(animate) {
				self._getList().children(':not(.button)').animate(css, 'fast'); //subtract padding between tabs
			} else {
				self._getList().children(':not(.button)').css(css);
			}
		}

		function mouseenter() {
			hover = true;

			if (resizeTimer) clearTimeout(resizeTimer);
		}

		function mouseout() {
			hover = false;

			if (resizeTimer) clearTimeout(resizeTimer);
			resizeTimer = setTimeout(function(){ doResize(true) }, 500);
		}

		function resize(e, animate) {
			if (resizeTimer) clearTimeout(resizeTimer);
			resizeTimer = setTimeout(function(){ doResize(animate) }, 50);
		}

		self._ui = function( tab, panel ) {
			return {
				tab: tab,
				panel: panel,
				index: this.anchors.index( $(tab).find('a.ui-tabs-anchor') )
			};
		};

		// temporarily remove overflow buttons before adding a tab
		self.add = function(name, canAddTabs) {
			var newTab = false;

			if (!name) {
				name = 'New tab';
				newTab = true;
			}

			var ul = self._getList();
			var tabIndex = ul.children('li:not(.addTab)').size();
			var li = $( '<li><a href="#tabs-1" class="closable" role="presentation">'+name+'</a><span class="badge badge-danger badge-notification absolute" ng-show="tabHasError('+tabIndex+')"><i class="fa fa-exclamation"></i></span></li>' )

			var canAddTabs = $(ul).children('li.addTab').size() > 0;
			if (canAddTabs) {
				li = li.insertBefore( $(ul).children('li.addTab') );
			} else {
				ul.append(li);
			}

			li.uniqueId();

			if(newTab)
				li.attr('data-newtab', 1);

			var index = li.index();

			this.refresh(this);
			doResize(true);

			this.option( "active", index );
			this._trigger( "add", null, this._ui( this.tabs[ index ], this.panels[ index ] ) );

			return li;
		};

		self.remove = function(index, preventCallback) {
			if (!preventCallback) {
				var result = this._trigger( "beforeRemove", null, this._ui( this.tabs[ index ], this.panels[ index ] ) );

				if(result === false) {
					return;
				}
			}

			index = this._getIndex( index );
			var options = this.options,
				tab = this.tabs.eq( index ).remove();
			// var panel = this._getPanelForTab( tab ).remove();

			// If selected tab was removed focus tab to the right or
			// in case the last tab was removed the tab to the left.
			// We check for more than 2 tabs, because if there are only 2,
			// then when we remove this tab, there will only be one tab left
			// so we don't need to detect which tab to activate.
			if ( tab.hasClass( "ui-tabs-active" ) && this.anchors.length > 2 ) {
				var isRemovingTab = true;
				this._activate( index + ( index + 1 < this.anchors.length ? 1 : -1 ), isRemovingTab );
			}

			this.refresh(isRemovingTab);

			if(!hover)
				doResize();

			this._trigger( "remove", null, this._ui( this.tabs[ index ], this.panels[ index ] ) );

			return this;
		};

		self.activate = function(index) {
			this.option( "active", index );
		};

		self.getPanelForTab = function( tab ) {
			return this._getPanelForTab( tab );
		};

		init();
	}
});

})(jQuery);
