//0.define
	
	ButtonList = {
		"area": {text: "面积图", img: "area.png"},
		"area_c": {text: "面积图C", img: "area_c.png"},
		"area_pa": {text: "面积图PA", img: "area_pa.png"},
		"area_r": {text: "面积图Ｒ", img: "area_r.png"},
		"bar": {text: "柱形图", img: "bar.png"},
		"bar_a": {text: "柱形图A", img: "bar_a.png"},
		"bar_c": {text: "柱形图C", img: "bar_c.png"},
		"bubble": {text: "汽泡图", img: "bubble.png"},
		"combine": {text: "组合图", img: "combine.png"},
		"combine_m": {text: "组合图M", img: "combine_m.png"},
		"dot": {text: "点图", img: "dot.png"},
		"funnel": {text: "图", img: "funnel.png"},
		"heat": {text: "热力图", img: "heat.png"},
		"line": {text: "折线图", img: "line.png"},
		"line_map": {text: "折线图M", img: "line_map.png"},
		"map_c": {text: "地图C", img: "map_c.png"},
		"map_g": {text: "地图G", img: "map_g.png"},
		"map_gis": {text: "地图GIS", img: "map_gis.png"},
		"map_s": {text: "地图Ｓ", img: "map_s.png"},
		"pie": {text: "饼图", img: "pie.png"},
		"multiPie": {text: "复合饼图", img: "multiPie.png"},
		"multiPieLighter": {text: "复合饼图L", img: "multiPieLighter.png"},
		"multiPieThicker": {text: "复合饼图T", img: "multiPieThicker.png"},
		"radar": {text: "雷达图", img: "radar.png"},
		"radar_a": {text: "雷达图A", img: "radar_a.png"},
		"rectTree": {text: "矩形树", img: "rectTree.png"},
		"wordCloud": {text: "文字云", img: "wordCloud.png"}
	};

//1.OlapTab                                        =========

	OlapTab = {
		items: {},
		
		init: function(items) {
			for (var i = 0; i < items.length; i++) {
				var option = items[i];
				
				var item = {code: option.code};
				
				item.header = $("#" + option.header);
				item.body = $("#" + option.body);
				
				if (i > 0) {
					item.body.hide();
				}
				
				this.items[option.code] = item;
				
				if (i == 0) {
					this.setHeaderActive(item.header, true);
				}
			}
		},
		
		changeActive: function(id) {
			for (var code in this.items) {
				var item = this.items[code];
				item.body.hide();
				this.setHeaderActive(item.header, false);
			}
			
			var item = this.items[id];
			item.body.show();
			this.setHeaderActive(item.header, true);
		},
		
		setHeaderActive: function(header, active) {
			if (active) {
				header.addClass("tab-avtiveHeader");
			}
			else {
				header.removeClass("tab-avtiveHeader");
			}
		}
	};
	
	
//2.Toolbar                                        =========
	
	Toolbar = $.fm.Toolbar = Control.subClass({
		items: {},
		element: null,
		
		init: function(options) {
			Control.call(this, options);
			
			if (!options.items) {
				return;
			}
			
			for (var i = 0; i< options.items.length; i++) {
				this.createOneItem(options.items[i]);
			}
		},
		
		createOneItem: function(itemOption) {
			if ("spliter" == itemOption.type) {
				this.createSpliter();
			}
			else if (this.btnTemplate) {
				this.createOneButton(itemOption, this.btnTemplate);
			}
			else {
				var template = [
					"<div class='tool-btn'>",
					"</div>"].join("");
				this.createOneButton(itemOption, template);
			}
		},
		
		createOneButton: function(option, template) {
			var me = this;
			
			//1.
			var btn = $(template);
			
			//2. image and icon
			if (option.img) {
				var image = $("#img", btn);
				
				if (!image.length) {
					image = $("<img class='tool-btn-img'>");
					btn.append(image);
				}
				
				image.attr("src", (this.imgPath || "") + option.img);
				btn.image = image;
			}
			else if (option.icon) {
				var icon = $("#icon", btn);
				
				if (!icon.length) {
					icon = $("<span class='tool-btn-icon iconfont'>");
					btn.append(icon);
				}
				
				icon.addClass(option.icon);
				btn.append(icon);
			}
			
			//3. text
			if (option.text) {
				var text = $("#text", btn);
				
				if (!text.length) {
					text = $("<div id='text' class='tool-btn-text'></div>");
					btn.append(text);
				}
				
				text.html(option.text);
				btn.text = text;
			}
			
			//4. click
			btn.click(function() {
				if (option.onClick) {
					option.onClick(option.code, btn);
				}
				else if (me.onClick) {
					me.onClick.call(me, option.code, btn);
				}
			});
			
			//5. 
			this.element.append(btn);
			this.items[option.code] = btn;
			
			return btn;
		},
		
		createSpliter: function() {
			var spliter = $([
				"<div class='tool-btn-spliter'>|</div>"].join(""));
			this.element.append(spliter);
		}
	});
	
	
