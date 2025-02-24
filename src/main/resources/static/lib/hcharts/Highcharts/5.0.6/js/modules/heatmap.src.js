/**
 * @license Highcharts JS v5.0.6 (2016-12-07)
 *
 * (c) 2009-2016 Torstein Honsi
 *
 * License: www.highcharts.com/license
 */
(function(factory) {
    if (typeof module === 'object' && module.exports) {
        module.exports = factory;
    } else {
        factory(Highcharts);
    }
}(function(Highcharts) {
    (function(H) {
        /**
         * (c) 2010-2016 Torstein Honsi
         *
         * License: www.highcharts.com/license
         */
        'user strict';
        var Axis = H.Axis,
            Chart = H.Chart,
            color = H.color,
            ColorAxis,
            each = H.each,
            extend = H.extend,
            isNumber = H.isNumber,
            Legend = H.Legend,
            LegendSymbolMixin = H.LegendSymbolMixin,
            noop = H.noop,
            merge = H.merge,
            pick = H.pick,
            wrap = H.wrap;

        /**
         * The ColorAxis object for inclusion in gradient legends
         */
        ColorAxis = H.ColorAxis = function() {
            this.init.apply(this, arguments);
        };
        extend(ColorAxis.prototype, Axis.prototype);
        extend(ColorAxis.prototype, {
            defaultColorAxisOptions: {
                lineWidth: 0,
                minPadding: 0,
                maxPadding: 0,
                gridLineWidth: 1,
                tickPixelInterval: 72,
                startOnTick: true,
                endOnTick: true,
                offset: 0,
                marker: {
                    animation: {
                        duration: 50
                    },
                    width: 0.01

                },
                labels: {
                    overflow: 'justify'
                },
                minColor: '#e6ebf5',
                maxColor: '#003399',
                tickLength: 5,
                showInLegend: true
            },

            // Properties to preserve after destroy, for Axis.update (#5881)
            keepProps: ['legendGroup', 'legendItem', 'legendSymbol']
                .concat(Axis.prototype.keepProps),

            /**
             * Initialize the color axis
             */
            init: function(chart, userOptions) {
                var horiz = chart.options.legend.layout !== 'vertical',
                    options;

                this.coll = 'colorAxis';

                // Build the options
                options = merge(this.defaultColorAxisOptions, {
                    side: horiz ? 2 : 1,
                    reversed: !horiz
                }, userOptions, {
                    opposite: !horiz,
                    showEmpty: false,
                    title: null
                });

                Axis.prototype.init.call(this, chart, options);

                // Base init() pushes it to the xAxis array, now pop it again
                //chart[this.isXAxis ? 'xAxis' : 'yAxis'].pop();

                // Prepare data classes
                if (userOptions.dataClasses) {
                    this.initDataClasses(userOptions);
                }
                this.initStops(userOptions);

                // Override original axis properties
                this.horiz = horiz;
                this.zoomEnabled = false;

                // Add default values		
                this.defaultLegendLength = 200;
            },

            /*
             * Return an intermediate color between two colors, according to pos where 0
             * is the from color and 1 is the to color.
             * NOTE: Changes here should be copied
             * to the same function in drilldown.src.js and solid-gauge-src.js.
             */
            tweenColors: function(from, to, pos) {
                // Check for has alpha, because rgba colors perform worse due to lack of
                // support in WebKit.
                var hasAlpha,
                    ret;

                // Unsupported color, return to-color (#3920)
                if (!to.rgba.length || !from.rgba.length) {
                    ret = to.input || 'none';

                    // Interpolate
                } else {
                    from = from.rgba;
                    to = to.rgba;
                    hasAlpha = (to[3] !== 1 || from[3] !== 1);
                    ret = (hasAlpha ? 'rgba(' : 'rgb(') +
                        Math.round(to[0] + (from[0] - to[0]) * (1 - pos)) + ',' +
                        Math.round(to[1] + (from[1] - to[1]) * (1 - pos)) + ',' +
                        Math.round(to[2] + (from[2] - to[2]) * (1 - pos)) +
                        (hasAlpha ? (',' + (to[3] + (from[3] - to[3]) * (1 - pos))) : '') + ')';
                }
                return ret;
            },

            initDataClasses: function(userOptions) {
                var axis = this,
                    chart = this.chart,
                    dataClasses,
                    colorCounter = 0,
                    colorCount = chart.options.chart.colorCount,
                    options = this.options,
                    len = userOptions.dataClasses.length;
                this.dataClasses = dataClasses = [];
                this.legendItems = [];

                each(userOptions.dataClasses, function(dataClass, i) {
                    var colors;

                    dataClass = merge(dataClass);
                    dataClasses.push(dataClass);
                    if (!dataClass.color) {
                        if (options.dataClassColor === 'category') {

                            dataClass.colorIndex = colorCounter;

                            // increase and loop back to zero
                            colorCounter++;
                            if (colorCounter === colorCount) {
                                colorCounter = 0;
                            }
                        } else {
                            dataClass.color = axis.tweenColors(
                                color(options.minColor),
                                color(options.maxColor),
                                len < 2 ? 0.5 : i / (len - 1) // #3219
                            );
                        }
                    }
                });
            },

            initStops: function(userOptions) {
                this.stops = userOptions.stops || [
                    [0, this.options.minColor],
                    [1, this.options.maxColor]
                ];
                each(this.stops, function(stop) {
                    stop.color = color(stop[1]);
                });
            },

            /**
             * Extend the setOptions method to process extreme colors and color
             * stops.
             */
            setOptions: function(userOptions) {
                Axis.prototype.setOptions.call(this, userOptions);

                this.options.crosshair = this.options.marker;
            },

            setAxisSize: function() {
                var symbol = this.legendSymbol,
                    chart = this.chart,
                    legendOptions = chart.options.legend || {},
                    x,
                    y,
                    width,
                    height;

                if (symbol) {
                    this.left = x = symbol.attr('x');
                    this.top = y = symbol.attr('y');
                    this.width = width = symbol.attr('width');
                    this.height = height = symbol.attr('height');
                    this.right = chart.chartWidth - x - width;
                    this.bottom = chart.chartHeight - y - height;

                    this.len = this.horiz ? width : height;
                    this.pos = this.horiz ? x : y;
                } else {
                    // Fake length for disabled legend to avoid tick issues and such (#5205)
                    this.len = (this.horiz ? legendOptions.symbolWidth : legendOptions.symbolHeight) || this.defaultLegendLength;
                }
            },

            /**
             * Translate from a value to a color
             */
            toColor: function(value, point) {
                var pos,
                    stops = this.stops,
                    from,
                    to,
                    color,
                    dataClasses = this.dataClasses,
                    dataClass,
                    i;

                if (dataClasses) {
                    i = dataClasses.length;
                    while (i--) {
                        dataClass = dataClasses[i];
                        from = dataClass.from;
                        to = dataClass.to;
                        if ((from === undefined || value >= from) && (to === undefined || value <= to)) {
                            color = dataClass.color;
                            if (point) {
                                point.dataClass = i;
                                point.colorIndex = dataClass.colorIndex;
                            }
                            break;
                        }
                    }

                } else {

                    if (this.isLog) {
                        value = this.val2lin(value);
                    }
                    pos = 1 - ((this.max - value) / ((this.max - this.min) || 1));
                    i = stops.length;
                    while (i--) {
                        if (pos > stops[i][0]) {
                            break;
                        }
                    }
                    from = stops[i] || stops[i + 1];
                    to = stops[i + 1] || from;

                    // The position within the gradient
                    pos = 1 - (to[0] - pos) / ((to[0] - from[0]) || 1);

                    color = this.tweenColors(
                        from.color,
                        to.color,
                        pos
                    );
                }
                return color;
            },

            /**
             * Override the getOffset method to add the whole axis groups inside the legend.
             */
            getOffset: function() {
                var group = this.legendGroup,
                    sideOffset = this.chart.axisOffset[this.side];

                if (group) {

                    // Hook for the getOffset method to add groups to this parent group
                    this.axisParent = group;

                    // Call the base
                    Axis.prototype.getOffset.call(this);

                    // First time only
                    if (!this.added) {

                        this.added = true;

                        this.labelLeft = 0;
                        this.labelRight = this.width;
                    }
                    // Reset it to avoid color axis reserving space
                    this.chart.axisOffset[this.side] = sideOffset;
                }
            },

            /**
             * Create the color gradient
             */
            setLegendColor: function() {
                var grad,
                    horiz = this.horiz,
                    options = this.options,
                    reversed = this.reversed,
                    one = reversed ? 1 : 0,
                    zero = reversed ? 0 : 1;

                grad = horiz ? [one, 0, zero, 0] : [0, zero, 0, one]; // #3190
                this.legendColor = {
                    linearGradient: {
                        x1: grad[0],
                        y1: grad[1],
                        x2: grad[2],
                        y2: grad[3]
                    },
                    stops: options.stops || [
                        [0, options.minColor],
                        [1, options.maxColor]
                    ]
                };
            },

            /**
             * The color axis appears inside the legend and has its own legend symbol
             */
            drawLegendSymbol: function(legend, item) {
                var padding = legend.padding,
                    legendOptions = legend.options,
                    horiz = this.horiz,
                    width = pick(legendOptions.symbolWidth, horiz ? this.defaultLegendLength : 12),
                    height = pick(legendOptions.symbolHeight, horiz ? 12 : this.defaultLegendLength),
                    labelPadding = pick(legendOptions.labelPadding, horiz ? 16 : 30),
                    itemDistance = pick(legendOptions.itemDistance, 10);

                this.setLegendColor();

                // Create the gradient
                item.legendSymbol = this.chart.renderer.rect(
                    0,
                    legend.baseline - 11,
                    width,
                    height
                ).attr({
                    zIndex: 1
                }).add(item.legendGroup);

                // Set how much space this legend item takes up
                this.legendItemWidth = width + padding + (horiz ? itemDistance : labelPadding);
                this.legendItemHeight = height + padding + (horiz ? labelPadding : 0);
            },
            /**
             * Fool the legend
             */
            setState: noop,
            visible: true,
            setVisible: noop,
            getSeriesExtremes: function() {
                var series;
                if (this.series.length) {
                    series = this.series[0];
                    this.dataMin = series.valueMin;
                    this.dataMax = series.valueMax;
                }
            },
            drawCrosshair: function(e, point) {
                var plotX = point && point.plotX,
                    plotY = point && point.plotY,
                    crossPos,
                    axisPos = this.pos,
                    axisLen = this.len;

                if (point) {
                    crossPos = this.toPixels(point[point.series.colorKey]);
                    if (crossPos < axisPos) {
                        crossPos = axisPos - 2;
                    } else if (crossPos > axisPos + axisLen) {
                        crossPos = axisPos + axisLen + 2;
                    }

                    point.plotX = crossPos;
                    point.plotY = this.len - crossPos;
                    Axis.prototype.drawCrosshair.call(this, e, point);
                    point.plotX = plotX;
                    point.plotY = plotY;

                    if (this.cross) {
                        this.cross
                            .addClass('highcharts-coloraxis-marker')
                            .add(this.legendGroup);



                    }
                }
            },
            getPlotLinePath: function(a, b, c, d, pos) {
                return isNumber(pos) ? // crosshairs only // #3969 pos can be 0 !!
                    (this.horiz ? ['M', pos - 4, this.top - 6, 'L', pos + 4, this.top - 6, pos, this.top, 'Z'] : ['M', this.left, pos, 'L', this.left - 6, pos + 6, this.left - 6, pos - 6, 'Z']) :
                    Axis.prototype.getPlotLinePath.call(this, a, b, c, d);
            },

            update: function(newOptions, redraw) {
                var chart = this.chart,
                    legend = chart.legend;

                each(this.series, function(series) {
                    series.isDirtyData = true; // Needed for Axis.update when choropleth colors change
                });

                // When updating data classes, destroy old items and make sure new ones are created (#3207)
                if (newOptions.dataClasses && legend.allItems) {
                    each(legend.allItems, function(item) {
                        if (item.isDataClass) {
                            item.legendGroup.destroy();
                        }
                    });
                    chart.isDirtyLegend = true;
                }

                // Keep the options structure updated for export. Unlike xAxis and yAxis, the colorAxis is
                // not an array. (#3207)
                chart.options[this.coll] = merge(this.userOptions, newOptions);

                Axis.prototype.update.call(this, newOptions, redraw);
                if (this.legendItem) {
                    this.setLegendColor();
                    legend.colorizeItem(this, true);
                }
            },

            /**
             * Get the legend item symbols for data classes
             */
            getDataClassLegendSymbols: function() {
                var axis = this,
                    chart = this.chart,
                    legendItems = this.legendItems,
                    legendOptions = chart.options.legend,
                    valueDecimals = legendOptions.valueDecimals,
                    valueSuffix = legendOptions.valueSuffix || '',
                    name;

                if (!legendItems.length) {
                    each(this.dataClasses, function(dataClass, i) {
                        var vis = true,
                            from = dataClass.from,
                            to = dataClass.to;

                        // Assemble the default name. This can be overridden by legend.options.labelFormatter
                        name = '';
                        if (from === undefined) {
                            name = '< ';
                        } else if (to === undefined) {
                            name = '> ';
                        }
                        if (from !== undefined) {
                            name += H.numberFormat(from, valueDecimals) + valueSuffix;
                        }
                        if (from !== undefined && to !== undefined) {
                            name += ' - ';
                        }
                        if (to !== undefined) {
                            name += H.numberFormat(to, valueDecimals) + valueSuffix;
                        }
                        // Add a mock object to the legend items
                        legendItems.push(extend({
                            chart: chart,
                            name: name,
                            options: {},
                            drawLegendSymbol: LegendSymbolMixin.drawRectangle,
                            visible: true,
                            setState: noop,
                            isDataClass: true,
                            setVisible: function() {
                                vis = this.visible = !vis;
                                each(axis.series, function(series) {
                                    each(series.points, function(point) {
                                        if (point.dataClass === i) {
                                            point.setVisible(vis);
                                        }
                                    });
                                });

                                chart.legend.colorizeItem(this, vis);
                            }
                        }, dataClass));
                    });
                }
                return legendItems;
            },
            name: '' // Prevents 'undefined' in legend in IE8
        });

        /**
         * Handle animation of the color attributes directly
         */
        each(['fill', 'stroke'], function(prop) {
            H.Fx.prototype[prop + 'Setter'] = function() {
                this.elem.attr(
                    prop,
                    ColorAxis.prototype.tweenColors(
                        color(this.start),
                        color(this.end),
                        this.pos
                    ),
                    null,
                    true
                );
            };
        });

        /**
         * Extend the chart getAxes method to also get the color axis
         */
        wrap(Chart.prototype, 'getAxes', function(proceed) {

            var options = this.options,
                colorAxisOptions = options.colorAxis;

            proceed.call(this);

            this.colorAxis = [];
            if (colorAxisOptions) {
                new ColorAxis(this, colorAxisOptions); // eslint-disable-line no-new
            }
        });


        /**
         * Wrap the legend getAllItems method to add the color axis. This also removes the
         * axis' own series to prevent them from showing up individually.
         */
        wrap(Legend.prototype, 'getAllItems', function(proceed) {
            var allItems = [],
                colorAxis = this.chart.colorAxis[0];

            if (colorAxis && colorAxis.options) {
                if (colorAxis.options.showInLegend) {
                    // Data classes
                    if (colorAxis.options.dataClasses) {
                        allItems = allItems.concat(colorAxis.getDataClassLegendSymbols());
                        // Gradient legend
                    } else {
                        // Add this axis on top
                        allItems.push(colorAxis);
                    }
                }

                // Don't add the color axis' series
                each(colorAxis.series, function(series) {
                    series.options.showInLegend = false;
                });
            }

            return allItems.concat(proceed.call(this));
        });

        wrap(Legend.prototype, 'colorizeItem', function(proceed, item, visible) {
            proceed.call(this, item, visible);
            if (visible && item.legendColor) {
                item.legendSymbol.attr({
                    fill: item.legendColor
                });
            }
        });

    }(Highcharts));
    (function(H) {
        /**
         * (c) 2010-2016 Torstein Honsi
         *
         * License: www.highcharts.com/license
         */
        'user strict';
        var defined = H.defined,
            each = H.each,
            noop = H.noop,
            seriesTypes = H.seriesTypes;

        /**
         * Mixin for maps and heatmaps
         */
        H.colorPointMixin = {
            /**
             * Color points have a value option that determines whether or not it is a null point
             */
            isValid: function() {
                return this.value !== null;
            },

            /**
             * Set the visibility of a single point
             */
            setVisible: function(vis) {
                var point = this,
                    method = vis ? 'show' : 'hide';

                // Show and hide associated elements
                each(['graphic', 'dataLabel'], function(key) {
                    if (point[key]) {
                        point[key][method]();
                    }
                });
            },
            setState: function(state) {
                H.Point.prototype.setState.call(this, state);
                if (this.graphic) {
                    this.graphic.attr({
                        zIndex: state === 'hover' ? 1 : 0
                    });
                }
            }
        };

        H.colorSeriesMixin = {
            pointArrayMap: ['value'],
            axisTypes: ['xAxis', 'yAxis', 'colorAxis'],
            optionalAxis: 'colorAxis',
            trackerGroups: ['group', 'markerGroup', 'dataLabelsGroup'],
            getSymbol: noop,
            parallelArrays: ['x', 'y', 'value'],
            colorKey: 'value',



            /**
             * In choropleth maps, the color is a result of the value, so this needs translation too
             */
            translateColors: function() {
                var series = this,
                    nullColor = this.options.nullColor,
                    colorAxis = this.colorAxis,
                    colorKey = this.colorKey;

                each(this.data, function(point) {
                    var value = point[colorKey],
                        color;

                    color = point.options.color ||
                        (point.isNull ? nullColor : (colorAxis && value !== undefined) ? colorAxis.toColor(value, point) : point.color || series.color);

                    if (color) {
                        point.color = color;
                    }
                });
            },

            /**
             * Get the color attibutes to apply on the graphic
             */
            colorAttribs: function(point) {
                var ret = {};
                if (defined(point.color)) {
                    ret[this.colorProp || 'fill'] = point.color;
                }
                return ret;
            }
        };

    }(Highcharts));
    (function(H) {
        /**
         * (c) 2010-2016 Torstein Honsi
         *
         * License: www.highcharts.com/license
         */
        'user strict';
        var colorPointMixin = H.colorPointMixin,
            colorSeriesMixin = H.colorSeriesMixin,
            each = H.each,
            LegendSymbolMixin = H.LegendSymbolMixin,
            merge = H.merge,
            noop = H.noop,
            pick = H.pick,
            Series = H.Series,
            seriesType = H.seriesType,
            seriesTypes = H.seriesTypes;

        // The Heatmap series type
        seriesType('heatmap', 'scatter', {
            animation: false,
            borderWidth: 0,

            dataLabels: {
                formatter: function() { // #2945
                    return this.point.value;
                },
                inside: true,
                verticalAlign: 'middle',
                crop: false,
                overflow: false,
                padding: 0 // #3837
            },
            marker: null,
            pointRange: null, // dynamically set to colsize by default
            tooltip: {
                pointFormat: '{point.x}, {point.y}: {point.value}<br/>'
            },
            states: {
                normal: {
                    animation: true
                },
                hover: {
                    halo: false, // #3406, halo is not required on heatmaps
                    brightness: 0.2
                }
            }
        }, merge(colorSeriesMixin, {
            pointArrayMap: ['y', 'value'],
            hasPointSpecificOptions: true,
            supportsDrilldown: true,
            getExtremesFromAll: true,
            directTouch: true,

            /**
             * Override the init method to add point ranges on both axes.
             */
            init: function() {
                var options;
                seriesTypes.scatter.prototype.init.apply(this, arguments);

                options = this.options;
                options.pointRange = pick(options.pointRange, options.colsize || 1); // #3758, prevent resetting in setData
                this.yAxis.axisPointRange = options.rowsize || 1; // general point range
            },
            translate: function() {
                var series = this,
                    options = series.options,
                    xAxis = series.xAxis,
                    yAxis = series.yAxis,
                    between = function(x, a, b) {
                        return Math.min(Math.max(a, x), b);
                    };

                series.generatePoints();

                each(series.points, function(point) {
                    var xPad = (options.colsize || 1) / 2,
                        yPad = (options.rowsize || 1) / 2,
                        x1 = between(Math.round(xAxis.len - xAxis.translate(point.x - xPad, 0, 1, 0, 1)), -xAxis.len, 2 * xAxis.len),
                        x2 = between(Math.round(xAxis.len - xAxis.translate(point.x + xPad, 0, 1, 0, 1)), -xAxis.len, 2 * xAxis.len),
                        y1 = between(Math.round(yAxis.translate(point.y - yPad, 0, 1, 0, 1)), -yAxis.len, 2 * yAxis.len),
                        y2 = between(Math.round(yAxis.translate(point.y + yPad, 0, 1, 0, 1)), -yAxis.len, 2 * yAxis.len);

                    // Set plotX and plotY for user in K-D-Tree and more
                    point.plotX = point.clientX = (x1 + x2) / 2;
                    point.plotY = (y1 + y2) / 2;

                    point.shapeType = 'rect';
                    point.shapeArgs = {
                        x: Math.min(x1, x2),
                        y: Math.min(y1, y2),
                        width: Math.abs(x2 - x1),
                        height: Math.abs(y2 - y1)
                    };
                });

                series.translateColors();
            },
            drawPoints: function() {
                seriesTypes.column.prototype.drawPoints.call(this);

                each(this.points, function(point) {

                    // In styled mode, user CSS, otherwise the fill used in the style
                    // sheet will take precesence over the fill attribute.
                    point.graphic.css(this.colorAttribs(point));

                }, this);
            },
            animate: noop,
            getBox: noop,
            drawLegendSymbol: LegendSymbolMixin.drawRectangle,
            alignDataLabel: seriesTypes.column.prototype.alignDataLabel,
            getExtremes: function() {
                // Get the extremes from the value data
                Series.prototype.getExtremes.call(this, this.valueData);
                this.valueMin = this.dataMin;
                this.valueMax = this.dataMax;

                // Get the extremes from the y data
                Series.prototype.getExtremes.call(this);
            }

        }), colorPointMixin);

    }(Highcharts));
}));
