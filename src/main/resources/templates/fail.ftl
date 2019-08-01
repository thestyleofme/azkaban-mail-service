<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>警告！${profile}数据库${project}任务失败通知</title>
</head>
<body>
<p style="color: red;font-size: large"><b>’你的任务流：${project}执行编号为：${execid} 完成失败！’</b></p>
<br>
开始时间：${startTime?number_to_datetime}
<br>
现在任务流的状态为：${status}
<br>
各任务节点执行详情：<br>
<table>
    <tr>
        <th>节点名称</th>
        <th>开始时间</th>
        <th>结束时间</th>
        <th>执行状态</th>
    </tr>
<#list nodes as node>
    <tr>
        <td>${node.nestedId}</td>
        <td>${node.startTime?number_to_datetime}</td>
        <td>${node.endTime?number_to_datetime}</td>
        <td>${node.status}</td>
    </tr>
</#list>
</table>
<br>
</body>
</html>