//3.FieldArea                                        =========
	
	FieldArea = {
		items: {},
		element: null,
		
		init: function(options) {
			this.options = options;
			
			this.element = $(options.element);
			this.dimensionArea = $("#olap-fields-dimension", this.element);
			this.valueArea = $("#olap-fields-value", this.element);
			
			var dimensions = options.report.theme.dimension;
			this.appendDimensions(dimensions);
			
			var values = options.report.theme.value;
			this.appendValues(values);
		},
		
		appendDimensions: function(dimensions) {
			for (var i = 0; i < dimensions.length; i++) {
				var group = dimensions[i];
				
				var groupElement = $([
				  	"<div class='fields-group'>",
						"<div class='fields-group-header'>",
							"<span id='icon' class='iconfont' style='font-size:16px'></span>",
							"<span id='groupText' class='groupText'></span>",							
						"<div>",				  		
					"</div>"
				].join(""));
				
				groupElement.icon = $("#icon", groupElement);
				groupElement.icon.addClass(this.options.groupIcon);
				
				groupElement.text = $("#groupText", groupElement);
				groupElement.text.html(group.text);
				
				var fields = group.fields;
				for (var j = 0; j < fields.length; j++) {
					var fieldOption = fields[j];
					
					if (!fieldOption.group) {
						fieldOption.group = group;
					}
					
					var item = this.appendOneField(groupElement, fieldOption);
					item.group = group;
					item.type = "dimension";
				}
				
				this.dimensionArea.append(groupElement);
			}
		},
		
		appendValues: function(values) {
			for (var i = 0; i < values.length; i++) {
				var valueOption = values[i];
				var item = this.appendOneField(this.element, valueOption);
				item.type = "value";
				
				this.valueArea.append(item);
				this.items[valueOption.field] = item;
			}			
		},
		
		appendOneField: function(parent, fieldOption) {
			var item = $([
				"<div class='fields-item'>",
					"<label id='text'></label>",
					"<div id='checkBox' class='field-check'>",
						"<input type='checkbox'>",
					"<div>",
				"</div>"			
			].join(""));
			
			item.option = fieldOption;
			
			item.text = $("#text", item);
			item.text.html(fieldOption.caption);
			
			item.checkbox = $("#checkBox", item);
			item.checkbox.change(function() {
				var checked = item.checkbox.val();
				if (this.onChangeFilterSetting) {
					this.onChangeFilterSetting(fieldOption, checked);
				}
			});
			
			parent.append(item);
			this.items[fieldOption.field] = item;
			
			item.draggable({
				helper: function( event ) {
			        return $( "<div style='position: fixed; z-index:100; background-color: #dad2d2; width: 120px; height: 22px; text-align: center; line-height: 22px;'>" + fieldOption.caption + "</div>" );
			      },
			    containment: "window",
				revert: "invalid",
				scope: "axis",
				cursorAt: {left: 110, top: 15}
			});
			
			return item;
		}
	};
	
	
//3.FiltersArea                                        =========
		
	FilterArea = {
		items: {},
		groups: {},
		element: null,
		
		init: function(options) {
			this.options = options;
			
			this.element = $(options.element);
			this.createFilterItems(options.items);
		},
		
		createFilterItems: function(items) {
			for (var no in items) {
				var option = items[no].option;
				
				if ("value" == option.type) {
					continue;
				}

				var group = this.addGroup(option.group);
				if (!group) {
					continue;
				}
				this.addFilter(group, option);
			}
		},
		
		addGroup: function(option) {
			if (!option) {
				return;
			}
			
			var code = option.code;
			
			if (this.groups[code]) {
				return this.groups[code];
			}
			
			var group = $([
			  	"<div class='fields-group'>",
					"<div class='fields-group-header'>",
						"<span id='icon' class='iconfont' style='font-size:16px'></span>",
						"<span id='groupText' class='groupText'></span>",
					"<div>",				  		
				"</div>"
			].join(""));
			
			group.icon = $("#icon", group);
			group.icon.addClass(this.options.groupIcon);
			
			group.text = $("#groupText", group);
			group.text.html(option.text);
			
			this.element.append(group);
			this.groups[code] = group;
			return group;
		},
		
		addFilter: function(group, itemOption) {
			var item = $([
				"<div class='filter-line'>",
					"<div id='text' class='filter-label'></div>",
					"<div id='editorContainer' class='filter-edit'><input /></div>",
				"</div>"
			].join(""));
			
			item.text = $("#text", item);
			item.text.html(itemOption.caption + "：");
			
			item.editorContainer = $("#editorContainer", item);
			item.editor = ControlCreator.create({
				type: "text"
			}, item.editorContainer);
			
			this.items[itemOption.field] = item;
			group.append(item);
		},
		
		deleteFilter: function(itemOption) {
			var item = this.items[itemOption.field];
			if (item) {
				item.remove();
			}
		}
	};
	
