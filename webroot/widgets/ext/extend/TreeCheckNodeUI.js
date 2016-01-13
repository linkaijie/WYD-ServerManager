Ext.tree.TreeCheckNodeUI = function() {   
    /**
      *'multiple':多选; 'single':单选; 'cascade':级联多选,包括所有父节点以及子节点; 
      *'cascadechild':级联多选,包括所有子节点; 'child':级联多选,只包括已加载子节点;
      *'cascade1':级联多选特例1,在取消选择时对于不是根节点下第一级子节点并且存在父节点时不作级联操作
      */   
    this.checkModel = 'multiple';   
       
    //false:所有节点都可选; true:只有leaf节点才可选
    this.onlyLeafCheckable = false;   
       
    Ext.tree.TreeCheckNodeUI.superclass.constructor.apply(this, arguments);   
};   
  
Ext.extend(Ext.tree.TreeCheckNodeUI, Ext.tree.TreeNodeUI, {   
  
    renderElements : function(n, a, targetNode, bulkRender){   
        var tree = n.getOwnerTree();   
        this.checkModel = tree.checkModel || this.checkModel;   
        this.onlyLeafCheckable = tree.onlyLeafCheckable || false;   
           
        // add some indent caching, this helps performance when rendering a large tree   
        this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '';   
  
    var cb = (!this.onlyLeafCheckable || a.leaf);   
        var href = a.href ? a.href : Ext.isGecko ? "" : "#";   
        var buf = ['<li class="x-tree-node"><div ext:tree-node-id="',n.id,'" class="x-tree-node-el x-tree-node-leaf x-unselectable ', a.cls,'" unselectable="on">',   
            '<span class="x-tree-node-indent">',this.indentMarkup,"</span>",   
            '<img src="', this.emptyIcon, '" class="x-tree-ec-icon x-tree-elbow" />',   
            '<img src="', a.icon || this.emptyIcon, '" class="x-tree-node-icon',(a.icon ? " x-tree-node-inline-icon" : ""),(a.iconCls ? " "+a.iconCls : ""),'" unselectable="on" />',   
            cb ? ('<input class="x-tree-node-cb" type="checkbox" ' + (a.checked ? 'checked="checked" />' : '/>')) : '',   
            '<a hidefocus="on" class="x-tree-node-anchor" href="',href,'" tabIndex="1" ',   
             a.hrefTarget ? ' target="'+a.hrefTarget+'"' : "", '><span unselectable="on">',n.text,"</span></a></div>",   
            '<ul class="x-tree-node-ct" style="display:none;"></ul>',   
            "</li>"].join('');   
  
        var nel;   
        if(bulkRender !== true && n.nextSibling && (nel = n.nextSibling.ui.getEl())){   
            this.wrap = Ext.DomHelper.insertHtml("beforeBegin", nel, buf);   
        }else{   
            this.wrap = Ext.DomHelper.insertHtml("beforeEnd", targetNode, buf);   
        }   
           
        this.elNode = this.wrap.childNodes[0];   
        this.ctNode = this.wrap.childNodes[1];   
        var cs = this.elNode.childNodes;   
        this.indentNode = cs[0];   
        this.ecNode = cs[1];   
        this.iconNode = cs[2];   
        var index = 3;   
        if(cb){   
            this.checkbox = cs[3];   
            Ext.fly(this.checkbox).on('click', this.check.createDelegate(this,[null]));   
            index++;   
        }   
        this.anchor = cs[index];   
        this.textNode = cs[index].firstChild;   
    },   
       
    // private   
    check : function(checked){   
        var n = this.node;   
        var tree = n.getOwnerTree();   
        this.checkModel = tree.checkModel || this.checkModel;   
           
        if( checked === null ) {   
            checked = this.checkbox.checked;   
        } else {   
            this.checkbox.checked = checked;   
        }   
           
        n.attributes.checked = checked;   
        tree.fireEvent('check', n, checked);   
           
        if(!this.onlyLeafCheckable && this.checkModel == 'cascade'){   
            var parentNode = n.parentNode;   
            if(parentNode !== null) {   
                this.parentCheck(parentNode,checked);   
            }   
            if( !n.expanded && !n.childrenRendered ) {   
                n.expand(false,false,this.childCheck);   
            }   
            else {   
                this.childCheck(n);   
            }   
        }else if(!this.onlyLeafCheckable && this.checkModel == 'cascade1'){ // javaly extend   
            var parentNode = n.parentNode;   
            if(parentNode !== null) {   
                this.parentCheck1(parentNode,checked);   
            }   
            if( !n.expanded && !n.childrenRendered ) {   
                n.expand(false,false,this.childCheck);   
            }   
            else {   
                this.childCheck(n);   
            }   
        }else if(!this.onlyLeafCheckable && this.checkModel == 'cascadechild'){	// javaly extend
            if( !n.expanded && !n.childrenRendered ) {   
                n.expand(false,false,this.childCheck);   
            }   
            else {   
                this.childCheck(n);   
            }   
        }else if(!this.onlyLeafCheckable && this.checkModel == 'child'){ 
                this.childCheck(n);   
        }else if(this.checkModel == 'single'){   
            var checkedNodes = tree.getChecked();   
            for(var i=0;i<checkedNodes.length;i++){   
                var node = checkedNodes[i];   
                if(node.id != n.id){   
                    node.getUI().checkbox.checked = false;   
                    node.attributes.checked = false;   
                    tree.fireEvent('check', node, false);   
                }   
            }   
        }   
           
    },   
       
    // private   
    childCheck : function(node){   
        var a = node.attributes;   
        if(!a.leaf) {   
            var cs = node.childNodes;   
            var csui;   
            for(var i = 0; i < cs.length; i++) {   
                csui = cs[i].getUI();   
                if(csui.checkbox.checked ^ a.checked)   
                    csui.check(a.checked);   
            }   
        }   
    },   
       
    // private   
    parentCheck : function(node ,checked){   
        var checkbox = node.getUI().checkbox;   
        if(typeof checkbox == 'undefined')return ;   
        if(!(checked ^ checkbox.checked))return;   
        if(!checked && this.childHasChecked(node))return;  
        checkbox.checked = checked;   
        node.attributes.checked = checked;   
        node.getOwnerTree().fireEvent('check', node, checked);   
           
        var parentNode = node.parentNode;   
        if( parentNode !== null){   
            this.parentCheck(parentNode,checked);   
        }   
    },   
       
    // private   javaly extend   
    parentCheck1 : function(node ,checked){   
        var checkbox = node.getUI().checkbox;   
        if(typeof checkbox == 'undefined')return ;   
        if(!(checked ^ checkbox.checked))return;   
        if(!checked && this.childHasChecked(node))return;  
        if(!checked && node.parentNode != null && node.parentNode.parentNode != null)return; // 关键     
        checkbox.checked = checked;   
        node.attributes.checked = checked;   
        node.getOwnerTree().fireEvent('check', node, checked);   
           
        var parentNode = node.parentNode;   
        if( parentNode !== null){   
            this.parentCheck1(parentNode,checked);   
        }   
    },   
       
    // private   
    childHasChecked : function(node){   
        var childNodes = node.childNodes;   
        if(childNodes || childNodes.length>0){   
            for(var i=0;i<childNodes.length;i++){   
                if(childNodes[i].getUI().checkbox.checked)   
                    return true;   
            }   
        }   
        return false;   
    },   
       
    toggleCheck : function(value){   
        var cb = this.checkbox;   
        if(cb){   
            var checked = (value === undefined ? !cb.checked : value);   
            this.check(checked);   
        }   
    }   
});  