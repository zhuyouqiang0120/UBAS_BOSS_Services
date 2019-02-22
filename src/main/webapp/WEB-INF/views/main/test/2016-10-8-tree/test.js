//官方平滑树
function convert(rows){
    function exists(rows, parentId){
        for(var i=0; i<rows.length; i++){
            if (rows[i].id == parentId) return true;
        }
        return false;
    }

    var nodes = [];
    // get the top level nodes
    for(var i=0; i<rows.length; i++){
        var row = rows[i];
        if (!exists(rows, row.parentId)){
            nodes.push({
                id:row.id,
                text:row.name,
                memo:row.memo
            });
        }
    }

    var toDo = [];
    for(var i=0; i<nodes.length; i++){
        toDo.push(nodes[i]);
    }
    while(toDo.length){
        var node = toDo.shift();	// the parent node
        // get the children nodes
        for(var i=0; i<rows.length; i++){
            var row = rows[i];
            if (row.parentId == node.id){
                var child = {id:row.id,text:row.name,memo:row.memo};
                if (node.children){
                    node.children.push(child);
                } else {
                    node.children = [child];
                }
                toDo.push(child);
            }
        }
    }
    return nodes;
}

$(function () {
    alert(123);
    $('#mytree').tree({
        url: 'tree_data2.json',
        loadFilter: function(rows){
            return convert(rows);
        }
    });
    //点击事件
    $('#mytree').tree({
        onClick: function(node){
            alert(node.text);  // alert node text property when clicked
        }
    });
});
//增加、删除修改事件
var test = function () {
    var selectedNode = $('#mytree').tree('getSelected');
    if(selectedNode != null){
        alert(selectedNode.id);
        try{
            alert(selectedNode.memo);
        }catch(e) {
            alert(e);
        }
    }
};