//4.AxisArea                                        =========
	
	AxisArea = {
		items: {},
		buttons: {},
		element: null,
		
		init: function(options) {
			this.options = options;
			this.element = $(options.element);

            this.axisX1 = $("#axisX-1", this.element);
            this.axisX2 = $("#axisX-2", this.element);
            this.axisY1 = $("#axisY-1", this.element);
            this.axisY2 = $("#axisY-2", this.element);

            this.createButtons();
            this.createAxises(this.options.axises);

            //this.createDrapEvent();
		},

        createButtons: function() {
			this.createOneButton("axisY-add", {segments: ["y-segment1", "y-segment2"], axises: [this.axisY1, this.axisY2], operation: "expand"});
            this.createOneButton("axisY-delete", {segments: ["y-segment1", "y-segment2"], axises: [this.axisY1, this.axisY2], operation: "collapse"});
            this.createOneButton("axisX-add", {segments: ["x-segment1", "x-segment2"], axises: [this.axisX1, this.axisX2], operation: "expand"});
            this.createOneButton("axisX-delete", {segments: ["x-segment1", "x-segment2"], axises: [this.axisX1, this.axisX2], operation: "collapse"});
		},

		createOneButton: function(btnCode, option) {
            var btn = this.buttons[btnCode] = $("#" + btnCode, this.element);
            btn.click(function() {
				if ("expand" == option.operation) {
                    $("#" + option.segments[0]).width("50%");
                    $("#" + option.segments[1]).show();
				}
				else if ("collapse" == option.operation) {
                    this.transferFields("moveTo", option.axises[1], option.axises[0]);

                    $("#" + option.segments[0]).width("100%");
                    $("#" + option.segments[1]).hide();
                }
			});
		},

        transferFields: function(operation, from, to) {
			if ("move" == operation) {
				var items = from.items;
				for (var no in items) {
                    items[no].remove();
                    to.append(items[no]);
				}
                from.items = [];
			}
			else if ("exchange" == operation) {

			}
		},

        createAxises: function(axises) {
            var route = {
                x: {count: 0, axis: [this.axisX1, this.axisX2]},
                y: {count: 0, axis: [this.axisY1, this.axisY2]}
            };

            for (var i = 0; i < axises.length; i++) {
                var axisOption = axises[i];
                var type = axisOption.axis;

                var index = route[type];
                var axis = route[type].axis[index];
                route[type].count = route[type].count + 1;

                this.createOneAxis(axis, index, axisOption);

            }
        },

        createOneAxis: function(axis, index, option) {
            this.addAxisFields(axis, option.fields);
        },

       addAxisFields: function(axis, fields) {
            if (!fields) {
                return;
            }

            for (var i = 0; i < fields.length; i++) {
                this.addOneAxisField(axis, fields[i]);
            }
        },

        addOneAxisField: function(axis, option) {
            var me = this;

            var fieldItem = $([
                "<div class='axix-field'>",
                	"<div id='text'></div><span class='iconfont axix-field-close icon-guanbi' style='font-size: 10px'></span>",
                "</div>"
            ].join(""));

            var text = fieldItem.text = $("#text", fieldItem);
            text.html(option.caption || option.name);

            fieldItem.click(function() {
                if (me.options.onFieldClick) {
                    me.options.onFieldClick.call(me, option.name, option);
                }
            });

            axis.append(fieldItem);
            this.items[option.name] = fieldItem;
        },

        createDragEvent: function() {
            var me = this;

            this.axisX.droppable({
                scope: "axis",
                activeClass: "axis-hotBody",
                drop: function(event, ui) {
                    me.axisX.html(me.axisX.html() + ui.draggable.text().replace("：", "") + ";  ");
                }
            });

            this.axisY.droppable({
                scope: "axis",
                activeClass: "axis-hotBody",
                drop: function(event, ui) {
                    me.axisY.html(me.axisY.html() + ui.draggable.text().replace("：", "") + ";  ");
                }
            });
        }

    };
	
	
//5.Spliter                                        =========
	
	Spliter = {
		element: null,
		leftEl: null,
		rightEl: null,
		width: 4, 
		
		init: function(option) {
			var me = this;
			
			this.element = $(option.element);
			this.leftEl = $(option.left);
			this.rightEl = $(option.right);
			
			this.element.draggable({
				axis: "x",
				zIndex: 200,
				containment: "document",
				stop: function(event, ui) {
					var left = ui.position.left;
					me.reposition(left);
				}
    		});
		},
	
		reposition: function(left) {
			if (left <= 0) {
				left = 3;
				this.setElementLeft(this.element, left);
			}
			
			this.leftEl.width(left);
			this.setElementLeft(this.rightEl, left + this.width);
		},
		
		setElementLeft: function(el, left) {
			var offset = el.offset();
			offset.left = left;
			el.offset(offset);
		}
	};