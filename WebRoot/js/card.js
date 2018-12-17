(function ($) {
	
	Card = $.fm.Card = Control.subClass({
		template: [
		    '<table class="datacard">',  
			'</table>'
		],
		template_title: [
		    '<tr>',
		    	'<td id="title_body" class="datacard_group">',
		    		'<div class="datacard_title"><img class="datacard_title_img" src="../../../image/detail.png"><label id="lbl_title"></label></div>',
		    	'</td>',
		    '</tr>'
		],
		template_line: '<tr></tr>',
		template_line_validate: '<tr><td id="cell" class="datacard_group"><div></div></td></tr>',
		template_key: '<th class="datacard_cell datacard_key"></th>',
		template_value: [
		    '<td class="datacard_cell datacard_value">',
		    	'<div style="position: relative; width: 100%; height: 100%">',
		    		'<div id="value" class="datacard_value_editor"></div>',
		    		'<div id="validator" class="Validform_checktip datacard_value_validator"></div>',
		    	'</div>',
		    '</td>'
		    ],
		template_validate: '<td><label class="Validform_checktip"></label></td>',
		template_editor: '<input>', 
		template_select: '<select></select>',
		template_select_option: '<option></option>',
		    		
		init: function(options) {
			this.linkElement = null;
			this.editClasss = ["input", "textarea"];
			this.editable = false;
			this.columns = [];
			this.editors = {};
			this.tiptype = 2;
			this.columnCount = 1;
			this.cellCount = 0;
			this.columnPosition = 0;
			this.currentLine = null;
			this.dataObject = new DataObject();
			
			Control.call(this, options);
			
			this.createElements();
    		if (this.linkElement) {
    			this.collectElements();
    		}
    		
			if (this.url) {
    			var me = this;
				Server.getData(this.url, function(data) {
					Loading.hide();
					
					if (me.onGetData) {
						data = me.onGetData(data);
					}
					me.setData(data);
				});				
			}
			else if (this.data) {
				this.setData(this.data);
			}
		},
		
		createElements: function() {
    		var me = this;
    		
    		this.canvas.addClass("datacard");
    		var body = this.body = $(this.template.join(""));
    		
    		this.columnCount > 1 ? (this.cellCount = this.columnCount * 2) : (this.cellCount = 3);
    		
    		if (this.columnCount > 1) {
				var validateLine = $(this.template_line_validate);
				$("#cell", validateLine).attr("colspan", this.cellCount);
				this.body.append(validateLine);   	
    		}
    		
    		var tableLine = null;
    		for (var i = 0; i < this.columns.length; i++) {
    			var column = this.columns[i];
    			column.index = i;
    			column.span = column.span || 1;
    			tableLine = this.createOneColumn(tableLine, column);
    		}
    		
    		if (this.columnPosition > 0 && this.columnPosition < this.columnCount) {
    			for (var i = this.columnPosition; i <= this.columnCount; i++) {
    				var column = {span: 1};
    				tableLine = this.createOneColumn(tableLine, column);
    			}
    		}
    		
    		body.Validform({
    			tiptype: this.tiptype
    		});
    		
    		this.canvas.append(body);
		},
		
		createOneColumn: function(tableLine, column) {
			var me = this;
			
			//1. create title
			if (column.group) {
				var title = $(this.template_title.join(""));
				$("#lbl_title", title).html(column.group);
				$("#title_body", title).attr("colspan", this.cellCount);
				
				this.body.append(title);
				this.columnPosition = 0;
			}
			
			//2. create row
			if ((this.columnPosition == 0) || (this.columnPosition >= this.columnCount)) {
				tableLine = this.currentLine = $(this.template_line);
				tableLine.cells = [];
				
				this.body.append(tableLine);
				this.columnPosition = 0;
			}
			
			//2. create column
			var cell = {};
			tableLine.cells.push(cell);
			
			//2.1
			cell.key = $(this.template_key);
			if (column.caption) {
				cell.key.html(column.caption);
			}
			tableLine.append(cell.key);
			
			//2.2
			var content = $(this.template_value.join(""));
			cell.value = $("#value", content);
			cell.validator = $("#validator", content);
			cell.validator.click(function() {
				var el = $(this);
				var message = $(".Validform_checktip", el);
				if(message.html() != null && message.html() != "") {
					alert(message.html());
				}
			});
			
			if (column.span > 1) {
				content.attr("colspan", column.span * 2 - 1);
			}
			this.columnPosition = this.columnPosition + column.span;
			
			if (column.field) {
				var editor = ControlCreator.create(column, this);
				editor.onValueChange = function() {
					if (me.onValueChange) {
						me.onValueChange.call(me);
					}
				}
				
				this.editors[column.field] = editor;
			}
			
			editor.appendTo(cell.value);
			tableLine.append(content);
			
			return tableLine;
		},
		
		collectElements: function() {
			var linkElement = this.getEl(this.linkElement);
			if (!linkElement) return;
			
			for (var no in this.editClasss) {
				var editClass = this.editClasss[no];
				var valueEl = $(editClass, linkElement);
				this.linkTo(valueEl, this.editors);
			}
			
			var valueEl = $("select", linkElement);
			this.linkTo(valueEl, this.editors);
			
			var valueEl = $("label", linkElement);
			this.linkTo(valueEl, this.editors);

			var valueEl = $("div", linkElement);
			this.linkTo(valueEl, this.editors);
			
			var valueEl = $("img", linkElement);
			this.linkTo(valueEl, this.editors);			
		},
		
		linkTo: function(valueEl, link) {
			for (var i = 0; i < valueEl.length; i++) {
				var item = $(valueEl.get(i));
				
				var fieldname = item.attr("field");
				if (!fieldname) { fieldname = item.attr("name")}
				
				if (fieldname) {
					fieldname = fieldname.toLowerCase();
					link[fieldname] = item;
				}
			}
		},
		
		setData: function(line) {
			var dataObject = this.dataObject;
			
			dataObject.record = line;
			dataObject.old = $.extend({}, line);
			dataObject.changed = false;
			
		//	console.log(JSON.stringify(line));
			
			for (var prop in line) {
				var editor = this.editors[prop];
				
				if (editor) {
					editor.setValue ? editor.setValue(line[prop]) : editor.val(line[prop]);
				}
			}
		},
		
		setValue: function(field, value) {
			var dataObject = this.dataObject;
			
			var oldValue = dataObject.getValue(field);
			if (oldValue == value) {
				return;
			}
			
			dataObject.setValue(field, value);
			
			var editor = this.editors[field];
			if (editor) {
				editor.setValue ? editor.setValue(value) : editor.val(value);
			}
			
			if (this.onValueChange) {
				this.onValueChange();
			}
		},		
		
		getData: function() {
			var record = this.dataObject.record, old = this.dataObject.old;
			
			for (var prop in this.editors) {
				var editor = this.editors[prop];
				record[prop] = editor.getValue ? editor.getValue() : editor.val();
			}
			
			for (var prop in record) {
				if (old[prop] != record[prop]) {
					this.dataObject.changed = true;
					break;
				}
			}
			
			return this.dataObject;
		},
		
		commit: function() {
			this.dataObject.commit();
		},
		
		getEditByField: function(field) {
			return this.editors[field];
		},
		
		setReadOnly: function(prop, value) {
			if (typeof prop == "string") {
				var editor = this.editors[prop];
				editor.setReadOnly(value);
			}
			else {
				value = prop;
				
				for (var prop in this.editors) {
					var editor = this.editors[prop];
					editor.setReadOnly(value);
				}
			}
		},
		
		notify: function(event) {
			var column = this.columns[event.senderKey];
			if (!column) {
				return;
			}
			
			var reciver = column[event.name];
			if (reciver) {
				reciver(event.newValue, event.record, column, this);
			}
		},
		
		empty: function() {
			this.canvas.empty();
		}
	
	});
	
})(jQuery);