(function ($) {
	
	Tree = $.fm.Tree = Control.subClass({
		template: [
	    	'<div class="tree-inner">',
	    		'<ul id="tree-body" class="tree-body">',
	    	'</div>',	
		],
		
		init: function(options) {
			this.checkable = false;
			this.autoExpended = true;
			this.childrenIndex = 0;
			Control.call(this, options);
			
			this.createElements();
			
			if (this.url) {
    			var me = this;
				Server.getData(this.url, function(data) {
					Loading.hide();
					
					if (me.onGetData) {
						data = me.onGetData(data);
					}
					me.loadData(data);
				});				
			}
			else if (this.data) {
				this.loadData(this.data);
			}
		},
		
		createElements: function() {
    		var me = this;
    		
    		this.canvas.addClass("tree");
    		
    		var tree = $(this.template.join(""));
    		this.body = $("#tree-body", tree);
    		
    		this.canvas.append(tree);
		},
		
		loadData: function(array) {
			this.data = this.parseArray(array);
		//	this.parseArray(array)
			this.doLoadData(null, this.data, 0);
		},
		
		parseArray: function(array) {
			var i, l,
	            id = "id",
	            parent = "parentid",
	            children = "children";
	        if (!id || id == "" || !array) return [];
	
	        if ($.isArray(array)) {
	            var r = [];
	            var tmpMap = {};
	            for (i = 0; i < array.length; i++) {
	                tmpMap[array[i][id]] = array[i];
	            }
	            for (i = 0; i < array.length; i++) {
	                if (tmpMap[array[i][parent]] && array[i][id] != array[i][parent]) {
	                    if (!tmpMap[array[i][parent]][children])
	                        tmpMap[array[i][parent]][children] = [];
	                    tmpMap[array[i][parent]][children].push(array[i]);
	                } else {
	                    r.push(array[i]);
	                }
	            }
	            return r;
	        } else {
	            return [array];
	        }
		},
		
		doLoadData: function(parent, array, level) {
        	var node;
        	var cnt = array.length || 1;
        	
        	for (var i = 0; i < cnt; i++) {
        		var line = array[i];
        		
        		node = new $.fm.Tree.Node({
        			tree: this,
        			parent: parent,
        			isLast: i == cnt - 1,
        			isRoot: !parent,
        			isLeaf: !line.children,
        			level: level,
        			expended: line.children && (this.autoExpended || line.expended),
        			record: line
        		});
        		
        		node.isRoot ? this.body.append(node.element) : parent.element.children.append(node.element);
        		
        		if (line.children) {
        			this.doLoadData(node, line.children, level + 1);
        		}
        	}
		},
		
		addNode: function(parent, data) {
			data['tree'] = this;
			data['parent'] = parent; 
			data['isLast'] = true; 
			data['isLeaf'] = (data['isLeaf'] != null) ? data['isLeaf'] : true;
    		
			if (parent) {
				data['level'] = parent.level + 1;
				
				var lastNode = parent.children ? parent.children[parent.children.length - 1] : null;
				if (lastNode) {
					lastNode.setLast(false);
				}
			}
			
    		var node = new $.fm.Tree.Node(data);
    //		node.render();
    		
    		if (parent) {
    			parent.element.children.append(node.element); 
    			parent.expand(); 
    		}
    		else {
    			this.element.ul.append(node.element);
    		}	
    		
    		this.doNodeSelect(node);
		},
		
		doNodeSelect: function(node) {
			if (this.selected) {
				this.selected.unSelect();
			}
			
			node.select();
			this.selected = node;
			
			if (this.onNodeSelect) {
				this.onNodeSelect(node);
			}
		}
	});
	
	
	$.fm.Tree.Node = Control.subClass({
		template_node: [
	    	'<li>',
				'<div id="node-body" class="l-body"></div>',
			'</li>'	
		],
		template_node_box: '<div class="l-box" />',
		template_node_icon: '<div id="icon" class="l-box l-tree-icon"></div>',
		template_node_text: '<label id="text" class="node"></label>',
		template_node_children:	'<ul id="children" class="l-children"></ul>',
				
		init: function(options) {
			this.selected = false;
			this.expended = false;
			this.children = [];
			this.autoSize = false;
			
			Control.call(this, options);
			this.createElements();
		},
		
		createElements: function() {
			var me = this;
			var element = this.element = this.doCreateElements();
			
			if (element.button) {
				element.button.click(function() {
					if (me.expended) {
						me.collapse();
					}
					else {
						me.expand();
					}
				});
			}
			
			/*window.oncontextmenu=function(e){
				//取消默认的浏览器自带右键 很重要！！
				e.preventDefault();
			}*/
			
			element.text.click(function(e) {
				if (!me.selected) {
					me.tree.doNodeSelect(me);
				};
				
				/*if (me.tree.nodeTextClick) {
					me.tree.nodeTextClick(me.record, e);
	        	}*/
				
			});
		},
		
		doCreateElements: function() {
			//1. create element
			var element = $(this.template_node.join(""));
			var body = $("#node-body", element);
			
			if (this.isRoot) {
				element.addClass("l-onlychild");
			}
			else if (this.isLast) {
				element.addClass("l-last");
			}
			
			//2. create box 
			var me = this;
			var parentlevel = this.parentLevel(me);
            for (var i = 0; i <= this.level; i++) {
            	var box = $(this.template_node_box);
            	if (this.isLeaf) {
            		if (i == 0) {
            			if (this.parent) {
            				this.isLast ? box.addClass("l-note-last") : box.addClass("l-note");
            			}
            		}
            		else if ((i < this.level) && (this.parent && !me.parent.isLast)) {
            			if (!parentlevel || i < this.level - parentlevel) {
            				box.addClass("l-line");
            			}
            		}
            		else {
            			me = me.parent;
            			if (me.isLast) {
            				parentlevel = this.parentLevel(me);
            			}
            		}
            		body.prepend(box);
            	}
            	else {
            		if (i == (this.level)) {
            			this.expended ? box.addClass("l-expandable-open") : box.addClass("l-expandable-close");
            			element.button = box;
            		}
            		else if (i && (i <= this.level) && (me.parent && !me.parent.isLast) && me.parent.level > 0) {
            			if (parentlevel && i == parentlevel - 1) {
            				box.addClass("l-line");
            			}
            		}
            		else if (i == 0 && (this.parent && !this.parent.isLast) && this.parent.level > 0) {
            			box.addClass("l-line");
            		}
            		else {
            			me = me.parent;
            			if (!me.isLast) {
            				parentlevel = this.parentLevel(me);
            			}
            		}
            		if (i == this.level) {
            			body.append(box);
            		}
            		else {
            			body.prepend(box);
            		}
            	}
	        }
            
            //3. create icon
        	element.icon = $(this.template_node_icon);
        	if (this.tree.renderNodeIcon) {
        		this.tree.renderNodeIcon(element.icon);
        	}
        	else if (this.isLeaf) {
        		element.icon.addClass("tree-body-icon-leaf");
        	}
        	else {
        		this.expended ? element.icon.addClass("tree-body-icon-folder-open") : element.icon.addClass("tree-body-icon-folder");
        	}
        	body.append(element.icon);
        	
        	//4. create text
        	element.text = $(this.template_node_text);
        	if (this.tree.renderNodeText) {
        		this.tree.renderNodeText(element.text, this.record ? this.record : this);
        	}
        	else {
        		var value = this.name || (this.record ? this.record.name : "empty");
        		element.text.html(value)
        	}
        	body.append(element.text);
        	
            //5. create children
            if (!this.isLeaf) {
            	var children = element.children = $(this.template_node_children);
            	this.expended ? children.show() : children.hide();
            	element.append(children);
            }
        	
        	return element;
		},
		
		parentLevel: function(me) {
			var current = me;
			for (var i = 1; i <= current.level; i++) {
				if (me.parent && me.parent.isLast) {
					return me.parent.level;
				}
				me = me.parent
			}
			return null;
		},
		
		expand: function() {
			this.expended = true;
			
			this.element.button.removeClass("l-expandable-close").addClass("l-expandable-open");
			this.element.icon.removeClass("tree-body-icon-folder").addClass("tree-body-icon-folder-open");
			this.element.children.css({display: 'block'});
            
			if (this.tree.afterExpand) {
				this.tree.afterExpand(this);
			}
		},
		
		collapse: function() {
			this.expended = false;
			
			this.element.button.removeClass("l-expandable-open").addClass("l-expandable-close");
			this.element.icon.removeClass("tree-body-icon-folder-open").addClass("tree-body-icon-folder");
			this.element.children.css({display: 'none'});
            
			if (this.tree.afterCollapse) {
				this.tree.afterCollapse(this);
			}
		},
		
		select: function() {
			this.selected = true;	
			
			this.element.icon.addClass("node-selected");
			this.element.text.addClass("node-selected");
		},
		
		unSelect: function() {
			this.selected = false;	
			
			this.element.icon.removeClass("node-selected");
			this.element.text.removeClass("node-selected");
		},
		
		setLast: function(flag) {
			if (this.isLast == flag) {
				return;
			}
			
			this.isLast = flag;
			
			if (this.element) {
				if (flag) {
					this.element.line.removeClass('l-note');
					this.element.line.addClass('l-note-last');	
					
				}
				else {
					this.element.line.removeClass('l-note-last');	
					this.element.line.addClass('l-note');					
				}
			}
		},
		
		render: function() {
			
		}
		
	});
	
})(jQuery);