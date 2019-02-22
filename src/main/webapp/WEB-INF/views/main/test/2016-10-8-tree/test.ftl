<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <script type="text/javascript" src="${PATH}/WEB-INF/views/main/test/jqueryeasyui/jquery-2.2.3.js"></script>
    <script type="text/javascript" src="${PATH}/WEB-INF/views/main/test/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${PATH}/WEB-INF/views/main/test/jqueryeasyui/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${PATH}/WEB-INF/views/main/test/2016-10-8-tree/test.js"></script>

    <link rel="stylesheet" type="text/css" href="${PATH}/WEB-INF/views/main/test/jqueryeasyui/themes/default/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="${PATH}/WEB-INF/views/main/test/jqueryeasyui/themes/icon.css"/>
    <title>123</title>
</head>
<body>
<h1>Json</h1>
<div class="easyui-panel" style="padding:5px">
    <ul class="easyui-tree" data-options="url:'${PATH}/WEB-INF/views/main/test/2016-10-8-tree/tree_data1.json',method:'get',animate:true,lines:true"></ul>
</div>
<hr/>
<h1>Tree1</h1>
<div class="easyui-panel" style="padding:5px">
    <ul id="mytree" class="easyui-tree" data-options="animate:true,lines:true"></ul>
</div>
<div><input type="button" value="测试树" onclick="test();"></div>

</body>
</html>